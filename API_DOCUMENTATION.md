# Documentation API - Student Service avec Gestion des Cours

## Vue d'ensemble

Ce service gère les étudiants avec une intégration complète pour la gestion des cours via le microservice COURSE SERVICE (Django + PostgreSQL).

## Fonctionnalités

### 🎓 Gestion des Étudiants
- CRUD complet pour les étudiants
- Filtrage par université
- Filtrage par cours
- Filtrage par département
- Recherche par nom

### 📚 Gestion des Cours
- CRUD complet pour les cours
- Synchronisation avec le microservice COURSE SERVICE
- Filtrage par département
- Filtrage par nombre de crédits
- Recherche par nom de cours

### 🔗 Relations Étudiant-Cours
- Association Many-to-Many entre étudiants et cours
- Ajout/suppression de cours pour un étudiant
- Filtrage des étudiants par cours

## Endpoints API

### Étudiants (`/api/students`)

#### 1. Lister tous les étudiants
```
GET /api/students
```

#### 2. Obtenir un étudiant par ID
```
GET /api/students/{id}
```

#### 3. Ajouter un étudiant
```
POST /api/students
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@email.com",
  "university": {
    "id": 1
  }
}
```

#### 4. Mettre à jour un étudiant
```
PUT /api/students/{id}
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@email.com",
  "university": {
    "id": 1
  }
}
```

#### 5. Supprimer un étudiant
```
DELETE /api/students/{id}
```

#### 6. Rechercher des étudiants par nom
```
GET /api/students/search?name=John
```

#### 7. Rechercher des étudiants par université
```
GET /api/students/search/university?name=Harvard
```

#### 8. Ajouter un cours à un étudiant
```
POST /api/students/{studentId}/courses/{courseId}
```

#### 9. Supprimer un cours d'un étudiant
```
DELETE /api/students/{studentId}/courses/{courseId}
```

#### 10. Obtenir les cours d'un étudiant
```
GET /api/students/{studentId}/courses
```

#### 11. Rechercher des étudiants par cours
```
GET /api/students/search/course?courseName=Mathematics
```

#### 12. Rechercher des étudiants par code de cours
```
GET /api/students/search/course-code?courseCode=CS101
```

#### 13. Rechercher des étudiants par département
```
GET /api/students/search/department?department=Computer Science
```

### Cours (`/api/courses`)

#### 1. Lister tous les cours
```
GET /api/courses
```

#### 2. Obtenir un cours par ID
```
GET /api/courses/{id}
```

#### 3. Obtenir un cours par code
```
GET /api/courses/code/{courseCode}
```

#### 4. Ajouter un cours
```
POST /api/courses
Content-Type: application/json

{
  "courseCode": "CS101",
  "courseName": "Introduction to Computer Science",
  "description": "Basic concepts of computer science",
  "credits": 3,
  "department": "Computer Science"
}
```

#### 5. Mettre à jour un cours
```
PUT /api/courses/{id}
Content-Type: application/json

{
  "courseCode": "CS101",
  "courseName": "Introduction to Computer Science",
  "description": "Basic concepts of computer science",
  "credits": 3,
  "department": "Computer Science"
}
```

#### 6. Supprimer un cours
```
DELETE /api/courses/{id}
```

#### 7. Rechercher des cours par nom
```
GET /api/courses/search?name=Mathematics
```

#### 8. Rechercher des cours par département
```
GET /api/courses/department/{department}
```

#### 9. Rechercher des cours par nombre de crédits
```
GET /api/courses/credits/{credits}
```

#### 10. Synchroniser tous les cours avec le microservice
```
POST /api/courses/sync
```

#### 11. Synchroniser un cours spécifique
```
POST /api/courses/sync/{externalCourseId}
```

## Configuration

### Base de données
- **Type**: MySQL
- **URL**: `jdbc:mysql://localhost:3306/student`
- **Port**: 8081

### Microservice COURSE SERVICE
- **URL**: `http://localhost:8000`
- **Timeout**: 5000ms

## Modèles de données

### Student
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@email.com",
  "university": {
    "id": 1,
    "name": "Harvard University",
    "location": "Cambridge, MA"
  },
  "courses": [
    {
      "id": 1,
      "courseCode": "CS101",
      "courseName": "Introduction to Computer Science",
      "credits": 3,
      "department": "Computer Science"
    }
  ]
}
```

### Course
```json
{
  "id": 1,
  "courseCode": "CS101",
  "courseName": "Introduction to Computer Science",
  "description": "Basic concepts of computer science",
  "credits": 3,
  "department": "Computer Science",
  "externalCourseId": 123
}
```

## Relations

### Student ↔ University
- **Type**: Many-to-One**
- **Description**: Un étudiant appartient à une université

### Student ↔ Course
- **Type**: Many-to-Many**
- **Description**: Un étudiant peut suivre plusieurs cours, un cours peut être suivi par plusieurs étudiants
- **Table de liaison**: `student_courses`

## Synchronisation avec le microservice COURSE SERVICE

Le service peut synchroniser les cours avec le microservice COURSE SERVICE (Django + PostgreSQL) via des appels HTTP.

### Endpoints de synchronisation
- `POST /api/courses/sync` - Synchronise tous les cours
- `POST /api/courses/sync/{externalCourseId}` - Synchronise un cours spécifique

### Configuration requise
- Le microservice COURSE SERVICE doit être accessible sur `http://localhost:8000`
- Les endpoints du microservice doivent exposer :
  - `GET /api/courses` - Liste tous les cours
  - `GET /api/courses/{id}` - Détails d'un cours

## Exemples d'utilisation

### 1. Créer un étudiant et lui assigner des cours

```bash
# 1. Créer un étudiant
curl -X POST http://localhost:8081/api/students \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Alice",
    "lastName": "Smith",
    "email": "alice.smith@email.com",
    "university": {"id": 1}
  }'

# 2. Synchroniser les cours depuis le microservice
curl -X POST http://localhost:8081/api/courses/sync

# 3. Assigner un cours à l'étudiant
curl -X POST http://localhost:8081/api/students/1/courses/1
```

### 2. Rechercher des étudiants par cours

```bash
# Rechercher tous les étudiants suivant le cours "Mathematics"
curl "http://localhost:8081/api/students/search/course?courseName=Mathematics"

# Rechercher tous les étudiants suivant le cours avec le code "CS101"
curl "http://localhost:8081/api/students/search/course-code?courseCode=CS101"
```

### 3. Obtenir les cours d'un étudiant

```bash
# Obtenir tous les cours de l'étudiant avec l'ID 1
curl "http://localhost:8081/api/students/1/courses"
```

## Gestion des erreurs

Le service retourne des codes de statut HTTP appropriés :
- `200 OK` - Succès
- `201 Created` - Ressource créée
- `204 No Content` - Suppression réussie
- `400 Bad Request` - Requête invalide
- `404 Not Found` - Ressource non trouvée
- `500 Internal Server Error` - Erreur serveur

## CORS

Le service est configuré pour accepter les requêtes depuis `http://localhost:4200` (Angular/React frontend).

