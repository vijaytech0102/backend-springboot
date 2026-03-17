@echo off
REM Windows Desktop Jarvis - Setup Script

echo ╔════════════════════════════════════════════════════════════╗
echo ║          Jarvis - Windows Desktop Assistant Setup           ║
echo ╚════════════════════════════════════════════════════════════╝
echo.

REM Check if Python is installed
python --version >nul 2>&1
if errorlevel 1 (
    echo ❌ Python is not installed or not in PATH
    echo Please install Python 3.8+ from https://www.python.org/
    pause
    exit /b 1
)

echo ✅ Python found

REM Create virtual environment
echo.
echo 📦 Creating virtual environment...
if exist venv (
    echo Virtual environment already exists
) else (
    python -m venv venv
)

REM Activate virtual environment
echo 🔧 Activating virtual environment...
call venv\Scripts\activate.bat

REM Upgrade pip
echo 📥 Upgrading pip...
python -m pip install --upgrade pip -q

REM Install requirements
echo 📚 Installing dependencies...
pip install -q -r requirements.txt

if errorlevel 1 (
    echo ❌ Failed to install some packages
    echo Try installing requirements_minimal.txt instead:
    echo pip install -r requirements_minimal.txt
    pause
    exit /b 1
)

echo.
echo ✅ Installation complete!
echo.
echo 📝 Next steps:
echo    1. Edit .env file and add your ANTHROPIC_API_KEY
echo    2. Run: python jarvis_app.py
echo.
echo Get your API key from: https://console.anthropic.com/
echo.
pause
