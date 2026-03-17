"""
SSML Builder for Alexa voice responses.
Helps format responses with proper speech markup for natural sounding Alexa responses.
"""
import re
from typing import Optional


class SSMLBuilder:
    """
    Helper class to build SSML (Speech Synthesis Markup Language) responses for Alexa.
    SSML allows control over speech rate, pitch, volume, and other voice characteristics.
    """
    
    @staticmethod
    def text(content: str) -> str:
        """Wrap plain text in SSML tags."""
        return f"<speak>{content}</speak>"
    
    @staticmethod
    def pause(milliseconds: int = 500) -> str:
        """Insert a pause in the speech."""
        return f"<break time='{milliseconds}ms'/>"
    
    @staticmethod
    def emphasis(text: str, level: str = "moderate") -> str:
        """Add emphasis to text (level: strong, moderate, reduced)."""
        return f"<emphasis level='{level}'>{text}</emphasis>"
    
    @staticmethod
    def slow(text: str, rate: float = 0.8) -> str:
        """Slow down speech rate."""
        return f"<prosody rate='{rate}'>{text}</prosody>"
    
    @staticmethod
    def fast(text: str, rate: float = 1.2) -> str:
        """Speed up speech rate."""
        return f"<prosody rate='{rate}'>{text}</prosody>"
    
    @staticmethod
    def pitch(text: str, change: str = "+10%") -> str:
        """Change pitch of voice (e.g., '+10%', '-5%', 'high', 'low')."""
        return f"<prosody pitch='{change}'>{text}</prosody>"
    
    @staticmethod
    def volume(text: str, level: str = "loud") -> str:
        """Change volume (level: silent, x-soft, soft, medium, loud, x-loud)."""
        return f"<amazon:effect name='whispered'>{text}</amazon:effect>" if level == "whispered" else \
               f"<prosody volume='{level}'>{text}</prosody>"
    
    @staticmethod
    def spell_out(text: str) -> str:
        """Spell out individual characters."""
        return f"<amazon:effect phoneme='characters'>{text}</amazon:effect>"
    
    @staticmethod
    def build_response(parts: list, pause_between: int = 300) -> str:
        """
        Build a complete response from multiple parts.
        
        Args:
            parts: List of strings or SSML elements
            pause_between: Pause time in milliseconds between parts
        """
        combined = f"{SSMLBuilder.pause(pause_between)}".join(parts)
        return SSMLBuilder.text(combined)
    
    @staticmethod
    def clean_text(text: str) -> str:
        """Clean text by removing special characters that might affect SSML."""
        # Don't remove < or > if they're part of SSML tags
        # Only escape & when not part of an entity
        text = re.sub(r'&(?![a-zA-Z]+;)', '&amp;', text)
        text = text.replace('<', '&lt;').replace('>', '&gt;')
        text = text.replace('"', '&quot;').replace("'", '&apos;')
        return text


class ResponseFormatter:
    """
    Formats complete Alexa skill responses with card and speech output.
    """
    
    def __init__(self):
        self.speech_output = ""
        self.reprompt_text = ""
        self.card_title = ""
        self.card_content = ""
        self.should_end_session = False
    
    def set_speech(self, text: str, use_ssml: bool = True) -> 'ResponseFormatter':
        """Set the main speech output."""
        if use_ssml and not text.startswith("<speak>"):
            self.speech_output = SSMLBuilder.text(text)
        else:
            self.speech_output = text
        return self
    
    def set_reprompt(self, text: str, use_ssml: bool = True) -> 'ResponseFormatter':
        """Set the reprompt text if user doesn't respond."""
        if use_ssml and not text.startswith("<speak>"):
            self.reprompt_text = SSMLBuilder.text(text)
        else:
            self.reprompt_text = text
        return self
    
    def set_card(self, title: str, content: str) -> 'ResponseFormatter':
        """Set card content for display in Alexa app."""
        self.card_title = title
        self.card_content = content
        return self
    
    def end_session(self, should_end: bool = True) -> 'ResponseFormatter':
        """Set whether to end the session after this response."""
        self.should_end_session = should_end
        return self
    
    def build(self) -> dict:
        """Build the final Alexa response dictionary."""
        response = {
            "version": "1.0",
            "sessionAttributes": {},
            "response": {
                "outputSpeech": {
                    "type": "SSML" if self.speech_output.startswith("<speak>") else "PlainText",
                    "text": self.speech_output
                },
                "card": {
                    "type": "Simple",
                    "title": self.card_title,
                    "content": self.card_content
                }
            }
        }
        
        if self.reprompt_text:
            response["response"]["reprompt"] = {
                "outputSpeech": {
                    "type": "SSML" if self.reprompt_text.startswith("<speak>") else "PlainText",
                    "text": self.reprompt_text
                }
            }
        
        response["response"]["shouldEndSession"] = self.should_end_session
        
        return response


def format_error_response(error_message: str) -> dict:
    """Quick helper to format an error response."""
    return ResponseFormatter() \
        .set_speech(f"Sorry, an error occurred: {error_message}") \
        .set_reprompt("What would you like me to do?") \
        .build()
