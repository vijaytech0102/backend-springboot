# Django Frontend for Alexa Voice AI

Complete Django web interface for the Alexa Voice AI skill with test endpoints and conversation history.

## 📋 Features

- **Chat Interface** - Real-time conversation with Alexa
- **Information & Q&A** - Query Claude AI for answers
- **Smart Home Control** - Control simulated IoT devices
- **Task Management** - Create and manage tasks
- **Reminders** - Set reminders and time-based notifications
- **Business Workflows** - Execute custom workflows
- **Agent Communication** - Have deep conversations with AI agent
- **Admin Panel** - Manage data through Django admin

## 🚀 Quick Start

### Install Dependencies
```bash
cd d:\project\alexa_frontend_django
python -m venv venv
venv\Scripts\activate
pip install -r requirements.txt
```

### Setup Django
```bash
# Create database
python manage.py migrate

# Create admin user
python manage.py createsuperuser

# Collect static files
python manage.py collectstatic --noinput
```

### Run Development Server
```bash
python manage.py runserver
```

Access at: `http://localhost:8000`

## 🔗 URLs

| URL | Purpose |
|-----|---------|
| `/` | Dashboard home |
| `/chat/` | Chat interface |
| `/information/` | Information & Q&A |
| `/smart-home/` | Smart home control |
| `/tasks/` | Task management |
| `/workflows/` | Business workflows |
| `/agent/` | Agent communication |
| `/admin/` | Django admin panel |
| `/api/health/` | Backend health check |
| `/api/debug/` | Debug information |

## 🔐 Admin Access

- URL: `http://localhost:8000/admin/`
- Username: (what you created with `createsuperuser`)
- Password: (what you created)

## 📊 Database

- **SQLite** (default - `db.sqlite3`)
- Models:
  - `ConversationLog` - Chat history
  - `TaskLog` - Tasks and to-do items
  - `SmartHomeDevice` - Connected devices

## 🎯 Integration with Flask Backend

This Django app connects to the Flask backend running on `http://localhost:5000`

**Endpoints assumed on Flask backend:**
- `POST /` - Main Alexa skill endpoint
- `GET /health` - Health check
- `GET /debug` - Debug info
- `POST /query` - Process natural language queries
- `POST /smart_home` - Smart home commands
- `POST /task` - Task management
- `POST /reminder` - Reminder creation
- `POST /workflow` - Workflow execution
- `POST /agent` - Agent communication

## 🛠️ Development

### Add a New Page

1. Create a view in `dashboard/views.py`:
```python
def my_feature(request):
    return render(request, 'dashboard/my_feature.html')
```

2. Add URL in `dashboard/urls.py`:
```python
path('my-feature/', views.my_feature, name='my_feature'),
```

3. Create template `dashboard/templates/dashboard/my_feature.html`

4. Add link in navigation (base.html)

### Modify Static Files

- CSS: `dashboard/static/css/style.css`
- JS: `dashboard/static/js/main.js`
- Auto-loaded by Django

### Customize Bootstrap

Edit `base.html` to change Bootstrap CDN version or add custom CSS

## 🚨 Troubleshooting

### Frontend won't load
```bash
python manage.py runserver 0.0.0.0:8000
# Then visit: http://192.168.1.10:8000
```

### Backend connection fails
- Check Flask server is running on port 5000
- Verify `.env` has correct `FLASK_API_URL`
- Check `/api/health/` endpoint

### Database errors
```bash
# Fresh database
rm db.sqlite3
python manage.py migrate
```

## 📝 Environment Variables

| Variable | Default | Purpose |
|----------|---------|---------|
| `DEBUG` | True | Debug mode (disable for production) |
| `SECRET_KEY` | See `.env` | Django security key |
| `ALLOWED_HOSTS` | localhost,127.0.0.1 | Allowed domains |
| `FLASK_API_URL` | http://localhost:5000 | Backend API URL |

## 🔄 Workflow

1. **Django Frontend** (This project) runs on port 8000
2. **Flask Backend** (Alexa skill) runs on port 5000
3. **Frontend** makes HTTP requests to **Backend**
4. **Backend** processes with Claude AI and local storage
5. **Frontend** displays results to user

## 📚 File Structure

```
alexa_frontend_django/
├── manage.py                          # Django entry point
├── requirements.txt                   # Dependencies
├── .env                               # Configuration
├── db.sqlite3                         # Database (auto-created)
│
├── alexa_frontend/                    # Project settings
│   ├── settings.py
│   ├── urls.py
│   ├── wsgi.py
│   └── __init__.py
│
└── dashboard/                         # Main app
    ├── models.py                      # Database models
    ├── views.py                       # View logic
    ├── forms.py                       # HTML forms
    ├── urls.py                        # URL routing
    ├── admin.py                       # Admin config
    ├── apps.py                        # App configuration
    ├── templates/dashboard/           # HTML templates
    │   ├── base.html
    │   ├── index.html
    │   ├── chat.html
    │   ├── information.html
    │   ├── smart_home.html
    │   ├── tasks.html
    │   ├── workflows.html
    │   └── agent.html
    └── static/
        ├── css/style.css
        └── js/main.js
```

## 🚀 Deployment

For production:

1. Set `DEBUG=False` in `.env`
2. Generate new `SECRET_KEY`
3. Use PostgreSQL or MySQL
4. Deploy to Heroku, AWS, or Azure
5. Use Gunicorn + Nginx as reverse proxy
6. Enable HTTPS/SSL

## 📞 Support

- Check Flask backend logs for API errors
- Use `/api/debug/` to inspect backend state
- Check Django admin for data issues
- Review browser console for JavaScript errors

---

**Quick Start Checklist:**
- [ ] Python 3.11+ installed
- [ ] Virtual environment created and activated
- [ ] Dependencies installed
- [ ] `.env` configured with Flask API URL
- [ ] Database migrated
- [ ] Admin user created
- [ ] Django server running on port 8000
- [ ] Flask backend running on port 5000
- [ ] Both servers accessible
