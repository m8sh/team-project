package interface_adapters.Scoreboard;

/**
 * Represents a single row in the scoreboard UI,
 * containing the rank, username, and score of one player.
 */

public class ScoreboardRowViewModel {
    private final int rank;
    private final String name;
    private final int score;

    /**
     * Create a view model row for the scoreboard.
     *
     * @param rank the player's ranking on the scoreboard
     * @param name the player's display name
     * @param score the player's current score
     */

    public ScoreboardRowViewModel(int rank, String name, int score) {
        this.rank = rank;
        this.name = name;
        this.score = score;
    }

    public int getRank() {
        return rank;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }
    /**
     * Set the player's rank.
     *
     * @param rank the new rank to assign
     */

    public void setRank(int rank) {
    }
    /**
     * Set the player's name.
     *
     * @param name the new name to assign
     */

    public void setName(String name) {
    }
    /**
     * Set the player's score.
     *
     * @param score the new score to assign
     */

    public void setScore(int score) {
    }
}
