package com.campus.studentservice.model;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder //Ajout de @Builder C'est pratique pour créer des objets dans les tests ou lors de l'initialisation :
public class Student {

    // 🆔 Identifiant unique auto-incrémenté
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 👤 Prénom de l'étudiant
    @Column(nullable = false, name = "first_name")
    private String firstName;

    // 👤 Nom de famille
    @Column(nullable = false, name = "last_name")
    private String lastName;

    // 📧 Email (facultatif mais souvent utile)
    @Column(unique = true)
    private String email;
    
    
    // 🎓 Université associée (relation ManyToOne)
   // 🔗 Relation avec University
    @ManyToOne(fetch = FetchType.EAGER) // 👈 Important : EAGER pour forcer le chargement de l'université
    @JoinColumn(name = "university_id")
    private University university;
}
