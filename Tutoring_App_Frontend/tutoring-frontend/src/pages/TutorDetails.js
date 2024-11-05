import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import Modal from '../components/Modal';
import styles from '../components/Lists.module.css';

export const TutorDetails = () => {
    const { id } = useParams();
    const [tutor, setTutor] = useState(null);
    const [loading, setLoading] = useState(true);
    const [editMode, setEditMode] = useState(false);
    const [updatedTutor, setUpdatedTutor] = useState({});
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [newCourse, setNewCourse] = useState({ courseId: '' });
    const [tutorCourses, setTutorCourses] = useState([]);
    const [allCourses, setCourses] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchTutorDetail = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/TutoringApp/MyTutor`);
                const foundTutor = response.data.find(tutor => tutor.id === parseInt(id));
                setTutor(foundTutor);
                setUpdatedTutor(foundTutor);
            } catch (error) {
                console.error("Error fetching tutor details!", error);
            } finally {
                setLoading(false);
            }
        };

        const fetchTutorCourses = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/TutoringApp/MyTutorCourses`);
                const filteredCourses = response.data.filter(course => course.tutor_id === parseInt(id));
                setTutorCourses(filteredCourses);
            } catch (error) {
                console.error("Error fetching tutor courses!", error);
            }
        };

        const fetchCourses = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/TutoringApp/myCourse`);
                const sortedCourses = response.data.sort((a, b) => a.name.localeCompare(b.name));
                setCourses(sortedCourses);
            } catch (error) {
                console.error("Error fetching courses!", error);
            }
        };

        fetchTutorDetail();
        fetchTutorCourses();
        fetchCourses();
    }, [id]);

    const handleDelete = async () => {
        const confirmDelete = window.confirm('Are you sure you want to delete this tutor?');
        if (confirmDelete) {
            try {
                await axios.delete(`http://localhost:8080/TutoringApp/MyTutor/${id}`);
                navigate('/tutors');
            } catch (error) {
                console.error('Error deleting the tutor', error);
            }
        }
    };

    const handleUpdate = async () => {
        try {
            const updatedData = {
                id: tutor.id,
                firstName: updatedTutor.firstName,
                lastName: updatedTutor.lastName,
                phone: updatedTutor.phone,
                email: updatedTutor.email,
                salary: updatedTutor.salary
            };

            await axios.put(`http://localhost:8080/TutoringApp/myTutor/${id}`, updatedData);
            setEditMode(false);
            setTutor(updatedData);
            navigate(`/tutors/${id}`);
        } catch (error) {
            console.error('Error updating the tutor', error);
        }
    };

    const toggleEditMode = () => {
        setEditMode(!editMode);
    };

    const handleAddCourse = async () => {
        try {
            const newCourseData = {
                tutor_id: tutor.id,
                course_id: Number(newCourse.courseId)
            };

            console.log(newCourseData); // Debugging log

            const response = await axios.post(`http://localhost:8080/TutoringApp/myTutorCourses`, newCourseData);
            setTutorCourses(prevCourses => [
                ...prevCourses,
                response.data
            ]);
            setIsModalOpen(false);
            setNewCourse({ courseId: '' });
        } catch (error) {
            console.error('Error adding new course:', error);
        }
    };

    const handleDeleteCourse = async (tutorCourseId) => {
        const confirmDelete = window.confirm('Are you sure you want to delete this course?');
        if (confirmDelete) {
            try {
                await axios.delete(`http://localhost:8080/TutoringApp/myTutorCourses/${tutorCourseId}`);
                setTutorCourses(prevCourses => prevCourses.filter(course => course.tutor_course_id !== tutorCourseId));
            } catch (error) {
                console.error('Error deleting the course', error);
            }
        }
    };

    if (loading) return <div>Loading...</div>;
    if (!tutor) return <div>Tutor not found</div>;

    // Sort tutorCourses by course name before rendering
    const sortedTutorCourses = tutorCourses.sort((a, b) => {
        const courseA = allCourses.find(course => course.courseId === a.course_id);
        const courseB = allCourses.find(course => course.courseId === b.course_id);
        return courseA?.name.localeCompare(courseB?.name);
    });

    return (
        <div>
            <h2>{tutor.firstName} {tutor.lastName}</h2>

            {editMode ? (
                <div>
                    <label>
                        First Name:
                        <input
                            type="text"
                            value={updatedTutor.firstName}
                            onChange={(e) => setUpdatedTutor({ ...updatedTutor, firstName: e.target.value })}
                        />
                    </label>
                    <label>
                        Last Name:
                        <input
                            type="text"
                            value={updatedTutor.lastName}
                            onChange={(e) => setUpdatedTutor({ ...updatedTutor, lastName: e.target.value })}
                        />
                    </label>
                    <label>
                        Phone:
                        <input
                            type="text"
                            value={updatedTutor.phone}
                            onChange={(e) => setUpdatedTutor({ ...updatedTutor, phone: e.target.value })}
                        />
                    </label>
                    <label>
                        Email:
                        <input
                            type="text"
                            value={updatedTutor.email}
                            onChange={(e) => setUpdatedTutor({ ...updatedTutor, email: e.target.value })}
                        />
                    </label>
                    <label>
                        Salary:
                        <input
                            type="number"
                            value={updatedTutor.salary || ''} // Set default value if undefined
                            onChange={(e) => setUpdatedTutor({ ...updatedTutor, salary: Number(e.target.value) })}
                        />
                    </label>
                    <button onClick={handleUpdate}>Save Changes</button>
                    <button className={styles.deletebutton} onClick={toggleEditMode}>Cancel</button>
                </div>
            ) : (
                <div>
                    <p><strong>Phone:</strong> {tutor.phone}</p>
                    <p><strong>Email:</strong> {tutor.email}</p>
                    <p><strong>Salary:</strong> €{tutor.salary.toFixed(2)}</p>

                    <h3>Actions</h3>
                    <button onClick={toggleEditMode}>Edit Tutor</button>
                    <button className={styles.deletebutton} onClick={handleDelete}>Delete Tutor</button>
                </div>
            )}

            <h3>Courses</h3>
            {sortedTutorCourses && sortedTutorCourses.length > 0 && allCourses.length > 0 ? (
                <table>
                    <thead>
                        <tr>
                            <th>Course</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {sortedTutorCourses.map((tutorCourse) => (
                            <tr key={tutorCourse.tutor_course_id}>
                                <td>
                                    {allCourses.find(course => course.courseId === tutorCourse.course_id)?.name || "Unknown"}
                                </td>
                                <td>
                                    <button
                                        className={styles.deletebutton}
                                        onClick={() => handleDeleteCourse(tutorCourse.tutor_course_id)}
                                    >
                                        Delete
                                    </button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            ) : (
                <p>No courses taught by this tutor.</p>
            )}

            <button onClick={() => setIsModalOpen(true)}>Add Course</button>
            <button onClick={() => navigate('/tutors')}>Back</button>

            <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)}>
                <h2>Add New Course</h2>
                <label>
                    Select Course:
                    <select
                        value={newCourse.courseId}
                        onChange={(e) => setNewCourse({ courseId: e.target.value })}
                    >
                        <option value="">--Select a course--</option>
                        {allCourses.map(course => (
                            <option key={course.courseId} value={course.courseId}>{course.name}</option>
                        ))}
                    </select>
                </label>
                <button onClick={handleAddCourse}>Add Course</button>
            </Modal>
        </div>
    );
};
