import React, { useState } from 'react';
import { View, Text, Image, StyleSheet, TouchableOpacity, Dimensions } from 'react-native';
import { useNavigation } from '@react-navigation/native';
import { StackNavigationProp } from '@react-navigation/stack';

import { RootStackParamList } from '../types';

type NavigationProp = StackNavigationProp<RootStackParamList, 'Home'>;

const Navbar = () => {
    const navigation = useNavigation<NavigationProp>(); 
    const [menuVisible, setMenuVisible] = useState(false);

    const handleMenuToggle = () => {
        setMenuVisible(!menuVisible);
    };

    const windowWidth = Dimensions.get('window').width;

    return (
        <View style={styles.navbar}>
            <TouchableOpacity style={styles.logoContainer} onPress={() => navigation.navigate('Home')}>
                <Image source={require('../assets/book32.png')} style={styles.logo} />
                <Text style={styles.title}>Biblioteka</Text>
            </TouchableOpacity>

            <TouchableOpacity onPress={handleMenuToggle} style={styles.menuButton}>
                <Text style={styles.navLinkText}>{menuVisible ? 'Zamknij' : 'Menu'}</Text>
            </TouchableOpacity>

            {menuVisible && (
                <View style={[styles.dropdownMenu, { width: windowWidth }]}>
                    <TouchableOpacity onPress={handleMenuToggle} style={styles.closeButton}>
                        <Text style={styles.navLinkText}>Zamknij</Text>
                    </TouchableOpacity>

                    <TouchableOpacity onPress={() => navigation.navigate('UserList')} style={styles.dropdownLink}>
                        <Text style={styles.navLinkText}>Lista użytkowników</Text>
                    </TouchableOpacity>
                    <TouchableOpacity onPress={() => navigation.navigate('UserAdd')} style={styles.dropdownLink}>
                        <Text style={styles.navLinkText}>Utwórz użytkownika</Text>
                    </TouchableOpacity>
                    <TouchableOpacity onPress={() => navigation.navigate('RentList')} style={styles.dropdownLink}>
                        <Text style={styles.navLinkText}>Lista wypożyczeń</Text>
                    </TouchableOpacity>
                    <TouchableOpacity onPress={() => navigation.navigate('RentAdd')} style={styles.dropdownLink}>
                        <Text style={styles.navLinkText}>Utwórz wypożyczenie</Text>
                    </TouchableOpacity>
                </View>
            )}
        </View>
    );
};

const styles = StyleSheet.create({
    navbar: {
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'flex-start',
        backgroundColor: '#343a40',
        paddingTop: 40,
        padding: 10,
    },
    logoContainer: {
        flexDirection: 'row',
        alignItems: 'center',
        marginBottom: 10,
    },
    logo: {
        width: 32,
        height: 32,
    },
    title: {
        color: '#fff',
        marginLeft: 10,
        fontSize: 18,
        fontWeight: 'bold',
    },
    menuButton: {
        padding: 10,
    },
    dropdownMenu: {
        position: 'absolute',
        top: 70,
        left: 0,
        backgroundColor: '#343a40',
        padding: 10,
        zIndex: 999,
    },
    closeButton: {
        padding: 10,
        alignItems: 'flex-end',
    },
    dropdownLink: {
        marginVertical: 10,
    },
    navLinkText: {
        color: '#fff',
        fontSize: 16,
    },
});

export default Navbar;