package com.campus.studentservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.campus.studentservice.repository.UniversityRepository;
import com.campus.studentservice.model.University;

@SpringBootApplication
public class StudentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentServiceApplication.class, args);
	}

@Bean
CommandLineRunner initUniversities(UniversityRepository repo) {
    return args -> {
        // Initialiser quelques universités si la base est vide
        if (repo.count() == 0) {
            System.out.println("Initialisation des universités...");
            University u1 = new University();
            u1.setName("Harvard University");
            u1.setLocation("Cambridge, MA");
            repo.save(u1);
            
            University u2 = new University();
            u2.setName("MIT");
            u2.setLocation("Cambridge, MA");
            repo.save(u2);
            
            University u3 = new University();
            u3.setName("Stanford University");
            u3.setLocation("Stanford, CA");
            repo.save(u3);
            
            University u4 = new University();
            u4.setName("Université de Paris");
            u4.setLocation("Paris, France");
            repo.save(u4);
            
            University u5 = new University();
            u5.setName("Oxford University");
            u5.setLocation("Oxford, UK");
            repo.save(u5);
            
            System.out.println("✅ " + repo.count() + " universités créées.");
        }
        
        System.out.println("Universities in DB: " + repo.count());
        repo.findAll().forEach(u -> System.out.println("  - " + u.getName()));
    };
}
}
