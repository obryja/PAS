import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';
import { ConfirmationProvider } from './context/ConfirmationContext';
import { View, StyleSheet } from 'react-native';

import { RootStackParamList } from './types';
import Navbar from './components/Navbar';
import UserList from './pages/user/UserList';
import UserEdit from './pages/user/UserEdit';
/* import ClientDetails from './pages/client/ClientDetails';
import RentList from './pages/rent/RentList';
import UserAdd from './pages/user/UserAdd';
import RentAdd from './pages/rent/RentAdd';
 */
import HomePage from './pages/HomePage';

const Stack = createStackNavigator<RootStackParamList>()

export default function App() {
    return (
        <ConfirmationProvider>
            <NavigationContainer>
                <View style={styles.container}>
                    <Navbar />
                    <Stack.Navigator initialRouteName="Home" screenOptions={{ headerShown: false }}>
                        <Stack.Screen name="Home" component={HomePage} />

                        <Stack.Screen name="UserList" component={UserList} />
                        {/* <Stack.Screen name="UserAdd" component={UserAdd} /> */}
                        <Stack.Screen name="UserEdit" component={UserEdit} />
                        {/* <Stack.Screen name="ClientDetails" component={ClientDetails} /> */}

                        {/* <Stack.Screen name="RentList" component={RentList} /> */}
                        {/* <Stack.Screen name="RentAdd" component={RentAdd} /> */}
                    </Stack.Navigator>
                </View>
            </NavigationContainer>
        </ConfirmationProvider>
    );
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff',
    },
});
