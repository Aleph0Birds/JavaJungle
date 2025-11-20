package controller;

import model.gameIO.Command;
import model.MainModel;
import view.MainView;

public abstract class Controller {
    protected MainModel model;
    protected MainView view;

    public Controller(MainModel model, MainView view) {
        this.model = model;
        this.view = view;
    }


    public abstract void acceptCommand(Command command, String... args);
}
