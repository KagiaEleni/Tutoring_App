import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import styles from '../components/Forms.module.css'

export const CreateTutor = () => {
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [phone, setPhone] = useState('');
    const [email, setEmail] = useState('');
    const [salary, setSalary] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (event) => {
        event.preventDefault();
        
        const tutorData = {
            firstName,
            lastName,
            phone,
            email,
            salary
        };

        try {
            const response = await axios.post("http://localhost:8080/TutoringApp/myTutor", tutorData, {
                headers: {
                    "Content-Type": "application/json",
                },
            });
            console.log("Tutor created successfully", response.data);
            navigate('/tutors');
        } catch (error) {
            console.error("There was an error creating the tutor!", error);
        }
    };

    return (
        <div className="CreateTutor">
            <h2>Create New Tutor</h2>
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
                    Salary:
                    <input
                        type="salary"
                        value={salary}
                        onChange={(e) => setSalary(e.target.value)}
                        required
                    />
                </label>
                <button type="submit">Submit</button>
                <button className={styles.deletebutton} onClick={() => navigate('/tutors')}>Cancel</button>
            </form>
        </div>
    );
};
