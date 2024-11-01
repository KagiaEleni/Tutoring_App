import React, { useEffect, useState } from 'react';
import axios from 'axios';

export const Finance = () => {
    const [totalEarnings, setTotalEarnings] = useState(0);
    const [totalPaymentsToTutors, setTotalPaymentsToTutors] = useState(0);
    const [netProfit, setNetProfit] = useState(0);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchFinanceData = async () => {
            try {
                const tutorsResponse = await axios.get('http://localhost:8080/TutoringApp/myTutor');
                const studentsResponse = await axios.get('http://localhost:8080/TutoringApp/myStudent');

                const tutors = tutorsResponse.data;
                const students = studentsResponse.data;

                // Calculate total earnings from students
                const earnings = students.reduce((total, student) => {
                    const studentEarnings = student.enrollments.reduce((sum, enrollment) => sum + enrollment.monthly_fee, 0);
                    return total + studentEarnings;
                }, 0);

                setTotalEarnings(earnings);

                // Calculate total payments to tutors based on individual salaries
                const totalPayments = tutors.reduce((sum, tutor) => sum + tutor.salary, 0);
                setTotalPaymentsToTutors(totalPayments);

                // Calculate net profit
                setNetProfit(earnings - totalPayments);
            } catch (error) {
                console.error("Error fetching finance data", error);
                setError('Failed to load financial data. Please try again later.');
            } finally {
                setLoading(false);
            }
        };

        fetchFinanceData();
    }, []);

    return (
        <div>
            <h2>Finance Overview</h2>
            {loading ? (
                <p>Loading financial data...</p>
            ) : error ? (
                <p>{error}</p>
            ) : (
                <div>
                    <p><strong>Total Earnings from Students:</strong> €{totalEarnings.toFixed(2)}</p>
                    <p><strong>Total Payments to Tutors:</strong> €{totalPaymentsToTutors.toFixed(2)}</p>
                    <p><strong>Net Profit:</strong> €{netProfit.toFixed(2)}</p>
                </div>
            )}
        </div>
    );
};
