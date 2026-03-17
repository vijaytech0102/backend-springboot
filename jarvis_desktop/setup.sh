#!/bin/bash
# Windows Desktop Jarvis - Setup Script (for WSL/Git Bash)

echo "╔════════════════════════════════════════════════════════════╗"
echo "║          Jarvis - Windows Desktop Assistant Setup           ║"
echo "╚════════════════════════════════════════════════════════════╝"
echo ""

# Check if Python is installed
if ! command -v python3 &> /dev/null; then
    echo "❌ Python is not installed"
    echo "Please install Python 3.8+ from https://www.python.org/"
    exit 1
fi

echo "✅ Python found"
python3 --version

# Create virtual environment
echo ""
echo "📦 Creating virtual environment..."
if [ -d "venv" ]; then
    echo "Virtual environment already exists"
else
    python3 -m venv venv
fi

# Activate virtual environment
echo "🔧 Activating virtual environment..."
source venv/bin/activate

# Upgrade pip
echo "📥 Upgrading pip..."
python3 -m pip install --upgrade pip -q

# Install requirements
echo "📚 Installing dependencies..."
pip install -q -r requirements.txt

if [ $? -ne 0 ]; then
    echo "❌ Failed to install some packages"
    echo "Try installing requirements_minimal.txt instead:"
    echo "pip install -r requirements_minimal.txt"
    exit 1
fi

echo ""
echo "✅ Installation complete!"
echo ""
echo "📝 Next steps:"
echo "   1. Edit .env file and add your ANTHROPIC_API_KEY"
echo "   2. Run: python jarvis_app.py"
echo ""
echo "Get your API key from: https://console.anthropic.com/"
echo ""
