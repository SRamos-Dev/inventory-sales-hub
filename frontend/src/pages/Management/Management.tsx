import React from "react";
import axios from "axios";
import { useAuth } from "../../context/AuthContext";
import { Navigate } from "react-router-dom";
import { useState, useEffect } from "react";
import '../../styles/Management.css'

export function Management() {
    const { user } = useAuth();
    const [createForm, showCreateForm] = useState(false)
    const [name, setName] = useState("")
    const [description, setDescription] = useState("")
    const [price, setPrice] = useState(1)
    const [stock, setStock] = useState(1)
    const [image, setImage] = useState<File | null>(null)
    const [categoryId, setCategoryId] = useState(2)

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
    }

    return (
        <div className="management-pannel">
            <h1>Panel de gestion</h1>
            {!createForm &&
            <button className="add-article-button" onClick={() => showCreateForm(true)}>Añadir Artículo</button>
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
                
            </ul>
        </div>
    )
}

