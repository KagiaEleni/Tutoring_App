import React from 'react';
import './Header.css';

export const Header = () => {
    return (
        <header className="header">
            <h1>Tutoring Web App</h1>
            <nav>
                <ul>
                    <li><a href="/">Home</a></li>
                    <li><a href="/students">Students</a></li>
                    <li><a href="/courses">Courses</a></li>
                    <li><a href="/tutors">Tutors</a></li>
                    <li><a href="/finance">Finance</a></li>
                </ul>
            </nav>
        </header>
    );
};
