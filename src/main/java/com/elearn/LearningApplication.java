package com.elearn;
import com.elearn.config.AppConstants;
import com.elearn.entity.Roles;
import com.elearn.repository.CourseRepo;
import com.elearn.repository.RoleRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.UUID;

@SpringBootApplication
public class LearningApplication  {

	public static void main(String[] args) {
		SpringApplication.run(LearningApplication.class, args);
	}}

