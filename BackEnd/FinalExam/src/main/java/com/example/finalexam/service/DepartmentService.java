package com.example.finalexam.service;


import com.example.finalexam.entity.Account;
import com.example.finalexam.entity.Department;
import com.example.finalexam.entity.dto.DepartmentDTO;
import com.example.finalexam.form.DepartmentFilterForm;
import com.example.finalexam.form.DepartmentFormCreating;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {
    List<Department> getAll();

    Page<?> getAllPage(Pageable pageable, String search, DepartmentFilterForm filterForm);
    Optional<Department> getOne(Integer id);

    Department createform(DepartmentFormCreating formCreating);
    Department create(Department department);

    DepartmentDTO update(Integer id, Department account) throws NotFoundException;

    Department delete(Integer id);

    Optional<Department> getOneReturnDTO(Integer id);

    boolean isDepartmentExistsByName(String name);
}
