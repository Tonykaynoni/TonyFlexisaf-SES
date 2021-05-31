package com.flexisaf.ses.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.flexisaf.ses.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
	Optional<Student> findByMatric(String MatricNo);

	@Query(value = "SELECT * from students left join department on students.department_id = department.id"
			+ " where ( students.first_name LIKE %?1% or students.last_name LIKE %?1%"
			+ " or students.other_names LIKE %?1%" + " or students.email LIKE %?1%" + " or students.matric LIKE %?1%"
			+ " or CONCAT(students.first_name, ' ', students.last_name, ' ', students.other_names) LIKE %?1% )"
			+ " or students.gender LIKE %?1%"
			+ " or department.name LIKE %?1%  and students.created_date between ?2 and ?3", nativeQuery = true)
	Page<Student> findByQueryAndDateRange(String query, LocalDate fromDate, LocalDate toDate, Pageable pageable);

	@Query(value = "SELECT * from students left join department on students.department_id = department.id"
			+ " where students.created_date between ?2 and ?3", nativeQuery = true)
	Page<Student> findByDateRange(LocalDate fromDate, LocalDate toDate, Pageable pageable);

	@Query(value = "SELECT * from students left join department on students.department_id = department.id"
			+ " where ( students.first_name LIKE %?1% or students.last_name LIKE %?1%" + " or students.matric LIKE %?1%"
			+ " or students.other_names LIKE %?1%" + " or students.email LIKE %?1%"
			+ " or CONCAT(students.first_name, ' ', students.last_name, ' ', students.other_names) LIKE %?1% )"
			+ " or students.gender LIKE %?1%" + " or department.name LIKE %?1%", nativeQuery = true)
	Page<Student> findByQuery(String query, Pageable pageable);

	List<Student> findByDob(LocalDate now);

	Optional<Student> findByEmail(String email);
}
