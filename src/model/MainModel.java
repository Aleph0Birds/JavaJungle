package model;

import model.chess.ChessBoard;
import model.chess.Move;
import model.chess.Team;

import java.util.ArrayList;
import java.util.Stack;

public final class MainModel {
    public ChessBoard chessBoard;
    public String playerRedName;
    public String playerBlackName;
    public Team turn;
    public GameState gameState;
    //public int round;
    //public Stack<Move> lastMoves;

    public byte undoChanceRed;
    public byte undoChanceBlack;
    public ArrayList<Move> moves;

    public void setDefault() {
        chessBoard = new ChessBoard();
        playerRedName = "Red"; // default
        playerBlackName = "Black"; // default
        gameState = GameState.NotStarted;
        turn = Team.RED;
        //round = 0;
        undoChanceRed = 3;
        undoChanceBlack = 3;
        //lastMoves = new Stack<>();
        moves = new ArrayList<>();
    }

    private MainModel() {
        setDefault();
    }
    private static MainModel instance;
    public static MainModel getInstance() {
        if (instance == null)
            instance = new MainModel();
        return instance;
    }
}
