package com.example.finalexam.form;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@Data
public class DepartmentFilterForm {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date minDate;
    private Date maxDate;


}
