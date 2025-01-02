import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';

import { ConfirmationProvider } from './context/ConfirmationContext';
import Navbar from './components/Navbar';
import UserList from './pages/user/UserList';
import ClientDetails from './pages/client/ClientDetails'
import RentList from './pages/rent/RentList';
import UserAdd from './pages/user/UserAdd';
import RentAdd from './pages/rent/RentAdd';

const App: React.FC = () => {
    return (
        <ConfirmationProvider>
            <Router>
                <Navbar />
                <div className="container mt-4">
                    <Routes>
                        <Route path="/users/list" element={<UserList />} />
                        <Route path="/clients/:id" element={<ClientDetails />} />
                        <Route path="/rents/list" element={<RentList />} />
                        <Route path="/users/add" element={<UserAdd />} />
                        <Route path="/rents/add" element={<RentAdd />} />
                    </Routes>
                </div>
            </Router>
        </ConfirmationProvider>
    );
};

export default App;
