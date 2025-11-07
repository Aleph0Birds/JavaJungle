package model.chess;

public record Move(Piece piece,
                   Vec2 position,
                   MoveDirection direction,
                   Piece capturedPiece)
{

}
