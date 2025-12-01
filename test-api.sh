#!/bin/bash

# Script de test pour l'API Student Service avec appels HTTP directs
# Assurez-vous que le service est démarré sur le port 8081
# Assurez-vous que le microservice COURSE SERVICE est démarré sur le port 8000

BASE_URL="http://localhost:8081"
COURSE_SERVICE_URL="http://localhost:8000"

echo "🧪 Test de l'API Student Service avec appels HTTP directs"
echo "========================================================"

# Test 1: Lister tous les étudiants
echo "📋 Test 1: Lister tous les étudiants"
curl -s -X GET "$BASE_URL/api/students" | jq '.' || echo "Erreur: Service non disponible"

echo -e "\n"

# Test 2: Vérifier la connectivité avec le microservice COURSE SERVICE
echo "🔗 Test 2: Vérifier la connectivité avec le microservice COURSE SERVICE"
curl -s -X GET "$COURSE_SERVICE_URL/api/courses" | jq '.' || echo "Erreur: Microservice COURSE SERVICE non disponible"

echo -e "\n"

# Test 3: Obtenir tous les cours disponibles (via microservice)
echo "📚 Test 3: Obtenir tous les cours disponibles (via microservice)"
curl -s -X GET "$BASE_URL/api/students/courses/available" | jq '.' || echo "Aucun cours trouvé"

echo -e "\n"

# Test 4: Créer un étudiant
echo "👤 Test 4: Créer un étudiant"
STUDENT_RESPONSE=$(curl -s -X POST "$BASE_URL/api/students" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@email.com",
    "university": {
      "id": 1
    }
  }')

echo "$STUDENT_RESPONSE" | jq '.' || echo "Erreur lors de la création de l'étudiant"

echo -e "\n"

# Test 5: Rechercher des cours par nom (via microservice)
echo "🔍 Test 5: Rechercher des cours par nom (via microservice)"
curl -s -X GET "$BASE_URL/api/students/courses/search?name=Mathematics" | jq '.' || echo "Aucun cours trouvé"

echo -e "\n"

# Test 6: Rechercher des étudiants par nom
echo "🔍 Test 6: Rechercher des étudiants par nom"
curl -s -X GET "$BASE_URL/api/students/search?name=John" | jq '.' || echo "Aucun étudiant trouvé"

echo -e "\n"

# Test 7: Rechercher des étudiants par cours (via microservice)
echo "🔍 Test 7: Rechercher des étudiants par cours (via microservice)"
curl -s -X GET "$BASE_URL/api/students/search/course?courseName=Mathematics" | jq '.' || echo "Aucun étudiant trouvé"

echo -e "\n"

# Test 8: Obtenir les cours d'un étudiant (via microservice)
echo "📚 Test 8: Obtenir les cours d'un étudiant (via microservice)"
curl -s -X GET "$BASE_URL/api/students/1/courses" | jq '.' || echo "Aucun cours trouvé pour cet étudiant"

echo -e "\n"

echo "✅ Tests terminés!"
echo "Note: Les tests de cours nécessitent que le microservice COURSE SERVICE soit démarré sur le port 8000"
echo "Pour plus d'informations, consultez la documentation dans README.md"
