package gameIO;

import controller.ChessController;
import controller.MainController;
import model.GameState;

public class CommandList {
    public static Command start;
    public static Command end;
    public static Command namePlayer;

    public static Command move;
    public static Command undoMove;

    public static Command replayRecord;

    public static Command saveGame;
    public static Command loadGame;

    public static void initialize(MainController mainController) {
        int id = -1;
        start = new Command("start", ++id,
                new GameState[]{GameState.NotStarted, GameState.GameOver},
                "Starts the chess game.");
        end  = new Command("end", ++id,
                new GameState[]{GameState.GameStarted},
                "Ends the chess game.");
        namePlayer = new Command("playerName", ++id,
                new GameState[]{GameState.NotStarted, GameState.GameOver},
                "Names the players.");

        move = new Command("move", ++id,
                new GameState[]{GameState.GameStarted},
                "Moves pieces of the current player.");
        undoMove = new Command("undo", ++id,
                new GameState[]{GameState.GameStarted},
                "Undo the last move by previous player, at most 3 moves for each players.");



    }
}
