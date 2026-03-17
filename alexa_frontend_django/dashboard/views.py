"""
Views for dashboard app - interfaces with Flask backend.
"""
import json
import requests
import logging
from django.shortcuts import render, redirect
from django.contrib import messages
from django.conf import settings
from django.http import JsonResponse
from django.views.decorators.http import require_http_methods
from django.views.decorators.csrf import csrf_exempt

from .forms import (
    ConversationForm, TaskForm, ReminderForm, 
    SmartHomeCommandForm, WorkflowForm
)
from .models import ConversationLog, TaskLog, SmartHomeDevice

logger = logging.getLogger(__name__)
FLASK_API_URL = settings.FLASK_API_URL


def index(request):
    """Dashboard home page."""
    return render(request, 'dashboard/index.html')


def chat_interface(request):
    """Main chat/conversation interface."""
    form = ConversationForm()
    recent_conversations = ConversationLog.objects.all()[:10]
    
    if request.method == 'POST':
        form = ConversationForm(request.POST)
        if form.is_valid():
            user_message = form.cleaned_data['message']
            
            # Send to Flask backend
            try:
                response = requests.post(
                    f'{FLASK_API_URL}/query',
                    json={'message': user_message},
                    timeout=10
                )
                
                if response.status_code == 200:
                    data = response.json()
                    alexa_response = data.get('response', 'No response')
                    intent = data.get('intent', 'unknown')
                    
                    # Save to database
                    ConversationLog.objects.create(
                        user_message=user_message,
                        alexa_response=alexa_response,
                        intent=intent,
                        domain='general'
                    )
                    
                    messages.success(request, f'Alexa: {alexa_response}')
                else:
                    messages.error(request, 'Error communicating with Alexa')
            except Exception as e:
                logger.error(f"Chat error: {str(e)}")
                messages.error(request, f'Error: {str(e)}')
            
            return redirect('chat_interface')
    
    context = {
        'form': form,
        'conversations': recent_conversations,
    }
    return render(request, 'dashboard/chat.html', context)


def information_qa(request):
    """Information & Q&A domain interface."""
    form = ConversationForm()
    recent_queries = ConversationLog.objects.filter(domain='general')[:10]
    
    if request.method == 'POST':
        form = ConversationForm(request.POST)
        if form.is_valid():
            query = form.cleaned_data['message']
            
            try:
                response = requests.post(
                    f'{FLASK_API_URL}/query',
                    json={'message': query, 'intent': 'InfoIntent'},
                    timeout=10
                )
                
                if response.status_code == 200:
                    data = response.json()
                    result = data.get('response', 'No answer')
                    
                    ConversationLog.objects.create(
                        user_message=query,
                        alexa_response=result,
                        intent='InfoIntent',
                        domain='information'
                    )
                    
                    messages.success(request, result)
                else:
                    messages.error(request, 'Error getting information')
            except Exception as e:
                logger.error(f"Info query error: {str(e)}")
                messages.error(request, 'Error processing query')
            
            return redirect('information_qa')
    
    context = {
        'form': form,
        'recent_queries': recent_queries,
        'title': 'Information & Q&A'
    }
    return render(request, 'dashboard/information.html', context)


def smart_home_control(request):
    """Smart home control interface."""
    form = SmartHomeCommandForm()
    devices = SmartHomeDevice.objects.all()
    
    if request.method == 'POST':
        form = SmartHomeCommandForm(request.POST)
        if form.is_valid():
            device = form.cleaned_data['device']
            command = form.cleaned_data['command']
            parameter = form.cleaned_data.get('parameter', '')
            
            try:
                response = requests.post(
                    f'{FLASK_API_URL}/smart_home',
                    json={
                        'device': device,
                        'command': command,
                        'parameter': parameter
                    },
                    timeout=10
                )
                
                if response.status_code == 200:
                    data = response.json()
                    result = data.get('result', 'Command executed')
                    
                    # Update device state
                    device_obj, _ = SmartHomeDevice.objects.get_or_create(
                        name=device,
                        defaults={'device_type': 'light', 'location': 'home'}
                    )
                    device_obj.state = command
                    device_obj.save()
                    
                    messages.success(request, result)
                else:
                    messages.error(request, 'Error executing command')
            except Exception as e:
                logger.error(f"Smart home error: {str(e)}")
                messages.error(request, 'Error controlling device')
            
            return redirect('smart_home_control')
    
    context = {
        'form': form,
        'devices': devices,
        'title': 'Smart Home Control'
    }
    return render(request, 'dashboard/smart_home.html', context)


def task_management(request):
    """Task and reminder management interface."""
    task_form = TaskForm()
    reminder_form = ReminderForm()
    tasks = TaskLog.objects.all()
    
    if request.method == 'POST':
        if 'create_task' in request.POST:
            task_form = TaskForm(request.POST)
            if task_form.is_valid():
                data = task_form.cleaned_data
                
                try:
                    response = requests.post(
                        f'{FLASK_API_URL}/task',
                        json={
                            'action': 'create',
                            'title': data['title'],
                            'priority': data['priority']
                        },
                        timeout=10
                    )
                    
                    if response.status_code == 200:
                        TaskLog.objects.create(
                            title=data['title'],
                            description=data.get('description', ''),
                            priority=data['priority']
                        )
                        messages.success(request, 'Task created successfully')
                    else:
                        messages.error(request, 'Error creating task')
                except Exception as e:
                    logger.error(f"Task creation error: {str(e)}")
                    messages.error(request, 'Error creating task')
                
                return redirect('task_management')
        
        elif 'create_reminder' in request.POST:
            reminder_form = ReminderForm(request.POST)
            if reminder_form.is_valid():
                data = reminder_form.cleaned_data
                
                try:
                    response = requests.post(
                        f'{FLASK_API_URL}/reminder',
                        json={
                            'text': data['reminder_text'],
                            'time': data.get('time', 'later')
                        },
                        timeout=10
                    )
                    
                    if response.status_code == 200:
                        messages.success(request, 'Reminder set successfully')
                    else:
                        messages.error(request, 'Error setting reminder')
                except Exception as e:
                    logger.error(f"Reminder error: {str(e)}")
                    messages.error(request, 'Error setting reminder')
                
                return redirect('task_management')
    
    context = {
        'task_form': task_form,
        'reminder_form': reminder_form,
        'tasks': tasks,
        'title': 'Task & Reminder Management'
    }
    return render(request, 'dashboard/tasks.html', context)


def business_workflows(request):
    """Business workflow execution interface."""
    form = WorkflowForm()
    
    if request.method == 'POST':
        form = WorkflowForm(request.POST)
        if form.is_valid():
            workflow = form.cleaned_data['workflow']
            workflow_data = form.cleaned_data.get('workflow_data', '')
            
            try:
                response = requests.post(
                    f'{FLASK_API_URL}/workflow',
                    json={
                        'workflow': workflow,
                        'data': workflow_data
                    },
                    timeout=10
                )
                
                if response.status_code == 200:
                    data = response.json()
                    result = data.get('result', 'Workflow initiated')
                    messages.success(request, result)
                else:
                    messages.error(request, 'Error initiating workflow')
            except Exception as e:
                logger.error(f"Workflow error: {str(e)}")
                messages.error(request, 'Error initiating workflow')
            
            return redirect('business_workflows')
    
    context = {
        'form': form,
        'title': 'Business Workflows'
    }
    return render(request, 'dashboard/workflows.html', context)


def agent_communication(request):
    """Human-to-AI agent communication interface."""
    form = ConversationForm()
    conversations = ConversationLog.objects.filter(domain='agent')[:20]
    
    if request.method == 'POST':
        form = ConversationForm(request.POST)
        if form.is_valid():
            message = form.cleaned_data['message']
            
            try:
                response = requests.post(
                    f'{FLASK_API_URL}/agent',
                    json={'message': message},
                    timeout=10
                )
                
                if response.status_code == 200:
                    data = response.json()
                    response_text = data.get('response', 'No response')
                    sentiment = data.get('sentiment', 'neutral')
                    
                    ConversationLog.objects.create(
                        user_message=message,
                        alexa_response=response_text,
                        intent='agent_communication',
                        domain='agent'
                    )
                    
                    messages.success(request, response_text)
                else:
                    messages.error(request, 'Error communicating with agent')
            except Exception as e:
                logger.error(f"Agent communication error: {str(e)}")
                messages.error(request, 'Error with agent')
            
            return redirect('agent_communication')
    
    context = {
        'form': form,
        'conversations': conversations,
        'title': 'Human-to-Agent Communication'
    }
    return render(request, 'dashboard/agent.html', context)


def dashboard_health(request):
    """Check backend health status."""
    try:
        response = requests.get(f'{FLASK_API_URL}/health', timeout=5)
        if response.status_code == 200:
            data = response.json()
            return JsonResponse({
                'status': 'healthy',
                'backend': data
            })
        else:
            return JsonResponse({'status': 'unhealthy'}, status=500)
    except Exception as e:
        logger.error(f"Health check error: {str(e)}")
        return JsonResponse({'status': 'offline'}, status=500)


def api_debug_info(request):
    """Get debug information about backend."""
    try:
        response = requests.get(f'{FLASK_API_URL}/debug', timeout=5)
        if response.status_code == 200:
            return JsonResponse(response.json())
        else:
            return JsonResponse({'error': 'Failed to get debug info'}, status=500)
    except Exception as e:
        return JsonResponse({'error': str(e)}, status=500)
