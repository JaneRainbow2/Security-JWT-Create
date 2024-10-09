package com.softserve.itacademy.todolist.controller;

import com.softserve.itacademy.todolist.dto.StateResponse;
import com.softserve.itacademy.todolist.model.State;
import com.softserve.itacademy.todolist.repository.StateRepository;
import com.softserve.itacademy.todolist.service.StateService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/states")
@PreAuthorize("hasRole('ADMIN')")
@AllArgsConstructor
public class StateController {

    private final StateService stateService;
    private final StateRepository stateRepository;

    @GetMapping("/getAll")
    public List<StateResponse> getAllStates() {
        return stateRepository.findByOrderByIdAsc().stream().map(StateResponse::toStateResponse).toList();
    }
    @PostMapping("/add")
    public ResponseEntity<String> addState(@RequestBody State state) {
        stateService.create(state);
        return new ResponseEntity<>("Successfully added new state!", HttpStatus.CREATED);
    }
}
