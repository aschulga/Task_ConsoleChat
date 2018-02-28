package server_thread;

import controller.ServerController;
import controller.ServerThread;
import model.Parameters;
import model.ServerBase;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.Socket;

public class CreatePairTest {

    private ServerController controller;
    private Socket socket;
    private ServerThread serverThread;

    private void commonData() {
        ServerBase base = new ServerBase(8071);
        controller = new ServerController(base);
        socket = new Socket();
        serverThread = new ServerThread(socket, controller);
    }

    @Test
    public void createPairListsAreEmpty() throws IOException {
        commonData();
        int expectedSize = 0;
        serverThread.createPair();
        int size = controller.getMapParameters().size();
        Assert.assertEquals(size, expectedSize);
        socket.close();
    }

    @Test
    public void createPair() throws IOException {
        commonData();
        int expectedSizeListAgent = 0;
        int expectedSizeListClient = 0;
        int expectedSizeMapPair = 2;
        Socket socket1 = new Socket();

        controller.getListClient().add(socket);
        controller.getListAgent().add(socket1);
        controller.getMapParameters().put(socket, new Parameters<>("client", "Alex"));
        controller.getMapParameters().put(socket1, new Parameters<>("agent", "Sergey"));

        serverThread.createPair();

        Assert.assertEquals(controller.getListClient().size(), expectedSizeListClient);
        Assert.assertEquals(controller.getListAgent().size(), expectedSizeListAgent);
        Assert.assertEquals(controller.getMapPair().size(), expectedSizeMapPair);
        socket1.close();
        socket.close();
    }
}
