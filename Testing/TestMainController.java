import controller.MainController;
import model.GameState;
import model.MainModel;
import model.gameIO.Command;
import model.gameIO.CommandList;
import org.junit.Before;
import org.junit.Test;
import view.MainView;

import static org.junit.Assert.*;

public class TestMainController {
    private MainModel model;
    private MainView view;
    private MainController controller;

    @Before
    public void setUp() {
        model = new MainModel();
        view = new MainView();
        controller = new MainController(model, view);
        controller.initialize();
    }

    @Test
    public void testInitialize() {
        // Test that initialize sets up the model correctly
        assertNotNull("Model should not be null", model);
        assertNotNull("ChessBoard should be initialized", model.chessBoard);
        assertEquals("Initial game state should be NotStarted", GameState.NotStarted, model.gameState);
    }

    @Test
    public void testStartCommand() {
        // Create a start command and invoke it
        Command startCmd = new Command(
                "start",
                new GameState[]{GameState.NotStarted, GameState.GameOver},
                "Starts the chess game.",
                ""
        );
        startCmd.bindController(controller);

        controller.acceptCommand(startCmd);

        assertEquals("Game state should be GameStarted after start command",
                GameState.GameStarted, model.gameState);
        assertNotNull("ChessBoard should be initialized", model.chessBoard);
    }

    @Test
    public void testNameCommandRed() {
        Command nameCmd = new Command(
                "name",
                new GameState[]{GameState.NotStarted, GameState.GameOver},
                "Names the players.",
                "name <red|black> <[name]>"
        );
        nameCmd.bindController(controller);

        // Test naming red player
        controller.acceptCommand(nameCmd, "name", "red", "Alice");
        assertEquals("Red player name should be Alice", "Alice", model.playerRedName);
    }

    @Test
    public void testNameCommandBlack() {
        Command nameCmd = new Command(
                "name",
                new GameState[]{GameState.NotStarted, GameState.GameOver},
                "Names the players.",
                "name <red|black> <[name]>"
        );
        nameCmd.bindController(controller);

        // Test naming black player
        controller.acceptCommand(nameCmd, "name", "black", "Bob");
        assertEquals("Black player name should be Bob", "Bob", model.playerBlackName);
    }

    @Test
    public void testNameCommandInvalidTeam() {
        Command nameCmd = new Command(
                "name",
                new GameState[]{GameState.NotStarted, GameState.GameOver},
                "Names the players.",
                "name <red|black> <[name]>"
        );
        nameCmd.bindController(controller);

        String originalRedName = model.playerRedName;
        String originalBlackName = model.playerBlackName;

        // Test with invalid team name
        controller.acceptCommand(nameCmd, "name", "green", "Charlie");

        // Names should remain unchanged
        assertEquals("Red player name should not change", originalRedName, model.playerRedName);
        assertEquals("Black player name should not change", originalBlackName, model.playerBlackName);
    }

    @Test
    public void testNameCommandMissingArguments() {
        Command nameCmd = new Command(
                "name",
                new GameState[]{GameState.NotStarted, GameState.GameOver},
                "Names the players.",
                "name <red|black> <[name]>"
        );
        nameCmd.bindController(controller);

        String originalRedName = model.playerRedName;

        // Test with missing team argument
        controller.acceptCommand(nameCmd, "name");
        assertEquals("Red player name should not change", originalRedName, model.playerRedName);

        // Test with missing name argument
        controller.acceptCommand(nameCmd, "name", "red");
        assertEquals("Red player name should not change", originalRedName, model.playerRedName);
    }

    @Test
    public void testEndCommand() {
        // Start the game first
        Command startCmd = new Command(
                "start",
                new GameState[]{GameState.NotStarted, GameState.GameOver},
                "Starts the chess game.",
                ""
        );
        startCmd.bindController(controller);
        controller.acceptCommand(startCmd);

        // End the game
        Command endCmd = new Command(
                "end",
                new GameState[]{GameState.GameStarted},
                "Ends the chess game.",
                ""
        );
        endCmd.bindController(controller);
        controller.acceptCommand(endCmd);

        assertEquals("Game state should be GameOver after end command",
                GameState.GameOver, model.gameState);
    }

    @Test
    public void testDefaultPlayerNames() {
        assertEquals("Default red player name should be 'Red'", "Red", model.playerRedName);
        assertEquals("Default black player name should be 'Black'", "Black", model.playerBlackName);
    }

    @Test
    public void testGameStateTransitions() {
        // Initial state
        assertEquals("Initial state should be NotStarted", GameState.NotStarted, model.gameState);

        // Start game
        Command startCmd = new Command(
                "start",
                new GameState[]{GameState.NotStarted, GameState.GameOver},
                "Starts the chess game.",
                ""
        );
        startCmd.bindController(controller);
        controller.acceptCommand(startCmd);
        assertEquals("State should be GameStarted", GameState.GameStarted, model.gameState);

        // End game
        Command endCmd = new Command(
                "end",
                new GameState[]{GameState.GameStarted},
                "Ends the chess game.",
                ""
        );
        endCmd.bindController(controller);
        controller.acceptCommand(endCmd);
        assertEquals("State should be GameOver", GameState.GameOver, model.gameState);
    }

    @Test
    public void testControllerSubControllers() {
        // Test that sub-controllers are accessible
        assertNotNull("ChessController should not be null", controller.chessControl());
        assertNotNull("RecordController should not be null", controller.recordControl());
        assertNotNull("SaveController should not be null", controller.saveControl());
    }

    @Test
    public void testNameCommandCaseInsensitive() {
        Command nameCmd = new Command(
                "name",
                new GameState[]{GameState.NotStarted, GameState.GameOver},
                "Names the players.",
                "name <red|black> <[name]>"
        );
        nameCmd.bindController(controller);

        // Test with uppercase RED
        controller.acceptCommand(nameCmd, "name", "RED", "Alice");
        assertEquals("Red player name should be Alice", "Alice", model.playerRedName);

        // Test with mixed case BlAcK
        controller.acceptCommand(nameCmd, "name", "BlAcK", "Bob");
        assertEquals("Black player name should be Bob", "Bob", model.playerBlackName);
    }

    @Test
    public void testMultipleStartCommands() {
        Command startCmd = new Command(
                "start",
                new GameState[]{GameState.NotStarted, GameState.GameOver},
                "Starts the chess game.",
                ""
        );
        startCmd.bindController(controller);

        // Start game
        controller.acceptCommand(startCmd);
        assertEquals("Game should be started", GameState.GameStarted, model.gameState);

        // Start again - should reset the game
        controller.acceptCommand(startCmd);
        assertEquals("Game should still be started", GameState.GameStarted, model.gameState);
        assertEquals("Moves should be empty after restart", 0, model.moves.size());
    }
}

