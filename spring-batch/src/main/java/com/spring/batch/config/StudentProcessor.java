package com.spring.batch.config;

import org.springframework.batch.item.ItemProcessor;

import com.spring.batch.entity.Student;

public class StudentProcessor implements ItemProcessor<Student,Student> {

    @Override
    public Student process(Student student) {
    	//all business logic goes here
    	student.setAge(2); // we are doing this business logic as of now
        return student;
    }
}
