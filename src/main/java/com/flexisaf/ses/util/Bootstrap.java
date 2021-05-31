package com.flexisaf.ses.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.flexisaf.ses.model.Department;
import com.flexisaf.ses.repository.DepartmentRepository;

@Component
public class Bootstrap implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private DepartmentRepository departmentRepository;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {

		// add departments to the database
		if (!departmentRepository.findAll().isEmpty())
			return;
		departmentRepository.deleteAll();

		Department enginerringDept = new Department();
		enginerringDept.setName("Enigineering Dept");

		Department sciencesDept = new Department();
		sciencesDept.setName("Science Dept");

		departmentRepository.save(enginerringDept);
		departmentRepository.save(sciencesDept);

	}

}