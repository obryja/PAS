import React, { useEffect, useState } from 'react';
import { View, Text, TextInput, Button, StyleSheet } from 'react-native';
import { Picker } from '@react-native-picker/picker';
import { AxiosError } from 'axios';
import * as Yup from 'yup';

import axios from '../../api/Axios';
import UserGet from '../../model/UserGet';
import Book from '../../model/Book';
import { rentAddSchema } from '../../model/RentAddSchema';
import { useConfirmation } from '../../context/ConfirmationContext';

const RentForm: React.FC = () => {
    const [users, setUsers] = useState<UserGet[]>([]);
    const [books, setBooks] = useState<Book[]>([]);
    const [selectedUser, setSelectedUser] = useState<string>('');
    const [selectedBook, setSelectedBook] = useState<string>('');
    const [beginDate, setBeginDate] = useState<string>(new Date().toISOString().replace('T', ' ').slice(0, 16));
    const [errors, setErrors] = useState<{ [key: string]: string }>({});
    const [successMessage, setSuccessMessage] = useState<string | null>(null);
    const { showConfirmation } = useConfirmation();

    useEffect(() => {
        axios.get('/users/active/client').then((response) => setUsers(response.data));
        axios.get('/books/available').then((response) => setBooks(response.data));
    }, []);

    const handleSubmit = async () => {
        try {
            setErrors({});

            const formattedBeginDate = beginDate.replace(' ', 'T');
            await rentAddSchema.validate({ selectedUser, selectedBook, beginDate }, { abortEarly: false });

            showConfirmation(
                `Czy na pewno chcesz utworzyć wypożyczenie?`,
                async () => {
                    try {
                        await axios.post('/rents', {
                            userId: selectedUser,
                            bookId: selectedBook,
                            beginDate: formattedBeginDate,
                        });

                        const response = await axios.get('/books/available');
                        setBooks(response.data);

                        setSuccessMessage('Wypożyczenie zostało utworzone!');
                        setTimeout(() => {
                            setSuccessMessage(null);
                        }, 5000);

                        setSelectedUser('');
                        setSelectedBook('');
                        setBeginDate(new Date().toISOString().replace('T', ' ').slice(0, 16));
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
        <View style={styles.container}>
            <Text style={styles.title}>Utwórz wypożyczenie</Text>

            {errors.form && <Text style={styles.errorMessage}>{errors.form}</Text>}
            {successMessage && <Text style={styles.successMessage}>{successMessage}</Text>}

            <View style={styles.inputContainer}>
                <Text style={styles.label}>Data rozpoczęcia:</Text>
                <TextInput
                    style={styles.input}
                    value={beginDate}
                    onChangeText={setBeginDate}
                    placeholder="YYYY-MM-DD HH:mm"
                />
                {errors.beginDate && <Text style={styles.error}>{errors.beginDate}</Text>}
            </View>

            <View style={styles.inputContainer}>
                <Text style={styles.label}>Użytkownik:</Text>
                <Picker
                    selectedValue={selectedUser}
                    onValueChange={(itemValue) => setSelectedUser(itemValue)}
                    style={styles.picker}
                >
                    <Picker.Item label="Wybierz użytkownika" value="" />
                    {users.map((user) => (
                        <Picker.Item key={user.id} label={user.username} value={user.id} />
                    ))}
                </Picker>
                {errors.selectedUser && <Text style={styles.error}>{errors.selectedUser}</Text>}
            </View>

            <View style={styles.inputContainer}>
                <Text style={styles.label}>Książka:</Text>
                <Picker
                    selectedValue={selectedBook}
                    onValueChange={(itemValue) => setSelectedBook(itemValue)}
                    style={styles.picker}
                >
                    <Picker.Item label="Wybierz książkę" value="" />
                    {books.map((book) => (
                        <Picker.Item key={book.id} label={book.title} value={book.id} />
                    ))}
                </Picker>
                {errors.selectedBook && <Text style={styles.error}>{errors.selectedBook}</Text>}
            </View>

            <Button title="Utwórz wypożyczenie" onPress={handleSubmit} color="#28a745" />
        </View>
    );
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
        padding: 20,
        backgroundColor: '#f8f9fa',
    },
    title: {
        fontSize: 24,
        fontWeight: 'bold',
        marginBottom: 20,
        textAlign: 'center',
    },
    inputContainer: {
        marginBottom: 15,
    },
    label: {
        fontSize: 16,
        marginBottom: 5,
    },
    picker: {
        borderWidth: 1,
        borderColor: '#ced4da',
        borderRadius: 5,
        padding: 10,
        backgroundColor: '#fff',
    },
    input: {
        height: 50,
        borderColor: '#ccc',
        borderWidth: 1,
        borderRadius: 5,
        paddingHorizontal: 10,
        backgroundColor: '#fff',
    },
    error: {
        color: '#dc3545',
        fontSize: 14,
        marginTop: 5,
    },
    successMessage: {
        backgroundColor: '#d4edda',
        color: '#155724',
        borderColor: '#c3e6cb',
        textAlign: 'center',
        marginBottom: 16,
        padding: 10,
        borderRadius: 5,
    },
    errorMessage: {
        backgroundColor: '#f1aeb5',
        color: '#58151c',
        borderColor: '#58151c',
        textAlign: 'center',
        marginBottom: 16,
        padding: 10,
        borderRadius: 5,
    },
});

export default RentForm;