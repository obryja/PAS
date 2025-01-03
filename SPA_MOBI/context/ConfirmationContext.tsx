import React, { createContext, useState, useContext, ReactNode } from 'react';
import { Modal, View, Text, StyleSheet, TouchableOpacity } from 'react-native';

type ConfirmationContextType = {
    showConfirmation: (message: string, onConfirm: () => void) => void;
    closeConfirmation: () => void;
};

const ConfirmationContext = createContext<ConfirmationContextType | undefined>(undefined);

export const useConfirmation = () => {
    const context = useContext(ConfirmationContext);
    if (!context) {
        throw new Error("useConfirmation must be used within a ConfirmationProvider");
    }
    return context;
};

type ConfirmationProviderProps = {
    children: ReactNode;
};

export const ConfirmationProvider: React.FC<ConfirmationProviderProps> = ({ children }) => {
    const [isOpen, setIsOpen] = useState(false);
    const [message, setMessage] = useState('');
    const [onConfirmCallback, setOnConfirmCallback] = useState<() => void>(() => () => {});

    const showConfirmation = (msg: string, onConfirm: () => void) => {
        setMessage(msg);
        setOnConfirmCallback(() => onConfirm);
        setIsOpen(true);
    };

    const closeConfirmation = () => {
        setIsOpen(false);
        setMessage('');
        setOnConfirmCallback(() => () => {});
    };

    const handleConfirm = () => {
        onConfirmCallback();
        closeConfirmation();
    };

    return (
        <ConfirmationContext.Provider value={{ showConfirmation, closeConfirmation }}>
            {children}
            <Modal
                visible={isOpen}
                transparent={true}
                animationType="slide"
                onRequestClose={closeConfirmation}
            >
                <View style={styles.modalOverlay}>
                    <View style={styles.modalContainer}>
                        <Text style={styles.modalTitle}>Potwierdzenie</Text>
                        <Text style={styles.modalMessage}>{message}</Text>
                        <View style={styles.modalButtons}>
                            <TouchableOpacity style={styles.confirmButton} onPress={handleConfirm}>
                                <Text style={styles.buttonText}>Potwierdź</Text>
                            </TouchableOpacity>
                            <TouchableOpacity style={styles.cancelButton} onPress={closeConfirmation}>
                                <Text style={styles.buttonText}>Anuluj</Text>
                            </TouchableOpacity>
                        </View>
                    </View>
                </View>
            </Modal>
        </ConfirmationContext.Provider>
    );
};

const styles = StyleSheet.create({
    modalOverlay: {
        flex: 1,
        backgroundColor: 'rgba(0, 0, 0, 0.5)',
        justifyContent: 'center',
        alignItems: 'center',
    },
    modalContainer: {
        backgroundColor: '#fff',
        padding: 20,
        borderRadius: 10,
        width: '80%',
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.25,
        shadowRadius: 4,
        elevation: 5,
    },
    modalTitle: {
        fontSize: 18,
        fontWeight: 'bold',
        marginBottom: 10,
    },
    modalMessage: {
        fontSize: 16,
        marginBottom: 20,
    },
    modalButtons: {
        flexDirection: 'row',
        justifyContent: 'space-between',
    },
    confirmButton: {
        backgroundColor: '#007bff',
        padding: 10,
        borderRadius: 5,
    },
    cancelButton: {
        backgroundColor: '#6c757d',
        padding: 10,
        borderRadius: 5,
    },
    buttonText: {
        color: '#fff',
        fontSize: 16,
    },
});