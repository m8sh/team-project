package use_cases.Scoreboard;

import java.util.List;

public class ScoreboardOutputData {

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

    private final List<Row> rows;
    private final int lobbyPin;

    public ScoreboardOutputData(List<Row> rows, int lobbyPin) {
        this.rows = rows;
        this.lobbyPin = lobbyPin;
    }

    public List<Row> getRows() {
        return rows;
    }

    public int getLobbyPin() {
        return lobbyPin;
    }
}
