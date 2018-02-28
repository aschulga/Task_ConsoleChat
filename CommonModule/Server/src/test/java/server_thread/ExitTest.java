package server_thread;

import controller.ServerController;
import controller.ServerThread;
import model.Parameters;
import model.ServerBase;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.Socket;

public class ExitTest {
    private ServerBase base;
    private ServerController controller;
    private Socket socket;
    private ServerThread serverThread;

    private void commonData() {
        base = new ServerBase(8071);
        controller = new ServerController(base);
        socket = new Socket();
        serverThread = new ServerThread(socket, controller);
    }

    @Test
    public void exitClient() throws IOException {
        commonData();
        int expectedSize = 0;
        controller.getListClient().add(socket);
        controller.getMapParameters().put(socket, new Parameters<>("client", "Alex"));

        serverThread.exit();

        Assert.assertEquals(controller.getListClient().size(), expectedSize);
        Assert.assertEquals(controller.getMapParameters().size(), expectedSize);
        socket.close();
    }

    @Test
    public void exitAgent() throws IOException {
        commonData();
        int expectedSize = 0;
        controller.getListAgent().add(socket);
        controller.getMapParameters().put(socket, new Parameters<>("agent", "Sergey"));

        serverThread.exit();

        Assert.assertEquals(controller.getListAgent().size(), expectedSize);
        Assert.assertEquals(controller.getMapParameters().size(), expectedSize);
        socket.close();
    }

    @Test
    public void exitClientInPair() throws IOException {
        commonData();
        int expectedSizeListClient = 0;
        int expectedSizeListAgent = 1;
        int expectedSizeMapParameters = 1;
        int expectedSizeMapPair = 0;
        Socket socket1 = new Socket();

        controller.getMapParameters().put(socket, new Parameters<>("client", "Alex"));
        controller.getMapParameters().put(socket1, new Parameters<>("agent", "Sergey"));
        controller.getMapPair().put(socket, new Parameters<>(socket1, true));
        controller.getMapPair().put(socket1, new Parameters<>(socket, true));

        serverThread.exit();

        Assert.assertEquals(controller.getListClient().size(), expectedSizeListClient);
        Assert.assertEquals(controller.getListAgent().size(), expectedSizeListAgent);
        Assert.assertEquals(controller.getMapParameters().size(), expectedSizeMapParameters);
        Assert.assertEquals(controller.getMapPair().size(), expectedSizeMapPair);
        socket1.close();
        socket.close();
    }

    @Test
    public void exitAgentInPair() throws IOException {
        commonData();
        int expectedSizeListClient = 1;
        int expectedSizeListAgent = 0;
        int expectedSizeMapParameters = 1;
        int expectedSizeMapPair = 0;
        Socket socket1 = new Socket();

        controller.getMapParameters().put(socket, new Parameters<>("agent", "Alex"));
        controller.getMapParameters().put(socket1, new Parameters<>("client", "Sergey"));
        controller.getMapPair().put(socket, new Parameters<>(socket1, true));
        controller.getMapPair().put(socket1, new Parameters<>(socket, true));

        serverThread.exit();

        Assert.assertEquals(controller.getListClient().size(), expectedSizeListClient);
        Assert.assertEquals(controller.getListAgent().size(), expectedSizeListAgent);
        Assert.assertEquals(controller.getMapParameters().size(), expectedSizeMapParameters);
        Assert.assertEquals(controller.getMapPair().size(), expectedSizeMapPair);
        socket1.close();
        socket.close();
    }
}
