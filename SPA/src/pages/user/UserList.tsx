import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import axios from '../../api/Axios';
import UserGet from '../../model/UserGet';
import { useConfirmation } from '../../context/ConfirmationContext';
import { useUserContext } from '../../context/UserContext';

const UserList: React.FC = () => {
    const [users, setUsers] = useState<UserGet[]>([]);
    const [filter, setFilter] = useState('');
    const { showConfirmation } = useConfirmation();
    const navigate = useNavigate();
    const { user } = useUserContext();

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
                        <th>ID</th>
                        <th>Nazwa użytkownika</th>
                        <th>Rola</th>
                        <th>Status</th>
                        <th>Akcja</th>
                    </tr>
                    </thead>
                    <tbody>
                    {users
                        .filter((userr) => userr.username.toLowerCase().includes(filter.toLowerCase()))
                        .map((userr) => (
                        <tr key={userr.id}>
                            <td>{userr.id}</td>
                            <td>{userr.username}</td>
                            <td>{roleMap[userr.role] || userr.role}</td>
                            <td>{userr.active ? 'Aktywny' : 'Nieaktywny'}</td>
                            <td>
                                {user.role === "ROLE_ADMIN" && (
                                    <>
                                        <button
                                            className="btn btn-sm btn-success me-2"
                                            disabled={userr.active}
                                            onClick={() => handleActivate(userr.id)}
                                        >
                                            Aktywuj
                                        </button>
                                        <button
                                            className="btn btn-sm btn-danger me-2"
                                            disabled={!userr.active}
                                            onClick={() => handleDeactivate(userr.id)}
                                        >
                                            Dezaktywuj
                                        </button>
                                        <button
                                            className="btn btn-sm btn-primary me-2"
                                            onClick={() => navigate(`/users/edit/${userr.id}`)}
                                        >
                                            Modyfikuj
                                        </button>
                                    </>
                                )}
                                {user.role === "ROLE_MANAGER" && userr.role === "ROLE_CLIENT" && (
                                    <button
                                        className="btn btn-sm btn-info"
                                        onClick={() => navigate(`/clients/${userr.id}`)}
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