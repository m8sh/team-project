package use_cases.Scoreboard;

import java.util.List;
/**
 * Output data containing scoreboard rows and lobby information.
 */

public class ScoreboardOutputData {

    private final List<Row> rows;
    private final int lobbyPin;
    /**
     * Construct the output data for scoreboard display.
     *
     * @param rows     the list of scoreboard rows
     * @param lobbyPin the lobby PIN associated with this scoreboard
     */

    public ScoreboardOutputData(List<Row> rows, int lobbyPin) {
        this.rows = rows;
        this.lobbyPin = lobbyPin;
    }
    /**
     * Get the scoreboard rows.
     *
     * @return list of rows in the scoreboard
     */

    public List<Row> getRows() {
        return rows;
    }
    /**
     * Get the lobby PIN.
     *
     * @return the lobby PIN
     */

    public int getLobbyPin() {
        return lobbyPin;
    }
    /**
     * Data class representing a single scoreboard row.
     */

    public static class Row {
        private final int rank;
        private final String name;
        private final int score;

        public Row(int rank, String name, int score) {
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
    }
}
