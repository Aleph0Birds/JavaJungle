package controller;

import model.GameState;
import model.gameIO.Command;
import model.gameIO.CommandList;
import model.MainModel;
import view.MainView;

import java.util.Scanner;

public final class MainController extends Controller {
    private final Scanner scanner;
    private final RecordController recordControl;
    private final SaveController saveControl;
    private final ChessController chessController;

    public MainController(MainModel model, MainView view) {
        super(model, view);
        scanner = new Scanner(System.in);
        recordControl = new RecordController(model, view);
        saveControl = new SaveController(model, view);
        chessController = new ChessController(model, view);
    }

    public void initialize() {
        model.chessBoard.initChessBoard();
        CommandList.initialize(this);
    }

    /**
     * The main logical loop
     */
    public void startLoop() {
        while (true) {
            Command[] choices = CommandList.getCommands(model.gameState);
            view.displayActionChoices(model.gameState, choices);
            String[] input = scanner.nextLine().strip().split(" ");
            if (input.length == 0) continue;

            final Command targetCmd = parseInput(input[0], choices);
            if (targetCmd != null)
                targetCmd.invoke(input);
            else
                view.printMsg("Unknown command: " + input[0]);
        }
    }

    public ChessController chessControl() { return chessController; }
    public RecordController recordControl() { return recordControl; }
    public SaveController saveControl() { return saveControl; }

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

    @Override
    public void acceptCommand(Command command,  String... args) {

        switch (command.getKey()) {
            case "exit":
                view.printMsg("Exiting the game...");
                System.exit(0);
            return;
            case "start":
                view.printMsg("Starting the game...");
                model.gameState = GameState.GameStarted;
                view.displayBoard(model);
                break;
        }
    }

}

// categorize
// -- Chess Controller --
// 1 start new game
// 2 end ongoing game
// 3 player naming
// 4 ---cli--- already
// 5 status of game (chess board and turns)
// 6 taking back 3
// -- RecordController --
// 7 record game > file.record
// 8 replay the recorded game < file.record
// -- SaveController --
// 9 save game > file.jungle
// 10 load game < file.jungle
