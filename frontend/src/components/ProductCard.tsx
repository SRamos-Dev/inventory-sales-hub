import type { Product } from "../types/product.types";
import '../styles/ProductCard.css'

interface ProductCardProps {
    product: Product;
}

export const ProductCard = ({product}: ProductCardProps) => {
    const imageUrl = product.imageUrl
        ? (product.imageUrl.startsWith('http') ? product.imageUrl : `http://localhost:8080/uploads/products/${product.imageUrl}`)
        : null;

    return (
            <div className="product-card">
                <div className="card-image-container">
                    {imageUrl ? (
                        <img src={imageUrl} alt={product.name} />
                    ) : (
                        <div className="placeholder-image">
                            <span>Sin imagen</span>
                        </div>
                    )}
                </div>
                
                <div className="card-info">
                    <h3>{product.name}</h3>
                    <p className="price">${product.price}</p>
                    <p className="category">{product.categoryName}</p>
                    
                    <button 
                        className="add-cart-btn"
                        onClick={() => alert(`Añadido ${product.name} al carrito`)}
                    >
                        Añadir al carrito
                    </button>
                </div>
            </div>
    );
}