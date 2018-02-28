package controller;

import model.Parameters;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerThread extends Thread {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final String COMMAND_REGISTER = "/register";
    private static final String COMMAND_LEAVE = "/leave";
    private static final String COMMAND_EXIT = "/exit";
    private static final int EXIT = 3;
    private static final int MESSAGE = 4;

    private Socket socket;
    private ServerController controller;

    public ServerThread(Socket socket, ServerController controller) {
        this.socket = socket;
        this.controller = controller;
    }

    public Socket findValue(Socket socket) {
        return controller.getMapPair().get(socket).getParameter1();
    }

    public void createPair(){
        if (!controller.getListClient().isEmpty() && !controller.getListAgent().isEmpty()) {

            Socket s1 = controller.getListClient().remove(0);
            Socket s2 = controller.getListAgent().remove(0);

            controller.getMapPair().put(s1, new Parameters<>(s2, true));
            controller.getMapPair().put(s2, new Parameters<>(s1, false));

            LOGGER.log(Level.INFO, " - The beginning of a chat between "+controller.getMapParameters().get(s1).getParameter1()+" "+
                    controller.getMapParameters().get(s1).getParameter2()+" and "+controller.getMapParameters().get(s2).getParameter1()+" "+
                    controller.getMapParameters().get(s2).getParameter2());
        }
    }

    public void authorize(String status, String name){
        if ("agent".equals(status)) {
            controller.getListAgent().add(socket);
        } else if ("client".equals(status)) {
            controller.getListClient().add(socket);
        }

        LOGGER.log(Level.INFO, " - The appearance of "+status+" "+name+" in the system");

        controller.getMapParameters().put(socket, new Parameters<>(status, name));
        createPair();
    }

    public void leave(){
        Socket s = findValue(socket);

        controller.getListAgent().add(s);
        controller.getListClient().add(socket);

        LOGGER.log(Level.INFO, " - The end of the chat between "+controller.getMapParameters().get(socket).getParameter1()+" "+
                controller.getMapParameters().get(socket).getParameter2()+" and "+controller.getMapParameters().get(s).getParameter1()+" "+
                controller.getMapParameters().get(s).getParameter2());

        controller.getMapPair().remove(s);
        controller.getMapPair().remove(socket);
        createPair();
    }

    public void exit() {
        if (controller.getMapPair().containsKey(socket) == true) {

            if ("client".equals(controller.getMapParameters().get(socket).getParameter1()))
                controller.getListAgent().add(findValue(socket));
            else
                controller.getListClient().add(findValue(socket));

            LOGGER.log(Level.INFO, " - The "+controller.getMapParameters().get(socket).getParameter1()+" "+
                    controller.getMapParameters().get(socket).getParameter2()+" left the system");

            Socket s = findValue(socket);

            LOGGER.log(Level.INFO, " - The end of the chat between "+controller.getMapParameters().get(socket).getParameter1()+" "+
                    controller.getMapParameters().get(socket).getParameter2()+" and "+controller.getMapParameters().get(s).getParameter1()+" "+
                    controller.getMapParameters().get(s).getParameter2());

            controller.getMapPair().remove(s);
            controller.getMapPair().remove(socket);
            controller.getMapParameters().remove(socket);
            createPair();
        }
        else if(controller.getMapParameters().containsKey(socket)){
            LOGGER.log(Level.INFO, " - The "+controller.getMapParameters().get(socket).getParameter1()+" "+
                    controller.getMapParameters().get(socket).getParameter2()+" left the system");

            if("client".equals(controller.getMapParameters().get(socket).getParameter1()))
                controller.getListClient().remove(socket);
            else
                controller.getListAgent().remove(socket);

            controller.getMapParameters().remove(socket);
        }
    }

    public String createName(String[] strings){
        String name = "";
        for(int i = 2; i < strings.length; i++)
            name += strings[i]+" ";
        return name.trim();
    }

    @Override
    public void run() {
        clientHandling();
    }

    public synchronized void clientHandling(){
        Scanner reader = null;
        PrintWriter writer = null;
        String request;
        try {

            reader = new Scanner(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);

            while (reader.hasNextLine()) {

                request = reader.nextLine();
                String[]strings = request.split(" ");

                if(strings.length > 3){
                    strings[2] = createName(strings);
                }

                if (COMMAND_REGISTER.equals(strings[0])) {
                    authorize(strings[1], strings[2]);
                }
                else if (COMMAND_LEAVE.equals(strings[0]) && "client".equals(controller.getMapParameters().get(socket).getParameter1())){
                    if (controller.getMapPair().containsKey(socket)) {
                        leave();
                    }
                }
                else if (COMMAND_EXIT.equals(strings[0])) {
                    exit();
                    writer.println(EXIT);
                }
                else{
                    while (true) {
                        if (controller.getMapPair().containsKey(socket)) {
                            if(controller.getMapPair().get(socket).getParameter2()) {
                                sendMessageToUser(findValue(socket), request);
                                controller.getMapPair().get(findValue(socket)).setParameter2(true);
                            }
                            break;
                        }
                    }
                }
            }
        }catch (IOException e) {
            exit();
            LOGGER.catching(e);
        }
        finally {
            disconnect(writer, reader);
        }
    }

    public void sendMessageToUser(Socket socket, String str) throws IOException {
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);

        writer.println(MESSAGE + "/"+controller.getMapParameters().get(findValue(socket)).getParameter1()+"/"+
                controller.getMapParameters().get(findValue(socket)).getParameter2()+"/"+str);

    }

    public void disconnect(PrintWriter writer, Scanner scanner) {
        if(writer != null){
            writer.close();
        }
        if(scanner != null){
            scanner.close();
        }
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                LOGGER.catching(e);
            }
        }
    }
}