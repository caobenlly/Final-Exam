package com.example.finalexam.entity.dto;

import com.example.finalexam.entity.Account;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data

public class DepartmentDTO
    extends RepresentationModel<DepartmentDTO> {

    private Integer id;

    private String name;

    private LocalDateTime createdDate;




    public DepartmentDTO name(String name) {
        this.name = name;
        return this;
    }

    public DepartmentDTO createdDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }


}
