package entities;

import java.util.ArrayList;
import java.util.List;

public class Lobby {

    private final int pin;
    private final List<Question> questions;
    private final List<User> users;

    /**
     * Create a new Lobby with a given pin.
     * Questions and users lists start empty.
     */
    public Lobby(int pin) {
        this.pin = pin;
        this.questions = new ArrayList<>();
        this.users = new ArrayList<>();
    }

    public int getPin() {
        return pin;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public List<User> getUsers() {
        return users;
    }

    /**
     * Replace the current questions list with the given list.
     * Used by the client when questions are received from the server.
     */
    public void setQuestions(List<Question> newQuestions) {
        this.questions.clear();
        if (newQuestions != null) {
            this.questions.addAll(newQuestions);
        }
    }

    /**
     * Add a single question to the lobby.
     * Used by the AddQuestion use case on the host side.
     */
    public void addQuestion(Question question) {
        if (question != null) {
            this.questions.add(question);
        }
    }

    /**
     * Add a user (player) to this lobby.
     * Used on the client side when the player joins.
     */
    public void addUser(User user) {
        if (user != null) {
            this.users.add(user);
        }
    }
}
