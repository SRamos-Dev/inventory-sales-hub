import api from './api';
import type { User } from '../types/auth.types';

export interface UpdateProfileRequest {
    name?: string;
    currentPassword?: string;
    newPassword?: string;
    profileImage?: string;
}

interface ApiResponse<T> {
    success: boolean;
    message: string;
    data: T;
}

export const userService = {
    updateProfile: async (data: UpdateProfileRequest): Promise<User> => {
        const response = await api.post<ApiResponse<User>>('/api/users/update', data);
        return response.data.data;
    }
}