import React, { useState } from 'react';
import { View, Text, TextInput, StyleSheet, TouchableOpacity, ScrollView } from 'react-native';
import { Picker } from '@react-native-picker/picker';
import { AxiosError } from 'axios';
import * as Yup from 'yup';

import axios from '../../api/Axios';
import { useConfirmation } from '../../context/ConfirmationContext';
import { userAddSchema } from '../../model/UserAddSchema';

const UserAdd: React.FC = () => {
    const [username, setUsername] = useState<string>('');
    const [password, setPassword] = useState<string>('');
    const [userRole, setUserRole] = useState<string>('client');
    const [passwordVisible, setPasswordVisible] = useState<boolean>(false);
    const [errors, setErrors] = useState<{ [key: string]: string | null }>({});
    const [successMessage, setSuccessMessage] = useState<string | null>(null);
    const { showConfirmation } = useConfirmation();

    const userRoleMap: { [key: string]: string } = {
        client: 'Klient',
        manager: 'Manager',
        admin: 'Administrator',
    };

    const handleSubmit = async () => {
        try {
            setErrors({});
            await userAddSchema.validate({ username, password, userRole }, { abortEarly: false });
            const endpoint = `/users/${userRole}`;

            showConfirmation('Czy jesteś pewny, że chcesz utworzyć tego użytkownika?', async () => {
                try {
                    await axios.post(endpoint, { username, password });
                    
                    setUsername('');
                    setPassword('');
                    setUserRole('client');
                    setSuccessMessage(`${userRoleMap[userRole]} został dodany!`);
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
        <ScrollView contentContainerStyle={styles.container}>
            <Text style={styles.title}>Utwórz użytkownika</Text>

            {errors.form && <Text style={styles.errorMessage}>{errors.form}</Text>}
            {successMessage && <Text style={styles.successMessage}>{successMessage}</Text>}

            <View style={styles.inputGroup}>
                <TextInput
                    style={[styles.input, errors.username && styles.errorInput]}
                    value={username}
                    onChangeText={setUsername}
                    placeholder="Nazwa użytkownika"
                />
                {errors.username && <Text style={styles.errorText}>{errors.username}</Text>}
            </View>

            <View style={styles.inputGroup}>
                <View style={styles.passwordGroup}>
                    <TextInput
                        style={[styles.input, styles.passwordInput, errors.password && styles.errorInput]}
                        value={password}
                        onChangeText={setPassword}
                        secureTextEntry={!passwordVisible}
                        placeholder="Hasło"
                    />
                    <TouchableOpacity onPress={() => setPasswordVisible(!passwordVisible)}>
                        <Text style={styles.togglePassword}>{passwordVisible ? 'Ukryj' : 'Pokaż'}</Text>
                    </TouchableOpacity>
                </View>
                {errors.password && <Text style={styles.errorText}>{errors.password}</Text>}
            </View>

            <View style={styles.inputGroup}>
                <Picker
                    selectedValue={userRole}
                    onValueChange={(value) => setUserRole(value)}
                    style={[styles.input, errors.userRole ? { borderColor: 'red' } : {}]}
                >
                    <Picker.Item label="Klient" value="client" />
                    <Picker.Item label="Manager" value="manager" />
                    <Picker.Item label="Administrator" value="admin" />
                </Picker>
                {errors.userRole && <Text style={styles.errorText}>{errors.userRole}</Text>}
            </View>

            <TouchableOpacity style={styles.submitButton} onPress={handleSubmit}>
                <Text style={styles.submitButtonText}>Utwórz użytkownika</Text>
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
    submitButton: {
        backgroundColor: '#007bff',
        padding: 12,
        borderRadius: 5,
        alignItems: 'center',
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
});

export default UserAdd;