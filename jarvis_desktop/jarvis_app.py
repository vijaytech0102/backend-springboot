"""
Jarvis Windows Desktop Application - Main PyQt5 GUI
"""

import sys
import os
import threading
from datetime import datetime
from typing import Optional
from dotenv import load_dotenv

from PyQt5.QtWidgets import (QApplication, QMainWindow, QWidget, QVBoxLayout, 
                             QHBoxLayout, QTextEdit, QPushButton, QLabel, 
                             QComboBox, QSpinBox, QSystemTrayIcon, QMenu)
from PyQt5.QtCore import Qt, QTimer, pyqtSignal, QObject, QThread
from PyQt5.QtGui import QIcon, QFont, QColor, QTextCursor, QPixmap, QPalette

# Import custom modules - with graceful fallback
try:
    from utils.voice_recognition import VoiceRecognizer
    VOICE_AVAILABLE = True
except ImportError:
    VOICE_AVAILABLE = False
    print("⚠️  Voice recognition not available (install SpeechRecognition)")

try:
    from utils.text_to_speech import TextToSpeech
    TTS_AVAILABLE = True
except ImportError:
    TTS_AVAILABLE = False
    print("⚠️  Text-to-speech not available (install pyttsx3)")

from utils.app_launcher import AppLauncher
from utils.music_controller import MusicController
from utils.system_commands import SystemCommands
from utils.ai_brain import AiBrain

# Load environment
load_dotenv()

class CommandSignals(QObject):
    """Signals for command thread communication"""
    response_ready = pyqtSignal(dict)
    error = pyqtSignal(str)

class CommandWorker(QThread):
    """Worker thread for processing commands"""
    def __init__(self, ai_brain: AiBrain, command: str, signals: CommandSignals):
        super().__init__()
        self.ai_brain = ai_brain
        self.command = command
        self.signals = signals
    
    def run(self):
        try:
            result = self.ai_brain.process_command(self.command)
            self.signals.response_ready.emit(result)
        except Exception as e:
            self.signals.error.emit(str(e))

class JarvisApp(QMainWindow):
    def __init__(self):
        super().__init__()
        
        # Initialize components
        self.api_key = os.getenv('ANTHROPIC_API_KEY', '').strip()
        if not self.api_key or self.api_key == 'sk-ant-your-api-key-here':
            print("⚠️  ANTHROPIC_API_KEY not set in .env")
            print("Visit: https://console.anthropic.com/ to get your API key")
            self.api_key = None
        
        self.ai_brain = None
        if self.api_key:
            try:
                self.ai_brain = AiBrain(self.api_key)
            except Exception as e:
                print(f"❌ Error initializing AI: {e}")
                self.api_key = None
        
        self.voice = None
        if VOICE_AVAILABLE:
            try:
                self.voice = VoiceRecognizer(
                    wake_words=['hey jarvis', 'jarvis'],
                    on_command=self.on_voice_command
                )
            except Exception as e:
                print(f"⚠️  Voice recognition error: {e}")
        
        self.tts = None
        if TTS_AVAILABLE:
            try:
                self.tts = TextToSpeech()
            except Exception as e:
                print(f"⚠️  Text-to-speech error: {e}")
        
        self.apps = AppLauncher()
        self.music = MusicController()
        self.system = SystemCommands()
        
        self.command_signals = CommandSignals()
        self.command_signals.response_ready.connect(self.on_command_result)
        self.command_signals.error.connect(self.on_command_error)
        
        # UI Setup
        self.init_ui()
        self.setup_tray()
        
        # Status
        self.listening = False
        self.processing = False
        
    def init_ui(self):
        """Initialize user interface"""
        self.setWindowTitle('Jarvis - Windows Desktop Assistant')
        self.setGeometry(100, 100, 900, 700)
        self.setStyleSheet(self._get_stylesheet())
        
        # Central widget
        central_widget = QWidget()
        self.setCentralWidget(central_widget)
        layout = QVBoxLayout(central_widget)
        layout.setSpacing(10)
        layout.setContentsMargins(20, 20, 20, 20)
        
        # Title
        title = QLabel('🎙️ Jarvis - Voice Assistant')
        title_font = QFont()
        title_font.setPointSize(18)
        title_font.setBold(True)
        title.setFont(title_font)
        layout.addWidget(title)
        
        # Status
        status_layout = QHBoxLayout()
        self.status_label = QLabel('Ready to listen')
        self.status_label.setStyleSheet("color: #4CAF50; font-weight: bold;")
        status_layout.addWidget(self.status_label)
        status_layout.addStretch()
        layout.addLayout(status_layout)
        
        # Conversation display
        self.conversation_display = QTextEdit()
        self.conversation_display.setReadOnly(True)
        self.conversation_display.setStyleSheet("""
            QTextEdit {
                background-color: #1e1e1e;
                color: #e0e0e0;
                border: 1px solid #333;
                border-radius: 5px;
                padding: 10px;
                font-family: 'Segoe UI';
                font-size: 11pt;
            }
        """)
        layout.addWidget(self.conversation_display, 4)
        
        # Input area
        input_label = QLabel('Voice Command or Text:')
        input_label.setStyleSheet("font-weight: bold;")
        layout.addWidget(input_label)
        
        # Input box
        input_layout = QHBoxLayout()
        self.input_text = QTextEdit()
        self.input_text.setMaximumHeight(80)
        self.input_text.setPlaceholderText('Type command or click "Listen" to use voice...')
        self.input_text.setStyleSheet("""
            QTextEdit {
                background-color: #2d2d2d;
                color: #e0e0e0;
                border: 1px solid #444;
                border-radius: 3px;
                padding: 5px;
                font-family: 'Segoe UI';
            }
        """)
        input_layout.addWidget(self.input_text)
        layout.addLayout(input_layout)
        
        # Control buttons
        button_layout = QHBoxLayout()
        
        # Listen button
        self.listen_btn = QPushButton('🎤 Listen')
        self.listen_btn.clicked.connect(self.start_listening)
        self.listen_btn.setStyleSheet(self._get_button_style('#2196F3'))
        button_layout.addWidget(self.listen_btn)
        
        # Send button
        self.send_btn = QPushButton('▶ Send')
        self.send_btn.clicked.connect(self.send_text_command)
        self.send_btn.setStyleSheet(self._get_button_style('#4CAF50'))
        button_layout.addWidget(self.send_btn)
        
        # Settings button
        self.settings_btn = QPushButton('⚙️ Settings')
        self.settings_btn.clicked.connect(self.show_settings)
        self.settings_btn.setStyleSheet(self._get_button_style('#FF9800'))
        button_layout.addWidget(self.settings_btn)
        
        # Clear button
        self.clear_btn = QPushButton('🗑️ Clear')
        self.clear_btn.clicked.connect(self.clear_conversation)
        self.clear_btn.setStyleSheet(self._get_button_style('#f44336'))
        button_layout.addWidget(self.clear_btn)
        
        layout.addLayout(button_layout)
        
        # Info section
        info_layout = QHBoxLayout()
        self.info_label = QLabel('Ready. Say "Hey Jarvis" to start or type a command.')
        self.info_label.setStyleSheet("color: #888; font-style: italic;")
        info_layout.addWidget(self.info_label)
        layout.addLayout(info_layout)
        
        # Print startup banner
        self.print_banner()
    
    def print_banner(self):
        """Print startup banner"""
        banner = """
===================================================
    JARVIS - Windows Desktop Assistant
===================================================

Voice Control Features:
  * Open Applications (Spotify, Discord, VSCode, etc.)
  * Play Music
  * Control Volume & Brightness
  * Lock/Sleep/Shutdown
  * Open Websites
  * Get System Information

Wake Words: "Hey Jarvis" / "Jarvis"

===================================================
        """
        try:
            self.log_message('system', banner)
            self.log_message('system', '[OK] Jarvis initialized and ready!')
            self.log_message('system', '[INFO] Click Listen or say "Hey Jarvis" to begin')
        except Exception as e:
            print(f"Banner error: {e}")
    
    def _get_stylesheet(self) -> str:
        """Get dark theme stylesheet"""
        return """
        QMainWindow {
            background-color: #121212;
        }
        
        QLabel {
            color: #e0e0e0;
        }
        
        QPushButton {
            border-radius: 5px;
            padding: 8px 16px;
            font-weight: bold;
            color: white;
            border: none;
        }
        
        QPushButton:hover {
            opacity: 0.9;
        }
        
        QPushButton:pressed {
            opacity: 0.7;
        }
        """
    
    def _get_button_style(self, color: str) -> str:
        """Get button styling"""
        return f"""
        QPushButton {{
            background-color: {color};
            border-radius: 5px;
            padding: 8px 16px;
            font-weight: bold;
            color: white;
            border: none;
        }}
        
        QPushButton:hover {{
            background-color: {color}dd;
        }}
        
        QPushButton:pressed {{
            background-color: {color}aa;
        }}
        """
    
    def setup_tray(self):
        """Setup system tray icon"""
        self.tray = QSystemTrayIcon(self)
        
        # Tray menu
        tray_menu = QMenu()
        show_action = tray_menu.addAction('Show')
        show_action.triggered.connect(self.show_window)
        listen_action = tray_menu.addAction('Listen')
        listen_action.triggered.connect(self.start_listening)
        tray_menu.addSeparator()
        quit_action = tray_menu.addAction('Quit')
        quit_action.triggered.connect(self.quit_app)
        
        self.tray.setContextMenu(tray_menu)
        self.tray.show()
    
    def start_listening(self):
        """Start listening for voice commands"""
        if not VOICE_AVAILABLE or not self.voice:
            self.log_message('error', '❌ Voice recognition not available. Please install SpeechRecognition')
            return
        
        if self.listening:
            self.voice.stop_listening()
            self.listening = False
            self.listen_btn.setText('🎤 Listen')
            self.status_label.setText('Stopped listening')
            self.status_label.setStyleSheet("color: #FF9800; font-weight: bold;")
        else:
            self.listening = True
            self.listen_btn.setText('⏹️ Stop Listening')
            self.status_label.setText('🎤 Listening for "Hey Jarvis"...')
            self.status_label.setStyleSheet("color: #2196F3; font-weight: bold;")
            
            # Start listening in background
            self.voice.start_listening()
    
    def on_voice_command(self, command: str):
        """Handle voice command"""
        self.log_message('user', f'🎤 {command}')
        self.process_command(command)
    
    def send_text_command(self):
        """Send text command"""
        command = self.input_text.toPlainText().strip()
        if not command:
            return
        
        self.input_text.clear()
        self.log_message('user', f'💬 {command}')
        self.process_command(command)
    
    def process_command(self, command: str):
        """Process command with AI"""
        if not self.ai_brain:
            self.log_message('error', '❌ AI not initialized. Please check ANTHROPIC_API_KEY in .env')
            return
        
        if self.processing:
            return
        
        self.processing = True
        self.status_label.setText('⏳ Processing...')
        self.status_label.setStyleSheet("color: #FF9800; font-weight: bold;")
        
        # Process in thread
        worker = CommandWorker(self.ai_brain, command, self.command_signals)
        worker.start()
    
    def on_command_result(self, result: dict):
        """Handle command result"""
        response = result.get('response', '')
        action = result.get('action', {})
        
        self.log_message('jarvis', response)
        
        # Execute action
        self.execute_action(action)
        
        # Speak response if available
        if TTS_AVAILABLE and self.tts:
            try:
                self.tts.speak_async(response)
            except Exception as e:
                print(f"TTS error: {e}")
        
        self.processing = False
        self.status_label.setText('✅ Ready')
        self.status_label.setStyleSheet("color: #4CAF50; font-weight: bold;")
    
    def on_command_error(self, error: str):
        """Handle command error"""
        self.log_message('error', f'❌ Error: {error}')
        self.processing = False
        self.status_label.setText('❌ Error')
        self.status_label.setStyleSheet("color: #f44336; font-weight: bold;")
    
    def execute_action(self, action: dict):
        """Execute system action"""
        action_type = action.get('type', '')
        params = action.get('params', {})
        
        if action_type == 'open_app':
            app_name = action.get('app', '').replace('open', '').replace('launch', '').strip()
            if self.apps.launch_app(app_name):
                self.log_message('action', f'📱 Opened: {app_name}')
            else:
                self.log_message('action', f'❌ Could not open: {app_name}')
        
        elif action_type == 'play_music':
            query = action.get('query', '')
            self.music.play(query)
            self.log_message('action', f'🎵 Playing: {query}')
        
        elif action_type == 'volume_up':
            self.system.volume_up()
            self.log_message('action', '🔊 Volume increased')
        
        elif action_type == 'volume_down':
            self.system.volume_down()
            self.log_message('action', '🔉 Volume decreased')
        
        elif action_type == 'mute':
            self.system.mute()
            self.log_message('action', '🔇 Muted')
        
        elif action_type == 'lock_screen':
            self.system.lock_screen()
            self.log_message('action', '🔒 Screen locked')
        
        elif action_type == 'sleep':
            self.system.sleep()
            self.log_message('action', '💤 Going to sleep')
        
        elif action_type == 'shutdown':
            self.system.shutdown(params.get('delay', 60))
            self.log_message('action', '🛑 Shutdown initiated')
        
        elif action_type == 'get_time':
            time_str = self.system.get_time()
            self.log_message('action', f'⏰ Time: {time_str}')
        
        elif action_type == 'get_date':
            date_str = self.system.get_date()
            self.log_message('action', f'📅 Date: {date_str}')
        
        elif action_type == 'open_url':
            url = action.get('url', '')
            self.system.open_url(url)
            self.log_message('action', f'🌐 Opening: {url}')
    
    def log_message(self, sender: str, message: str):
        """Log message to conversation display"""
        timestamp = datetime.now().strftime('%H:%M:%S')
        
        # Color based on sender
        if sender == 'user':
            color = '#2196F3'  # Blue
        elif sender == 'jarvis':
            color = '#4CAF50'  # Green
        elif sender == 'action':
            color = '#FF9800'  # Orange
        elif sender == 'system':
            color = '#9C27B0'  # Purple
        else:
            color = '#f44336'  # Red (error)
        
        formatted_msg = f'<span style="color: {color}; font-weight: bold;">[{timestamp}] {sender}:</span> {message}\n'
        self.conversation_display.insertHtml(formatted_msg)
        
        # Auto-scroll to bottom
        cursor = self.conversation_display.textCursor()
        cursor.movePosition(QTextCursor.End)
        self.conversation_display.setTextCursor(cursor)
    
    def show_settings(self):
        """Show settings dialog"""
        self.log_message('system', '⚙️  Settings: Not yet implemented')
    
    def clear_conversation(self):
        """Clear conversation display"""
        self.conversation_display.clear()
        self.ai_brain.clear_history()
        self.log_message('system', '🗑️ Conversation cleared')
    
    def show_window(self):
        """Show window from tray"""
        self.showNormal()
        self.activateWindow()
    
    def quit_app(self):
        """Quit application"""
        if self.voice and self.listening:
            self.voice.stop_listening()
        if self.tts:
            self.tts.stop()
        sys.exit()
    
    def closeEvent(self, event):
        """Handle close event - minimize to tray"""
        event.ignore()
        self.hide()

def main():
    app = QApplication(sys.argv)
    jarvis = JarvisApp()
    jarvis.show()
    sys.exit(app.exec_())

if __name__ == '__main__':
    main()
