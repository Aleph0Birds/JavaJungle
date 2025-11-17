package model.gameIO;

import controller.Controller;
import model.GameState;

public class Command {

    private final String key;
    private final GameState[] availableStates;
    private final String description;
    private final String usage;
    private Controller controller;

    public Command (String key,
                    GameState[] availableStates,
                    String description,
                    String usage) {
        assert key != null && !key.isEmpty();
        assert availableStates != null;
        assert description != null;
        assert usage != null;

        this.key = key;
        this.availableStates = availableStates;
        this.description = description;
        this.usage = usage;
    }

    public Command (String key, GameState[] availableStates, String description) {
        this(key, availableStates,description,"");
    }

    public void invoke(String... args) {
        controller.acceptCommand(this,  args);
    }

    public boolean is(String key) {
        return key.equalsIgnoreCase(this.key);
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

    public String getUsage() {
        return usage;
    }

}
