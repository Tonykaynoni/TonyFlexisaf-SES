package com.flexisaf.ses.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.flexisaf.ses.model.PreviousMatric;

@Repository
public interface PreviousMatricRepository extends JpaRepository<PreviousMatric, Long>{

	@Query(value = "Select pre_matric from previous_matric LIMIT 1", nativeQuery = true)
	Optional<String> fetchPreviousMatric();
	
}


