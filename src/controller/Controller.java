package controller;

import model.MainModel;
import view.MainView;

public class Controller {
    protected MainModel model;
    protected MainView view;

    public Controller(MainModel model, MainView view) {
        this.model = model;
        this.view = view;
    }

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
