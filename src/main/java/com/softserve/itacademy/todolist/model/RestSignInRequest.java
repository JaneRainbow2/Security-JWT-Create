package com.softserve.itacademy.todolist.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RestSignInRequest {
    private String username;
    private String password;
}
