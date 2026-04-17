package za.ac.mycput.studentenrolmentsystem.client;

import za.ac.cput.studentenrolmentsystem.common.Enrolment;
import java.io.*;
import java.net.*;
import za.ac.mycput.studentenrolmentsystem.Domain.*;
import za.ac.cput.studentenrolmentsystem.common.Request;
import za.ac.cput.studentenrolmentsystem.common.Response;

public class ClientApp {

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12345)) {
            System.out.println("Connected to server");

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            
            User user = new User("admin", "12345", "admin");
            out.writeObject(new Request("LOGIN", user));
            out.flush();

            Response res = (Response) in.readObject();
            System.out.println("Server: " + res.getMessage());

            
            out.writeObject(new Request("GET_COURSES", null));
            out.flush();

            res = (Response) in.readObject();
            System.out.println("Courses: " + res.getData());
            
            
            Enrolment enrollment = new Enrolment("S123", "ADP152S");
            out.writeObject(new Request("ENROLL_STUDENT", enrollment));
            out.flush();

            res = (Response) in.readObject();
            System.out.println("Server: " + res.getMessage());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
