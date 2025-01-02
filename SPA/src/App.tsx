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

const App: React.FC = () => {
    return (
        <ConfirmationProvider>
            <Router>
                <Navbar />
                <div className="container mt-4">
                    <Routes>
                        <Route path="/" element={<HomePage />} />

                        <Route path="/users/list" element={<UserList />} />
                        <Route path="/users/add" element={<UserAdd />} />
                        <Route path="/users/edit/:id" element={<UserEdit />} />
                        <Route path="/clients/:id" element={<ClientDetails />} />

                        <Route path="/rents/list" element={<RentList />} />
                        <Route path="/rents/add" element={<RentAdd />} />

                        <Route path="*" element={<Navigate to="/" replace />} />
                    </Routes>
                </div>
            </Router>
        </ConfirmationProvider>
    );
};

export default App;