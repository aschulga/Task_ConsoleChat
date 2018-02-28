package controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReadFromServer extends Thread {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final int EXIT = 3;
    private static final int MESSAGE = 4;

    private ClientController controller;

    public ReadFromServer(ClientController controller) {
        this.controller = controller;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String response = controller.getScanner().nextLine();
                String[] strings = response.split("/");

                switch (Integer.parseInt(strings[0])) {
                    case EXIT: {
                        controller.close();
                        break;
                    }
                    case MESSAGE: {
                        controller.receive(strings[1], strings[2], strings[3]);
                        break;
                    }
                }
            }
        } finally {
            controller.close();
        }
    }
}








