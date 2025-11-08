package controller;

import model.MainModel;
import model.chess.*;
import model.gameIO.Command;
import view.MainView;

public final class ChessController extends Controller {
    public ChessController(MainModel model, MainView view) {
        super(model, view);
    }

    @Override
    public void acceptCommand(Command command, String... args) {
        switch(command.getKey()) {
            case "move":
                if (args.length == 1) {
                    view.printErrUnderBoard("Please specific which piece you want to move. ie: A1 up / 豹 left");
                    break;
                }

                final Vec2 posByName = model.chessBoard.getIndexByPieceName(args[1], model.turn);
                final Vec2 posByIndex = model.chessBoard.getIndexByBoardIndex(args[1].toUpperCase());

                if (posByIndex == null && posByName == null) {
                    view.printErrUnderBoard("Piece by name '%s' or index '%s' not found", args[1], args[1]);
                    break;
                }

                final Vec2 pos = posByName == null ? posByIndex : posByName;
                if (posByIndex != null) {
                    final Piece targetPiece = model.chessBoard.getCell(pos.y, pos.x).getPiece();
                    if (targetPiece == null) {
                        view.printErrUnderBoard("Cell %s does not have a piece.", args[1]);
                        break;
                    }
                    else if (targetPiece.getTeam() != model.turn) {
                        view.printErrUnderBoard("Piece at %s does not belongs to you.", args[1]);
                        break;
                    }
                }
                if (pos.x <= -1 || pos.y <= -1) {
                    view.printErrUnderBoard("Piece %s is out of the board.", args[1]);
                    break;
                }

                if (args.length == 2) {
                    view.printErrUnderBoard("Please specific which direction you want to move. ie: A1 up / 豹 left");
                    break;
                }

                MoveDirection direction = switch (args[2].toLowerCase()) {
                    case "up" -> MoveDirection.UP;
                    case "left" -> MoveDirection.LEFT;
                    case "right" -> MoveDirection.RIGHT;
                    case "down" -> MoveDirection.DOWN;
                    default -> null;
                };

                if (direction == null) {
                    view.printErrUnderBoard("Invalid direction %s, please enter 'up', 'down' 'left' or 'right'.", args[2]);
                    break;
                }

                final Move move = tryMove(posByName == null ? posByIndex : posByName, direction);
                // moves failed
                if (move == null)
                    break;

                // valid move
                final Vec2 dest = move.destination();
                final Vec2 start = move.position();
                final Cell destCell = model.chessBoard.getCell(dest.y, dest.x);
                final Cell startCell = model.chessBoard.getCell(start.y, start.x);

                if (move.capturedPiece() != null)
                    // replace destCell piece with moved piece
                    move.capturedPiece().setPosition(new Vec2(-1, -1));

                // moves piece
                destCell.setPiece(move.piece());
                move.piece().setPosition(dest);
                startCell.setPiece(null);

                model.turn = model.turn == Team.RED ? Team.BLACK : Team.RED;
                model.moves.add(move);

                view.printMsgUnderBoard("Moves successfully.");
                break;
            case "undo":
                if (model.moves.isEmpty()) {
                    view.printErrUnderBoard("There are no previous moves to take back.");
                    break;
                }
                int times;
                String playerName;
                if (model.turn == Team.RED) {
                    playerName = model.playerRedName;
                    if (model.undoChanceRed <= 0) {
                        view.printErrUnderBoard("%s (RED) you don't have any undo chance left.", playerName);
                        break;
                    }
                    times = --model.undoChanceRed;
                } else {
                    playerName = model.playerBlackName;
                    if (model.undoChanceBlack <= 0) {
                        view.printErrUnderBoard("%s (BLACK) you don't have any undo chance left.", playerName);
                        break;
                    }
                    times = --model.undoChanceBlack;
                }

                final Move lastMove = model.moves.removeLast();

                final ChessBoard chessBoard = model.chessBoard;
                chessBoard.getCell(lastMove.position().y , lastMove.position().x).setPiece(lastMove.piece());
                chessBoard.getCell(lastMove.destination().y , lastMove.destination().x).setPiece(lastMove.capturedPiece());

                view.printMsgUnderBoard("%s (%s) has took back their last move, %d times left.", playerName, model.turn.name(), times);
                model.turn = model.turn == Team.RED ? Team.BLACK : Team.RED;
                break;
        }
        view.displayBoard(model);
    }

    private Move tryMove(Vec2 pos, MoveDirection direction) {
        final ChessBoard board = model.chessBoard;
        // x is column and y is row
        final Cell attackCell = board.getCell(pos.y, pos.x);
        final Piece attackerPiece = attackCell.getPiece();

        final Vec2 dirVec = model.turn == Team.RED ? direction.vec() : direction.vec().neg();
        Vec2 movePos = pos.add(dirVec);


        if (movePos.x < 0 || movePos.x > ChessBoard.cols ||
                movePos.y < 0 || movePos.y > ChessBoard.rows) {
            view.printErrUnderBoard("Cell %d, %d is out of bound.", movePos.x,  movePos.y);
            return null;
        }


        Cell defendCell = board.getCell(movePos.y, movePos.x);

        // move rules checking

        // lion and tiger can jump across river
        if (defendCell.getType() == CellType.RIVER &&
                (attackerPiece.getRank() == 5 ||  attackerPiece.getRank() == 6)) {
            while (defendCell.getType() == CellType.RIVER) {
                movePos = movePos.add(direction.vec());
                defendCell = board.getCell(movePos.y, movePos.x);

                // rat stuck the jump
                if (defendCell.getType() == CellType.RIVER &&
                        defendCell.getPiece() != null) {
                    view.printErrUnderBoard("Tiger or Lion cannot jump across the river with rat in the path.");
                    return null;
                }
            }
        }


        Move move = new Move(attackerPiece, pos, movePos, defendCell.getPiece());

        // no piece
        if (defendCell.getPiece() == null) {

            // only rat can move into river
            if (defendCell.getType() == CellType.RIVER &&
                    attackerPiece.getRank() == 0)
                return move;

            // cannot enter own den
            if (defendCell.getType() == CellType.DEN) {
                if ((model.turn == Team.RED && movePos.y == 0) ||
                    (model.turn == Team.BLACK && movePos.y == ChessBoard.rows-1)) {
                    view.printErrUnderBoard("You cannot move piece into your own den.");
                    return null;
                }

                view.printMsgUnderBoard("========== %s wins! ==========", model.turn == Team.RED ? model.playerRedName : model.playerBlackName);
                // wins
                return move;
            }

            return move;
        }

        // have piece
        final Piece defenderPiece = defendCell.getPiece();
        // new defenderCell to move on
        move = new Move(attackerPiece, pos, movePos, defenderPiece);

        // check team
        if (defenderPiece.getTeam() == model.turn) {
            view.printErrUnderBoard("You cannot capture your own pieces.");
            return null;
        }

        // check rank
        // rat can eat elephant
        if (attackerPiece.getRank() == 0 && defenderPiece.getRank() == 7) {
            return move;
        }
        // elephant cannot eat rat
        else if (attackerPiece.getRank() == 7 && defenderPiece.getRank() == 0) {
            view.printErrUnderBoard("Elephant cannot capture rat.");
            return null;
        } else if (attackerPiece.getRank() < defenderPiece.getRank()) {
            view.printErrUnderBoard("%s with rank %d cannot capture %s which has a higher rank %d.",
                    attackerPiece.getName(), attackerPiece.getRank(),
                    defenderPiece.getName(), defenderPiece.getRank());
            return null;
        }

        // TODO rat cannot eat pieces if other pieces are in different cell
        // river rat cannot capture land elephant
        // land rat cannot capture river rat and vice versa

        // captures enemy piece regardless of ranking
        if (defendCell.getType() == CellType.TRAP)
            return move;

        return move;
    }
}
