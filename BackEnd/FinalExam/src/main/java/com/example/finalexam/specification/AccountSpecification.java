package com.example.finalexam.specification;

import com.example.finalexam.entity.Account;
import com.example.finalexam.entity.Department;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class AccountSpecification implements Specification<Account> {

    private  String field;
    private  String operator;
    private  Object value;

    public AccountSpecification(String field, String operator, Object value) {
        this.field = field;
        this.operator = operator;
        this.value = value;
    }
    @Override
    public Predicate toPredicate(Root<Account> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if(operator.equalsIgnoreCase("LIKE")){
            return criteriaBuilder.like(root.get(field),"%"+value.toString()+"%");
        }

        if(operator.equalsIgnoreCase(">=")){
            return criteriaBuilder.greaterThanOrEqualTo(root.get(field),value.toString());
        }

        if(operator.equalsIgnoreCase("<=")){
            return criteriaBuilder.lessThanOrEqualTo(root.get(field),value.toString());
        }
        return null;
    }
}
