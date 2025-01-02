import book from '/book32.png';
import { Link, NavLink } from 'react-router-dom';

const Navbar = () => {
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
                        <li className="nav-item">
                            <NavLink className="nav-link" to="/users/list">Lista użytkowników</NavLink>
                        </li> 
                        <li className="nav-item">
                            <NavLink className="nav-link" to="/users/add">Utwórz użytkownika</NavLink>
                        </li> 
                        <li className="nav-item">
                            <NavLink className="nav-link" to="/rents/list">Lista wypożyczeń</NavLink>
                        </li> 
                        <li className="nav-item">
                            <NavLink className="nav-link" to="/rents/add">Utwórz wypożyczenie</NavLink>
                        </li> 
                    </ul>
                </div>
            </div>
        </nav>
    );
};

export default Navbar;