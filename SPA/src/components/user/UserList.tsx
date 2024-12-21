import React, { useEffect, useState } from 'react';
import axios from '../../api/Axios';
import { UserGet } from '../../model/UserGet';

const UserList: React.FC = () => {
    const [users, setUsers] = useState<UserGet[]>([]);
    const [filter, setFilter] = useState('');

    useEffect(() => {
        axios.get('/users').then((response) => setUsers(response.data));
    }, []);

    const roleMap: { [key: string]: string } = {
        ROLE_ADMIN: "Administrator",
        ROLE_MANAGER: "Manager",
        ROLE_CLIENT: "Klient",
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
                                >
                                    Aktywuj
                                </button>
                                <button
                                    className="btn btn-sm btn-danger"
                                    disabled={!user.active}
                                >
                                    Dezaktywuj
                                </button>
                                {user.role === "ROLE_CLIENT" && (
                                    <button
                                        className="btn btn-sm btn-info"
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
