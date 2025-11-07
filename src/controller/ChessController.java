package controller;

import model.MainModel;
import model.gameIO.Command;
import view.MainView;

public class ChessController extends Controller {
    public ChessController(MainModel model, MainView view) {
        super(model, view);
    }

    @Override
    public void acceptCommand(Command command, String... args) {

    }
}
