package com.softserve.itacademy.todolist.security;

import com.softserve.itacademy.todolist.model.ToDo;
import com.softserve.itacademy.todolist.service.impl.ToDoServiceImpl;
import org.springframework.stereotype.Component;

@Component
public class TodoSecurityService {

    private final ToDoServiceImpl todoService;

    public TodoSecurityService(ToDoServiceImpl todoService) {
        this.todoService = todoService;
    }

    public boolean isOwner(Long userId, Long todoId) {
        ToDo todo = todoService.readById(todoId);
        return todo.getOwner().getId().equals(userId);
    }
}