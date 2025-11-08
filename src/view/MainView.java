package view;

import model.gameIO.Command;
import model.*;
import model.chess.*;

import java.io.PrintStream;

public final class MainView {
    public static final char fullWidthSpace = '　';
    public static final String emptyName = "　　　";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    // public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    // public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    // public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";

    private final PrintStream out;

    private String pendingMsg = null;
    private String pendingErr = null;

    public void displayBoard(MainModel model) {
        final boolean isRedTurn = model.turn == Team.RED;
        final short startR = (short) (isRedTurn ? ChessBoard.rows-1 : 0);
        final short startC = (short) (isRedTurn ? 0 : ChessBoard.cols-1);
        final short incrementR = (short) (isRedTurn ? -1 : 1);
        final short incrementC = (short) (isRedTurn ? 1 : -1);

        Cell[][] cells = model.chessBoard.getBoard();
        final String playerName = isRedTurn ? model.playerRedName : model.playerBlackName;

        final short lengthOpp = (short)(!isRedTurn ? model.playerRedName : model.playerBlackName).length();
        for (short i = 0; i < 14 - lengthOpp / 3; i++) {
            out.print(fullWidthSpace);
        }
        out.println(!isRedTurn ? model.playerRedName : model.playerBlackName);

        for (short r = startR; checkIndex(r, true, isRedTurn); r+=incrementR) {
            // placeholder var no need index-checking
            out.print("  "); // indentation for id
            for (short c = 0; c < ChessBoard.cols; c++)
                out.print("+－－－");
            out.println('+');

            // print row id
            out.print((r+1) + " ");

            for (short c = startC; checkIndex(c, false, isRedTurn); c+=incrementC) {
                out.print(ANSI_RESET + '|');
                final CellType cellType = cells[r][c].getType();

                /* background seems not suitable
                String backgroundColor = switch (cellType) {
                    case TRAP -> ANSI_YELLOW_BACKGROUND;
                    case RIVER -> ANSI_BLUE_BACKGROUND;
                    case DEN -> ANSI_RED_BACKGROUND;
                    case null, default -> ANSI_RESET;
                };

                backgroundColor = "";
                */

                final String cellTypeChar = switch (cellType) {
                    case TRAP -> ANSI_YELLOW + "Ｔ";
                    case RIVER -> ANSI_BLUE + "～";
                    case DEN -> ANSI_RED + "０";
                    case null, default -> String.valueOf(fullWidthSpace);
                };

                out.print(/* backgroundColor + */cellTypeChar);

                final Piece piece = cells[r][c].getPiece();
                if (piece != null) {
                    final String teamColor = piece.getTeam() == Team.RED ? ANSI_RED : "";
                    out.print(ANSI_RESET +/* backgroundColor + */ teamColor + piece.getName() + ANSI_RESET);
                } else out.print(cellTypeChar);

                out.print(/*backgroundColor + */cellTypeChar);
            }
            out.println(ANSI_RESET + '|');
        }
        // placeholder var no need index-checking
        out.print("  ");
        for (short c = 0; c < ChessBoard.cols; c++) {
            out.print("+－－－");
        }
        out.println('+');

        // print col id
        out.print(emptyName);
        for (short c = 0; c < ChessBoard.cols; c++) {
            final char colID = isRedTurn ? (char) ('A' + c) : (char)('G' - c);
            out.print(colID + emptyName);
        }
        out.println();

        short length = (short)playerName.length();
        for (short i = 0; i < 14 - length / 3; i++) {
            out.print(fullWidthSpace);
        }
        out.println(playerName);


        final String turnStr = isRedTurn ? "Red" : "Black";
        out.printf("Current turn: %s (%s)%n", playerName, turnStr);

        if (pendingMsg != null) {
            printMsg(pendingMsg);
            pendingMsg = null;
        }

        if (pendingErr != null) {
            printErr(pendingErr);
            pendingErr = null;
        }
    }

    public void displayActionChoices(GameState state, Command[] choices) {
        final String outState = switch (state) {
            case NotStarted -> "not started";
            case GameStarted -> "ongoing";
            case GameOver -> "over";
        };

        out.printf("Game is %s, please select one of the following actions:%n", outState);
        int i = 0;
        for  (; i < choices.length; i++) {
            final Command c = choices[i];
            out.printf("%d: %s - %s%n", i+1, c.getKey(), c.getDescription());
            if (!c.getUsage().isEmpty())
                out.printf("   Usage: %s%n", c.getUsage());
        }

        // prompt
        out.printf("Enter your choice (1-%d or cmd name): ", i);
    }

    public void printMsg(String msg, Object... args) {
        msg = ANSI_BLUE + msg.formatted(args) + ANSI_RESET;
        out.println(msg);
    }

    public void printMsgUnderBoard(String msg, Object... args) {
        pendingMsg = msg.formatted(args);
    }

    public void printErr(String msg, Object... args) {
        msg = msg.formatted(args);
        out.printf("%sError: %s%s%n", ANSI_RED, msg, ANSI_RESET);
    }

    public void printErrUnderBoard(String msg, Object... args) {
        pendingErr = msg.formatted(args);
    }

    private boolean checkIndex(short x, boolean isRow, boolean isRed) {
        if (isRed)
            return isRow ? x >= 0 : x < ChessBoard.cols;
        else
            return isRow ? x < ChessBoard.rows : x >= 0;
    }

    private MainView() {
        out = System.out;
    }
    private static MainView instance;
    public static MainView getInstance() {
        if (instance == null)
            instance = new MainView();
        return instance;
    }
}