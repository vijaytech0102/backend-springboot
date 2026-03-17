"""
Text-to-Speech Module - Convert responses to voice output
"""

import pyttsx3
import threading
from typing import Optional

class TextToSpeech:
    def __init__(self, volume: int = 100, rate: int = 150):
        """
        Initialize text-to-speech engine
        
        Args:
            volume: Volume level (0-100)
            rate: Speech rate in words per minute
        """
        self.engine = pyttsx3.init()
        self.volume = min(100, max(0, volume)) / 100.0
        self.rate = rate
        self.is_speaking = False
        
        # Set voice properties
        self.engine.setProperty('volume', self.volume)
        self.engine.setProperty('rate', self.rate)
        
        # Get available voices
        self.voices = self.engine.getProperty('voices')
        # Use first English voice
        for voice in self.voices:
            if 'english' in voice.languages or 'en' in voice.languages:
                self.engine.setProperty('voice', voice.id)
                break
    
    def speak(self, text: str, wait: bool = True):
        """
        Speak text aloud
        
        Args:
            text: Text to speak
            wait: Wait for speech to finish if True
        """
        if not text or not text.strip():
            return
        
        self.is_speaking = True
        self.engine.say(text)
        self.engine.runAndWait()
        self.is_speaking = False
    
    def speak_async(self, text: str):
        """Speak text asynchronously (non-blocking)"""
        thread = threading.Thread(target=self.speak, args=(text, False))
        thread.daemon = True
        thread.start()
    
    def stop(self):
        """Stop current speech"""
        self.engine.stop()
        self.is_speaking = False
    
    def set_volume(self, volume: int):
        """Set volume (0-100)"""
        self.volume = min(100, max(0, volume)) / 100.0
        self.engine.setProperty('volume', self.volume)
    
    def set_rate(self, rate: int):
        """Set speech rate (words per minute)"""
        self.rate = rate
        self.engine.setProperty('rate', self.rate)
    
    def set_voice(self, voice_index: int = 0):
        """Set voice by index"""
        if 0 <= voice_index < len(self.voices):
            self.engine.setProperty('voice', self.voices[voice_index].id)
    
    def get_available_voices(self) -> list:
        """Get list of available voice names"""
        return [voice.name for voice in self.voices]
    
    def get_available_voices_full(self) -> list:
        """Get list of available voices with details"""
        result = []
        for i, voice in enumerate(self.voices):
            result.append({
                'index': i,
                'name': voice.name,
                'id': voice.id,
                'languages': voice.languages if hasattr(voice, 'languages') else []
            })
        return result
