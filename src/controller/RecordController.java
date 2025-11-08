package controller;

import model.MainModel;
import model.chess.Move;
import model.gameIO.Command;
import model.gameIO.SaveLoad;
import view.MainView;

import java.io.*;

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
            files = SaveLoad.getFileNames();
        } catch (IOException e) {
            view.printErr(e.getMessage());
            return;
        }

        if (args[0].equalsIgnoreCase("list")) {
                // should not happen
                if (files == null){
                    view.printErr("SaveLoad.initialize() should be called first.");
                    return;
                }

                if (files.length == 0){
                    view.printErr("There are no recordings available.");
                    return;
                }

                for (int i = 0; i < files.length; i++) {
                    view.printMsg("%d: %s", i+1, files[i]);
                }
                return;
        }

        try {
            final int choice = Integer.parseInt(args[1]);

            return;
        } catch (NumberFormatException ignored) {

        }
    }

    public void saveRecording() {
        try (PrintWriter fileWriter = SaveLoad.getFileWriter()) {
            fileWriter.printf("%s %s%n", model.playerRedName, model.playerBlackName);
            for (Move move : model.moves) {
                fileWriter.println(move.toString());
            }
        } catch (IOException e) {
            view.printErr(e.getMessage());
            return;
        }
    }
}
