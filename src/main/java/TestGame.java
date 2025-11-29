import entities.GameFrame;
import entities.Lobby;
import entities.Question;
import entities.User;

import javax.swing.*;
import java.util.Arrays;

public class TestGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create a lobby with a pin
            Lobby lobby = new Lobby(123456);

            // Add players
            User player1 = new User("Player 1", 123456);
            lobby.addUser(player1);

            // Add some questions
            lobby.addQuestion(new Question(
                    "2 + 2 = ?",
                    Arrays.asList("3", "4", "5", "22"),
                    1  // "4"
            ));
            lobby.addQuestion(new Question(
                    "Capital of France?",
                    Arrays.asList("Berlin", "Paris", "Rome", "Madrid"),
                    1  // "Paris"
            ));
            lobby.addQuestion(new Question(
                    "Which is a Java keyword?",
                    Arrays.asList("banana", "if", "loop", "fun"),
                    1  // "if"
            ));

            // maxQuestions = 3 here, but you can set it to 2 etc.
            GameFrame gameFrame = new GameFrame(lobby, player1, 3);
            gameFrame.setVisible(true);
        });
    }
}
