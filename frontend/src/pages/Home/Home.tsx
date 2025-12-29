import React from "react";
import { Link } from "react-router-dom";
import '../../styles/Home.css';

export function Home() {

    return (
        <div className="home-container">
            <div className="hero">
                <h1>Tu marketplace de confianza</h1>
                <p>Encuentra lo que buscas en una sola plataforma</p>
            </div>
            <div className="top-offers">
                <h1>Ofertas TOP</h1>
            </div>
            <div className="most-selled">
                <h1>Más Vendidos</h1>
            </div>
            <div className="show-by-categories">
                
            </div>
        </div>
    )
}