package com.example.finalexam.form;

import lombok.Data;

@Data
public class AccountsFilterForm {
    private int page;
    private int size;
    private String sortField;
    private String sortType;

    private String role;
    private String search;
    private String departmentName;
}
