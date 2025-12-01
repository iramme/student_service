package com.campus.studentservice.service;

import com.campus.studentservice.model.University;
import com.campus.studentservice.repository.UniversityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor // ✅ génère automatiquement un constructeur pour les champs finals
public class UniversityService {

    // 🧩 Injection du repository (accès base de données)
private final UniversityRepository universityRepository;

    public List<University> getAllUniversities() {
        return universityRepository.findAll();
    }

    public University addUniversity(University university) {
        return universityRepository.save(university);
    }
    // ============================
    // ➕ 1. Ajouter une université
    // ============================
   

    // ============================
    // 📋 2. Lister toutes les universités
    // ============================
    

    // ============================
    // 🔍 3. Trouver une université par ID
    // ============================
    public Optional<University> getUniversityById(Long id) {
        // 🔹 Retourne un Optional (présente ou non)
        return universityRepository.findById(id);
    }

    // ============================
    // ✏️ 4. Modifier une université
    // ============================
    public University updateUniversity(Long id, University newUniversity) {
        // 🔎 On cherche l’université à mettre à jour
        return universityRepository.findById(id).map(university -> {
            // 🏫 Mise à jour du nom et de la localisation
            university.setName(newUniversity.getName());
            university.setLocation(newUniversity.getLocation()); 
            // 💾 Sauvegarde et retourne l’objet mis à jour
            return universityRepository.save(university);
        }).orElse(null); // ❌ Si non trouvée, retourne null
    }

    // ============================
    // 🗑️ 5. Supprimer une université
    // ============================
    public boolean deleteUniversity(Long id) {
        // ⚠️ Vérifie d’abord si elle existe avant suppression
        if (!universityRepository.existsById(id)) {
            return false; // ❌ Rien à supprimer
        }
        universityRepository.deleteById(id);
        return true; // ✅ Suppression réussie
    }
}
