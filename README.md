# Student Service - Gestion des Étudiants Ultra-Simplifiée

## 📋 Description

Ce service Spring Boot gère les étudiants avec des appels HTTP directs vers le microservice COURSE SERVICE (Django + PostgreSQL) pour la gestion des cours. Architecture ultra-simplifiée avec tout centralisé dans StudentService.

## 🚀 Fonctionnalités

### Gestion des Étudiants
- ✅ CRUD complet (Create, Read, Update, Delete)
- ✅ Filtrage par université
- ✅ Filtrage par cours
- ✅ Recherche par nom

### Gestion des Cours (via microservice)
- ✅ Appels HTTP directs vers COURSE SERVICE
- ✅ Récupération des cours en temps réel
- ✅ Recherche par nom

### Relations Étudiant-Cours
- ✅ Appels HTTP pour récupérer les cours
- ✅ Filtrage des étudiants par cours (via microservice)
- ✅ Pas de duplication des données

## 🛠️ Prérequis

- Java 17+
- Maven 3.6+
- MySQL 8.0+
- Le microservice COURSE SERVICE (Django + PostgreSQL) sur le port 8000

## 📦 Installation

### 1. Cloner le projet
```bash
git clone <repository-url>
cd student-service
```

### 2. Configurer la base de données
Modifiez le fichier `src/main/resources/application.properties` :

```properties
# Configuration de la base de données
spring.datasource.url=jdbc:mysql://localhost:3306/student?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=1234
```

### 3. Démarrer MySQL
Assurez-vous que MySQL est démarré et accessible.

### 4. Compiler et démarrer l'application
```bash
mvn clean install
mvn spring-boot:run
```

L'application sera disponible sur `http://localhost:8081`

## 🔧 Configuration

### Base de données
- **Type**: MySQL
- **Port**: 3306
- **Base**: `student`
- **Tables**: Créées automatiquement par Hibernate

### Microservice COURSE SERVICE
- **URL**: `http://localhost:8000`
- **Timeout**: 5000ms

## 📚 API Endpoints

### Étudiants (`/api/students`)
- `GET /api/students` - Lister tous les étudiants
- `GET /api/students/{id}` - Obtenir un étudiant par ID
- `POST /api/students` - Créer un étudiant
- `PUT /api/students/{id}` - Mettre à jour un étudiant
- `DELETE /api/students/{id}` - Supprimer un étudiant
- `GET /api/students/search?name={name}` - Rechercher par nom
- `GET /api/students/search/university?name={name}` - Rechercher par université
- `GET /api/students/search/course?courseName={name}` - Rechercher par cours (via microservice)
- `GET /api/students/search/course-code?courseCode={code}` - Rechercher par code de cours (via microservice)
- `GET /api/students/{studentId}/courses` - Obtenir les cours d'un étudiant (via microservice)

### Cours (via microservice COURSE SERVICE)
- `GET /api/students/courses/available` - Lister tous les cours disponibles
- `GET /api/students/courses/search?name={name}` - Rechercher des cours par nom

## 🧪 Tests

### Test manuel avec curl
```bash
# Lister tous les étudiants
curl http://localhost:8081/api/students

# Créer un étudiant
curl -X POST http://localhost:8081/api/students \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@email.com",
    "university": {"id": 1}
  }'

# Créer un cours
curl -X POST http://localhost:8081/api/courses \
  -H "Content-Type: application/json" \
  -d '{
    "courseCode": "CS101",
    "courseName": "Introduction to Computer Science",
    "description": "Basic concepts of computer science",
    "credits": 3,
    "department": "Computer Science"
  }'
```

### Script de test automatisé
```bash
./test-api.sh
```

## 🔗 Intégration avec le microservice COURSE SERVICE

### Configuration requise
Le microservice COURSE SERVICE (Django + PostgreSQL) doit exposer les endpoints suivants :

- `GET /api/courses` - Liste tous les cours
- `GET /api/courses/{id}` - Détails d'un cours

### Synchronisation
```bash
# Synchroniser tous les cours
curl -X POST http://localhost:8081/api/courses/sync

# Synchroniser un cours spécifique
curl -X POST http://localhost:8081/api/courses/sync/123
```

## 📊 Modèles de données

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

## 🗄️ Base de données

### Tables créées automatiquement
- `students` - Table des étudiants
- `universities` - Table des universités
- `courses` - Table des cours
- `student_courses` - Table de liaison Many-to-Many

### Relations
- **Student ↔ University**: Many-to-One
- **Student ↔ Course**: Many-to-Many

## 🚨 Gestion des erreurs

Le service retourne des codes de statut HTTP appropriés :
- `200 OK` - Succès
- `201 Created` - Ressource créée
- `204 No Content` - Suppression réussie
- `400 Bad Request` - Requête invalide
- `404 Not Found` - Ressource non trouvée
- `500 Internal Server Error` - Erreur serveur

## 🌐 CORS

Le service est configuré pour accepter les requêtes depuis `http://localhost:4200` (Angular/React frontend).

## 📝 Documentation complète

Pour plus de détails sur l'API, consultez le fichier `API_DOCUMENTATION.md`.

## 🤝 Contribution

1. Fork le projet
2. Créer une branche feature (`git checkout -b feature/AmazingFeature`)
3. Commit les changements (`git commit -m 'Add some AmazingFeature'`)
4. Push vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvrir une Pull Request

## 📄 Licence

Ce projet est sous licence MIT. Voir le fichier `LICENSE` pour plus de détails.
