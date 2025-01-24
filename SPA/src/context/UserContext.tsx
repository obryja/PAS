import React, { createContext, useState, useContext, ReactNode } from 'react';
import { jwtDecode } from 'jwt-decode';
import { UserLogged } from '../model/UserLogged';
import { JwtPayload } from '../model/JwtPayload';

interface UserContextType {
    user: UserLogged;
    login: (token: string) => void;
    logout: () => void;
}

const UserContext = createContext<UserContextType | undefined>(undefined);

export const UserProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
    const [user, setUserState] = useState<UserLogged>({ username: null, role: null });

    const login = (token: string) => {
        try {
            const decoded: JwtPayload = jwtDecode<JwtPayload>(token);
            setUserState({ username: decoded.sub, role: decoded.auth });
            localStorage.setItem('jwt', token);
        } catch (error) {
            console.error('Invalid token', error);
            logout();
        }
    };
      
    const logout = () => {
        setUserState({ username: null, role: null });
        localStorage.removeItem('jwt');
    };
    
    React.useEffect(() => {
        const token = localStorage.getItem('jwt');
        if (token) {
            login(token);
        }
    }, []);

    return (
        <UserContext.Provider value={{ user, login, logout }}>
            {children}
        </UserContext.Provider>
    );
};

export const useUserContext = (): UserContextType => {
    const context = useContext(UserContext);
    if (!context) {
        throw new Error('useUserContext must be used within a UserProvider');
    }
    return context;
};