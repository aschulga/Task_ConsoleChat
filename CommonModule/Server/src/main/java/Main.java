import controller.ServerController;
import model.ServerBase;
import view.ServerConsole;

public class Main {
    public static void main(String args[]){
        ServerBase base = new ServerBase(8071);
        ServerController controller = new ServerController(base);
        ServerConsole console = new ServerConsole(controller);
        console.init();
    }
}
