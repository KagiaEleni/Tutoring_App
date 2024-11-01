import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { Home } from './pages/Home';
import { Students } from './pages/Students';
import { Courses } from './pages/Courses';
import { Tutors } from './pages/Tutors.js';
import { NotFoundPage } from './pages/NotFoundPage';
import { StudentDetails } from './pages/StudentDetails';
import { TutorDetails } from './pages/TutorDetails.js';
import { CreateStudent } from './pages/CreateStudent';
import { CreateTutor } from './pages/CreateTutor';
import { Finance } from './pages/Finance';

export const Rout = () => {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/students" element={<Students />} />
                <Route path="/courses" element={<Courses />} />
                <Route path="/tutors" element={<Tutors />} />
                <Route path="/students/:id" element={<StudentDetails />} />
                <Route path="/tutors/:id" element={<TutorDetails />} />
                <Route path="/students/new" element={<CreateStudent />} />
                <Route path="/tutors/new" element={<CreateTutor />} />
                <Route path="/finance" element={<Finance />} />
                <Route path="*" element={<NotFoundPage />} />
            </Routes>
        </BrowserRouter>
    );
}