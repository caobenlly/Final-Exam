package com.example.finalexam.entity;

import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "account")
@Data
public class Account extends CommonEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String username;

    @Column(name = "password", length = 800)
    private String password;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name",nullable = false, length = 50)
    private String lastName;

    @Column(name = "role", nullable = false)
    private String role;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;


    @Column(name = "is_deleted" )
    @Enumerated(EnumType.ORDINAL)
    private Delete isDeleted;
    public Account id(Integer id) {
        this.id = id;
        return this;
    }

    public Account username(String username) {
        this.username = username;
        return this;
    }

    public Account password(String password) {
        this.password = password;
        return this;
    }

    public Account firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public Account lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }


    public Account department(Department department) {
        this.department = department;
        return this;
    }

    public Account role(String role) {
        this.role = role;
        return this;
    }
}
