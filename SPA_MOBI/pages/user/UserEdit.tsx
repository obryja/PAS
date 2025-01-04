import React, { useState, useEffect } from 'react';
import { View, Text, TextInput, StyleSheet, ScrollView, TouchableOpacity } from 'react-native';
import { useRoute, RouteProp } from '@react-navigation/native';
import { AxiosError } from 'axios';
import * as Yup from 'yup';

import axios from '../../api/Axios';
import { useConfirmation } from '../../context/ConfirmationContext';
import { RootStackParamList } from '../../types';
import { userUpdateSchema } from '../../model/UserUpdateSchema';

type RouteParams = RouteProp<RootStackParamList, 'UserEdit'>;

const UserEdit: React.FC = () => {
    const route = useRoute<RouteParams>();
    const { id } = route.params; 

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

    const handleSubmit = async () => {
        try {
            setErrors({});
            await userUpdateSchema.validate({ username, password }, { abortEarly: false });

            showConfirmation('Czy na pewno chcesz zapisać zmiany?', async () => {
                try {
                    await axios.put(`/users/${id}`, { username, password });
                    setSuccessMessage('Użytkownik został zaktualizowany!');

                    setTimeout(() => {
                        setSuccessMessage(null);
                    }, 5000);
                } catch (err) {
                    if (err instanceof AxiosError && err.response) {
                        const errorMessage = err.response.data || 'Błąd podczas aktualizacji użytkownika. Spróbuj ponownie później.';
                        setErrors({ form: errorMessage });
                    } else {
                        setErrors({ form: 'Błąd podczas aktualizacji użytkownika. Spróbuj ponownie później.' });
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
            } else {
                console.error('Błąd podczas aktualizacji użytkownika:', validationError);
            }
        }
    };

    return (
        <ScrollView contentContainerStyle={styles.container}>
            <Text style={styles.title}>Edytuj użytkownika</Text>

            {errors.form && <Text style={styles.errorMessage}>{errors.form}</Text>}
            {successMessage && <Text style={styles.successMessage}>{successMessage}</Text>}

            <View style={styles.inputGroup}>
                <Text style={styles.label}>ID</Text>
                <TextInput style={[styles.input, styles.nonEditableInput]}  value={id} editable={false} />
            </View>

            <View style={styles.inputGroup}>
                <Text style={styles.label}>Nazwa użytkownika</Text>
                <TextInput
                    style={[styles.input, errors.username && styles.errorInput]}
                    value={username}
                    onChangeText={setUsername}
                    placeholder="Wprowadź nazwę użytkownika"
                />
                {errors.username && <Text style={styles.errorText}>{errors.username}</Text>}
            </View>

            <View style={styles.inputGroup}>
                <Text style={styles.label}>Hasło</Text>
                <View style={styles.passwordGroup}>
                    <TextInput
                        style={[styles.input, styles.passwordInput, errors.password && styles.errorInput]}
                        value={password}
                        onChangeText={setPassword}
                        secureTextEntry={!passwordVisible}
                        placeholder="Wprowadź nowe hasło"
                    />
                    <TouchableOpacity onPress={() => setPasswordVisible(!passwordVisible)}>
                        <Text style={styles.togglePassword}>{passwordVisible ? 'Ukryj' : 'Pokaż'}</Text>
                    </TouchableOpacity>
                </View>
                {errors.password && <Text style={styles.errorText}>{errors.password}</Text>}
            </View>

            <View style={styles.inputGroup}>
                <Text style={styles.label}>Rola</Text>
                <TextInput style={[styles.input, styles.nonEditableInput]}  value={roleMap[role]} editable={false} />
            </View>

            <Text style={[styles.status, active ? styles.activeStatus : styles.inactiveStatus]}>
                {active ? 'Aktywny' : 'Nieaktywny'}
            </Text>

            <TouchableOpacity style={styles.submitButton} onPress={handleSubmit}>
                <Text style={styles.submitButtonText}>Zapisz zmiany</Text>
            </TouchableOpacity>
        </ScrollView>
    );
};

const styles = StyleSheet.create({
    container: {
        padding: 16,
        backgroundColor: '#f8f9fa',
    },
    title: {
        fontSize: 24,
        fontWeight: 'bold',
        marginBottom: 16,
        textAlign: 'center',
    },
    inputGroup: {
        marginBottom: 16,
    },
    label: {
        fontSize: 16,
        marginBottom: 8,
    },
    input: {
        borderWidth: 1,
        borderColor: '#ced4da',
        borderRadius: 5,
        padding: 10,
        backgroundColor: '#fff',
    },
    errorInput: {
        borderColor: '#dc3545',
    },
    errorText: {
        color: '#dc3545',
        marginTop: 4,
        fontSize: 12,
    },
    passwordGroup: {
        flexDirection: 'row',
        alignItems: 'center',
    },
    passwordInput: {
        flex: 1,
    },
    togglePassword: {
        marginLeft: 8,
        color: '#007bff',
        fontSize: 14,
    },
    status: {
        textAlign: 'center',
        fontSize: 16,
        fontWeight: 'bold',
        padding: 8,
        borderRadius: 5,
    },
    activeStatus: {
        backgroundColor: '#28a745',
        color: '#fff',
    },
    inactiveStatus: {
        backgroundColor: '#dc3545',
        color: '#fff',
    },
    submitButton: {
        backgroundColor: '#007bff',
        padding: 12,
        borderRadius: 5,
        alignItems: 'center',
        marginTop: 20,
    },
    submitButtonText: {
        color: '#fff',
        fontSize: 16,
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
    nonEditableInput: {
        backgroundColor: '#e9ecef',
        color: '#6c757d',
        borderColor: '#ced4da',
    },
});

export default UserEdit;