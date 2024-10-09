package com.softserve.itacademy.todolist.service.impl;

import com.softserve.itacademy.todolist.dto.TaskRequestDto;
import com.softserve.itacademy.todolist.dto.TaskResponseDto;
import com.softserve.itacademy.todolist.exception.NullEntityReferenceException;
import com.softserve.itacademy.todolist.model.Priority;
import com.softserve.itacademy.todolist.model.Task;
import com.softserve.itacademy.todolist.model.ToDo;
import com.softserve.itacademy.todolist.repository.TaskRepository;
import com.softserve.itacademy.todolist.service.StateService;
import com.softserve.itacademy.todolist.service.TaskService;
import com.softserve.itacademy.todolist.service.ToDoService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    private final TaskRepository taskRepository;
    private final ToDoService todoService;
    private final StateService stateService;

    @Override
    public Task create(Task task) {
        if (task != null) {
            return taskRepository.save(task);
        }
        throw new NullEntityReferenceException("Task cannot be 'null'");
    }

    @Override
    public TaskResponseDto createTask(long todoId, TaskRequestDto dto) {
        Task task = mapToEntity(dto);
        task.setTodo(todoService.readById(todoId));
        task.setState(stateService.readById(1L));
        Task savedTask = taskRepository.save(task);
        return mapToResponseDto(savedTask);
    }

    @Override
    public Task readById(long id) {

        EntityNotFoundException exception = new EntityNotFoundException("Task with id " + id + " not found");
        logger.error(exception.getMessage(), exception);

        return taskRepository.findById(id).orElseThrow(
                () -> exception);
    }

    @Override
    public Task update(Task task) {
        if (task != null) {
            readById(task.getId());
            return taskRepository.save(task);
        }
        throw new NullEntityReferenceException("Task cannot be 'null'");
    }

    @Override
    public void delete(long id) {
        Task task = readById(id);
        taskRepository.delete(task);
    }

    @Override
    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    @Override
    public List<TaskResponseDto> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponseDto> getTasksByTodoId(long todoId) {
        return taskRepository.getByTodoId(todoId).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getByTodoId(long todoId) {
        return taskRepository.getByTodoId(todoId);
    }

    private Task mapToEntity(TaskRequestDto dto) {
        Task task = new Task();
        task.setName(dto.getName());
        task.setPriority(Priority.valueOf(dto.getPriority()));
        return task;
    }

    private TaskResponseDto mapToResponseDto(Task task) {
        return new TaskResponseDto(task);
    }

    @Override
    public List<TaskResponseDto> getTasks(Long todoId, Long userId) {
        ToDo todo = todoService.readById(todoId);
        if (!todo.getOwner().getId().equals(userId)) {
            throw new RuntimeException("User is not authorized to access tasks for this ToDo.");
        }
        return taskRepository.getByTodoId(todoId)
                .stream()
                .map(TaskResponseDto::new)
                .collect(Collectors.toList());
    }
}
