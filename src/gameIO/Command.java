package gameIO;

import controller.Controller;
import model.GameState;

public class Command {

    private final String key;
    private final GameState[] availableStates;
    private final String description;
    private final int id;
    private Controller controller;

    public Command (String key, int id,
                    GameState[] availableStates,
                    String description) {
        assert key != null && !key.isEmpty();
        assert availableStates != null;
        assert description != null;
        assert id >= 0;
        this.key = key;
        this.availableStates = availableStates;
        this.description = description;
        this.id = id;
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

    public int getID() { return id; }
}
