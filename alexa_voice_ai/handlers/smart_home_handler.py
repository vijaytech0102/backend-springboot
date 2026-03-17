"""
Smart Home Control Handler - Simulate IoT device management.
Handles requests to control lights, temperature, locks, and other smart devices.
"""
import json
from ask_sdk_core.dispatch_components import AbstractRequestHandler
from ask_sdk_core.utils import is_intent_name
from ask_sdk_core.handler_input import HandlerInput

from utils.session_memory import session_memory
from utils.ssml_builder import ResponseFormatter, SSMLBuilder


# In-memory device state (would connect to real IoT in production)
SMART_DEVICES = {
    "lights": {
        "living_room": {"state": "off", "brightness": 0, "color": "white"},
        "bedroom": {"state": "off", "brightness": 0, "color": "white"},
        "kitchen": {"state": "off", "brightness": 0, "color": "white"},
    },
    "thermostats": {
        "main": {"temperature": 70, "mode": "auto", "humidity": 45},
    },
    "locks": {
        "front_door": {"state": "locked"},
        "garage": {"state": "locked"},
    },
    "cameras": {
        "front_porch": {"recording": False, "motion_detection": True},
        "back_patio": {"recording": False, "motion_detection": True},
    }
}


class LightControlHandler(AbstractRequestHandler):
    """Handle smart light control (turn on/off, brightness, color)."""
    
    can_handle_func = is_intent_name("LightControlIntent")
    
    def handle(self, handler_input: HandlerInput):
        """Control smart lights."""
        request = handler_input.request_envelope.request
        user_id = handler_input.request_envelope.context.system.user.user_id
        session_id = handler_input.request_envelope.session.session_id
        
        # Extract slots
        location = request.intent.slots.get("location", {}).value or "living_room"
        action = request.intent.slots.get("action", {}).value or "on"
        brightness = request.intent.slots.get("brightness", {}).value
        
        # Normalize inputs
        location = location.lower().replace(" ", "_")
        action = action.lower()
        
        if location not in SMART_DEVICES["lights"]:
            return ResponseFormatter() \
                .set_speech(f"I don't have a light in the {location}. Available rooms are: {', '.join(SMART_DEVICES['lights'].keys())}") \
                .build()
        
        if action in ["on", "turn on"]:
            SMART_DEVICES["lights"][location]["state"] = "on"
            SMART_DEVICES["lights"][location]["brightness"] = int(brightness or 100)
            response_text = f"I've turned on the {location} light"
            if brightness:
                response_text += f" to {brightness} percent brightness"
            response_text += "."
        
        elif action in ["off", "turn off"]:
            SMART_DEVICES["lights"][location]["state"] = "off"
            SMART_DEVICES["lights"][location]["brightness"] = 0
            response_text = f"I've turned off the {location} light."
        
        elif action in ["dim", "brighten"]:
            new_brightness = int(brightness or 50)
            SMART_DEVICES["lights"][location]["brightness"] = new_brightness
            response_text = f"I've set the {location} light to {new_brightness} percent brightness."
        
        else:
            response_text = f"I'm not sure how to {action} the lights."
        
        # Record interaction
        session_memory.add_turn(user_id, session_id, f"Control lights: {action} {location}", response_text, "LightControlIntent")
        
        return ResponseFormatter() \
            .set_speech(response_text) \
            .set_card("Light Control", response_text) \
            .build()


class ThermostatControlHandler(AbstractRequestHandler):
    """Handle temperature and HVAC control."""
    
    can_handle_func = is_intent_name("ThermostatIntent")
    
    def handle(self, handler_input: HandlerInput):
        """Control thermostat."""
        request = handler_input.request_envelope.request
        user_id = handler_input.request_envelope.context.system.user.user_id
        session_id = handler_input.request_envelope.session.session_id
        
        action = request.intent.slots.get("action", {}).value or "status"
        temperature = request.intent.slots.get("temperature", {}).value
        mode = request.intent.slots.get("mode", {}).value
        
        thermostat = SMART_DEVICES["thermostats"]["main"]
        
        if action and "set" in action.lower() and temperature:
            target_temp = int(temperature)
            thermostat["temperature"] = target_temp
            response_text = f"I've set the temperature to {target_temp} degrees."
        
        elif mode:
            thermostat["mode"] = mode.lower()
            response_text = f"I've switched the thermostat to {mode} mode."
        
        else:
            current_temp = thermostat["temperature"]
            mode = thermostat["mode"]
            response_text = f"The current temperature is {current_temp} degrees and the thermostat is in {mode} mode."
        
        session_memory.add_turn(user_id, session_id, f"Thermostat: {action}", response_text, "ThermostatIntent")
        
        return ResponseFormatter() \
            .set_speech(response_text) \
            .set_card("Thermostat Control", response_text) \
            .build()


class LockControlHandler(AbstractRequestHandler):
    """Handle door locks and security."""
    
    can_handle_func = is_intent_name("LockIntent")
    
    def handle(self, handler_input: HandlerInput):
        """Control smart locks."""
        request = handler_input.request_envelope.request
        user_id = handler_input.request_envelope.context.system.user.user_id
        session_id = handler_input.request_envelope.session.session_id
        
        location = request.intent.slots.get("location", {}).value or "front_door"
        action = request.intent.slots.get("action", {}).value or "status"
        
        location = location.lower().replace(" ", "_")
        location = location if location in SMART_DEVICES["locks"] else "front_door"
        
        lock = SMART_DEVICES["locks"][location]
        
        if action and "lock" in action.lower():
            lock["state"] = "locked"
            response_text = f"I've locked the {location}."
        elif action and "unlock" in action.lower():
            # Confirmation needed for security
            response_text = f"For security, please confirm: should I unlock the {location}?"
        else:
            state = lock["state"]
            response_text = f"The {location} is currently {state}."
        
        session_memory.add_turn(user_id, session_id, f"Lock: {action} {location}", response_text, "LockIntent")
        
        return ResponseFormatter() \
            .set_speech(response_text) \
            .set_card("Lock Control", response_text) \
            .build()


class CameraControlHandler(AbstractRequestHandler):
    """Handle security cameras."""
    
    can_handle_func = is_intent_name("CameraIntent")
    
    def handle(self, handler_input: HandlerInput):
        """Control cameras."""
        request = handler_input.request_envelope.request
        user_id = handler_input.request_envelope.context.system.user.user_id
        session_id = handler_input.request_envelope.session.session_id
        
        location = request.intent.slots.get("location", {}).value or "front_porch"
        action = request.intent.slots.get("action", {}).value or "status"
        
        location = location.lower().replace(" ", "_")
        camera = SMART_DEVICES["cameras"].get(location, SMART_DEVICES["cameras"]["front_porch"])
        
        if action and "record" in action.lower():
            camera["recording"] = True
            response_text = f"I've started recording on the {location} camera."
        elif action and "stop" in action.lower():
            camera["recording"] = False
            response_text = f"I've stopped recording on the {location} camera."
        else:
            status = "recording" if camera["recording"] else "not recording"
            response_text = f"The {location} camera is currently {status}."
        
        session_memory.add_turn(user_id, session_id, f"Camera: {action}", response_text, "CameraIntent")
        
        return ResponseFormatter() \
            .set_speech(response_text) \
            .set_card("Camera Control", response_text) \
            .build()


class SmartHomeStatusHandler(AbstractRequestHandler):
    """Get overall smart home status."""
    
    can_handle_func = is_intent_name("SmartHomeStatusIntent")
    
    def handle(self, handler_input: HandlerInput):
        """Report overall smart home status."""
        user_id = handler_input.request_envelope.context.system.user.user_id
        session_id = handler_input.request_envelope.session.session_id
        
        # Build status summary
        status_parts = []
        
        # Lights
        lights_on = [name for name, device in SMART_DEVICES["lights"].items() if device["state"] == "on"]
        if lights_on:
            status_parts.append(f"Lights on: {', '.join(lights_on)}")
        
        # Temperature
        temp = SMART_DEVICES["thermostats"]["main"]["temperature"]
        status_parts.append(f"Thermostat set to {temp} degrees")
        
        # Locks
        unlocked = [name for name, device in SMART_DEVICES["locks"].items() if device["state"] == "unlocked"]
        if unlocked:
            status_parts.append(f"Warning: {', '.join(unlocked)} is unlocked")
        else:
            status_parts.append("All doors are locked")
        
        response_text = SSMLBuilder.build_response(status_parts, pause_between=300)
        
        session_memory.add_turn(user_id, session_id, "Request smart home status", response_text, "SmartHomeStatusIntent")
        
        return ResponseFormatter() \
            .set_speech(response_text) \
            .set_card("Smart Home Status", "\n".join(status_parts)) \
            .build()
