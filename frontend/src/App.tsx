import React from 'react';
import AppRouter from './router/AppRouter';
import { CartSidebar } from './components/CartSidebar';
import './styles/App.css';
import { Popup } from './components/Popup';

function App() {
    return (
    <>
        <AppRouter />
        <CartSidebar />
        <Popup />
    </>
    )
}

export default App;