import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './Lists.module.css';

export const TutorsList = () => {
    const [tutors, setTutors] = useState([]);
    const [searchTerm, setSearchTerm] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const fetchTutors = async () => {
            try {
                const response = await axios.get('http://localhost:8080/TutoringApp/myTutor');
                setTutors(response.data);
            } catch (error) {
                console.error("Error fetching tutors!", error);
            }
        };
        fetchTutors();
    }, []);

    const filteredTutors = tutors.filter(tutor => {
        const fullName = `${tutor.firstName} ${tutor.lastName}`.toLowerCase();
        return fullName.includes(searchTerm.toLowerCase());
    });

    const handleTutorClick = (tutorId) => {
        navigate(`/tutors/${tutorId}`);
    };

    return (
        <div className="App">
            <main>
                <h2>Tutors' List</h2>
                <input
                    type="text"
                    placeholder="Search by tutor name"
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    className="searchInput"
                />
                <button onClick={() => navigate('/tutors/new')} className="createButton">
                    Create Tutor
                </button>

                <table className="tutorsTable">
                    <thead>
                        <tr>
                            <th>First Name</th>
                            <th>Last Name</th>
                        </tr>
                    </thead>
                    <tbody>
                        {filteredTutors.map(tutor => (
                            <tr
                                key={tutor.id}
                                onClick={() => handleTutorClick(tutor.id)}
                                className="clickableRow"
                            >
                                <td>{tutor.firstName}</td>
                                <td>{tutor.lastName}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </main>
        </div>
    );
};
