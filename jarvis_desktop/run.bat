@echo off
REM Quick launch script for Jarvis

if not exist venv (
    echo ❌ Virtual environment not found
    echo Run setup.bat first
    pause
    exit /b 1
)

echo 🚀 Starting Jarvis...
call venv\Scripts\activate.bat
python jarvis_app.py

pause
