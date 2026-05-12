// Role-Based Permission System for KARYAM Construction ERP

// Define permissions for each role
const PERMISSIONS = {
  ADMIN: {
    // Admin has full access
    dashboard: { view: true },
    projects: { view: true, create: true, edit: true, delete: true },
    labor: { view: true, create: true, edit: true, delete: true },
    attendance: { view: true, mark: true, export: true},
    vendors: { view: true, create: true, edit: true, delete: true },
    expenses: { view: true, create: true, edit: true, delete: true },
    approvals: { view: true, approve: true, reject: true },
    payroll: { view: true, generate: true, export: true, approve: true},
    reports: { view: true, export: true },
    auditLogs: { view: true }
  },
  
  SITE_MANAGER: {
    dashboard: { view: true },
    projects: { view: true, create: false, edit: false, delete: false },
    labor: { view: true, create: false, edit: false, delete: false },
    attendance: { view: true, mark: false },
    vendors: { view: false, create: false, edit: false, delete: false },
    expenses: { view: true, create: false, edit: false, delete: false },
    approvals: { view: true, approve: true, reject: true },
    payroll: { view: true, generate: false, export: false },
    reports: { view: true, export: true },
    auditLogs: { view: false }
  },
  
  HR: {
    dashboard: { view: true },
    projects: { view: false, create: false, edit: false, delete: false },
    labor: { view: true, create: true, edit: true, delete: true },
    attendance: { view: true, mark: true },
    vendors: { view: false, create: false, edit: false, delete: false },
    expenses: { view: false, create: false, edit: false, delete: false },
    approvals: { view: false, approve: false, reject: false },
    payroll: { view: true, generate: true, export: true },
    reports: { view: true, export: true },
    auditLogs: { view: false }
  },
  
  ACCOUNTANT: {
    dashboard: { view: true },
    projects: { view: true, create: false, edit: false, delete: false },
    labor: { view: true, create: false, edit: false, delete: false },
    attendance: { view: false, mark: false },
    vendors: { view: true, create: true, edit: true, delete: true },
    expenses: { view: true, create: true, edit: true, delete: true },
    approvals: { view: false, approve: false, reject: false },
    payroll: { view: true, generate: false, export: true },
    reports: { view: true, export: true },
    auditLogs: { view: false }
  }
};

/**
 * Check if user has permission for a module action
 * @param {string} module - Module name (e.g., 'projects')
 * @param {string} action - Action name (e.g., 'create')
 * @returns {boolean}
 */
function hasPermission(module, action) {
  const role = getCurrentRole();
  if (!role || !PERMISSIONS[role]) return false;
  
  const modulePermissions = PERMISSIONS[role][module];
  if (!modulePermissions) return false;
  
  return modulePermissions[action] === true;
}

/**
 * Check if user can view a module
 * @param {string} module 
 * @returns {boolean}
 */
function canView(module) {
  return hasPermission(module, 'view');
}

/**
 * Check if user can create in a module
 * @param {string} module 
 * @returns {boolean}
 */
function canCreate(module) {
  return hasPermission(module, 'create');
}

/**
 * Check if user can edit in a module
 * @param {string} module 
 * @returns {boolean}
 */
function canEdit(module) {
  return hasPermission(module, 'edit');
}

/**
 * Check if user can delete in a module
 * @param {string} module 
 * @returns {boolean}
 */
function canDelete(module) {
  return hasPermission(module, 'delete');
}

/**
 * Protect a page based on module permission
 * @param {string} module 
 */
function protectPageByRole(module) {
  if (!protectPage()) return false;
  
  if (!canView(module)) {
    window.location.href = 'access-denied.html';
    return false;
  }
  
  return true;
}

/**
 * Show/hide element based on permission
 * @param {string} selector - CSS selector
 * @param {string} module 
 * @param {string} action 
 */
function toggleElementByPermission(selector, module, action) {
  const element = document.querySelector(selector);
  if (!element) return;
  
  if (hasPermission(module, action)) {
    element.style.display = '';
  } else {
    element.style.display = 'none';
  }
}

/**
 * Get navigation items based on role
 * @returns {Array} Array of allowed navigation items
 */
function getAllowedNavigationItems() {
  const role = getCurrentRole();
  if (!role) return [];
  
  const navItems = [
    { id: 'dashboard', label: 'Dashboard', icon: '📊', path: 'dashboard.html', module: 'dashboard' },
    { id: 'projects', label: 'Projects', icon: '🏗️', path: 'projects.html', module: 'projects' },
    { id: 'labor', label: 'Labor', icon: '👷', path: 'labor-list.html', module: 'labor' },
    { id: 'attendance', label: 'Attendance', icon: '✓', path: 'attendance.html', module: 'attendance' },
    { id: 'vendors', label: 'Vendors', icon: '🏪', path: 'vendors.html', module: 'vendors' },
    { id: 'expenses', label: 'Expenses', icon: '💰', path: 'expenses.html', module: 'expenses' },
    { id: 'approvals', label: 'Approvals', icon: '✅', path: 'approvals.html', module: 'approvals' },
    { id: 'payroll', label: 'Payroll', icon: '💵', path: 'payroll.html', module: 'payroll' },
    { id: 'reports', label: 'Reports', icon: '📈', path: 'reports.html', module: 'reports' },
    { id: 'audit', label: 'Audit Logs', icon: '📋', path: 'audit-logs.html', module: 'auditLogs' }
  ];
  
  return navItems.filter(item => canView(item.module));
}

/**
 * Get role badge color
 * @param {string} role 
 * @returns {string} CSS class
 */
function getRoleBadgeClass(role) {
  const classes = {
    'ADMIN': 'badge-danger',
    'SITE_MANAGER': 'badge-info',
    'HR': 'badge-success',
    'ACCOUNTANT': 'badge-warning'
  };
  return classes[role] || 'badge-gray';
}

/**
 * Get role display name
 * @param {string} role 
 * @returns {string}
 */
function getRoleDisplayName(role) {
  const names = {
    'ADMIN': 'Administrator',
    'SITE_MANAGER': 'Site Manager',
    'HR': 'HR Manager',
    'ACCOUNTANT': 'Accountant'
  };
  return names[role] || role;
}
