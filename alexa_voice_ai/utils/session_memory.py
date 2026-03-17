"""
Multi-turn conversation context manager for Alexa skill.
Maintains session state and conversation history per user/session.
"""
import json
from datetime import datetime
from typing import Any, Optional, Dict, List


class SessionMemory:
    """
    In-memory session store for maintaining conversation context.
    Each session is identified by a combination of user ID and session ID.
    """
    
    def __init__(self):
        self.sessions: Dict[str, Dict[str, Any]] = {}
        self.max_history = 10  # Keep last 10 turns in history
    
    def get_or_create_session(self, user_id: str, session_id: str) -> Dict[str, Any]:
        """Get existing session or create new one."""
        key = f"{user_id}:{session_id}"
        
        if key not in self.sessions:
            self.sessions[key] = {
                "user_id": user_id,
                "session_id": session_id,
                "created_at": datetime.now().isoformat(),
                "last_updated": datetime.now().isoformat(),
                "conversation_history": [],
                "context": {
                    "current_intent": None,
                    "user_preferences": {},
                    "pending_tasks": [],
                    "smart_home_state": {},
                    "previous_queries": []
                },
                "tokens_used": 0
            }
        
        return self.sessions[key]
    
    def add_turn(self, user_id: str, session_id: str, user_input: str, alexa_response: str, intent: Optional[str] = None):
        """Add a conversation turn to history."""
        session = self.get_or_create_session(user_id, session_id)
        
        session["conversation_history"].append({
            "timestamp": datetime.now().isoformat(),
            "user_input": user_input,
            "alexa_response": alexa_response,
            "intent": intent
        })
        
        # Keep only last N turns
        if len(session["conversation_history"]) > self.max_history:
            session["conversation_history"] = session["conversation_history"][-self.max_history:]
        
        session["last_updated"] = datetime.now().isoformat()
    
    def get_conversation_history(self, user_id: str, session_id: str, limit: Optional[int] = None) -> List[Dict[str, Any]]:
        """Get previous conversation turns for context."""
        session = self.get_or_create_session(user_id, session_id)
        history = session["conversation_history"]
        
        if limit:
            history = history[-limit:]
        
        return history
    
    def set_context(self, user_id: str, session_id: str, key: str, value: Any):
        """Store context information for later retrieval."""
        session = self.get_or_create_session(user_id, session_id)
        session["context"][key] = value
    
    def get_context(self, user_id: str, session_id: str, key: str, default: Any = None) -> Any:
        """Retrieve stored context information."""
        session = self.get_or_create_session(user_id, session_id)
        return session["context"].get(key, default)
    
    def update_token_count(self, user_id: str, session_id: str, tokens: int):
        """Track API token usage per session."""
        session = self.get_or_create_session(user_id, session_id)
        session["tokens_used"] += tokens
    
    def get_session_summary(self, user_id: str, session_id: str) -> str:
        """Generate a summary of the session for context."""
        session = self.get_or_create_session(user_id, session_id)
        history = session["conversation_history"]
        
        if not history:
            return "No previous conversation history."
        
        summary_parts = []
        for turn in history[-3:]:  # Last 3 turns
            summary_parts.append(f"User: {turn['user_input']}")
            summary_parts.append(f"Alexa: {turn['alexa_response']}")
        
        return "\n".join(summary_parts)
    
    def clear_session(self, user_id: str, session_id: str):
        """Clear session data (useful for logout or end of session)."""
        key = f"{user_id}:{session_id}"
        if key in self.sessions:
            del self.sessions[key]
    
    def get_all_sessions_for_user(self, user_id: str) -> List[Dict[str, Any]]:
        """Get all sessions for a specific user."""
        return [s for s in self.sessions.values() if s["user_id"] == user_id]


# Global session manager instance
session_memory = SessionMemory()
