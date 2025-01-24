import React, { useEffect, useState } from 'react';
import { AxiosError } from 'axios';
import * as Yup from 'yup';

import axios from '../../api/Axios';
import { useConfirmation } from '../../context/ConfirmationContext';
import UserGet from '../../model/UserGet';
import Book from '../../model/Book';
import { rentAddSchema } from '../../model/RentAddSchema';
import { useUserContext } from '../../context/UserContext';

const RentForm: React.FC = () => {
    const { user } = useUserContext();
    const [users, setUsers] = useState<UserGet[]>([]);
    const [books, setBooks] = useState<Book[]>([]);
    const [selectedUser, setSelectedUser] = useState<string>('');
    const [selectedBook, setSelectedBook] = useState<string>('');
    const [beginDate, setBeginDate] = useState<string>(new Date().toISOString().slice(0, 16));
    const [errors, setErrors] = useState<{ [key: string]: string }>({});
    const [successMessage, setSuccessMessage] = useState<string | null>(null);
    const { showConfirmation } = useConfirmation();

    useEffect(() => {
        if (user.role === 'ROLE_CLIENT') {
            axios.get('/users/me').then((response) => {
                setSelectedUser(response.data.id);
            });
        } else {
            axios.get('/users/active/client').then((response) => setUsers(response.data));
        }

        axios.get('/books/available').then((response) => setBooks(response.data));
    }, [user.role]);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        try {
            setErrors({});
            await rentAddSchema.validate({ selectedUser, selectedBook, beginDate }, { abortEarly: false });

            showConfirmation(
                `Czy na pewno chcesz utworzyć wypożyczenie?`,
                async () => {
                    try {
                        await axios.post('/rents', {
                            userId: selectedUser,
                            bookId: selectedBook,
                            beginDate,
                        });

                        const response = await axios.get('/books/available');
                        setBooks(response.data);
                        
                        setSuccessMessage('Wypożyczenie zostało utworzone!');
                        setTimeout(() => {
                            setSuccessMessage(null);
                        }, 5000);

                        setSelectedUser('');
                        setSelectedBook('');
                        setBeginDate(new Date().toISOString().slice(0, 16));
                    } catch (err) {
                         if (err instanceof AxiosError && err.response) {
                            const errorMessage = err.response.data || 'Nie udało się utworzyć wypożyczenia';
                            setErrors({ form: errorMessage });
                        } else {
                            setErrors({ form: 'Nie udało się utworzyć wypożyczenia' });
                        }
                    }
                }
            );
        } catch(err) {
            const fieldErrors: { [key: string]: string } = {};

            if (err instanceof Yup.ValidationError) {
                err.inner.forEach((error) => {
                    if (error.path) {
                        fieldErrors[error.path] = error.message;
                    }
                });
            }

            setErrors(fieldErrors);
        }
    };

    return (
        <div className="container mt-5">
            <div className="row justify-content-center">
                <div className="col-md-6 col-lg-4">
                    <form onSubmit={handleSubmit} className="p-4 border rounded shadow-sm mt-4 mb-4">
                        <h3 className="text-center mb-4">Utwórz wypożyczenie</h3>

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

                        {user.role === 'ROLE_MANAGER' && (
                            <div className="mb-4">
                                <select
                                    id="user"
                                    className={`form-select ${errors.selectedUser ? 'is-invalid' : ''}`}
                                    value={selectedUser}
                                    onChange={(e) => setSelectedUser(e.target.value)}
                                >
                                    <option value="" disabled>Wybierz użytkownika</option>
                                    {users.map((user) => (
                                        <option key={user.id} value={user.id}>
                                            {user.username}
                                        </option>
                                    ))}
                                </select>
                                {errors.selectedUser && <div className="invalid-feedback">{errors.selectedUser}</div>}
                            </div>
                        )}

                        <div className="mb-4">
                            <select
                                id="book"
                                className={`form-select ${errors.selectedBook ? 'is-invalid' : ''}`}
                                value={selectedBook}
                                onChange={(e) => setSelectedBook(e.target.value)}
                            >
                                <option value="" disabled>Wybierz książkę</option>
                                {books.map((book) => (
                                    <option key={book.id} value={book.id}>
                                        {book.title}
                                    </option>
                                ))}
                            </select>
                            {errors.selectedBook && <div className="invalid-feedback">{errors.selectedBook}</div>}
                        </div>

                        <div className="mb-4">
                            <input
                                type="datetime-local"
                                className={`form-control ${errors.beginDate ? 'is-invalid' : ''}`}
                                value={beginDate}
                                onChange={(e) => setBeginDate(e.target.value)}
                                required
                            />
                            {errors.beginDate && <div className="invalid-feedback">{errors.beginDate}</div>}
                        </div>

                        <button type="submit" className="btn btn-success w-100">
                            Utwórz wypożyczenie
                        </button>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default RentForm;