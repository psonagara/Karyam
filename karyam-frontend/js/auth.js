// Authentication Utilities for KARYAM Construction ERP

/**
 * Login user
 * @param {string} email 
 * @param {string} password 
 * @param {string} role 
 */
async function login(email, password, role) {
  try {
    const response = await apiPost('/auth/login', {
      email,
      password,
      role
    });

    // Store token and user data
    localStorage.setItem('token', response.content.token);
    localStorage.setItem('user', JSON.stringify({
      id: response.content.userId,
      name: response.content.name,
      email: response.content.email,
      role: response.content.role
    }));

    return response;
  } catch (error) {
    throw error;
  }
}

/**
 * Register new user
 * @param {string} name 
 * @param {string} email 
 * @param {string} password 
 * @param {string} role 
 */
async function register(name, email, password, role) {
  try {
    const response = await apiPost('/auth/register', {
      name,
      email,
      password,
      role
    });

    return response;
  } catch (error) {
    throw error;
  }
}

// Handle logout
function handleLogout() {
  if (confirm('Are you sure you want to logout?')) {
    logout();
  }
}

/**
 * Logout user
 */
function logout() {
  localStorage.removeItem('token');
  localStorage.removeItem('user');
  window.location.href = 'login.html';
}

/**
 * Check if user is authenticated
 */
function isAuthenticated() {
  const token = localStorage.getItem('token');
  const user = localStorage.getItem('user');
  return !!(token && user);
}

/**
 * Get current user role
 */
function getCurrentRole() {
  const user = getCurrentUser();
  return user ? user.role : null;
}

/**
 * Protect page - redirect to login if not authenticated
 */
function protectPage() {
  if (!isAuthenticated()) {
    window.location.href = 'login.html';
    return false;
  }
  return true;
}

/**
 * Redirect to dashboard based on role
 */
function redirectToDashboard() {
  window.location.href = 'dashboard.html';
}

/**
 * Get user initials for avatar
 */
function getUserInitials() {
  const user = getCurrentUser();
  if (!user || !user.name) return 'U';

  const nameParts = user.name.split(' ');
  if (nameParts.length >= 2) {
    return nameParts[0][0] + nameParts[1][0];
  }
  return nameParts[0][0];
}

/**
 * Get user display name
 */
function getUserDisplayName() {
  const user = getCurrentUser();
  return user ? user.name : 'User';
}

/**
 * Auto-redirect to dashboard if already logged in (for login page)
 */
function autoRedirectIfLoggedIn() {
  if (isAuthenticated()) {
    redirectToDashboard();
  }
}
