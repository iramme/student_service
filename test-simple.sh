#!/bin/bash

echo "🧪 Test du Student Service Ultra-Simplifié"
echo "========================================"

BASE_URL="http://localhost:8081"

echo "📋 Test 1: Vérifier que l'application répond"
curl -s -X GET "$BASE_URL/api/students" || echo "❌ Service non disponible - vérifiez que l'application est démarrée"

echo -e "\n"

echo "📚 Test 2: Obtenir tous les cours disponibles (via microservice)"
curl -s -X GET "$BASE_URL/api/students/courses/available" || echo "❌ Microservice COURSE SERVICE non disponible"

echo -e "\n"

echo "👤 Test 3: Créer un étudiant"
curl -s -X POST "$BASE_URL/api/students" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@email.com",
    "university": {
      "id": 1
    }
  }' || echo "❌ Erreur lors de la création de l'étudiant"

echo -e "\n"

echo "🔍 Test 4: Rechercher des étudiants par nom"
curl -s -X GET "$BASE_URL/api/students/search?name=John" || echo "❌ Aucun étudiant trouvé"

echo -e "\n"

echo "✅ Tests terminés!"
echo "Note: Pour les tests de cours, assurez-vous que le microservice COURSE SERVICE est démarré sur le port 8000"

