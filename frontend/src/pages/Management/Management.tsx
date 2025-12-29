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
    const [editingId, setEditingId] = useState<number | null>(null)

    const fetchProducts = async () => {
        try {
            const token = localStorage.getItem('token');
            const response = await axios.get('/api/products/my-products', {
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
        
        const formData = new FormData();
        formData.append('name', name);
        formData.append('description', description);
        formData.append('price', price.toString());
        formData.append('stock', stock.toString());
        formData.append('categoryId', categoryId.toString());
        
        if (image) {
            formData.append('image', image);
        }

        const token = localStorage.getItem('token');
        
        try {
            if (editingId) {
                // MODO EDICIÓN
                await axios.put(
                    `/api/products/${editingId}`,
                    formData,
                    { 
                        headers: { 
                            Authorization: `Bearer ${token}`
                        } 
                    }
                );
            } else {
                // MODO CREACIÓN
                await axios.post(
                    '/api/products',
                    formData,
                    { 
                        headers: { 
                            Authorization: `Bearer ${token}`
                        } 
                    }
                );
            }
            
            // Limpiar formulario y recargar productos
            setName("");
            setDescription("");
            setPrice(1);
            setStock(1);
            setImage(null);
            setEditingId(null);
            showCreateForm(false);
            fetchProducts();
        } catch (error) {
            console.error("Error al guardar:", error);
            alert("Hubo un error al guardar el producto");
        }
    }

    const handleEdit = (product: Product) => {
        setEditingId(product.id);
        setName(product.name);
        setDescription(product.description);
        setPrice(product.price);
        setStock(product.stock);
        setImage(null);
        showCreateForm(true);
    }

    const handleCancel = () => {
        setName("");
        setDescription("");
        setPrice(1);
        setStock(1);
        setImage(null);
        setEditingId(null);
        showCreateForm(false);
    }

    const handleDelete = async (id: number) => {
        if (!window.confirm("¿Estas seguro de eliminar el producto?")) return;

        try{
            const token = localStorage.getItem('token');
            await axios.delete(`/api/products/${id}`, {
            headers: { Authorization: `Bearer ${token}` }
        });

        fetchProducts();

        } catch (error) {
            console.error("Error al eliminar:", error);
            alert("No se pudo eliminar el producto")
        }
    };

    return (
        <div className="management-pannel">
            <h1 className="title">Panel de gestion</h1>
            {!createForm &&
            <button className="add-article-button" onClick={() => showCreateForm(true)}><svg xmlns="http://www.w3.org/2000/svg" width="200" height="200" viewBox="0 0 24 24"><path fill="#000000" d="M10.5 13h-7c-.3 0-.5.2-.5.5v7c0 .3.2.5.5.5h7c.3 0 .5-.2.5-.5v-7c0-.3-.2-.5-.5-.5zm-.5 7H4v-6h6v6zm.5-17h-7c-.3 0-.5.2-.5.5v7c0 .3.2.5.5.5h7c.3 0 .5-.2.5-.5v-7c0-.3-.2-.5-.5-.5zm-.5 7H4V4h6v6zm10.5-7h-7c-.3 0-.5.2-.5.5v7c0 .3.2.5.5.5h7c.3 0 .5-.2.5-.5v-7c0-.3-.2-.5-.5-.5zm-.5 7h-6V4h6v6zm.5 6.5h-3v-3c0-.3-.2-.5-.5-.5s-.5.2-.5.5v3h-3c-.3 0-.5.2-.5.5s.2.5.5.5h3v3c0 .3.2.5.5.5s.5-.2.5-.5v-3h3c.3 0 .5-.2.5-.5s-.2-.5-.5-.5z"/></svg></button>
            }
            {createForm && (
            <div className="form-overlay" onClick={handleCancel}>
                <form action="" className="create-form" onClick={(e) => e.stopPropagation()}>
                <h2>{editingId ? "Editar Producto" : "Crear Nuevo Producto"}</h2>
                
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
                
                <input 
                    type="button" 
                    className='create-button' 
                    value={editingId ? "Guardar Cambios" : "Crear"} 
                    onClick={handleCreate}
                />
                <input 
                    type="button" 
                    className='cancel-button' 
                    value="Cancelar" 
                    onClick={handleCancel} 
                />
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

                        <div className="product-actions">
                            <button 
                                className="edit-button" 
                                onClick={(e) => {
                                    e.stopPropagation();
                                    handleEdit(product);
                                }}
                            >
                                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24">
                                    <path fill="currentColor" d="M20.71,7.04C21.1,6.65 21.1,6 20.71,5.63L18.37,3.29C18,2.9 17.35,2.9 16.96,3.29L15.12,5.12L18.87,8.87M3,17.25V21H6.75L17.81,9.93L14.06,6.18L3,17.25Z" />
                                </svg>
                            </button>
                            <button 
                                className="bin-button" 
                                onClick={(e) => {
                                    e.stopPropagation();
                                    handleDelete(product.id);
                                }}
                            >
                                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 16 16">
                                    <path fill="currentColor" d="M2 5v10c0 .55.45 1 1 1h9c.55 0 1-.45 1-1V5H2zm3 9H4V7h1v7zm2 0H6V7h1v7zm2 0H8V7h1v7zm2 0h-1V7h1v7zm2.25-12H10V.75A.753.753 0 0 0 9.25 0h-3.5A.753.753 0 0 0 5 .75V2H1.75a.752.752 0 0 0-.75.75V4h13V2.75a.752.752 0 0 0-.75-.75zM9 2H6v-.987h3V2z"/>
                                </svg>
                            </button>
                        </div>
                    </li>
                ))}
            </ul>
        </div>
    )
}

