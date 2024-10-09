package com.softserve.itacademy.todolist.controller;

import com.softserve.itacademy.todolist.dto.*;
import com.softserve.itacademy.todolist.model.User;
import com.softserve.itacademy.todolist.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto dto) {
        User user = new User(dto.getFirstName(), dto.getLastName(), dto.getEmail(), dto.getPassword(), dto.getRole());
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserResponseDto(userService.create(user)));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable long id) {
        return ResponseEntity.ok(new UserResponseDto(userService.readById(id)));
    }

    @PutMapping("/{id}/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable long id, @RequestBody UserRequestDto dto) {
        User updatedUser = userService.update(id, dto);
        return ResponseEntity.ok(new UserResponseDto(updatedUser));
    }

    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUserDtos();
    }

    @GetMapping("/{id}/todos")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or @todoSecurityService.isOwner(authentication.principal.id, #id)")
    public List<ToDoResponseDto> getAllToDo(@PathVariable long id) {
        return userService.getToDoDtosByUserId(id);
    }
}