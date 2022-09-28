package com.example.finalexam.service;

import com.example.finalexam.config.exceptions.AppException;
import com.example.finalexam.config.exceptions.ErrorResponseBase;
import com.example.finalexam.entity.Account;
import com.example.finalexam.entity.Delete;
import com.example.finalexam.entity.Department;
import com.example.finalexam.entity.dto.AccountCreateDTO;
import com.example.finalexam.entity.dto.AccountDTO;
import com.example.finalexam.entity.dto.AccountUpdateDTO;
import com.example.finalexam.form.AccountsFilterForm;
import com.example.finalexam.repository.AccountRepository;
import com.example.finalexam.repository.DepartmentRepository;
import com.example.finalexam.repository.specification.AccountSpecification;
import com.example.finalexam.sercurity.model.SysUserDetails;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;
    private final DepartmentRepository departmentRepo;

    public AccountServiceImpl(AccountRepository accountRepository, ModelMapper modelMapper, DepartmentRepository departmentRepo) {
        this.accountRepository = accountRepository;
        this.modelMapper = modelMapper;
        this.departmentRepo = departmentRepo;
    }


    @Override
    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    @Override
    public Optional<Account> getOne(Integer id) {
        Optional<Account> optional = accountRepository.findById(id);
        if (optional.isEmpty()) {
            throw new AppException(ErrorResponseBase.NOT_EXISTED_ACCOUNT);
        }
        return accountRepository.findById(id);
    }

    @Override
    public Account create(AccountCreateDTO dto) {

        Optional<Account> optional = accountRepository.findByUsername(dto.getUsername());
        if (optional.isPresent()) {
            throw new AppException(ErrorResponseBase.IS_EXISTED_USERNAME);
        }
        Optional<Department> optionalDepartment = departmentRepo.findById(dto.getDepartmentId());
        if (optionalDepartment.isEmpty()){
            throw new AppException(ErrorResponseBase.NOT_EXISTED_DEPARTMENT);
        }
        try {
            String password = new BCryptPasswordEncoder().encode("123456");
            Account entity = new Account();
            BeanUtils.copyProperties(dto, entity);
            entity.setPassword(password);
            entity.setDepartment(optionalDepartment.get());
            entity.setIsDeleted(Delete.FALSE);
            return accountRepository.save(entity);
        } catch (Exception e) {
            throw new AppException(e);
        }
    }

    @Override
    public Account update( AccountUpdateDTO accountUpdateDTO) {
        Optional<Account> optional = accountRepository.findById(accountUpdateDTO.getId());

        if (optional.isEmpty()) {
            throw new RuntimeException("acc ko tồn tại");
        }
        Optional<Department> optionalDepartment = departmentRepo.findById(accountUpdateDTO.getDepartmentId());
        if (optionalDepartment.isEmpty()){
            throw new AppException(ErrorResponseBase.NOT_EXISTED_DEPARTMENT);
        }
        Account entity = optional.get();
        entity.setRole(accountUpdateDTO.getRole());
        entity.setDepartment(optionalDepartment.get());
        entity.setFirstName(accountUpdateDTO.getFirstName());
        entity.setLastName(accountUpdateDTO.getLastName());
        return accountRepository.save(entity);
    }

    @Override
    public Account delete(Integer id) {
        Optional<Account> optional = accountRepository.findById(id);

        if (optional.isEmpty()) {
            throw new RuntimeException("acc ko tồn tại");
        }
        Account entity = optional.get();

        entity.setIsDeleted(Delete.TRUE);
        return accountRepository.save(entity);

    }


    @Override
    public Account save(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public SysUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> user = accountRepository.findByUsername(username);
        SysUserDetails sysUserDetails = new SysUserDetails();

        if (user.isEmpty()) {
            throw new BadCredentialsException("User ko tồn tại");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        BeanUtils.copyProperties(user.get(), sysUserDetails);
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.get().getDepartment().getName()));

        sysUserDetails.setAuthorities(authorities);

        return sysUserDetails;
    }

    @Override
    public Account getAccountByUsername(String username) {
        return accountRepository.findByUsername(username).get();
    }

    @Override
    public Page<?> getAllPage(AccountsFilterForm filterForm) {
        PageRequest pageable = null;
        if (filterForm.getSortType().equals("ASC")){
           pageable =  PageRequest.of(filterForm.getPage() - 1,filterForm.getSize(), Sort.by(filterForm.getSortField()).ascending());
        } else {
            pageable =  PageRequest.of(filterForm.getPage() - 1,filterForm.getSize(), Sort.by(filterForm.getSortField()).descending());
        }

//          config trong application.p roperties

        Specification<Account> where = AccountSpecification.buildCondition(filterForm);
            return accountRepository.findAll(where, pageable);
    }
}
