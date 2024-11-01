import React from 'react';
import { useNavigate } from 'react-router-dom';
import styles from '../components/Home.module.css';


export const Home = () => {
    const navigate = useNavigate();

    const navigateTo = (path) => {
        navigate(path);
    };

    return (
        <div className={styles.body}>
            <div className={styles.wrapper}>
                <div className={styles.homeContainer}>
                    <h1>Welcome to the Tutoring Web App</h1>
                    <div className={styles.buttonContainer}>
                        <button className={styles.button} onClick={() => navigateTo('/students')}>Students</button>
                        <button className={styles.button} onClick={() => navigateTo('/courses')}>Courses</button>
                        <button className={styles.button} onClick={() => navigateTo('/tutors')}>Tutors</button>
                        <button className={styles.button} onClick={() => navigateTo('/finance')}>Finance</button>
                    </div>
                </div>
            </div>
        </div>
    );
};
