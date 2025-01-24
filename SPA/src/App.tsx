import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';

import { ConfirmationProvider } from './context/ConfirmationContext';
import Navbar from './components/Navbar';
import UserList from './pages/user/UserList';
import ClientDetails from './pages/client/ClientDetails'
import RentList from './pages/rent/RentList';
import UserAdd from './pages/user/UserAdd';
import RentAdd from './pages/rent/RentAdd';
import UserEdit from './pages/user/UserEdit';
import HomePage from './pages/HomePage';
import LoginPage from './pages/LoginPage';
import { UserProvider } from './context/UserContext';
import PrivateRoute from './components/PrivateRoute';
import ResetPassword from './pages/user/ResetPassword';

const App: React.FC = () => {
    return (
        <ConfirmationProvider>
            <UserProvider>
                <Router>
                    <Navbar />
                    <div className="container mt-4">
                        <Routes>
                            <Route path="/" element={<HomePage />} />
                            <Route path="/login" element={<LoginPage />} />
                            <Route path="/users/add" element={<UserAdd />} />

                            <Route path="/password" element={
                                <PrivateRoute allowedRoles={['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_CLIENT']}>
                                    {<ResetPassword />}
                                </PrivateRoute>
                            }/>

                            <Route path="/users/list" element={
                                <PrivateRoute allowedRoles={['ROLE_ADMIN', 'ROLE_MANAGER']}>
                                    {<UserList />}
                                </PrivateRoute>
                            }/>

                            <Route path="/users/edit/:id" element={
                                <PrivateRoute allowedRoles={['ROLE_ADMIN']}>
                                    {<UserEdit />}
                                </PrivateRoute>
                            }/>

                            <Route path="/clients/:id" element={
                                <PrivateRoute allowedRoles={['ROLE_MANAGER']}>
                                    {<ClientDetails />}
                                </PrivateRoute>
                            }/>

                            
                            <Route path="/info" element={
                                <PrivateRoute allowedRoles={['ROLE_CLIENT']}>
                                    {<ClientDetails />}
                                </PrivateRoute>
                            }/>

                            <Route path="/rents/list" element={
                                <PrivateRoute allowedRoles={['ROLE_MANAGER']}>
                                    {<RentList />}
                                </PrivateRoute>
                            }/>

                            <Route path="/rents/add" element={
                                <PrivateRoute allowedRoles={['ROLE_MANAGER', 'ROLE_CLIENT']}>
                                    {<RentAdd />}
                                </PrivateRoute>
                            }/>

                            <Route path="*" element={<Navigate to="/" replace />} />
                        </Routes>
                    </div>
                </Router>
            </UserProvider>
        </ConfirmationProvider>
    );
};

export default App;