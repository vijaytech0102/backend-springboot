"""
Alexa Voice AI Skill - Flask Server Entry Point
Local Windows Development Environment (NOT AWS Lambda)

This Flask server exposes an Alexa skill interface that can be accessed via ngrok tunnel.
All secret management is done through .env files.
Session state is maintained in memory and persisted to JSON files as needed.
"""

import os
import json
import logging
from datetime import datetime
from dotenv import load_dotenv

from flask import Flask, request, jsonify
from ask_sdk_core.skill_builder import SkillBuilder
from ask_sdk_core.handler_input import HandlerInput
from ask_sdk_core.dispatch_components import AbstractRequestHandler, AbstractExceptionHandler
from ask_sdk_core.utils import is_request_type
from ask_sdk_webservice_support import webservice_handler

# Load environment variables
load_dotenv()

# Import all handlers
from handlers.info_handler import (
    InfoIntentHandler, 
    DictionaryIntentHandler, 
    NewsDigestIntentHandler,
    HelpIntentHandler,
    LaunchRequestHandler
)
from handlers.smart_home_handler import (
    LightControlHandler,
    ThermostatControlHandler,
    LockControlHandler,
    CameraControlHandler,
    SmartHomeStatusHandler
)
from handlers.task_handler import (
    CreateTaskHandler,
    SetReminderHandler,
    ListTasksHandler,
    CompleteTaskHandler,
    GetRemindersHandler,
    PrioritizeTaskHandler
)
from handlers.business_handler import (
    InitiateWorkflowHandler,
    ExecuteWorkflowStepHandler,
    GetWorkflowStatusHandler,
    ListAvailableWorkflowsHandler,
    CustomWorkflowHandler
)
from handlers.agent_handler import (
    ConversationHandler,
    FollowUpQuestionHandler,
    SentimentAwareHandler,
    MultiTurnDialogHandler,
    ClarifyingQuestionHandler,
    SessionEndHandler
)

# Configure logging
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)


# Exception handlers
class GenericExceptionHandler(AbstractExceptionHandler):
    """Catch-all exception handler for any unhandled exceptions."""
    
    can_handle_func = lambda self, handler_input, exception: True
    
    def handle(self, handler_input: HandlerInput, exception: Exception):
        logger.error(f"Unhandled exception: {str(exception)}", exc_info=True)
        return {
            "version": "1.0",
            "sessionAttributes": {},
            "response": {
                "outputSpeech": {
                    "type": "PlainText",
                    "text": "Sorry, an unexpected error occurred. Please try again."
                },
                "shouldEndSession": False
            }
        }


class SessionEndedRequestHandler(AbstractRequestHandler):
    """Handle session ended requests."""
    
    can_handle_func = is_request_type("SessionEndedRequest")
    
    def handle(self, handler_input: HandlerInput):
        """Log session end."""
        logger.info("Session ended request received")
        return {
            "version": "1.0",
            "sessionAttributes": {},
            "response": {
                "shouldEndSession": True
            }
        }


# Build the Alexa skill
def create_skill():
    """Create and configure the Alexa skill with all handlers."""
    sb = SkillBuilder()
    
    # Register launch and help handlers
    sb.add_request_handler(LaunchRequestHandler())
    sb.add_request_handler(HelpIntentHandler())
    sb.add_request_handler(SessionEndedRequestHandler())
    
    # Register Information & Q&A handlers
    sb.add_request_handler(InfoIntentHandler())
    sb.add_request_handler(DictionaryIntentHandler())
    sb.add_request_handler(NewsDigestIntentHandler())
    
    # Register Smart Home handlers
    sb.add_request_handler(SmartHomeStatusHandler())
    sb.add_request_handler(LightControlHandler())
    sb.add_request_handler(ThermostatControlHandler())
    sb.add_request_handler(LockControlHandler())
    sb.add_request_handler(CameraControlHandler())
    
    # Register Task & Reminder handlers
    sb.add_request_handler(CreateTaskHandler())
    sb.add_request_handler(SetReminderHandler())
    sb.add_request_handler(ListTasksHandler())
    sb.add_request_handler(CompleteTaskHandler())
    sb.add_request_handler(GetRemindersHandler())
    sb.add_request_handler(PrioritizeTaskHandler())
    
    # Register Business Workflow handlers
    sb.add_request_handler(InitiateWorkflowHandler())
    sb.add_request_handler(ExecuteWorkflowStepHandler())
    sb.add_request_handler(GetWorkflowStatusHandler())
    sb.add_request_handler(ListAvailableWorkflowsHandler())
    sb.add_request_handler(CustomWorkflowHandler())
    
    # Register Agent Communication handlers
    sb.add_request_handler(MultiTurnDialogHandler())
    sb.add_request_handler(SentimentAwareHandler())
    sb.add_request_handler(FollowUpQuestionHandler())
    sb.add_request_handler(ClarifyingQuestionHandler())
    sb.add_request_handler(SessionEndHandler())
    sb.add_request_handler(ConversationHandler())  # Fallback - must be last
    
    # Add exception handler
    sb.add_exception_handler(GenericExceptionHandler())
    
    return sb.create()


# Create Flask app
app = Flask(__name__)
logger.info("Initializing Alexa Skill Flask Server")

# Create skill instance
skill = create_skill()


@app.route('/health', methods=['GET'])
def health():
    """Health check endpoint."""
    return jsonify({
        "status": "running",
        "timestamp": datetime.now().isoformat(),
        "version": "1.0.0"
    }), 200


@app.route('/', methods=['POST'])
def alexa_skill():
    """Main Alexa skill endpoint."""
    try:
        # Log incoming request
        request_body = request.get_json()
        logger.info(f"Incoming request: {json.dumps(request_body, indent=2)[:500]}")  # Log first 500 chars
        
        # Process with ask-sdk skill
        # Note: verify_signature=False for local development
        response = webservice_handler(
            skill=skill,
            verify_signature=os.getenv("VERIFY_SIGNATURE", "False") == "True"
        )(request)
        
        logger.info(f"Response sent: {response.status_code}")
        return response
    
    except Exception as e:
        logger.error(f"Error processing request: {str(e)}", exc_info=True)
        return jsonify({
            "error": str(e),
            "timestamp": datetime.now().isoformat()
        }), 500


@app.route('/debug', methods=['GET'])
def debug_info():
    """Debug endpoint showing configuration and status."""
    debug_data = {
        "environment": os.getenv("FLASK_ENV", "development"),
        "skill_id": os.getenv("ALEXA_SKILL_ID", "not-configured"),
        "ngrok_url": os.getenv("NGROK_URL", "not-configured"),
        "verify_signature": os.getenv("VERIFY_SIGNATURE", "False"),
        "timestamp": datetime.now().isoformat(),
        "handlers_loaded": [
            "LaunchRequestHandler",
            "InfoIntentHandler",
            "DictionaryIntentHandler",
            "NewsDigestIntentHandler",
            "SmartHomeStatusHandler",
            "LightControlHandler",
            "ThermostatControlHandler",
            "LockControlHandler",
            "CameraControlHandler",
            "CreateTaskHandler",
            "SetReminderHandler",
            "ListTasksHandler",
            "CompleteTaskHandler",
            "GetRemindersHandler",
            "PrioritizeTaskHandler",
            "InitiateWorkflowHandler",
            "ExecuteWorkflowStepHandler",
            "GetWorkflowStatusHandler",
            "ListAvailableWorkflowsHandler",
            "CustomWorkflowHandler",
            "ConversationHandler",
            "FollowUpQuestionHandler",
            "SentimentAwareHandler",
            "MultiTurnDialogHandler",
            "ClarifyingQuestionHandler",
            "SessionEndHandler"
        ],
        "api_keys": {
            "anthropic": "✓ configured" if os.getenv("ANTHROPIC_API_KEY") else "✗ missing",
        }
    }
    return jsonify(debug_data), 200


@app.before_request
def log_request():
    """Log all incoming requests."""
    logger.info(f"{request.method} {request.path} from {request.remote_addr}")


@app.after_request
def log_response(response):
    """Log all outgoing responses."""
    logger.info(f"Response: {response.status_code}")
    return response


@app.errorhandler(404)
def not_found(error):
    """Handle 404 errors."""
    return jsonify({"error": "Endpoint not found"}), 404


@app.errorhandler(500)
def internal_error(error):
    """Handle 500 errors."""
    logger.error(f"Internal server error: {str(error)}")
    return jsonify({"error": "Internal server error"}), 500


def print_startup_banner():
    """Print startup information."""
    banner = """
    ╔════════════════════════════════════════════════════════════════╗
    ║          ALEXA VOICE AI SKILL - LOCAL DEVELOPMENT              ║
    ║                   Flask Server v1.0.0                          ║
    ╚════════════════════════════════════════════════════════════════╝
    
    🚀 Server started on: http://localhost:5000
    
    📝 Endpoints:
       POST /              → Alexa skill interface
       GET  /health       → Health check
       GET  /debug        → Debug information
    
    🌐 To expose to Alexa cloud:
       1. Open another terminal
       2. Run: ngrok http 5000
       3. Copy HTTPS URL from ngrok
       4. Go to Alexa Developer Console
       5. Set Endpoint → HTTPS → paste ngrok URL + /
       6. Save and test
    
    🔑 Configuration:
    """
    print(banner)
    
    # Print configuration status
    anthropic_key = "✓ Configured" if os.getenv("ANTHROPIC_API_KEY") else "✗ Missing"
    skill_id = os.getenv("ALEXA_SKILL_ID", "Not set")
    env = os.getenv("FLASK_ENV", "development")
    
    print(f"    Anthropic API Key: {anthropic_key}")
    print(f"    Alexa Skill ID: {skill_id}")
    print(f"    Environment: {env}")
    print(f"    Signature Verification: {os.getenv('VERIFY_SIGNATURE', 'False')}")
    
    print("\n    📚 Capability Domains Loaded:")
    print("       ✓ Information & Q&A (Claude-powered)")
    print("       ✓ Smart Home Control (Local simulation)")
    print("       ✓ Task & Reminder Management (JSON persistence)")
    print("       ✓ Business Workflows (Custom automation)")
    print("       ✓ Human-to-Agent Communication (Context-aware)")
    
    print("\n    💡 Tips:")
    print("       - Use /health to check server status")
    print("       - Use /debug to view configuration")
    print("       - Check the terminal for request/response logs")
    print("       - Press CTRL+C to stop the server")
    print("\n")


if __name__ == '__main__':
    print_startup_banner()
    
    # Run Flask app
    app.run(
        host='0.0.0.0',
        port=5000,
        debug=True,
        use_reloader=True
    )
