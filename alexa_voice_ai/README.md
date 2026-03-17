# Alexa Voice AI Skill - Production-Ready Local Development

A complete, production-ready Alexa skills framework built in Python with 5 capability domains, running locally on Windows with Flask and Claude AI integration.

## 🎯 Features

**5 Capability Domains:**
1. **Information & Q&A** - Claude-powered intelligent question answering
2. **Smart Home Control** - Light, thermostat, lock, and camera control (simulated locally)
3. **Task & Reminder Management** - Create tasks, set reminders, local persistence
4. **Custom Business Workflows** - Execute business processes and automations
5. **Human-to-Agent Communication** - Natural multi-turn conversations with context

**Architecture:**
- Flask web server (runs on Windows, not Lambda)
- Ask-SDK core for Alexa request/response handling
- Anthropic Claude API for intelligent responses
- Session memory with multi-turn conversation history
- Local JSON file persistence (no AWS services required)
- ngrok tunnel for cloud access

---

## 📋 Prerequisites

### 1. Python
- **Minimum:** Python 3.11
- **Download:** https://www.python.org/downloads/
- **Verify installation:**
  ```bash
  python --version
  ```

### 2. Active Anthropic Account
- Create account: https://console.anthropic.com/
- Get API key from: https://console.anthropic.com/account/keys
- **Keep this secret!** Never commit to git.

### 3. Alexa Developer Account
- Sign up: https://developer.amazon.com/
- Create a custom skill in Alexa Developer Console
- Get your Skill ID

### 4. ngrok (for tunnel to cloud)
- Download: https://ngrok.com/download
- Create free account: https://ngrok.com/
- Install and authenticate: `ngrok config add-authtoken YOUR_TOKEN`

---

## 🚀 Installation & Setup

### Step 1: Clone or Extract This Project
```bash
# Navigate to the project directory
cd alexa_voice_ai
```

### Step 2: Create Python Virtual Environment
```bash
# Create virtual environment
python -m venv venv

# Activate virtual environment (Windows)
venv\Scripts\activate

# You should see (venv) in your terminal prompt
```

### Step 3: Install Dependencies
```bash
# Upgrade pip first
pip install --upgrade pip

# Install all required packages
pip install -r requirements.txt
```

### Step 4: Configure Environment Variables
```bash
# Copy template to actual .env file (if not already done)
copy .env.example .env

# OR manually create .env and add:
ANTHROPIC_API_KEY=sk-ant-your-key-here
ALEXA_SKILL_ID=amzn1.ask.skill.xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
FLASK_ENV=development
FLASK_DEBUG=True
VERIFY_SIGNATURE=False
```

**Get your values:**
1. **ANTHROPIC_API_KEY**: From https://console.anthropic.com/account/keys
2. **ALEXA_SKILL_ID**: From Alexa Developer Console (your skill's ID)

⚠️ **IMPORTANT:** Never commit `.env` to git. It's in `.gitignore` by default.

---

## 🎮 Running the Skill Locally

### Terminal 1: Start Flask Server

```bash
# Make sure (venv) is active
# From the main alexa_voice_ai directory
python app.py
```

**Success output:**
```
╔════════════════════════════════════════════════════════════════╗
║          ALEXA VOICE AI SKILL - LOCAL DEVELOPMENT              ║
║                   Flask Server v1.0.0                          ║
╚════════════════════════════════════════════════════════════════╝

🚀 Server started on: http://localhost:5000
```

The Flask server should now be running. **Keep this terminal open.**

### Terminal 2: Start ngrok Tunnel

Open a **new terminal/command prompt**:

```bash
# Start ngrok tunnel (port 5000 matches Flask)
ngrok http 5000
```

**Success output:**
```
Session Status                online
Account                       you@example.com
Forwarding                    https://12ab-34cd-56ef-78gh.ngrok.io -> http://localhost:5000
```

**Copy the HTTPS URL** (the one ending in `.ngrok.io`)

### Step: Configure Alexa Skill

1. Go to https://developer.amazon.com/alexa/console/ask
2. Select your skill
3. Go to **Build** → **Interaction Model** (skip code section)
4. Go to **Configuration** (or **Endpoint**)
5. Set Endpoint URL type to **HTTPS**
6. Paste your ngrok URL: `https://12ab-34cd-56ef-78gh.ngrok.io/`
7. Click **Save**

**Test the skill:**
- Use Alexa app on phone
- Say: "Alexa, open [your skill name]"
- Should hear welcome message
- Try: "Ask [skill name] what is Python?"

---

## 🧪 Testing & Verification

### Check Server Health
```bash
# In another terminal window:
curl http://localhost:5000/health
```

**Response:**
```json
{
  "status": "running",
  "timestamp": "2025-03-14T12:34:56.789123",
  "version": "1.0.0"
}
```

### View Debug Information
```bash
# See loaded handlers and configuration
curl http://localhost:5000/debug
```

### Test with curl (simulate Alexa request)
```bash
curl -X POST http://localhost:5000/ \
  -H "Content-Type: application/json" \
  -d '{
    "version": "1.0",
    "session": {
      "new": true,
      "sessionId": "test-session-123",
      "application": {
        "applicationId": "amzn1.ask.skill.test"
      },
      "user": {
        "userId": "test-user-123"
      }
    },
    "request": {
      "type": "LaunchRequest",
      "requestId": "test-request-123",
      "locale": "en-US",
      "timestamp": "2025-03-14T12:34:56Z"
    }
  }'
```

---

## 📝 File Structure

```
alexa_voice_ai/
├── app.py                              # Flask server (main entry)
├── requirements.txt                    # Python dependencies
├── .env                                # Configuration (keep secret)
├── .env.example                        # Configuration template
├── README.md                           # This file
│
├── handlers/                           # Intent handlers
│   ├── __init__.py
│   ├── info_handler.py                 # Q&A, definitions, digests
│   ├── smart_home_handler.py          # Lights, thermostat, locks
│   ├── task_handler.py                 # Tasks, reminders, to-do
│   ├── business_handler.py             # Workflows, automation
│   └── agent_handler.py                # Conversation, sentiment
│
├── utils/                              # Utility modules
│   ├── __init__.py
│   ├── claude_brain.py                 # Claude API wrapper
│   ├── ssml_builder.py                 # Speech markup formatting
│   └── session_memory.py               # Multi-turn conversation context
│
└── data/                               # Local data storage
    └── reminders.json                  # Tasks and reminders (auto-created)
```

---

## 🎤 Example Voice Commands

### Information & Q&A
- "Alexa, ask AI assistant what is machine learning?"
- "Alexa, ask AI assistant to define quantum computing"
- "Alexa, ask AI assistant for a digest on artificial intelligence"

### Smart Home Control
- "Alexa, ask AI assistant to turn on the living room lights"
- "Alexa, ask AI assistant to set brightness to 50 percent"
- "Alexa, ask AI assistant to set thermostat to 72 degrees"
- "Alexa, ask AI assistant to lock the front door"
- "Alexa, ask AI assistant to start recording the front porch camera"
- "Alexa, ask AI assistant for smart home status"

### Task & Reminder Management
- "Alexa, ask AI assistant to create a task: finish project report"
- "Alexa, ask AI assistant to set a reminder to call mom"
- "Alexa, ask AI assistant to list my tasks"
- "Alexa, ask AI assistant to mark buy groceries as complete"
- "Alexa, ask AI assistant to show my reminders"

### Business Workflows
- "Alexa, ask AI assistant to initiate sales pipeline workflow"
- "Alexa, ask AI assistant to execute workflow step with this data"
- "Alexa, ask AI assistant to list available workflows"
- "Alexa, ask AI assistant for the status of the current workflow"

### Human-to-Agent Communication
- "Alexa, ask AI assistant tell me how to be more productive"
- "Alexa, ask AI assistant I'm feeling overwhelmed"
- "Alexa, ask AI assistant let's have a discussion about goal setting"
- "Alexa, ask AI assistant to clarify what I'm asking"

---

## 🔑 Configuration Details

### .env Variables

| Variable | Description | Required | Example |
|----------|-------------|----------|---------|
| `ANTHROPIC_API_KEY` | Claude API key for AI responses | Yes | `sk-ant-...` |
| `ALEXA_SKILL_ID` | Your Alexa skill identifier | Yes | `amzn1.ask.skill...` |
| `FLASK_ENV` | Environment type | No | `development` |
| `FLASK_DEBUG` | Enable debug mode | No | `True` |
| `NGROK_URL` | ngrok tunnel URL (informational) | No | `https://xxx.ngrok.io` |
| `VERIFY_SIGNATURE` | Verify Alexa request signatures | No | `False` (local dev) |

### Claude Models Available
The skill uses `claude-sonnet-4-20250514` (fast, affordable, capable).

**To use other models**, edit [utils/claude_brain.py](utils/claude_brain.py) line ~22:
```python
self.model = "claude-3-5-sonnet-20241022"  # Change this
```

---

## 🛠️ Development

### Adding a New Intent Handler

1. Create handler in appropriate file (e.g., `handlers/info_handler.py`)
2. Extend `AbstractRequestHandler`
3. Implement `can_handle_func` and `handle()` methods
4. Import and register in `app.py`

**Example:**
```python
from ask_sdk_core.dispatch_components import AbstractRequestHandler
from ask_sdk_core.utils import is_intent_name

class MyNewHandler(AbstractRequestHandler):
    can_handle_func = is_intent_name("MyIntent")
    
    def handle(self, handler_input):
        return ResponseFormatter() \
            .set_speech("My response") \
            .build()
```

Then in `app.py`:
```python
from handlers.info_handler import MyNewHandler
sb.add_request_handler(MyNewHandler())
```

### Understanding the Data Flow

1. **Alexa** sends JSON request to ngrok URL
2. **ngrok** forwards to `localhost:5000/`
3. **Flask** route calls `webservice_handler()`
4. **ask-sdk** dispatches to matching handler based on intent
5. **Handler** uses Claude, session memory, or local storage
6. **Response** built with SSML formatting
7. **Response** returned through Flask → ngrok → Alexa

---

## 🚨 Troubleshooting

### Flask Server Won't Start
```bash
# Error: Port 5000 already in use
# Solution: Kill process on port 5000
netstat -ano | findstr :5000
taskkill /PID <PID> /F

# Or use different port
python app.py --port 5001
```

### Anthropic API Key Not Working
```bash
# Check that .env file exists and is readable
type .env

# Verify API key is valid
python -c "import os; from dotenv import load_dotenv; load_dotenv(); print(os.getenv('ANTHROPIC_API_KEY')[:20] + '...')"
```

### Alexa Can't Reach Skill
1. Check ngrok is running: `curl https://xxx.ngrok.io/health`
2. Verify URL in Alexa Developer Console ends with `/`
3. Check Flask is still running (don't close that terminal!)
4. Restart both Flask and ngrok

### Session Memory Not Persisting
- Reminders/tasks are stored in `data/reminders.json`
- Check file exists: `type data\reminders.json`
- Check folder permissions for write access

---

## 📊 Monitoring & Logs

### Terminal Output
- Flask automatically logs all requests
- Claude API calls are logged with response times
- Error stack traces appear in terminal

### Request/Response Inspection
```python
# In app.py, search for logger.info()
# Modify log level for more/less detail:
logging.basicConfig(level=logging.DEBUG)  # More verbose
```

---

## 🔒 Security Notes

### For Local Development
- ✓ `VERIFY_SIGNATURE=False` is safe for localhost
- ✓ No sensitive data in code
- ✓ API keys in `.env` only (in `.gitignore`)
- ✓ In-memory session data (no persistence)

### For Production Deployment
- Change `VERIFY_SIGNATURE=True`
- Use AWS Secrets Manager or Azure Key Vault
- Deploy to AWS Lambda or cloud service
- Enable HTTPS certificates
- Add rate limiting and authentication
- Use DynamoDB for persistent storage

---

## 📚 Further Reading

- [Alexa Skills Kit Documentation](https://developer.amazon.com/en-US/docs/alexa/ask-overviews/what-is-the-alexa-skills-kit.html)
- [ask-sdk-core on GitHub](https://github.com/alexa/alexa-skills-kit-sdk-for-python)
- [Anthropic Claude API](https://anthropic.com/api)
- [SSML Reference](https://developer.amazon.com/en-US/docs/alexa/custom-skills/speech-synthesis-markup-language-ssml-reference.html)
- [ngrok Documentation](https://ngrok.com/docs)

---

## 🤝 Contributing

To extend this skill:
1. Create feature branch
2. Add handlers for new intents
3. Update README with commands
4. Test with actual Alexa device
5. Document the changes

---

## 📄 License

This project is provided as-is for educational and commercial use.

---

## 🎉 Quick Start Checklist

- [ ] Python 3.11+ installed
- [ ] Virtual environment created and activated
- [ ] Dependencies installed (`pip install -r requirements.txt`)
- [ ] `.env` file configured with API keys
- [ ] Flask server running (`python app.py`)
- [ ] ngrok tunnel running (`ngrok http 5000`)
- [ ] Alexa skill endpoint configured
- [ ] Tested basic voice command
- [ ] Data/reminders.json persisting tasks

---

**Happy building!** 🚀

For questions or issues, check the code comments or review the handler implementations.
