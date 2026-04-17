package za.ac.cput.studentenrolmentsystem.common;


import java.io.Serializable;

/**
 *
 * @author Aidan
 */
import java.io.Serializable;

public class Enrolment implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String student_id;
    private String course_id;

    public Enrolment(String student_id, String course_id) {
        this.student_id = student_id;
        this.course_id = course_id;
    }

    // Getters and setters
    public String getStudent_id() { return student_id; }
    public void setStudent_id(String student_id) { this.student_id = student_id; }
    public String getCourse_id() { return course_id; }
    public void setCourse_id(String course_id) { this.course_id = course_id; }

    @Override
    public String toString() {
        return "Student: " + student_id + " → Course: " + course_id;
    }
}