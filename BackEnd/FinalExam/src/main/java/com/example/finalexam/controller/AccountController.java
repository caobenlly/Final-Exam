package com.example.finalexam.controller;

import com.example.finalexam.entity.dto.AccountCreateDTO;
import com.example.finalexam.entity.dto.AccountDTO;
import com.example.finalexam.entity.dto.AccountUpdateDTO;
import com.example.finalexam.entity.dto.DepartmentDTO;
import com.example.finalexam.form.AccountsFilterForm;
import com.example.finalexam.form.DepartmentFilterForm;
import com.example.finalexam.service.AccountService;
import com.example.finalexam.entity.Account;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("api/v1/accounts")
@CrossOrigin("*")
@Validated
public class AccountController {

    private final AccountService accountService;

    private final ModelMapper modelMapper;


    public AccountController(AccountService accountService, ModelMapper modelMapper) {
        this.accountService = accountService;
        this.modelMapper = modelMapper;
    }


    @GetMapping()
    public ResponseEntity<List<AccountDTO>> getAll() {
        List<Account> accountList = accountService.getAll();
        List<AccountDTO> accountDTOList = modelMapper.map(accountList, new TypeToken<List<AccountDTO>>(){

        }.getType());
        return ResponseEntity
                .ok()
                .body(accountDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<AccountDTO>> getOne(@PathVariable Integer id) {
        Optional <Account> account = accountService.getOne(id);
        Optional<AccountDTO> accountDTO = modelMapper.map(account, new TypeToken<Optional<AccountDTO>>(){

        }.getType());;
        return ResponseEntity
                .ok()
                .body(accountDTO);
    }

    @PostMapping("/create")
    public ResponseEntity<Account> create(@RequestBody AccountCreateDTO dto) {
        Account requestAccount = accountService.create(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(requestAccount);
    }

    @PostMapping("/update")
    public ResponseEntity<Account> update(
            @RequestBody AccountUpdateDTO accountUpdateDTO) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountService.update(accountUpdateDTO));
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<Account> delete (@PathVariable Integer id)  {
        Account  responseAccount = accountService.delete(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseAccount);
    }

    @PostMapping("/search")
    public ResponseEntity<Page<?>> getAllPage(@RequestBody AccountsFilterForm filterForm) {
        Page<?> entities =accountService.getAllPage(filterForm);
        Page<DepartmentDTO> DepartmentList = modelMapper.map(entities, new TypeToken<Page<DepartmentDTO>>(){

        }.getType());
        return ResponseEntity
                .ok()
                .body(entities);
    }
}
