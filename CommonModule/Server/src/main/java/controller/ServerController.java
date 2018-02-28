package controller;

import model.Parameters;
import model.ServerBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;

public class ServerController {

    private static final Logger LOGGER = LogManager.getLogger();

    private ServerSocket serverSocket;
    private ServerBase base;

    public ServerController(ServerBase base){
        this.base = base;
    }

    public List<Socket> getListAgent(){
        return base.getListAgent();
    }

    public List<Socket> getListClient(){
        return base.getListClient();
    }

    public Map<Socket, Parameters<Socket, Boolean>> getMapPair() {
        return base.getMapPair();
    }

    public Map<Socket, Parameters<String, String>> getMapParameters() {
        return base.getMapParameters();
    }

    public void connect() throws IOException {
        serverSocket = new ServerSocket(base.getPort());
    }

    public void waitClients() throws IOException {
        while (true) {
            Socket socket = serverSocket.accept();
            ServerThread thread = new ServerThread(socket, this);
            thread.start();
        }
    }

    public void close(){
        try{
            if(serverSocket != null){
                serverSocket.close();
            }
        }catch(IOException e){
            LOGGER.catching(e);
        }
    }
}
