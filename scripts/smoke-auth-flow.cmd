@echo off
setlocal
cd /d "%~dp0.."
powershell -NoProfile -ExecutionPolicy Bypass -File "scripts\test-swagger-registration-flow.ps1" -SkipTlsValidation
endlocal
