// PARTE 1: Imports
import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from '../context/AuthContext';
import Login from '../pages/Auth/Login';
import Register from '../pages/Auth/Register';
import { Navbar } from '../components/Navbar';
import { Home } from '../pages/Home/Home';
import { Profile } from '../pages/Profile/Profile';

// PARTE 2: Componente
const AppRouter: React.FC = () => {
    return (
        <BrowserRouter>
            <AuthProvider>
                <Navbar />
                <Routes>
                    <Route path='/' element={<Home />} />
                    <Route path='/login' element={<Login />} />
                    <Route path='/register' element={<Register />} />
                    <Route path='/profile' element={<Profile />} />
                </Routes>
            </AuthProvider>
        </BrowserRouter>
    );
};

// PARTE 3: Export
export default AppRouter;