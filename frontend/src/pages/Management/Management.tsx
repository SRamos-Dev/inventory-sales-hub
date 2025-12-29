import React from "react";
import axios from "axios";
import { useAuth } from "../../context/AuthContext";
import { Navigate } from "react-router-dom";
import { useState, useEffect } from "react";
import '../../styles/Management.css'
import type { Product } from "../../types/product.types";


export function Management() {
    const { user } = useAuth();
    const [createForm, showCreateForm] = useState(false)
    const [name, setName] = useState("")
    const [description, setDescription] = useState("")
    const [price, setPrice] = useState(1)
    const [stock, setStock] = useState(1)
    const [image, setImage] = useState<File | null>(null)
    const [categoryId, setCategoryId] = useState(2)
    const [products, setProducts] = useState<Product[]>([]);

    const fetchProducts = async () => {
        try {
            const token = localStorage.getItem('token');
            const response = await axios.get('/api/products', {
                headers: { Authorization: `Bearer ${token}` }
            });

            // El backend devuelve {success, message, data}
            if (response.data.data) {
                setProducts(response.data.data);
            }
        } catch (error) {
            console.error("Error cargando productos:", error);
        }
    };

    useEffect(() => {
        fetchProducts();
    }, []);

    // Desactivar scroll cuando el formulario esté abierto
    useEffect(() => {
        if (createForm) {
            document.body.style.overflow = 'hidden';
        } else {
            document.body.style.overflow = 'unset';
        }
        
        // Cleanup: restaurar el scroll cuando el componente se desmonte
        return () => {
            document.body.style.overflow = 'unset';
        };
    }, [createForm]);


    if (!user) {
        return <Navigate to='/login' replace />
    }
    if (user.roleName !== 'ADMIN') {
        return <Navigate to='/' replace />
    }

    const handleCreate = async (e: React.MouseEvent<HTMLInputElement>) => {
        e.preventDefault();
        
        // Crear FormData en lugar de un objeto JSON
        const formData = new FormData();
        formData.append('name', name);
        formData.append('description', description);
        formData.append('price', price.toString());
        formData.append('stock', stock.toString());
        formData.append('categoryId', categoryId.toString());
        
        // Añadir la imagen si existe
        if (image) {
            formData.append('image', image);
        }

        const token = localStorage.getItem('token')
        await axios.post(
            '/api/products',
            formData,
            { 
                headers: { 
                    Authorization: `Bearer ${token}`
                    // No establecer Content-Type manualmente, FormData lo hace automáticamente
                } 
            }
        )
        setName("")
        setDescription("")
        setPrice(1)
        setStock(1)
        setImage(null)
        showCreateForm(false)
        fetchProducts()
    }

    return (
        <div className="management-pannel">
            <h1 className="title">Panel de gestion</h1>
            {!createForm &&
            <button className="add-article-button" onClick={() => showCreateForm(true)}><svg xmlns="http://www.w3.org/2000/svg" width="200" height="200" viewBox="0 0 24 24"><path fill="#000000" d="M10.5 13h-7c-.3 0-.5.2-.5.5v7c0 .3.2.5.5.5h7c.3 0 .5-.2.5-.5v-7c0-.3-.2-.5-.5-.5zm-.5 7H4v-6h6v6zm.5-17h-7c-.3 0-.5.2-.5.5v7c0 .3.2.5.5.5h7c.3 0 .5-.2.5-.5v-7c0-.3-.2-.5-.5-.5zm-.5 7H4V4h6v6zm10.5-7h-7c-.3 0-.5.2-.5.5v7c0 .3.2.5.5.5h7c.3 0 .5-.2.5-.5v-7c0-.3-.2-.5-.5-.5zm-.5 7h-6V4h6v6zm.5 6.5h-3v-3c0-.3-.2-.5-.5-.5s-.5.2-.5.5v3h-3c-.3 0-.5.2-.5.5s.2.5.5.5h3v3c0 .3.2.5.5.5s.5-.2.5-.5v-3h3c.3 0 .5-.2.5-.5s-.2-.5-.5-.5z"/></svg></button>
            }
            {createForm && (
            <div className="form-overlay" onClick={() => showCreateForm(false)}>
                <form action="" className="create-form" onClick={(e) => e.stopPropagation()}>
                <label htmlFor="product-name">Nombre del artículo</label>
                <input 
                    id="product-name"
                    value={name} 
                    onChange={(e) => setName(e.target.value)} 
                    type="text" 
                    placeholder="Nombre del articulo" 
                />
                
                <label htmlFor="product-description">Descripción</label>
                <textarea 
                    id="product-description"
                    value={description} 
                    onChange={(e) => setDescription(e.target.value)} 
                    placeholder="Descripción del artículo (mínimo 20 caracteres)"
                ></textarea>
                
                <label htmlFor="product-image">Imagen del producto</label>
                <input
                    id="product-image"
                    onChange={(e) => {
                        if (e.target.files && e.target.files[0]) {
                            setImage(e.target.files[0]);
                        } else {
                            setImage(null);
                        }
                    }}
                    type="file"
                    accept="image/*"  
                />
                {image && <p className="file-name">Archivo seleccionado: {image.name}</p>}
                
                <label htmlFor="product-category">Categoría</label>
                <input
                    id="product-category"
                    type="number"
                    value={categoryId}
                    onChange={e => setCategoryId(Number(e.target.value))}
                    placeholder="ID de categoría"
                />
                
                <label htmlFor="product-stock">Stock</label>
                <input 
                    id="product-stock"
                    type="number" 
                    value={stock} 
                    onChange={(e) => setStock(Number(e.target.value))} 
                    className="stock-input"
                />
                
                <label htmlFor="product-price">Precio</label>
                <input 
                    id="product-price"
                    type="number" 
                    value={price} 
                    onChange={(e) => setPrice(Number(e.target.value))} 
                    className="price-input"
                />
                
                <input type="button" className='create-button' value="Crear" onClick={handleCreate}/>
                <input type="button" className='cancel-button' value="Cancelar" onClick={()=> showCreateForm(false)} />
            </form>
            </div>
            )}

            <ul className="management-items-list">
                {products.map((product) => (
                    <li key={product.id} className="product-item-card">
                        {product.imageUrl && (
                            <img 
                                src={`http://localhost:8080/uploads/products/${product.imageUrl}`}
                                alt={product.name} 
                                className="product-thumb" 
                            />
                        )}
                        
                        <div className="product-details">
                            <strong>{product.name}</strong> - ${product.price}
                            <br/>
                            <small>Stock: {product.stock} | Categoría: {product.categoryName}</small>
                        </div>
                    </li>
                ))}
            </ul>
        </div>
    )
}

