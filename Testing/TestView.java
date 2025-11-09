import model.MainModel;
import model.chess.Team;
import view.MainView;
import org.junit.Test;

import java.util.Stack;


public class TestView {
    @Test
    public void testDisplayBoard() {
        MainView mainView = new MainView();
        MainModel model = new MainModel();
        model.chessBoard.initChessBoard();
        model.playerBlackName = "asdfghj";
        model.turn = Team.RED;
//        doesn't work
//        if (System.console() != null && System.getenv().get("TERM") != null) {
//            System.out.println("supports color");
//        } else {
//            System.out.println("not supports color");
//        }

        mainView.displayBoard(model);
    }

    @Test
    public void testStack() {
        Stack<Integer> stack = new Stack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        System.out.println(stack);
        stack.removeLast();
        System.out.println(stack);
    }
}