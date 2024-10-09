package com.softserve.itacademy.todolist.service;

import com.softserve.itacademy.todolist.dto.TaskRequestDto;
import com.softserve.itacademy.todolist.dto.TaskResponseDto;
import com.softserve.itacademy.todolist.model.Task;

import java.util.List;

public interface TaskService {
    Task create(Task task);
    TaskResponseDto createTask(long todoId, TaskRequestDto dto);
    Task readById(long id);
    Task update(Task task);
    void delete(long id);
    List<Task> getAll();
    List<TaskResponseDto> getAllTasks();
    List<TaskResponseDto> getTasksByTodoId(long todoId);
    List<TaskResponseDto> getTasks(Long todoId, Long userId);

    List<Task> getByTodoId(long todoId);
}
