import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import Modal from '../components/Modal';
import styles from '../components/Home.module.css';

export const StudentDetails = () => {
    const { id } = useParams();
    const [student, setStudent] = useState(null);
    const [loading, setLoading] = useState(true);
    const [updatedStudent, setUpdatedStudent] = useState({});
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [isEditModalOpen, setIsEditModalOpen] = useState(false); // For edit modal
    const [newEnrollment, setNewEnrollment] = useState({ course_id: 0, hours_per_week: 0 });
    const [courses, setCourses] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchStudentDetail = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/TutoringApp/myStudent`);
                const foundStudent = response.data.find((student) => student.id === parseInt(id));
                setStudent(foundStudent);
                setUpdatedStudent(foundStudent);
            } catch (error) {
                console.error("Error fetching student details!", error);
            } finally {
                setLoading(false);
            }
        };

        const fetchCourses = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/TutoringApp/myCourse`);
                setCourses(response.data);
            } catch (error) {
                console.error("Error fetching courses!", error);
            }
        };

        fetchStudentDetail();
        fetchCourses();
    }, [id]);

    const handleDelete = async () => {
        const confirmDelete = window.confirm('Are you sure you want to delete this student?');
        if (confirmDelete) {
            try {
                await axios.delete(`http://localhost:8080/TutoringApp/myStudent/${id}`);
                navigate('/students');
            } catch (error) {
                console.error('Error deleting the student', error);
            }
        }
    };

    const handleUpdate = async () => {
        try {
            const updatedData = {
                ...updatedStudent,
                id: student.id,
                registrationDate: student.registrationDate, // Keep the original registration date
            };
            await axios.put(`http://localhost:8080/TutoringApp/myStudent/${id}`, updatedData);
            setIsEditModalOpen(false); // Close modal on successful update
            setStudent(updatedData);
        } catch (error) {
            console.error('Error updating the student', error);
        }
    };

    const handleSaveEnrollmentChange = async () => {
        const updatedEnrollment = {
            student_id: student.id,
            course_id: newEnrollment.course_id,
            hours_per_week: newEnrollment.hours_per_week
        };

        try {
            const response = await axios.post(`http://localhost:8080/TutoringApp/myEnrollment`, updatedEnrollment);
            addEnrollmentState(response.data);
            setIsModalOpen(false);
            resetEnrollmentState();
        } catch (error) {
            console.error("Error adding enrollment", error);
        }
    };

    const addEnrollmentState = (newEnrollment) => {
        setStudent(prev => ({
            ...prev,
            enrollments: [...prev.enrollments, newEnrollment],
        }));
    };

    const resetEnrollmentState = () => {
        setNewEnrollment({ course_id: '', hours_per_week: 0 });
    };

    const handleDeleteEnrollment = async (enrollmentId) => {
        const confirmDelete = window.confirm('Are you sure you want to delete this enrollment?');
        if (confirmDelete) {
            try {
                await axios.delete(`http://localhost:8080/TutoringApp/myEnrollment/${enrollmentId}`);
                setStudent(prev => ({
                    ...prev,
                    enrollments: prev.enrollments.filter(enrollment => enrollment.enrollment_id !== enrollmentId)
                }));
            } catch (error) {
                console.error('Error deleting the enrollment', error);
            }
        }
    };

    const formatRegistrationDate = (date) => {
        if (Array.isArray(date)) {
            const [year, month, day] = date;
            return new Date(year, month - 1, day).toLocaleDateString();
        }
        return "Invalid date";
    };

    const calculateTotalMonthlyFee = () => {
        return student.enrollments.reduce((total, enrollment) => {
            const course = courses.find(course => course.courseId === enrollment.course_id);
            return course ? total + (course.hourlyRate * enrollment.hours_per_week * 4) : total;
        }, 0);
    };

    if (loading) return <div>Loading...</div>;
    if (!student) return <div>Student not found</div>;

    return (
        <div>
            <h2>{student.firstName} {student.lastName}</h2>

            <div>
                <p><strong>Phone:</strong> {student.phone}</p>
                <p><strong>Email:</strong> {student.email}</p>
                <p><strong>Father Name:</strong> {student.fatherName || 'N/A'}</p>
                <p><strong>Registration Date:</strong> {formatRegistrationDate(student.registrationDate)}</p>
                <p><strong>Total Monthly Fee:</strong> ${calculateTotalMonthlyFee().toFixed(2)}</p>
                <button onClick={() => setIsEditModalOpen(true)}>Edit Student</button>
                <button className={styles.deletebutton} onClick={handleDelete}>Delete Student</button>
            </div>

            <h3>Enrollments</h3>
            {student.enrollments.length > 0 ? (
                <table>
                    <thead>
                        <tr>
                            <th>Course</th>
                            <th>Hours per Week</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {student.enrollments.map((enrollment) => (
                            <tr key={enrollment.enrollment_id}>
                                <td>{courses.find(course => course.courseId === enrollment.course_id)?.name || "Unknown"}</td>
                                <td>{enrollment.hours_per_week}</td>
                                <td>
                                    <button className={styles.deletebutton} onClick={() => handleDeleteEnrollment(enrollment.enrollment_id)}>Delete</button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            ) : (
                <p>There are no enrollments.</p>
            )}

            <button onClick={() => { setIsModalOpen(true); resetEnrollmentState(); }}>Add Enrollment</button>

            {/* Enrollment Modal */}
            <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)}>
                <h2>Add New Enrollment</h2>
                <label>
                    Course:
                    <select value={newEnrollment.course_id} onChange={(e) => setNewEnrollment({ ...newEnrollment, course_id: e.target.value })}>
                        <option value="">Select Course</option>
                        {courses.map(course => (
                            <option key={course.courseId} value={course.courseId}>{course.name}</option>
                        ))}
                    </select>
                </label>
                <label>Hours per Week:
                    <input
                        type="number"
                        value={newEnrollment.hours_per_week}
                        onChange={(e) => setNewEnrollment({ ...newEnrollment, hours_per_week: e.target.value })}
                    />
                </label>
                <button onClick={handleSaveEnrollmentChange}>Add Enrollment</button>
            </Modal>

            {/* Edit Student Modal */}
            <Modal isOpen={isEditModalOpen} onClose={() => setIsEditModalOpen(false)}>
                <h2>Edit Student</h2>
                <label>First Name:
                    <input type="text" value={updatedStudent.firstName} onChange={(e) => setUpdatedStudent({ ...updatedStudent, firstName: e.target.value })} />
                </label>
                <label>Last Name:
                    <input type="text" value={updatedStudent.lastName} onChange={(e) => setUpdatedStudent({ ...updatedStudent, lastName: e.target.value })} />
                </label>
                <label>Father Name:
                    <input type="text" value={updatedStudent.fatherName} onChange={(e) => setUpdatedStudent({ ...updatedStudent, fatherName: e.target.value })} />
                </label>
                <label>Phone:
                    <input type="text" value={updatedStudent.phone} onChange={(e) => setUpdatedStudent({ ...updatedStudent, phone: e.target.value })} />
                </label>
                <label>Email:
                    <input type="email" value={updatedStudent.email} onChange={(e) => setUpdatedStudent({ ...updatedStudent, email: e.target.value })} />
                </label>
                <button onClick={handleUpdate}>Save Changes</button>
            </Modal>

            <button onClick={() => navigate('/students')}>Back</button>
        </div>
    );
};
