package model.chess;

public class Vec2 {
    public int x;
    public int y;

    public Vec2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vec2 add(Vec2 other) {
        return new Vec2(x + other.x, y + other.y);
    }

    public Vec2 neg(){
        return new Vec2(-x, -y);
    }

    @Override
    public String toString() {
        return x + "," + y;
    }

    public static Vec2 fromString(String str) {
        final String[] xy =  str.split(",");
        // incorrect format
        if (xy.length != 2)
            return null;
        try {
            final int x = Integer.parseInt(xy[0].trim());
            final int y = Integer.parseInt(xy[1].trim());

            return new Vec2(x, y);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
