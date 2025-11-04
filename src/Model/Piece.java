package Model;

import java.util.Objects;

/**
 * Immutable piece class
 * @param rank must be within 0-7
 * @param team the team the piece belongs to, not null
 */
public final record Piece(byte rank, Team team) {
    public static final String[] names = {
        "鼠", "貓", "狗", "狼", "豹", "虎", "獅", "象"
    };

    /**
     * @param rank must be within 0-7
     * @param team the team the piece belongs to, not null
     * @throws IllegalArgumentException if rank is not in [0, 7]
     * @throws NullPointerException if team is null
     */
    public Piece {
        if (rank < 0 || rank > 7)
            throw new IllegalArgumentException("Rank must be between 0 and 7, rank: " + rank);
        Objects.requireNonNull(team);
    }

    /**
     * @return the name of the piece
     */
    public String getName() {
        return names[rank];
    }
}
