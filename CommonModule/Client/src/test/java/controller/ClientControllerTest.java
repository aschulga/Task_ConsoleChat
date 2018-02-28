package controller;

import model.ClientBase;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientControllerTest {
    private ClientBase base;
    private ClientController controller;
    private String inputString;
    private ByteArrayOutputStream response;
    private Socket socket;
    private ServerSocket serverSocket;

    private void commonData() throws IOException {
        serverSocket = new ServerSocket(1111);
        base = new ClientBase("localhost",1111);
        controller = new ClientController(base);
        response = new ByteArrayOutputStream();

        socket = new Socket() {

            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(inputString.getBytes("UTF-8"));
            }

            @Override
            public OutputStream getOutputStream(){
                return response;
            }
        };

    }

    @Test
    public void sendDataToServer() throws IOException {
        commonData();
        inputString = "hello";
        controller.setSocket(socket);
        controller.createThreads();
        controller.sendDataToServer(inputString);
        Assert.assertEquals("hello\r\n", response.toString("UTF-8"));

        socket.close();
        serverSocket.close();
        response.close();
    }
}
