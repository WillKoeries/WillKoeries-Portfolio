package za.ac.cput.studentenrolmentsystem.common;

import java.io.Serializable;

/**
 *
 * @author Aidan
 */
import java.io.Serializable;

public class Student implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String student_id;
    private String name;
    private String surname;
    private String email;

    public Student(String student_id, String name, String surname, String email) {
        this.student_id = student_id;
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    // Getters and setters
    public String getStudent_id() { return student_id; }
    public void setStudent_id(String student_id) { this.student_id = student_id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return student_id + " - " + name + " " + surname + " (" + email + ")";
    }
}


