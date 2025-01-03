import React, { useEffect, useState } from 'react';

import axios from '../../api/Axios';
import RentDetails from '../../model/RentDetails';
import { useConfirmation } from '../../context/ConfirmationContext';

const RentList: React.FC = () => {
    const [rents, setRents] = useState<RentDetails[]>([]);
    const { showConfirmation } = useConfirmation();

    useEffect(() => {
        axios.get('/rents/details').then((response) => setRents(response.data));
    }, []);

    const handleEndRent = async (rentId: string) => {
        showConfirmation(
            'Czy na pewno chcesz zakończyć to wypożyczenie?',
            async () => {
                try {
                    const response = await axios.post(`/rents/${rentId}`);
                    const updatedRent = response.data;

                    setRents((prevRents) =>
                        prevRents.map((rent) =>
                            rent.id === rentId ? { ...rent, endDate: updatedRent.endDate } : rent
                        )
                    );
                } catch (error) {
                    console.error(error);
                }
            }
        );
    };

    return (
        <div className="container my-5">
            <h2>Lista wypożyczeń</h2>

            <div className="table-responsive">
                <table className="table table-striped table-hover table-bordered">
                    <thead className='table-dark text-center'>
                    <tr>
                        <th>ID</th>
                        <th>Nazwa użytkownika</th>
                        <th>Tytuł</th>
                        <th>Data rozpoczęcia</th>
                        <th>Data zakończenia</th>
                        <th>Akcja</th>
                    </tr>
                    </thead>
                    <tbody>
                    {rents
                        .map((rent) => (
                        <tr key={rent.id}>
                            <td>{rent.id}</td>
                            <td>{rent.username}</td>
                            <td>{rent.title}</td>
                            <td>{new Date(rent.beginDate).toLocaleString()}</td>
                            <td>{rent.endDate ? new Date(rent.endDate).toLocaleString() : 'Brak'}</td>
                            <td>
                                {rent.endDate === null && (
                                    <button
                                        className="btn btn-sm btn-danger"
                                        onClick={() => handleEndRent(rent.id)}
                                    >
                                    Zakończ wypożyczenie
                                    </button>
                                )}
                            </td>
                        </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default RentList;