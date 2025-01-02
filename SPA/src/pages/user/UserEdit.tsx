import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import * as Yup from 'yup';

import axios from '../../api/Axios';
import { userUpdateSchema } from '../../model/UserUpdateSchema';
import { useConfirmation } from '../../context/ConfirmationContext';

const UserEdit: React.FC = () => {
    const { id } = useParams<{ id: string }>();

    const [username, setUsername] = useState<string>('');
    const [password, setPassword] = useState<string>('');
    const [role, setRole] = useState<string>('');
    const [active, setActive] = useState<boolean>(false);
    const [passwordVisible, setPasswordVisible] = useState<boolean>(false);
    const [errors, setErrors] = useState<{ [key: string]: string | null }>({});
    const [successMessage, setSuccessMessage] = useState<string | null>(null);
    const { showConfirmation } = useConfirmation();

    const roleMap: { [key: string]: string } = {
        ROLE_ADMIN: "Administrator",
        ROLE_MANAGER: "Manager",
        ROLE_CLIENT: "Klient",
    };

    useEffect(() => {
        const fetchUser = async () => {
            try {
                const response = await axios.get(`/users/${id}/details`);
                const { username, password, role, active } = response.data;
                setUsername(username);
                setPassword(password);
                setRole(role);
                setActive(active);
            } catch (error) {
                console.error('Błąd podczas pobierania użytkownika:', error);
            }
        };

        fetchUser();
    }, [id]);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        try {
            setErrors({});
            await userUpdateSchema.validate({ username, password }, { abortEarly: false });

            showConfirmation(
                'Czy na pewno chcesz zapisać zmiany?',
                async () => {
                    await axios.put(`/users/${id}`, { username, password });
                    setSuccessMessage('Użytkownik został zaktualizowany!');

                    setTimeout(() => {
                        setSuccessMessage(null);
                    }, 5000);
                }
            );
        } catch (validationError) {
            if (validationError instanceof Yup.ValidationError) {
                const fieldErrors: { [key: string]: string } = {};
                validationError.inner.forEach((error) => {
                    if (error.path) {
                        fieldErrors[error.path] = error.message;
                    }
                });
                setErrors(fieldErrors);
            } else {
                console.error('Błąd podczas aktualizacji użytkownika:', validationError);
            }
        }
    };

    return (
        <div className="container mt-5">
            <div className="row justify-content-center">
                <div className="col-md-6 col-lg-4">
                    <form onSubmit={handleSubmit} className="p-4 border rounded shadow-sm mt-4 mb-4">
                        <h3 className="text-center mb-4">Edytuj użytkownika</h3>

                        {successMessage && (
                            <div className="alert alert-success" role="alert">
                                {successMessage}
                            </div>
                        )}

                        {errors.form && (
                            <div className="alert alert-danger" role="alert">
                                {errors.form}
                            </div>
                        )}

                        <div className="mb-4">
                            <label htmlFor="userId" className="form-label">ID</label>
                            <input
                                type="text"
                                className="form-control"
                                id="userId"
                                value={id}
                                disabled
                            />
                        </div>

                        <div className="mb-4">
                            <label htmlFor="username" className="form-label">Nazwa użytkownika</label>
                            <input
                                type="text"
                                className={`form-control ${errors.username ? 'is-invalid' : ''}`}
                                id="username"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                                required
                            />
                            {errors.username && <div className="invalid-feedback">{errors.username}</div>}
                        </div>

                        <div className="mb-4 input-group">
                            <label htmlFor="password" className="form-label w-100">Nowe hasło (opcjonalnie)</label>
                            <input
                                type={passwordVisible ? 'text' : 'password'}
                                className={`form-control ${errors.password ? 'is-invalid' : ''}`}
                                id="password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                            />
                            <button
                                type="button"
                                className="btn btn-link input-group-text border"
                                onClick={() => setPasswordVisible(!passwordVisible)}
                            >
                                <i className={`bi ${passwordVisible ? 'bi-eye-slash' : 'bi-eye'}`}></i>
                            </button>
                            {errors.password && <div className="invalid-feedback">{errors.password}</div>}
                        </div>

                        <div className="mb-4">
                            <label htmlFor="role" className="form-label">Rola</label>
                            <input
                                type="text"
                                className="form-control"
                                id="role"
                                value={roleMap[role]}
                                disabled
                            />
                        </div>

                        <div className={`badge ${active ? 'bg-success' : 'bg-danger'} mt-2 w-100 d-flex justify-content-center align-items-center fs-6 mb-3`}>
                            {active ? 'Aktywny' : 'Nieaktywny'}
                        </div>

                        <button type="submit" className="btn btn-primary w-100">
                            Zapisz zmiany
                        </button>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default UserEdit;