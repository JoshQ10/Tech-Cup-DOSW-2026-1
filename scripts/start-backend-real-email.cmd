@echo off
setlocal
cd /d "%~dp0.."
powershell -NoProfile -ExecutionPolicy Bypass -File "run-dev.ps1" -UseHttps -RealEmail
endlocal
