import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import UserList from './components/user/UserList';
import Navbar from './components/Navbar';

const App: React.FC = () => {
    return (
        <Router>
            <Navbar />
            <div className="container mt-4">
                <Routes>
                    <Route path="/users" element={<UserList />} />
                </Routes>
            </div>
        </Router>
    );
};

export default App;
