package com.campus.studentservice.repository;

import com.campus.studentservice.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
   // Recherche par prénom ou nom (insensible à la casse)
    List<Student> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);

    // Recherche par nom d'université
    List<Student> findByUniversity_NameIgnoreCase(String universityName);
}
