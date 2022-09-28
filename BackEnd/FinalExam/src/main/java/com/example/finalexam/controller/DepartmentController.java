package com.example.finalexam.controller;


import com.example.finalexam.form.DepartmentFilterForm;
import com.example.finalexam.form.DepartmentFormCreating;
import com.example.finalexam.service.DepartmentService;
import com.example.finalexam.entity.Department;
import com.example.finalexam.entity.dto.DepartmentDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/departments")
@CrossOrigin("*")
public class DepartmentController {

    private final DepartmentService departmentService;
    private final ModelMapper modelMapper;
    public DepartmentController(DepartmentService departmentService, ModelMapper modelMapper) {
        this.departmentService = departmentService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<DepartmentDTO>> getAll() {
        List<Department> entities =departmentService.getAll();
        List<DepartmentDTO> DepartmentList = modelMapper.map(entities, new TypeToken<List<DepartmentDTO>>(){

        }.getType());
        return ResponseEntity
            .ok()
            .body(DepartmentList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<DepartmentDTO>> getOne(@PathVariable Integer id) {
        Optional<Department> entities = departmentService.getOneReturnDTO(id);
        Optional<DepartmentDTO> departmentDTO = modelMapper.map(entities, new TypeToken<Optional<DepartmentDTO>>(){

        }.getType());
        return ResponseEntity
            .ok()
            .body(departmentDTO);
    }

    @PostMapping
    public ResponseEntity<Department> create(@RequestBody DepartmentFormCreating formCreating) {
        Department responseDepartment = departmentService.createform(formCreating);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(responseDepartment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentDTO> update(
        @PathVariable Integer id,
        @RequestBody Department Department
    )
        throws NotFoundException {
        DepartmentDTO responseDepartment = departmentService.update(id, Department);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(responseDepartment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Department> delete(@PathVariable Integer id){
        Department responseDepartment = departmentService.delete(id);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(responseDepartment);
    }

    @GetMapping("/{name}/exists")
    public ResponseEntity<?> existByName(@PathVariable String name) {

        return ResponseEntity
                .ok()
                .body(departmentService.isDepartmentExistsByName(name));
    }


    @GetMapping("/search")
    public ResponseEntity<Page<?>> getAllPage(
            Pageable pageable,
            @RequestParam(required = false) String search,
            DepartmentFilterForm filterForm
    ) {
        Page<?> entities =departmentService.getAllPage( pageable,search,filterForm);
        Page<DepartmentDTO> DepartmentList = modelMapper.map(entities, new TypeToken<Page<DepartmentDTO>>(){

        }.getType());
        return ResponseEntity
                .ok()
                .body(entities);
    }

    @GetMapping("/exception")
    public void testException() throws  Exception{
        throw  new EntityNotFoundException("Excrption Information");
    }
}
