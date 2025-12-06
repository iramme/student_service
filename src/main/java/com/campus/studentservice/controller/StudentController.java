package com.campus.studentservice.controller;

import com.campus.studentservice.model.Student;
import com.campus.studentservice.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@CrossOrigin(
    origins = {"http://localhost:3000", "http://127.0.0.1:3000"},
    allowedHeaders = "*",
    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, 
              RequestMethod.DELETE, RequestMethod.OPTIONS},
    allowCredentials = "true",
    maxAge = 3600
)
public class StudentController {

    private final StudentService studentService;

    // ============================
    // 🟢 1. Ajouter un étudiant
    // ============================
    @PostMapping
    public ResponseEntity<Student> addStudent(@Valid @RequestBody Student student) {
        Student savedStudent = studentService.addStudent(student);
        return ResponseEntity.ok(savedStudent);
    }

    // ============================
    // 🟡 2. Lister tous les étudiants
    // ============================
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    // ============================
    // 🔵 3. Obtenir un étudiant par ID
    // ============================
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
        Student student = studentService.getStudentById(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    // ============================
    // 🟣 4. Mettre à jour un étudiant
    // ============================
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateStudent(@PathVariable Long id, 
                                                           @Valid @RequestBody Student student) {
        Student updatedStudent = studentService.updateStudent(id, student);

        if (updatedStudent == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Student not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Student updated successfully");
        response.put("updatedStudent", updatedStudent);

        return ResponseEntity.ok(response);
    }

    // ============================
    // 🗑️ 5. Supprimer un étudiant par ID
    // ============================
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
        boolean deleted = studentService.deleteStudent(id);

        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("❌ Étudiant introuvable avec l'ID : " + id);
        }

        return ResponseEntity.ok("✅ L'étudiant avec l'ID " + id + " a été supprimé avec succès !");
    }

    // ============================
    // 🔍 6. Rechercher un étudiant par nom
    // ============================
    @GetMapping("/search")
    public ResponseEntity<List<Student>> searchStudents(@RequestParam String name) {
        List<Student> results = studentService.searchStudentsByName(name);

        if (results.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(results);
    }

    // ============================
    // 🎓 7. Rechercher les étudiants par université
    // ============================
    @GetMapping("/search/university")
    public ResponseEntity<List<Student>> searchByUniversity(@RequestParam("name") String universityName) {
        List<Student> results = studentService.filterByUniversity(universityName);

        if (results.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(results);
    }

    // ============================
    // 🧪 8. Endpoint de test CORS (pour debug)
    // ============================
    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> testEndpoint() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "✅ API Spring Boot fonctionne !");
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        response.put("cors", "Configuré pour localhost:3000");
        return ResponseEntity.ok(response);
    }

    // ============================
    // 🔧 9. Endpoint OPTIONS pour CORS preflight
    // ============================
    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> options() {
        return ResponseEntity.ok().build();
    }
}