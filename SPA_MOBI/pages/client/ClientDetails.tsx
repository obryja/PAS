import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, FlatList, ActivityIndicator } from 'react-native';
import { useRoute } from '@react-navigation/native';

import axios from '../../api/Axios';
import UserGet from '../../model/UserGet';
import RentDetails from '../../model/RentDetails';

const UserDetails: React.FC = () => {
    const route = useRoute();
    const { id } = route.params as { id: string };

    const [user, setUser] = useState<UserGet | null>(null);
    const [error, setError] = useState<string | null>(null);
    const [currentRents, setCurrentRents] = useState<RentDetails[]>([]);
    const [archiveRents, setArchiveRents] = useState<RentDetails[]>([]);

    useEffect(() => {
        const fetchUser = async () => {
            try {
                const response = await axios.get(`/users/${id}`);
                setUser(response.data);
            } catch (err) {
                console.error('Błąd podczas pobierania danych użytkownika:', err);
                setError('Nie udało się załadować danych użytkownika.');
            }
        };

        const fetchRents = async () => {
            try {
                const currentRentsResponse = await axios.get(`/rents/user/current/${id}/details`);
                setCurrentRents(currentRentsResponse.data);

                const archiveRentsResponse = await axios.get(`/rents/user/archive/${id}/details`);
                setArchiveRents(archiveRentsResponse.data);
            } catch (err) {
                console.error('Błąd podczas pobierania wypożyczeń:', err);
                setError('Nie udało się załadować wypożyczeń.');
            }
        };

        fetchUser();
        fetchRents();
    }, [id]);

    if (error) {
        return (
            <View style={styles.centered}>
                <Text style={styles.errorText}>{error}</Text>
            </View>
        );
    }

    const renderRentItem = ({ item }: { item: RentDetails }) => (
        <View style={styles.rentItem}>
            <Text style={styles.rentText}><Text style={styles.label}>ID:</Text> {item.id}</Text>
            <Text style={styles.rentText}><Text style={styles.label}>Tytuł:</Text> {item.title}</Text>
            <Text style={styles.rentText}><Text style={styles.label}>Data rozpoczęcia:</Text>: {new Date(item.beginDate).toLocaleString()}</Text>
            <Text style={styles.rentText}><Text style={styles.label}>Data zakończenia:</Text> {item.endDate ? new Date(item.endDate).toLocaleString() : 'Brak'}</Text>
        </View>
    );

    return (
        <View style={styles.container}>
            <Text style={styles.title}>Szczegóły użytkownika</Text>
            <View style={styles.card}>
                <Text style={styles.cardText}><Text style={styles.label}>ID:</Text> {user?.id}</Text>
                <Text style={styles.cardText}><Text style={styles.label}>Nazwa użytkownika:</Text> {user?.username}</Text>
                <Text style={styles.cardText}><Text style={styles.label}>Status:</Text> {user?.active ? 'Aktywny' : 'Nieaktywny'}</Text>
            </View>

            <Text style={styles.sectionTitle}>Aktywne wypożyczenia</Text>
            <FlatList
                data={currentRents}
                renderItem={renderRentItem}
                keyExtractor={(item) => item.id}
                ListEmptyComponent={<Text style={styles.emptyText}>Brak aktywnych wypożyczeń</Text>}
            />

            <Text style={styles.sectionTitle}>Zakończone wypożyczenia</Text>
            <FlatList
                data={archiveRents}
                renderItem={renderRentItem}
                keyExtractor={(item) => item.id}
                ListEmptyComponent={<Text style={styles.emptyText}>Brak zakończonych wypożyczeń</Text>}
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
    centered: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
    },
    title: {
        fontSize: 24,
        fontWeight: 'bold',
        marginBottom: 16,
        textAlign: 'center',
    },
    card: {
        padding: 16,
        backgroundColor: '#fff',
        borderRadius: 8,
        marginBottom: 16,
        elevation: 3,
    },
    cardText: {
        fontSize: 16,
        marginBottom: 8,
    },
    sectionTitle: {
        fontSize: 20,
        fontWeight: 'bold',
        marginBottom: 8,
    },
    rentItem: {
        padding: 16,
        backgroundColor: '#fff',
        borderRadius: 8,
        marginBottom: 8,
        elevation: 2,
    },
    rentText: {
        fontSize: 16,
        marginBottom: 4,
    },
    label: {
        fontWeight: 'bold',
    },
    emptyText: {
        fontSize: 16,
        textAlign: 'center',
        marginTop: 16,
    },
    errorText: {
        fontSize: 16,
        color: '#dc3545',
        textAlign: 'center',
    },
});

export default UserDetails;