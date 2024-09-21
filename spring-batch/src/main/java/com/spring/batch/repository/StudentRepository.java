package com.spring.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.batch.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
}
