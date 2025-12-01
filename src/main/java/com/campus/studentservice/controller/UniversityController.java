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
@RequiredArgsConstructor // ✅ Remplace @Autowired, injection propre via constructeur
@CrossOrigin("*") // 🌍 Autorise les appels depuis d'autres domaines (utile pour un frontend)
public class UniversityController {
@GetMapping("/test")
public String test() {
    return "API is working!";
}

    // 🧩 Service injecté automatiquement
    private final UniversityService universityService;

    // ============================
    // 📋 1. Lister toutes les universités
    // ============================
    @GetMapping
    public ResponseEntity<List<University>> getAllUniversities() {
        List<University> universities = universityService.getAllUniversities();
        // ✅ HTTP 200 OK avec la liste
        return ResponseEntity.ok(universities);
    }

    // ============================
    // 🔍 2. Récupérer une université par ID
    // ============================
    @GetMapping("/{id}")
    public ResponseEntity<University> getUniversityById(@PathVariable Long id) {
        return universityService.getUniversityById(id)
                .map(ResponseEntity::ok) // ✅ Université trouvée → 200 OK
                .orElse(ResponseEntity.notFound().build()); // ❌ Sinon → 404 Not Found
    }

    // ============================
    // ➕ 3. Ajouter une nouvelle université
    // ============================
    @PostMapping
    public ResponseEntity<University> addUniversity(@RequestBody University university) {
        University saved = universityService.addUniversity(university);
        // ✅ Retourne 200 OK (tu peux aussi mettre 201 Created avec URI si besoin)
        return ResponseEntity.ok(saved);
    }

    // ============================
    // ✏️ 4. Modifier une université existante
    // ============================
    @PutMapping("/{id}")
    public ResponseEntity<University> updateUniversity(@PathVariable Long id, @RequestBody University university) {
        University updated = universityService.updateUniversity(id, university);
        if (updated == null) {
            // ❌ Si l’université n’existe pas → 404
            return ResponseEntity.notFound().build();
        }
        // ✅ Sinon → 200 OK avec les nouvelles données
        return ResponseEntity.ok(updated);
    }

    // 🗑️ 5. Supprimer une université par ID (avec message clair)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUniversity(@PathVariable Long id) {
        boolean deleted = universityService.deleteUniversity(id);

        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("❌ Université introuvable avec l'ID : " + id);
        }

        return ResponseEntity.ok("✅ L'université avec l'ID " + id + " a été supprimée avec succès !");
    }
}