import model.chess.Move;
import model.chess.Piece;
import model.chess.Team;
import model.chess.Vec2;
import org.junit.Test;

public class TestChessModel {
    @Test
    public void testMove() {


        final Move move = new Move(
                new Piece((byte) 0, Team.RED),
                new Vec2(0,0),
                new Vec2(0,1),
                new Piece((byte) 7, Team.BLACK));
        System.out.println(move);
    }

    @Test
    public void testCast() {
        try {
            Piece ignored = new Piece((byte)-1, Team.BLACK);

        } catch (IllegalArgumentException ignored) {}

        try {
            Piece ignored = new Piece((byte)9, Team.BLACK);
        } catch (IllegalArgumentException ignored) {}

        try {
            Vec2 ignored = Vec2.fromString("asd,asd");
        } catch (NumberFormatException ignored) {}
    }
}
