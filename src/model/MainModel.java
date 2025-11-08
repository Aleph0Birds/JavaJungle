package model;

import model.chess.ChessBoard;
import model.chess.Move;

import java.util.ArrayList;
import java.util.Stack;

public final class MainModel {
    public ChessBoard chessBoard;
    public String playerRedName;
    public String playerBlackName;
    public boolean isRedTurn;
    public GameState gameState;
    public int round;
    public Stack<Move> lastMoves;
    public ArrayList<Move> moves;

    private MainModel() {
        chessBoard = new ChessBoard();
        playerRedName = "Red"; // default
        playerBlackName = "Black"; // default
        gameState = GameState.NotStarted;
        isRedTurn = true;
        round = 0;
        lastMoves = new Stack<>();
        moves = new ArrayList<>();
    }
    private static MainModel instance;
    public static MainModel getInstance() {
        if (instance == null)
            instance = new MainModel();
        return instance;
    }
}
