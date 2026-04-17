import za.ac.cput.studentenrolmentsystem.common.Course;
import za.ac.mycput.studentenrolmentsystem.Server.CourseDAO;

public class test {
    public static void main(String[] args) {
        CourseDAO dao = new CourseDAO();

        // Add a course
        Course c1 = new Course("ADP152S", "Application Development Practical 2");
        boolean added = dao.addCourse(c1);
        System.out.println("Course added: " + added);

        // Get all courses before deletion
        System.out.println("All courses before deletion:");
        for (Course c : dao.getAllCourses()) {
            System.out.println(c);
        }

        // Delete the course
        boolean deleted = dao.deleteCourse("ADP152S");
        System.out.println("Course deleted: " + deleted);

        // Verify deletion
        System.out.println("All courses after deletion:");
        for (Course c : dao.getAllCourses()) {
            System.out.println(c);
        }
    }
}
