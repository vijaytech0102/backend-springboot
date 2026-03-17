"""
URL routing for dashboard app.
"""
from django.urls import path
from . import views

urlpatterns = [
    path('', views.index, name='index'),
    path('chat/', views.chat_interface, name='chat_interface'),
    path('information/', views.information_qa, name='information_qa'),
    path('smart-home/', views.smart_home_control, name='smart_home_control'),
    path('tasks/', views.task_management, name='task_management'),
    path('workflows/', views.business_workflows, name='business_workflows'),
    path('agent/', views.agent_communication, name='agent_communication'),
    path('api/health/', views.dashboard_health, name='api_health'),
    path('api/debug/', views.api_debug_info, name='api_debug'),
]
