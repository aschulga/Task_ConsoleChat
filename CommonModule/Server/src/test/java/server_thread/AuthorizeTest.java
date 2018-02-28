package server_thread;

import controller.ServerController;
import controller.ServerThread;
import model.ServerBase;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.Socket;

public class AuthorizeTest {

    private ServerController controller;
    private Socket socket;

    private void commonData() {
        ServerBase base = new ServerBase(8071);
        controller = new ServerController(base);
        socket = new Socket();
    }

    @Test
    public void authorizeAgent() throws IOException {
        commonData();
        int expectedSize = 1;
        new ServerThread(socket, controller).authorize("agent", "Alex");
        int size = controller.getListAgent().size();

        Assert.assertEquals(size, expectedSize);
        Assert.assertEquals(controller.getMapParameters().size(), 1);
        socket.close();
    }

    @Test
    public void authorizeClient() throws IOException {
        commonData();
        ServerBase base = new ServerBase(8071);
        controller = new ServerController(base);
        socket = new Socket();

        int expectedSize = 1;
        new ServerThread(socket, controller).authorize("client", "Alex");
        int size = controller.getListClient().size();

        Assert.assertEquals(size, expectedSize);
        Assert.assertEquals(controller.getMapParameters().size(), 1);
        socket.close();
    }

    @Test
    public void authorizePutInMapParameters() throws IOException {
        commonData();
        int expectedSize = 1;
        new ServerThread(socket, controller).authorize("client", "Alex");
        int size = controller.getMapParameters().size();

        Assert.assertEquals(size, expectedSize);
        Assert.assertEquals(controller.getMapParameters().size(), 1);
        socket.close();
    }
}
