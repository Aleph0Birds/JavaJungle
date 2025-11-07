package model;

import model.chess.ChessBoard;

public final class MainModel {
    public ChessBoard chessBoard;
    public String playerRedName;
    public String playerBlackName;
    public boolean isRedTurn;
    public GameState gameState;
    public int round;

    private MainModel() {
        chessBoard = new ChessBoard();
        playerRedName = "Red"; // default
        playerBlackName = "Black"; // default
        gameState = GameState.NotStarted;
        isRedTurn = false;
        round = 0;
    }
    private static MainModel instance;
    public static MainModel getInstance() {
        if (instance == null)
            instance = new MainModel();
        return instance;
    }
}
