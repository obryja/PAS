import React, { useEffect, useState } from 'react';
import { View, Text, TextInput, StyleSheet, FlatList, TouchableOpacity } from 'react-native';
import { useNavigation } from '@react-navigation/native';
import { StackNavigationProp } from '@react-navigation/stack';

import axios from '../../api/Axios';
import UserGet from '../../model/UserGet';
import { useConfirmation } from '../../context/ConfirmationContext';
import { RootStackParamList } from '../../types';

type NavigationProp = StackNavigationProp<RootStackParamList, 'UserList'>;

const UserList: React.FC = () => {
    const [users, setUsers] = useState<UserGet[]>([]);
    const [filter, setFilter] = useState('');
    const { showConfirmation } = useConfirmation();
    const navigation = useNavigation<NavigationProp>();

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

    const renderItem = ({ item }: { item: UserGet }) => (
        <View style={styles.userRow}>
            <Text style={styles.userText}>
                <Text style={styles.label}>ID:</Text> {item.id}
            </Text>
            <Text style={styles.userText}>
                <Text style={styles.label}>Nazwa użytkownika:</Text> {item.username}
            </Text>
            <Text style={styles.userText}>
                <Text style={styles.label}>Rola:</Text> {roleMap[item.role] || item.role}
            </Text>
            <Text style={styles.userText}>
                <Text style={styles.label}>Status:</Text> {item.active ? 'Aktywny' : 'Nieaktywny'}
            </Text>
            <View style={styles.actionButtons}>
                <TouchableOpacity
                    style={[styles.button, styles.activateButton, item.active && styles.disabledButton]}
                    disabled={item.active}
                    onPress={() => handleActivate(item.id)}>
                    <Text style={styles.buttonText}>Aktywuj</Text>
                </TouchableOpacity>
                <TouchableOpacity
                    style={[styles.button, styles.deactivateButton, !item.active && styles.disabledButton]}
                    disabled={!item.active}
                    onPress={() => handleDeactivate(item.id)}>
                    <Text style={styles.buttonText}>Dezaktywuj</Text>
                </TouchableOpacity>
                <TouchableOpacity
                    style={[styles.button, styles.editButton]}
                    onPress={() => navigation.navigate('UserEdit', { id: item.id })}>
                    <Text style={styles.buttonText}>Modyfikuj</Text>
                </TouchableOpacity>
                {item.role === "ROLE_CLIENT" && (
                    <TouchableOpacity
                        style={[styles.button, styles.detailsButton]}
                        onPress={() => navigation.navigate('ClientDetails', { id: item.id })}>
                        <Text style={styles.buttonText}>Szczegóły</Text>
                    </TouchableOpacity>
                )}
            </View>
        </View>
    );

    return (
        <View style={styles.container}>
            <Text style={styles.title}>Lista użytkowników</Text>
            <TextInput
                style={styles.input}
                placeholder="Filtruj po nazwie użytkownika"
                value={filter}
                onChangeText={setFilter}
            />
            <FlatList
                data={users.filter((user) =>
                    user.username.toLowerCase().includes(filter.toLowerCase())
                )}
                keyExtractor={(item) => item.id.toString()}
                renderItem={renderItem}
            />
        </View>
    );
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
        padding: 16,
        backgroundColor: '#f8f9fa',
    },
    title: {
        fontSize: 24,
        fontWeight: 'bold',
        marginBottom: 16,
        textAlign: 'center',
    },
    input: {
        height: 40,
        borderColor: '#ced4da',
        borderWidth: 1,
        borderRadius: 5,
        paddingHorizontal: 10,
        marginBottom: 16,
        backgroundColor: '#fff',
    },
    userRow: {
        padding: 16,
        marginBottom: 8,
        backgroundColor: '#fff',
        borderRadius: 5,
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 1 },
        shadowOpacity: 0.3,
        shadowRadius: 2,
        elevation: 3,
    },
    userText: {
        fontSize: 16,
        marginBottom: 8,
        lineHeight: 24,
    },
    label: {
        fontWeight: 'bold',
    },
    actionButtons: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        marginTop: 8,
    },
    button: {
        padding: 8,
        borderRadius: 5,
    },
    activateButton: {
        backgroundColor: '#28a745',
    },
    deactivateButton: {
        backgroundColor: '#dc3545',
    },
    editButton: {
        backgroundColor: '#007bff',
    },
    detailsButton: {
        backgroundColor: '#17a2b8',
    },
    buttonText: {
        color: '#fff',
        fontSize: 14,
        textAlign: 'center',
    },
    disabledButton: {
        opacity: 0.5,
    },
});

export default UserList;