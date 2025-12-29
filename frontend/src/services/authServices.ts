import api from './api';
import type { LoginRequest, RegisterRequest, LoginResponse, RegisterResponse, User } from '../types/auth.types';

// Estructura de respuesta del backend
interface ApiResponse<T> {
    success: boolean;
    message: string;
    data: T;
}

export const authService = {
    login: async (credentials: LoginRequest) => {
        const response = await api.post<ApiResponse<LoginResponse>>('/api/auth/login', credentials);
        const loginData = response.data.data;
        
        // Construir el objeto user a partir de la respuesta
        const user: User = {
            id: loginData.userId,
            name: loginData.userName,
            email: loginData.userEmail,
            roleName: loginData.roleName
        };
        
        return { token: loginData.token, user };
    },
    register: async (credentials: RegisterRequest) => {
        const response = await api.post<ApiResponse<RegisterResponse>>('/api/auth/register', credentials);
        const userData = response.data.data;
        
        // Construir el objeto user
        const user: User = {
            id: userData.id,
            name: userData.name,
            email: userData.email,
            roleName: userData.roleName
        };
        
        // Register no devuelve token, hay que hacer login después
        return { user };
    },
    logout: () => {
        localStorage.removeItem('token')
        localStorage.removeItem('user')
    },
    saveSession: (token: string, user: any) => {
        localStorage.setItem('token', token);
        localStorage.setItem('user', JSON.stringify(user))
    },
    getCurrentUser: () => {
        const user = localStorage.getItem('user');
        return user ? JSON.parse(user) : null;
    },

    isAuthenticated: (): boolean => {
        return !!localStorage.getItem('token');
    }

};