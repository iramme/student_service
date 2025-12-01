# Architecture du Student Service

## Vue d'ensemble

Le Student Service est un microservice Spring Boot qui gère les étudiants et leurs relations avec les cours, avec une intégration complète pour le microservice COURSE SERVICE (Django + PostgreSQL).

## Architecture du système

```
┌─────────────────────────────────────────────────────────────────┐
│                        Frontend (Angular/React)                │
│                         Port: 4200                             │
└─────────────────────┬───────────────────────────────────────────┘
                      │ HTTP/REST API
                      │
┌─────────────────────▼───────────────────────────────────────────┐
│                    Student Service                             │
│                    Port: 8081                                  │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │                Controllers                               │   │
│  │  ┌─────────────────┐  ┌─────────────────┐              │   │
│  │  │StudentController│  │ CourseController│              │   │
│  │  └─────────────────┘  └─────────────────┘              │   │
│  └─────────────────────────────────────────────────────────┘   │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │                 Services                                │   │
│  │  ┌─────────────────┐  ┌─────────────────┐              │   │
│  │  │  StudentService │  │  CourseService  │              │   │
│  │  └─────────────────┘  └─────────────────┘              │   │
│  └─────────────────────────────────────────────────────────┘   │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │                Repositories                               │   │
│  │  ┌─────────────────┐  ┌─────────────────┐              │   │
│  │  │StudentRepository│  │ CourseRepository│              │   │
│  │  └─────────────────┘  └─────────────────┘              │   │
│  └─────────────────────────────────────────────────────────┘   │
└─────────────────────┬───────────────────────────────────────────┘
                      │ JPA/Hibernate
                      │
┌─────────────────────▼───────────────────────────────────────────┐
│                    MySQL Database                               │
│                    Port: 3306                                  │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │                    Tables                                │   │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐    │   │
│  │  │  students   │  │ universities│  │   courses    │    │   │
│  │  └─────────────┘  └─────────────┘  └─────────────┘    │   │
│  │  ┌─────────────────────────────────────────────────┐   │   │
│  │  │            student_courses                      │   │   │
│  │  │         (table de liaison)                      │   │   │
│  │  └─────────────────────────────────────────────────┘   │   │
│  └─────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────┘
                      │ HTTP/REST API
                      │
┌─────────────────────▼───────────────────────────────────────────┐
│                COURSE SERVICE                                   │
│                (Django + PostgreSQL)                           │
│                Port: 8000                                       │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │                Django API                                │   │
│  │  ┌─────────────────────────────────────────────────┐   │   │
│  │  │  GET /api/courses                               │   │   │
│  │  │  GET /api/courses/{id}                          │   │   │
│  │  └─────────────────────────────────────────────────┘   │   │
│  └─────────────────────────────────────────────────────────┘   │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │              PostgreSQL Database                       │   │
│  │              Port: 5432                                │   │
│  └─────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────┘
```

## Relations entre entités

### Student ↔ University
- **Type**: Many-to-One
- **Description**: Un étudiant appartient à une université
- **JPA**: `@ManyToOne` dans Student, `@OneToMany` dans University

### Student ↔ Course
- **Type**: Many-to-Many
- **Description**: Un étudiant peut suivre plusieurs cours, un cours peut être suivi par plusieurs étudiants
- **JPA**: `@ManyToMany` avec `@JoinTable`
- **Table de liaison**: `student_courses`

## Flux de données

### 1. Création d'un étudiant
```
Frontend → StudentController → StudentService → StudentRepository → MySQL
```

### 2. Ajout d'un cours à un étudiant
```
Frontend → StudentController → StudentService → StudentRepository → MySQL
```

### 3. Synchronisation des cours
```
CourseController → CourseService → RestTemplate → COURSE SERVICE → PostgreSQL
CourseService → CourseRepository → MySQL
```

### 4. Filtrage des étudiants par cours
```
Frontend → StudentController → StudentService → StudentRepository → MySQL
```

## Endpoints principaux

### Student Service (Port 8081)
- `GET /api/students` - Lister tous les étudiants
- `POST /api/students` - Créer un étudiant
- `GET /api/students/{id}/courses` - Obtenir les cours d'un étudiant
- `POST /api/students/{studentId}/courses/{courseId}` - Ajouter un cours à un étudiant
- `GET /api/students/search/course?courseName={name}` - Filtrer par cours

### Course Service (Port 8081)
- `GET /api/courses` - Lister tous les cours
- `POST /api/courses` - Créer un cours
- `POST /api/courses/sync` - Synchroniser avec le microservice

### COURSE SERVICE (Port 8000)
- `GET /api/courses` - Lister tous les cours (Django)
- `GET /api/courses/{id}` - Détails d'un cours (Django)

## Configuration

### Student Service
```properties
server.port=8081
spring.datasource.url=jdbc:mysql://localhost:3306/student
course.service.url=http://localhost:8000
```

### COURSE SERVICE
```python
# Django settings
DATABASES = {
    'default': {
        'ENGINE': 'django.db.backends.postgresql',
        'NAME': 'course_db',
        'HOST': 'localhost',
        'PORT': '5432',
    }
}
```

## Sécurité et CORS

- **CORS**: Configuré pour accepter les requêtes depuis `http://localhost:4200`
- **Validation**: Utilisation de Jakarta Validation pour valider les données
- **Gestion d'erreurs**: Codes de statut HTTP appropriés

## Monitoring et logs

- **Logs**: Affichage des requêtes SQL avec `spring.jpa.show-sql=true`
- **Health checks**: Endpoints de santé Spring Boot
- **Error handling**: Gestion centralisée des erreurs avec ResponseEntity

## Déploiement

### Développement local
1. Démarrer MySQL sur le port 3306
2. Démarrer le COURSE SERVICE sur le port 8000
3. Démarrer le Student Service sur le port 8081
4. Accéder à l'API via `http://localhost:8081`

### Production
- Utilisation de conteneurs Docker
- Configuration via variables d'environnement
- Load balancing pour la haute disponibilité

