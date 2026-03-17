"""
Voice Recognition Module - Listens for wake words and captures user commands
"""

import threading
from typing import Callable, Optional
import os

try:
    import speech_recognition as sr
    SPEECH_RECOGNITION_AVAILABLE = True
except (ImportError, ModuleNotFoundError):
    SPEECH_RECOGNITION_AVAILABLE = False
    sr = None

class VoiceRecognizer:
    def __init__(self, wake_words: list = None, on_command: Callable = None):
        """
        Initialize voice recognizer with wake word detection
        
        Args:
            wake_words: List of wake words to listen for (e.g., ['hey jarvis', 'jarvis'])
            on_command: Callback function when command is recognized
        """
        if not SPEECH_RECOGNITION_AVAILABLE:
            self.recognizer = None
            self.microphone = None
        else:
            self.recognizer = sr.Recognizer()
            self.microphone = sr.Microphone()
        
        self.wake_words = wake_words or ['hey jarvis', 'jarvis']
        self.on_command = on_command
        self.is_listening = False
        self.listening_thread = None
        
        # Configure recognizer for better recognition
        if self.recognizer:
            self.recognizer.dynamic_energy_threshold = True
            self.recognizer.energy_threshold = 4000
        
    def start_listening(self):
        """Start background listening for wake words"""
        if not SPEECH_RECOGNITION_AVAILABLE:
            return  # Silently fail if not available
        
        if not self.is_listening:
            self.is_listening = True
            self.listening_thread = threading.Thread(target=self._listen_loop, daemon=True)
            self.listening_thread.start()
            print("🎤 Listening for wake words...")
    
    def stop_listening(self):
        """Stop listening"""
        self.is_listening = False
        if self.listening_thread:
            self.listening_thread.join(timeout=2)
    
    def _listen_loop(self):
        """Continuous listening loop for wake words"""
        if not SPEECH_RECOGNITION_AVAILABLE or not self.microphone:
            return
        
        with self.microphone as source:
            self.recognizer.adjust_for_ambient_noise(source, duration=1)
            
        while self.is_listening:
            try:
                with self.microphone as source:
                    print("Listening for wake word...")
                    audio = self.recognizer.listen(source, timeout=5, phrase_time_limit=5)
                    
                # Try to recognize with Google Speech API
                try:
                    text = self.recognizer.recognize_google(audio).lower()
                    print(f"Heard: {text}")
                    
                    # Check for wake words
                    for wake_word in self.wake_words:
                        if wake_word.lower() in text:
                            self._listen_for_command()
                            break
                            
                except sr.UnknownValueError:
                    continue
                except sr.RequestError as e:
                    print(f"Speech API error: {e}")
                    
            except sr.RequestError as e:
                print(f"Microphone error: {e}")
                continue
            except Exception as e:
                if self.is_listening:
                    print(f"Error in listening loop: {e}")
    
    def _listen_for_command(self):
        """Listen for command after wake word detected"""
        if not SPEECH_RECOGNITION_AVAILABLE or not self.microphone:
            return
        
        print("🎯 Wake word detected! Listening for command...")
        
        try:
            with self.microphone as source:
                self.recognizer.adjust_for_ambient_noise(source, duration=0.5)
                audio = self.recognizer.listen(source, timeout=10, phrase_time_limit=10)
            
            command = self.recognizer.recognize_google(audio).lower()
            print(f"📝 Command: {command}")
            
            if self.on_command:
                self.on_command(command)
                
        except sr.UnknownValueError:
            print("❌ Could not understand command")
        except sr.RequestError as e:
            print(f"Speech API error: {e}")
        except Exception as e:
            print(f"Error capturing command: {e}")
    
    def recognize_speech(self, timeout: int = 10) -> Optional[str]:
        """
        Listen for and recognize speech (one-time)
        
        Args:
            timeout: Max seconds to listen
            
        Returns:
            Recognized text or None
        """
        if not SPEECH_RECOGNITION_AVAILABLE or not self.microphone:
            return None
        
        try:
            with self.microphone as source:
                audio = self.recognizer.listen(source, timeout=timeout, phrase_time_limit=timeout)
            
            text = self.recognizer.recognize_google(audio)
            return text.lower()
            
        except sr.UnknownValueError:
            return None
        except sr.RequestError as e:
            print(f"Speech recognition error: {e}")
            return None

    def get_microphone_info(self) -> dict:
        """Get list of available microphones"""
        if not SPEECH_RECOGNITION_AVAILABLE:
            return {}
        
        result = {}
        for i, mic_name in enumerate(sr.Microphone.list_microphone_indexes()):
            try:
                mic = sr.Microphone(device_index=i)
                result[i] = f"Microphone {i}: {mic_name}"
            except:
                pass
        return result
