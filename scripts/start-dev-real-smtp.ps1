param(
    [ValidateSet("microsoft", "google", "primary", "dual", "resend")]
    [string]$Provider = "microsoft",
    [string]$SmtpUsername,
    [System.Security.SecureString]$SmtpPassword,
    [string]$SmtpFrom,
    [string]$SmtpHost,
    [int]$SmtpPort = 587,
    [int]$SmtpTimeoutMs = 20000,
    [string]$MicrosoftSmtpUsername,
    [System.Security.SecureString]$MicrosoftSmtpPassword,
    [string]$MicrosoftSmtpFrom,
    [string]$GoogleSmtpUsername,
    [System.Security.SecureString]$GoogleSmtpPassword,
    [string]$GoogleSmtpFrom,
    [switch]$EnableExchangeSmtpAuth,
    [string]$ExchangeAdminUpn,
    [string]$ExchangeMailboxUpn,
    [switch]$EnableResendApi,
    [string]$ResendApiKey,
    [string]$ResendFrom,
    [switch]$PreferResendApi,
    [string]$ResendBaseUrl = "https://api.resend.com",
    [string]$ResendFallbackFrom = "onboarding@resend.dev",
    [string]$ResendReplyTo,
    [switch]$SkipClean,
    [switch]$UseHttps
)

$ErrorActionPreference = "Stop"

function Write-Step {
    param([string]$Message)
    Write-Host "[REAL-SMTP] $Message" -ForegroundColor Cyan
}

function Read-RequiredValue {
    param(
        [string]$CurrentValue,
        [string]$Prompt
    )

    if (-not [string]::IsNullOrWhiteSpace($CurrentValue)) {
        return $CurrentValue.Trim()
    }

    $inputValue = Read-Host $Prompt
    if ([string]::IsNullOrWhiteSpace($inputValue)) {
        throw "Valor requerido no proporcionado para: $Prompt"
    }

    return $inputValue.Trim()
}

function Convert-SecureToPlainText {
    param([System.Security.SecureString]$SecureValue)

    $bstr = [Runtime.InteropServices.Marshal]::SecureStringToBSTR($SecureValue)
    try {
        return [Runtime.InteropServices.Marshal]::PtrToStringBSTR($bstr)
    }
    finally {
        [Runtime.InteropServices.Marshal]::ZeroFreeBSTR($bstr)
    }
}

function Read-SecretValue {
    param(
        [System.Security.SecureString]$CurrentValue,
        [string]$Prompt = "Ingresa contrasena SMTP"
    )

    if ($null -ne $CurrentValue) {
        return Convert-SecureToPlainText -SecureValue $CurrentValue
    }

    $secure = Read-Host $Prompt -AsSecureString
    $plain = Convert-SecureToPlainText -SecureValue $secure
    if ([string]::IsNullOrWhiteSpace($plain)) {
        throw "No se proporciono contrasena SMTP para: $Prompt"
    }

    return $plain
}

function Set-ProcessEnv {
    param(
        [string]$Name,
        [string]$Value
    )

    [Environment]::SetEnvironmentVariable($Name, $Value, "Process")
}

function Set-MicrosoftProviderConfig {
    param(
        [string]$Username,
        [string]$SecretValue,
        [string]$From,
        [int]$Port,
        [int]$TimeoutMs
    )

    Set-ProcessEnv -Name "MAIL_MICROSOFT_ENABLED" -Value "true"
    Set-ProcessEnv -Name "MAIL_MICROSOFT_HOST" -Value "smtp.office365.com"
    Set-ProcessEnv -Name "MAIL_MICROSOFT_PORT" -Value $Port.ToString()
    Set-ProcessEnv -Name "MAIL_MICROSOFT_USERNAME" -Value $Username
    Set-ProcessEnv -Name "MAIL_MICROSOFT_PASSWORD" -Value $SecretValue
    Set-ProcessEnv -Name "MAIL_MICROSOFT_FROM" -Value $From
    Set-ProcessEnv -Name "MAIL_MICROSOFT_CONNECTION_TIMEOUT" -Value $TimeoutMs.ToString()
    Set-ProcessEnv -Name "MAIL_MICROSOFT_TIMEOUT" -Value $TimeoutMs.ToString()
    Set-ProcessEnv -Name "MAIL_MICROSOFT_WRITE_TIMEOUT" -Value $TimeoutMs.ToString()
}

function Set-GoogleProviderConfig {
    param(
        [string]$Username,
        [string]$SecretValue,
        [string]$From,
        [int]$Port,
        [int]$TimeoutMs
    )

    Set-ProcessEnv -Name "MAIL_GOOGLE_ENABLED" -Value "true"
    Set-ProcessEnv -Name "MAIL_GOOGLE_HOST" -Value "smtp.gmail.com"
    Set-ProcessEnv -Name "MAIL_GOOGLE_PORT" -Value $Port.ToString()
    Set-ProcessEnv -Name "MAIL_GOOGLE_USERNAME" -Value $Username
    Set-ProcessEnv -Name "MAIL_GOOGLE_PASSWORD" -Value $SecretValue
    Set-ProcessEnv -Name "MAIL_GOOGLE_FROM" -Value $From
    Set-ProcessEnv -Name "MAIL_GOOGLE_CONNECTION_TIMEOUT" -Value $TimeoutMs.ToString()
    Set-ProcessEnv -Name "MAIL_GOOGLE_TIMEOUT" -Value $TimeoutMs.ToString()
    Set-ProcessEnv -Name "MAIL_GOOGLE_WRITE_TIMEOUT" -Value $TimeoutMs.ToString()
}

function Set-PrimaryProviderConfig {
    param(
        [string]$HostName,
        [int]$Port,
        [string]$Username,
        [string]$SecretValue,
        [string]$From,
        [int]$TimeoutMs
    )

    Set-ProcessEnv -Name "MAIL_PRIMARY_ENABLED" -Value "true"
    Set-ProcessEnv -Name "MAIL_HOST" -Value $HostName
    Set-ProcessEnv -Name "MAIL_PORT" -Value $Port.ToString()
    Set-ProcessEnv -Name "MAIL_USERNAME" -Value $Username
    Set-ProcessEnv -Name "MAIL_PASSWORD" -Value $SecretValue
    Set-ProcessEnv -Name "EMAIL_FROM" -Value $From
    Set-ProcessEnv -Name "MAIL_SMTP_CONNECTION_TIMEOUT" -Value $TimeoutMs.ToString()
    Set-ProcessEnv -Name "MAIL_SMTP_TIMEOUT" -Value $TimeoutMs.ToString()
    Set-ProcessEnv -Name "MAIL_SMTP_WRITE_TIMEOUT" -Value $TimeoutMs.ToString()
}

function Reset-ProviderFlags {
    Set-ProcessEnv -Name "MAIL_MICROSOFT_ENABLED" -Value "false"
    Set-ProcessEnv -Name "MAIL_GOOGLE_ENABLED" -Value "false"
    Set-ProcessEnv -Name "MAIL_PRIMARY_ENABLED" -Value "false"
}

function Configure-ResendApi {
    param(
        [switch]$Enable,
        [string]$ApiKey,
        [string]$From,
        [switch]$PreferApi,
        [string]$BaseUrl,
        [string]$FallbackFrom,
        [string]$ReplyTo
    )

    Set-ProcessEnv -Name "RESEND_ENABLED" -Value ($(if ($Enable) { "true" } else { "false" }))
    Set-ProcessEnv -Name "RESEND_PREFER_API" -Value ($(if ($PreferApi) { "true" } else { "false" }))
    Set-ProcessEnv -Name "RESEND_BASE_URL" -Value $BaseUrl
    Set-ProcessEnv -Name "RESEND_FALLBACK_FROM" -Value $FallbackFrom
    Set-ProcessEnv -Name "RESEND_REPLY_TO" -Value $ReplyTo

    if (-not $Enable) {
        return
    }

    $resolvedApiKey = Read-RequiredValue -CurrentValue $ApiKey -Prompt "Ingresa RESEND_API_KEY"
    $resolvedFrom = $From
    if ([string]::IsNullOrWhiteSpace($resolvedFrom)) {
        $resolvedFrom = $FallbackFrom
        Write-Step "RESEND_FROM no fue proporcionado, se usara fallback sender: $resolvedFrom"
    }
    if ([string]::IsNullOrWhiteSpace($resolvedFrom)) {
        throw "Debes proporcionar RESEND_FROM o RESEND_FALLBACK_FROM"
    }

    Set-ProcessEnv -Name "RESEND_API_KEY" -Value $resolvedApiKey
    Set-ProcessEnv -Name "RESEND_FROM" -Value $resolvedFrom

    Write-Step "Resend API habilitada (prefer-api=$PreferApi, from=$resolvedFrom, fallback=$FallbackFrom)"
}

function Ensure-ExchangeOnlineModule {
    $module = Get-Module -ListAvailable -Name ExchangeOnlineManagement
    if ($null -ne $module) {
        return
    }

    Write-Step "Instalando modulo ExchangeOnlineManagement"
    try {
        Set-PSRepository -Name PSGallery -InstallationPolicy Trusted -ErrorAction SilentlyContinue
    }
    catch {
        Write-Step "No fue posible cambiar InstallationPolicy de PSGallery automaticamente"
    }

    Install-Module ExchangeOnlineManagement -Scope CurrentUser -Force -AllowClobber
}

function Enable-ExchangeSmtpAuth {
    param(
        [string]$AdminUpn,
        [string]$MailboxUpn
    )

    if ([string]::IsNullOrWhiteSpace($MailboxUpn)) {
        throw "Debes proporcionar un mailbox para habilitar SMTP AUTH en Exchange"
    }

    Ensure-ExchangeOnlineModule
    Import-Module ExchangeOnlineManagement -Force

    Write-Step "Conectando a Exchange Online"
    if ([string]::IsNullOrWhiteSpace($AdminUpn)) {
        Connect-ExchangeOnline -ShowBanner:$false
    }
    else {
        Connect-ExchangeOnline -ShowBanner:$false -UserPrincipalName $AdminUpn
    }

    try {
        $tenantBefore = Get-TransportConfig | Select-Object -ExpandProperty SmtpClientAuthenticationDisabled
        Write-Step "Tenant SmtpClientAuthenticationDisabled(before)=$tenantBefore"
        if ($tenantBefore -ne $false) {
            Set-TransportConfig -SmtpClientAuthenticationDisabled $false
        }

        $mailboxBefore = Get-CASMailbox -Identity $MailboxUpn | Select-Object -ExpandProperty SmtpClientAuthenticationDisabled
        Write-Step "Mailbox $MailboxUpn SmtpClientAuthenticationDisabled(before)=$mailboxBefore"
        if ($mailboxBefore -ne $false) {
            Set-CASMailbox -Identity $MailboxUpn -SmtpClientAuthenticationDisabled $false
        }

        $tenantAfter = Get-TransportConfig | Select-Object -ExpandProperty SmtpClientAuthenticationDisabled
        $mailboxAfter = Get-CASMailbox -Identity $MailboxUpn | Select-Object -ExpandProperty SmtpClientAuthenticationDisabled

        Write-Step "Tenant SmtpClientAuthenticationDisabled(after)=$tenantAfter"
        Write-Step "Mailbox $MailboxUpn SmtpClientAuthenticationDisabled(after)=$mailboxAfter"

        if ($tenantAfter -ne $false -or $mailboxAfter -ne $false) {
            throw "No fue posible habilitar SMTP AUTH completamente en Exchange"
        }
    }
    finally {
        Disconnect-ExchangeOnline -Confirm:$false -ErrorAction SilentlyContinue | Out-Null
    }
}

function Start-ApiWithRealSmtp {
    param(
        [switch]$SkipCleanBuild,
        [switch]$UseHttpsProfile
    )

    if ($UseHttpsProfile) {
        Write-Step "Iniciando backend HTTPS (perfil dev) con SMTP real"
    }
    else {
        Write-Step "Iniciando backend dev-http con SMTP real"
    }
    $runScript = Join-Path (Resolve-Path (Join-Path $PSScriptRoot "..")).Path "run-dev.ps1"

    $runArgs = @()
    if ($SkipCleanBuild) {
        $runArgs += "-SkipClean"
    }
    if ($UseHttpsProfile) {
        $runArgs += "-UseHttps"
    }

    & $runScript @runArgs
}

Set-ProcessEnv -Name "EMAIL_MOCK_ENABLED" -Value "false"
Set-ProcessEnv -Name "MAIL_PROVIDER_ROUTING_ENABLED" -Value "true"
$effectiveEnableResendApi = $EnableResendApi -or ($Provider -eq "resend")
$effectivePreferResendApi = $PreferResendApi -or ($Provider -eq "resend")
Configure-ResendApi -Enable:$effectiveEnableResendApi -ApiKey $ResendApiKey -From $ResendFrom -PreferApi:$effectivePreferResendApi -BaseUrl $ResendBaseUrl -FallbackFrom $ResendFallbackFrom -ReplyTo $ResendReplyTo
Reset-ProviderFlags

$resolvedDefaultUsername = $null
$resolvedDefaultSecret = $null

if ($Provider -ne "dual" -and $Provider -ne "resend") {
    $resolvedDefaultUsername = Read-RequiredValue -CurrentValue $SmtpUsername -Prompt "Ingresa usuario SMTP"
    $resolvedDefaultSecret = Read-SecretValue -CurrentValue $SmtpPassword -Prompt "Ingresa contrasena SMTP"
}

$resolvedMailbox = $ExchangeMailboxUpn

switch ($Provider) {
    "microsoft" {
        $resolvedUsername = Read-RequiredValue -CurrentValue $resolvedDefaultUsername -Prompt "Ingresa usuario SMTP Microsoft"
        $resolvedSecret = Read-SecretValue -CurrentValue $SmtpPassword -Prompt "Ingresa contrasena SMTP Microsoft"
        $resolvedFrom = if ([string]::IsNullOrWhiteSpace($SmtpFrom)) { $resolvedUsername } else { $SmtpFrom.Trim() }
        Set-MicrosoftProviderConfig -Username $resolvedUsername -SecretValue $resolvedSecret -From $resolvedFrom -Port $SmtpPort -TimeoutMs $SmtpTimeoutMs
        Set-ProcessEnv -Name "MAIL_PRIMARY_ENABLED" -Value "false"
        if ([string]::IsNullOrWhiteSpace($resolvedMailbox)) {
            $resolvedMailbox = $resolvedUsername
        }
        Write-Step "Proveedor SMTP configurado: Microsoft 365"
    }
    "google" {
        $resolvedUsername = Read-RequiredValue -CurrentValue $resolvedDefaultUsername -Prompt "Ingresa usuario SMTP Google"
        $resolvedSecret = Read-SecretValue -CurrentValue $SmtpPassword -Prompt "Ingresa contrasena SMTP Google"
        $resolvedFrom = if ([string]::IsNullOrWhiteSpace($SmtpFrom)) { $resolvedUsername } else { $SmtpFrom.Trim() }
        Set-GoogleProviderConfig -Username $resolvedUsername -SecretValue $resolvedSecret -From $resolvedFrom -Port $SmtpPort -TimeoutMs $SmtpTimeoutMs
        Set-ProcessEnv -Name "MAIL_PRIMARY_ENABLED" -Value "false"
        Write-Step "Proveedor SMTP configurado: Gmail"
    }
    "primary" {
        Set-ProcessEnv -Name "MAIL_PRIMARY_ENABLED" -Value "true"
        $resolvedHost = Read-RequiredValue -CurrentValue $SmtpHost -Prompt "Ingresa host SMTP primario"
        $resolvedUsername = Read-RequiredValue -CurrentValue $resolvedDefaultUsername -Prompt "Ingresa usuario SMTP primario"
        $resolvedSecret = Read-SecretValue -CurrentValue $SmtpPassword -Prompt "Ingresa contrasena SMTP primario"
        $resolvedFrom = if ([string]::IsNullOrWhiteSpace($SmtpFrom)) { $resolvedUsername } else { $SmtpFrom.Trim() }
        Set-PrimaryProviderConfig -HostName $resolvedHost -Port $SmtpPort -Username $resolvedUsername -SecretValue $resolvedSecret -From $resolvedFrom -TimeoutMs $SmtpTimeoutMs
        Write-Step "Proveedor SMTP configurado: primario"
    }
    "dual" {
        $resolvedMicrosoftUsername = Read-RequiredValue -CurrentValue $(
            if ([string]::IsNullOrWhiteSpace($MicrosoftSmtpUsername)) { $SmtpUsername } else { $MicrosoftSmtpUsername }
        ) -Prompt "Ingresa usuario SMTP Microsoft"
        $resolvedMicrosoftSecret = Read-SecretValue -CurrentValue $(
            if ($null -ne $MicrosoftSmtpPassword) { $MicrosoftSmtpPassword } else { $SmtpPassword }
        ) -Prompt "Ingresa contrasena SMTP Microsoft"
        $resolvedMicrosoftFrom = if ([string]::IsNullOrWhiteSpace($MicrosoftSmtpFrom)) {
            $resolvedMicrosoftUsername
        }
        else {
            $MicrosoftSmtpFrom.Trim()
        }

        $resolvedGoogleUsername = Read-RequiredValue -CurrentValue $GoogleSmtpUsername -Prompt "Ingresa usuario SMTP Google"
        $resolvedGoogleSecret = Read-SecretValue -CurrentValue $GoogleSmtpPassword -Prompt "Ingresa contrasena SMTP Google"
        $resolvedGoogleFrom = if ([string]::IsNullOrWhiteSpace($GoogleSmtpFrom)) {
            $resolvedGoogleUsername
        }
        else {
            $GoogleSmtpFrom.Trim()
        }

        Set-MicrosoftProviderConfig -Username $resolvedMicrosoftUsername -SecretValue $resolvedMicrosoftSecret -From $resolvedMicrosoftFrom -Port $SmtpPort -TimeoutMs $SmtpTimeoutMs
        Set-GoogleProviderConfig -Username $resolvedGoogleUsername -SecretValue $resolvedGoogleSecret -From $resolvedGoogleFrom -Port $SmtpPort -TimeoutMs $SmtpTimeoutMs
        Set-ProcessEnv -Name "MAIL_PRIMARY_ENABLED" -Value "false"
        if ([string]::IsNullOrWhiteSpace($resolvedMailbox)) {
            $resolvedMailbox = $resolvedMicrosoftUsername
        }
        Write-Step "Proveedores SMTP configurados: Microsoft 365 + Gmail"
    }
    "resend" {
        if (-not $effectiveEnableResendApi) {
            throw "Provider=resend requiere EnableResendApi=true o configuracion equivalente"
        }
        Set-ProcessEnv -Name "MAIL_PRIMARY_ENABLED" -Value "false"
        Write-Step "Proveedor configurado: Resend API (sin SMTP)"
    }
}

if ($EnableExchangeSmtpAuth) {
    Enable-ExchangeSmtpAuth -AdminUpn $ExchangeAdminUpn -MailboxUpn $resolvedMailbox
}

Write-Step "EMAIL_MOCK_ENABLED=false aplicado"
Write-Step "Timeout SMTP aplicado (ms): $SmtpTimeoutMs"

Start-ApiWithRealSmtp -SkipCleanBuild:$SkipClean -UseHttpsProfile:$UseHttps
