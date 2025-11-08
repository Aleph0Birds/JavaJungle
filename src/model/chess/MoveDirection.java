package model.chess;

public enum MoveDirection {
    UP(new Vec2(0, 1)),
    DOWN(new Vec2(0, -1)),
    LEFT(new Vec2(-1, 0)),
    RIGHT(new Vec2(1, 0));

    private final Vec2 direction;

    MoveDirection(Vec2 vector) {
        direction = vector;
    }

    public Vec2 direction() {
        return direction;
    }
}
