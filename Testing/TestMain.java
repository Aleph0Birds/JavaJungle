import controller.MainController;
import model.MainModel;
import model.gameIO.Command;
import model.gameIO.CommandList;
import org.junit.Before;
import org.junit.Test;
import view.MainView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class TestMain {
    MainModel model;
    MainView view;

    @Before
    public void setUp() {
        model = new MainModel();
        view = new MainView();
    }

    @Test
    public void testMainLoop() {
        Main.main(null);
        InputStream prevIn = System.in;
        System.setIn(new ByteArrayInputStream(
                ("0\nasd\nstart\nend\n1")
                        .getBytes()));

        MainController controller = new MainController(model, view);

        controller.initialize();
        controller.startLoop();
//        System.setIn(prevIn);
    }

    @Test
    public void testMainCommand() {
        MainController controller = new MainController(model, view);
        controller.initialize();

        //name
        namePlayerTest(controller);
        //start
        controller.acceptCommand(CommandList.start, "start");
        //end
        controller.acceptCommand(CommandList.end, "end");
    }

    @Test
    public void testMainExit() {
        MainController controller = new MainController(model, view);
        controller.initialize();

        controller.acceptCommand(CommandList.exit, "exit");
    }

    private static void namePlayerTest(MainController controller) {
        final Command command = CommandList.namePlayer;
        String[] args = {"name", };
        controller.acceptCommand(command, args);
        args = new String[]{"name", "red",};
        controller.acceptCommand(command, args);
        args = new String[]{"name", "red", "Villain"};
        controller.acceptCommand(command, args);
        args[1] = "black";
        controller.acceptCommand(command, args);
        args[1] = "red";
        args[2] = "哈基米#1f1e33";
        controller.acceptCommand(command, args);
        args[1] = "asd";
        controller.acceptCommand(command, args);
    }
}
