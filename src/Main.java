import Controller.MainController;
import Model.MainModel;
import View.MainView;

public class Main {
    public static void main(String[] args) {
        MainModel model = MainModel.getInstance();
        MainView view = MainView.getInstance();
        MainController controller = new MainController(model, view);

        controller.initialize();
        // starts the main loop
        controller.startLoop();
    }
}