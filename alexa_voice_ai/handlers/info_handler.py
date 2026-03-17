"""
Information & Q&A Handler - Powered by Claude AI.
Handles general questions, information lookup, and knowledge-based queries.
"""
from ask_sdk_core.dispatch_components import AbstractRequestHandler
from ask_sdk_core.utils import is_intent_name, is_request_type
from ask_sdk_core.handler_input import HandlerInput

from utils.claude_brain import claude_brain
from utils.session_memory import session_memory
from utils.ssml_builder import ResponseFormatter


class InfoIntentHandler(AbstractRequestHandler):
    """Handle user questions about general information and Q&A."""
    
    can_handle_func = is_intent_name("InfoIntent")
    
    def handle(self, handler_input: HandlerInput):
        """Process information queries using Claude."""
        request = handler_input.request_envelope.request
        session_attrs = handler_input.attributes_manager.session_attributes
        
        # Extract user and session IDs
        user_id = handler_input.request_envelope.context.system.user.user_id
        session_id = handler_input.request_envelope.session.session_id
        
        # Get the user's query
        query = request.intent.slots.get("query", {}).value if request.intent.slots else ""
        
        if not query:
            return ResponseFormatter() \
                .set_speech("I'm ready to answer your questions. What would you like to know?") \
                .set_reprompt("What's your question?") \
                .build()
        
        # Get conversation context
        context = session_memory.get_session_summary(user_id, session_id)
        
        # Use Claude to answer the question
        response_text = claude_brain.ask(
            user_message=query,
            session_id=f"{user_id}:{session_id}",
            domain="general_qa",
            context=context
        )
        
        # Record this interaction
        session_memory.add_turn(user_id, session_id, query, response_text, "InfoIntent")
        
        # Build response
        formatter = ResponseFormatter() \
            .set_speech(response_text) \
            .set_reprompt("Do you have another question?") \
            .set_card("Information", query)
        
        return formatter.build()


class DictionaryIntentHandler(AbstractRequestHandler):
    """Handle dictionary/definition lookup."""
    
    can_handle_func = is_intent_name("DictionaryIntent")
    
    def handle(self, handler_input: HandlerInput):
        """Define or explain a term."""
        request = handler_input.request_envelope.request
        user_id = handler_input.request_envelope.context.system.user.user_id
        session_id = handler_input.request_envelope.session.session_id
        
        term = request.intent.slots.get("term", {}).value if request.intent.slots else ""
        
        if not term:
            return ResponseFormatter() \
                .set_speech("What term would you like me to define?") \
                .set_reprompt("What term should I look up?") \
                .build()
        
        # Use Claude to define the term
        prompt = f"Define the term '{term}' in 1-2 sentences suitable for voice output. Be concise."
        response_text = claude_brain.ask(
            user_message=prompt,
            session_id=f"{user_id}:{session_id}",
            domain="general_qa"
        )
        
        return ResponseFormatter() \
            .set_speech(response_text) \
            .set_card("Definition", f"{term}: {response_text}") \
            .build()


class NewsDigestIntentHandler(AbstractRequestHandler):
    """Provide summaries on topics of interest."""
    
    can_handle_func = is_intent_name("NewsDigestIntent")
    
    def handle(self, handler_input: HandlerInput):
        """Provide a digest on a topic."""
        request = handler_input.request_envelope.request
        user_id = handler_input.request_envelope.context.system.user.user_id
        session_id = handler_input.request_envelope.session.session_id
        
        topic = request.intent.slots.get("topic", {}).value if request.intent.slots else ""
        
        if not topic:
            return ResponseFormatter() \
                .set_speech("What topic would you like a digest on?") \
                .set_reprompt("What topic?") \
                .build()
        
        # Use Claude for topical digest
        prompt = f"""Provide a 2-3 sentence summary of the latest trends and key points about {topic}. 
                   Focus on what a user should know right now. Be conversational."""
        
        response_text = claude_brain.ask(
            user_message=prompt,
            session_id=f"{user_id}:{session_id}",
            domain="general_qa"
        )
        
        return ResponseFormatter() \
            .set_speech(response_text) \
            .set_card(f"{topic} Digest", response_text) \
            .build()


class HelpIntentHandler(AbstractRequestHandler):
    """Built-in help intent - explain what the skill can do."""
    
    can_handle_func = is_intent_name("AMAZON.HelpIntent")
    
    def handle(self, handler_input: HandlerInput):
        """Help the user understand the skill's capabilities."""
        help_text = """I'm an AI-powered Alexa assistant with five main capabilities:
        
        First, I can answer general questions and provide information on any topic.
        
        Second, I can control smart home devices like lights and thermostats.
        
        Third, I can help you create tasks, set reminders, and organize your to-do list.
        
        Fourth, I can execute custom business workflows and automations tailored to your needs.
        
        And finally, I can communicate naturally with you about anything you need help with.
        
        What would you like to do?"""
        
        return ResponseFormatter() \
            .set_speech(help_text) \
            .set_reprompt("How can I help you?") \
            .set_card("Help", help_text) \
            .build()


class LaunchRequestHandler(AbstractRequestHandler):
    """Handle skill launch."""
    
    can_handle_func = is_request_type("LaunchRequest")
    
    def handle(self, handler_input: HandlerInput):
        """Welcome the user when they launch the skill."""
        user_id = handler_input.request_envelope.context.system.user.user_id
        session_memory.get_or_create_session(user_id, handler_input.request_envelope.session.session_id)
        
        welcome_text = """Welcome to the AI Assistant! 
                        I'm here to help with questions, smart home control, task management, 
                        business workflows, and much more. What can I do for you?"""
        
        return ResponseFormatter() \
            .set_speech(welcome_text) \
            .set_reprompt("What can I help you with?") \
            .set_card("Welcome", "AI Assistant Ready") \
            .build()
