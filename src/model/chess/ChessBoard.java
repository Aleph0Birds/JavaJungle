package model.chess;

public final class ChessBoard {
    public static final short rows = 9, cols = 7;

    private final Cell[][] board;
    private final Piece[] pieces;

    public ChessBoard() {
        board = new Cell[rows][cols];
        pieces = new Piece[8 * 2];
    }

    public void initChessBoard() {
        initChessBoard(true);
    }

    public void initChessBoard(boolean withPieces) {
        for (short r = 0; r < rows; ++r) {
            for (short c = 0; c < cols; ++c) {
                board[r][c] = new Cell();
            }
        }
        initCellType();
        if (withPieces)
            initCellPieces();
    }

    public void movePiece(Vec2 from, Vec2 to) {
        // asserted from pos must have a piece
        final Cell fromCell = board[from.y][from.x];
        board[to.y][to.x].setPiece(fromCell.getPiece());
        fromCell.setPiece(null);
    }

    private void setCellTypes(int r, int c, CellType type) {
        board[r][c].setType(type);
        board[rows - r - 1][cols - c - 1].setType(type);
    }

    private void placePiece(int r, int c, int rank) {
        final Piece red = new Piece((byte)rank, Team.RED);
        board[r][c].setPiece(red);
        red.setPosition(new Vec2(c, r));
        pieces[rank] = red;

        final Piece black = new Piece((byte)rank, Team.BLACK);
        board[rows - r - 1][cols - c - 1]
                .setPiece(black);
        black.setPosition(new Vec2(rows - r - 1, cols - c - 1));
        pieces[rank + 8] = black;
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
        placePiece(2, 6, 0); // rat
        placePiece(1, 1, 1); // cat
        placePiece(1, 5, 2); // dog
        placePiece(2, 2, 3); // wolf
        placePiece(2, 4, 4); // leopard
        placePiece(0, 0, 5); // tiger
        placePiece(0, 6, 6); // lion
        placePiece(2, 0, 7); // elephant
    }

    public Vec2 getIndexByPieceName(String name, Team team) {
        for (int i = 0; i < Piece.names.length; i++) {
            if (name.equals(Piece.names[i])) {
                if (team == Team.BLACK) i += 8;
                return pieces[i].getPosition();
            };
        }
        return null;
    }

    public Vec2 getIndexByBoardIndex(String boardIndex) {
        if (boardIndex.length() != 2)
            return null;
        return new Vec2(boardIndex.charAt(0) - 'A', boardIndex.charAt(1) - '1');
    }

    public Cell getCell(int r, int c) {
        return board[r][c];
    }

    public Cell[][] getBoard() {
        return board;
    }
}
