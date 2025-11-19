import controller.ChessController;
import controller.MainController;
import model.GameState;
import model.MainModel;
import model.chess.*;
import model.gameIO.Command;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import view.MainView;

public class TestChess {

    MainModel model;
    MainView view;
    Command move;
    Command undoMove;
    ChessController controller;

    @Before
    public void setUp() {
        model = new MainModel();
        view = new MainView();
        move = new Command("move",
                new GameState[]{GameState.GameStarted},
                "Moves pieces of the current player.",
                "move <[piece name]|[cell index]> <up|down|left|right>");
        undoMove = new Command("undo",
                new GameState[]{GameState.GameStarted},
                "Undo the last move by previous player, at most 3 moves for each players.");
        controller = new ChessController(model, view);
    }

    @Test
    public void testMoveParsing() {

        model.chessBoard.initChessBoard(false);
        model.gameState = GameState.GameStarted;

        final Piece[] pieces = initPieces(model.chessBoard);
        final String key = "";
        // L(x) = Line# in ChessController.java
        controller.acceptCommand(move, key); // L19
        controller.acceptCommand(move, key, "a"); // L26
        controller.acceptCommand(move, key, "A1"); // L32
        controller.acceptCommand(move, key, "a1"); // same as above
        controller.acceptCommand(move, key, "狼"); // L43

        setPiece(new Vec2(0,0), pieces[3]); // red 狼
        setPiece(new Vec2(6,8), pieces[11]); // black 狼
        controller.acceptCommand(move, key, "G9"); // L38
        controller.acceptCommand(move, key, "a1"); // L48
        controller.acceptCommand(move, key, "狼", "asdasdasdsad"); // L58
        model.turn = Team.RED;
        controller.acceptCommand(move, key, "狼", "up"); // L54
        model.turn = Team.RED;
        controller.acceptCommand(move, key, "狼", "down"); // L55
        model.turn = Team.RED;
        controller.acceptCommand(move, key, "狼", "right"); // L56
        model.turn = Team.RED;
        controller.acceptCommand(move, key, "狼", "left"); // L57, L61
        model.turn = Team.RED;

        setPiece(new Vec2(0,1), pieces[2+8]);
        controller.acceptCommand(move, key, "狼", "up"); // L87
    }

    @Test
    public void testMoving() {
        model.chessBoard.initChessBoard(false);
        model.gameState = GameState.GameStarted;
        model.turn = Team.RED;

        final Piece[] pieces = initPieces(model.chessBoard);
        final String key = "";

        setPiece(new Vec2(1,0), pieces[3]); // red 狼

        // L139
        moveTest(pieces[3], MoveDirection.DOWN, true);
        // L151
        setPiece(new Vec2(4,2), pieces[6]); // lion jump
        moveTest(pieces[6], MoveDirection.UP, false);
        // L155
        setPiece(new Vec2(4,4), pieces[0+8]); // rat in path
        moveTest(pieces[6], MoveDirection.UP, true);

        // ---------- no piece tests ---------- //
        // L173
        setPiece(new Vec2(3,3), pieces[0]); // red rat the fat rat
        setPiece(new Vec2(5,2), pieces[4]); // red leopard
        moveTest(pieces[4], MoveDirection.UP, true);
        moveTest(pieces[0], MoveDirection.RIGHT, false);
        // L178
        setPiece(new Vec2(4,0), pieces[1]);
        moveTest(pieces[1], MoveDirection.LEFT, true);
        // L184
        setPiece(new Vec2(2,0), pieces[6+8]);
        model.turn = Team.BLACK;
        moveTest(pieces[6+8], MoveDirection.LEFT, false);
        Assert.assertEquals(GameState.GameOver, model.gameState);
        model.gameState = GameState.GameStarted;
        model.turn = Team.RED;

        view.displayBoard(model);

        // ---------- have piece tests ---------- //
        // L201
        moveTest(pieces[6], MoveDirection.RIGHT, true);
        // L208
        setPiece(new Vec2(3,4), pieces[7+8]); // black elephant
        moveTest(pieces[0], MoveDirection.UP, false); // eat elephant
        // L212
        model.turn = Team.BLACK;
        moveTest(pieces[7+8], MoveDirection.UP, true); // elephant eat rat
        // L215
        setPiece(new Vec2(1,2), pieces[2+8]); // black dog 5 osu
        setPiece(new Vec2(0,2), pieces[5]); // red tiger
        moveTest(pieces[2+8], MoveDirection.RIGHT, true); // dog can't eat tiger
        // L225 rat eat each other in different square
        model.chessBoard.movePiece(new Vec2(4, 4), new  Vec2(4, 3));
        moveTest(pieces[0+8], MoveDirection.RIGHT, true);
        model.turn = Team.RED;
        moveTest(pieces[0], MoveDirection.RIGHT, true);
        // L234
        moveTest(pieces[3], MoveDirection.RIGHT, false); // eat any pieces in trap square
        // L237
        moveTest(pieces[5], MoveDirection.RIGHT, false);

        view.displayBoard(model);
    }

    @Test
    public void testUndo() {
        final String key = "";
        model.chessBoard.initChessBoard(false);
        final Piece[] pieces = initPieces(model.chessBoard);
        // L93
        controller.acceptCommand(undoMove, key);

        setPiece(new Vec2(0, 4), pieces[7]);
        setPiece(new Vec2(6, 4), pieces[7+8]);
        model.moves.add(new Move(pieces[7], new Vec2(0,0), new Vec2(0, 1),null));
        model.moves.add(new Move(pieces[7], new Vec2(0,1), new Vec2(0, 2),null));
        model.moves.add(new Move(pieces[7], new Vec2(0,2), new Vec2(0, 3), pieces[3+8]));
        model.moves.add(new Move(pieces[7], new Vec2(0,3), new Vec2(0, 4),null));

        model.moves.add(new Move(pieces[7+8], new Vec2(6,8), new Vec2(6, 7),null));
        model.moves.add(new Move(pieces[7+8], new Vec2(6,7), new Vec2(6, 6),null));
        model.moves.add(new Move(pieces[7+8], new Vec2(6,6), new Vec2(6, 5), pieces[3]));
        model.moves.add(new Move(pieces[7+8], new Vec2(6,5), new Vec2(6, 4),null));

        for (int i = 0; i < 7; i++) {
            controller.acceptCommand(undoMove, key);
        }
        model.turn = Team.BLACK;
        controller.acceptCommand(undoMove, key);
    }

    private void moveTest(Piece piece, MoveDirection moveDirection, boolean resultNull) {
        Move move = controller.tryMove(piece.getPosition(), moveDirection);
        if (resultNull)
            Assert.assertNull(move);
        else
            Assert.assertNotNull(move);

    }

    private void setPiece(Vec2 position, Piece piece) {
        model.chessBoard.getCell(position).setPiece(null);
        piece.setPosition(position);
        model.chessBoard.getCell(position).setPiece(piece);
    }

    private Piece[] initPieces(ChessBoard board) {
        final Piece[] pieces = new Piece[8*2];
        for (int i = 0; i < 8; i++) {
            pieces[i] = new Piece((byte)i, Team.RED);
            pieces[i].setPosition(new Vec2(-1, -1));
        }

        for (int i = 8; i < 16; i++) {
            pieces[i] = new Piece((byte)(i-8), Team.BLACK);
            pieces[i].setPosition(new Vec2(-1, -1));
        }

        board.setPieces(pieces);
        return pieces;
    }
}
