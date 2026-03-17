// Alexa Frontend Django - Main JavaScript

document.addEventListener('DOMContentLoaded', function() {
    // Check backend health status
    checkBackendHealth();
    
    // Auto-scroll to latest message in chat
    autoScrollChat();
    
    // Set up real-time updates
    setupRealtimeUpdates();
});

// Check backend API health
function checkBackendHealth() {
    fetch('/api/health/')
        .then(response => response.json())
        .then(data => {
            const statusElement = document.getElementById('backend-status');
            if (statusElement) {
                if (data.status === 'healthy') {
                    statusElement.innerHTML = '<span class="badge bg-success">Online</span>';
                } else {
                    statusElement.innerHTML = '<span class="badge bg-warning">Checking...</span>';
                }
            }
        })
        .catch(error => {
            const statusElement = document.getElementById('backend-status');
            if (statusElement) {
                statusElement.innerHTML = '<span class="badge bg-danger">Offline</span>';
            }
            console.error('Health check failed:', error);
        });
}

// Auto-scroll chat to latest message
function autoScrollChat() {
    const conversationArea = document.querySelector('.conversation-history');
    if (conversationArea) {
        conversationArea.scrollTop = conversationArea.scrollHeight;
    }
}

// Setup real-time updates (polling)
function setupRealtimeUpdates() {
    // Check for new conversations every 30 seconds
    setInterval(function() {
        if (window.location.pathname.includes('chat') || 
            window.location.pathname.includes('agent')) {
            location.reload();
        }
    }, 30000);
}

// Form validation helper
function validateForm(formId) {
    const form = document.getElementById(formId);
    if (!form) return true;
    
    const inputs = form.querySelectorAll('input[required], textarea[required], select[required]');
    let isValid = true;
    
    inputs.forEach(input => {
        if (!input.value.trim()) {
            input.classList.add('is-invalid');
            isValid = false;
        } else {
            input.classList.remove('is-invalid');
        }
    });
    
    return isValid;
}

// Show notification
function showNotification(message, type = 'info') {
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type} alert-dismissible fade show`;
    alertDiv.role = 'alert';
    alertDiv.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    
    const container = document.querySelector('.container-fluid');
    if (container) {
        container.insertBefore(alertDiv, container.firstChild);
        
        // Auto-dismiss after 5 seconds
        setTimeout(() => {
            alertDiv.remove();
        }, 5000);
    }
}

// Format timestamp
function formatTime(timestamp) {
    const date = new Date(timestamp);
    return date.toLocaleTimeString('en-US', {
        hour: '2-digit',
        minute: '2-digit'
    });
}

// Debounce function for search/filter
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

// Copy to clipboard
function copyToClipboard(text) {
    navigator.clipboard.writeText(text).then(() => {
        showNotification('Copied to clipboard!', 'success');
    }).catch(() => {
        showNotification('Failed to copy', 'danger');
    });
}

// Loading spinner
function showLoadingSpinner(elementId) {
    const element = document.getElementById(elementId);
    if (element) {
        element.innerHTML = '<div class="loading"></div><div class="loading"></div><div class="loading"></div>';
    }
}

// Stop loading spinner
function hideLoadingSpinner(elementId, content) {
    const element = document.getElementById(elementId);
    if (element) {
        element.innerHTML = content;
    }
}

// Export data helper
function exportAsJSON(data, filename) {
    const json = JSON.stringify(data, null, 2);
    const blob = new Blob([json], { type: 'application/json' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = filename || 'export.json';
    document.body.appendChild(a);
    a.click();
    window.URL.revokeObjectURL(url);
    document.body.removeChild(a);
}

// Initialize tooltips if using Bootstrap tooltips
function initializeTooltips() {
    const tooltipTriggerList = [].slice.call(
        document.querySelectorAll('[data-bs-toggle="tooltip"]')
    );
    tooltipTriggerList.map(tooltipTriggerEl => {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
}

// Initialize popovers if using Bootstrap popovers
function initializePopovers() {
    const popoverTriggerList = [].slice.call(
        document.querySelectorAll('[data-bs-toggle="popover"]')
    );
    popoverTriggerList.map(popoverTriggerEl => {
        return new bootstrap.Popover(popoverTriggerEl);
    });
}

// Call initializers
document.addEventListener('DOMContentLoaded', function() {
    initializeTooltips();
    initializePopovers();
});

// Keyboard shortcuts
document.addEventListener('keydown', function(e) {
    // Ctrl/Cmd + Enter sends message in chat
    const textarea = document.querySelector('textarea[data-chat]');
    if (textarea && (e.ctrlKey || e.metaKey) && e.key === 'Enter') {
        textarea.closest('form').submit();
    }
});
