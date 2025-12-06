package com.campus.studentservice.controller;

import com.campus.studentservice.model.University;
import com.campus.studentservice.service.UniversityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/universities")
@RequiredArgsConstructor
@CrossOrigin(
    origins = {"http://localhost:3000", "http://127.0.0.1:3000"},
    allowedHeaders = "*",
    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, 
              RequestMethod.DELETE, RequestMethod.OPTIONS},
    allowCredentials = "true",
    maxAge = 3600
)
public class UniversityController {

    private final UniversityService universityService;

    // ============================
    // 🧪 Endpoint de test
    // ============================
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("✅ API University is working!");
    }

    // ============================
    // 📋 1. Lister toutes les universités
    // ============================
    @GetMapping
    public ResponseEntity<List<University>> getAllUniversities() {
        List<University> universities = universityService.getAllUniversities();
        return ResponseEntity.ok(universities);
    }

    // ============================
    // 🔍 2. Récupérer une université par ID
    // ============================
    @GetMapping("/{id}")
    public ResponseEntity<University> getUniversityById(@PathVariable Long id) {
        return universityService.getUniversityById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ============================
    // ➕ 3. Ajouter une nouvelle université
    // ============================
    @PostMapping
    public ResponseEntity<University> addUniversity(@RequestBody University university) {
        University saved = universityService.addUniversity(university);
        return ResponseEntity.ok(saved);
    }

    // ============================
    // ✏️ 4. Modifier une université existante
    // ============================
    @PutMapping("/{id}")
    public ResponseEntity<University> updateUniversity(@PathVariable Long id, @RequestBody University university) {
        University updated = universityService.updateUniversity(id, university);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    // ============================
    // 🗑️ 5. Supprimer une université par ID
    // ============================
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUniversity(@PathVariable Long id) {
        boolean deleted = universityService.deleteUniversity(id);

        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("❌ Université introuvable avec l'ID : " + id);
        }

        return ResponseEntity.ok("✅ L'université avec l'ID " + id + " a été supprimée avec succès !");
    }

    // ============================
    // 🔧 6. Endpoint OPTIONS pour CORS preflight
    // ============================
    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> options() {
        return ResponseEntity.ok().build();
    }
}