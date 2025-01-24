import React from 'react';
import { Link } from 'react-router-dom';

import { useUserContext } from '../context/UserContext';

const HomePage: React.FC = () => {
    const { user } = useUserContext();

    return (
        <div className="container text-center mt-5">
            <h1 className="display-4 text-dark">Biblioteka</h1>
            <div className="mt-4">
                <h3>Wybierz opcję:</h3>
                <div className="row justify-content-center mt-3">
                    <div className="col-12 col-md-6 col-lg-4">
                        <div className="d-grid gap-3">
                            {user?.role === 'ROLE_ADMIN' && (
                                <>
                                    <Link to="/users/list" className="btn btn-outline-secondary">Lista użytkowników</Link>
                                </>
                            )}
                            {user?.role === 'ROLE_MANAGER' && (
                                <>
                                    <Link to="/users/list" className="btn btn-outline-secondary">Lista użytkowników</Link>
                                    <Link to="/rents/list" className="btn btn-outline-secondary">Lista wypożyczeń</Link>
                                    <Link to="/rents/add" className="btn btn-outline-secondary">Dodaj wypożyczenie</Link>
                                </>
                            )}
                            {user?.role === 'ROLE_CLIENT' && (
                                <>
                                    <Link to="/rents/add" className="btn btn-outline-secondary">Dodaj wypożyczenie</Link>
                                    <Link to="/info" className="btn btn-outline-secondary">Szczegóły użytkownika</Link>
                                </>
                            )}
                            {!user?.role && (
                                <>
                                    <Link to="/login" className="btn btn-outline-secondary">Zaloguj się</Link>
                                    <Link to="/users/add" className="btn btn-outline-secondary">Dodaj użytkownika</Link>
                                </>
                            )}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default HomePage;