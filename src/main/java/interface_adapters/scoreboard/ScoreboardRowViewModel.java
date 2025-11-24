package interface_adapters.scoreboard;



public class ScoreboardRowViewModel {
    private final int rank;
    private final String name;
    private final int score;

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
}
