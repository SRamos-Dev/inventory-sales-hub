import React, { createContext, useContext, useState, useEffect } from 'react';
import type { User } from '../types/auth.types';
import { authService } from '../services/authServices';
import { userService } from '../services/userService';

interface AuthContextType {
    user: User | null;
    isAuthenticated: boolean;
    login: (token: string, user: User) => void;
    logout: () => void;
    loading: boolean;
    updateUser: (token: string, user: User) => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    const [user, setUser] = useState<User | null>(null);
    const [loading, setLoading] = useState<boolean>(true);

    useEffect(() => {
        const savedUser = authService.getCurrentUser();
        if (savedUser) {
            setUser(savedUser)
        }
        setLoading(false)
    }, []);

    const login = (token: string, userData: User) => {
        authService.saveSession(token, userData)
        setUser(userData)
    }

    const logout = () => {
        authService.logout()
        setUser(null)
    }

    const updateUser = (token: string, userData: User) => {
        setUser(userData)
        authService.saveSession(token, userData)
    }

    return (
        <AuthContext.Provider
            value = {{
                user,
                isAuthenticated: !!user,
                login,
                logout,
                loading,
                updateUser,
            }}
        >
            {children}
        </AuthContext.Provider>
    )
};

export const useAuth = () => {
    const context = useContext(AuthContext);
    if(!context) {
        throw new Error('useAuth debe usarse dentro de AuthProvider');
    } 
    return context;
};