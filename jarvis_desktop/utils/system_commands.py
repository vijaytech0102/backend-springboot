"""
System Commands - Execute Windows system operations
"""

import subprocess
import os
from typing import Dict
from datetime import datetime

try:
    import psutil
    PSUTIL_AVAILABLE = True
except ImportError:
    PSUTIL_AVAILABLE = False

class SystemCommands:
    def __init__(self):
        """Initialize system commands handler"""
        pass
    
    # ==================== System Information ====================
    
    def get_time(self) -> str:
        """Get current time"""
        return datetime.now().strftime("%I:%M %p")
    
    def get_date(self) -> str:
        """Get current date"""
        return datetime.now().strftime("%A, %B %d, %Y")
    
    def get_system_info(self) -> Dict:
        """Get system information"""
        if not PSUTIL_AVAILABLE:
            return {
                'cpu_usage': 'N/A',
                'memory': 'N/A',
                'disk': 'N/A',
                'battery': 'N/A',
            }
        
        return {
            'cpu_usage': psutil.cpu_percent(interval=1),
            'memory': psutil.virtual_memory().percent,
            'disk': psutil.disk_usage('C:\\').percent,
            'battery': psutil.sensors_battery().percent if psutil.sensors_battery() else None,
        }
    
    def get_weather(self, city: str = None) -> str:
        """Get weather (requires API)"""
        # This would need a weather API integration
        return "Weather feature not configured"
    
    # ==================== Volume Control ====================
    
    def set_volume(self, level: int):
        """Set system volume (0-100)"""
        level = max(0, min(100, level))
        try:
            os.system(f'nircmd.exe setsysvolume {int(level * 655.35)}')
        except:
            pass
    
    def get_volume(self) -> int:
        """Get current system volume"""
        try:
            output = subprocess.check_output('nircmd.exe getsysvolume', shell=True).decode()
            return int(output.split()[-1])
        except:
            return 50
    
    def mute(self):
        """Mute system"""
        os.system('nircmd.exe mutesysvolume 1')
    
    def unmute(self):
        """Unmute system"""
        os.system('nircmd.exe mutesysvolume 0')
    
    # ==================== Screen Control ====================
    
    def set_brightness(self, level: int):
        """Set screen brightness (0-100)"""
        level = max(0, min(100, level))
        try:
            os.system(f'nircmd.exe setbrightness {level}')
        except:
            pass
    
    def lock_screen(self):
        """Lock Windows"""
        os.system('rundll32.exe user32.dll,LockWorkStation')
    
    def sleep(self):
        """Put system to sleep"""
        os.system('rundll32.exe PowrProf.dll,SetSuspendState 0,1,0')
    
    def shutdown(self, delay: int = 60):
        """Shutdown computer"""
        os.system(f'shutdown /s /t {delay}')
    
    def restart(self, delay: int = 60):
        """Restart computer"""
        os.system(f'shutdown /r /t {delay}')
    
    def cancel_shutdown(self):
        """Cancel scheduled shutdown"""
        os.system('shutdown /a')
    
    # ==================== Window Management ====================
    
    def minimize_all_windows(self):
        """Minimize all open windows"""
        os.system('nircmd.exe sendkey alt+d alt+m')
    
    def show_desktop(self):
        """Show desktop / minimize all"""
        os.system('nircmd.exe sendkey win+d')
    
    def open_file_explorer(self, path: str = None):
        """Open File Explorer"""
        if path and os.path.exists(path):
            os.startfile(path)
        else:
            os.startfile('explorer.exe')
    
    # ==================== Network ====================
    
    def open_url(self, url: str):
        """Open URL in default browser"""
        if not url.startswith(('http://', 'https://')):
            url = 'https://' + url
        os.startfile(url)
    
    def get_ip_address(self) -> str:
        """Get local IP address"""
        try:
            import socket
            s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
            s.connect(('8.8.8.8', 80))
            ip = s.getsockname()[0]
            s.close()
            return ip
        except:
            return "Unknown"
    
    # ==================== Settings ====================
    
    def open_settings(self, section: str = None):
        """Open Windows Settings"""
        settings_map = {
            'display': 'ms-settings:display',
            'sound': 'ms-settings:sound',
            'bluetooth': 'ms-settings:bluetooth',
            'wifi': 'ms-settings:network-wifi',
            'apps': 'ms-settings:apps',
            'about': 'ms-settings:about',
        }
        
        url = settings_map.get(section.lower(), 'ms-settings:') if section else 'ms-settings:'
        os.system(f'start {url}')
    
    def open_control_panel(self):
        """Open Windows Control Panel"""
        os.system('control')
    
    # ==================== Task Management ====================
    
    def kill_process(self, process_name: str) -> bool:
        """Kill process by name"""
        try:
            os.system(f'taskkill /IM {process_name} /F')
            return True
        except:
            return False
    
    def get_running_processes(self) -> list:
        """Get list of running processes"""
        if not PSUTIL_AVAILABLE:
            return []
        
        processes = []
        for proc in psutil.process_iter(['pid', 'name']):
            try:
                processes.append(proc.info['name'])
            except:
                pass
        return sorted(list(set(processes)))
    
    # ==================== Clipboard ====================
    
    def copy_to_clipboard(self, text: str):
        """Copy text to clipboard"""
        try:
            import tkinter as tk
            root = tk.Tk()
            root.withdraw()
            root.clipboard_clear()
            root.clipboard_append(text)
            root.update()
            root.destroy()
        except:
            pass
    
    def paste_from_clipboard(self) -> str:
        """Get text from clipboard"""
        try:
            import tkinter as tk
            root = tk.Tk()
            root.withdraw()
            text = root.clipboard_get()
            root.destroy()
            return text
        except:
            return ""
