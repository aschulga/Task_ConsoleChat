import controller.ClientController;
import model.ClientBase;
import view.ClientConsole;

public class Main {
    public static void main(String args[]){
        ClientBase base = new ClientBase("localhost", 8071);
        ClientController controller = new ClientController(base);
        ClientConsole console = new ClientConsole(controller);
        console.init();
    }
}
