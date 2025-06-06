package com.elearn;
import com.elearn.entity.Course;
import com.elearn.entity.User;
import com.elearn.repository.CourseRepo;
import com.elearn.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.util.UUID;

@SpringBootApplication
public class LearningApplication   implements CommandLineRunner {
	@Autowired
	private CourseRepo courseRepo;
	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(LearningApplication.class, args);
	}

	/**
	 * @param args
	 * @throws Exception
	 */

	public void run(String... args) throws Exception {
//
//		Course course1=new Course();
//		course1.setId(UUID.randomUUID().toString());
//		course1.setTitle("head");
//		course1.setCreatedDate(LocalDate.now());
//
//		courseRepo.save(course1);
//		System.out.println(course1);
//
//
//		User user=new User();
//		user.setUserId(UUID.randomUUID().toString());
//		user.setEmail("msg@gmail.com");
//		user.setPassword("msmammsmsaa");
//		userRepository.save(user);

	}
}
