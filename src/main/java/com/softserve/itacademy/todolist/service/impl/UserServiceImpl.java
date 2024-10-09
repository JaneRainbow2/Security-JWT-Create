package com.softserve.itacademy.todolist.service.impl;

import com.softserve.itacademy.todolist.dto.ToDoResponseDto;
import com.softserve.itacademy.todolist.dto.UserRequestDto;
import com.softserve.itacademy.todolist.dto.UserResponseDto;
import com.softserve.itacademy.todolist.exception.EntityNotFoundException;
import com.softserve.itacademy.todolist.exception.NullEntityReferenceException;
import com.softserve.itacademy.todolist.model.Role;
import com.softserve.itacademy.todolist.model.User;
import com.softserve.itacademy.todolist.repository.UserRepository;
import com.softserve.itacademy.todolist.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;

    @Override
    public User create(User role) {
        if (role != null) {
            return userRepository.save(role);
        }
        throw new NullEntityReferenceException("User cannot be 'null'");
    }

    @Override
    public User readById(long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User with id " + id + " not found"));
    }

    @Override
    public User readByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return user;
        }
        throw new EntityNotFoundException(String.format("User with email %s not found", email));
    }

    @Override
    public User update(User role) {
        if (role != null) {
            readById(role.getId());
            return userRepository.save(role);
        }
        throw new NullEntityReferenceException("User cannot be 'null'");
    }

    @Override
    public User update(long id, UserRequestDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole(new Role(dto.getRole()));
        return userRepository.save(user);
    }

    @Override
    public void delete(long id) {
        User user = readById(id);
        userRepository.delete(user);
    }

    public List<UserResponseDto> getAllUserDtos() {
        return userRepository.findAll().stream()
                .map(UserResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<ToDoResponseDto> getToDoDtosByUserId(Long userId) {
        User user = readById(userId);  // Assuming readById is already implemented
        return user.getMyTodos().stream()
                .map(ToDoResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.of(userRepository.findByEmail(username));
    }


}