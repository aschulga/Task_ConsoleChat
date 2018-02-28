package view;

import controller.ServerController;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class ServerConsole {
    private static final Logger LOGGER = LogManager.getLogger();
    private ServerController controller;

    public ServerConsole(ServerController controller) {
        this.controller = controller;
    }

    public void init(){
        try {
            controller.connect();
            LOGGER.log(Level.INFO, " - START SERVER");
            controller.waitClients();
        } catch (IOException e) {
            LOGGER.catching(e);
            controller.close();
        }
    }
}
