import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './Lists.module.css';

export const StudentList = () => {
    const [students, setStudents] = useState([]); 
    const [searchTerm, setSearchTerm] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const fetchStudents = async () => {
            try {
                const response = await axios.get('http://localhost:8080/TutoringApp/myStudent');
                setStudents(response.data);
            } catch (error) {
                console.error("There was an error fetching the students!", error);
            }
        };
        fetchStudents();
    }, []);

    // Function to format the registration date
    const formatRegistrationDate = (date) => {
        if (Array.isArray(date)) {
            const [year, month, day] = date;
            const formattedDate = new Date(year, month - 1, day);
            return formattedDate.toLocaleDateString();
        }
        return "Invalid date";
    };

    const handleStudentClick = (studentId) => {
        navigate(`/students/${studentId}`);
    };

    const handleCreateNewStudent = () => {
        navigate('/students/new');
    };

    // Filtered student list based on the search term
    const filteredStudents = students.filter(student => {
        const fullName = `${student.firstName} ${student.lastName} ${student.fatherName}`.toLowerCase();
        return fullName.includes(searchTerm.toLowerCase());
    });

    return (
        <div className="App">
            <main>
                <h2>Students' List</h2>
                               
                {/* Search Bar */}
                <input
                    type="text"
                    placeholder="Search by name or father name"
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    style={{ marginBottom: '20px', padding: '10px', width: '300px' }}
                />

                <button onClick={handleCreateNewStudent}>Create New Student</button>
                
                <table>
                    <thead>
                        <tr>
                            <th>First Name</th>
                            <th>Last Name</th>
                            <th>Father Name</th>
                            <th>Phone</th>
                            <th>Email</th>
                            <th>Registration Date</th>
                        </tr>
                    </thead>
                    <tbody>
                        {filteredStudents.map((student) => (
                            <tr key={student.id} onClick={() => handleStudentClick(student.id)} style={{ cursor: 'pointer' }}>
                                <td>{student.firstName}</td>
                                <td>{student.lastName}</td>
                                <td>{student.fatherName}</td>
                                <td>{student.phone}</td>
                                <td>{student.email}</td>
                                <td>{formatRegistrationDate(student.registrationDate)}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </main>
        </div>
    );
};
