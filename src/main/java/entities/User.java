package entities;

public class User {
    private final String name;
    private int score;
    private final int lobbyPin;

    public User(String name, int lobbyPin) {
        this.name = name;
        this.score = 0;
        this.lobbyPin = lobbyPin;
    }

    // Optional extra constructor if you want to load users from Supabase with an existing score
    public User(String name, int lobbyPin, int score) {
        this.name = name;
        this.lobbyPin = lobbyPin;
        this.score = score;
    }

    public String getName() {
        return this.name;
    }

    public int getScore() {
        return this.score;
    }

    public int getLobbyPin() {
        return this.lobbyPin;
    }

    public void incrementScore() {
        this.score++;
    }

    public void resetScore() {
        this.score = 0;
    }

    // If you ever need to set score directly (e.g. from DB):
    public void setScore(int score) {
        this.score = score;
    }
}
