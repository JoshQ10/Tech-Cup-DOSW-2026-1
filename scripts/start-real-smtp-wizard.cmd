@echo off
setlocal
cd /d "%~dp0.."
powershell -NoProfile -ExecutionPolicy Bypass -File "scripts\start-dev-real-smtp.ps1" -Provider resend -EnableResendApi -PreferResendApi -UseHttps
endlocal
