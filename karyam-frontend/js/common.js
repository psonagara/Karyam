// Common Utilities for KARYAM Construction ERP
// Toast notifications, modals, loaders, and other UI helpers

/**
 * Show toast notification
 * @param {string} message 
 * @param {string} type - success, error, warning, info
 * @param {number} duration - milliseconds
 */
function showToast(message, type = 'info', duration = 3000) {
  // Remove existing toasts
  const existing = document.querySelectorAll('.toast');
  existing.forEach(toast => toast.remove());

  const toast = document.createElement('div');
  toast.className = `toast toast-${type}`;

  const icons = {
    success: '✓',
    error: '✕',
    warning: '⚠',
    info: 'ℹ'
  };

  toast.innerHTML = `
    <span style="font-size: 20px;">${icons[type] || icons.info}</span>
    <span style="flex: 1;">${message}</span>
  `;

  document.body.appendChild(toast);

  // Auto remove after duration
  setTimeout(() => {
    toast.classList.add('hiding');
    setTimeout(() => {
      toast.remove();
    }, 300);
  }, duration);
}

/**
 * Show loading overlay
 */
function showLoader() {
  const existing = document.querySelector('.loading-overlay');
  if (existing) return;

  const overlay = document.createElement('div');
  overlay.className = 'loading-overlay';
  overlay.innerHTML = '<div class="loader loader-lg"></div>';
  document.body.appendChild(overlay);
}

/**
 * Hide loading overlay
 */
function hideLoader() {
  const overlay = document.querySelector('.loading-overlay');
  if (overlay) {
    overlay.remove();
  }
}

/**
 * Confirm dialog
 * @param {string} message 
 * @returns {Promise<boolean>}
 */
function confirmDialog(message) {
  return new Promise((resolve) => {
    const backdrop = document.createElement('div');
    backdrop.className = 'modal-backdrop';

    backdrop.innerHTML = `
      <div class="card" style="max-width: 500px; padding: 32px;">
        <h3 style="margin-bottom: 16px; font-size: 1.25rem;">Confirm Action</h3>
        <p style="color: var(--gray-600); margin-bottom: 24px;">${message}</p>
        <div style="display: flex; gap: 12px; justify-content: flex-end;">
          <button class="btn btn-outline" onclick="this.closest('.modal-backdrop').remove()">Cancel</button>
          <button class="btn btn-danger" id="confirmBtn">Confirm</button>
        </div>
      </div>
    `;

    document.body.appendChild(backdrop);

    backdrop.querySelector('#confirmBtn').addEventListener('click', () => {
      backdrop.remove();
      resolve(true);
    });

    backdrop.addEventListener('click', (e) => {
      if (e.target === backdrop) {
        backdrop.remove();
        resolve(false);
      }
    });
  });
}

/**
 * Prompt dialog for input
 * @param {string} message 
 * @param {string} placeholder 
 * @returns {Promise<string|null>}
 */
function promptDialog(message, placeholder = '') {
  return new Promise((resolve) => {
    const backdrop = document.createElement('div');
    backdrop.className = 'modal-backdrop';

    backdrop.innerHTML = `
      <div class="card" style="max-width: 500px; padding: 32px;">
        <h3 style="margin-bottom: 16px; font-size: 1.25rem;">${message}</h3>
        <input type="text" class="form-input" id="promptInput" placeholder="${placeholder}" style="margin-bottom: 24px;">
        <div style="display: flex; gap: 12px; justify-content: flex-end;">
          <button class="btn btn-outline" onclick="this.closest('.modal-backdrop').remove()">Cancel</button>
          <button class="btn btn-primary" id="submitBtn">Submit</button>
        </div>
      </div>
    `;

    document.body.appendChild(backdrop);

    const input = backdrop.querySelector('#promptInput');
    input.focus();

    backdrop.querySelector('#submitBtn').addEventListener('click', () => {
      const value = input.value.trim();
      backdrop.remove();
      resolve(value || null);
    });

    input.addEventListener('keypress', (e) => {
      if (e.key === 'Enter') {
        const value = input.value.trim();
        backdrop.remove();
        resolve(value || null);
      }
    });

    backdrop.addEventListener('click', (e) => {
      if (e.target === backdrop) {
        backdrop.remove();
        resolve(null);
      }
    });
  });
}

/**
 * Format currency
 * @param {number} amount 
 * @returns {string}
 */
function formatCurrency(amount) {
  return new Intl.NumberFormat('en-IN', {
    style: 'currency',
    currency: 'INR',
    minimumFractionDigits: 0
  }).format(amount);
}

/**
 * Format date
 * @param {string|Date} date 
 * @returns {string}
 */
function formatDate(date) {
  const d = new Date(date);
  return d.toLocaleDateString('en-IN', {
    day: '2-digit',
    month: 'short',
    year: 'numeric'
  });
}

/**
 * Format datetime
 * @param {string|Date} datetime 
 * @returns {string}
 */
function formatDateTime(datetime) {
  const d = new Date(datetime);
  return d.toLocaleString('en-IN', {
    day: '2-digit',
    month: 'short',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  });
}

/**
 * Validate email
 * @param {string} email 
 * @returns {boolean}
 */
function isValidEmail(email) {
  const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return re.test(email);
}

/**
 * Validate phone
 * @param {string} phone 
 * @returns {boolean}
 */
function isValidPhone(phone) {
  const re = /^[6-9]\d{9}$/;
  return re.test(phone);
}

/**
 * Debounce function
 * @param {Function} func 
 * @param {number} wait 
 * @returns {Function}
 */
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

/**
 * Handle API errors and show appropriate message
 * @param {Error} error 
 */
function handleApiError(error) {
  console.error('API Error:', error);

  if (error.message.includes('Unauthorized')) {
    showToast('Session expired. Please login again.', 'error');
    setTimeout(() => {
      logout();
    }, 2000);
  } else if (error.message.includes('Access denied')) {
    showToast('You do not have permission for this action.', 'error');
  } else if (error.message.includes('Network')) {
    showToast('Network error. Please check your connection.', 'error');
  } else {
    showToast(error.message || 'An error occurred', 'error');
  }
}

/**
 * Download file from blob
 * @param {Blob} blob 
 * @param {string} filename 
 */
function downloadFile(blob, filename) {
  const url = window.URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = filename;
  document.body.appendChild(a);
  a.click();
  document.body.removeChild(a);
  window.URL.revokeObjectURL(url);
}

/**
 * Get status badge HTML
 * @param {string} status 
 * @returns {string}
 */
function getStatusBadge(status) {
  const badges = {
    'ACTIVE': '<span class="badge badge-success">Active</span>',
    'INACTIVE': '<span class="badge badge-gray">Inactive</span>',
    'PENDING': '<span class="badge badge-warning">Pending</span>',
    'APPROVED': '<span class="badge badge-success">Approved</span>',
    'REJECTED': '<span class="badge badge-danger">Rejected</span>',
    'COMPLETED': '<span class="badge badge-success">Completed</span>',
    'IN_PROGRESS': '<span class="badge badge-info">In Progress</span>',
    'PAID': '<span class="badge badge-success">Paid</span>',
    'UNPAID': '<span class="badge badge-danger">Unpaid</span>',
    'PRESENT': '<span class="badge badge-success">Present</span>',
    'ABSENT': '<span class="badge badge-danger">Absent</span>',
    'HALF_DAY': '<span class="badge badge-warning">Half Day</span>',
    'OVERTIME': '<span class="badge badge-info">Overtime</span>'
  };
  return badges[status] || `<span class="badge badge-gray">${status}</span>`;
}

/**
 * Initialize page - protect and render role-based UI
 * @param {string} module - Module name for permission check
 */
function initializePage(module) {
  // Protect page
  if (!protectPageByRole(module)) {
    return false;
  }

  // Update user info in header
  updateUserInfo();

  return true;
}

/**
 * Update user info in header
 */
function updateUserInfo() {
  const userNameEl = document.getElementById('userName');
  const userRoleEl = document.getElementById('userRole');
  const userInitialsEl = document.getElementById('userInitials');

  if (userNameEl) userNameEl.textContent = getUserDisplayName();
  if (userRoleEl) {
    const role = getCurrentRole();
    userRoleEl.textContent = getRoleDisplayName(role);
    userRoleEl.className = `badge ${getRoleBadgeClass(role)}`;
  }
  if (userInitialsEl) userInitialsEl.textContent = getUserInitials();
}

/**
 * Truncate text
 * @param {string} text 
 * @param {number} maxLength 
 * @returns {string}
 */
function truncate(text, maxLength = 50) {
  if (!text) return '';
  if (text.length <= maxLength) return text;
  return text.substring(0, maxLength) + '...';
}

/**
 * Escape HTML
 * @param {string} text 
 * @returns {string}
 */
function escapeHtml(text) {
  const div = document.createElement('div');
  div.textContent = text;
  return div.innerHTML;
}

function setPageTitle(title, subtitle) {
  const titleElement = document.getElementById("pageTitle");
  const subtitleElement = document.getElementById("pageSubtitle");

  if (titleElement) {
    titleElement.textContent = title;
  }

  if (subtitleElement) {
    subtitleElement.textContent = subtitle;
  }

  document.title = title + " | Karyam";
}


function populateSidebar() {
  const navItems = getAllowedNavigationItems();
  const sidebarNav = document.getElementById('sidebarNav');
  const currentPath = window.location.pathname;

  if (!sidebarNav) return;

  sidebarNav.innerHTML = navItems.map(item => {
    const isActive = currentPath.includes(item.path);
    return `
      <a href="${item.path}" class="nav-item ${isActive ? 'active' : ''}">
        <span class="nav-icon">${item.icon}</span>
        <span>${item.label}</span>
      </a>
    `;
  }).join('');
}

function initSidebar() {
  const sidebar = document.getElementById('sidebar');
  const menuToggle = document.getElementById('mobileMenuToggle');

  if (menuToggle && sidebar) {
    menuToggle.addEventListener('click', () => {
      sidebar.classList.toggle('mobile-open');
    });

    document.addEventListener('click', (e) => {
      if (!sidebar.contains(e.target) && !menuToggle.contains(e.target)) {
        sidebar.classList.remove('mobile-open');
      }
    });
  }

  populateSidebar();
}

function initHeader() {
  const notificationBtn = document.getElementById('notificationBtn');
  const notificationDropdown = document.getElementById('notificationDropdown');
  const userMenuBtn = document.getElementById('userMenuBtn');
  const userDropdown = document.getElementById('userDropdown');

  if (notificationBtn && notificationDropdown) {
    notificationBtn.addEventListener('click', (e) => {
      e.stopPropagation();
      notificationDropdown.style.display =
        notificationDropdown.style.display === 'none' ? 'block' : 'none';

      if (userDropdown) userDropdown.style.display = 'none';
    });
  }

  if (userMenuBtn && userDropdown) {
    userMenuBtn.addEventListener('click', (e) => {
      e.stopPropagation();
      userDropdown.style.display =
        userDropdown.style.display === 'none' ? 'block' : 'none';

      if (notificationDropdown) notificationDropdown.style.display = 'none';
    });
  }

  // Close on outside click
  document.addEventListener('click', () => {
    if (notificationDropdown) notificationDropdown.style.display = 'none';
    if (userDropdown) userDropdown.style.display = 'none';
  });

  connect();
  loadNotifications();
}

// Notifications API
async function loadNotifications() {
  try {
    const data = await apiGet('/notifications', 'notification');

    const notificationList = document.getElementById('notificationList');
    const notificationBadge = document.getElementById('notificationBadge');

    if (!notificationList || !notificationBadge) return;

    if (data && data.length > 0) {
      notificationBadge.style.display = 'block';

      notificationList.innerHTML = data.slice(0, 5).map(notif => `
        <div style="padding: 12px; border-bottom: 1px solid var(--gray-100); cursor: pointer;">
          <div style="font-weight: 600; font-size: 13px;">${notif.title}</div>
          <div style="font-size: 12px; color: var(--gray-600);">${notif.message}</div>
          <div style="font-size: 11px; color: var(--gray-500);">${formatDateTime(notif.createdAt)}</div>
        </div>
      `).join('');
    }
  } catch (error) {
    console.error('Failed to load notifications:', error);
  }
}

// Keep utility functions here OR move to common.js
function setPageTitle(title, subtitle = '') {
  const pageTitleEl = document.getElementById('pageTitle');
  const pageSubtitleEl = document.getElementById('pageSubtitle');

  if (pageTitleEl) pageTitleEl.textContent = title;
  if (pageSubtitleEl) pageSubtitleEl.textContent = subtitle;
}

function handleLogout() {
  if (confirm('Are you sure you want to logout?')) {
    logout();
  }
}

let stompClient = null;
let isSocketConnected = false;

function connect() {
  if (isSocketConnected) {
    console.log('WebSocket already connected');
    return;
  }
  // Connect to the GATEWAY port
  const socket = new SockJS('http://localhost:8082/notification/ws');
  stompClient = Stomp.over(socket);

  const headers = {
    'Authorization': 'Bearer ' + localStorage.getItem('token')
  };

  stompClient.connect(headers, function (frame) {
    console.log('Connected to WS');
    isSocketConnected = true;

    // For private messages, Spring expects /user/queue/...
    stompClient.subscribe('/user/queue/notifications', function (payload) {
      const notification = JSON.parse(payload.body);
      showToastNotification(notification);
      showNotificationUI(notification);
    });
  }, function (error) {
    isSocketConnected = false;
    console.error('STOMP error:', error);
  });
}

function showToastNotification(notification) {

  console.log("Inside showToastNotification");

  const container = document.getElementById('notificationToastContainer');
  const sound = document.getElementById('notificationSound');

  console.log("Notification: ", notification);

  // Play sound
  if (sound) {
    sound.currentTime = 0;
    sound.play().catch(() => { }); // ignore autoplay restrictions
  }

  const toast = document.createElement('div');

  toast.style = `
    min-width: 280px;
    max-width: 320px;
    background: white;
    border-left: 5px solid var(--primary);
    border-radius: 10px;
    box-shadow: var(--shadow-lg);
    padding: 14px 16px;
    animation: slideIn 0.3s ease;
    font-family: var(--font-display);
  `;

  toast.innerHTML = `
    <div style="font-weight: 700; margin-bottom: 4px;">
      ${notification.title}
    </div>
    <div style="font-size: 13px; color: var(--gray-600);">
      ${notification.message}
    </div>
  `;

  container.appendChild(toast);

  // Auto remove after 4 sec
  setTimeout(() => {
    toast.style.opacity = '0';
    toast.style.transform = 'translateX(100%)';
    setTimeout(() => toast.remove(), 800);
  }, 7000);
}

function showNotificationUI(notif) {
  const list = document.getElementById('notificationList');

  const html = `
        <div style="padding: 12px; border-bottom: 1px solid var(--gray-100); cursor: pointer;">
          <div style="font-weight: 600; font-size: 13px;">${notif.title}</div>
          <div style="font-size: 12px; color: var(--gray-600);">${notif.message}</div>
          <div style="font-size: 11px; color: var(--gray-500);">${formatDateTime(notif.createdAt)}</div>
        </div>
  `;

  list.innerHTML = html + list.innerHTML;

  document.getElementById('notificationBadge').style.display = 'block';
}