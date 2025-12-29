import React, { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { authService } from '../../services/authServices';
import '../../styles/Auth.css';

const Register: React.FC = () => {
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    const [error, setError] = useState('')
    const [loading, setLoading] = useState(false)
    const [name, setName] = useState('')
    const [roleId, setRoleId] = useState(2)

    const { login, isAuthenticated } = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        if (isAuthenticated) {
            navigate('/dashboard');
        }
    }, [isAuthenticated, navigate]);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();        
        setError('');
        setLoading(true);
        
        try {
            await authService.register({ name, email, password, roleId })
        } catch (registerError: any) {
            if (registerError.response?.status !== 400) {
                console.log('Error en registro, pero intentando login...')
            } else {
                setError(registerError.response?.data?.message || 'Error al registrarse')
                setLoading(false)
                return;
            }
        }
        
        try {
            const loginResponse = await authService.login({ email, password })
            login(loginResponse.token, loginResponse.user)
            
            if (loginResponse.user.roleName === 'ADMIN'){
                navigate('/admin/dashboard')
            } else {
                navigate('/dashboard')
            }
        } catch (loginError: any) {
            setError('Cuenta creada pero error al iniciar sesión. Por favor, inicia sesión manualmente.')
        } finally {
            setLoading(false)
        }
    };

    return (
        <div className="auth-container">
            <div className="auth-card">
                <h2>Crear Cuenta</h2>
                <form onSubmit={handleSubmit} className="auth-form">

                    <div className="role-selector" style={{ display: 'flex', justifyContent: 'center', gap: '1.5rem', marginBottom: '1rem' }}>
                        <label style={{ display: 'flex', gap: '0.5rem', alignItems: 'center', cursor: 'pointer', color: 'var(--text-primary)' }}>
                            <input 
                                type="radio" 
                                name="accountType" 
                                value="2" 
                                checked={roleId === 2}
                                onChange={() => setRoleId(2)}
                            />
                            Usuario
                        </label>
                        <label style={{ display: 'flex', gap: '0.5rem', alignItems: 'center', cursor: 'pointer', color: 'var(--text-primary)' }}>
                            <input 
                                type="radio" 
                                name="accountType" 
                                value="1" 
                                checked={roleId === 1}
                                onChange={() => setRoleId(1)}
                            />
                            Empresa
                        </label>
                    </div>

                    <div className="form-group">
                        <input 
                            className="auth-input"
                            type="text" 
                            value={name} 
                            onChange={(e) => setName(e.target.value)} 
                            placeholder="Nombre de usuario" 
                            required 
                        />
                    </div>
                    <div className="form-group">
                        <input 
                            className="auth-input"
                            type="email" 
                            value={email} 
                            onChange={(e) => setEmail(e.target.value)} 
                            placeholder="Correo electrónico"
                            required
                        />
                    </div>
                    <div className="form-group">
                        <input 
                            className="auth-input"
                            type="password" 
                            value={password} 
                            onChange={(e) => setPassword(e.target.value)} 
                            placeholder="Contraseña"
                            required
                        />
                    </div>

                    {error && <div className="error-message">{error}</div>}

                    <button type="submit" className="auth-button" disabled={loading}>
                        {loading ? 'Cargando...' : 'Registrarse'}
                    </button>

                    <div className="auth-link">
                        ¿Ya tienes cuenta? <Link to="/login">Inicia sesión aquí</Link>
                    </div>
                </form>
            </div>
        </div>
    )
};

export default Register