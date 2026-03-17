"""
Music Controller - Control music playback across different platforms
"""

import subprocess
import os
from typing import Optional
import requests
import webbrowser

class MusicController:
    def __init__(self):
        """Initialize music controller"""
        self.current_player = None
    
    # ==================== Windows Media Player ====================
    
    def wmp_play(self, file_path: Optional[str] = None):
        """Play media in Windows Media Player"""
        if file_path:
            subprocess.Popen(['C:\\Program Files\\Windows Media Player\\wmplayer.exe', file_path])
        else:
            # Play current media or resume
            self._send_wmp_command('Play')
    
    def wmp_pause(self):
        """Pause Windows Media Player"""
        self._send_wmp_command('Pause')
    
    def wmp_next(self):
        """Next track in Windows Media Player"""
        self._send_wmp_command('Next')
    
    def wmp_previous(self):
        """Previous track in Windows Media Player"""
        self._send_wmp_command('Previous')
    
    def wmp_stop(self):
        """Stop Windows Media Player"""
        self._send_wmp_command('Stop')
    
    def _send_wmp_command(self, command: str):
        """Send command to Windows Media Player via shell"""
        try:
            # This is a simplified approach - WMP can be controlled via COM objects
            subprocess.run(['wmic', 'path', 'win32_process', 'call', 'create', 
                           f'nircmd.exe sendkey {command}'], check=False)
        except:
            pass
    
    # ==================== Spotify ====================
    
    def spotify_play(self, query: Optional[str] = None):
        """
        Play on Spotify
        
        Args:
            query: Song/artist name or Spotify URI
        """
        try:
            if query:
                # Open Spotify with search
                webbrowser.open(f'spotify:search:{query}')
            else:
                # Play current
                subprocess.Popen(['spotify.exe'])
        except:
            print("Spotify not installed")
    
    def spotify_pause(self):
        """Pause Spotify"""
        try:
            # Use keyboard shortcut
            os.system('nircmd.exe sendkey space')
        except:
            pass
    
    def spotify_next(self):
        """Next track on Spotify"""
        try:
            os.system('nircmd.exe sendkey next')
        except:
            pass
    
    def spotify_previous(self):
        """Previous track on Spotify"""
        try:
            os.system('nircmd.exe sendkey prev')
        except:
            pass
    
    # ==================== YouTube Music ====================
    
    def youtube_search(self, query: str):
        """Search and play on YouTube Music"""
        try:
            webbrowser.open(f'https://music.youtube.com/search?q={query}')
        except:
            print("Could not open YouTube Music")
    
    # ==================== Local Files ====================
    
    def play_local_file(self, file_path: str):
        """Play local audio file"""
        if not os.path.exists(file_path):
            return False
        
        try:
            # Use default media player
            os.startfile(file_path)
            return True
        except:
            return False
    
    def play_local_music_folder(self, folder_path: str = None):
        """Open music folder"""
        if folder_path is None:
            # Default Music folder
            folder_path = os.path.expandvars('%USERPROFILE%\\Music')
        
        if os.path.exists(folder_path):
            os.startfile(folder_path)
    
    # ==================== Generic Commands ====================
    
    def play(self, query: Optional[str] = None):
        """Generic play command (tries multiple platforms)"""
        if query:
            # Try Spotify first
            self.spotify_play(query)
        else:
            self.wmp_play()
    
    def pause(self):
        """Generic pause command"""
        # Try WMP
        self.wmp_pause()
        # Also try keyboard shortcut for Spotify
        try:
            os.system('nircmd.exe sendkey space')
        except:
            pass
    
    def next_track(self):
        """Next track"""
        self.wmp_next()
    
    def previous_track(self):
        """Previous track"""
        self.wmp_previous()
    
    def stop(self):
        """Stop playback"""
        self.wmp_stop()
    
    def volume_up(self):
        """Increase volume"""
        try:
            os.system('nircmd.exe changesysvolume 1000')
        except:
            pass
    
    def volume_down(self):
        """Decrease volume"""
        try:
            os.system('nircmd.exe changesysvolume -1000')
        except:
            pass
    
    def mute(self):
        """Mute system"""
        try:
            os.system('nircmd.exe mutesysvolume 2')
        except:
            pass
