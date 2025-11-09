import controller.MainController;
import model.GameState;
import model.MainModel;
import model.chess.Cell;
import model.chess.ChessBoard;
import model.chess.Piece;
import model.gameIO.Command;
import model.gameIO.CommandList;
import org.junit.Test;
import view.MainView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

public class TestChess {

    @Test
    public void testMove(){
        MainModel model = new MainModel();
        MainView view =  new  MainView();
        MainController controller = new MainController(model, view);

        controller.initialize();
        final ChessBoard chessBoard = model.chessBoard;

        Cell redLion = chessBoard.getCell(0, 6);
        chessBoard.getCell(2, 1).setPiece(redLion.getPiece());
        redLion.setPiece(null);

        model.gameState = GameState.NotStarted;

        InputStream prevIn = System.in;
        System.setIn(new ByteArrayInputStream(
                ("""
                    2
                    undo
                    3 b3 up
                    3 f8 up
                    3 g3 left
                    3 B8 left
                    3 e3 down
                    3 c7 left
                    undo
                    undo
                    undo
                    undo
                    undo
                    undo
                    undo
                    3 b3 up
                    3 a7 up
                    undo
                    3 g3 up
                    undo
                    """)
                        .getBytes()));
//        System.setIn(prevIn);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            Command[] choices = CommandList.getCommands(model.gameState);
            view.displayActionChoices(model.gameState, choices);
            String input = scanner.nextLine();
            System.out.println(input);
            String[] inputs = input.strip().split(" ");
            if (inputs.length == 0 || inputs[0].isEmpty()) continue;

            final Command targetCmd = parseInput(inputs[0], choices);
            if (targetCmd != null)
                targetCmd.invoke(inputs);
            else
                view.printErr("Unknown command: " + inputs[0]);
            if (!scanner.hasNextLine()) {
                System.setIn(prevIn);
            }
        }
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
