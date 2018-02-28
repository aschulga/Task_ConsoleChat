package server_thread;

import controller.ServerController;
import controller.ServerThread;
import model.Parameters;
import model.ServerBase;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.*;
import java.net.Socket;

public class ClientHandlingTest {

    private static final String COMMAND_LEAVE = "/leave";
    private static final String COMMAND_EXIT = "/exit";

    private boolean isClosed;
    private ServerBase base;
    private ServerController controller;
    private ServerThread serverThread;
    private String inputString;
    private ByteArrayOutputStream response;
    private Socket socket;

    private void commonData() {
        isClosed = false;
        base = new ServerBase(8071);
        controller = new ServerController(base);
        response = new ByteArrayOutputStream();

        socket = new Socket() {

            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(inputString.getBytes("UTF-8"));
            }

            @Override
            public void close(){
                isClosed = true;
            }

            @Override
            public OutputStream getOutputStream(){
                return response;
            }
        };

        serverThread = new ServerThread(socket, controller);
    }

    @Test
    public void clientHandlingAuthorizeClient(){
        commonData();
        int expectedSize = 1;
        inputString = "/register client Alex";
        serverThread.clientHandling();
        Assert.assertEquals(controller.getListClient().size(), expectedSize);
        Assert.assertTrue(isClosed);
    }

    @Test
    public void clientHandlingAuthorizeAgent(){
        commonData();
        int expectedSize = 1;
        inputString = "/register agent Luda";
        serverThread.clientHandling();
        Assert.assertEquals(controller.getListAgent().size(), expectedSize);
        Assert.assertTrue(isClosed);
    }

    @Test
    public void clientHandlingLeaveOneClientOneAgent() throws IOException {
        commonData();
        int expectedSize = 2;
        Socket socket1 = new Socket();

        controller.getMapParameters().put(socket, new Parameters<>("client", "Alex"));
        controller.getMapParameters().put(socket1, new Parameters<>("agent", "Sergey"));
        controller.getMapPair().put(socket, new Parameters<>(socket1, true));
        controller.getMapPair().put(socket1, new Parameters<>(socket, true));

        inputString = COMMAND_LEAVE;
        serverThread.clientHandling();
        Assert.assertEquals(controller.getMapPair().size(), expectedSize);
        Assert.assertTrue(isClosed);
        socket1.close();
    }

    @Test
    public void clientHandlingLeaveOneClientTwoAgent() throws IOException {
        commonData();
        Socket socket1 = new Socket();
        Socket socket2 = new Socket();

        controller.getMapParameters().put(socket, new Parameters<>("client", "Alex"));
        controller.getMapParameters().put(socket1, new Parameters<>("agent", "Luda"));
        controller.getListAgent().add(socket2);
        controller.getMapParameters().put(socket2, new Parameters<>("agent", "Sergey"));

        controller.getMapPair().put(socket, new Parameters<>(socket1, true));
        controller.getMapPair().put(socket1, new Parameters<>(socket, true));

        inputString = COMMAND_LEAVE;
        serverThread.clientHandling();
        Socket s = controller.getMapPair().get(socket).getParameter1();
        Assert.assertEquals(controller.getMapParameters().get(s).getParameter2(), "Sergey");
        Assert.assertTrue(isClosed);
        socket1.close();
        socket2.close();
    }

    @Test
    public void clientHandlingLeaveWithoutPair(){
        commonData();
        int expectedSize = 0;
        controller.getMapParameters().put(socket, new Parameters<>("client", "Sergey"));

        inputString = COMMAND_LEAVE;
        serverThread.clientHandling();
        Assert.assertEquals(controller.getMapPair().size(), expectedSize);
        Assert.assertTrue(isClosed);
    }

    @Test
    public void clientHandlingExitUser() throws IOException {
        commonData();
        controller.getMapParameters().put(socket, new Parameters<>("client", "Alex"));

        inputString = COMMAND_EXIT;
        serverThread.clientHandling();
        Assert.assertEquals("3\r\n", response.toString("UTF-8"));
        Assert.assertTrue(isClosed);
    }

    @Test
    public void clientHandlingSendMessageFromActivePair() throws IOException {
        commonData();
        ByteArrayOutputStream responseSecondUser = new ByteArrayOutputStream();
        Socket socket1 = new Socket() {

            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(inputString.getBytes("UTF-8"));
            }

            @Override
            public OutputStream getOutputStream(){
                return responseSecondUser;
            }
        };

        controller.getMapParameters().put(socket, new Parameters<>("client", "Luda"));
        controller.getMapParameters().put(socket1, new Parameters<>("agent", "Sergey"));
        controller.getMapPair().put(socket, new Parameters<>(socket1, true));
        controller.getMapPair().put(socket1, new Parameters<>(socket, false));

        inputString = "hello";
        serverThread.clientHandling();
        String[]strings = responseSecondUser.toString("UTF-8").split("/");

        Assert.assertEquals("4", strings[0]);
        Assert.assertEquals("client", strings[1]);
        Assert.assertEquals("Luda", strings[2]);
        Assert.assertEquals("hello\r\n", strings[3]);
        Assert.assertTrue(controller.getMapPair().get(socket1).getParameter2());
        socket1.close();
    }
}
