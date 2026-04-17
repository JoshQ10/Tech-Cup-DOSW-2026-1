param(
    [string]$MailboxUpn,
    [string]$AdminUpn,
    [string]$WorkspacePath = (Resolve-Path (Join-Path $PSScriptRoot "..")),
    [string]$TestRecipient,
    [switch]$SkipExchange,
    [switch]$SkipSmtpSendTest,
    [switch]$SkipRestart,
    [switch]$SkipClean
)

$ErrorActionPreference = "Stop"

function Write-Step {
    param([string]$Message)
    Write-Host "[SMTP-FIX] $Message" -ForegroundColor Cyan
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
        default { return $false }
    }
}

function Test-Placeholder {
    param([string]$Value)
    if ([string]::IsNullOrWhiteSpace($Value)) {
        return $true
    }

    $v = $Value.Trim().ToLowerInvariant()
    return $v.StartsWith("tu-") -or $v.Contains("example") -or $v.Contains("placeholder")
}

function Read-DotEnv {
    param([string]$FilePath)

    if (-not (Test-Path $FilePath)) {
        return
    }

    foreach ($line in Get-Content $FilePath) {
        if ([string]::IsNullOrWhiteSpace($line)) {
            continue
        }
        if ($line.TrimStart().StartsWith("#")) {
            continue
        }

        $eqIndex = $line.IndexOf("=")
        if ($eqIndex -le 0) {
            continue
        }

        $key = $line.Substring(0, $eqIndex).Trim()
        $value = $line.Substring($eqIndex + 1).Trim()

        if ($value.StartsWith('"') -and $value.EndsWith('"')) {
            $value = $value.Substring(1, $value.Length - 2)
        }

        if ([string]::IsNullOrWhiteSpace([Environment]::GetEnvironmentVariable($key, "Process"))) {
            [Environment]::SetEnvironmentVariable($key, $value, "Process")
        }
    }
}

function Get-EnvValue {
    param(
        [string]$Name,
        [string]$Default = ""
    )

    $value = [Environment]::GetEnvironmentVariable($Name, "Process")
    if ([string]::IsNullOrWhiteSpace($value)) {
        return $Default
    }

    return $value
}

function Set-ExchangeSmtpAuthEnabled {
    param(
        [string]$Admin,
        [string]$Mailbox
    )

    Write-Step "Importando ExchangeOnlineManagement"
    Import-Module ExchangeOnlineManagement -Force

    Write-Step "Conectando a Exchange Online"
    if ([string]::IsNullOrWhiteSpace($Admin)) {
        Connect-ExchangeOnline -ShowBanner:$false
    }
    else {
        Connect-ExchangeOnline -ShowBanner:$false -UserPrincipalName $Admin
    }

    try {
        $tenantBefore = Get-TransportConfig | Select-Object -ExpandProperty SmtpClientAuthenticationDisabled
        Write-Step "Tenant SmtpClientAuthenticationDisabled(before)=$tenantBefore"

        if ($tenantBefore -ne $false) {
            Write-Step "Habilitando SMTP AUTH a nivel tenant"
            Set-TransportConfig -SmtpClientAuthenticationDisabled $false
        }

        $mailboxBefore = Get-CASMailbox -Identity $Mailbox | Select-Object -ExpandProperty SmtpClientAuthenticationDisabled
        Write-Step "Mailbox $Mailbox SmtpClientAuthenticationDisabled(before)=$mailboxBefore"

        if ($mailboxBefore -ne $false) {
            Write-Step "Habilitando SMTP AUTH en mailbox $Mailbox"
            Set-CASMailbox -Identity $Mailbox -SmtpClientAuthenticationDisabled $false
        }

        $tenantAfter = Get-TransportConfig | Select-Object -ExpandProperty SmtpClientAuthenticationDisabled
        $mailboxAfter = Get-CASMailbox -Identity $Mailbox | Select-Object -ExpandProperty SmtpClientAuthenticationDisabled

        Write-Step "Tenant SmtpClientAuthenticationDisabled(after)=$tenantAfter"
        Write-Step "Mailbox $Mailbox SmtpClientAuthenticationDisabled(after)=$mailboxAfter"

        if ($tenantAfter -ne $false) {
            throw "No fue posible dejar SMTP AUTH habilitado en tenant. Valor actual: $tenantAfter"
        }

        if ($mailboxAfter -ne $false) {
            throw "No fue posible dejar SMTP AUTH habilitado en mailbox $Mailbox. Valor actual: $mailboxAfter"
        }
    }
    finally {
        Disconnect-ExchangeOnline -Confirm:$false -ErrorAction SilentlyContinue | Out-Null
    }
}

function Resolve-SmtpProviderConfig {
    $microsoftEnabled = Test-Truthy (Get-EnvValue "MAIL_MICROSOFT_ENABLED" "false")

    if ($microsoftEnabled) {
        $config = [ordered]@{
            Name     = "microsoft"
            Host     = Get-EnvValue "MAIL_MICROSOFT_HOST" "smtp.office365.com"
            Port     = Get-EnvValue "MAIL_MICROSOFT_PORT" "587"
            Username = Get-EnvValue "MAIL_MICROSOFT_USERNAME"
            Password = Get-EnvValue "MAIL_MICROSOFT_PASSWORD"
            From     = Get-EnvValue "MAIL_MICROSOFT_FROM" (Get-EnvValue "MAIL_MICROSOFT_USERNAME")
        }
        return $config
    }

    $config = [ordered]@{
        Name     = "primary"
        Host     = Get-EnvValue "MAIL_HOST" "smtp.gmail.com"
        Port     = Get-EnvValue "MAIL_PORT" "587"
        Username = Get-EnvValue "MAIL_USERNAME"
        Password = Get-EnvValue "MAIL_PASSWORD"
        From     = Get-EnvValue "EMAIL_FROM" (Get-EnvValue "MAIL_USERNAME")
    }
    return $config
}

function Test-SmtpCredentials {
    param([hashtable]$Config)

    if (Test-Placeholder $Config.Username) {
        throw "El usuario SMTP para proveedor '$($Config.Name)' esta vacio o es placeholder."
    }

    if (Test-Placeholder $Config.Password) {
        throw "La contrasena SMTP para proveedor '$($Config.Name)' esta vacia o es placeholder."
    }

    Write-Step "Credenciales SMTP detectadas para proveedor '$($Config.Name)' con usuario '$($Config.Username)'"
}

function ConvertTo-PlainText {
    param([System.Security.SecureString]$SecureValue)

    if ($null -eq $SecureValue) {
        return ""
    }

    $bstr = [Runtime.InteropServices.Marshal]::SecureStringToBSTR($SecureValue)
    try {
        return [Runtime.InteropServices.Marshal]::PtrToStringBSTR($bstr)
    }
    finally {
        [Runtime.InteropServices.Marshal]::ZeroFreeBSTR($bstr)
    }
}

function Read-SmtpCredentialsInteractive {
    param([hashtable]$Config)

    if (Test-Placeholder $Config.Username) {
        $inputUser = Read-Host "Ingresa usuario SMTP para proveedor '$($Config.Name)'"
        if (-not [string]::IsNullOrWhiteSpace($inputUser)) {
            $Config.Username = $inputUser.Trim()
        }
    }

    if (Test-Placeholder $Config.Password) {
        $securePass = Read-Host "Ingresa contrasena SMTP para proveedor '$($Config.Name)'" -AsSecureString
        $plainPass = ConvertTo-PlainText -SecureValue $securePass
        if (-not [string]::IsNullOrWhiteSpace($plainPass)) {
            $Config.Password = $plainPass
        }
    }

    if (Test-Placeholder $Config.From) {
        $Config.From = $Config.Username
    }
}

function Test-SmtpSend {
    param(
        [hashtable]$Config,
        [string]$Recipient
    )

    if ([string]::IsNullOrWhiteSpace($Recipient)) {
        Write-Step "No se ejecuto envio de prueba SMTP (sin TestRecipient)."
        return
    }

    Write-Step "Enviando correo de prueba via $($Config.Name) a $Recipient"

    $smtp = New-Object System.Net.Mail.SmtpClient($Config.Host, [int]$Config.Port)
    $smtp.EnableSsl = $true
    $smtp.Credentials = New-Object System.Net.NetworkCredential($Config.Username, $Config.Password)

    $mail = New-Object System.Net.Mail.MailMessage
    $mail.From = $Config.From
    $mail.To.Add($Recipient)
    $mail.Subject = "SMTP verification TechCup"
    $mail.Body = "SMTP auth verification from fix-smtp-auth-and-restart.ps1"

    try {
        $smtp.Send($mail)
        Write-Step "Correo de prueba enviado correctamente."
    }
    finally {
        $mail.Dispose()
        $smtp.Dispose()
    }
}

function Start-ApiRestart {
    param(
        [string]$ProjectPath,
        [switch]$SkipCleanBuild
    )

    Write-Step "Liberando puerto 8080"
    $owners = Get-NetTCPConnection -LocalPort 8080 -State Listen -ErrorAction SilentlyContinue |
        Select-Object -ExpandProperty OwningProcess -Unique

    if ($owners) {
        foreach ($pidValue in $owners) {
            Stop-Process -Id $pidValue -Force -ErrorAction SilentlyContinue
        }
    }

    $runScript = Join-Path $ProjectPath "run-dev.ps1"
    if (-not (Test-Path $runScript)) {
        throw "No existe run-dev.ps1 en: $ProjectPath"
    }

    Write-Step "Reiniciando API con run-dev.ps1"
    $processArgs = @("-NoProfile", "-ExecutionPolicy", "Bypass", "-File", $runScript)
    if ($SkipCleanBuild) {
        $processArgs += "-SkipClean"
    }

    Start-Process -FilePath "powershell.exe" -ArgumentList $processArgs -WorkingDirectory $ProjectPath | Out-Null
    Write-Step "API reiniciada en proceso separado."
}

$workspace = (Resolve-Path $WorkspacePath).Path
Read-DotEnv -FilePath (Join-Path $workspace ".env")

$smtpConfig = Resolve-SmtpProviderConfig
$effectiveMailbox = $MailboxUpn
if ([string]::IsNullOrWhiteSpace($effectiveMailbox)) {
    if ($smtpConfig.Name -eq "microsoft") {
        $effectiveMailbox = $smtpConfig.Username
    }
}

if (-not $SkipExchange) {
    if ([string]::IsNullOrWhiteSpace($effectiveMailbox)) {
        throw "Debes enviar -MailboxUpn o configurar MAIL_MICROSOFT_USERNAME para habilitar SMTP AUTH en Exchange."
    }
    Set-ExchangeSmtpAuthEnabled -Admin $AdminUpn -Mailbox $effectiveMailbox
}

Read-SmtpCredentialsInteractive -Config $smtpConfig
Test-SmtpCredentials -Config $smtpConfig

if (-not $SkipSmtpSendTest) {
    Test-SmtpSend -Config $smtpConfig -Recipient $TestRecipient
}

if (-not $SkipRestart) {
    Start-ApiRestart -ProjectPath $workspace -SkipCleanBuild:$SkipClean
}

Write-Step "Proceso completado."
