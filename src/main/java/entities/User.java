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
    public String getName() {return this.name;
    }
    public int getScore() {
        return this.score;
    }
    public int getLobbyPin() {
        return this.lobbyPin;}
    public void incrementScore() {
        this.score++;
    }

    public void resetScore() {
        this.score = 0;
    }
}
