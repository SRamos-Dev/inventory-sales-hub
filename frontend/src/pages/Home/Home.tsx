import React, { useEffect } from "react";
import { Link } from "react-router-dom";
import '../../styles/Home.css';
import { useState } from "react";
import { ProductCard } from "../../components/ProductCard";
import type { Product } from "../../types/product.types";
import axios from "axios";

export function Home() {

    const [products, setProducts] = useState<Product[]>([])
    const fetchProducts = async () => {
        try {
            const response = await axios.get('/api/products');

            if (response.data.data) {
                setProducts(response.data.data);
            }
        } catch (error) {
            console.error("Error cargando productos:", error);
        }
    };

    useEffect (() => {
        fetchProducts();
    }, [])

    return (
        <div className="home-container">
            <div className="hero">
                <h1>Tu marketplace de confianza</h1>
                <p>Encuentra lo que buscas en una sola plataforma</p>
            </div>
            <div className="products-grid" >
                {products.map((product) => (
                    <ProductCard key={product.id} product={product} />
                ))}
            </div>
        </div>
    )
}