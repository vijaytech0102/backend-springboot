"""
Task & Reminder Management Handler - Create and manage tasks locally.
Handles task creation, reminders, to-do lists, and productivity management.
"""
import json
import os
from datetime import datetime
from ask_sdk_core.dispatch_components import AbstractRequestHandler
from ask_sdk_core.utils import is_intent_name
from ask_sdk_core.handler_input import HandlerInput

from utils.session_memory import session_memory
from utils.ssml_builder import ResponseFormatter


REMINDERS_FILE = "data/reminders.json"


def load_reminders():
    """Load reminders from file."""
    if os.path.exists(REMINDERS_FILE):
        with open(REMINDERS_FILE, 'r') as f:
            return json.load(f)
    return {"tasks": [], "reminders": []}


def save_reminders(data):
    """Save reminders to file."""
    os.makedirs(os.path.dirname(REMINDERS_FILE), exist_ok=True)
    with open(REMINDERS_FILE, 'w') as f:
        json.dump(data, f, indent=2)


class CreateTaskHandler(AbstractRequestHandler):
    """Create a new task or to-do item."""
    
    can_handle_func = is_intent_name("CreateTaskIntent")
    
    def handle(self, handler_input: HandlerInput):
        """Create a task."""
        request = handler_input.request_envelope.request
        user_id = handler_input.request_envelope.context.system.user.user_id
        session_id = handler_input.request_envelope.session.session_id
        
        task_description = request.intent.slots.get("task", {}).value
        priority = request.intent.slots.get("priority", {}).value or "normal"
        
        if not task_description:
            return ResponseFormatter() \
                .set_speech("What task would you like to create?") \
                .set_reprompt("Tell me the task") \
                .build()
        
        # Load and update reminders
        data = load_reminders()
        task = {
            "id": len(data["tasks"]) + 1,
            "user_id": user_id,
            "description": task_description,
            "priority": priority.lower(),
            "completed": False,
            "created_at": datetime.now().isoformat(),
            "due_date": None
        }
        
        data["tasks"].append(task)
        save_reminders(data)
        
        response_text = f"I've added '{task_description}' to your task list with {priority} priority."
        
        session_memory.add_turn(user_id, session_id, f"Create task: {task_description}", response_text, "CreateTaskIntent")
        
        return ResponseFormatter() \
            .set_speech(response_text) \
            .set_card("Task Created", task_description) \
            .build()


class SetReminderHandler(AbstractRequestHandler):
    """Set a reminder for a specific time."""
    
    can_handle_func = is_intent_name("SetReminderIntent")
    
    def handle(self, handler_input: HandlerInput):
        """Set a reminder."""
        request = handler_input.request_envelope.request
        user_id = handler_input.request_envelope.context.system.user.user_id
        session_id = handler_input.request_envelope.session.session_id
        
        reminder_text = request.intent.slots.get("reminder", {}).value
        time_slot = request.intent.slots.get("time", {}).value
        
        if not reminder_text:
            return ResponseFormatter() \
                .set_speech("What should I remind you about?") \
                .set_reprompt("What's the reminder?") \
                .build()
        
        # Load and update reminders
        data = load_reminders()
        reminder = {
            "id": len(data["reminders"]) + 1,
            "user_id": user_id,
            "text": reminder_text,
            "time": time_slot or "later",
            "created_at": datetime.now().isoformat(),
            "completed": False
        }
        
        data["reminders"].append(reminder)
        save_reminders(data)
        
        when = f" {time_slot}" if time_slot else ""
        response_text = f"I'll remind you to {reminder_text}{when}."
        
        session_memory.add_turn(user_id, session_id, f"Set reminder: {reminder_text}", response_text, "SetReminderIntent")
        
        return ResponseFormatter() \
            .set_speech(response_text) \
            .set_card("Reminder Set", reminder_text) \
            .build()


class ListTasksHandler(AbstractRequestHandler):
    """List all pending tasks for the user."""
    
    can_handle_func = is_intent_name("ListTasksIntent")
    
    def handle(self, handler_input: HandlerInput):
        """List user's tasks."""
        user_id = handler_input.request_envelope.context.system.user.user_id
        session_id = handler_input.request_envelope.session.session_id
        
        data = load_reminders()
        user_tasks = [t for t in data["tasks"] if t["user_id"] == user_id and not t["completed"]]
        
        if not user_tasks:
            response_text = "You don't have any pending tasks. Great job!"
        else:
            # Build a readable list
            task_list = []
            for task in user_tasks[:5]:  # Limit to first 5
                priority_badge = "🔴 " if task["priority"] == "high" else "🟡 " if task["priority"] == "normal" else ""
                task_list.append(f"{priority_badge}{task['description']}")
            
            response_text = f"You have {len(user_tasks)} tasks. Here are your top ones: " + ", ".join(task_list)
        
        session_memory.add_turn(user_id, session_id, "List tasks", response_text, "ListTasksIntent")
        
        return ResponseFormatter() \
            .set_speech(response_text) \
            .set_card("Your Tasks", "\n".join([t['description'] for t in user_tasks[:5]])) \
            .build()


class CompleteTaskHandler(AbstractRequestHandler):
    """Mark a task as complete."""
    
    can_handle_func = is_intent_name("CompleteTaskIntent")
    
    def handle(self, handler_input: HandlerInput):
        """Complete a task."""
        request = handler_input.request_envelope.request
        user_id = handler_input.request_envelope.context.system.user.user_id
        session_id = handler_input.request_envelope.session.session_id
        
        task_name = request.intent.slots.get("task", {}).value
        
        if not task_name:
            return ResponseFormatter() \
                .set_speech("Which task should I mark as complete?") \
                .set_reprompt("Which task?") \
                .build()
        
        data = load_reminders()
        matched_task = None
        
        for task in data["tasks"]:
            if task["user_id"] == user_id and task_name.lower() in task["description"].lower():
                task["completed"] = True
                matched_task = task
                break
        
        if matched_task:
            save_reminders(data)
            response_text = f"Great! I've marked '{matched_task['description']}' as complete. Keep up the good work!"
        else:
            response_text = f"I couldn't find a task matching '{task_name}'. Could you say it again?"
        
        session_memory.add_turn(user_id, session_id, f"Complete task: {task_name}", response_text, "CompleteTaskIntent")
        
        return ResponseFormatter() \
            .set_speech(response_text) \
            .set_card("Task Completed", task_name if matched_task else f"Task not found") \
            .build()


class GetRemindersHandler(AbstractRequestHandler):
    """Get upcoming reminders."""
    
    can_handle_func = is_intent_name("GetRemindersIntent")
    
    def handle(self, handler_input: HandlerInput):
        """Get user's active reminders."""
        user_id = handler_input.request_envelope.context.system.user.user_id
        session_id = handler_input.request_envelope.session.session_id
        
        data = load_reminders()
        user_reminders = [r for r in data["reminders"] if r["user_id"] == user_id and not r["completed"]]
        
        if not user_reminders:
            response_text = "You don't have any active reminders."
        else:
            reminder_list = [f"{r['text']} {r['time']}" for r in user_reminders[:3]]
            response_text = f"You have {len(user_reminders)} reminders: " + ", ".join(reminder_list)
        
        session_memory.add_turn(user_id, session_id, "Get reminders", response_text, "GetRemindersIntent")
        
        return ResponseFormatter() \
            .set_speech(response_text) \
            .set_card("Your Reminders", "\n".join([r['text'] for r in user_reminders[:5]])) \
            .build()


class PrioritizeTaskHandler(AbstractRequestHandler):
    """Help user prioritize their tasks."""
    
    can_handle_func = is_intent_name("PrioritizeTaskIntent")
    
    def handle(self, handler_input: HandlerInput):
        """Prioritize tasks based on user input."""
        user_id = handler_input.request_envelope.context.system.user.user_id
        session_id = handler_input.request_envelope.session.session_id
        
        data = load_reminders()
        high_priority = [t for t in data["tasks"] if t["user_id"] == user_id and t["priority"] == "high" and not t["completed"]]
        
        if high_priority:
            response_text = f"Your high priority tasks are: " + ", ".join([t['description'] for t in high_priority[:3]])
        else:
            response_text = "You don't have any high priority tasks. Would you like to set one?"
        
        session_memory.add_turn(user_id, session_id, "Prioritize tasks", response_text, "PrioritizeTaskIntent")
        
        return ResponseFormatter() \
            .set_speech(response_text) \
            .set_card("High Priority Tasks", "\n".join([t['description'] for t in high_priority])) \
            .build()
