import React, { useEffect, useState } from 'react';
import { View, Text, FlatList, StyleSheet, TouchableOpacity } from 'react-native';

import axios from '../../api/Axios';
import RentDetails from '../../model/RentDetails';
import { useConfirmation } from '../../context/ConfirmationContext';

const RentList: React.FC = () => {
    const [rents, setRents] = useState<RentDetails[]>([]);
    const { showConfirmation } = useConfirmation();

    useEffect(() => {
        axios.get('/rents/details').then((response) => setRents(response.data));
    }, []);


    const handleEndRent = async (rentId: string) => {
        showConfirmation(
            'Czy na pewno chcesz zakończyć to wypożyczenie?',
            async () => {
                try {
                    const response = await axios.post(`/rents/${rentId}`);
                    const updatedRent = response.data;

                    setRents((prevRents) =>
                        prevRents.map((rent) =>
                            rent.id === rentId ? { ...rent, endDate: updatedRent.endDate } : rent
                        )
                    );
                } catch (error) {
                    console.error(error);
                }
            }
        );
    };

    const renderItem = ({ item }: { item: RentDetails }) => (
        <View style={styles.rentRow}>
            <Text style={styles.rentText}>
                <Text style={styles.label}>ID:</Text> {item.id}
            </Text>
            <Text style={styles.rentText}>
                <Text style={styles.label}>Użytkownik:</Text> {item.username}
            </Text>
            <Text style={styles.rentText}>
                <Text style={styles.label}>Tytuł:</Text> {item.title}
            </Text>
            <Text style={styles.rentText}>
                <Text style={styles.label}>Rozpoczęcie:</Text> {new Date(item.beginDate).toLocaleString()}
            </Text>
            <Text style={styles.rentText}>
                <Text style={styles.label}>Zakończenie:</Text> {item.endDate ? new Date(item.endDate).toLocaleString() : 'Brak'}
            </Text>
            {item.endDate === null && (
                <TouchableOpacity
                    style={[styles.button, styles.endRentButton]}
                    onPress={() => handleEndRent(item.id)}
                >
                    <Text style={styles.buttonText}>Zakończ wypożyczenie</Text>
                </TouchableOpacity>
            )}
        </View>
    );


    return (
        <View style={styles.container}>
            <Text style={styles.title}>Lista wypożyczeń</Text>
            <FlatList
                data={rents}
                keyExtractor={(item) => item.id}
                renderItem={renderItem}
                ListEmptyComponent={<Text style={styles.emptyText}>Brak wypożyczeń do wyświetlenia.</Text>}
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
    rentRow: {
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
    rentText: {
        fontSize: 16,
        marginBottom: 8,
        lineHeight: 24,
    },
    label: {
        fontWeight: 'bold',
    },
    button: {
        padding: 8,
        borderRadius: 5,
        marginTop: 8,
    },
    endRentButton: {
        backgroundColor: '#dc3545',
    },
    buttonText: {
        color: '#fff',
        fontSize: 14,
        textAlign: 'center',
    },
    loadingContainer: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
    },
    emptyText: {
        textAlign: 'center',
        color: '#888',
        marginTop: 20,
    },
});

export default RentList;