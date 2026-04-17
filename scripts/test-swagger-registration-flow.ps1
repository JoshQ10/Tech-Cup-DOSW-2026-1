param(
    [string]$BaseUrl = "https://localhost:8443",
    [string]$MicrosoftEmail = "juan.bohorquez-m@mail.escuelaing.edu.co",
    [string]$GoogleEmail = "juancarlos12023@gmail.com",
    [string]$Password = "Aa123456!",
    [switch]$SkipTlsValidation
)

$ErrorActionPreference = "Stop"

if ($SkipTlsValidation -and $PSVersionTable.PSVersion.Major -lt 7) {
    [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.SecurityProtocolType]::Tls12
    [System.Net.ServicePointManager]::ServerCertificateValidationCallback = { $true }
}

function Write-Step {
    param([string]$Message)
    Write-Host "[SWAGGER-FLOW] $Message" -ForegroundColor Cyan
}

function Invoke-ApiJson {
    param(
        [string]$Method,
        [string]$Url,
        [object]$Body
    )

    $jsonBody = $null
    if ($null -ne $Body) {
        $jsonBody = $Body | ConvertTo-Json -Depth 10
    }

    try {
        if ($null -eq $jsonBody) {
            if ($SkipTlsValidation -and $PSVersionTable.PSVersion.Major -ge 7) {
                $response = Invoke-WebRequest -Uri $Url -Method $Method -UseBasicParsing -TimeoutSec 120 -SkipCertificateCheck
            }
            else {
                $response = Invoke-WebRequest -Uri $Url -Method $Method -UseBasicParsing -TimeoutSec 120
            }
        }
        else {
            if ($SkipTlsValidation -and $PSVersionTable.PSVersion.Major -ge 7) {
                $response = Invoke-WebRequest -Uri $Url -Method $Method -UseBasicParsing -ContentType "application/json" -Body $jsonBody -TimeoutSec 120 -SkipCertificateCheck
            }
            else {
                $response = Invoke-WebRequest -Uri $Url -Method $Method -UseBasicParsing -ContentType "application/json" -Body $jsonBody -TimeoutSec 120
            }
        }

        return [ordered]@{
            Status = [int]$response.StatusCode
            Body   = $response.Content
        }
    }
    catch {
        $resp = $_.Exception.Response
        if ($null -eq $resp) {
            return [ordered]@{
                Status = -1
                Body   = $_.Exception.Message
            }
        }

        $reader = New-Object IO.StreamReader($resp.GetResponseStream())
        $content = $reader.ReadToEnd()
        $reader.Close()

        return [ordered]@{
            Status = [int]$resp.StatusCode.value__
            Body   = $content
        }
    }
}

function Test-RegisterAndQuery {
    param(
        [string]$Email,
        [string]$Label
    )

    $suffix = [DateTimeOffset]::UtcNow.ToUnixTimeSeconds()
    $username = "{0}.{1}" -f ($Label.ToLowerInvariant()), $suffix

    $registerPayload = [ordered]@{
        firstName       = "Flow"
        lastName        = "SMTP"
        username        = $username
        email           = $Email
        password        = $Password
        confirmPassword = $Password
        userType        = "INTERNAL"
        role            = "PLAYER"
    }

    Write-Step "Registrando usuario para ${Label}: $Email"
    $registerResult = Invoke-ApiJson -Method "POST" -Url "$BaseUrl/api/auth/register" -Body $registerPayload

    Write-Step "Consultando estado de usuario en endpoint dev para $Label"
    $statusResult = Invoke-ApiJson -Method "GET" -Url "$BaseUrl/api/auth/dev/user-status?email=$Email" -Body $null

    return [ordered]@{
        label           = $Label
        email           = $Email
        username        = $username
        registerStatus  = $registerResult.Status
        registerBody    = $registerResult.Body
        userStatusHttp  = $statusResult.Status
        userStatusBody  = $statusResult.Body
    }
}

Write-Step "Validando disponibilidad de Swagger"
$swagger = Invoke-ApiJson -Method "GET" -Url "$BaseUrl/swagger-ui.html" -Body $null
Write-Host ("SWAGGER_STATUS=" + $swagger.Status)

$results = @()
$results += Test-RegisterAndQuery -Email $MicrosoftEmail -Label "MICROSOFT"
$results += Test-RegisterAndQuery -Email $GoogleEmail -Label "GOOGLE"

Write-Host ""
Write-Host "===== RESUMEN FLUJO =====" -ForegroundColor Yellow
foreach ($item in $results) {
    Write-Host ("[{0}] email={1}" -f $item.label, $item.email)
    Write-Host ("  registerStatus={0}" -f $item.registerStatus)
    Write-Host ("  userStatusHttp={0}" -f $item.userStatusHttp)
}

Write-Host ""
Write-Host "===== DETALLE JSON =====" -ForegroundColor Yellow
$results | ConvertTo-Json -Depth 10
