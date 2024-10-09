package com.softserve.itacademy.todolist.service;

import com.softserve.itacademy.todolist.dto.TaskResponseDto;
import com.softserve.itacademy.todolist.dto.ToDoResponseDto;
import com.softserve.itacademy.todolist.dto.UserRequestDto;
import com.softserve.itacademy.todolist.dto.UserResponseDto;
import com.softserve.itacademy.todolist.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService{
    User create(User user);
    User readById(long id);
    User readByEmail(String email);
    User update(User user);
    User update(long id, UserRequestDto dto);
    void delete(long id);
    List<UserResponseDto> getAllUserDtos();
    List<ToDoResponseDto> getToDoDtosByUserId(Long userId);
    List<User> getAll();
    Optional<User> findByUsername(String username);
}
