package com.campus.studentservice.controller;

// ✅ Importation des classes nécessaires
import com.campus.studentservice.model.Student;
import com.campus.studentservice.service.StudentService;
import lombok.RequiredArgsConstructor;
import java.util.Map;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;  // Pour gérer les réponses HTTP personnalisées
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;            // Pour la validation des données reçues

import java.util.HashMap;
import java.util.List;

@RestController // 🧩 Indique à Spring que cette classe gère les requêtes REST (API)
@RequestMapping("/api/students") // 📍 Chemin de base pour tous les endpoints de ce contrôleur
@RequiredArgsConstructor // 💉 Lombok : crée un constructeur pour injecter les dépendances finales

// 🌐 Autorise les requêtes depuis le frontend (Angular/React)

public class StudentController {

    // 💉 Injection automatique du service (grâce à @RequiredArgsConstructor)
    private final StudentService studentService;

    // ============================
    // 🟢 1. Ajouter un étudiant
    // ============================
    @PostMapping
    public ResponseEntity<Student> addStudent(@Valid @RequestBody Student student) {
        // @Valid → déclenche la validation selon les annotations dans Student (ex: @NotBlank, @Email)
        Student savedStudent = studentService.addStudent(student);
        // 📨 Retourne une réponse HTTP 200 OK avec l’objet créé
        return ResponseEntity.ok(savedStudent);
    }

    // ============================
    // 🟡 2. Lister tous les étudiants
    // ============================
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        // 🔁 Récupère la liste complète depuis le service
        List<Student> students = studentService.getAllStudents();
        // 📦 Retourne la liste avec statut 200 OK
        return ResponseEntity.ok(students);
    }

    // ============================
    // 🔵 3. Obtenir un étudiant par ID
    // ============================
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
        // 🕵️‍♀️ Recherche de l’étudiant par ID
        Student student = studentService.getStudentById(id);
        // ❌ Si aucun étudiant trouvé → renvoie 404 Not Found
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        // ✅ Si trouvé → renvoie 200 OK avec l’étudiant
        return ResponseEntity.ok(student);
    }

    // ============================
    // 🟣 4. Mettre à jour un étudiant
    // ============================
   @PutMapping("/{id}")
public ResponseEntity<Map<String, Object>> updateStudent(@PathVariable Long id, @Valid @RequestBody Student student) {
    Student updatedStudent = studentService.updateStudent(id, student);

    // ⚠️ Si l’étudiant n’existe pas
    if (updatedStudent == null) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Student not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // ✅ Message + étudiant mis à jour
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
    // 🧩 Appel du service pour suppression (retourne true si trouvé et supprimé)
    boolean deleted = studentService.deleteStudent(id);

    // ⚠️ Si suppression impossible (étudiant introuvable)
    if (!deleted) {
        // 🔴 HTTP 404 : Not Found
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("❌ Étudiant introuvable avec l'ID : " + id);
    }

    // ✅ Si suppression réussie → HTTP 200 : OK avec message
    return ResponseEntity.ok("✅ L'étudiant avec l'ID " + id + " a été supprimé avec succès !");
}


// ============================
// 🔍 6. Rechercher un étudiant par nom (endpoint optionnel)
// ============================
@GetMapping("/search")
public ResponseEntity<List<Student>> searchStudents(@RequestParam String name) {
    // 🔎 Recherche tous les étudiants dont le nom contient la chaîne donnée
    List<Student> results = studentService.searchStudentsByName(name);

    // ⚠️ Si aucun étudiant trouvé → HTTP 404 (facultatif)
    if (results.isEmpty()) {
        return ResponseEntity.notFound().build();
    }

    // ✅ Sinon, retourne la liste (HTTP 200 OK)
    return ResponseEntity.ok(results);
}
// ============================
// 🎓 7. Rechercher les étudiants par université
// ============================
@GetMapping("/search/university")
public ResponseEntity<List<Student>> searchByUniversity(@RequestParam("name") String universityName) {
    // Appel au service pour filtrer les étudiants
    List<Student> results = studentService.filterByUniversity(universityName);

    // Si aucun étudiant trouvé → 404 (facultatif)
    if (results.isEmpty()) {
        return ResponseEntity.notFound().build();
    }

    // ✅ Sinon, retourne la liste (200 OK)
    return ResponseEntity.ok(results);
}


}
