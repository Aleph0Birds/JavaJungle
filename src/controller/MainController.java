package controller;

import gameIO.Command;
import model.MainModel;
import view.MainView;

import java.util.Scanner;

public final class MainController extends Controller {
    private final Scanner scanner;
    private final RecordController recordControl;
    private final SaveController saveControl;
    private final ChessController chessController;

    public MainController(MainModel model, MainView view) {
        super(model, view);
        scanner = new Scanner(System.in);
        recordControl = new RecordController(model, view);
        saveControl = new SaveController(model, view);
        chessController = new ChessController(model, view);
    }

    public void initialize() {
        model.chessBoard.initChessBoard();
    }

    /**
     * The main logical loop
     */
    public void startLoop() {
        while (true) {
            String[] choices = getChoices();
            view.displayActionChoices(model.gameState, choices);
            String input = scanner.nextLine();
        }
    }

    public ChessController chessControl() { return chessController; }
    public RecordController recordControl() { return recordControl; }
    public SaveController saveControl() { return saveControl; }

    private String[] getChoices() {
        return null;
    }

    private void parseCommand(String command) {
        switch (command) {}
    }

    @Override
    public void acceptCommand(Command command) {
        int a = 1 + 1;
    }

}

// categorize
// -- Chess Controller --
// 1 start new game
// 2 end ongoing game
// 3 player naming
// 4 ---cli--- already
// 5 status of game (chess board and turns)
// 6 taking back 3
// -- RecordController --
// 7 record game > file.record
// 8 replay the recorded game < file.record
// -- SaveController --
// 9 save game > file.jungle
// 10 load game < file.jungle
