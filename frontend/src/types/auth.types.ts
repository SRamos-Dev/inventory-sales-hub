export interface User {
    id: number;
    name: string;
    email: string;
    roleName: string;
    profileImage?: string;
}

export interface LoginRequest {
    email: string;
    password: string;
}

export interface RegisterRequest {
    name: string;
    email: string;
    password: string;
    roleId: number;
}

// Respuesta del login (con token)
export interface LoginResponse {
    token: string;
    tokenType: string;
    userId: number;
    userName: string;
    userEmail: string;
    roleName: string;
}

// Respuesta del register (sin token)
export interface RegisterResponse {
    id: number;
    name: string;
    email: string;
    roleName: string;
}