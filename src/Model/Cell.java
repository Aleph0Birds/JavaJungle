package Model;

public final class Cell {
    public Cell() {}

    private Piece piece;
    private CellType type;

    public void setPiece(Piece p) {
        piece = p;
    }
    public Piece getPiece() {
        return piece;
    }

    public void setType(CellType t) {
        type = t;
    }
    public CellType getType() {
        return type;
    }
}