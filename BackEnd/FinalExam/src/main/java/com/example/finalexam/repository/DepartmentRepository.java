package com.example.finalexam.repository;

import com.example.finalexam.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;

@Repository
public interface DepartmentRepository
        extends JpaRepository<Department, Integer>, JpaSpecificationExecutor<Department> {

    public boolean existsDepartmentByName(String name);


}
