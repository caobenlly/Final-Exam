package com.example.finalexam.service;

import com.example.finalexam.entity.Delete;
import com.example.finalexam.entity.Department;
import com.example.finalexam.entity.dto.DepartmentDTO;
import com.example.finalexam.form.DepartmentFilterForm;
import com.example.finalexam.form.DepartmentFormCreating;
import com.example.finalexam.repository.DepartmentRepository;
import com.example.finalexam.specification.DepartmentSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository deptRepo;
    private final ModelMapper modelMapper;


    public DepartmentServiceImpl(
            DepartmentRepository deptRepo,
            ModelMapper modelMapper) {
        this.deptRepo = deptRepo;
        this.modelMapper = modelMapper;

    }

    @Override
    public List<Department> getAll() {

        return deptRepo.findAll();
    }

    @Override
    public Page<?> getAllPage(Pageable pageable, String search, DepartmentFilterForm filterForm) {
//        Pageable pageable =  PageRequest.of(page,size);
//          config trong application.properties

        Specification<Department> where = null;

        if (StringUtils.isEmpty(search)){
            DepartmentSpecification departmentSpecification = new DepartmentSpecification("name","LIKE",search);
            where = Specification.where(departmentSpecification);
        }
        DepartmentSpecification departmentSpecification = new DepartmentSpecification("name", "LIKE", search);

        if (filterForm != null && filterForm.getMinDate() != null) {
            DepartmentSpecification minDateSpecification = new DepartmentSpecification("createDate", ">=", filterForm.getMinDate());
            if (where == null){
                where = Specification.where(minDateSpecification);
            }else {
                where = where.and(minDateSpecification);
            }
        }

        if (filterForm != null && filterForm.getMaxDate() != null) {
            DepartmentSpecification maxDateSpecification = new DepartmentSpecification("createDate", "<=", filterForm.getMaxDate());
            if (where == null){
                where = Specification.where(maxDateSpecification);
            }else {
                where = where.and(maxDateSpecification);
            }
        }


        //dk where
        //return deptRepo.findAll(Specification.where(searchSpecification).and(dk 2 truyền vào);
        return deptRepo.findAll(where, pageable);
    }

    @Override
    public Optional<Department> getOne(Integer id) {
        return deptRepo.findById(id);
    }

    @Override
    public Department createform(DepartmentFormCreating formCreating) {

        //convert form--> entity

        Department department = new Department(formCreating.getName());
        department.setIsDeleted(Delete.FALSE);

        return deptRepo.save(department);
    }
    // check id của account có tồn tại hay không?
    // nếu có -> update dữ liệu của account thành các dữ liệu mới trong accountUpdateDTO
    //           nếu departmentId == null -> set department của account về null
    //           nếu departmentId != null -> update department mới cho account
    // nếu không có account ->  return null;

    @Override
    public Department create(Department department) {

        //convert form--> entity


        return deptRepo.save(department);
    }

    @Override
    public DepartmentDTO update(Integer id, Department departmentUpdateDTO) throws NotFoundException {
//        return getOne(id)
//                .map(department -> modelMapper.map(departmentUpdateDTO, Department.class))
//                .map(this::create)
//                .map(department -> modelMapper.map(department,DepartmentDTO.class))
//                .orElse(null);

        Optional<Department> optional = deptRepo.findById(id);

        if (optional.isEmpty()) {
            throw new RuntimeException("deparrrtment ko tồn tại");
        }
        Department entity = optional.get();
        entity.setTotalMember(departmentUpdateDTO.getTotalMember());
        entity.setName(departmentUpdateDTO.getName());
        deptRepo.save(entity);

        DepartmentDTO response = new DepartmentDTO();
        BeanUtils.copyProperties(entity, response);


        return response;
    }


    @Override
    public Optional<Department> getOneReturnDTO(Integer id) {
        return deptRepo.findById(id);
    }

    @Override
    public Department delete(Integer id) {
        Optional<Department> optional = deptRepo.findById(id);

        if (optional.isEmpty()) {
            throw new RuntimeException("deparrrtment ko tồn tại");
        }
        Department entity = optional.get();

        entity.setIsDeleted(Delete.TRUE);
        return deptRepo.save(entity);
    }

    @Override
    public boolean isDepartmentExistsByName(String name) {
        return deptRepo.existsDepartmentByName(name);
    }
}
