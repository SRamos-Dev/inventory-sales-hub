import { useCart } from "../context/CartContext";
import { useState, useEffect } from "react";
import "../styles/Popup.css";

export const Popup = () => {
    const {showNotification} = useCart();
    const [isExiting, setIsExiting] = useState(false);

    useEffect(() => {
        if (showNotification) {
            setIsExiting(false);  // Reinicia el estado al aparecer
        }
    }, [showNotification]);

    if (!showNotification && !isExiting) return null;

    return (
        <div className={`popup-container ${isExiting ? 'exit' : ''}`}>
            <div className="popup-content">
                ✅ ¡Producto añadido al carrito!
            </div>
        </div>
    );
};