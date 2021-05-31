package com.flexisaf.ses.service;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.Min;

import com.flexisaf.ses.exceptionHandlers.FlexiException;
import com.flexisaf.ses.model.SESRequestValidator;
import com.flexisaf.ses.model.Student;

public interface StudentService {

	Student addStudent(String firstName, String lastName, String otherNames, String email, String gender, LocalDate dob,
			int departmentId) throws FlexiException;

	List<Student> fetchAllStudents();

	Student updateStudent(SESRequestValidator request) throws FlexiException;

	Student fetchStudentByMatricNumber(String matricNo) throws FlexiException;

	void deleteStudent(String MatricNo) throws FlexiException;

	List<Student> getStudentsByQueryOrDateRange(@Min(0) int page, @Min(10) int size, String query, LocalDate fromDate,
			LocalDate toDate);

	List<Student> getStudentsByQuery(@Min(0) int page, @Min(10) int size, String query) throws FlexiException;

	void sendBithdayWishes();

}
