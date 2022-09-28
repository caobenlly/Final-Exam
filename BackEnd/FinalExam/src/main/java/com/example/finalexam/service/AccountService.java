package com.example.finalexam.service;

import com.example.finalexam.entity.Account;
import com.example.finalexam.entity.dto.AccountCreateDTO;
import com.example.finalexam.entity.dto.AccountDTO;
import com.example.finalexam.entity.dto.AccountUpdateDTO;
import com.example.finalexam.form.AccountsFilterForm;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface AccountService extends UserDetailsService {

    List<Account> getAll();
    Optional getOne(Integer id);
    Account create(AccountCreateDTO dto);
    Account update(AccountUpdateDTO accountUpdateDTO);
    Account delete(Integer id);
    Account save(Account account);
    Account getAccountByUsername(String username);
    Page<?> getAllPage(AccountsFilterForm filterForm);
}