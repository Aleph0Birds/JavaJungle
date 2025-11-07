package controller;

import model.gameIO.Command;

public final class SaveController extends Controller {
    public SaveController(model.MainModel model, view.MainView view) {
        super(model, view);
    }

    @Override
    public void acceptCommand(Command command, String... args) {
        super.acceptCommand(command, args);
    }
}
