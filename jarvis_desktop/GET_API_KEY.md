# Getting Your Anthropic API Key - Step by Step Guide

## ⏱️ Estimated Time: 5 minutes

---

## STEP 1: Visit Anthropic Console

Open your browser and go to:
```
https://console.anthropic.com/
```

---

## STEP 2: Sign Up or Login

### If you don't have an account:
1. Click **"Sign Up"**
2. Enter your email
3. Create a password
4. Check your email for verification link
5. Click the verification link

### If you already have an account:
1. Click **"Log In"** or **"Sign In"**
2. Enter your credentials

---

## STEP 3: Create New API Key

1. **Left sidebar:** Look for **"API Keys"** section
2. Click **"Create Key"** or **"New Key"** button
3. Give it a name (example: "Jarvis Desktop")
4. Click **"Generate"** or **"Create"**

---

## STEP 4: Copy Your Key

You'll see your API key displayed like:
```
sk-ant-xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
```

⚠️ **IMPORTANT:**
- Copy the ENTIRE key (starts with `sk-ant-`)
- This is shown only ONCE - save it somewhere safe
- Never share this key with anyone
- If you lose it, create a new one

---

## STEP 5: Add Key to Jarvis

### Option A: Direct Edit (Easiest)

1. Open this file:
```
d:\project\jarvis_desktop\.env
```

2. Find this line:
```ini
ANTHROPIC_API_KEY=sk-ant-your-api-key-here
```

3. Replace it with your actual key:
```ini
ANTHROPIC_API_KEY=sk-ant-xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
```

4. Save the file (Ctrl+S)

### Option B: Update via Terminal

```bash
cd d:\project\jarvis_desktop
echo ANTHROPIC_API_KEY=sk-ant-YOUR-KEY-HERE >> .env
```

---

## STEP 6: Verify It Works

Run this command:
```bash
cd d:\project\jarvis_desktop
python test_quick.py
```

You should see:
```
[4] AI Brain Status
API Key: CONFIGURED
```

---

## STEP 7: Launch Jarvis!

Now run the full application:
```bash
python jarvis_app.py
```

Or use the launcher:
```bash
run.bat
```

---

## 🎤 Test It Out

1. **Jarvis window** will open
2. In the text box, type: `"What time is it?"`
3. Click **"▶ Send"**
4. Jarvis should respond!

Try these commands:
```
"Open Chrome"
"Set volume to 50"
"What date is today?"
"Go to YouTube"
"Open Notepad"
```

---

## 🔍 Troubleshooting

### ❌ "ANTHROPIC_API_KEY not set"
- Check your .env file
- Make sure you saved it with Ctrl+S
- Restart jarvis_app.py

### ❌ "API authentication failed"
- Your key might be wrong
- Go to console.anthropic.com and create a new key
- Update .env with the new key

### ❌ "No microphone detected"
- Don't worry! You can still type commands
- Click "Listen" button will show error
- Just type in the text box instead

### ❌ "Rate limit exceeded"
- You're making requests too fast
- Wait a few seconds between commands
- Check fair usage at console.anthropic.com

---

## 💰 Pricing & Credits

Anthropic gives you:
- **Free trial credits** when you sign up
- **Pay-as-you-go** after trial ends
- **~$0.003 per command** (very cheap!)

Check your usage:
1. Go to console.anthropic.com
2. Click "Usage" in sidebar
3. View your spending

---

## 📌 Tips

✅ **Keep your key safe** - Treat it like a password
✅ **Use version control** - Add `.env` to `.gitignore`
✅ **Monitor usage** - Check console.anthropic.com regularly
✅ **Create backups** - Save your key somewhere safe offline

---

## ✨ What's Next?

After adding your API key:

1. **Full Voice Control** (optional)
   ```bash
   pip install SpeechRecognition pyttsx3
   ```

2. **Try Advanced Commands**
   - "Open Discord"
   - "Play music"
   - "Lock my screen"
   - "What's my IP address?"

3. **Customize Settings**
   - Edit `config/favorites.json` to add your apps
   - Edit `config/settings.json` for preferences

4. **Add to Startup** (optional)
   - Create shortcut in Windows Startup folder

---

## 🎓 Learning Resources

- [Anthropic Documentation](https://docs.anthropic.com/)
- [Claude API Guide](https://docs.anthropic.com/en/api/getting-started)
- [API Examples](https://docs.anthropic.com/en/api/getting-started)

---

**Ready to activate Jarvis?** 🎙️✨

Get your key from: **https://console.anthropic.com/**

Then update your `.env` file and run: `python jarvis_app.py`
