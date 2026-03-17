"""
Claude AI Brain - Wrapper around Anthropic Claude API.
Handles intelligent Q&A, instruction following, and context-aware responses.
"""
import os
import json
from typing import Optional, Dict, Any, List
from anthropic import Anthropic


class ClaudeBrain:
    """
    AI brain powered by Claude Sonnet for intelligent responses.
    Maintains conversation history for context-aware answers.
    """
    
    def __init__(self, api_key: Optional[str] = None):
        self.api_key = api_key or os.getenv("ANTHROPIC_API_KEY")
        if not self.api_key:
            raise ValueError("ANTHROPIC_API_KEY not found in environment variables")
        
        self.client = Anthropic()
        self.conversation_history: Dict[str, List[Dict[str, str]]] = {}
        self.model = "claude-sonnet-4-20250514"
        self.max_tokens = 1024
    
    def _get_system_prompt(self, domain: str) -> str:
        """Get domain-specific system prompt."""
        prompts = {
            "general_qa": """You are an intelligent Alexa voice assistant. 
                Answer questions concisely and clearly in 1-2 sentences suitable for voice output.
                Avoid technical jargon. Use natural, conversational language.""",
            
            "smart_home": """You are a smart home control expert. 
                You help users control their IoT devices through natural voice commands.
                Respond with clear confirmations like 'I've turned on the living room lights.'
                Always confirm the action taken.""",
            
            "task_management": """You are a task and reminder management assistant.
                Help users organize, create, and manage their tasks and reminders.
                Be encouraging and help break down large tasks into smaller steps.
                Respond concisely and actionably.""",
            
            "business_workflow": """You are a business process automation expert.
                Help execute custom business workflows and automations.
                Ask clarifying questions if needed, but try to infer intent from context.
                Provide step-by-step confirmation of actions.""",
            
            "agent_communication": """You are a helpful AI agent designed to communicate naturally with humans.
                Focus on understanding user intent and providing empathetic, helpful responses.
                Ask follow-up questions to better understand their needs.
                Be conversational and professional."""
        }
        return prompts.get(domain, prompts["general_qa"])
    
    def ask(self, user_message: str, session_id: str, domain: str = "general_qa", 
            context: Optional[str] = None) -> str:
        """
        Ask Claude a question with conversation history.
        
        Args:
            user_message: The user's question or input
            session_id: Unique session identifier for history tracking
            domain: The capability domain (general_qa, smart_home, task_management, etc.)
            context: Additional context about the user or situation
        
        Returns:
            Claude's response as a string
        """
        # Initialize session history if needed
        if session_id not in self.conversation_history:
            self.conversation_history[session_id] = []
        
        # Add user message to history
        self.conversation_history[session_id].append({
            "role": "user",
            "content": user_message
        })
        
        # Build system prompt with context
        system_prompt = self._get_system_prompt(domain)
        if context:
            system_prompt += f"\n\nAdditional context: {context}"
        
        try:
            # Call Claude API
            response = self.client.messages.create(
                model=self.model,
                max_tokens=self.max_tokens,
                system=system_prompt,
                messages=self.conversation_history[session_id]
            )
            
            # Extract response text
            assistant_message = response.content[0].text
            
            # Add to history
            self.conversation_history[session_id].append({
                "role": "assistant",
                "content": assistant_message
            })
            
            # Keep only last 10 messages to manage token usage
            if len(self.conversation_history[session_id]) > 20:
                self.conversation_history[session_id] = self.conversation_history[session_id][-20:]
            
            return assistant_message
        
        except Exception as e:
            print(f"Claude API error: {str(e)}")
            return f"Sorry, I encountered an error communicating with the AI service. Please try again."
    
    def analyze_sentiment(self, text: str, session_id: str) -> Dict[str, Any]:
        """Analyze sentiment of user message."""
        prompt = f"""Analyze the sentiment of this message and respond with JSON:
        Message: "{text}"
        
        Respond in this exact JSON format:
        {{"sentiment": "positive|negative|neutral", "confidence": 0.0-1.0, "emotion": "string"}}
        """
        
        try:
            response = self.client.messages.create(
                model=self.model,
                max_tokens=200,
                messages=[{"role": "user", "content": prompt}]
            )
            
            result_text = response.content[0].text
            # Extract JSON from response
            json_start = result_text.find('{')
            json_end = result_text.rfind('}') + 1
            json_str = result_text[json_start:json_end]
            return json.loads(json_str)
        except:
            return {"sentiment": "neutral", "confidence": 0.5, "emotion": "unknown"}
    
    def extract_entities(self, text: str) -> Dict[str, List[str]]:
        """Extract named entities from user input."""
        prompt = f"""Extract entities from this message. Respond with JSON.
        Message: "{text}"
        
        Response format:
        {{"entities": {{"DEVICE": [...], "LOCATION": [...], "ACTION": [...]}}}}
        """
        
        try:
            response = self.client.messages.create(
                model=self.model,
                max_tokens=300,
                messages=[{"role": "user", "content": prompt}]
            )
            
            result_text = response.content[0].text
            json_start = result_text.find('{')
            json_end = result_text.rfind('}') + 1
            json_str = result_text[json_start:json_end]
            return json.loads(json_str)
        except:
            return {"entities": {"DEVICE": [], "LOCATION": [], "ACTION": []}}
    
    def clear_history(self, session_id: str):
        """Clear conversation history for a session."""
        if session_id in self.conversation_history:
            del self.conversation_history[session_id]


# Global Claude brain instance
claude_brain = ClaudeBrain()
