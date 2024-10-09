package com.softserve.itacademy.todolist.dto;

import com.softserve.itacademy.todolist.model.State;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StateResponse {
    private long id;
    private String state;

    public static StateResponse toStateResponse(State state) {
         return new StateResponse(state.getId(), state.getName());
    }
}
