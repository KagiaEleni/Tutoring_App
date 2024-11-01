import React, { useEffect, useState } from 'react';
import axios from 'axios';
import Modal from './Modal';
import styles from './Lists.module.css';

export const CoursesList = () => {
    const [courses, setCourses] = useState([]);
    const [searchTerm, setSearchTerm] = useState('');
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [currentCourse, setCurrentCourse] = useState({ name: '', hourlyRate: '', description: '' });
    const [isEditMode, setIsEditMode] = useState(false);
    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState('');

    useEffect(() => {
        fetchCourses();
    }, []);

    const fetchCourses = async () => {
        setLoading(true);
        try {
            const response = await axios.get('http://localhost:8080/TutoringApp/myCourse');
            
            // Sort courses alphabetically by name before setting the state
            const sortedCourses = response.data.sort((a, b) =>
                a.name.localeCompare(b.name)
            );
    
            setCourses(sortedCourses);
        } catch (error) {
            console.error("There was an error fetching the courses!", error);
            setMessage('Failed to load courses. Please try again later.');
        } finally {
            setLoading(false);
        }
    };

    const handleEditCourse = (course) => {
        setCurrentCourse(course);
        setIsEditMode(true);
        setIsModalOpen(true);
    };

    const handleDeleteCourse = async (courseId) => {
        const confirmDelete = window.confirm('Are you sure you want to delete this course?');
        if (confirmDelete) {
            try {
                await axios.delete(`http://localhost:8080/TutoringApp/myCourse/${courseId}`);
                setCourses(courses.filter(course => course.courseId !== courseId));
                setMessage('Course deleted successfully.');
            } catch (error) {
                console.error("There was an error deleting the course!", error);
                setMessage('Failed to delete course. Please try again later.');
            }
        }
    };

    const handleCreateCourse = () => {
        setCurrentCourse({ name: '', hourlyRate: '', description: '' });
        setIsEditMode(false);
        setIsModalOpen(true);
    };

    const handleSaveCourse = async () => {
        if (isNaN(currentCourse.hourlyRate) || parseFloat(currentCourse.hourlyRate) <= 0) {
            setMessage('Hourly Rate must be a positive number.');
            return;
        }

        try {
            const courseData = {
                ...currentCourse,
                hourlyRate: parseFloat(currentCourse.hourlyRate)
            };

            if (isEditMode) {
                await axios.put(`http://localhost:8080/TutoringApp/myCourse/${currentCourse.courseId}`, courseData, {
                    headers: { "Content-Type": "application/json" },
                });
                setMessage('Course updated successfully.');
            } else {
                const response = await axios.post('http://localhost:8080/TutoringApp/myCourse', courseData, {
                    headers: { "Content-Type": "application/json" },
                });
                setCourses([...courses, response.data]);
                setMessage('Course created successfully.');
            }

            setIsModalOpen(false);
            fetchCourses();
        } catch (error) {
            console.error("There was an error saving the course!", error);
            setMessage('Failed to save course. Please try again later.');
        }
    };

    const filteredCourses = courses.filter(course => 
        course.name.toLowerCase().includes(searchTerm.toLowerCase()) || 
        course.description.toLowerCase().includes(searchTerm.toLowerCase())
    );

    return (
        <div className="App">
            <main>
                <h2>Courses' List</h2>

                {/* Search Bar */}
                <input
                    type="text"
                    placeholder="Search by course name or description"
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    style={{ marginBottom: '20px', padding: '10px', width: '300px' }}
                />

                {/* Create Course Button */}
                <button className={styles.createButton} onClick={handleCreateCourse}>
                    Create New Course
                </button>

                {loading && <p>Loading courses...</p>}
                {message && <p>{message}</p>}

                <table>
                    <thead>
                        <tr>
                            <th>Course Name</th>
                            <th>Hourly Rate</th>
                            <th>Description</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {filteredCourses.length > 0 ? (
                            filteredCourses.map((course) => (
                                <tr key={course.courseId}>
                                    <td>{course.name}</td>
                                    <td>{course.hourlyRate}</td>
                                    <td>{course.description}</td>
                                    <td>
                                        <button onClick={() => handleEditCourse(course)}>Edit</button>
                                        <button className={styles.deletebutton} onClick={() => handleDeleteCourse(course.courseId)}>Delete</button>
                                    </td>
                                </tr>
                            ))
                        ) : (
                            <tr>
                                <td colSpan="4">No courses found.</td>
                            </tr>
                        )}
                    </tbody>
                </table>

                {/* Modal for Create/Edit */}
                <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)}>
                    <h2>{isEditMode ? 'Edit Course' : 'Create New Course'}</h2>
                    <form onSubmit={(e) => { e.preventDefault(); handleSaveCourse(); }}>
                        <div>
                            <label>Course Name:</label>
                            <input
                                type="text"
                                value={currentCourse.name}
                                onChange={(e) => setCurrentCourse({ ...currentCourse, name: e.target.value })}
                                required
                            />
                        </div>
                        <div>
                            <label>Hourly Rate:</label>
                            <input
                                type="number"
                                value={currentCourse.hourlyRate}
                                onChange={(e) => setCurrentCourse({ ...currentCourse, hourlyRate: e.target.value })}
                                required
                            />
                        </div>
                        <div>
                            <label>Description:</label>
                            <input
                                type="text"
                                value={currentCourse.description}
                                onChange={(e) => setCurrentCourse({ ...currentCourse, description: e.target.value })}
                                required
                            />
                        </div>
                        <button type="submit">
                            {isEditMode ? 'Update Course' : 'Create Course'}
                        </button>
                    </form>
                </Modal>
            </main>
        </div>
    );
};
