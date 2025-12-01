# Test du Student Service Ultra-Simplifié
Write-Host "🧪 Test du Student Service Ultra-Simplifié" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green

$BASE_URL = "http://localhost:8081"

Write-Host "📋 Test 1: Vérifier que l'application répond" -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$BASE_URL/api/students" -Method GET
    Write-Host "✅ Service disponible" -ForegroundColor Green
} catch {
    Write-Host "❌ Service non disponible - vérifiez que l'application est démarrée" -ForegroundColor Red
}

Write-Host "`n📚 Test 2: Obtenir tous les cours disponibles (via microservice)" -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$BASE_URL/api/students/courses/available" -Method GET
    Write-Host "✅ Cours récupérés" -ForegroundColor Green
} catch {
    Write-Host "❌ Microservice COURSE SERVICE non disponible" -ForegroundColor Red
}

Write-Host "`n👤 Test 3: Créer un étudiant" -ForegroundColor Yellow
$studentData = @{
    firstName = "John"
    lastName = "Doe"
    email = "john.doe@email.com"
    university = @{
        id = 1
    }
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "$BASE_URL/api/students" -Method POST -Body $studentData -ContentType "application/json"
    Write-Host "✅ Étudiant créé" -ForegroundColor Green
} catch {
    Write-Host "❌ Erreur lors de la création de l'étudiant" -ForegroundColor Red
}

Write-Host "`n🔍 Test 4: Rechercher des étudiants par nom" -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$BASE_URL/api/students/search?name=John" -Method GET
    Write-Host "✅ Recherche réussie" -ForegroundColor Green
} catch {
    Write-Host "❌ Aucun étudiant trouvé" -ForegroundColor Red
}

Write-Host "`n✅ Tests terminés!" -ForegroundColor Green
Write-Host "Note: Pour les tests de cours, assurez-vous que le microservice COURSE SERVICE est démarré sur le port 8000" -ForegroundColor Cyan

