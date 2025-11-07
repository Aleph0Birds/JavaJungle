package model.gameIO;

import controller.Controller;
import model.GameState;

public class Command {

    private final String key;
    private final GameState[] availableStates;
    private final String description;
    private Controller controller;

    public Command (String key,
                    GameState[] availableStates,
                    String description) {
        assert key != null && !key.isEmpty();
        assert availableStates != null;
        assert description != null;

        this.key = key;
        this.availableStates = availableStates;
        this.description = description;
    }

    public void invoke(String... args) {
        controller.acceptCommand(this,  args);
    }

    public void bindController(Controller controller) {
        this.controller = controller;
    }

    public String getKey() {
        return key;
    }
    public boolean isStateAvailable(GameState gameState) {
        for (GameState state : availableStates) {
            if (state == gameState) {
                return true;
            }
        }
        return false;
    }

    public String getDescription() {
        return description;
    }

}
