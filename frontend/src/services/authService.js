import axios from 'axios';

const API_URL = 'http://localhost:5000/api';

const authService = {
    login: async (email, password) => {
        const response = await axios.post(`${API_URL}/auth/login`, {
            email,
            password
        });
        if (response.data.token) {
            localStorage.setItem('user', JSON.stringify(response.data));
        }
        return response.data;
    },

    register: async (userData) => {
        const response = await axios.post(`${API_URL}/auth/register`, userData);
        if (response.data.token) {
            localStorage.setItem('user', JSON.stringify(response.data));
        }
        return response.data;
    },

    logout: () => {
        localStorage.removeItem('user');
    },

    getCurrentUser: () => {
        return JSON.parse(localStorage.getItem('user'));
    },

    getAuthHeader: () => {
        const user = JSON.parse(localStorage.getItem('user'));
        if (user && user.token) {
            return { Authorization: `Bearer ${user.token}` };
        }
        return {};
    }
};

export default authService; 