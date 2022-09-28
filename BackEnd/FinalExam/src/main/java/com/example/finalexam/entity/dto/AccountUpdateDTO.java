package com.example.finalexam.entity.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountUpdateDTO {
    private int id;

    private String username;

    private String firstName;

    private String lastName;

    private String role;

    private Integer departmentId;
}
