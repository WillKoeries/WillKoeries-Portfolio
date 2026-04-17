package za.ac.mycput.studentenrolmentsystem.client;

import za.ac.cput.studentenrolmentsystem.common.Request;
import za.ac.cput.studentenrolmentsystem.common.Response;

import java.io.*;
import java.net.Socket;

public class ClientConnection {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ClientConnection(String host, int port) throws IOException {
        socket = new Socket(host, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    public void sendRequest(Request request) throws IOException {
        out.writeObject(request);
        out.flush();
    }

    public Response receiveResponse() throws IOException, ClassNotFoundException {
        return (Response) in.readObject();
    }

    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
}
