package controller;

import model.MainModel;
import model.chess.Move;
import model.chess.Piece;
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
        if (args.length == 1) {
            view.printErr("Please use 'list' to get a list of the recordings or specific the recording name to replay.");
            return;
        }

        String[] files;
        try {
            files = SaveLoad.getFileNames(true);
        } catch (IOException e) {
            view.printErr(e.getMessage());
            return;
        }

        if (args[1].equalsIgnoreCase("list")) {
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
        try (PrintWriter fileWriter = SaveLoad.getFileWriter(true)) {
            fileWriter.printf("%s %s%n", model.playerRedName, model.playerBlackName);
            for (Move move : model.moves) {
                fileWriter.println(move.position() + " " + move.destination());
            }
            model.gameSaved = true;
        } catch (IOException e) {
            view.printErr(e.getMessage());
        }
    }

    public void playRecording(String fileName) {
        try (BufferedReader reader = SaveLoad.getFileReader(fileName, true)) {
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
                final String[] pos = vec[0].split(",");
                final String[] dest =  vec[1].split(",");

                final int fromX = Integer.parseInt(pos[0]);
                final int fromY = Integer.parseInt(pos[1]);
                final int toX = Integer.parseInt(dest[0]);
                final int toY = Integer.parseInt(dest[1]);

                dummyModel.chessBoard.movePiece(new Vec2(fromX, fromY), new Vec2(toX, toY));
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
