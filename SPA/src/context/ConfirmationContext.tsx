import React, { createContext, useState, useContext, ReactNode } from 'react';

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
            {isOpen && (
            <div className="modal show d-block" tabIndex={-1} role="dialog">
                <div className="modal-dialog" role="document">
                <div className="modal-content">
                    <div className="modal-header">
                        <h5 className="modal-title">Potwierdzenie</h5>
                        <button type="button" className="btn-close" onClick={closeConfirmation} aria-label="Close"></button>
                    </div>
                    <div className="modal-body">
                        <p>{message}</p>
                    </div>
                    <div className="modal-footer">
                        <button type="button" className="btn btn-primary" onClick={handleConfirm}>
                            Potwierdź
                        </button>
                        <button type="button" className="btn btn-secondary" onClick={closeConfirmation}>
                            Anuluj
                        </button>
                    </div>
                </div>
                </div>
            </div>
            )}
        </ConfirmationContext.Provider>
    );
};