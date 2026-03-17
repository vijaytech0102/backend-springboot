"""
Admin configuration for dashboard.
"""
from django.contrib import admin
from .models import ConversationLog, TaskLog, SmartHomeDevice


@admin.register(ConversationLog)
class ConversationLogAdmin(admin.ModelAdmin):
    list_display = ('user_message', 'intent', 'domain', 'timestamp')
    list_filter = ('domain', 'intent', 'timestamp')
    search_fields = ('user_message', 'alexa_response')
    readonly_fields = ('timestamp',)


@admin.register(TaskLog)
class TaskLogAdmin(admin.ModelAdmin):
    list_display = ('title', 'priority', 'completed', 'created_at')
    list_filter = ('priority', 'completed', 'created_at')
    search_fields = ('title', 'description')
    readonly_fields = ('created_at', 'completed_at')


@admin.register(SmartHomeDevice)
class SmartHomeDeviceAdmin(admin.ModelAdmin):
    list_display = ('name', 'device_type', 'location', 'state', 'last_updated')
    list_filter = ('device_type', 'location')
    search_fields = ('name', 'location')
    readonly_fields = ('last_updated',)
