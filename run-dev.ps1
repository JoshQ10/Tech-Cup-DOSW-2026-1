# TechCup Backend - Development startup (HTTP)
# 1) Free port 8080 if needed
# 2) Clean and compile
# 3) (Optional) run tests
# 4) Run Spring Boot with dev-http profile

param(
    [switch]$RunTests,
    [switch]$SkipClean,
    [switch]$UseHttps,
    [switch]$RealEmail,
    [switch]$MockEmail
)

$springProfile = "dev-http"
$targetPort = if ($UseHttps) { 8443 } else { 8080 }
$swaggerUrl = if ($UseHttps) { "https://localhost:8443/swagger-ui.html" } else { "http://localhost:8080/swagger-ui.html" }

if ($UseHttps) {
    # Run on dev-http profile and override server SSL to avoid Flyway/H2 incompatibilities in dev profile.
    [Environment]::SetEnvironmentVariable("SERVER_PORT", "8443", "Process")
    [Environment]::SetEnvironmentVariable("SERVER_SSL_ENABLED", "true", "Process")
    [Environment]::SetEnvironmentVariable("SERVER_SSL_KEY_STORE", "classpath:keystore.p12", "Process")
    [Environment]::SetEnvironmentVariable("SERVER_SSL_KEY_STORE_PASSWORD", "password", "Process")
    [Environment]::SetEnvironmentVariable("SERVER_SSL_KEY_STORE_TYPE", "PKCS12", "Process")
    [Environment]::SetEnvironmentVariable("SERVER_SSL_KEY_ALIAS", "techcup", "Process")
    [Environment]::SetEnvironmentVariable("APP_SECURITY_REQUIRE_SSL", "true", "Process")
}

function Get-EnvValue {
    param([string]$Name)

    $value = [Environment]::GetEnvironmentVariable($Name, "Process")
    if ($null -eq $value) {
        return ""
    }

    return $value.Trim()
}

function Test-Truthy {
    param([string]$Value)

    if ([string]::IsNullOrWhiteSpace($Value)) {
        return $false
    }

    switch ($Value.Trim().ToLowerInvariant()) {
        "1" { return $true }
        "true" { return $true }
        "yes" { return $true }
        "y" { return $true }
        "on" { return $true }
        default { return $false }
    }
}

function Import-DotEnvIfAvailable {
    $envFile = Join-Path $PSScriptRoot ".env"
    if (-not (Test-Path $envFile)) {
        return
    }

    foreach ($line in Get-Content $envFile) {
        if ([string]::IsNullOrWhiteSpace($line)) {
            continue
        }

        $trimmed = $line.Trim()
        if ($trimmed.StartsWith("#")) {
            continue
        }

        $separatorIndex = $trimmed.IndexOf("=")
        if ($separatorIndex -le 0) {
            continue
        }

        $key = $trimmed.Substring(0, $separatorIndex).Trim()
        $value = $trimmed.Substring($separatorIndex + 1).Trim()

        if ($value.StartsWith('"') -and $value.EndsWith('"') -and $value.Length -ge 2) {
            $value = $value.Substring(1, $value.Length - 2)
        }

        if ([string]::IsNullOrWhiteSpace([Environment]::GetEnvironmentVariable($key, "Process"))) {
            [Environment]::SetEnvironmentVariable($key, $value, "Process")
        }
    }

    Write-Host "Variables cargadas desde .env (solo faltantes en la sesion actual)." -ForegroundColor DarkGray
}

function Test-EmailProviderConfigured {
    $mailHost = Get-EnvValue "MAIL_HOST"
    $mailUsername = Get-EnvValue "MAIL_USERNAME"
    $mailPassword = Get-EnvValue "MAIL_PASSWORD"
    $mailAuthRaw = Get-EnvValue "MAIL_SMTP_AUTH"
    $mailAuthEnabled = $true
    if (-not [string]::IsNullOrWhiteSpace($mailAuthRaw)) {
        $mailAuthEnabled = Test-Truthy $mailAuthRaw
    }
    $primaryConfigured = -not [string]::IsNullOrWhiteSpace($mailHost)
    if ($primaryConfigured -and $mailAuthEnabled) {
        $primaryConfigured = -not [string]::IsNullOrWhiteSpace($mailUsername) -and -not [string]::IsNullOrWhiteSpace($mailPassword)
    }

    $microsoftEnabled = Test-Truthy (Get-EnvValue "MAIL_MICROSOFT_ENABLED")
    $microsoftConfigured = $microsoftEnabled -and
        -not [string]::IsNullOrWhiteSpace((Get-EnvValue "MAIL_MICROSOFT_USERNAME")) -and
        -not [string]::IsNullOrWhiteSpace((Get-EnvValue "MAIL_MICROSOFT_PASSWORD"))

    $googleEnabled = Test-Truthy (Get-EnvValue "MAIL_GOOGLE_ENABLED")
    $googleConfigured = $googleEnabled -and
        -not [string]::IsNullOrWhiteSpace((Get-EnvValue "MAIL_GOOGLE_USERNAME")) -and
        -not [string]::IsNullOrWhiteSpace((Get-EnvValue "MAIL_GOOGLE_PASSWORD"))

    $resendConfigured = Test-Truthy (Get-EnvValue "RESEND_ENABLED") -and
        -not [string]::IsNullOrWhiteSpace((Get-EnvValue "RESEND_API_KEY")) -and
        -not [string]::IsNullOrWhiteSpace((Get-EnvValue "RESEND_FROM"))

    return $primaryConfigured -or $microsoftConfigured -or $googleConfigured -or $resendConfigured
}

if ($RealEmail -and $MockEmail) {
    Write-Host "No puedes usar -RealEmail y -MockEmail al mismo tiempo." -ForegroundColor Red
    exit 1
}

Import-DotEnvIfAvailable

$emailMockFlag = Get-EnvValue "EMAIL_MOCK_ENABLED"
if ($MockEmail) {
    [Environment]::SetEnvironmentVariable("EMAIL_MOCK_ENABLED", "true", "Process")
    Write-Host "EMAIL_MOCK_ENABLED=true (forzado por -MockEmail)." -ForegroundColor Yellow
}
elseif ($RealEmail) {
    [Environment]::SetEnvironmentVariable("EMAIL_MOCK_ENABLED", "false", "Process")
    Write-Host "EMAIL_MOCK_ENABLED=false (forzado por -RealEmail)." -ForegroundColor Green
}
elseif ([string]::IsNullOrWhiteSpace($emailMockFlag)) {
    if (Test-EmailProviderConfigured) {
        [Environment]::SetEnvironmentVariable("EMAIL_MOCK_ENABLED", "false", "Process")
        Write-Host "EMAIL_MOCK_ENABLED not set. SMTP/Resend configurado, se activa envio real." -ForegroundColor Green
    }
    else {
        # For local development, avoid registration failures when SMTP providers are not configured.
        [Environment]::SetEnvironmentVariable("EMAIL_MOCK_ENABLED", "true", "Process")
        Write-Host "EMAIL_MOCK_ENABLED not set y sin SMTP/Resend configurado. Se mantiene mock local." -ForegroundColor Yellow
    }
}

Write-Host ""
Write-Host "TechCup Backend - Development Server" -ForegroundColor Cyan
Write-Host ""

Write-Host "[0/4] Checking port $targetPort..." -ForegroundColor Yellow
$portOwners = Get-NetTCPConnection -LocalPort $targetPort -State Listen -ErrorAction SilentlyContinue |
    Select-Object -ExpandProperty OwningProcess -Unique

if ($portOwners) {
    foreach ($ownerProcess in $portOwners) {
        Write-Host "Port $targetPort in use by process $ownerProcess. Stopping process..." -ForegroundColor Yellow
        Stop-Process -Id $ownerProcess -Force -ErrorAction SilentlyContinue
    }
    Write-Host "Port $targetPort released." -ForegroundColor Green
} else {
    Write-Host "Port $targetPort is available." -ForegroundColor Green
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
if ($UseHttps) {
    Write-Host "Spring profile: dev-http (SSL override enabled)" -ForegroundColor Green
}
else {
    Write-Host "Spring profile: $springProfile" -ForegroundColor Green
}
Write-Host "Open Swagger at: $swaggerUrl" -ForegroundColor Green
if ($UseHttps) {
    Write-Host "If browser shows certificate warning, accept local self-signed certificate for development." -ForegroundColor Yellow
}
Write-Host "EMAIL_MOCK_ENABLED=$([Environment]::GetEnvironmentVariable('EMAIL_MOCK_ENABLED', 'Process'))" -ForegroundColor Green
Write-Host "Press Ctrl+C to stop the server." -ForegroundColor Green

& .\mvnw.cmd "-Dmaven.test.skip=true" "-Dspring-boot.run.profiles=dev-http" "spring-boot:run"
