package com.flexisaf.ses.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flexisaf.ses.exceptionHandlers.FlexiException;
import com.flexisaf.ses.model.Response;
import com.flexisaf.ses.model.SESRequestValidator;
import com.flexisaf.ses.model.Student;
import com.flexisaf.ses.service.StudentService;

@RestController
@RequestMapping("/students")
public class StudentController {

	@Autowired
	StudentService studentService;

	@PostMapping("/add-student")
	public ResponseEntity<Response> addStudent(@Valid @RequestBody SESRequestValidator request) throws FlexiException {

		if (!request.getGender().matches("M|F"))
			throw new FlexiException("Gender must be M or F");

		Student student = studentService.addStudent(request.getFirstName(), request.getLastName(),
				request.getOtherNames(), request.getEmail(), request.getGender(), request.getDob(),
				request.getDepartmentId());

		Response response = new Response();
		response.setStatus(Response.SUCCESS);
		response.setMessage("Student enrolled successfully");
		response.setData(student);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/fetch-students")
	public ResponseEntity<Response> fetchAllStudents() {

		List<Student> students = studentService.fetchAllStudents();

		Response response = new Response();
		response.setStatus(Response.SUCCESS);
		response.setMessage("Students fetched successfully");
		response.setData(students);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PutMapping("/update-student")
	public ResponseEntity<Response> updateStudent(@Valid @RequestBody SESRequestValidator request)
			throws FlexiException {

		if (!request.getGender().matches("M|F"))
			throw new FlexiException("Gender must be M or F");

		Student student = studentService.updateStudent(request);

		Response response = new Response();
		response.setStatus(Response.SUCCESS);
		response.setMessage("Student update successfully");
		response.setData(student);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/fetch-by-matric")
	public ResponseEntity<Response> fetchStudentByMatricNumber(
			@RequestParam(name = "matricNumber", required = true) String matricNo) throws FlexiException {

		Student student = studentService.fetchStudentByMatricNumber(matricNo);

		Response response = new Response();
		response.setStatus(Response.SUCCESS);
		response.setMessage("Student update successfully");
		response.setData(student);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<Response> deleteStudent(@RequestParam(name = "matricNumber", required = true) String matricNo)
			throws FlexiException {

		studentService.deleteStudent(matricNo);

		Response response = new Response();
		response.setStatus(Response.SUCCESS);
		response.setMessage("Delete Operation was successfully");

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/search")
	public ResponseEntity<Response> fetchStudentsByQueryOrDateRange(
			@RequestParam(name = "size", required = true, defaultValue = "30") @Min(value = 10) int size,
			@RequestParam(name = "query", required = false, defaultValue = "") String query,
			@RequestParam(name = "page", required = false, defaultValue = "0") @Min(value = 0) int page,
			@RequestParam(name = "fromDate", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate fromDate,
			@RequestParam(name = "toDate", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate toDate)
			throws FlexiException {
		Response response = new Response();
		List<Student> students = new ArrayList<Student>();

		if (fromDate != null) {
			students = studentService.getStudentsByQueryOrDateRange(page, size, query, fromDate, toDate);
		} else {
			students = studentService.getStudentsByQuery(page, size, query);
		}
		response.setStatus(Response.SUCCESS);
		response.setMessage("Students fetched successfully");
		response.setData(students);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@Scheduled(cron = "0 0 0 * * ?", zone = "Africa/Lagos")
	public void bithdayMessagesScheduler() {

		// Send Birthday Messages 12am daily
		studentService.sendBithdayWishes();
	}
}
