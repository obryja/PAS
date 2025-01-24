import React, { useState } from 'react';
import { AxiosError } from 'axios';
import * as Yup from 'yup';

import axios from '../../api/Axios';
import { useConfirmation } from '../../context/ConfirmationContext';
import { ResetPasswordSchema } from '../../model/ResetPasswordSchema';

const ResetPassword: React.FC = () => {
    const [password, setPassword] = useState<string>('');
    const [passwordVisible, setPasswordVisible] = useState<boolean>(false);
    const [errors, setErrors] = useState<{ [key: string]: string | null }>({});
    const [successMessage, setSuccessMessage] = useState<string | null>(null);
    const { showConfirmation } = useConfirmation();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        try {
            setErrors({});
            await ResetPasswordSchema.validate({ password }, { abortEarly: false });

            showConfirmation('Czy na pewno chcesz zapisać zmiany?', async () => {
                    try {
                        await axios.post(`/users/password`, { password });
                        setSuccessMessage('Hasło zostało zmienione!');

                        setTimeout(() => {
                            setSuccessMessage(null);
                        }, 5000);
                    } catch (err) {
                        if (err instanceof AxiosError && err.response) {
                            const errorMessage = err.response.data || 'Błąd podczas zmiany hasła. Spróbuj ponownie później.';
                            setErrors({ form: errorMessage });
                        } else {
                            setErrors({ form: 'Błąd podczas zmiany hasła. Spróbuj ponownie później.' });
                        }
                    }
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
                console.error('Błąd podczas zmiany hasła:', validationError);
            }
        }
    };

    return (
        <div className="container mt-5">
            <div className="row justify-content-center">
                <div className="col-md-6 col-lg-4">
                    <form onSubmit={handleSubmit} className="p-4 border rounded shadow-sm mt-4 mb-4">
                        <h3 className="text-center mb-4">Zmień hasło</h3>

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

                        <div className="mb-4 input-group">
                            <label htmlFor="password" className="form-label w-100">Hasło</label>
                            <input
                                type={passwordVisible ? 'text' : 'password'}
                                className={`form-control ${errors.password ? 'is-invalid' : ''}`}
                                id="password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
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

                        <button type="submit" className="btn btn-primary w-100">
                            Zmień hasło
                        </button>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default ResetPassword;