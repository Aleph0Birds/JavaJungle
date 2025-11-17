import controller.MainController;
import model.GameState;
import model.MainModel;
import model.chess.Cell;
import model.chess.ChessBoard;
import model.gameIO.Command;
import model.gameIO.CommandList;
import org.junit.Test;
import view.MainView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

public class TestMain {
    @Test
    public void testMainLoop() {
        MainModel model = new MainModel();
        MainView view =  new  MainView();



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
        MainModel model = new MainModel();
        MainView view =  new MainView();
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
        MainModel model = new MainModel();
        MainView view =  new MainView();
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

    private Command parseInput(String input, Command[] commands) {
        try {
            final int num = Integer.parseInt(input) - 1;
            if (num < 0 || num > commands.length)
                throw new Exception();
            return commands[num];
        } catch (Exception e) {
            for  (Command command : commands) {
                if (command.getKey().equals(input.strip()))
                    return command;
            }
        }

        return null;
    }
}
