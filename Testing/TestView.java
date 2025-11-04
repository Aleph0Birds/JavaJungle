import Model.ChessBoard;
import View.MainView;
import org.junit.Test;


public class TestView {
    @Test
    public void testDisplayBoard() {
        MainView mainView = MainView.getInstance();
        ChessBoard chessBoard = new ChessBoard();
        chessBoard.initChessBoard();

        if (System.console() != null && System.getenv().get("TERM") != null) {
            System.out.println("supports color");
        } else {
            System.out.println("not supports color");
        }

        mainView.displayBoard(chessBoard);
    }
}