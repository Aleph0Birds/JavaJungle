package model.chess;

public record Move(Piece piece,
                   Vec2 position,
                   Vec2 destination,
                   Piece capturedPiece)
{
    @Override
    public String toString() {
        return piece.getRank() +
                " " +
                position +
                " " +
                destination +
                " " +
                (capturedPiece == null ? -1 : capturedPiece.getRank());
    }
}
