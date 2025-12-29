import React, { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import { authService } from '../../services/authServices';
import { useNavigate, Link } from 'react-router-dom';
import '../../styles/Auth.css';

const Login: React.FC = () => {
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    const [error, setError] = useState('')
    const [loading, setLoading] = useState(false)

    const { login, isAuthenticated } = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        console.log("Login component mounted");
        if (isAuthenticated) {
            navigate('/');
        }
    }, [isAuthenticated, navigate]);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();        
        setError('');
        setLoading(true);
        try {
            const response = await authService.login({ email, password })
            login(response.token, response.user)
            if (response.user.roleName === 'ADMIN'){
                navigate('/')
            } else {
                navigate('/')
            }
        } catch (err: any) {
            setError (err.response?.data?.message || 'Error al iniciar sesion')
        } finally {
            setLoading(false)
        }
    };

    return (
        <div className="auth-container">
            <div className="auth-card">
                <h2>Iniciar Sesión</h2>
                <form onSubmit={handleSubmit} className="auth-form">
                    <div className="form-group">
                        <input 
                            className="auth-input"
                            type="email" 
                            value={email} 
                            onChange={(e) => setEmail(e.target.value)} 
                            placeholder="Email"
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
                        {loading ? 'Cargando...' : 'Entrar'}
                    </button>
                    
                    <div className="auth-link">
                        ¿No tienes cuenta? <Link to="/register">Regístrate aquí</Link>
                    </div>
                </form>
            </div>
        </div>
    )
};

export default Login