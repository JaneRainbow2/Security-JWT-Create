package com.softserve.itacademy.todolist.dto;

public class RestAuthRequestDto {
    private String username;
    private String password;

    public RestAuthRequestDto() {
    }

    public RestAuthRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Геттеры и сеттеры
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}