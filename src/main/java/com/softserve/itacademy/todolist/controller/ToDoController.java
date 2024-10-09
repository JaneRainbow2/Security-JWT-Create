package com.softserve.itacademy.todolist.controller;

import com.softserve.itacademy.todolist.dto.TaskResponseDto;
import com.softserve.itacademy.todolist.dto.ToDoRequestDto;
import com.softserve.itacademy.todolist.dto.ToDoResponseDto;
import com.softserve.itacademy.todolist.dto.UserResponseDto;
import com.softserve.itacademy.todolist.service.TaskService;
import com.softserve.itacademy.todolist.service.ToDoService;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
@AllArgsConstructor
public class ToDoController {

    private final ToDoService todoService;
    private final TaskService taskService;


    @PostMapping("/todos/create/users/{owner_id}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ToDoResponseDto> create(@PathVariable("owner_id") Long ownerId,
                                                  @RequestBody ToDoRequestDto toDoRequestDto) {
        log.info("[POST] Request to create todo");
        ToDoResponseDto response = todoService.createToDo(ownerId, toDoRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/todos/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or @todoSecurityService.isOwner(authentication.principal.id, #id)")
    public ToDoResponseDto read(@PathVariable Long id) {
        log.info("[GET] Request to read todo with ID: {}", id);
        return new ToDoResponseDto(todoService.readById(id));
    }

    @PatchMapping("/todos/{id}/update")
    @PreAuthorize("hasRole('ADMIN') or @todoSecurityService.isOwner(authentication.principal.id, #id)")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ToDoResponseDto> update(@PathVariable Long id,
                                                  @RequestBody ToDoRequestDto toDoRequestDto) {
        log.info("[PATCH] Request to update todo with ID: {}", id);
        ToDoResponseDto response = todoService.updateToDo(id, toDoRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/todos/{id}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or @todoSecurityService.isOwner(authentication.principal.id, #id)")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        log.info("[DELETE] Request to delete todo with ID: {}", id);
        todoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/todos")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public List<ToDoResponseDto> getAll() {
        log.info("[GET] Request to read all todos");
        return todoService.getAllTodos();
    }

    @GetMapping("/todos/{todo_id}/tasks")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or @todoSecurityService.isOwner(authentication.principal.id, #todoId)")
    public List<TaskResponseDto> readTasks(@PathVariable("todo_id") Long todoId) {
        log.info("[GET] Request to read tasks for todo with ID: {}", todoId);
        return taskService.getTasksByTodoId(todoId);
    }

    @GetMapping("/todos/{todo_id}/collaborators")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or @todoSecurityService.isOwner(authentication.principal.id, #todoId)")
    public List<UserResponseDto> getAllCollaborators(@PathVariable("todo_id") Long todoId) {
        log.info("[GET] Request to read collaborators for todo with ID: {}", todoId);
        return todoService.getCollaboratorsForToDo(todoId);
    }

    @GetMapping("/users/{user_id}/todos/{todo_id}/collaborators")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or @todoSecurityService.isOwner(authentication.principal.id, #todoId)")
    public List<UserResponseDto> getAllCollaborator(@PathVariable("user_id") Long userId,
                                                    @PathVariable("todo_id") Long todoId) {
        log.info("[GET] Request to read collaborators for todo with ID: {} and user with ID: {}", todoId, userId);
        return todoService.getCollaborators(todoId, userId);
    }

    @PostMapping("/todos/{todo_id}/users/{user_id}/add")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN') or @todoSecurityService.isOwner(authentication.principal.id, #todoId)")
    public ResponseEntity<Void> addCollaborator(@PathVariable("todo_id") Long todoId,
                                                @PathVariable("user_id") Long userId,
                                                Principal principal) {
        log.info("[POST] Request to add collaborator to todo with ID: {}", todoId);
        todoService.addCollaborator(todoId, userId, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/todos/{todo_id}/users/{user_id}/remove")
    @PreAuthorize("hasRole('ADMIN') or @todoSecurityService.isOwner(authentication.principal.id, #todoId)")
    public ResponseEntity<Void> removeCollaborator(@PathVariable("todo_id") Long todoId,
                                                   @PathVariable("user_id") Long userId,
                                                   Principal principal) {
        log.info("[DELETE] Request to remove collaborator from todo with ID: {}", todoId);
        todoService.removeCollaborator(todoId, userId, principal.getName());
        return ResponseEntity.noContent().build();
    }
}