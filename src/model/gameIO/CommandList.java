package model.gameIO;

import controller.MainController;
import model.GameState;

import java.util.ArrayList;

public class CommandList {
    public static Command exit;
    public static Command start;
    public static Command end;
    public static Command namePlayer;

    public static Command move;
    public static Command undoMove;

    public static Command replayRecord;

    public static Command saveGame;
    public static Command loadGame;

    private static Command[] commands;

    public static void initialize(MainController mainController) {

        start = new Command("start",
                new GameState[]{GameState.NotStarted, GameState.GameOver},
                "Starts the chess game.");
        end  = new Command("end",
                new GameState[]{GameState.GameStarted},
                "Ends the chess game.");
        namePlayer = new Command("name",
                new GameState[]{GameState.NotStarted, GameState.GameOver},
                "Names the players.",
                "name <red|black> <name>");

        move = new Command("move",
                new GameState[]{GameState.GameStarted},
                "Moves pieces of the current player.",
                "move <[piece name]|[cell index]> <up|down|left|right>");
        undoMove = new Command("undo",
                new GameState[]{GameState.GameStarted},
                "Undo the last move by previous player, at most 3 moves for each players.");

        replayRecord = new Command("replay",
                new GameState[]{GameState.NotStarted, GameState.GameOver},
                "Replays the game with the recorded .record file, can choose from a list of record files",
                "replay <list|[record file name]>");

        saveGame = new Command("save",
                new GameState[]{GameState.GameStarted},
                "Saves the current game into .jungle file.",
                "save <[save name]>");
        loadGame = new Command("load",
                new GameState[]{GameState.NotStarted, GameState.GameOver},
                "Loads the current game from .jungle file.",
                "load <[saved file name]>");

        exit = new Command("exit",
                GameState.values(),
                "Exits the game.");

        exit.bindController(mainController);
        start.bindController(mainController);
        end.bindController(mainController);
        namePlayer.bindController(mainController);

        move.bindController(mainController.chessControl());
        undoMove.bindController(mainController.chessControl());

        replayRecord.bindController(mainController.recordControl());

        saveGame.bindController(mainController.saveControl());
        loadGame.bindController(mainController.recordControl());

        commands = new Command[]{exit, start, end, namePlayer, move, undoMove, replayRecord, saveGame, loadGame};
    }

    // not in use
    public Command parseCommand(String key) {
        for (Command command : commands) {
            if(command.getKey().equals(key))
                return command;
        }

        return null;
    }

    public static Command[] getCommands(GameState gameState) {
        ArrayList<Command> commandList = new ArrayList<>();

        for (Command command : commands) {
            if (command.isStateAvailable(gameState))
                commandList.add(command);
        }

        return commandList.toArray(new Command[0]);
    }
}
