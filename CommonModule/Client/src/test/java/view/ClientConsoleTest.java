package view;

import controller.ClientController;
import model.ClientBase;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;

public class ClientConsoleTest {

    private ClientBase base;
    private ClientController controller;
    private ClientConsole clientConsole;

    @BeforeClass
    public void createObjectClientConsole(){
        base = new ClientBase("localhost", 1211);
        controller = new ClientController(base);
        clientConsole = new ClientConsole(controller);
    }

    @Test(expectedExceptions = IOException.class)
    public void catchIOEceptions() throws IOException {
        controller.connect();
    }
}
