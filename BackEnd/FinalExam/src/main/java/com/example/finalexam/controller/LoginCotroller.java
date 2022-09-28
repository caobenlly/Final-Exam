//package com.example.finalexam.controller;
//
//
//import com.example.finalexam.entity.Account;
//import com.example.finalexam.entity.dto.LoginDTO;
//import com.example.finalexam.service.AccountService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.security.Principal;
//
//@RestController
//@RequestMapping("api/v1/login")
//@CrossOrigin("*")
//@Validated
//public class LoginCotroller {
//
//
//    private final AccountService accountService;
//
//    public LoginCotroller(AccountService accountService) {
//        this.accountService = accountService;
//    }
//
//    @GetMapping()
//    public ResponseEntity<?> login(Principal principal){
//        String username = principal.getName();
//
//        Account entity =  accountService.getAccountByUsername(username);
//
//        LoginDTO dto = new LoginDTO(entity.getId(),entity.getLastName(),entity.getRole());
//       return ResponseEntity
//                .ok()
//                .body(dto);
//    }
//}
