import entities.Lobby;
import entities.Question;
import entities.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class GameFrame extends JFrame {

    private final Lobby lobby;
    private final User currentUser;  // single player for now
    private final CardLayout cardLayout;
    private final JPanel cards;

    private JPanel questionPanel;
    private JLabel questionLabel;
    private JButton[] choiceButtons;

    private int currentQuestionIndex = 0;
    private Question currentQuestion;

    private final int maxQuestions; // how many questions until end

    public GameFrame(Lobby lobby, User currentUser, int maxQuestions) {
        this.lobby = lobby;
        this.currentUser = currentUser;
        this.maxQuestions = maxQuestions;

        setTitle("Quiz Game");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        createQuestionPanel();
        cards.add(questionPanel, "QUESTION");

        setContentPane(cards);

        loadNextQuestionOrEnd();
    }

    // Build question UI
    private void createQuestionPanel() {
        questionPanel = new JPanel();
        questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));

        questionLabel = new JLabel("Question text", SwingConstants.CENTER);
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        questionPanel.add(Box.createVerticalStrut(10));
        questionPanel.add(questionLabel);
        questionPanel.add(Box.createVerticalStrut(10));

        choiceButtons = new JButton[4]; // up to 4 choices
        for (int i = 0; i < choiceButtons.length; i++) {
            final int idx = i;
            JButton btn = new JButton("Choice " + i);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.addActionListener((ActionEvent e) -> handleAnswer(idx));
            choiceButtons[i] = btn;
            questionPanel.add(btn);
            questionPanel.add(Box.createVerticalStrut(5));
        }
    }

    // When a choice is clicked
    private void handleAnswer(int chosenIndex) {
        if (currentQuestion != null && chosenIndex == currentQuestion.getCorrectIndex()) {
            currentUser.incrementScore();
            JOptionPane.showMessageDialog(this, "Correct!");
        } else {
            JOptionPane.showMessageDialog(this,
                    "Incorrect.\nCorrect answer: " + currentQuestion.getCorrectAnswer());
        }

        currentQuestionIndex++;
        loadNextQuestionOrEnd();
    }

    // Decide: next question or end screen
    private void loadNextQuestionOrEnd() {
        List<Question> questions = lobby.getQuestions();

        if (currentQuestionIndex >= questions.size() || currentQuestionIndex >= maxQuestions) {
            showEndScreen();
            return;
        }

        currentQuestion = questions.get(currentQuestionIndex);

        questionLabel.setText(currentQuestion.getPrompt());

        List<String> choices = currentQuestion.getChoices();
        for (int i = 0; i < choiceButtons.length; i++) {
            if (i < choices.size()) {
                choiceButtons[i].setText(choices.get(i));
                choiceButtons[i].setVisible(true);
            } else {
                choiceButtons[i].setVisible(false);
            }
        }

        cardLayout.show(cards, "QUESTION");
    }

    // Build end screen with scores and "Play Again"
    private void showEndScreen() {
        JPanel endPanel = new JPanel();
        endPanel.setLayout(new BoxLayout(endPanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Game Over!", SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        endPanel.add(Box.createVerticalStrut(10));
        endPanel.add(title);
        endPanel.add(Box.createVerticalStrut(10));

        // Show each player's score
        for (User u : lobby.getUsers()) {
            JLabel label = new JLabel(u.getName() + " : " + u.getScore());
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            endPanel.add(label);
        }

        endPanel.add(Box.createVerticalStrut(20));

        JButton playAgainButton = new JButton("Play Again");
        playAgainButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playAgainButton.addActionListener(e -> resetGame());
        endPanel.add(playAgainButton);

        JButton exitButton = new JButton("Exit");
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.addActionListener(e -> dispose());
        endPanel.add(Box.createVerticalStrut(10));
        endPanel.add(exitButton);

        cards.add(endPanel, "END");
        cardLayout.show(cards, "END");
    }

    // Reset scores + restart questions
    private void resetGame() {
        currentQuestionIndex = 0;
        currentQuestion = null;

        for (User u : lobby.getUsers()) {
            u.resetScore();
        }

        loadNextQuestionOrEnd();
    }
}
