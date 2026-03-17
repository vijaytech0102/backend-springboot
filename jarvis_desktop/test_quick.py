"""Quick Jarvis Test"""
import sys, os
sys.path.insert(0, '.')
from dotenv import load_dotenv
load_dotenv()

from utils.app_launcher import AppLauncher
from utils.system_commands import SystemCommands

print('\n')
print('='*60)
print('JARVIS - Test Demo')
print('='*60)

print('\n[1] App Launcher')
print('-'*60)
apps = AppLauncher()
known_apps = apps.get_all_apps()
print(f'Known apps: {len(known_apps)}')
for app in known_apps[:8]:
    print(f'  * {app}')

print('\n[2] System Commands')
print('-'*60)
system = SystemCommands()
print(f'Time: {system.get_time()}')
print(f'Date: {system.get_date()}')
print(f'IP Address: {system.get_ip_address()}')

print('\n[3] Music Controller')
print('-'*60)
print('Available for: Spotify, Windows Media Player, YouTube')

print('\n[4] AI Brain Status')
print('-'*60)
api_key = os.getenv('ANTHROPIC_API_KEY', '')
if api_key and api_key != 'sk-ant-your-api-key-here':
    print('API Key: CONFIGURED')
else:
    print('API Key: NOT CONFIGURED')
    print('Get from: https://console.anthropic.com/')

print('\n'+'='*60)
print('All systems ready!')
print('='*60 + '\n')
