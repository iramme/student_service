package com.campus.studentservice.service;

import com.campus.studentservice.model.Student;
import com.campus.studentservice.model.University;
import com.campus.studentservice.repository.StudentRepository;
import com.campus.studentservice.repository.UniversityRepository;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;




@Service
@RequiredArgsConstructor // ✅ Génère automatiquement un constructeur pour les attributs finals
public class StudentService {
    // 🧩 Injection des repositories et services (accès à la base de données)
    private final StudentRepository studentRepository;
    private final UniversityRepository universityRepository;

 
    // ============================
    // 📋 1. Lister tous les étudiants
    // ============================
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // ============================
    // 🔍 2. Récupérer un étudiant par ID
    // ============================
    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    // ============================
    // ➕ 3. Ajouter un nouvel étudiant
    // ============================
    public Student addStudent(Student student) {
    if (student.getUniversity() != null && student.getUniversity().getId() != null) {
        // 🔹 Charger l'université complète depuis la base
        University uni = universityRepository.findById(student.getUniversity().getId())
                .orElse(null);
        student.setUniversity(uni);
    }
    return studentRepository.save(student);
}


    // ============================
    // ✏️ 4. Mettre à jour un étudiant existant
    // ============================
    public Student updateStudent(Long id, Student studentDetails) {
        return studentRepository.findById(id).map(student -> {
            student.setFirstName(studentDetails.getFirstName());
            student.setLastName(studentDetails.getLastName());
            student.setEmail(studentDetails.getEmail());

            // ✅ Charger la vraie université depuis la base
            if (studentDetails.getUniversity() != null && studentDetails.getUniversity().getId() != null) {
                University university = universityRepository
                        .findById(studentDetails.getUniversity().getId())
                        .orElse(null);
                student.setUniversity(university);
            }

            return studentRepository.save(student);
        }).orElse(null);
    }
    // ============================
    // 🗑️ 5. Supprimer un étudiant par ID
    // ============================
    public boolean deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            return false;
        }
        studentRepository.deleteById(id);
        return true;
    }

    // ============================
    // 🔎 6. Rechercher des étudiants par nom
    // ============================
   

    public List<Student> searchStudentsByName(String name) {
        return studentRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name);
    }
    // ============================
    // 🎓 7. Filtrer les étudiants par université
    // ============================
    public List<Student> filterByUniversity(String universityName) {
        return studentRepository.findByUniversity_NameIgnoreCase(universityName);
    }

    
}
