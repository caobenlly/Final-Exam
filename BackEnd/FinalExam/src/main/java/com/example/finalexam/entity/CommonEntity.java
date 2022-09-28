package com.example.finalexam.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;

@Data
@MappedSuperclass
public class CommonEntity {

//    @Column(name = "is_deleted" )
//    private Integer isDeleted;
//
//    @PrePersist
//    public void prePersist() {
//        this.isDeleted = 0;
//    }
//
//    public CommonEntity isDeleted(Integer isDeleted) {
//        this.isDeleted = isDeleted;
//        return this;
//    }
}
