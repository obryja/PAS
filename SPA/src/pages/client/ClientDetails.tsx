import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from '../../api/Axios';
import UserGet from '../../model/UserGet';
import Rent from '../../model/Rent';

const UserDetails: React.FC = () => {
    const { id } = useParams<{ id: string }>();
    const navigate = useNavigate();
    const [user, setUser] = useState<UserGet | null>(null);
    const [error, setError] = useState<string | null>(null);
    const [currentRents, setCurrentRents] = useState<Rent[]>([]);
    const [archiveRents, setArchiveRents] = useState<Rent[]>([]);

    useEffect(() => {
        const fetchUser = async () => {
            try {
                const response = await axios.get(`/users/${id}`);
                setUser(response.data);
            } catch (err) {
                console.error('Błąd podczas pobierania danych użytkownika:', err);
                setError('Nie udało się załadować danych użytkownika.');
            }
        };

        const fetchRents = async () => {
            try {
                const currentRentsResponse = await axios.get(`/rents/user/current/${id}`);
                setCurrentRents(currentRentsResponse.data);

                const archiveRentsResponse = await axios.get(`/rents/user/archive/${id}`);
                setArchiveRents(archiveRentsResponse.data);
            } catch (err) {
                console.error('Błąd podczas pobierania wypożyczeń:', err);
                setError('Nie udało się załadować wypożyczeń.');
            }
        };

        fetchUser();
        fetchRents();
    }, [id]);

    if (error) {
        return <div className="alert alert-danger">{error}</div>;
    }

    if (!user) {
        return <div className="text-center mt-4"><span className="spinner-border text-primary"></span> Ładowanie danych...</div>;
    }

    return (
        <div className="container my-4">
            <h2>Szczegóły użytkownika</h2>
            <div className="card mb-4">
                <div className="card-body">
                    <h5 className="card-title">Informacje o użytkowniku</h5>
                    <p><strong>ID:</strong> {user.id}</p>
                    <p><strong>Nazwa użytkownika:</strong> {user.username}</p>
                    <p><strong>Status:</strong> {user.active ? 'Aktywny' : 'Nieaktywny'}</p>
                    <button
                        className="btn btn-secondary mt-3"
                        onClick={() => navigate(-1)}
                    >
                        Powrót do listy
                    </button>
                </div>
            </div>

            <h3>Aktywne wypożyczenia</h3>
            <div className="table-responsive mb-4">
                <table className="table table-striped table-hover table-bordered">
                    <thead className="table-dark">
                        <tr>
                            <th>ID</th>
                            <th>Data rozpoczęcia</th>
                            <th>Data zakończenia</th>
                            <th>Identyfikator książki</th>
                        </tr>
                    </thead>
                    <tbody>
                        {currentRents.map((rent) => (
                            <tr key={rent.id}>
                                <td>{rent.id}</td>
                                <td>{new Date(rent.beginDate).toLocaleDateString()}</td>
                                <td>{rent.endDate ? new Date(rent.endDate).toLocaleDateString() : 'Brak'}</td>
                                <td>{rent.bookId}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>

            <h3>Zakończone wypożyczenia</h3>
            <div className="table-responsive">
                <table className="table table-striped table-hover table-bordered">
                    <thead className="table-dark">
                        <tr>
                            <th>ID</th>
                            <th>Data rozpoczęcia</th>
                            <th>Data zakończenia</th>
                            <th>Identyfikator książki</th>
                        </tr>
                    </thead>
                    <tbody>
                        {archiveRents.map((rent) => (
                            <tr key={rent.id}>
                                <td>{rent.id}</td>
                                <td>{new Date(rent.beginDate).toLocaleDateString()}</td>
                                <td>{rent.endDate ? new Date(rent.endDate).toLocaleDateString() : 'Brak'}</td>
                                <td>{rent.bookId}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default UserDetails;
