import { Link, NavLink } from 'react-router-dom';

import book from '/book32.png';
import { useUserContext } from '../context/UserContext';

const Navbar = () => {
    const { logout, user } = useUserContext();

    const roleMap: { [key: string]: string } = {
        ROLE_ADMIN: "Administrator",
        ROLE_MANAGER: "Manager",
        ROLE_CLIENT: "Klient",
    };

    return (
        <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
            <div className="container">
                <Link className="navbar-brand" to="/">
                    <img src={book} alt='Logo biblioteki' className="d-inline-block align-text-top" />
                    <span className="ms-2">Biblioteka</span>
                </Link>
                <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                    <span className="navbar-toggler-icon"></span>
                </button>
                <div className="collapse navbar-collapse" id="navbarNav">
                    <ul className="navbar-nav me-auto">
                        {user?.role === 'ROLE_CLIENT' && (
                            <>
                                <li className="nav-item">
                                    <NavLink className="nav-link" to="/rents/add">Utwórz wypożyczenie</NavLink>
                                </li> 
                                <li className="nav-item">
                                    <NavLink className="nav-link" to="/info">Szczegóły użytkownika</NavLink>
                                </li> 
                            </>
                        )}
                        {user?.role === 'ROLE_ADMIN' && (
                            <>
                                <li className="nav-item">
                                    <NavLink className="nav-link" to="/users/list">Lista użytkowników</NavLink>
                                </li> 
                            </>
                            
                        )}
                        {user?.role === 'ROLE_MANAGER' && (
                            <>
                                <li className="nav-item">
                                    <NavLink className="nav-link" to="/users/list">Lista użytkowników</NavLink>
                                </li> 
                                <li className="nav-item">
                                    <NavLink className="nav-link" to="/rents/list">Lista wypożyczeń</NavLink>
                                </li> 
                                <li className="nav-item">
                                    <NavLink className="nav-link" to="/rents/add">Utwórz wypożyczenie</NavLink>
                                </li> 
                            </>
                        )}
                    </ul>
                    <ul className="navbar-nav">
                        {user.role ? (
                                <>
                                    <li className="nav-item">
                                        <NavLink className="nav-link" to="/password">{`${user.username} (${roleMap[user.role]})`}</NavLink>
                                    </li>
                                    <li className="nav-item">
                                        <button className="btn btn-link nav-link" onClick={() => { logout();}}>Wyloguj się</button>
                                    </li> 
                                </>
                            ) : (
                            <>
                                <li className="nav-item">
                                    <NavLink className="nav-link" to="/login">Zaloguj się</NavLink>
                                </li> 
                                <li className="nav-item">
                                    <NavLink className="nav-link" to="/users/add">Utwórz użytkownika</NavLink>
                                </li> 
                            </>
                        )}
                    </ul>
                </div>
            </div>
        </nav>
    );
};

export default Navbar;