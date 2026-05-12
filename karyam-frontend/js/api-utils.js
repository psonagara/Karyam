// API Utilities for KARYAM Construction ERP
// Handles all HTTP requests with automatic token injection

const API_BASE_URL = 'http://localhost:8080/operations/api';
const NOTIFICATION_URL = "http://localhost:8080/notification/api";
const AUDIT_URL = "http://localhost:8080/audit/api";


/**
 * Get auth token from localStorage
 */
function getAuthToken() {
  return localStorage.getItem('token');
}

/**
 * Get current user info from localStorage
 */
function getCurrentUser() {
  const userStr = localStorage.getItem('user');
  return userStr ? JSON.parse(userStr) : null;
}

/**
 * Main API request function
 * @param {string} endpoint - API endpoint (e.g., '/projects')
 * @param {object} options - Fetch options
 * @returns {Promise<object>} - Response data
 */
async function apiRequest(endpoint, options = {}, service = 'operations') {
  const token = getAuthToken();
  
  const config = {
    ...options,
    headers: {
      'Content-Type': 'application/json',
      ...(token && { 'Authorization': `Bearer ${token}` }),
      ...options.headers
    }
  };

  try {
    let API_URL = API_BASE_URL;
    if (service === 'notification') {
      API_URL = NOTIFICATION_URL;
    }
    if (service === 'audit') {
      API_URL = AUDIT_URL;
    }

    const response = await fetch(`${API_URL}${endpoint}`, config);
    
    // Handle unauthorized
    if (response.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = 'login.html';
      throw new Error('Unauthorized - Please login again');
    }
    
    // Handle forbidden
    if (response.status === 403) {
      window.location.href = 'access-denied.html';
      throw new Error('Access denied');
    }
    
    const data = await response.json();
    
    if (!response.ok) {
      throw new Error(data.message || 'Something went wrong');
    }
    
    return data;
  } catch (error) {
    console.error('API Error:', error);
    throw error;
  }
}

/**
 * GET request
 */
async function apiGet(endpoint, service = 'operations') {
  return apiRequest(endpoint, {
    method: 'GET'
  }, service);
}

/**
 * POST request
 */
async function apiPost(endpoint, data) {
  return apiRequest(endpoint, {
    method: 'POST',
    body: JSON.stringify(data)
  });
}

/**
 * PUT request
 */
async function apiPut(endpoint, data) {
  return apiRequest(endpoint, {
    method: 'PUT',
    body: JSON.stringify(data)
  });
}

/**
 * DELETE request
 */
async function apiDelete(endpoint) {
  return apiRequest(endpoint, {
    method: 'DELETE'
  });
}

/**
 * Upload file with form data
 */
async function apiUpload(endpoint, formData) {
  const token = getAuthToken();
  
  const config = {
    method: 'POST',
    headers: {
      ...(token && { 'Authorization': `Bearer ${token}` })
    },
    body: formData
  };

  try {
    const response = await fetch(`${API_BASE_URL}${endpoint}`, config);
    
    if (response.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = 'login.html';
      throw new Error('Unauthorized');
    }
    
    const data = await response.json();
    
    if (!response.ok) {
      throw new Error(data.message || 'Upload failed');
    }
    
    return data;
  } catch (error) {
    console.error('Upload Error:', error);
    throw error;
  }
}

/**
 * Export CSV
 */
async function apiExportCSV(endpoint, filename = 'export.csv', service = 'operations') {
  const token = getAuthToken();
  let API_URL = API_BASE_URL;
  if (service === 'audit') {
    API_URL = AUDIT_URL;
  }
  try {
    const response = await fetch(`${API_URL}${endpoint}`, {
      method: 'GET',
      headers: {
        ...(token && { 'Authorization': `Bearer ${token}` })
      }
    });
    
    if (!response.ok) {
      throw new Error('Export failed');
    }
    
    const blob = await response.blob();
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = filename;
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    window.URL.revokeObjectURL(url);
  } catch (error) {
    console.error('Export Error:', error);
    throw error;
  }
}
