"""
Business Workflow Handler - Execute custom business processes and automations.
Handles workflow triggers, business logic, and process automation.
"""
from ask_sdk_core.dispatch_components import AbstractRequestHandler
from ask_sdk_core.utils import is_intent_name
from ask_sdk_core.handler_input import HandlerInput

from utils.claude_brain import claude_brain
from utils.session_memory import session_memory
from utils.ssml_builder import ResponseFormatter


# Mock business workflows
BUSINESS_WORKFLOWS = {
    "sales_pipeline": {
        "name": "Sales Pipeline Update",
        "description": "Log sales activities and update pipeline",
        "steps": ["Identify opportunity", "Log activity", "Update timeline", "Set follow-up"]
    },
    "customer_inquiry": {
        "name": "Customer Inquiry Handler",
        "description": "Route and manage customer  inquiries",
        "steps": ["Collect details", "Determine category", "Route to team", "Log response"]
    },
    "expense_approval": {
        "name": "Expense Approval Workflow",
        "description": "Review and approve business expenses",
        "steps": ["Submit receipt", "Verify amount", "Check budget", "Approve/Reject"]
    },
    "meeting_scheduler": {
        "name": "Meeting Scheduler",
        "description": "Schedule and manage meetings",
        "steps": ["Set date/time", "Invite attendees", "Send calendar", "Send reminder"]
    },
    "project_status": {
        "name": "Project Status Report",
        "description": "Generate project status updates",
        "steps": ["Collect metrics", "Assess completion", "Identify risks", "Report status"]
    }
}


class InitiateWorkflowHandler(AbstractRequestHandler):
    """Initiate a custom business workflow."""
    
    can_handle_func = is_intent_name("InitiateWorkflowIntent")
    
    def handle(self, handler_input: HandlerInput):
        """Start a business workflow."""
        request = handler_input.request_envelope.request
        user_id = handler_input.request_envelope.context.system.user.user_id
        session_id = handler_input.request_envelope.session.session_id
        
        workflow_type = request.intent.slots.get("workflow_type", {}).value or ""
        
        if not workflow_type:
            workflow_list = ", ".join(BUSINESS_WORKFLOWS.keys())
            return ResponseFormatter() \
                .set_speech(f"Which workflow would you like to initiate? Available options are: {workflow_list}") \
                .set_reprompt("Which workflow?") \
                .build()
        
        workflow_type = workflow_type.lower().replace(" ", "_")
        
        if workflow_type not in BUSINESS_WORKFLOWS:
            return ResponseFormatter() \
                .set_speech(f"I don't recognize that workflow. Please try one of the available workflows.") \
                .build()
        
        workflow = BUSINESS_WORKFLOWS[workflow_type]
        
        # Build workflow confirmation message
        steps_text = " → ".join(workflow["steps"][:2])  # Show first 2 steps
        response_text = f"Initiating {workflow['name']}. Here's the workflow: {steps_text}. Ready to proceed?"
        
        # Store workflow context
        session_memory.set_context(user_id, session_id, "current_workflow", workflow_type)
        session_memory.add_turn(user_id, session_id, f"Initiate workflow: {workflow_type}", response_text, "InitiateWorkflowIntent")
        
        return ResponseFormatter() \
            .set_speech(response_text) \
            .set_reprompt("Should I proceed with this workflow?") \
            .set_card("Workflow Initiated", f"{workflow['name']}\n{workflow['description']}") \
            .build()


class ExecuteWorkflowStepHandler(AbstractRequestHandler):
    """Execute a specific step in a workflow."""
    
    can_handle_func = is_intent_name("ExecuteStepIntent")
    
    def handle(self, handler_input: HandlerInput):
        """Execute a workflow step."""
        request = handler_input.request_envelope.request
        user_id = handler_input.request_envelope.context.system.user.user_id
        session_id = handler_input.request_envelope.session.session_id
        
        step_data = request.intent.slots.get("step_data", {}).value
        
        if not step_data:
            return ResponseFormatter() \
                .set_speech("What data should I process for this step?") \
                .set_reprompt("Tell me the data") \
                .build()
        
        current_workflow = session_memory.get_context(user_id, session_id, "current_workflow")
        
        if not current_workflow or current_workflow not in BUSINESS_WORKFLOWS:
            return ResponseFormatter() \
                .set_speech("No workflow is currently active. Please initiate a workflow first.") \
                .build()
        
        workflow = BUSINESS_WORKFLOWS[current_workflow]
        
        # Use Claude to process the step
        prompt = f"""You are processing a step in the '{workflow['name']}' workflow.
                   The data provided is: {step_data}
                   Current step: {workflow['steps'][0] if workflow['steps'] else 'Initial'}
                   
                   Process this data and confirm the action taken. Be concise and professional."""
        
        response_text = claude_brain.ask(
            user_message=prompt,
            session_id=f"{user_id}:{session_id}",
            domain="business_workflow"
        )
        
        session_memory.add_turn(user_id, session_id, f"Execute step: {step_data}", response_text, "ExecuteStepIntent")
        
        return ResponseFormatter() \
            .set_speech(response_text) \
            .set_card("Step Executed", f"Workflow: {workflow['name']}\nData: {step_data}") \
            .build()


class GetWorkflowStatusHandler(AbstractRequestHandler):
    """Get the status of a workflow."""
    
    can_handle_func = is_intent_name("GetWorkflowStatusIntent")
    
    def handle(self, handler_input: HandlerInput):
        """Get workflow status."""
        user_id = handler_input.request_envelope.context.system.user.user_id
        session_id = handler_input.request_envelope.session.session_id
        
        current_workflow = session_memory.get_context(user_id, session_id, "current_workflow")
        
        if not current_workflow or current_workflow not in BUSINESS_WORKFLOWS:
            response_text = "No workflow is currently active."
        else:
            workflow = BUSINESS_WORKFLOWS[current_workflow]
            status_steps = ", ".join(workflow["steps"])
            response_text = f"Current workflow: {workflow['name']}. Steps: {status_steps}. Use voice commands to execute each step."
        
        session_memory.add_turn(user_id, session_id, "Get workflow status", response_text, "GetWorkflowStatusIntent")
        
        return ResponseFormatter() \
            .set_speech(response_text) \
            .set_card("Workflow Status", response_text) \
            .build()


class ListAvailableWorkflowsHandler(AbstractRequestHandler):
    """List all available business workflows."""
    
    can_handle_func = is_intent_name("ListWorkflowsIntent")
    
    def handle(self, handler_input: HandlerInput):
        """List available workflows."""
        user_id = handler_input.request_envelope.context.system.user.user_id
        session_id = handler_input.request_envelope.session.session_id
        
        workflows = list(BUSINESS_WORKFLOWS.values())
        workflow_list = ", ".join([w["name"] for w in workflows])
        
        response_text = f"Available workflows are: {workflow_list}. Which one would you like to start?"
        
        session_memory.add_turn(user_id, session_id, "List workflows", response_text, "ListWorkflowsIntent")
        
        card_content = "\n".join([f"- {w['name']}: {w['description']}" for w in workflows])
        
        return ResponseFormatter() \
            .set_speech(response_text) \
            .set_card("Available Workflows", card_content) \
            .build()


class CustomWorkflowHandler(AbstractRequestHandler):
    """Execute a custom workflow defined by voice instruction."""
    
    can_handle_func = is_intent_name("CustomWorkflowIntent")
    
    def handle(self, handler_input: HandlerInput):
        """Execute a custom workflow."""
        request = handler_input.request_envelope.request
        user_id = handler_input.request_envelope.context.system.user.user_id
        session_id = handler_input.request_envelope.session.session_id
        
        workflow_instruction = request.intent.slots.get("instruction", {}).value
        
        if not workflow_instruction:
            return ResponseFormatter() \
                .set_speech("What custom workflow would you like to execute? Please describe it.") \
                .set_reprompt("Describe the workflow") \
                .build()
        
        # Use Claude to interpret and execute custom workflow
        prompt = f"""Create and execute a custom business workflow based on this request: {workflow_instruction}
                   
                   Steps to follow:
                   1. Understand the intent
                   2. Create appropriate steps
                   3. Provide clear execution instructions
                   4. Ask for confirmation on each critical step
                   
                   Respond with the workflow plan and next action."""
        
        response_text = claude_brain.ask(
            user_message=prompt,
            session_id=f"{user_id}:{session_id}",
            domain="business_workflow"
        )
        
        session_memory.add_turn(user_id, session_id, f"Custom workflow: {workflow_instruction}", response_text, "CustomWorkflowIntent")
        
        return ResponseFormatter() \
            .set_speech(response_text) \
            .set_card("Custom Workflow", workflow_instruction) \
            .build()
