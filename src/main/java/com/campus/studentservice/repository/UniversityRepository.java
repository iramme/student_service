package com.campus.studentservice.repository;

import com.campus.studentservice.model.University;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UniversityRepository extends JpaRepository<University, Long> {

    // 🔍 Recherche toutes les universités contenant le nom (insensible à la casse)
    List<University> findByNameContainingIgnoreCase(String name);
}