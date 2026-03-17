# Windows Desktop Jarvis - Voice Assistant

## 🎙️ What is Jarvis?

**Jarvis** is a Windows desktop voice assistant powered by Claude AI. Control your computer with natural voice commands:

### Voice Commands Examples

- **"Open Spotify"** → Launches Spotify
- **"Play some music"** → Plays music in default player
- **"Set volume to 50"** → Adjusts system volume
- **"Lock my screen"** → Locks Windows
- **"Open Google"** → Opens Google in your browser
- **"What time is it?"** → Tells you the time
- **"Increase brightness"** → Brightens screen
- **"Open Discord"** → Launches Discord

## 🚀 Installation

### Prerequisites
- Windows 10/11
- Python 3.8+
- Microphone
- Internet connection (for Claude AI)

### Step 1: Clone/Download Project

```bash
cd d:\project\jarvis_desktop
```

### Step 2: Create Virtual Environment

```bash
python -m venv venv
venv\Scripts\activate
```

### Step 3: Install Dependencies

```bash
pip install -r requirements.txt
```

**If you encounter issues with audio:**

```bash
pip install -r requirements_minimal.txt
```

Then manually install:
```bash
pip install PyQt5 SpeechRecognition pyttsx3
```

### Step 4: Configure API

1. Open `.env` file
2. Add your Anthropic API key:
```
ANTHROPIC_API_KEY=sk-ant-your-key-here
```

Get your key from: https://console.anthropic.com/

### Step 5: Run Jarvis

```bash
python jarvis_app.py
```

## 🎯 Features

### Voice Control
- ✅ "Hey Jarvis" wake word detection
- ✅ Natural language command processing
- ✅ Text-to-speech responses
- ✅ Real-time conversation display

### Application Management
- ✅ Open any installed Windows app
- ✅ Favorite apps quick launch
- ✅ App search and selection

### Media Control
- ✅ Spotify control
- ✅ Windows Media Player
- ✅ YouTube Music
- ✅ Local music files
- ✅ Play/Pause/Next/Previous
- ✅ Volume control

### System Control
- ✅ Adjust volume and brightness
- ✅ Lock/Sleep/Shutdown
- ✅ Network information
- ✅ System performance monitoring
- ✅ Browser automation

### AI Features
- ✅ Claude AI-powered responses
- ✅ Multi-turn conversations
- ✅ Intelligent command parsing
- ✅ Context-aware assistance

## 📖 Usage

### Starting Jarvis

```bash
python jarvis_app.py
```

### Using Voice Commands

1. Click **"🎤 Listen"** button
2. Wait for: "Listening for 'Hey Jarvis'..."
3. Say **"Hey Jarvis"**
4. Wait for: "Wake word detected!"
5. Give your command (e.g., "Open Spotify")
6. Jarvis will respond and execute

### Using Text Commands

1. Type command in the input box
2. Click **"▶ Send"** or press Ctrl+Enter
3. Jarvis processes and responds

### Command Examples

#### Application Control
```
"Open VSCode"
"Launch Discord"
"Start Spotify"
"Run Calculator"
```

#### Music
```
"Play some music"
"Play jazz"
"Spotify, play Beatles"
"Next track"
"Previous song"
"Stop music"
```

#### System Control
```
"Set volume to 50"
"Mute audio"
"Increase brightness"
"Lock my screen"
"Go to sleep"
"Shutdown computer"
```

#### Information
```
"What time is it?"
"What's today's date?"
"Get system information"
"Show IP address"
```

#### Browser
```
"Open Google"
"Go to YouTube"
"Open GitHub"
"Visit Stack Overflow"
```

## ⚙️ Configuration

### Edit `.env` file

```ini
# API Key (Required)
ANTHROPIC_API_KEY=sk-ant-your-key-here

# Spotify Integration (Optional)
SPOTIFY_CLIENT_ID=your-id
SPOTIFY_CLIENT_SECRET=your-secret

# Microphone (Optional)
MICROPHONE_INDEX=0

# Audio Settings
VOICE_VOLUME=100          # 0-100
SPEECH_RATE=150          # Words per minute

# Music Player
MUSIC_PLAYER=default     # default, spotify, youtube, vlc

# UI
THEME=dark               # dark, light
WINDOW_OPACITY=0.95      # 0.0-1.0
```

### Add Favorite Apps

Edit `config/favorites.json`:

```json
{
    "spotify": "C:\\Users\\YourUsername\\AppData\\Roaming\\Spotify\\spotify.exe",
    "chrome": "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe",
    "myapp": "C:\\Path\\To\\Your\\App\\app.exe"
}
```

## 🐛 Troubleshooting

### Microphone not detected
```bash
# Check available microphones
python -c "import speech_recognition as sr; print(sr.Microphone.list_microphone_indexes())"
```

Update `MICROPHONE_INDEX` in `.env`

### Speech recognition not working
- Check internet connection (Google Speech API required)
- Try different microphone
- Test microphone volume

### Can't open applications
- Check app path is correct
- Application might not be in PATH
- Try adding to favorites with full path

### Claude API errors
- Verify API key is correct
- Check API key has credits
- Ensure internet connection

### pyttsx3 not working
- Install espeak: https://github.com/espeak-ng/espeak-ng
- Set voice in settings

## 🔐 Security Notes

- **API Key:** Keep `.env` file secure - don't share!
- **Microphone:** May record during wake word detection
- **System Access:** Jarvis can execute system commands
- **Storage:** Conversation history stored in memory only

## 📊 Performance Tips

- Use a dedicated microphone for better recognition
- Keep your room quiet during voice input
- Speak clearly and naturally
- Use favorites for frequently-used apps
- Limit background applications for faster response

## 🆘 Common Issues

| Issue | Solution |
|-------|----------|
| "No module named 'PyQt5'" | `pip install PyQt5` |
| Microphone errors | Update microphone drivers |
| Speech recognition fails | Check internet, try again |
| API throttling | Wait a few seconds, try again |
| App won't open | Check full path in favorites |
| No sound output | Check volume and speakers |

## 🎨 Customization

### Change Theme
Edit `config/settings.json`:
```json
{
    "theme": "light"
}
```

### Add New Commands
Edit `utils/ai_brain.py` in `_parse_action()` method

### Change Wake Words
Edit `.env`:
```ini
WAKE_WORDS=hey jarvis,yo jarvis,jarvis wake up
```

## 📚 File Structure

```
jarvis_desktop/
├── jarvis_app.py                 # Main GUI application
├── requirements.txt              # All dependencies
├── requirements_minimal.txt      # Minimal dependencies
├── .env                          # Configuration (customize)
├── .env.example                  # Configuration template
├── README.md                     # This file
│
├── utils/                        # Core modules
│   ├── voice_recognition.py     # Microphone & wake word
│   ├── text_to_speech.py        # TTS engine
│   ├── app_launcher.py          # App opening
│   ├── music_controller.py      # Music playback
│   ├── system_commands.py       # System control
│   └── ai_brain.py              # Claude AI integration
│
├── config/                       # Configuration files
│   ├── favorites.json           # Favorite apps
│   └── settings.json            # User settings
│
└── resources/                    # Images, icons, etc
```

## 🚀 Advanced Features

### Running on Startup

```bash
# Create shortcut in Startup folder
# C:\Users\YourUsername\AppData\Roaming\Microsoft\Windows\Start Menu\Programs\Startup
```

### System Tray Only Mode

The app minimizes to system tray. Click tray icon to show.

### Command History

Commands are stored in AI memory (not persisted).

### Settings Management

Modify `config/settings.json` to change defaults.

## 📝 License

MIT - Use freely, modify as needed.

## 🤝 Contributing

Have ideas? Feel free to modify and extend!

## 📞 Support

For issues or features:
1. Check troubleshooting section
2. Review `.env` configuration
3. Check app favorites paths
4. Test microphone and speakers

## 🎓 Learning Resources

- [Speech Recognition](https://github.com/Uberi/speech_recognition)
- [pyttsx3 Docs](https://pyttsx3.readthedocs.io/)
- [PyQt5 Tutorial](https://www.riverbankcomputing.com/static/Docs/PyQt5/)
- [Anthropic Claude API](https://docs.anthropic.com/)

---

**Enjoy your personal Windows assistant!** 🎙️✨

Quick Start:
```bash
python jarvis_app.py
```
Say "Hey Jarvis" and start commanding! 🚀
