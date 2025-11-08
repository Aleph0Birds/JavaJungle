package model.chess;

import java.util.Objects;


public final class Piece {
    public static final String[] names = {
        "鼠", "貓", "狗", "狼", "豹", "虎", "獅", "象"
    };

    /**
     * @param rank must be within 0-7
     * @param team the team the piece belongs to, not null
     * @throws IllegalArgumentException if rank is not in [0, 7]
     * @throws NullPointerException if team is null
     */
    public Piece (byte rank, Team team) {
        if (rank < 0 || rank > 7)
            throw new IllegalArgumentException("Rank must be between 0 and 7, rank: " + rank);
        Objects.requireNonNull(team);

        this.rank = rank;
        this.team = team;
    }

    /**
     * @return the name of the piece
     */
    public String getName() {
        return names[rank];
    }

    private final byte rank;
    private final Team team;
    private Vec2 position;

    public byte getRank() { return rank; }
    public Team getTeam() { return team; }
    public Vec2 getPosition() { return position; }
    public void setPosition(Vec2 position) { this.position = position; }
}
