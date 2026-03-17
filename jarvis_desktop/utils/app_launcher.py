"""
Windows App Launcher - Open applications by name
"""

import subprocess
import winreg
import os
from typing import Dict, List, Optional
import json

class AppLauncher:
    def __init__(self, favorites_file: str = 'config/favorites.json'):
        """
        Initialize app launcher
        
        Args:
            favorites_file: Path to favorites configuration file
        """
        self.favorites_file = favorites_file
        self.app_cache = {}
        self.favorites = self._load_favorites()
        self._scan_common_apps()
    
    def _load_favorites(self) -> Dict:
        """Load favorite apps from JSON file"""
        if os.path.exists(self.favorites_file):
            try:
                with open(self.favorites_file, 'r') as f:
                    return json.load(f)
            except:
                pass
        return {}
    
    def save_favorites(self):
        """Save favorites to JSON file"""
        os.makedirs(os.path.dirname(self.favorites_file) or '.', exist_ok=True)
        with open(self.favorites_file, 'w') as f:
            json.dump(self.favorites, f, indent=2)
    
    def add_favorite(self, name: str, path: str):
        """Add app to favorites"""
        self.favorites[name.lower()] = path
        self.save_favorites()
    
    def _scan_common_apps(self):
        """Scan common app locations"""
        common_paths = {
            'Spotify': 'C:\\Users\\%USERNAME%\\AppData\\Roaming\\Spotify\\spotify.exe',
            'Discord': 'C:\\Users\\%USERNAME%\\AppData\\Local\\Discord\\app-*\\Discord.exe',
            'VSCode': 'C:\\Program Files\\Microsoft VS Code\\Code.exe',
            'Chrome': 'C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe',
            'Firefox': 'C:\\Program Files\\Mozilla Firefox\\firefox.exe',
            'Notepad': 'C:\\Windows\\System32\\notepad.exe',
            'Calculator': 'C:\\Windows\\System32\\calc.exe',
            'Media Player': 'C:\\Program Files\\Windows Media Player\\wmplayer.exe',
            'Explorer': 'C:\\Windows\\explorer.exe',
            'Settings': 'C:\\Windows\\System32\\settings.exe',
        }
        
        for app_name, path in common_paths.items():
            path = os.path.expandvars(path)
            if os.path.exists(path):
                self.app_cache[app_name.lower()] = path
    
    def find_app(self, app_name: str) -> Optional[str]:
        """
        Find application path by name
        
        Args:
            app_name: Name of application
            
        Returns:
            Path to executable or None
        """
        app_name_lower = app_name.lower()
        
        # Check favorites first
        if app_name_lower in self.favorites:
            return self.favorites[app_name_lower]
        
        # Check cache
        if app_name_lower in self.app_cache:
            return self.app_cache[app_name_lower]
        
        # Search registry
        path = self._search_registry(app_name)
        if path:
            self.app_cache[app_name_lower] = path
            return path
        
        # Search Program Files
        path = self._search_program_files(app_name)
        if path:
            self.app_cache[app_name_lower] = path
            return path
        
        return None
    
    def _search_registry(self, app_name: str) -> Optional[str]:
        """Search Windows registry for app"""
        try:
            reg_path = r'SOFTWARE\Microsoft\Windows\CurrentVersion\App Paths'
            with winreg.OpenKey(winreg.HKEY_LOCAL_MACHINE, reg_path) as key:
                # Try exact match first
                try:
                    sub_key = winreg.OpenKey(key, f'{app_name}.exe')
                    path, _ = winreg.QueryValueEx(sub_key, '')
                    return path
                except:
                    pass
                
                # Try partial match
                i = 0
                while True:
                    try:
                        subkey = winreg.EnumKey(key, i)
                        if app_name.lower() in subkey.lower():
                            sub_key = winreg.OpenKey(key, subkey)
                            path, _ = winreg.QueryValueEx(sub_key, '')
                            return path
                        i += 1
                    except OSError:
                        break
        except:
            pass
        
        return None
    
    def _search_program_files(self, app_name: str) -> Optional[str]:
        """Search Program Files directories"""
        program_files = [
            os.path.expandvars('%ProgramFiles%'),
            os.path.expandvars('%ProgramFiles(x86)%'),
            os.path.expandvars('%LocalAppData%')
        ]
        
        for base_path in program_files:
            if not os.path.exists(base_path):
                continue
                
            for root, dirs, files in os.walk(base_path):
                for file in files:
                    if file.lower() == f'{app_name.lower()}.exe':
                        return os.path.join(root, file)
                
                # Limit search depth
                if root.count(os.sep) - base_path.count(os.sep) > 3:
                    dirs[:] = []
        
        return None
    
    def launch_app(self, app_name: str, args: List[str] = None) -> bool:
        """
        Launch application
        
        Args:
            app_name: Name of application to launch
            args: Additional command line arguments
            
        Returns:
            True if launched successfully
        """
        path = self.find_app(app_name)
        
        if not path:
            return False
        
        try:
            cmd = [path] + (args or [])
            subprocess.Popen(cmd)
            return True
        except Exception as e:
            print(f"Error launching {app_name}: {e}")
            return False
    
    def run_command(self, command: str) -> bool:
        """Run Windows command"""
        try:
            subprocess.Popen(command, shell=True)
            return True
        except:
            return False
    
    def get_all_apps(self) -> List[str]:
        """Get list of known apps"""
        apps = set()
        apps.update(self.app_cache.keys())
        apps.update(self.favorites.keys())
        return sorted(list(apps))
