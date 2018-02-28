package model;

import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ServerBase {

    private List<Socket> listAgent = new LinkedList<>();
    private List<Socket> listClient = new LinkedList<>();
    private Map<Socket, Parameters<Socket, Boolean>> mapPair = new HashMap<>();
    private Map<Socket, Parameters<String, String>> mapParameters = new HashMap<>();
    private int port;

    public ServerBase(int port){
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public List<Socket> getListAgent() {
        return listAgent;
    }

    public List<Socket> getListClient() {
        return listClient;
    }

    public Map<Socket, Parameters<Socket, Boolean>> getMapPair() {
        return mapPair;
    }

    public Map<Socket, Parameters<String, String>> getMapParameters() {
        return mapParameters;
    }
}
