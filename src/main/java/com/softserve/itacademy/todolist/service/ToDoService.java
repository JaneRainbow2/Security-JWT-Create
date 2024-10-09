package com.softserve.itacademy.todolist.service;

import com.softserve.itacademy.todolist.dto.TaskResponseDto;
import com.softserve.itacademy.todolist.dto.ToDoRequestDto;
import com.softserve.itacademy.todolist.dto.ToDoResponseDto;
import com.softserve.itacademy.todolist.dto.UserResponseDto;
import com.softserve.itacademy.todolist.model.ToDo;

import java.util.List;

public interface ToDoService {
    ToDo create(ToDo todo);

    ToDoResponseDto createToDo(Long ownerId, ToDoRequestDto toDoRequestDto);

    ToDo readById(long id);

    ToDo update(ToDo todo);

    ToDoResponseDto updateToDo(Long id, ToDoRequestDto toDoRequestDto);

    void delete(long id);

    List<ToDo> getAll();

    List<ToDoResponseDto> getAllTodos();

    List<UserResponseDto> getCollaboratorsForToDo(Long todoId);

    List<UserResponseDto> getCollaborators(Long todoId, Long userId);

    List<ToDo> getByUserId(long userId);

    void addCollaborator(Long todoId, Long userId, String principalEmail);

    void removeCollaborator(Long todoId, Long userId, String principalEmail);
}
