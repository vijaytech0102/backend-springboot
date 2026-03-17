"""
Human-to-AI Agent Communication Handler - Natural conversation with AI.
Handles free-form conversation, follow-ups, context-aware responses, and empathetic interaction.
"""
from ask_sdk_core.dispatch_components import AbstractRequestHandler
from ask_sdk_core.utils import is_intent_name
from ask_sdk_core.handler_input import HandlerInput

from utils.claude_brain import claude_brain
from utils.session_memory import session_memory
from utils.ssml_builder import ResponseFormatter, SSMLBuilder


class ConversationHandler(AbstractRequestHandler):
    """Handle free-form conversations with the AI agent."""
    
    can_handle_func = is_intent_name("AMAZON.FallbackIntent")
    
    def handle(self, handler_input: HandlerInput):
        """Handle conversation fallback - respond naturally to anything."""
        request = handler_input.request_envelope.request
        user_id = handler_input.request_envelope.context.system.user.user_id
        session_id = handler_input.request_envelope.session.session_id
        
        user_input = request.intent.slots.get("query", {}).value if request.intent.slots else ""
        
        if not user_input:
            return ResponseFormatter() \
                .set_speech("I'm not sure what you meant. Could you rephrase that?") \
                .set_reprompt("Could you say that again?") \
                .build()
        
        # Get conversation history for context
        history = session_memory.get_session_summary(user_id, session_id)
        
        # Use Claude for intelligent response
        response_text = claude_brain.ask(
            user_message=user_input,
            session_id=f"{user_id}:{session_id}",
            domain="agent_communication",
            context=history
        )
        
        # Record the interaction
        session_memory.add_turn(user_id, session_id, user_input, response_text, "AMAZON.FallbackIntent")
        
        return ResponseFormatter() \
            .set_speech(response_text) \
            .set_reprompt("Is there anything else I can help you with?") \
            .set_card("Conversation", f"Q: {user_input}\n\nA: {response_text}") \
            .build()


class FollowUpQuestionHandler(AbstractRequestHandler):
    """Handle follow-up questions in ongoing conversation."""
    
    can_handle_func = is_intent_name("FollowUpIntent")
    
    def handle(self, handler_input: HandlerInput):
        """Process follow-up questions."""
        request = handler_input.request_envelope.request
        user_id = handler_input.request_envelope.context.system.user.user_id
        session_id = handler_input.request_envelope.session.session_id
        
        follow_up = request.intent.slots.get("question", {}).value or "Tell me more"
        
        # Get full conversation history for context
        full_history = session_memory.get_conversation_history(user_id, session_id, limit=5)
        
        # Build context from history
        context_parts = []
        for turn in full_history[-2:]:  # Last 2 turns
            context_parts.append(f"Previously: User asked about {turn.get('user_input', 'topic')}")
        
        context = " ".join(context_parts)
        
        # Get Claude response with full context
        response_text = claude_brain.ask(
            user_message=follow_up,
            session_id=f"{user_id}:{session_id}",
            domain="agent_communication",
            context=context
        )
        
        session_memory.add_turn(user_id, session_id, follow_up, response_text, "FollowUpIntent")
        
        return ResponseFormatter() \
            .set_speech(response_text) \
            .set_reprompt("Do you have any other questions?") \
            .build()


class SentimentAwareHandler(AbstractRequestHandler):
    """Respond with empathy based on detected sentiment."""
    
    can_handle_func = is_intent_name("ShareFeelingIntent")
    
    def handle(self, handler_input: HandlerInput):
        """Respond empathetically to user's feelings."""
        request = handler_input.request_envelope.request
        user_id = handler_input.request_envelope.context.system.user.user_id
        session_id = handler_input.request_envelope.session.session_id
        
        user_message = request.intent.slots.get("message", {}).value or "I have something on my mind"
        
        # Analyze sentiment
        sentiment_data = claude_brain.analyze_sentiment(user_message, f"{user_id}:{session_id}")
        sentiment = sentiment_data.get("sentiment", "neutral")
        emotion = sentiment_data.get("emotion", "")
        
        # Build empathetic response based on sentiment
        if sentiment == "negative":
            empathy_intro = f"I'm sorry you're feeling {emotion}. "
            prompt = f"{empathy_intro}Tell me more about what's going on. How can I help?"
        elif sentiment == "positive":
            empathy_intro = f"That's wonderful that you're feeling {emotion}! "
            prompt = f"{empathy_intro}I'm happy for you. What can I do to support you?"
        else:
            prompt = f"I understand. Let me help you work through this. {user_message}"
        
        # Get supportive response
        response_text = claude_brain.ask(
            user_message=user_message,
            session_id=f"{user_id}:{session_id}",
            domain="agent_communication"
        )
        
        # Combine empathy prefix with AI response
        full_response = prompt + " " + response_text
        
        session_memory.add_turn(user_id, session_id, user_message, full_response, "ShareFeelingIntent")
        
        return ResponseFormatter() \
            .set_speech(full_response) \
            .set_card("Sentiment", f"{sentiment.capitalize()} ({emotion})") \
            .build()


class MultiTurnDialogHandler(AbstractRequestHandler):
    """Support extended multi-turn dialog for complex topics."""
    
    can_handle_func = is_intent_name("DialogIntent")
    
    def handle(self, handler_input: HandlerInput):
        """Handle complex multi-turn dialogs."""
        request = handler_input.request_envelope.request
        user_id = handler_input.request_envelope.context.system.user.user_id
        session_id = handler_input.request_envelope.session.session_id
        
        user_input = request.intent.slots.get("subject", {}).value or ""
        
        if not user_input:
            return ResponseFormatter() \
                .set_speech("What would you like to talk about?") \
                .set_reprompt("What's on your mind?") \
                .build()
        
        # Get conversation history
        history = session_memory.get_conversation_history(user_id, session_id, limit=10)
        turn_count = len(history)
        
        # Add context about conversation depth
        context_msg = ""
        if turn_count > 3:
            context_msg = "This has been an ongoing discussion. Continue building on previous points."
        
        prompt = f"""You are having an in-depth conversation with a user.
                   Current topic: {user_input}
                   Conversation depth: {turn_count} turns
                   {context_msg}
                   
                   Ask a thoughtful follow-up question to deepen the dialog."""
        
        response_text = claude_brain.ask(
            user_message=prompt,
            session_id=f"{user_id}:{session_id}",
            domain="agent_communication"
        )
        
        session_memory.add_turn(user_id, session_id, user_input, response_text, "DialogIntent")
        
        return ResponseFormatter() \
            .set_speech(response_text) \
            .set_reprompt("What are your thoughts on that?") \
            .set_card("Dialog", user_input) \
            .build()


class ClarifyingQuestionHandler(AbstractRequestHandler):
    """Ask clarifying questions when user input is ambiguous."""
    
    can_handle_func = is_intent_name("AmbiguousIntentIntent")
    
    def handle(self, handler_input: HandlerInput):
        """Handle ambiguous user requests by asking for clarification."""
        request = handler_input.request_envelope.request
        user_id = handler_input.request_envelope.context.system.user.user_id
        session_id = handler_input.request_envelope.session.session_id
        
        ambiguous_input = request.intent.slots.get("input", {}).value or ""
        
        # Use Claude to generate clarifying questions
        prompt = f"""The user said: "{ambiguous_input}"
                   
                   This is ambiguous. Generate 2-3 clarifying questions to better understand their intent.
                   Ask the questions naturally, as if you're having a conversation.
                   Keep the response brief and conversational."""
        
        response_text = claude_brain.ask(
            user_message=prompt,
            session_id=f"{user_id}:{session_id}",
            domain="agent_communication"
        )
        
        session_memory.add_turn(user_id, session_id, ambiguous_input, response_text, "AmbiguousIntentIntent")
        
        return ResponseFormatter() \
            .set_speech(response_text) \
            .set_reprompt("Could you clarify that for me?") \
            .build()


class SessionEndHandler(AbstractRequestHandler):
    """Handle session end gracefully with summary."""
    
    can_handle_func = is_intent_name("AMAZON.StopIntent")
    
    def handle(self, handler_input: HandlerInput):
        """End the session gracefully."""
        user_id = handler_input.request_envelope.context.system.user.user_id
        session_id = handler_input.request_envelope.session.session_id
        
        # Get session summary
        history = session_memory.get_conversation_history(user_id, session_id)
        
        if history:
            response_text = f"Thank you for our conversation today. We discussed {len(history)} topics. Goodbye!"
        else:
            response_text = "Thanks for using the AI Assistant. Goodbye!"
        
        # Save session if needed (optional)
        
        return ResponseFormatter() \
            .set_speech(response_text) \
            .end_session(True) \
            .set_card("Session Complete", "Goodbye") \
            .build()
