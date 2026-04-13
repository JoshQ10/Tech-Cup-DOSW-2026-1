# TechCup Backend - Development startup (HTTP)
# 1) Free port 8080 if needed
# 2) Clean and compile
# 3) (Optional) run tests
# 4) Run Spring Boot with dev-http profile

param(
    [switch]$RunTests,
    [switch]$SkipClean
)

Write-Host ""
Write-Host "TechCup Backend - Development Server" -ForegroundColor Cyan
Write-Host ""

Write-Host "[0/4] Checking port 8080..." -ForegroundColor Yellow
$portOwners = Get-NetTCPConnection -LocalPort 8080 -State Listen -ErrorAction SilentlyContinue |
    Select-Object -ExpandProperty OwningProcess -Unique

if ($portOwners) {
    foreach ($ownerProcess in $portOwners) {
        Write-Host "Port 8080 in use by process $ownerProcess. Stopping process..." -ForegroundColor Yellow
        Stop-Process -Id $ownerProcess -Force -ErrorAction SilentlyContinue
    }
    Write-Host "Port 8080 released." -ForegroundColor Green
} else {
    Write-Host "Port 8080 is available." -ForegroundColor Green
}

if (-not $SkipClean) {
    Write-Host ""
    Write-Host "[1/4] Cleaning previous build..." -ForegroundColor Yellow
    & .\mvnw.cmd clean -q -DskipTests
    if ($LASTEXITCODE -ne 0) {
        Write-Host "Clean failed." -ForegroundColor Red
        exit 1
    }
} else {
    Write-Host ""
    Write-Host "[1/4] Skipping clean (requested)." -ForegroundColor Yellow
}

Write-Host "[2/4] Compiling project..." -ForegroundColor Yellow
& .\mvnw.cmd compile -q -DskipTests
if ($LASTEXITCODE -ne 0) {
    Write-Host "Compile failed." -ForegroundColor Red
    exit 1
}

if ($RunTests) {
    Write-Host "[3/4] Running tests..." -ForegroundColor Yellow
    & .\mvnw.cmd test
    if ($LASTEXITCODE -ne 0) {
        Write-Host "Tests failed." -ForegroundColor Red
        exit 1
    }
} else {
    Write-Host "[3/4] Skipping tests (use -RunTests to enable)." -ForegroundColor Yellow
}

Write-Host ""
Write-Host "[4/4] Starting server..." -ForegroundColor Yellow
Write-Host "Open Swagger at: http://localhost:8080/swagger-ui.html" -ForegroundColor Green
Write-Host "Press Ctrl+C to stop the server." -ForegroundColor Green

& .\mvnw.cmd "-Dmaven.test.skip=true" "-Dspring-boot.run.profiles=dev-http" "spring-boot:run"
