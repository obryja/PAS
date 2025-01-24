import React from 'react';
import { Navigate } from 'react-router-dom';
import { useUserContext } from '../context/UserContext';

interface PrivateRouteProps {
    children: React.ReactNode;
    allowedRoles: string[];
}

const PrivateRoute: React.FC<PrivateRouteProps> = ({ children, allowedRoles }) => {
    const { user } = useUserContext();

    if (!user) {
        return <Navigate to="/login" replace />;
    }

    if (!user.role || !allowedRoles.includes(user.role)) {
        return <Navigate to="/" replace />;
    }

    return <>{children}</>;
};

export default PrivateRoute;