package controller;

import gameIO.Command;
import model.MainModel;
import view.MainView;

public class Controller {
    protected MainModel model;
    protected MainView view;

    public Controller(MainModel model, MainView view) {
        this.model = model;
        this.view = view;
    }

    public void acceptCommand(Command command) {}

    /**
     *
     * @return connected model
     */
    public MainModel getModel() { return model; }

    /**
     *
     * @return connected view
     */
    public MainView getView() { return view; }
}
