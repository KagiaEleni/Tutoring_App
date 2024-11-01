import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import styles from '../components/Forms.module.css';

export const CreateStudent = () => {
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [fatherName, setFatherName] = useState('');
    const [phone, setPhone] = useState('');
    const [email, setEmail] = useState('');
    const [registrationDate, setRegistrationDate] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (event) => {
        event.preventDefault();
        
        const studentData = {
            firstName,
            lastName,
            fatherName,
            phone,
            email,
            registrationDate
        };

        try {
            const response = await axios.post("http://localhost:8080/TutoringApp/myStudent", studentData, {
                headers: {
                    "Content-Type": "application/json",
                },
            });
            console.log("Student created successfully", response.data);
            navigate('/students');
        } catch (error) {
            console.error("There was an error creating the student!", error);
        }
    };

    return (
        <div className="CreateStudent">
            <h2>Create New Student</h2>
            <form onSubmit={handleSubmit}>
                <label>
                    First Name:
                    <input
                        type="text"
                        value={firstName}
                        onChange={(e) => setFirstName(e.target.value)}
                        required
                    />
                </label>
                <label>
                    Last Name:
                    <input
                        type="text"
                        value={lastName}
                        onChange={(e) => setLastName(e.target.value)}
                        required
                    />
                </label>
                <label>
                    Father Name:
                    <input
                        type="text"
                        value={fatherName}
                        onChange={(e) => setFatherName(e.target.value)}
                        required
                    />
                </label>
                <label>
                    Phone:
                    <input
                        type="text"
                        value={phone}
                        onChange={(e) => setPhone(e.target.value)}
                        required
                    />
                </label>
                <label>
                    Email:
                    <input
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                </label>
                <label>
                    Registration Date:
                    <input
                        type="date"
                        value={registrationDate}
                        onChange={(e) => setRegistrationDate(e.target.value)}
                        required
                    />
                </label>
                <button type="submit">Submit</button>
                <button className={styles.deletebutton} onClick={() => navigate('/students')}>Cancel</button>
            </form>
        </div>
    );
};
