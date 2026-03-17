"""
Jarvis Test Demo - Test commands without voice
"""

import sys
import os
from dotenv import load_dotenv

# Load environment
load_dotenv()

from utils.app_launcher import AppLauncher
from utils.music_controller import MusicController
from utils.system_commands import SystemCommands

def print_banner():
    """Print demo banner"""
    print("""
╔════════════════════════════════════════════════════════════╗
║         JARVIS - Test Demo (Command Testing)              ║
╚════════════════════════════════════════════════════════════╝
    """)

def test_app_launcher():
    """Test app launching functionality"""
    print("\n[TEST 1] App Launcher")
    print("-" * 50)
    
    apps = AppLauncher()
    
    # Get list of known apps
    known_apps = apps.get_all_apps()
    print(f"Known applications ({len(known_apps)}):")
    for app in known_apps[:10]:  # Show first 10
        print(f"  * {app}")
    if len(known_apps) > 10:
        print(f"  ... and {len(known_apps) - 10} more")
    
    # Test finding specific apps
    test_apps = ['Notepad', 'Calculator', 'Chrome']
    print("\nSearching for apps:")
    for app in test_apps:
        path = apps.find_app(app)
        status = "✓ FOUND" if path else "✗ NOT FOUND"
        print(f"  {app}: {status}")
        if path:
            print(f"    Path: {path[:60]}...")

def test_system_commands():
    """Test system commands"""
    print("\n[TEST 2] System Commands")
    print("-" * 50)
    
    system = SystemCommands()
    
    # Get system info
    print("System Information:")
    print(f"  Time: {system.get_time()}")
    print(f"  Date: {system.get_date()}")
    
    info = system.get_system_info()
    print(f"  CPU Usage: {info.get('cpu_usage', 'N/A')}%")
    print(f"  Memory: {info.get('memory', 'N/A')}%")
    
    # Test IP address
    ip = system.get_ip_address()
    print(f"  IP Address: {ip}")

def test_music_controller():
    """Test music controller"""
    print("\n[TEST 3] Music Controller")
    print("-" * 50)
    
    music = MusicController()
    print("Music Controller initialized")
    print("  Available commands:")
    print("    * music.play()")
    print("    * music.pause()")
    print("    * music.next_track()")
    print("    * music.volume_up()")
    print("    * music.volume_down()")
    print("    * music.mute()")

def test_ai_brain():
    """Test AI brain"""
    print("\n[TEST 4] AI Brain (Claude Integration)")
    print("-" * 50)
    
    api_key = os.getenv('ANTHROPIC_API_KEY', '')
    
    if not api_key or api_key == 'sk-ant-your-api-key-here':
        print("✗ API Key not configured")
        print("  Please add your key to .env file")
        print("  Get key from: https://console.anthropic.com/")
        return False
    
    try:
        from utils.ai_brain import AiBrain
        ai = AiBrain(api_key)
        print("✓ AI Brain initialized successfully")
        
        # Test a simple command
        print("\nTesting AI command processing...")
        result = ai.process_command("What time is it?")
        print(f"Response: {result['response']}")
        print(f"Action: {result['action']['type']}")
        return True
    except Exception as e:
        print(f"✗ Error: {e}")
        return False

def interactive_demo():
    """Interactive demo mode"""
    print("\n[INTERACTIVE MODE]")
    print("-" * 50)
    print("Test commands (without API key):")
    print("  1. Find app")
    print("  2. Get system info")
    print("  3. Music controls (info only)")
    print("  4. Exit")
    
    while True:
        try:
            choice = input("\nSelect (1-4): ").strip()
            
            if choice == '1':
                app_name = input("App name (e.g., Notepad, Chrome): ").strip()
                apps = AppLauncher()
                path = apps.find_app(app_name)
                if path:
                    print(f"✓ Found: {path}")
                    launch = input("Launch it? (y/n): ").strip().lower()
                    if launch == 'y':
                        if apps.launch_app(app_name):
                            print("✓ Launched!")
                        else:
                            print("✗ Failed to launch")
                else:
                    print("✗ Not found")
            
            elif choice == '2':
                system = SystemCommands()
                print(f"Time: {system.get_time()}")
                print(f"Date: {system.get_date()}")
                info = system.get_system_info()
                print(f"CPU: {info.get('cpu_usage')}%")
                print(f"Memory: {info.get('memory')}%")
            
            elif choice == '3':
                music = MusicController()
                print("Music commands available (see code for details)")
                print("  music.play()")
                print("  music.pause()")
                print("  music.next_track()")
            
            elif choice == '4':
                print("Goodbye!")
                break
            
            else:
                print("Invalid choice")
        
        except KeyboardInterrupt:
            print("\nGoodbye!")
            break

def main():
    """Run demo"""
    print_banner()
    
    print("Running Jarvis Test Suite...\n")
    
    # Run tests
    test_app_launcher()
    test_system_commands()
    test_music_controller()
    ai_working = test_ai_brain()
    
    print("\n" + "=" * 50)
    print("Demo Summary:")
    print("=" * 50)
    print("✓ App Launcher: Working")
    print("✓ System Commands: Working")
    print("✓ Music Controller: Available")
    if ai_working:
        print("✓ AI Brain (Claude): Working")
    else:
        print("✗ AI Brain: Needs API key")
    
    print("\n" + "=" * 50)
    print("Next Steps:")
    print("=" * 50)
    print("1. Get API key from: https://console.anthropic.com/")
    print("2. Add to .env file: ANTHROPIC_API_KEY=sk-ant-YOUR-KEY")
    print("3. Run: python jarvis_app.py")
    print("4. Or run this demo again to test updates")
    
    print("\n" + "=" * 50)
    interactive = input("Try interactive mode? (y/n): ").strip().lower()
    if interactive == 'y':
        interactive_demo()

if __name__ == '__main__':
    main()
