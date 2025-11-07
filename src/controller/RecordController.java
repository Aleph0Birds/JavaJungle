package controller;

import model.MainModel;
import model.gameIO.Command;
import view.MainView;

public final class RecordController extends Controller{
    public RecordController(MainModel model, MainView view) {
        super(model, view);
    }

    @Override
    public void acceptCommand(Command command, String... args) {

    }
}
