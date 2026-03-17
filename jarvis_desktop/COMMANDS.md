# Jarvis Command Reference Guide

## 📱 Application Control

### Open Applications
```
"Open Chrome"
"Launch Discord"
"Start Spotify"
"Open VSCode"
"Run Notepad"
"Start Calculator"
"Open Firefox"
"Launch Explorer"
```

### Check What's Loaded
```
"What apps can you open?"
"Show available applications"
"List known apps"
```

---

## 🎵 Music Control

### Play Music
```
"Play music"
"Play some jazz"
"Play Beatles on Spotify"
"Play classical music"
"Open YouTube Music"
```

### Playback Control
```
"Play"
"Pause"
"Next track"
"Previous song"
"Stop music"
"Repeat"
```

### Volume Control
```
"Set volume to 50"
"Increase volume"
"Volume up"
"Decrease volume"
"Volume down"
"Mute"
"Unmute"
```

---

## 🔊 Audio & System

### Volume
```
"Set volume to 30"
"Volume 75%"
"Max volume"
"Min volume"
"Louder"
"Quieter"
```

### Brightness & Display
```
"Set brightness to 80"
"Brightness 50"
"Make it brighter"
"Dim the screen"
"Max brightness"
```

### Screen Control
```
"Show desktop"
"Minimize windows"
"Lock my screen"
"Lock Windows"
```

---

## ⏰ System Control

### Sleep & Shutdown
```
"Go to sleep"
"Sleep mode"
"Shutdown"
"Turn off computer"
"Restart computer"
"Reboot"
"Cancel shutdown"
```

### Information
```
"What time is it?"
"Tell me the time"
"What date is today?"
"What's the date?"
"Show system information"
"CPU usage"
"Memory usage"
"Disk space"
```

### Network
```
"What's my IP address?"
"Show IP"
"Check network"
"Internet status"
```

---

## 🌐 Browser & Web

### Open Websites
```
"Open Google"
"Go to YouTube"
"Open GitHub"
"Visit Stack Overflow"
"Open Google Maps"
"Open Gmail"
"Go to Reddit"
"Visit Wikipedia"
```

### Quick Searches
```
"Search Python tutorial"
"Look up machine learning"
"Find fast food near me"
```

---

## 💻 System Settings

### Windows Settings
```
"Open Settings"
"Show display settings"
"Open sound settings"
"Show Bluetooth settings"
"Open WiFi settings"
"Show apps"
"About this computer"
```

### Control Panel
```
"Open Control Panel"
"System settings"
"Device Manager"
```

---

## 📁 File Management

### Browse Files
```
"Open File Explorer"
"Show Documents"
"Open Downloads"
"Show Desktop"
"Go to Music folder"
"Open Videos"
```

### Specific Locations
```
"Open C drive"
"Show Program Files"
"Go to Documents"
```

---

## 🤖 AI Assistant Features

### General Questions
```
"What is Python?"
"Explain machine learning"
"How do I use Docker?"
"What is cloud computing?"
"Tell me about AI"
```

### Conversational
```
"Hello"
"How are you?"
"What's your name?"
"Who are you?"
"Can you help me?"
```

### Follow-ups
```
"Tell me more"
"Explain that again"
"Give me an example"
"How does that work?"
```

---

## 🎯 Complex Commands

### Multi-Step
```
"Open Chrome and go to YouTube"
"Launch Spotify and play jazz"
"Open Discord and set volume to 50"
```

### Conditional
```
"If it's after 5pm, show me the time"
"Tell me the date and time"
```

### Chaining
```
"Set volume to 80 and play music"
"Close everything and lock the screen"
```

---

## 📋 Command Categories

### ✅ Working Without API Key
- App opening
- System information  
- Music controller (info)
- Basic system commands

### ⚠️ Need API Key (Claude AI)
- Intelligent responses
- Natural language understanding
- Contextual commands
- Conversation

---

## 🎙️ Voice vs Text

### Voice Commands
Say it naturally:
```
"Hey Jarvis, what time is it?"
"Jarvis, open Spotify"
"Play music for me"
```

### Text Commands  
Type normally:
```
"Open Spotify"
"What time is it?"
"Play music"
```

**Both work the same way!**

---

## 💡 Tips for Best Results

1. **Be specific**
   - ✅ "Open Google Chrome"
   - ❌ "Open browser"

2. **Use complete sentences**
   - ✅ "Set volume to 50"
   - ❌ "Volume 50" (sometimes works, but less reliable)

3. **Natural language**
   - ✅ "Play some jazz music"
   - ❌ "Jazz"

4. **One command at a time**
   - ✅ "Open Spotify" → wait → "Play music"
   - ❌ Chain multiple without waiting

5. **Be patient**
   - First response might take 2-3 seconds
   - Network latency is normal
   - Don't spam commands

---

## 🆘 If a Command Doesn't Work

1. **Try rephrasing**
   ```
   NOT working: "Play tunes"
   TRY: "Play music"
   ```

2. **Be more specific**
   ```
   NOT working: "Set sound"
   TRY: "Set volume to 50"
   ```

3. **Check context**
   - Is app installed? (Chrome, Spotify, etc)
   - Is internet connected?
   - Is speaker on?

4. **Check logs**
   - Look at Jarvis conversation window
   - Shows what was understood
   - Shows what was executed

---

## 🚀 Advanced Usage

### Add Custom Commands

1. Edit `utils/ai_brain.py`
2. Modify `_parse_action()` method
3. Add your custom logic
4. Restart Jarvis

### Create Macros

In `config/favorites.json`:
```json
{
    "dev_setup": "C:\\Path\\To\\Script.bat",
    "work_mode": "Open Chrome and Discord",
    "chill_mode": "Open Spotify and set volume to 70"
}
```

### Automation

Schedule Jarvis to:
- Open apps at specific times
- Play music automatically
- Run daily tasks
- Send notifications

---

## 📞 Quick Reference

| Need                    | Say                              |
|------------------------|----------------------------------|
| App launcher           | "Open [APP_NAME]"               |
| Music                  | "Play music" / "Next track"     |
| Volume                 | "Set volume to [0-100]"        |
| Time/Date              | "What time/date is it?"        |
| Lock screen            | "Lock my screen"               |
| Website                | "Open [WEBSITE]"               |
| Settings               | "Open Settings"                |
| System info            | "What's my IP address?"        |
| Help                   | "What can you do?"             |

---

## 🎓 Learning More

- 🎤 Experiment with natural language
- 📚 Check Jarvis window for responses
- 🔧 Modify config files for customization
- 💬 Have conversations with Claude
- 🤖 Train yourself on what works

**The more you use Jarvis, the better you'll get at phrasing commands!**

---

**Pro Tip:** Start simple, then try complex commands as you get comfortable! 🚀
