package controller;

import model.MainModel;
import view.MainView;

import java.util.Scanner;

public final class MainController extends Controller {
    private final Scanner scanner;
    private final RecordController recordControl;
    private final SaveController saveControl;


    public MainController(MainModel model, MainView view) {
        super(model, view);
        scanner = new Scanner(System.in);
        recordControl = new RecordController(model, view);
        saveControl = new SaveController(model, view);
    }

    public void initialize() {
        model.chessBoard.initChessBoard();
    }

    /**
     * The main logical loop
     */
    public void startLoop() {
        while (true) {



        }
    }
}

// categorize
// -- Chess Controller --
// 1 start new game
// 2 end ongoing game
// 3 player naming
// 4 ---cli--- already
// 5 status of game (chess board and turns)
// 6 taking back 3 moves
// 7 record game > file.record
// 8 replay the recorded game < file.record
// 9 save game > file.jungle
// 10 load game < file.jungle
