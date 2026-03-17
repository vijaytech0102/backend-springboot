"""
Models for dashboard app.
"""
from django.db import models


class ConversationLog(models.Model):
    """Store conversation history between user and Alexa."""
    user_message = models.TextField()
    alexa_response = models.TextField()
    intent = models.CharField(max_length=100, null=True, blank=True)
    domain = models.CharField(max_length=50, default='general')
    timestamp = models.DateTimeField(auto_now_add=True)
    
    class Meta:
        ordering = ['-timestamp']
    
    def __str__(self):
        return f"{self.user_message[:50]}..."


class TaskLog(models.Model):
    """Track tasks created/completed through the UI."""
    title = models.CharField(max_length=200)
    description = models.TextField(blank=True)
    priority = models.CharField(max_length=20, default='normal', 
                               choices=[('low', 'Low'), ('normal', 'Normal'), ('high', 'High')])
    completed = models.BooleanField(default=False)
    created_at = models.DateTimeField(auto_now_add=True)
    completed_at = models.DateTimeField(null=True, blank=True)
    
    class Meta:
        ordering = ['-created_at']
    
    def __str__(self):
        return self.title


class SmartHomeDevice(models.Model):
    """Track smart home device state."""
    DEVICE_TYPES = [
        ('light', 'Light'),
        ('thermostat', 'Thermostat'),
        ('lock', 'Lock'),
        ('camera', 'Camera'),
    ]
    
    name = models.CharField(max_length=100)
    device_type = models.CharField(max_length=20, choices=DEVICE_TYPES)
    location = models.CharField(max_length=100)
    state = models.CharField(max_length=50, default='unknown')
    last_updated = models.DateTimeField(auto_now=True)
    
    class Meta:
        unique_together = ('name', 'location')
    
    def __str__(self):
        return f"{self.name} ({self.location})"
