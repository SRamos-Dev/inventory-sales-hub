
import { } from "react";
import { useCart } from "../context/CartContext";
import "../styles/CartSidebar.css";
import { ProductCard } from "./ProductCard";
import { useEffect } from "react";


export function CartSidebar() {
    // 3. Obtener datos del contexto
    const {cart, total, isCartOpen, toggleCart, removeFromCart} = useCart();
    useEffect(() => {
        if (isCartOpen) {
            document.body.style.overflow = 'hidden';
        } else {
            document.body.style.overflow = 'unset';
        }

        return () => {
            document.body.style.overflow = 'unset;'
        }
    }, [isCartOpen])
    // 4. Return del JSX
    return (
        <div className={`cart-overlay ${isCartOpen ? 'open' : ''}`} onClick={toggleCart}>
            <div className={`cart-sidebar ${isCartOpen ? 'open' : ''}`} onClick={(e) => e.stopPropagation()}>
                <div className="cart-header">
                    <h2>Tu carrito ({cart.length})</h2>
                    <button className="close-button" onClick={toggleCart}>
                        <svg xmlns="http://www.w3.org/2000/svg" width="15" height="15" viewBox="0 0 20 20"><path fill="#000000" d="M10 8.586L2.929 1.515L1.515 2.929L8.586 10l-7.071 7.071l1.414 1.414L10 11.414l7.071 7.071l1.414-1.414L11.414 10l7.071-7.071l-1.414-1.414L10 8.586z"/></svg>
                    </button>
                </div>
                <div className="cart-items">
                    {cart.map((item) => (
                        <div key={item.id} className="cart-item">  {/* Contenedor principal */}
                            <img className="cart-item-image" src={item.imageUrl ? `http://localhost:8080/uploads/products/${item.imageUrl}` : 'placeholder.png'}  alt={item.name}  />  {/* Sección 1: Imagen */}
                            
                            <div className="cart-item-info">         {/* Sección 2: Info */}
                                <h3>{item.name}</h3>
                                <p>${item.price}</p>
                                <p>Cantidad: {item.quantity}</p>
                            </div>
                            
                            <button className="cart-item-remove" onClick={() => removeFromCart(item.id)}>    {/* Sección 3: Botón */}
                                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 16 16">
                                    <path fill="currentColor" d="M2 5v10c0 .55.45 1 1 1h9c.55 0 1-.45 1-1V5H2zm3 9H4V7h1v7zm2 0H6V7h1v7zm2 0H8V7h1v7zm2 0h-1V7h1v7zm2.25-12H10V.75A.753.753 0 0 0 9.25 0h-3.5A.753.753 0 0 0 5 .75V2H1.75a.752.752 0 0 0-.75.75V4h13V2.75a.752.752 0 0 0-.75-.75zM9 2H6v-.987h3V2z"/>
                                </svg>
                            </button>
                        </div>
                    ))}
                </div>
                <div className="cart-footer">
                    <p>Total: </p>
                    <button>Comprar</button>
                </div>
            </div>
        </div>
    );
}