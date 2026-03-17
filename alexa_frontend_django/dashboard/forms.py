"""
Forms for dashboard app.
"""
from django import forms


class ConversationForm(forms.Form):
    """Form for user to send message to Alexa."""
    message = forms.CharField(
        max_length=500,
        widget=forms.TextInput(attrs={
            'class': 'form-control',
            'placeholder': 'What do you want to ask?',
            'autofocus': True
        })
    )


class TaskForm(forms.Form):
    """Form to create a new task."""
    title = forms.CharField(
        max_length=200,
        widget=forms.TextInput(attrs={
            'class': 'form-control',
            'placeholder': 'Task title'
        })
    )
    description = forms.CharField(
        required=False,
        widget=forms.Textarea(attrs={
            'class': 'form-control',
            'placeholder': 'Task description',
            'rows': 3
        })
    )
    priority = forms.ChoiceField(
        choices=[('low', 'Low'), ('normal', 'Normal'), ('high', 'High')],
        widget=forms.Select(attrs={'class': 'form-select'})
    )


class ReminderForm(forms.Form):
    """Form to create a reminder."""
    reminder_text = forms.CharField(
        max_length=300,
        widget=forms.TextInput(attrs={
            'class': 'form-control',
            'placeholder': 'What should I remind you about?'
        })
    )
    time = forms.CharField(
        required=False,
        widget=forms.TextInput(attrs={
            'class': 'form-control',
            'placeholder': 'When? (e.g., "in 1 hour", "tomorrow")'
        })
    )


class SmartHomeCommandForm(forms.Form):
    """Form to control smart home devices."""
    COMMANDS = [
        ('', '--- Select Command ---'),
        ('turn_on', 'Turn On'),
        ('turn_off', 'Turn Off'),
        ('brightness', 'Set Brightness'),
        ('temperature', 'Set Temperature'),
        ('lock', 'Lock'),
        ('unlock', 'Unlock'),
        ('record', 'Start Recording'),
        ('stop', 'Stop Recording'),
    ]
    
    device = forms.CharField(
        max_length=100,
        widget=forms.TextInput(attrs={
            'class': 'form-control',
            'placeholder': 'Device name (e.g., living room lights)'
        })
    )
    command = forms.ChoiceField(
        choices=COMMANDS,
        widget=forms.Select(attrs={'class': 'form-select'})
    )
    parameter = forms.CharField(
        required=False,
        widget=forms.TextInput(attrs={
            'class': 'form-control',
            'placeholder': 'Parameter (e.g., brightness: 75%)'
        })
    )


class WorkflowForm(forms.Form):
    """Form to initiate a workflow."""
    WORKFLOWS = [
        ('', '--- Select Workflow ---'),
        ('sales_pipeline', 'Sales Pipeline Update'),
        ('customer_inquiry', 'Customer Inquiry Handler'),
        ('expense_approval', 'Expense Approval'),
        ('meeting_scheduler', 'Meeting Scheduler'),
        ('project_status', 'Project Status Report'),
    ]
    
    workflow = forms.ChoiceField(
        choices=WORKFLOWS,
        widget=forms.Select(attrs={'class': 'form-select'})
    )
    workflow_data = forms.CharField(
        required=False,
        widget=forms.Textarea(attrs={
            'class': 'form-control',
            'placeholder': 'Additional workflow data',
            'rows': 3
        })
    )
