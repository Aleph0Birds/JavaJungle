import controller.MainController;
import controller.SaveController;
import model.GameState;
import model.MainModel;
import model.chess.Piece;
import model.chess.Team;
import model.chess.Vec2;
import model.gameIO.Command;
import model.gameIO.SaveLoad;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import view.MainView;

import java.io.*;
import java.nio.file.Files;

import static org.junit.Assert.*;

public class TestSaveController {
    private MainModel model;
    private MainView view;
    private SaveController saveController;
    private MainController mainController;

    @Before
    public void setUp() throws IOException {
        model = new MainModel();
        view = new MainView();
        mainController = new MainController(model, view);
        mainController.initialize();
        saveController = mainController.saveControl();

        // Start a game
        model.gameState = GameState.GameStarted;
        model.chessBoard.initChessBoard();
    }

    @After
    public void tearDown() throws IOException {
        // Clean up test save files
        if (Files.exists(SaveLoad.savePath)) {
            Files.list(SaveLoad.savePath)
                    .filter(path -> path.toString().endsWith(".sav"))
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }
    }

    @Test
    public void testSaveGame() throws IOException {
        Command saveCmd = new Command(
                "save",
                new GameState[]{GameState.GameStarted},
                "Saves the game.",
                "save <[filename]>"
        );
        saveCmd.bindController(saveController);

        // Save with a custom filename
        saveController.acceptCommand(saveCmd, "save", "test_save");

        // Verify save file was created
        String[] files = SaveLoad.getFileNames(false);
        boolean found = false;
        for (String file : files) {
            if (file.contains("test_save")) {
                found = true;
                break;
            }
        }
        assertTrue("Save file should be created", found);
    }

    @Test
    public void testSaveGameAutoFilename() throws IOException {
        Command saveCmd = new Command(
                "save",
                new GameState[]{GameState.GameStarted},
                "Saves the game.",
                "save <[filename]>"
        );
        saveCmd.bindController(saveController);

        // Save without filename (auto-generate)
        saveController.acceptCommand(saveCmd, "save");

        // Verify a save file was created
        String[] files = SaveLoad.getFileNames(false);
        assertTrue("At least one save file should exist", files.length > 0);
    }

    @Test
    public void testSaveGameFileFormat() throws IOException {
        Command saveCmd = new Command(
                "save",
                new GameState[]{GameState.GameStarted},
                "Saves the game.",
                "save <[filename]>"
        );
        saveCmd.bindController(saveController);

        // Setup game state
        model.playerRedName = "SaveTestRed";
        model.playerBlackName = "SaveTestBlack";
        model.turn = Team.RED;

        // Save the game
        saveController.acceptCommand(saveCmd, "save", "format_test");

        // Read and verify format
        String[] files = SaveLoad.getFileNames(false);
        String targetFile = null;
        for (String file : files) {
            if (file.contains("format_test")) {
                targetFile = file;
                break;
            }
        }
        assertNotNull("Save file should exist", targetFile);

        try (BufferedReader reader = SaveLoad.getReader(targetFile, false)) {
            String firstLine = reader.readLine();
            assertNotNull("First line should exist", firstLine);

            // First line should contain: playerRedName playerBlackName turn
            String[] parts = firstLine.split(" ");
            assertEquals("Should have 3 parts", 3, parts.length);
            assertEquals("Red player name should match", "SaveTestRed", parts[0]);
            assertEquals("Black player name should match", "SaveTestBlack", parts[1]);
            assertEquals("Turn should be RED", "RED", parts[2]);
        }
    }

    @Test
    public void testLoadGameList() {
        Command loadCmd = new Command(
                "load",
                new GameState[]{GameState.NotStarted, GameState.GameOver},
                "Loads a saved game.",
                "load <[filename]>"
        );
        loadCmd.bindController(saveController);

        // Call load without arguments (should list available saves)
        saveController.acceptCommand(loadCmd, "load");
        // This should not throw an exception
        // If no saves exist, it should print an error message
    }

    @Test
    public void testSaveAndLoadGame() throws IOException {
        Command saveCmd = new Command(
                "save",
                new GameState[]{GameState.GameStarted},
                "Saves the game.",
                "save <[filename]>"
        );
        saveCmd.bindController(saveController);
        
        Command loadCmd = new Command(
                "load",
                new GameState[]{GameState.NotStarted, GameState.GameOver},
                "Loads a saved game.",
                "load <[filename]>"
        );
        loadCmd.bindController(saveController);

        // Setup a specific game state
        model.playerRedName = "Alice";
        model.playerBlackName = "Bob";
        model.turn = Team.BLACK;

        // Save the game
        saveController.acceptCommand(saveCmd, "save", "load_test");

        // Modify the model
        model.playerRedName = "ChangedRed";
        model.playerBlackName = "ChangedBlack";
        model.turn = Team.RED;

        // Load the game
        saveController.acceptCommand(loadCmd, "load", "load_test");

        // Verify the loaded state
        assertEquals("Red player name should be restored", "Alice", model.playerRedName);
        assertEquals("Black player name should be restored", "Bob", model.playerBlackName);
        assertEquals("Turn should be restored", Team.BLACK, model.turn);
        assertEquals("Game state should be GameStarted", GameState.GameStarted, model.gameState);
    }

    @Test
    public void testLoadNonExistentFile() {
        Command loadCmd = new Command(
                "load",
                new GameState[]{GameState.NotStarted, GameState.GameOver},
                "Loads a saved game.",
                "load <[filename]>"
        );
        loadCmd.bindController(saveController);

        // Try to load a file that doesn't exist
        saveController.acceptCommand(loadCmd, "load", "nonexistent_save_file");
        // Should handle gracefully without crashing
    }

    @Test
    public void testSaveGameWithAllPieces() throws IOException {
        Command saveCmd = new Command(
                "save",
                new GameState[]{GameState.GameStarted},
                "Saves the game.",
                "save <[filename]>"
        );
        saveCmd.bindController(saveController);

        // Initialize chess board with all pieces
        model.chessBoard.initChessBoard();

        // Save
        saveController.acceptCommand(saveCmd, "save", "all_pieces_test");

        // Read and count pieces
        String[] files = SaveLoad.getFileNames(false);
        String targetFile = null;
        for (String file : files) {
            if (file.contains("all_pieces_test")) {
                targetFile = file;
                break;
            }
        }
        assertNotNull("Save file should exist", targetFile);

        try (BufferedReader reader = SaveLoad.getReader(targetFile, false)) {
            reader.readLine(); // Skip metadata line

            int pieceCount = 0;
            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                pieceCount++;
            }
            assertEquals("Should save 16 pieces (8 red + 8 black)", 16, pieceCount);
        }
    }

    @Test
    public void testLoadGameRestoresBoard() throws IOException {
        Command saveCmd = new Command(
                "save",
                new GameState[]{GameState.GameStarted},
                "Saves the game.",
                "save <[filename]>"
        );
        saveCmd.bindController(saveController);
        
        Command loadCmd = new Command(
                "load",
                new GameState[]{GameState.NotStarted, GameState.GameOver},
                "Loads a saved game.",
                "load <[filename]>"
        );
        loadCmd.bindController(saveController);

        // Initialize board
        model.chessBoard.initChessBoard();

        // Move a piece to a specific location
        Piece testPiece = model.chessBoard.getPieces()[0];
        Vec2 newPos = new Vec2(3, 3);
        testPiece.setPosition(newPos);
        model.chessBoard.getCell(3, 3).setPiece(testPiece);

        // Save
        saveController.acceptCommand(saveCmd, "save", "board_test");

        // Reset model
        model.setDefault();
        model.chessBoard.initChessBoard();

        // Load
        saveController.acceptCommand(loadCmd, "load", "board_test");

        // Verify piece is at the saved position
        Piece loadedPiece = model.chessBoard.getCell(3, 3).getPiece();
        assertNotNull("Piece should be at saved position", loadedPiece);
    }

    @Test
    public void testSaveTurnState() throws IOException {
        Command saveCmd = new Command(
                "save",
                new GameState[]{GameState.GameStarted},
                "Saves the game.",
                "save <[filename]>"
        );
        saveCmd.bindController(saveController);
        
        Command loadCmd = new Command(
                "load",
                new GameState[]{GameState.NotStarted, GameState.GameOver},
                "Loads a saved game.",
                "load <[filename]>"
        );
        loadCmd.bindController(saveController);

        // Set turn to BLACK
        model.turn = Team.BLACK;

        // Save
        saveController.acceptCommand(saveCmd, "save", "turn_test");

        // Change turn
        model.turn = Team.RED;

        // Load
        saveController.acceptCommand(loadCmd, "load", "turn_test");

        // Verify turn is restored
        assertEquals("Turn should be restored to BLACK", Team.BLACK, model.turn);
    }

    @Test
    public void testMultipleSaveFiles() throws IOException {
        Command saveCmd = new Command(
                "save",
                new GameState[]{GameState.GameStarted},
                "Saves the game.",
                "save <[filename]>"
        );
        saveCmd.bindController(saveController);

        // Create multiple save files
        saveController.acceptCommand(saveCmd, "save", "save1");
        saveController.acceptCommand(saveCmd, "save", "save2");
        saveController.acceptCommand(saveCmd, "save", "save3");

        // Verify all exist
        String[] files = SaveLoad.getFileNames(false);
        assertTrue("Should have at least 3 save files", files.length >= 3);
    }

    @Test
    public void testSaveFileSuffix() throws IOException {
        Command saveCmd = new Command(
                "save",
                new GameState[]{GameState.GameStarted},
                "Saves the game.",
                "save <[filename]>"
        );
        saveCmd.bindController(saveController);

        // Save without extension
        saveController.acceptCommand(saveCmd, "save", "suffix_test");

        // Verify file has correct suffix
        String[] files = SaveLoad.getFileNames(false);
        boolean found = false;
        for (String file : files) {
            if (file.contains("suffix_test") && file.endsWith(".sav")) {
                found = true;
                break;
            }
        }
        assertTrue("Save file should have .sav extension", found);
    }

    @Test
    public void testLoadInvalidFileFormat() throws IOException {
        // Create a corrupted save file
        String corruptedFile = "corrupted_save";
        try (PrintWriter writer = SaveLoad.getWriter(corruptedFile, false)) {
            writer.println("Invalid Format Line");
            writer.println("Another Invalid Line");
        }

        Command loadCmd = new Command(
                "load",
                new GameState[]{GameState.NotStarted, GameState.GameOver},
                "Loads a saved game.",
                "load <[filename]>"
        );

        // Try to load corrupted file - should handle gracefully
        saveController.acceptCommand(loadCmd, "load", corruptedFile);
        // Should not crash, just print error
    }

    @Test
    public void testAcceptLoadCommand() {
        final Command asd = new Command(
                "asd",
                new GameState[]{},""
        );
        saveController.acceptCommand(asd, "asd");

        Command loadCmd = new Command(
                "load",
                new GameState[]{GameState.NotStarted, GameState.GameOver},
                "Loads a saved game.",
                "load <[filename]>"
        );
        saveController.acceptCommand(loadCmd, "", "asd");
        try {
            var w = SaveLoad.getWriter("save", false);
            w.close();
            saveController.acceptCommand(loadCmd, "");
            saveController.acceptCommand(loadCmd, "", "-1");
            saveController.acceptCommand(loadCmd, "", "420");
            saveController.acceptCommand(loadCmd, "", "asdsadada");
            saveController.acceptCommand(loadCmd, "", "save");
            var w2 = SaveLoad.getWriter("good", false);
            w2.println("asd");
            w2.close();
            saveController.acceptCommand(loadCmd, "", "good");
            var w3 = SaveLoad.getWriter("verygood", false);
            w3.println("ILovePosi IHateNega RED");
            w3.println("asdasd");
            w3.close();
            saveController.acceptCommand(loadCmd, "", "verygood");
            var w4 = SaveLoad.getWriter("nah", false);
            w4.println("ILovePosi IHateNega RED");
            w4.println("-1,0");
            w4.println("0,-1");
            for (int i = 0; i < 8*2-3; i++)
                w4.println("-1,-1");
            w4.println("5,4");
            w4.println("0,0");
            w4.println("0,0");
            w4.close();
            saveController.acceptCommand(loadCmd, "", "nah");
        }  catch(IOException ignored) {

        }
    }
}

