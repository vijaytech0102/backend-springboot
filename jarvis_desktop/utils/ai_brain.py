"""
AI Brain - Claude AI integration for intelligent command processing
"""

from anthropic import Anthropic
import json
from typing import Optional, Dict

class AiBrain:
    def __init__(self, api_key: str):
        """
        Initialize AI brain with Claude
        
        Args:
            api_key: Anthropic API key
        """
        self.client = Anthropic(api_key=api_key)
        self.conversation_history = []
        self.model = "claude-opus-4-1-20250805"  # Or claude-3-5-sonnet-20241022
        
        self.system_prompt = """You are Jarvis, a helpful Windows desktop assistant AI. Your name is Jarvis and users refer to you as Jarvis.

You help users with:
- Opening applications (Spotify, Discord, VSCode, Chrome, Firefox, etc.)
- Playing music
- Controlling system settings
- Providing information
- Setting reminders and tasks
- Executing Windows commands
- General assistance and conversation

When users ask you to do something, be helpful and respond naturally. If they ask to open an app, play music, or control the system, let them know what action you're taking.

Be concise but friendly. Keep responses short (1-2 sentences usually) since you'll be speaking responses aloud.

If you're asked to open something:
- Respond like: "Opening Spotify for you"
- Respond like: "Let me play some music"
- Respond like: "Launching Discord"

Available system capabilities:
- Open any Windows application
- Play music (Spotify, Windows Media Player, YouTube Music, local files)
- Control volume, brightness, sleep, shutdown
- Open websites and URLs
- Manage windows and tasks
- Get system information
- Show time, date, weather

Always be respectful and helpful."""
    
    def process_command(self, user_input: str) -> Dict:
        """
        Process user input and determine appropriate action
        
        Args:
            user_input: Voice command or text input
            
        Returns:
            Dict with 'response' (to speak) and 'action' (command to execute)
        """
        # Add to conversation history
        self.conversation_history.append({
            "role": "user",
            "content": user_input
        })
        
        # Get response from Claude
        response = self.client.messages.create(
            model=self.model,
            max_tokens=500,
            system=self.system_prompt,
            messages=self.conversation_history
        )
        
        assistant_message = response.content[0].text
        
        # Add to conversation history
        self.conversation_history.append({
            "role": "assistant",
            "content": assistant_message
        })
        
        # Keep conversation history manageable
        if len(self.conversation_history) > 20:
            self.conversation_history = self.conversation_history[-20:]
        
        # Parse response to extract action
        action = self._parse_action(user_input, assistant_message)
        
        return {
            'response': assistant_message,
            'action': action,
            'user_input': user_input
        }
    
    def _parse_action(self, user_input: str, response: str) -> Dict:
        """
        Parse command to determine what action to take
        
        Returns:
            Dict with action type and parameters
        """
        user_lower = user_input.lower()
        
        # Open App
        open_keywords = ['open', 'launch', 'start', 'run']
        if any(keyword in user_lower for keyword in open_keywords):
            return {
                'type': 'open_app',
                'app': user_input,
                'params': {}
            }
        
        # Play Music
        music_keywords = ['play', 'music', 'song', 'spotify', 'youtube']
        if any(keyword in user_lower for keyword in music_keywords):
            return {
                'type': 'play_music',
                'query': user_input.replace('play', '').replace('music', '').strip(),
                'params': {}
            }
        
        # Volume Control
        volume_keywords = ['volume', 'louder', 'quieter', 'mute', 'unmute']
        if any(keyword in user_lower for keyword in volume_keywords):
            if 'up' in user_lower or 'louder' in user_lower or 'increase' in user_lower:
                action_type = 'volume_up'
            elif 'down' in user_lower or 'quieter' in user_lower or 'decrease' in user_lower:
                action_type = 'volume_down'
            elif 'mute' in user_lower:
                action_type = 'mute'
            else:
                action_type = 'volume_control'
            
            return {
                'type': action_type,
                'params': {}
            }
        
        # Lock/Sleep/Shutdown
        if 'lock' in user_lower:
            return {'type': 'lock_screen', 'params': {}}
        elif 'sleep' in user_lower:
            return {'type': 'sleep', 'params': {}}
        elif 'shutdown' in user_lower or 'turn off' in user_lower:
            return {'type': 'shutdown', 'params': {'delay': 60}}
        elif 'restart' in user_lower:
            return {'type': 'restart', 'params': {'delay': 60}}
        
        # Time/Date
        if 'time' in user_lower:
            return {'type': 'get_time', 'params': {}}
        elif 'date' in user_lower:
            return {'type': 'get_date', 'params': {}}
        
        # URL/Website
        if 'open' in user_lower and any(domain in user_lower for domain in ['.com', '.org', 'website', 'site', 'google']):
            return {
                'type': 'open_url',
                'url': user_input.replace('open', '').strip(),
                'params': {}
            }
        
        # Conversation/Info
        return {
            'type': 'conversation',
            'params': {}
        }
    
    def get_conversation_history(self) -> list:
        """Get current conversation history"""
        return self.conversation_history.copy()
    
    def clear_history(self):
        """Clear conversation history"""
        self.conversation_history = []
    
    def get_summary(self) -> str:
        """Get conversation summary"""
        if not self.conversation_history:
            return "No conversation history"
        
        # Create summary request
        response = self.client.messages.create(
            model=self.model,
            max_tokens=200,
            messages=[
                {
                    "role": "user",
                    "content": "Summarize our conversation in 2-3 sentences"
                }
            ]
        )
        
        return response.content[0].text
