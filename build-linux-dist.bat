@echo off
REM Builds the Linux (x86_64) tar.gz distribution of Elasticsearch.
REM Output: distribution\archives\linux-tar\build\distributions\elasticsearch-*-linux-x86_64.tar.gz
REM This artifact bundles the JDK, so it is typically ~800 MB.
REM
REM Requires admin: unpacking the bundled Linux JDK creates real symlinks,
REM which Windows only allows for elevated processes (or with Developer Mode on).

REM Self-elevate if not already running as Administrator.
net session >nul 2>&1
if %errorlevel% neq 0 (
    echo Requesting administrator privileges...
    powershell -NoProfile -Command "Start-Process -FilePath '%~f0' -WorkingDirectory '%~dp0' -Verb RunAs"
    exit /b
)

setlocal
cd /d "%~dp0"

call gradlew.bat :distribution:archives:linux-tar:assemble
if errorlevel 1 (
    echo.
    echo Build failed.
    pause
    exit /b 1
)

echo.
echo Build succeeded. Output files:
dir /s /b "distribution\archives\linux-tar\build\distributions\*.tar.gz"

endlocal
pause
