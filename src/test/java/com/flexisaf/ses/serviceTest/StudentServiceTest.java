package com.flexisaf.ses.serviceTest;

import static org.mockito.Mockito.times;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import com.flexisaf.ses.exceptionHandlers.FlexiException;
import com.flexisaf.ses.model.Department;
import com.flexisaf.ses.model.SESRequestValidator;
import com.flexisaf.ses.model.Student;
import com.flexisaf.ses.repository.DepartmentRepository;
import com.flexisaf.ses.repository.PreviousMatricRepository;
import com.flexisaf.ses.repository.StudentRepository;
import com.flexisaf.ses.service.StudentService;
import com.flexisaf.ses.serviceImpl.StudentServiceImpl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:test.properties")
public class StudentServiceTest {

	@Mock
	private JavaMailSender javaMailSender;

	private StudentService studentService;

	@Mock
	private DepartmentRepository departmentRepository;

	@Mock
	private PreviousMatricRepository previousMatricRepo;

	@Mock
	private StudentRepository studentRepo;

	private SESRequestValidator request;
	private Department department;
	private Student student;

	@BeforeEach
	public void setUp() {

		department = new Department();
		department.setId((long) 1);
		department.setName("Physical Sciences");

		request = new SESRequestValidator();
		request.setDepartmentId(1);
		request.setDob(LocalDate.of(2000, 06, 12));
		request.setEmail("tonykay001@gmail.com");
		request.setFirstName("Adedayo");
		request.setLastName("Anthony");
		request.setLastName("Olajide");
		request.setMatric("FLEXISAF/001");

		studentService = new StudentServiceImpl(departmentRepository, studentRepo, previousMatricRepo, javaMailSender);
		student = Student.builder().email("mt@mt.com").dob(LocalDate.of(1995, 06, 12)).firstName("John").lastName("Doe")
				.otherNames(null).Gender("M").department(department).build();

		Mockito.when(departmentRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(department));

	}

	@Test
	public void shouldEnrollStudentTest() throws FlexiException {

		Mockito.when(previousMatricRepo.fetchPreviousMatric()).thenReturn(Optional.of("FLEXISAF/001"));

		studentService.addStudent("Test1", "TestLastname", "TestOthername", "testEmail@test.com", "M",
				LocalDate.of(2000, 01, 01), 1);

		Mockito.verify(studentRepo, times(1)).save(Mockito.any(Student.class));

		Mockito.when(departmentRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		Assertions.assertThrows(FlexiException.class, () -> studentService.addStudent("Test1", "TestLastname",
				"TestOthername", "testEmail@test.com", "M", LocalDate.of(2000, 01, 01), 1));

	}

	@Test
	public void shouldFailToEnrolltudentTest_EmailAlreadyExist() throws FlexiException {

		Mockito.when(studentRepo.findByEmail(Mockito.anyString())).thenReturn(Optional.of(student));
		Assertions.assertThrows(FlexiException.class, () -> studentService.addStudent("Test1", "TestLastname",
				"TestOthername", "testEmail@test.com", "M", LocalDate.of(1997, 01, 01), 1));

	}

	@Test
	public void shouldFailToEnrolltudentTest_InvalidDOB() throws FlexiException {

		Mockito.when(previousMatricRepo.fetchPreviousMatric()).thenReturn(Optional.of("FLEXISAF/001"));
		Assertions.assertThrows(FlexiException.class, () -> studentService.addStudent("Test1", "TestLastname",
				"TestOthername", "testEmail@test.com", "M", LocalDate.of(2005, 01, 01), 1));

	}

	@Test
	public void shouldUpdateStudent() throws FlexiException {

		Mockito.when(studentRepo.findByMatric(Mockito.anyString())).thenReturn(Optional.of(student));
		Mockito.when(departmentRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(department));

		studentService.updateStudent(request);
		Mockito.verify(studentRepo).save(Mockito.any(Student.class));
	}

	@Test
	public void shouldFailToUpdateStudent_InvalidDepartment() throws FlexiException {
		Mockito.when(studentRepo.findByMatric(Mockito.anyString())).thenReturn(Optional.of(student));
		Mockito.when(departmentRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

		Assertions.assertThrows(FlexiException.class, () -> studentService.updateStudent(request));

	}

	@Test
	public void shouldFetchAllStudents() {
		Mockito.when(studentRepo.findAll()).thenReturn(Arrays.asList(student));
		Assertions.assertTrue(studentService.fetchAllStudents().size() == 1);
	}

	@Test
	public void shouldFetchByMatric() throws FlexiException {

		Mockito.when(studentRepo.findByMatric(Mockito.anyString())).thenReturn(Optional.empty());
		Assertions.assertThrows(FlexiException.class,
				() -> studentService.fetchStudentByMatricNumber(Mockito.anyString()));

	}

	@Test
	public void getStudentsByQueryOrDateRangeTest() throws FlexiException {
		Mockito.when(studentRepo.findByQueryAndDateRange(Mockito.anyString(), Mockito.any(LocalDate.class),
				Mockito.any(LocalDate.class), Mockito.any(Pageable.class)))
				.thenReturn(new PageImpl<Student>(Arrays.asList(student)));

		Mockito.when(studentRepo.findByDateRange(Mockito.any(LocalDate.class), Mockito.any(LocalDate.class),
				Mockito.any(Pageable.class))).thenReturn(new PageImpl<Student>(Arrays.asList(student)));

		studentService.getStudentsByQueryOrDateRange(0, 10, "Test", LocalDate.now(), LocalDate.now());

		Mockito.verify(studentRepo, times(1)).findByQueryAndDateRange(Mockito.anyString(), Mockito.any(LocalDate.class),
				Mockito.any(LocalDate.class), Mockito.any(Pageable.class));

		Assertions.assertTrue(studentService
				.getStudentsByQueryOrDateRange(0, 10, "Test", LocalDate.now(), LocalDate.now()).size() == 1);
	}

	@Test
	public void getStudentsByQueryTest() throws FlexiException {

		Mockito.when(studentRepo.findByQuery(Mockito.anyString(), Mockito.any(Pageable.class)))
				.thenReturn(new PageImpl<Student>(Arrays.asList(student)));

		studentService.getStudentsByQuery(0, 10, "Test");

		Mockito.verify(studentRepo, times(1)).findByQuery(Mockito.anyString(), Mockito.any(Pageable.class));

		Assertions.assertTrue(studentService.getStudentsByQuery(0, 10, "Test").size() == 1);
	}

}
