// Общие функции для всех страниц

const API_BASE = '';

function getToken() {
    return localStorage.getItem('token');
}

function getUser() {
    return JSON.parse(localStorage.getItem('user') || '{}');
}

function isAuthenticated() {
    return !!getToken();
}

function redirectToLogin() {
    if (!isAuthenticated() && !window.location.pathname.includes('login') && !window.location.pathname.includes('register')) {
        window.location.href = '/login.html';
    }
}

function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    window.location.href = '/';
}

async function fetchAPI(url, options = {}) {
    const headers = {
        'Content-Type': 'application/json',
        ...options.headers
    };

    const token = getToken();
    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }

    const response = await fetch(url, {
        ...options,
        headers
    });

    if (response.status === 401) {
        logout();
        throw new Error('Сессия истекла');
    }

    return response;
}