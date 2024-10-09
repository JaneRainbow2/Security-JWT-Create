package com.softserve.itacademy.todolist.service.impl;

import com.softserve.itacademy.todolist.dto.ToDoRequestDto;
import com.softserve.itacademy.todolist.dto.ToDoResponseDto;
import com.softserve.itacademy.todolist.dto.UserResponseDto;
import com.softserve.itacademy.todolist.exception.NullEntityReferenceException;
import com.softserve.itacademy.todolist.model.ToDo;
import com.softserve.itacademy.todolist.model.User;
import com.softserve.itacademy.todolist.repository.ToDoRepository;
import com.softserve.itacademy.todolist.service.ToDoService;
import com.softserve.itacademy.todolist.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ToDoServiceImpl implements ToDoService {

    private final ToDoRepository todoRepository;
    private final UserService userService;

    @Override
    public ToDo create(ToDo todo) {
        if (todo != null) {
            return todoRepository.save(todo);
        }
        throw new NullEntityReferenceException("ToDo cannot be 'null'");
    }

    @Override
    public ToDoResponseDto createToDo(Long ownerId, ToDoRequestDto toDoRequestDto) {
        ToDo toDo = new ToDo();
        toDo.setTitle(toDoRequestDto.getTitle());
        toDo.setOwner(userService.readById(ownerId));
        toDo.setCreatedAt(LocalDateTime.now());
        return new ToDoResponseDto(todoRepository.save(toDo));
    }

    @Override
    public ToDo readById(long id) {
        return todoRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("ToDo with id " + id + " not found"));
    }

    @Override
    public ToDo update(ToDo todo) {
        if (todo != null) {
            readById(todo.getId());
            return todoRepository.save(todo);
        }
        throw new NullEntityReferenceException("ToDo cannot be 'null'");
    }

    @Override
    public ToDoResponseDto updateToDo(Long id, ToDoRequestDto toDoRequestDto) {
        ToDo toDo = readById(id);
        toDo.setTitle(toDoRequestDto.getTitle());
        return new ToDoResponseDto(todoRepository.save(toDo));
    }

    @Override
    public void delete(long id) {
        ToDo todo = readById(id);
        todoRepository.delete(todo);
    }

    @Override
    public List<ToDo> getAll() {
        return todoRepository.findAll();
    }

    @Override
    public List<ToDoResponseDto> getAllTodos() {
        return todoRepository.findAll().stream().map(ToDoResponseDto::new).collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDto> getCollaboratorsForToDo(Long todoId) {
        ToDo toDo = readById(todoId);
        return toDo.getCollaborators().stream().map(UserResponseDto::new).collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDto> getCollaborators(Long todoId, Long userId) {
        ToDo todo = readById(todoId);
        if (!todo.getOwner().getId().equals(userId)) {
            throw new RuntimeException("User is not authorized to access collaborators for this ToDo.");
        }
        return todo.getCollaborators()
                .stream()
                .map(UserResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ToDo> getByUserId(long userId) {
        return todoRepository.getByUserId(userId);
    }

    @Override
    public void addCollaborator(Long todoId, Long userId, String principalEmail) {
        ToDo todo = readById(todoId);
        User collaborator = userService.readById(userId);
        User currentUser = userService.readByEmail(principalEmail);

        if (!todo.getOwner().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You do not have permission to add collaborators to this ToDo.");
        }
        if (!todo.getCollaborators().contains(collaborator)) {
            todo.getCollaborators().add(collaborator);
            todoRepository.save(todo);
        } else {
            throw new IllegalArgumentException("User is already a collaborator.");
        }
    }

    @Override
    public void removeCollaborator(Long todoId, Long userId, String principalEmail) {

        ToDo todo = readById(todoId);
        User collaborator = userService.readById(userId);
        User currentUser = userService.readByEmail(principalEmail); // Get the user making the request

        if (!todo.getOwner().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You do not have permission to remove collaborators from this ToDo.");
        }

        if (todo.getCollaborators().contains(collaborator)) {
            todo.getCollaborators().remove(collaborator);
            todoRepository.save(todo);
        } else {
            throw new IllegalArgumentException("User is not a collaborator.");
        }
    }
}
