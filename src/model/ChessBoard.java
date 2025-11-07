package model;

public final class ChessBoard {
    public static final short rows = 9, cols = 7;
    private final Cell[][] board;

    public ChessBoard() {
        board = new Cell[rows][cols];
    }

    public void initChessBoard() {
        for (short r = 0; r < rows; ++r) {
            for (short c = 0; c < cols; ++c) {
                board[r][c] = new Cell();
            }
        }
        initCellType();
        initCellPieces();
    }

    private void setCellTypes(int r, int c, CellType type) {
        board[r][c].setType(type);
        board[rows - r - 1][cols - c - 1].setType(type);
    }

    private void placePiece(int r, int c, int rank) {
        board[r][c].setPiece(new Piece((byte)rank, Team.BLACK));
        board[rows - r - 1][cols - c - 1]
                .setPiece(new Piece((byte)rank, Team.RED));
    }

    private void initCellType() {
        for (short r = 3; r < 6; ++r) {
            for (short c = 1; c < 6; ++c) {
                if (c == 3) ++c;
                board[r][c].setType(CellType.RIVER);
            }
        }
        setCellTypes(0, 3, CellType.DEN);
        setCellTypes(0, 2, CellType.TRAP);
        setCellTypes(0, 4, CellType.TRAP);
        setCellTypes(1, 3, CellType.TRAP);
    }

    private void initCellPieces() {
        placePiece(2, 0, 0); // rat
        placePiece(1, 5, 1); // cat
        placePiece(1, 1, 2); // dog
        placePiece(2, 4, 3); // wolf
        placePiece(2, 2, 4); // leopard
        placePiece(0, 6, 5); // tiger
        placePiece(0, 0, 6); // lion
        placePiece(2, 6, 7); // elephant
    }

    public Cell getCell(int r, int c) {
        return board[r][c];
    }

    public Cell[][] getBoard() {
        return board;
    }
}
