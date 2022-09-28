package com.example.finalexam.repository.specification;

import com.example.finalexam.entity.Account;
import com.example.finalexam.entity.Delete;
import com.example.finalexam.entity.Department;
import com.example.finalexam.form.AccountsFilterForm;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Join;

@Data
public class AccountSpecification {
    public static Specification<Account> buildCondition(AccountsFilterForm context){
        return Specification.where(getUsername(context.getSearch()))
                .and(getRole(context.getRole()))
                .or(getFirstName(context.getSearch()))
                .and(getDepartmentName(context.getDepartmentName()))
                .and(isNotDelete());
    }

    public static Specification<Account> getUsername(String search){
        if (!StringUtils.isEmpty(search)){
                    return (root, query, cri) -> {
                        return cri.like(cri.lower(root.get("username")), "%" + search.toLowerCase().trim() + "%" );
                    };
        }
        return null;
    }
    public static Specification<Account> getRole(String role){
        if (!StringUtils.isEmpty(role)){
                return (root, query, cri) -> {
                    return cri.equal(root.get("role"),  role);
                };
        }
        return null;
    }

    public static Specification<Account> getFirstName(String name){
        if (!StringUtils.isEmpty(name)){
                return (root, query, cri) -> {
                    return cri.like(cri.lower(root.get("firstName")), "%" +name + "%" );
                };
        }
        return null;
    }

    public static Specification<Account> isNotDelete(){
            return (root, query, cri) -> {
                return cri.equal(root.get("isDeleted"), Delete.FALSE);
            };
    }

    public static Specification<Account> getDepartmentName(String dpName){
        if (!StringUtils.isEmpty(dpName)){
                return (root, query, cri) -> {
                    Join<Account, Department> join = root.join("department");
                    return cri.like(cri.lower(join.get("name")), "%" + dpName.toLowerCase() + "%" );
                };
        }
        return null;
    }
}
