package com.flexisaf.ses.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.flexisaf.ses.exceptionHandlers.FlexiException;
import com.flexisaf.ses.model.Department;
import com.flexisaf.ses.model.PreviousMatric;
import com.flexisaf.ses.model.SESRequestValidator;
import com.flexisaf.ses.model.Student;
import com.flexisaf.ses.repository.DepartmentRepository;
import com.flexisaf.ses.repository.PreviousMatricRepository;
import com.flexisaf.ses.repository.StudentRepository;
import com.flexisaf.ses.service.StudentService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StudentServiceImpl implements StudentService {

	private final DepartmentRepository departmentRepository;
	private final StudentRepository studentRepository;
	private final PreviousMatricRepository preMatricRepo;;
	private final JavaMailSender javaMailSender;

	@Override
	public Student addStudent(String firstName, String lastName, String otherNames, String email, String gender,
			LocalDate dob, int departmentId) throws FlexiException {

		validateEmailAndDOB(email, dob);
		Optional<Department> department = departmentRepository.findById((long) departmentId);
		if (!department.isPresent())
			throw new FlexiException("Invalid department");

		String matricNumber = generateMatricNumber();

		Student student = Student.builder().matric(matricNumber).firstName(firstName).email(email).lastName(lastName)
				.otherNames(otherNames).Gender(gender).dob(dob).department(department.get())
				.createdDate(LocalDateTime.now()).updatedDate(LocalDateTime.now()).build();

		preMatricRepo.save(PreviousMatric.builder().id((long) 1).preMatric(matricNumber).build());

		return studentRepository.save(student);
	}

	@Override
	public Student updateStudent(SESRequestValidator request) throws FlexiException {

		Optional<Student> student = studentRepository.findByMatric(request.getMatric());
		if (!student.isPresent())
			throw new FlexiException("No Student Found");

		validateEmailAndDOB(request.getEmail(), request.getDob());

		Optional<Department> department = departmentRepository.findById((long) request.getDepartmentId());
		if (!department.isPresent())
			throw new FlexiException("Invalid department");

		student.get().setFirstName(request.getFirstName());
		student.get().setLastName(request.getLastName());
		student.get().setOtherNames(request.getOtherNames());
		student.get().setEmail(request.getEmail());
		student.get().setGender(request.getGender());
		student.get().setDob(request.getDob());
		student.get().setDepartment(department.get());
		student.get().setUpdatedDate(LocalDateTime.now());

		return studentRepository.save(student.get());
	}

	private void validateEmailAndDOB(String email, LocalDate dob) throws FlexiException {

		Optional<Student> validateEmail = studentRepository.findByEmail(email);

		if (validateEmail.isPresent()) {
			throw new FlexiException("Email Already Exist");
		}

		long age = ChronoUnit.YEARS.between(dob, LocalDate.now());

		if (age < 18 || age > 25)
			throw new FlexiException("Student age is not eligible for enrollment");

	}

	private String generateMatricNumber() {
		Optional<String> prevMatric = preMatricRepo.fetchPreviousMatric();

		if (prevMatric.isEmpty())
			return "FLEXISAF/001";

		int extractMatric = Integer.parseInt(prevMatric.get().split("/")[1]);

		return String.format("FLEXISAF/%03d", extractMatric + 1);

	}

	@Override
	public List<Student> fetchAllStudents() {
		return studentRepository.findAll();
	}

	@Override
	public List<Student> getStudentsByQueryOrDateRange(int page, int size, String query, LocalDate fromDate,
			LocalDate toDate) {

		Pageable pageable = PageRequest.of(page, size);
		Page<Student> students;
		List<Student> fetchedStudents = new ArrayList<Student>();

		query = query.toLowerCase();

		if (toDate == null)
			toDate = LocalDate.now();

		if (!query.isEmpty()) {
			students = studentRepository.findByQueryAndDateRange(query, fromDate, toDate, pageable);
		} else {
			students = studentRepository.findByDateRange(fromDate, toDate, pageable);
		}

		fetchedStudents.addAll(students.getContent());
		return fetchedStudents;
	}

	@Override
	public List<Student> getStudentsByQuery(int page, int size, String query) throws FlexiException {

		if (query.isEmpty()) {
			throw new FlexiException("No Query is empty");
		}

		Pageable pageable = PageRequest.of(page, size);

		query = query.toLowerCase();
		Page<Student> students = studentRepository.findByQuery(query, pageable);

		return students.getContent();
	}

	@Override
	public Student fetchStudentByMatricNumber(String matricNo) throws FlexiException {
		Optional<Student> student = studentRepository.findByMatric(matricNo);

		if (!student.isPresent())
			throw new FlexiException("No Student Found");
		return student.get();
	}

	@Override
	public void deleteStudent(String MatricNo) throws FlexiException {
		Optional<Student> student = studentRepository.findByMatric(MatricNo);

		if (!student.isPresent())
			throw new FlexiException("No Student Found");

		studentRepository.delete(student.get());

	}

	@Override
	public void sendBithdayWishes() {
		List<Student> students = studentRepository.findByDob(LocalDate.now());

		for (Student student : students) {
			try {
				SimpleMailMessage message = new SimpleMailMessage();
				message.setTo(student.getEmail());
				message.setSubject("Happy Birthday from FLEXISAF");
				message.setText("Hello," + System.lineSeparator()
						+ "With love from Flexisaf, we wish you a happy birthday."
						+ ChronoUnit.YEARS.between(student.getDob(), LocalDate.now()) + "." + System.lineSeparator()
						+ System.lineSeparator() + "Best regards," + System.lineSeparator() + "Team Flexisaf");
				javaMailSender.send(message);

			} catch (MailException exception) {
				// log exceptions
			}
		}

	}

}