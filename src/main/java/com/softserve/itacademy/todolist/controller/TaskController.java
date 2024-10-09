package com.softserve.itacademy.todolist.controller;

import com.softserve.itacademy.todolist.dto.*;
import com.softserve.itacademy.todolist.service.TaskService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/api")
public class TaskController {
    private TaskService taskService;

    @PostMapping("/tasks/{todo_id}/create")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN') or @todoSecurityService.isOwner(authentication.principal.id, #todo_id)")
    public ResponseEntity<TaskResponseDto> createTask(@PathVariable long todo_id,
                                                      @RequestBody TaskRequestDto taskRequestDto) {
        log.info("[POST] Request to create task for ToDo ID: {}", todo_id);
        TaskResponseDto taskResponse = taskService.createTask(todo_id, taskRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskResponse);
    }

    @GetMapping("/tasks/{task_id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public TaskResponseDto read(@PathVariable long task_id) {
        log.info("[GET] Request to read task ID: {}", task_id);
        return new TaskResponseDto(taskService.readById(task_id));
    }

    @DeleteMapping("/tasks/{task_id}/todos/{todo_id}/delete")
    @PreAuthorize("hasRole('ADMIN') or @todoSecurityService.isOwner(authentication.principal.id, #todoId)")
    public ResponseEntity<Void> delete(@PathVariable long todoId, @PathVariable long task_id) {
        log.info("[DELETE] Request to delete task ID: {}", task_id);
        taskService.delete(task_id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tasks")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN' or hasRole('USER'))")
    public List<TaskResponseDto> getAll() {
        log.info("[GET] Request to read all tasks");
        return taskService.getAllTasks();
    }

    @GetMapping("/tasks/todos/{todo_id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or @todoSecurityService.isOwner(authentication.principal.id, #todo_id)")
    public List<TaskResponseDto> getAllTodoTasks(@PathVariable long todo_id) {
        log.info("[GET] Request to read all tasks for ToDo ID: {}", todo_id);
        return taskService.getTasksByTodoId(todo_id);
    }

    @GetMapping("/users/{user_id}/todos/{todo_id}/tasks")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or @todoSecurityService.isOwner(authentication.principal.id, #todoId)")
    public List<TaskResponseDto> readTasks(@PathVariable("user_id") Long userId, @PathVariable("todo_id") Long todoId) {
        log.info("[GET] Request to read tasks for todo with ID: {} and user with ID: {}", todoId, userId);
        return taskService.getTasks(todoId, userId);
    }
}