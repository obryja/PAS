import axios from 'axios';

const api = axios.create({
    baseURL: 'http://127.0.0.1:8080/api',
});

api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('jwt');
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

api.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response && error.response.status === 401) {
            localStorage.removeItem('jwt');
        }
        return Promise.reject(error);
    }
);

export default api;