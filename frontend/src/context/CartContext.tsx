import { createContext, useContext, useState } from "react";
import type { ReactNode } from "react";
import type { Product, CartItem } from "../types/product.types";

interface CartContextType {
    cart: CartItem[];
    addToCart: (product: Product) => void;
    removeFromCart: (id: number) => void;
    total: number;
    isCartOpen: boolean;
    toggleCart: () => void;
    showNotification: boolean;
}

const CartContext = createContext<CartContextType | undefined>(undefined)

export const useCart = () => {
    const context = useContext(CartContext)
    if (!context) {
        throw new Error('useCart debe usarse dentro de un CartProvider')
    }
    return context;
}

export const CartProvider = ({children} : {children: ReactNode}) => {
    const [cart, setCart] = useState<CartItem[]>([])
    const [isCartOpen, setIsCartOpen] = useState(false)
    const [showNotification, setShowNotification] = useState(false)

    const toggleCart = () => {
        setIsCartOpen(prev => !prev)
    }

    const addToCart = (product: Product) => {
        setCart(prevCart => {
            const itemExists = prevCart.find(item => item.id === product.id);

            if (itemExists) {
                return prevCart.map(item => 
                    item.id === product.id
                        ? {...item, quantity: item.quantity + 1}
                        : item
                );
            } else {
                return [...prevCart, {...product, quantity: 1}]
            }
        })

        setShowNotification(true)
        setTimeout(() => {
            setShowNotification(false)
        }, 2700);
    }

    const removeFromCart = (id: number) => {
        setCart(prevCart => prevCart.filter(item => item.id !== id))
    }

    const total = cart.reduce((acc, item) => acc + (item.price * item.quantity), 0)

    return (
        <CartContext.Provider value={{ cart, addToCart, removeFromCart, total, isCartOpen, toggleCart, showNotification }}>
            {children}
        </CartContext.Provider>
    )
    
} 
