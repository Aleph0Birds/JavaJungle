package model.chess;

public record Move(Piece piece,
                   Vec2 position,
                   Vec2 destination,
                   Piece capturedPiece)
{

}
