package za.ac.mycput.studentenrolmentsystem.client.GUI;

import za.ac.cput.studentenrolmentsystem.common.Response;
import za.ac.cput.studentenrolmentsystem.common.Request;
import za.ac.cput.studentenrolmentsystem.common.Enrolment;
import za.ac.cput.studentenrolmentsystem.common.Course;
import za.ac.mycput.studentenrolmentsystem.Domain.*;
import za.ac.cput.studentenrolmentsystem.common.Student;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.List;

public class Admin extends JFrame {

    private User loggedIn;

    public Admin(User user) {
        this.loggedIn = user;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Admin Dashboard - " + loggedIn.getUsername());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Student Management Tab
        tabbedPane.addTab("Student Management", createStudentPanel());

        // Course Management Tab
        tabbedPane.addTab("Course Management", createCoursePanel());

        // Enrollment Management Tab
        tabbedPane.addTab("Enrollment Management", createEnrollmentPanel());

        // User Management Tab
        tabbedPane.addTab("User Management", createUserPanel());

        add(tabbedPane);
    }

    private JPanel createStudentPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Buttons
        JButton addStudentBtn = new JButton("Add Student");
        JButton viewStudentsBtn = new JButton("View All Students");
        JButton updateStudentBtn = new JButton("Update Student");
        JButton deleteStudentBtn = new JButton("Delete Student");

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addStudentBtn);
        buttonPanel.add(viewStudentsBtn);
        buttonPanel.add(updateStudentBtn);
        buttonPanel.add(deleteStudentBtn);

        // Text area for display
        JTextArea displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Event handlers
        addStudentBtn.addActionListener(e -> addStudent());
        viewStudentsBtn.addActionListener(e -> viewAllStudents(displayArea));
        updateStudentBtn.addActionListener(e -> updateStudent());
        deleteStudentBtn.addActionListener(e -> deleteStudent());

        return panel;
    }

    private JPanel createCoursePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Buttons
        JButton addCourseBtn = new JButton("Add Course");
        JButton viewCoursesBtn = new JButton("View All Courses");
        JButton updateCourseBtn = new JButton("Update Course");
        JButton deleteCourseBtn = new JButton("Delete Course");

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addCourseBtn);
        buttonPanel.add(viewCoursesBtn);
        buttonPanel.add(updateCourseBtn);
        buttonPanel.add(deleteCourseBtn);

        // Text area for display
        JTextArea displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Event handlers
        addCourseBtn.addActionListener(e -> addCourse());
        viewCoursesBtn.addActionListener(e -> viewAllCourses(displayArea));
        updateCourseBtn.addActionListener(e -> updateCourse());
        deleteCourseBtn.addActionListener(e -> deleteCourse());

        return panel;
    }

    private JPanel createEnrollmentPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Buttons
        JButton enrollBtn = new JButton("Enroll Student");
        JButton viewByStudentBtn = new JButton("View by Student");
        JButton viewByCourseBtn = new JButton("View by Course");
        JButton removeEnrollmentBtn = new JButton("Remove Enrollment");

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(enrollBtn);
        buttonPanel.add(viewByStudentBtn);
        buttonPanel.add(viewByCourseBtn);
        buttonPanel.add(removeEnrollmentBtn);

        // Text area for display
        JTextArea displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Event handlers
        enrollBtn.addActionListener(e -> enrollStudent());
        viewByStudentBtn.addActionListener(e -> viewEnrollmentsByStudent(displayArea));
        viewByCourseBtn.addActionListener(e -> viewEnrollmentsByCourse(displayArea));
        removeEnrollmentBtn.addActionListener(e -> removeEnrollment());

        return panel;
    }

    private JPanel createUserPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Buttons
        JButton addUserBtn = new JButton("Add User");
        JButton viewUsersBtn = new JButton("View All Users");
        JButton deleteUserBtn = new JButton("Delete User");

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addUserBtn);
        buttonPanel.add(viewUsersBtn);
        buttonPanel.add(deleteUserBtn);

        // Text area for display
        JTextArea displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Event handlers
        addUserBtn.addActionListener(e -> addUser());
        viewUsersBtn.addActionListener(e -> viewAllUsers(displayArea));
        deleteUserBtn.addActionListener(e -> deleteUser());

        return panel;
    }

    // Student Management Methods
    private void addStudent() {
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField surnameField = new JTextField();
        JTextField emailField = new JTextField();

        Object[] message = {
            "Student ID:", idField,
            "Name:", nameField,
            "Surname:", surnameField,
            "Email:", emailField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add Student", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String surname = surnameField.getText().trim();
            String email = emailField.getText().trim();

            if (id.isEmpty() || name.isEmpty() || surname.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Student student = new Student(id, name, surname, email);
            sendRequest(new Request("ADD_STUDENT", student), "Student added successfully!");
        }
    }

    private void updateStudent() {
        String studentId = JOptionPane.showInputDialog(this, "Enter Student ID to update:");
        if (studentId != null && !studentId.trim().isEmpty()) {
            // First get the current student data
            try (Socket socket = new Socket("localhost", 12345)) {
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

                out.writeObject(new Request("GET_STUDENT_BY_ID", studentId));
                out.flush();
                Response res = (Response) in.readObject();

                if (res.isSuccess()) {
                    Student currentStudent = (Student) res.getData();
                    
                    JTextField nameField = new JTextField(currentStudent.getName());
                    JTextField surnameField = new JTextField(currentStudent.getSurname());
                    JTextField emailField = new JTextField(currentStudent.getEmail());

                    Object[] message = {
                        "Name:", nameField,
                        "Surname:", surnameField,
                        "Email:", emailField
                    };

                    int option = JOptionPane.showConfirmDialog(this, message, "Update Student", JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {
                        String name = nameField.getText().trim();
                        String surname = surnameField.getText().trim();
                        String email = emailField.getText().trim();

                        if (name.isEmpty() || surname.isEmpty() || email.isEmpty()) {
                            JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        Student updatedStudent = new Student(studentId, name, surname, email);
                        sendRequest(new Request("UPDATE_STUDENT", updatedStudent), "Student updated successfully!");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Student not found!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Connection error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteStudent() {
        String studentId = JOptionPane.showInputDialog(this, "Enter Student ID to delete:");
        if (studentId != null && !studentId.trim().isEmpty()) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete student " + studentId + "? This will also remove all their enrollments.",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                sendRequest(new Request("DELETE_STUDENT", studentId), "Student deleted successfully!");
            }
        }
    }

    // Course Management Methods
    private void addCourse() {
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();

        Object[] message = {
            "Course ID:", idField,
            "Course Name:", nameField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add Course", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();

            if (id.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Course course = new Course(id, name);
            sendRequest(new Request("ADD_COURSE", course), "Course added successfully!");
        }
    }

    private void updateCourse() {
        String courseId = JOptionPane.showInputDialog(this, "Enter Course ID to update:");
        if (courseId != null && !courseId.trim().isEmpty()) {
            // First get the current course data
            try (Socket socket = new Socket("localhost", 12345)) {
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

                out.writeObject(new Request("GET_COURSE_BY_ID", courseId));
                out.flush();
                Response res = (Response) in.readObject();

                if (res.isSuccess()) {
                    Course currentCourse = (Course) res.getData();
                    
                    JTextField nameField = new JTextField(currentCourse.getCourse_name());

                    Object[] message = {
                        "Course Name:", nameField
                    };

                    int option = JOptionPane.showConfirmDialog(this, message, "Update Course", JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {
                        String name = nameField.getText().trim();

                        if (name.isEmpty()) {
                            JOptionPane.showMessageDialog(this, "Course name is required!", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        Course updatedCourse = new Course(courseId, name);
                        sendRequest(new Request("UPDATE_COURSE", updatedCourse), "Course updated successfully!");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Course not found!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Connection error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteCourse() {
        String courseId = JOptionPane.showInputDialog(this, "Enter Course ID to delete:");
        if (courseId != null && !courseId.trim().isEmpty()) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete course " + courseId + "? This will also remove all enrollments for this course.",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                sendRequest(new Request("DELETE_COURSE", courseId), "Course deleted successfully!");
            }
        }
    }

    // Enrollment Management Methods
    private void enrollStudent() {
        JTextField studentIdField = new JTextField();
        JTextField courseIdField = new JTextField();

        Object[] message = {
            "Student ID:", studentIdField,
            "Course ID:", courseIdField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Enroll Student", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String studentId = studentIdField.getText().trim();
            String courseId = courseIdField.getText().trim();

            if (studentId.isEmpty() || courseId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Both fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Enrolment enrollment = new Enrolment(studentId, courseId);
            sendRequest(new Request("ENROLL_STUDENT", enrollment), "Student enrolled successfully!");
        }
    }

    private void removeEnrollment() {
        JTextField studentIdField = new JTextField();
        JTextField courseIdField = new JTextField();

        Object[] message = {
            "Student ID:", studentIdField,
            "Course ID:", courseIdField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Remove Enrollment", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String studentId = studentIdField.getText().trim();
            String courseId = courseIdField.getText().trim();

            if (studentId.isEmpty() || courseId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Both fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Enrolment enrollment = new Enrolment(studentId, courseId);
            sendRequest(new Request("REMOVE_ENROLLMENT", enrollment), "Enrollment removed successfully!");
        }
    }

    // User Management Methods
    private void addUser() {
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"admin", "student"});

        Object[] message = {
            "Username:", usernameField,
            "Password:", passwordField,
            "Role:", roleComboBox
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add User", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String role = (String) roleComboBox.getSelectedItem();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username and password are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            User user = new User(username, password, role);
            sendRequest(new Request("ADD_USER", user), "User added successfully!");
        }
    }

    private void deleteUser() {
        String username = JOptionPane.showInputDialog(this, "Enter Username to delete:");
        if (username != null && !username.trim().isEmpty()) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete user " + username + "?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                sendRequest(new Request("DELETE_USER", username), "User deleted successfully!");
            }
        }
    }

    // View Methods
    private void viewAllStudents(JTextArea displayArea) {
        sendRequest(new Request("GET_STUDENTS", null), displayArea, "students");
    }

    private void viewAllCourses(JTextArea displayArea) {
        sendRequest(new Request("GET_COURSES", null), displayArea, "courses");
    }

    private void viewAllUsers(JTextArea displayArea) {
        sendRequest(new Request("GET_USERS", null), displayArea, "users");
    }

    private void viewEnrollmentsByStudent(JTextArea displayArea) {
        String studentId = JOptionPane.showInputDialog(this, "Enter Student ID:");
        if (studentId != null && !studentId.trim().isEmpty()) {
            sendRequest(new Request("GET_ENROLLMENTS_BY_STUDENT", studentId), displayArea, "enrollments");
        }
    }

    private void viewEnrollmentsByCourse(JTextArea displayArea) {
        String courseId = JOptionPane.showInputDialog(this, "Enter Course ID:");
        if (courseId != null && !courseId.trim().isEmpty()) {
            sendRequest(new Request("GET_ENROLLMENTS_BY_COURSE", courseId), displayArea, "enrollments");
        }
    }

    // Utility Methods
    private void sendRequest(Request req, String successMsg) {
        try (Socket socket = new Socket("localhost", 12345)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(req);
            out.flush();
            Response res = (Response) in.readObject();

            if (res.isSuccess()) {
                JOptionPane.showMessageDialog(this, successMsg, "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, res.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Connection error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    private void sendRequest(Request req, JTextArea displayArea, String type) {
        try (Socket socket = new Socket("localhost", 12345)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(req);
            out.flush();
            Response res = (Response) in.readObject();

            if (res.isSuccess()) {
                java.util.List<Object> data = (java.util.List<Object>) res.getData();
                StringBuilder sb = new StringBuilder();
                sb.append("Total ").append(type).append(": ").append(data.size()).append("\n\n");

                for (Object item : data) {
                    sb.append(item.toString()).append("\n");
                }

                displayArea.setText(sb.toString());
            } else {
                displayArea.setText("Error: " + res.getMessage());
            }

        } catch (Exception e) {
            displayArea.setText("Connection error: " + e.getMessage());
        }
    }
}