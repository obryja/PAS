import React from 'react';
import { Link } from 'react-router-dom';

const HomePage: React.FC = () => {
    return (
        <div className="container text-center mt-5">
            <h1 className="display-4 text-dark">Biblioteka</h1>
            <div className="mt-4">
                <h3>Wybierz opcję:</h3>
                <div className="row justify-content-center mt-3">
                    <div className="col-12 col-md-6 col-lg-4">
                        <div className="d-grid gap-3">
                            <Link to="/users/list" className="btn btn-outline-secondary">Lista użytkowników</Link>
                            <Link to="/users/add" className="btn btn-outline-secondary">Dodaj użytkownika</Link>
                            <Link to="/rents/list" className="btn btn-outline-secondary">Lista wypożyczeń</Link>
                            <Link to="/rents/add" className="btn btn-outline-secondary">Dodaj wypożyczenie</Link>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default HomePage;