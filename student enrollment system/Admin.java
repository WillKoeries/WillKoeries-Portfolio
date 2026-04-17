package za.ac.cput.studentenrolmentsystem.common;

import java.io.Serializable;
/**
 *
 * @author Aidan
 */
import java.io.Serializable;

public class Course implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String course_id;
    private String course_name;

    public Course(String course_id, String course_name) {
        this.course_id = course_id;
        this.course_name = course_name;
    }

    // Getters and setters
    public String getCourse_id() { return course_id; }
    public void setCourse_id(String course_id) { this.course_id = course_id; }
    public String getCourse_name() { return course_name; }
    public void setCourse_name(String course_name) { this.course_name = course_name; }

    @Override
    public String toString() {
        return course_id + " - " + course_name;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Course course = (Course) obj;
        return course_id.equals(course.course_id);
    }
    
    @Override
    public int hashCode() {
        return course_id.hashCode();
    }
}