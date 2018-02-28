package view;

import controller.ClientController;
import controller.ReadFromServer;
import controller.WriteToServer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ConnectException;

public class ClientConsole {

    private static final Logger LOGGER = LogManager.getLogger();
    private ClientController controller;

    public ClientConsole(ClientController controller)
    {
        this.controller = controller;
    }

    public void init(){

        try {
            controller.connect();
            controller.createThreads();
        }
        catch (ConnectException e){
            LOGGER.log(Level.INFO, " - Server is not running");
        }
        catch (IOException e) {
            LOGGER.catching(e);
        }

        ReadFromServer readFromServer = new ReadFromServer(controller);
        readFromServer.start();

        WriteToServer writeToServer = new WriteToServer(controller);
        writeToServer.send();
    }

    public static void exit(){
        System.exit(0);
    }
}
