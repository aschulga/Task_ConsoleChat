package model;

public class ClientBase {

    private String host;
    private int port;

    public ClientBase(String host, int port){
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
