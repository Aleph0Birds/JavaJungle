import controller.MainController;
import controller.RecordController;
import model.GameState;
import model.MainModel;
import model.chess.Move;
import model.chess.Piece;
import model.chess.Team;
import model.chess.Vec2;
import model.gameIO.SaveLoad;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import view.MainView;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.*;

public class TestRecordController {
    private MainModel model;
    private MainView view;
    private RecordController recordController;
    private MainController mainController;

    @Before
    public void setUp() throws IOException {
        model = new MainModel();
        view = new MainView();
        mainController = new MainController(model, view);
        mainController.initialize();
        recordController = mainController.recordControl();

        // Ensure saves directory exists
        SaveLoad.initialize();
    }

    @After
    public void tearDown() throws IOException {
        // Clean up test files
        if (Files.exists(SaveLoad.savePath)) {
            Files.list(SaveLoad.savePath)
                    .filter(path -> path.toString().endsWith(".record"))
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
    public void testSaveRecordingWithMoves() throws IOException {
        // Setup: Add some moves to the model
        model.playerRedName = "Alice";
        model.playerBlackName = "Bob";
        model.chessBoard.initChessBoard();

        // Create a mock move
        Piece piece = new Piece((byte) 0, Team.RED);
        Vec2 from = new Vec2(0, 0);
        Vec2 to = new Vec2(0, 1);
        Move move = new Move(piece, from, to, null);
        model.moves.add(move);

        // Save the recording
        recordController.saveRecording();

        // Verify that a recording file was created
        String[] files = SaveLoad.getFileNames(true);
        assertTrue("At least one recording file should exist", files.length > 0);
        assertTrue("Game should be marked as saved", model.gameSaved);
    }

    @Test
    public void testSaveRecordingEmptyMoves() {
        // Setup: No moves in the model
        model.moves.clear();

        // Save the recording (should do nothing)
        recordController.saveRecording();

        // Verify no file was created (since moves is empty)
        assertFalse("Game should not be marked as saved when there are no moves", model.gameSaved);
    }

    @Test
    public void testSaveRecordingFileFormat() throws IOException {
        // Setup: Add player names and a move
        model.playerRedName = "TestRed";
        model.playerBlackName = "TestBlack";
        model.chessBoard.initChessBoard();

        Piece piece = new Piece((byte) 0, Team.RED);
        Vec2 from = new Vec2(1, 2);
        Vec2 to = new Vec2(1, 3);
        Move move = new Move(piece, from, to, null);
        model.moves.add(move);

        // Save the recording
        recordController.saveRecording();

        // Read the file and verify format
        String[] files = SaveLoad.getFileNames(true);
        assertTrue("Recording file should exist", files.length > 0);

        try (BufferedReader reader = SaveLoad.getReader(files[0], true)) {
            String firstLine = reader.readLine();
            assertNotNull("First line should contain player names", firstLine);
            assertTrue("First line should contain TestRed", firstLine.contains("TestRed"));
            assertTrue("First line should contain TestBlack", firstLine.contains("TestBlack"));

            String secondLine = reader.readLine();
            assertNotNull("Second line should contain move", secondLine);
            assertTrue("Move line should contain positions", secondLine.contains("1,2") && secondLine.contains("1,3"));
        }
    }

    @Test
    public void testPlayRecordingFileNotFound() {
        // Try to play a recording that doesn't exist
        recordController.playRecording("nonexistent_file");
        // Should handle gracefully without crashing
        // (The method prints error but doesn't throw exception)
    }

    @Test
    public void testSaveMultipleMoves() throws IOException {
        // Setup: Add multiple moves
        model.playerRedName = "Player1";
        model.playerBlackName = "Player2";
        model.chessBoard.initChessBoard();

        // Add three moves
        for (int i = 0; i < 3; i++) {
            Piece piece = new Piece((byte) i, Team.RED);
            Vec2 from = new Vec2(i, 0);
            Vec2 to = new Vec2(i, 1);
            Move move = new Move(piece, from, to, null);
            model.moves.add(move);
        }

        // Save the recording
        recordController.saveRecording();

        // Read and verify
        String[] files = SaveLoad.getFileNames(true);
        try (BufferedReader reader = SaveLoad.getReader(files[0], true)) {
            reader.readLine(); // Skip player names line

            int moveCount = 0;
            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                moveCount++;
            }
            assertEquals("Should have saved 3 moves", 3, moveCount);
        }
    }

    @Test
    public void testRecordingPreservesPlayerNames() throws IOException {
        // Setup with custom player names
        model.playerRedName = "AliceTheGreat";
        model.playerBlackName = "BobTheBrave";
        model.chessBoard.initChessBoard();

        // Add a move
        Piece piece = new Piece((byte) 0, Team.RED);
        Vec2 from = new Vec2(0, 0);
        Vec2 to = new Vec2(0, 1);
        Move move = new Move(piece, from, to, null);
        model.moves.add(move);

        // Save
        recordController.saveRecording();

        // Read and verify player names
        String[] files = SaveLoad.getFileNames(true);
        try (BufferedReader reader = SaveLoad.getReader(files[0], true)) {
            String firstLine = reader.readLine();
            String[] names = firstLine.split(" ");
            assertEquals("Red player name should match", "AliceTheGreat", names[0]);
            assertEquals("Black player name should match", "BobTheBrave", names[1]);
        }
    }

    @Test
    public void testGameSavedFlag() {
        // Initially not saved
        assertFalse("Game should not be marked as saved initially", model.gameSaved);

        // Add a move and save
        model.chessBoard.initChessBoard();
        Piece piece = new Piece((byte) 0, Team.RED);
        Vec2 from = new Vec2(0, 0);
        Vec2 to = new Vec2(0, 1);
        Move move = new Move(piece, from, to, null);
        model.moves.add(move);

        recordController.saveRecording();

        // Should be marked as saved
        assertTrue("Game should be marked as saved after recording", model.gameSaved);
    }

    @Test
    public void testCreateTestRecordingFile() throws IOException {
        // Create a test recording file manually
        String testFileName = "test_recording";
        try (PrintWriter writer = SaveLoad.getWriter(testFileName, true)) {
            writer.println("TestPlayer1 TestPlayer2");
            writer.println("0,0 0,1");
            writer.println("1,0 1,1");
        }

        // Verify file was created
        String[] files = SaveLoad.getFileNames(true);
        boolean found = false;
        for (String file : files) {
            if (file.contains("test_recording")) {
                found = true;
                break;
            }
        }
        assertTrue("Test recording file should exist", found);
    }

    @Test
    public void testMoveWithCapture() throws IOException {
        // Test recording a move with a captured piece
        model.playerRedName = "Red";
        model.playerBlackName = "Black";
        model.chessBoard.initChessBoard();

        Piece attackPiece = new Piece((byte) 0, Team.RED);
        Piece capturedPiece = new Piece((byte) 0, Team.BLACK);
        Vec2 from = new Vec2(0, 0);
        Vec2 to = new Vec2(0, 1);
        Move move = new Move(attackPiece, from, to, capturedPiece);
        model.moves.add(move);

        recordController.saveRecording();

        // Verify file was created
        String[] files = SaveLoad.getFileNames(true);
        assertTrue("Recording with capture should be saved", files.length > 0);
    }
}


