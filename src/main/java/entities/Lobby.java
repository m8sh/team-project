package entities;

import java.util.ArrayList;
import java.util.List;

public class Lobby {
    private int pin;
    private List<Question> questions;
    private List<User> users;
    // add list of player/users

    public Lobby(int pin) {
        this.pin = pin;
        this.questions = new ArrayList<>();
        this.users = new ArrayList<>();
    }
    public void addQuestion(Question question) {
        questions.add(question);
    }
    public void addUser(User user) {
        users.add(user);
    }
    public List<Question> getQuestions() {
        return new ArrayList<>(questions);
    }
    public int getPin() {
        return pin;
    }

    public List<User> getUsers() {
        return new ArrayList<>(users);
    }
}
//Empty placeholder at the moment
