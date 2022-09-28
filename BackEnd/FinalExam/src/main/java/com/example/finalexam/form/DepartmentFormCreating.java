package com.example.finalexam.form;

import com.example.finalexam.entity.CommonEntity;
import com.example.finalexam.entity.Department;
import com.example.finalexam.entity.dto.DepartmentDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Where(clause = "is_deleted = 0")
public class DepartmentFormCreating extends CommonEntity {

    private String name;


}
