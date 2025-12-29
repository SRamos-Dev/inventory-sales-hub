import { Link, useLocation } from "react-router-dom";
import { useState, useRef, useEffect } from "react";
import { useAuth } from "../context/AuthContext";
import { useTheme } from "../context/ThemeContext";
import '../styles/Navbar.css';


export function Navbar() {
    const {user, logout} = useAuth();  // ⬅️ Dentro del componente
    const {theme, toggleTheme} = useTheme();
    const [isMenuOpen, setIsMenuOpen] = useState(false);
    const menuRef = useRef<HTMLDivElement>(null);

    const location = useLocation();

    useEffect(() => {
        const handleClickOutside = (event: MouseEvent) => {
            if (menuRef.current && !menuRef.current.contains(event.target as Node)) {
                setIsMenuOpen(false);
            }
        };

        document.addEventListener('mousedown', handleClickOutside);

        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, []);

    const hideNavbarRoutes = ['/login', '/register'];
    if (hideNavbarRoutes.includes(location.pathname)) return null;

    return (
        <nav className="navbar">
            <Link to="/">
                <img className="logo" src="/logo.png" alt="logo" />
            </Link>
            <div className="navbar-right">
            <div className="search-bar">
                <input type="text" placeholder="Buscar en Sales Hub" />
                <button>
                    <svg xmlns="http://www.w3.org/2000/svg" width="200" height="200" viewBox="0 0 20 20"><path fill="#000000" d="M12.9 14.32a8 8 0 1 1 1.41-1.41l5.35 5.33l-1.42 1.42l-5.33-5.34zM8 14A6 6 0 1 0 8 2a6 6 0 0 0 0 12z"/></svg>
                </button>

            </div>
            
            <button className="theme-toggle" onClick={toggleTheme} title={`Cambiar a ${theme === 'light' ? 'modo oscuro' : 'modo claro'}`}>
                {theme === 'light' ? (
                    // Icono de luna (modo oscuro)
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                        <path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"></path>
                    </svg>
                ) : (
                    // Icono de sol (modo claro)
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                        <circle cx="12" cy="12" r="5"></circle>
                        <line x1="12" y1="1" x2="12" y2="3"></line>
                        <line x1="12" y1="21" x2="12" y2="23"></line>
                        <line x1="4.22" y1="4.22" x2="5.64" y2="5.64"></line>
                        <line x1="18.36" y1="18.36" x2="19.78" y2="19.78"></line>
                        <line x1="1" y1="12" x2="3" y2="12"></line>
                        <line x1="21" y1="12" x2="23" y2="12"></line>
                        <line x1="4.22" y1="19.78" x2="5.64" y2="18.36"></line>
                        <line x1="18.36" y1="5.64" x2="19.78" y2="4.22"></line>
                    </svg>
                )}
            </button>

            {user ? (
                <div className="user-menu" ref={menuRef}>
                    <button className="profile-button" onClick={() => setIsMenuOpen(!isMenuOpen)}>
                        <img className="profile-image" src={user.profileImage || '/default-avatar.png'} alt="Perfil" />
                    </button>
                        {isMenuOpen && (
                            <div className="dropdown-menu">
                                <Link to="/profile" className="dropdown-item">Perfil</Link>
                                <Link to="/settings" className="dropdown-item">Configuración</Link>
                                {user.roleName === 'admin' && ( 
                                    <Link to="/management" className="dropdown-item">Gestion</Link>
                                )}
                                <button onClick={logout} className="dropdown-item">Cerrar Sesión</button>
                            </div>
                        )}

                </div>
            ) : (
                <div className="auth-buttons">
                    <button><Link to='/login'>Iniciar Sesion</Link></button>
                    <button><Link to='/register'>Registrarse</Link></button>
                </div>
            )}
            </div>
        </nav>
    )
}