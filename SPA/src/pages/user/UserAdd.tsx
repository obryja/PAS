import React, { useState } from 'react';
import { AxiosError } from 'axios';
import * as Yup from 'yup';

import axios from '../../api/Axios';
import { useConfirmation } from '../../context/ConfirmationContext';
import { userAddSchema } from '../../model/UserAddSchema'; 

const UserAdd: React.FC = () => {
    const [username, setUsername] = useState<string>('');
    const [password, setPassword] = useState<string>('');
    const [userType, setUserType] = useState<string>('client');
    const [passwordVisible, setPasswordVisible] = useState<boolean>(false);
    const [errors, setErrors] = useState<{ [key: string]: string | null }>({});
    const [successMessage, setSuccessMessage] = useState<string | null>(null);
    const { showConfirmation } = useConfirmation();

    const userTypeMap: { [key: string]: string } = {
        client: 'Klient',
        manager: 'Menedżer',
        admin: 'Administrator',
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        try {
            setErrors({});
            await userAddSchema.validate({ username, password, userType }, { abortEarly: false });
            const endpoint = `/users/${userType}`;

            showConfirmation(`Czy jesteś pewny że chcesz utworzyć tego użytkownika?`, async () => {
                try {
                    await axios.post(endpoint, { username, password });
                    
                    setUsername('');
                    setPassword('');
                    setUserType('client');
                    setSuccessMessage(`${userTypeMap[userType]} został dodany!`);
                    setTimeout(() => {
                        setSuccessMessage(null);
                    }, 5000);
                } catch (err) {
                    if (err instanceof AxiosError && err.response) {
                        const errorMessage = err.response.data || 'Nie udało się utworzyć użytkownika. Spróbuj ponownie później.';
                        setErrors({ form: errorMessage });
                    } else {
                        setErrors({ form: 'Nie udało się utworzyć użytkownika. Spróbuj ponownie później.' });
                    }
                }
            });
        } catch (validationError) {
            if (validationError instanceof Yup.ValidationError) {
                const fieldErrors: { [key: string]: string } = {};
                validationError.inner.forEach((error) => {
                    if (error.path) {
                        fieldErrors[error.path] = error.message;
                    }
                });
                setErrors(fieldErrors);
            }
        }
    };

    return (
        <div className="container mt-5">
            <div className="row justify-content-center">
                <div className="col-md-6 col-lg-4">
                    <form onSubmit={handleSubmit} className="p-4 border rounded shadow-sm mt-4 mb-4">
                        <h3 className="text-center mb-4">Utwórz użytkownika</h3>
        
                        {errors.form && (
                            <div className="alert alert-danger" role="alert">
                                {errors.form}
                            </div>
                        )}
        
                        {successMessage && (
                            <div className="alert alert-success" role="alert">
                                {successMessage}
                            </div>
                        )}
        
                        <div className="mb-4">
                            <input
                                type="text"
                                className={`form-control ${errors.username ? 'is-invalid' : ''}`}
                                placeholder="Nazwa użytkownika"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                                required
                            />
                            {errors.username && <div className="invalid-feedback">{errors.username}</div>}
                        </div>
        
                        <div className="mb-4 position-relative">
                            <input
                                type={passwordVisible ? 'text' : 'password'}
                                className={`form-control ${errors.password ? 'is-invalid' : ''}`}
                                placeholder="Hasło"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                            />
                            <button
                                type="button"
                                className="btn btn-link position-absolute end-0 top-50 translate-middle-y"
                                onClick={() => setPasswordVisible(!passwordVisible)}
                            >
                                <i className={`bi ${passwordVisible ? 'bi-eye-slash' : 'bi-eye'}`}></i>
                            </button>
                            {errors.password && <div className="invalid-feedback">{errors.password}</div>}
                        </div>
        
                        <div className="mb-4">
                            <select
                                className={`form-select ${errors.userType ? 'is-invalid' : ''}`}
                                value={userType}
                                onChange={(e) => setUserType(e.target.value)}
                                required
                            >
                                <option value="client">Klient</option>
                                <option value="manager">Manager</option>
                                <option value="admin">Administrator</option>
                            </select>
                            {errors.userType && <div className="invalid-feedback">{errors.userType}</div>}
                        </div>
        
                        <button type="submit" className="btn btn-primary w-100">
                            Utwórz użytkownika
                        </button>
                    </form>
                </div>
            </div>
        </div>
    );
};
  
export default UserAdd;
