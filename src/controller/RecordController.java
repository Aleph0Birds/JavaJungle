package controller;

import model.MainModel;
import model.chess.Move;
import model.chess.Vec2;
import model.gameIO.Command;
import model.gameIO.SaveLoad;
import view.MainView;

import java.io.*;
import java.util.Scanner;

public final class RecordController extends Controller{
    public RecordController(MainModel model, MainView view) {
        super(model, view);
    }

    @Override
    public void acceptCommand(Command command, String... args) {
        String[] files;
        try {
            files = SaveLoad.getFileNames(true);
        } catch (IOException e) {
            view.printErr(e.getMessage());
            return;
        }

        if (args.length == 1 || args[1].equalsIgnoreCase("list")) {
            if (files.length == 0){
                view.printErr("There are no recordings available.");
                return;
            }

            view.printMsg("Available recordings:");
            for (int i = 0; i < files.length; i++) {
                view.printMsg("%d: %s", i+1, files[i]);
            }
            return;
        }

        try {
            final int choice = Integer.parseInt(args[1]) - 1;
            if (choice < 0 || choice > files.length) {
                view.printErr("Invalid choice.");
                return;
            }
            playRecording(files[choice]);
        } catch (NumberFormatException ignored) {
            String name = args[1];
            playRecording(name);
        }
    }

    public void saveRecording() {
        if (model.moves.isEmpty()) return;
        final String fileName = SaveLoad.getTimeString(true);
        try (PrintWriter fileWriter = SaveLoad.getWriter(fileName,true)) {
            fileWriter.printf("%s %s%n", model.playerRedName, model.playerBlackName);
            for (Move move : model.moves) {
                fileWriter.println(move.position() + " " + move.destination());
            }
            model.gameSaved = true;
            view.printMsg("Game saved as %s.",  fileName);
        } catch (IOException e) {
            view.printErr(e.getMessage());
        }
    }

    public void playRecording(String fileName) {
        try (BufferedReader reader = SaveLoad.getReader(fileName, true)) {
            final MainModel dummyModel = new MainModel();

            String[] playerNames = reader.readLine().split(" ");
            dummyModel.playerRedName = playerNames[0];
            dummyModel.playerBlackName = playerNames[1];
            dummyModel.chessBoard.initChessBoard();
            view.displayBoard(dummyModel);

            final Scanner scanner = new Scanner(System.in);

            String line = reader.readLine();
            while (line != null && !line.isEmpty()) {
                final String[] vec = line.split(" ");
                final Vec2 pos = Vec2.fromString(vec[0]);
                final Vec2 dest =  Vec2.fromString(vec[1]);

                if (pos == null || dest == null) {
                    view.printErr("Invalid coordinate in recording. Recording forced ended.");
                    return;
                }
                dummyModel.chessBoard.movePiece(pos, dest);
                view.displayBoard(dummyModel);
                dummyModel.switchTurn();

                view.printMsg("Type 'quit' to stop the replay or anything else to continue playing.");
                final String input = scanner.nextLine();
                if (input.equalsIgnoreCase("quit")) break;
                line = reader.readLine();

            }
            view.printMsg("Replay ended.");
        } catch (IOException e) {
            view.printErr(e.getMessage());
        }
    }
}
