package controller;

import model.GameState;
import model.chess.Piece;
import model.chess.Team;
import model.chess.Vec2;
import model.gameIO.Command;
import model.gameIO.SaveLoad;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public final class SaveController extends Controller {
    public SaveController(model.MainModel model, view.MainView view) {
        super(model, view);
    }

    @Override
    public void acceptCommand(Command command, String... args) {
        if (command.is("save")) {
            try {
                String fileName;
                PrintWriter writer;
                if (args.length >= 2)
                    fileName = args[1];
                else
                    fileName = SaveLoad.getTimeString();

                writer = SaveLoad.getWriter(fileName, false);

                final Piece[] pieces = model.chessBoard.getPieces();

                writer.printf("%s %s %s%n", model.playerRedName, model.playerBlackName, model.turn.name());

                for (Piece piece : pieces) {
                    writer.println(piece.getPosition().toString());
                }
                writer.close();
                view.printMsg("Successfully saved as %s", fileName);
            } catch (IOException e) {
                view.printErr(e.getMessage());
            }
        } else if (command.is("load")) {
            try {

                // list
                final String[] files = SaveLoad.getFileNames(false);

                if (args.length == 1) {
                    if (files.length == 0){
                        view.printErr("There are no saves available.");
                        return;
                    }

                    view.printMsg("Available saves:");
                    for (int i = 0; i < files.length; i++) {
                        view.printMsg("%d: %s", i+1, files[i]);
                    }
                    return;
                }

                BufferedReader reader;
                try {
                    final int choice = Integer.parseInt(args[1]) - 1;
                    if (choice < 0 || choice > files.length) {
                        view.printErr("Invalid choice.");
                        return;
                    }
                    reader = SaveLoad.getReader(files[choice], false);
                } catch (NumberFormatException ignored) {
                    String name = args[1];
                    reader = SaveLoad.getReader(name, false);
                }

                model.setDefault();
                model.chessBoard.initChessBoard(false);
                model.gameState = GameState.GameStarted;

                final String[] meta = reader.readLine().split(" ");
                model.playerRedName = meta[0];
                model.playerBlackName = meta[1];
                model.turn = Team.valueOf(meta[2]);

                final Piece[] pieces = new Piece[8 * 2];
                String line = reader.readLine();
                int i = 0;
                while (line != null && !line.isEmpty()) {
                    final Vec2 pos = Vec2.fromString(line);

                    if (pos == null) {
                        view.printErr("Invalid position format, possibly corrupted.");
                        return;
                    }

                    if (i > 16) {
                        view.printErr("Extra pieces detected, ignored.");
                        break;
                    }
                    Piece p = new Piece((byte)(i - (i / 8) * 8), i > 7 ? Team.BLACK : Team.RED);
                    p.setPosition(pos);
                    pieces[i] = p;
                    // place pieces
                    if (!(pos.x < 0 || pos.y < 0)) {
                        model.chessBoard.getCell(pos.y, pos.x).setPiece(p);
                    }
                    ++i;
                    line = reader.readLine();
                }

                reader.close();
                model.chessBoard.setPieces(pieces);

                view.printMsg("Successfully loaded.");
                view.displayBoard(model);
            } catch (IOException e) {
                view.printErr(e.getMessage());
            }
        }
    }
}
