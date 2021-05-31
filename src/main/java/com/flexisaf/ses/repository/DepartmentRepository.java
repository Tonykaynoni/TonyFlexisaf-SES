package com.flexisaf.ses.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flexisaf.ses.model.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

}