import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import axios from '../../api/Axios';
import UserGet from '../../model/UserGet';
import RentDetails from '../../model/RentDetails';

const UserDetails: React.FC = () => {
    const { id } = useParams<{ id: string }>();
    const [user, setUser] = useState<UserGet | null>(null);
    const [error, setError] = useState<string | null>(null);
    const [currentRents, setCurrentRents] = useState<RentDetails[]>([]);
    const [archiveRents, setArchiveRents] = useState<RentDetails[]>([]);

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
                const currentRentsResponse = await axios.get(`/rents/user/current/${id}/details`);
                setCurrentRents(currentRentsResponse.data);

                const archiveRentsResponse = await axios.get(`/rents/user/archive/${id}/details`);
                setArchiveRents(archiveRentsResponse.data);
            } catch (err) {
                console.error('Błąd podczas pobierania wypożyczeń:', err);
                setError('Nie udało się załadować wypożyczeń.');
            }
        };

        fetchUser();
        fetchRents();
    }, [id]);

    if (!user) {
        return;
    }

    return (
        <div className="container my-4">
            <h2>Szczegóły użytkownika</h2>
            <div className="row">
                <div className="col-md-4 mb-4">
                    <div className="card">
                        <div className="card-body text-center">
                            <h5 className="card-title">ID</h5>
                            <p className="card-text">{user.id}</p>
                        </div>
                    </div>
                </div>
                <div className="col-md-4 mb-4">
                    <div className="card">
                        <div className="card-body text-center">
                            <h5 className="card-title">Nazwa użytkownika</h5>
                            <p className="card-text">{user.username}</p>
                        </div>
                    </div>
                </div>
                <div className="col-md-4 mb-4">
                    <div className="card">
                        <div className="card-body text-center">
                            <h5 className="card-title">Status</h5>
                            <p className="card-text">{user.active ? 'Aktywny' : 'Nieaktywny'}</p>
                        </div>
                    </div>
                </div>
            </div>

            <h3>Aktywne wypożyczenia</h3>
            <div className="table-responsive">
                <table className="table table-striped table-hover table-bordered">
                    <thead className="table-dark">
                        <tr>
                            <th className="col-3">ID</th>
                            <th className="col-3">Data rozpoczęcia</th>
                            <th className="col-3">Data zakończenia</th>
                            <th className="col-3">Książka</th>
                        </tr>
                    </thead>
                    <tbody>
                        {currentRents.map((rent) => (
                            <tr key={rent.id}>
                                <td>{rent.id}</td>
                                <td>{new Date(rent.beginDate).toLocaleString()}</td>
                                <td>{rent.endDate ? new Date(rent.endDate).toLocaleString() : 'Brak'}</td>
                                <td>{rent.title}</td>
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
                            <th className="col-3">ID</th>
                            <th className="col-3">Data rozpoczęcia</th>
                            <th className="col-3">Data zakończenia</th>
                            <th className="col-3">Książka</th>
                        </tr>
                    </thead>
                    <tbody>
                        {archiveRents.map((rent) => (
                            <tr key={rent.id}>
                                <td>{rent.id}</td>
                                <td>{new Date(rent.beginDate).toLocaleString()}</td>
                                <td>{rent.endDate ? new Date(rent.endDate).toLocaleString() : 'Brak'}</td>
                                <td>{rent.title}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default UserDetails;