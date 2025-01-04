import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, ScrollView } from 'react-native';
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

    const renderRentItem = (item: RentDetails) => (
        <View key={item.id} style={styles.rentItem}>
            <Text style={styles.rentText}><Text style={styles.label}>ID:</Text> {item.id}</Text>
            <Text style={styles.rentText}><Text style={styles.label}>Tytuł:</Text> {item.title}</Text>
            <Text style={styles.rentText}><Text style={styles.label}>Data rozpoczęcia:</Text> {new Date(item.beginDate).toLocaleString()}</Text>
            <Text style={styles.rentText}><Text style={styles.label}>Data zakończenia:</Text> {item.endDate ? new Date(item.endDate).toLocaleString() : 'Brak'}</Text>
        </View>
    );

    return (
        <ScrollView contentContainerStyle={styles.container}>
            <Text style={styles.title}>Szczegóły użytkownika</Text>
            <View style={styles.card}>
                <Text style={styles.cardText}><Text style={styles.label}>ID:</Text> {user?.id}</Text>
                <Text style={styles.cardText}><Text style={styles.label}>Nazwa użytkownika:</Text> {user?.username}</Text>
                <Text style={styles.cardText}><Text style={styles.label}>Status:</Text> {user?.active ? 'Aktywny' : 'Nieaktywny'}</Text>
            </View>

            <Text style={styles.sectionTitle}>Aktywne wypożyczenia</Text>
            <ScrollView style={styles.innerScroll}>
                {currentRents.length > 0 ? (
                    currentRents.map((item) => renderRentItem(item))
                ) : (
                    <Text style={styles.emptyText}>Brak aktywnych wypożyczeń</Text>
                )}
            </ScrollView>

            <Text style={styles.sectionTitle}>Zakończone wypożyczenia</Text>
            <ScrollView style={styles.innerScroll}>
                {archiveRents.length > 0 ? (
                    archiveRents.map((item) => renderRentItem(item))
                ) : (
                    <Text style={styles.emptyText}>Brak zakończonych wypożyczeń</Text>
                )}
            </ScrollView>
        </ScrollView>
    );
};

const styles = StyleSheet.create({
    container: {
        padding: 16,
        backgroundColor: '#f8f9fa',
        flexGrow: 1,
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
    innerScroll: {
        marginBottom: 16,
    },
});

export default UserDetails;