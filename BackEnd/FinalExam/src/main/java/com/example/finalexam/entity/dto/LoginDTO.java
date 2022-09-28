package com.example.finalexam.entity.dto;

import lombok.Data;

@Data
public class LoginDTO {

    private Integer id;

    private String firstName;

    private String lastName;

    private String role;


    public LoginDTO(Integer id, String lastName,String role) {
        this.id = id;
        this.lastName = lastName;
        this.role = role;
    }
}
