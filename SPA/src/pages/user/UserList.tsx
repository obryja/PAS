import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import axios from '../../api/Axios';
import UserGet from '../../model/UserGet';
import { useConfirmation } from '../../context/ConfirmationContext';

const UserList: React.FC = () => {
    const [users, setUsers] = useState<UserGet[]>([]);
    const [filter, setFilter] = useState('');
    const { showConfirmation } = useConfirmation();
    const navigate = useNavigate();

    useEffect(() => {
        axios.get('/users').then((response) => setUsers(response.data));
    }, []);

    const roleMap: { [key: string]: string } = {
        ROLE_ADMIN: "Administrator",
        ROLE_MANAGER: "Manager",
        ROLE_CLIENT: "Klient",
    };

    const handleActivate = async (userId: string) => {
        showConfirmation(
            'Czy na pewno chcesz aktywować tego użytkownika?',
            async () => {
                try {
                    await axios.post(`/users/${userId}/activate`);

                    setUsers((prevUsers) =>
                        prevUsers.map((user) =>
                            user.id === userId ? { ...user, active: true } : user
                        )
                    );
                } catch (error) {
                    console.error(error);
                }
            }
        );
    };

    const handleDeactivate = async (userId: string) => {
        showConfirmation(
            'Czy na pewno chcesz dezaktywować tego użytkownika?',
            async () => {
                try {
                    await axios.post(`/users/${userId}/deactivate`);

                    setUsers((prevUsers) =>
                        prevUsers.map((user) =>
                            user.id === userId ? { ...user, active: false } : user
                        )
                    );
                } catch (error) {
                    console.error(error);
                }
            }
        );
    };

    return (
        <div className="container my-5">
            <h2>Lista użytkowników</h2>
            
            <input
                type="text"
                className="form-control mb-3"
                placeholder="Filtruj po nazwie użytkownika"
                value={filter}
                onChange={(e) => setFilter(e.target.value)}
            />

            <div className="table-responsive">
                <table className="table table-striped table-hover table-bordered">
                    <thead className='table-dark text-center'>
                    <tr>
                        <th>Nazwa użytkownika</th>
                        <th>Rola</th>
                        <th>Status</th>
                        <th>Akcja</th>
                    </tr>
                    </thead>
                    <tbody>
                    {users
                        .filter((user) => user.username.toLowerCase().includes(filter.toLowerCase()))
                        .map((user) => (
                        <tr key={user.id}>
                            <td>{user.username}</td>
                            <td>{roleMap[user.role] || user.role}</td>
                            <td>{user.active ? 'Aktywny' : 'Nieaktywny'}</td>
                            <td>
                                <button
                                    className="btn btn-sm btn-success"
                                    disabled={user.active}
                                    onClick={() => handleActivate(user.id)}
                                >
                                    Aktywuj
                                </button>
                                <button
                                    className="btn btn-sm btn-danger"
                                    disabled={!user.active}
                                    onClick={() => handleDeactivate(user.id)}
                                >
                                    Dezaktywuj
                                </button>
                                <button
                                    className="btn btn-sm btn-primary"
                                >
                                    Modyfikuj
                                </button>
                                {user.role === "ROLE_CLIENT" && (
                                    <button
                                        className="btn btn-sm btn-info"
                                        onClick={() => navigate(`/clients/${user.id}`)}
                                    >
                                    Szczegóły
                                    </button>
                                )}
                            </td>
                        </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default UserList;
