import controller.MainController;
import model.MainModel;
import view.MainView;

public class Main {
    public static void main(String[] args) {
        MainModel model = new MainModel();
        MainView view = new MainView();
        MainController controller = new MainController(model, view);

        controller.initialize();
        // starts the main loop
        controller.startLoop();
    }
}