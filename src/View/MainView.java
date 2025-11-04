package View;

import Model.Cell;
import Model.ChessBoard;
import Model.Team;

import java.io.PrintStream;

public final class MainView {
    public static final String emptyName = "　　　";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";

    private final PrintStream out;

    public void displayBoard(ChessBoard chessBoard) {
        Cell[][] cells = chessBoard.getBoard();
        for (short r = 0; r < ChessBoard.rows; r++) {
            for (short c = 0; c < ChessBoard.cols; c++)
                out.print("+－－－");
            out.println('+');

            for (short c = 0; c < ChessBoard.cols; c++) {
                out.print(ANSI_RESET + '|');
                String color = switch (cells[r][c].getType()) {
                    case TRAP -> ANSI_YELLOW_BACKGROUND;
                    case RIVER -> ANSI_BLUE_BACKGROUND;
                    case DEN -> ANSI_RED_BACKGROUND;
                    case null, default -> ANSI_RESET;
                };
                out.print(color);
                if(cells[r][c].getPiece() == null)
                    out.print(emptyName);
                else {
                    out.print('　');
                    if (cells[r][c].getPiece().team() == Team.RED)
                        out.print(ANSI_RED);
                    out.print(cells[r][c].getPiece().getName());
                    out.print('　');
                }
            }
            out.println(ANSI_RESET + '|');
        }
        for (short c = 0; c < ChessBoard.cols; c++) {
            out.print("+－－－");
        }
        out.println('+');
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
// Color deprecated
//public void displayBoard(ChessBoard chessBoard) {
//    Cell[][] cells = chessBoard.getBoard();
//    for (int r = 0; r < ChessBoard.rows; r++) {
//        for (int c = 0; c < ChessBoard.cols; c++) {
//            out.print("+－－－");
//        }
//        out.println('+');
//        for (int c = 0; c < ChessBoard.cols; c++) {
//            out.print(ANSI_RESET + "|");
//
//            switch (cells[r][c].getType()) {
//                case EMPTY:
//                    out.print(ANSI_BLACK_BACKGROUND);
//                    break;
//                case TRAP:
//                    out.print(ANSI_RED_BACKGROUND);
//                    break;
//                case RIVER:
//                    out.print(ANSI_BLUE_BACKGROUND);
//                    break;
//                case DEN:
//                    out.print(ANSI_YELLOW_BACKGROUND);
//                    break;
//                case null, default:
//                    out.print(ANSI_GREEN_BACKGROUND);
//            }
//            if(cells[r][c].getPiece() == null)
//                out.print(emptyName);
//            else{
//                out.print("　");
//                out.print(cells[r][c].getPiece().getName());
//                out.print("　");
//            }
//        }
//        out.println(ANSI_RESET + '|');
//
//    }
//    for (int c = 0; c < ChessBoard.cols; c++) {
//        out.print("+－－－");
//    }
//    out.println('+');
//}