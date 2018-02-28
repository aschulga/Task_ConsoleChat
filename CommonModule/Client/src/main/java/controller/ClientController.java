package controller;

import model.ClientBase;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import view.ClientConsole;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientController {

    private static final Logger LOGGER = LogManager.getLogger();

    private Socket socket = null;
    private ClientBase base;
    private Scanner reader;
    private PrintWriter writer;

    public ClientController(ClientBase base){
        this.base = base;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void connect() throws IOException {
       socket = new Socket(base.getHost(),base.getPort());
    }

    public void createThreads() throws IOException {
        reader = new Scanner(socket.getInputStream(), "UTF-8");
        writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
    }

    public Scanner getScanner() {
        return reader;
    }

    public void sendDataToServer(String data){
        writer.println(data);
    }

    public void receive(String status, String name, String message){
        LOGGER.log(Level.INFO, " - [" + status + " " + name + "] : " + message);
    }

    public void close(){
        if(writer != null){
            writer.close();
        }
        if(reader != null){
            reader.close();
        }
        try{
            if(socket != null){
                socket.close();
            }
        }catch(IOException e){
            LOGGER.catching(e);
        }

        ClientConsole.exit();
    }
}
