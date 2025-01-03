import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';
import { useNavigation } from '@react-navigation/native';
import { StackNavigationProp } from '@react-navigation/stack';

import { RootStackParamList } from '../types';

type NavigationProp = StackNavigationProp<RootStackParamList, 'Home'>;

const HomePage: React.FC = () => {
    const navigation = useNavigation<NavigationProp>();

    return (
        <View style={styles.container}>
            <Text style={styles.title}>Biblioteka</Text>
            <View style={styles.optionsContainer}>
                <Text style={styles.subtitle}>Wybierz opcję:</Text>
                <View style={styles.buttonContainer}>
                    <TouchableOpacity 
                        style={styles.button} 
                        onPress={() => navigation.navigate('UserList')}
                    >
                        <Text style={styles.buttonText}>Lista użytkowników</Text>
                    </TouchableOpacity>
                    <TouchableOpacity 
                        style={styles.button} 
                        onPress={() => navigation.navigate('UserAdd')}
                    >
                        <Text style={styles.buttonText}>Dodaj użytkownika</Text>
                    </TouchableOpacity>
                    <TouchableOpacity 
                        style={styles.button} 
                        onPress={() => navigation.navigate('RentList')}
                    >
                        <Text style={styles.buttonText}>Lista wypożyczeń</Text>
                    </TouchableOpacity>
                    <TouchableOpacity 
                        style={styles.button} 
                        onPress={() => navigation.navigate('RentAdd')}
                    >
                        <Text style={styles.buttonText}>Dodaj wypożyczenie</Text>
                    </TouchableOpacity>
                </View>
            </View>
        </View>
    );
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        padding: 20,
        backgroundColor: '#f8f9fa',
    },
    title: {
        fontSize: 32,
        fontWeight: 'bold',
        color: '#343a40',
        marginBottom: 20,
    },
    optionsContainer: {
        width: '100%',
        alignItems: 'center',
    },
    subtitle: {
        fontSize: 18,
        color: '#343a40',
        marginBottom: 15,
    },
    buttonContainer: {
        width: '100%',
        alignItems: 'center',
    },
    button: {
        backgroundColor: '#6c757d',
        paddingVertical: 10,
        paddingHorizontal: 20,
        borderRadius: 5,
        marginVertical: 5,
        width: '80%',
        alignItems: 'center',
    },
    buttonText: {
        color: '#fff',
        fontSize: 16,
    },
});

export default HomePage;