import '../../styles/Profile.css'
import { useAuth } from '../../context/AuthContext'
import { useState } from 'react';
import { userService } from '../../services/userService';


export function Profile() {
    const { user, updateUser } = useAuth();
    const [isEditing, setIsEditing] = useState(false);
    const [formData, setFormData] = useState({
        name: user?.name || '',
        currentPassword: '',
        newPassword: '',
        confirmPassword: '',
        profileImage: user?.profileImage || '',
    })


    if (!user) {
    return <div>Debes iniciar sesión para ver tu perfil</div>;
    }

    const handleSaveChanges = async () => {
    if (formData.newPassword && formData.newPassword !== formData.confirmPassword) {
        alert('Las contraseñas no coinciden')
        return 
    }
        try{
            // Solo enviar campos que tienen valores
            const dataToSend: any = {};
            if (formData.name && formData.name.trim()) {
                dataToSend.name = formData.name;
            }
            if (formData.newPassword && formData.newPassword.trim()) {
                dataToSend.currentPassword = formData.currentPassword;
                dataToSend.newPassword = formData.newPassword;
            }
            if (formData.profileImage && formData.profileImage.trim()) {
                dataToSend.profileImage = formData.profileImage;
            }
            
            const updatedUser = await userService.updateProfile(dataToSend)
            const token = localStorage.getItem('token')
            if (token) {
                updateUser(token, updatedUser)
            }
            alert('Perfil Actualizado')
            setIsEditing(false)
        }
        catch (error) {
            const errMsg = error instanceof Error ? error.message : String(error);
            alert('Error al guardar: ' + errMsg)
        }
    };
    return (
       <div className="profile-container">
    <div className="profile-header">
        <img src={user?.profileImage || '/default-avatar.png'} alt="Perfil" className="profile-avatar" />
        <h1>{user.name}</h1>
        <p>{user.roleName === 'ADMIN' ? 'Empresa' : 'Usuario'}</p>
        <button onClick={() => setIsEditing(!isEditing)} className='edit-profile-button'>{isEditing ? 'Cancelar' : 'Editar'}</button>
    </div>
    
    <div className="profile-info">
            <div className="info-item">
                <label>Nombre:</label>
                {isEditing ? (
                    <input 
                        type="text" 
                        value={formData.name}
                        onChange={(e) => setFormData({...formData, name: e.target.value})}
                    />
                ) : (
                    <span>{user.name}</span>
                )}
            </div>
            <div className="info-item">
                <label>Email:</label>
                <span>{user.email}</span>
            </div>
            {isEditing && (
                <>
                    <div className="info-item">
                        <label>Contraseña Actual:</label>
                        <input 
                            type="password" 
                            value={formData.currentPassword}
                            onChange={(e) => setFormData({...formData, currentPassword: e.target.value})}
                        />
                    </div>
                    <div className="info-item">
                        <label>Nueva Contraseña:</label>
                        <input 
                            type="password" 
                            value={formData.newPassword}
                            onChange={(e) => setFormData({...formData, newPassword: e.target.value})}
                        />
                    </div>
                    <div className="info-item">
                        <label>Confirmar Contraseña:</label>
                        <input 
                            type="password" 
                            value={formData.confirmPassword}
                            onChange={(e) => setFormData({...formData, confirmPassword: e.target.value})}
                        />
                    </div>

                    <div className="info-item">
                        <label>Imagen de Perfil:</label>
                        <input 
                            type="file" 
                            accept="image/*"
                            onChange={(e) => {
                                // Aquí manejaremos la imagen
                                const file = e.target.files?.[0];
                                if (file) {
                                    const reader = new FileReader();
                                    reader.onloadend = () => {
                                        setFormData({...formData, profileImage: reader.result as string});
                                    };
                                    reader.readAsDataURL(file);
                                }
                            }}
                        />
                    </div>
                    {isEditing && (
                    <button className="save-button" onClick={handleSaveChanges}>
                        Guardar Cambios
                    </button>
                    )}
                </>
            )}
        </div>
    </div>
    )
}