package View;

import entities.Lobby;
import entities.Question;
import entities.User;
import interface_adapters.Scoreboard.ScoreboardController;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GameFrameView extends JFrame {

    private final Lobby lobby;
    private final User currentUser;
    private final CardLayout cardLayout;
    private final JPanel cards;

    private int chosen = -1;
    private JPanel questionPanel;
    private JLabel questionLabel;
    private JRadioButton[] radioButtons;
    private int currentQuestionIndex = 0;
    private Question currentQuestion;

    private final ScoreboardController scoreboardController;
    private final int maxQuestions;

    public GameFrameView(Lobby lobby,
                         User currentUser,
                         int maxQuestions,
                         ScoreboardController scoreboardController) {
        this.lobby = lobby;
        this.currentUser = currentUser;
        this.maxQuestions = maxQuestions;
        this.scoreboardController = scoreboardController;

        setTitle("Quiz Game");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        // ------- QUESTION PANEL -------
        createQuestionPanel();
        cards.add(questionPanel, "QUESTION");

        // ------- ROOT LAYOUT -------
        JPanel root = new JPanel(new BorderLayout());

        JPanel infoBar = new JPanel(new BorderLayout());
        infoBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        String infoText = "PIN: " + lobby.getPin()
                + "   |   User: " + currentUser.getName();
        JLabel infoLabel = new JLabel(infoText, SwingConstants.RIGHT);

        infoBar.add(infoLabel, BorderLayout.EAST);

        root.add(infoBar, BorderLayout.NORTH);
        root.add(cards, BorderLayout.CENTER);

        setContentPane(root);

        // Start with first question or end screen
        loadNextQuestionOrEnd();
    }

    // ----------------------------------------------------
    //  UI construction for the question screen
    // ----------------------------------------------------
    private void createQuestionPanel() {
        questionPanel = new JPanel();
        questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));

        questionLabel = new JLabel("Question text", SwingConstants.CENTER);
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        questionPanel.add(Box.createVerticalStrut(10));
        questionPanel.add(questionLabel);
        questionPanel.add(Box.createVerticalStrut(10));

        ButtonGroup group = new ButtonGroup();
        JRadioButton[] radios = new JRadioButton[4];

        for (int i = 0; i < radios.length; i++) {
            radios[i] = new JRadioButton("Choice " + i);
            radios[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            radios[i].setVisible(false); // hidden until we have real choices
            group.add(radios[i]);
            questionPanel.add(radios[i]);
            questionPanel.add(Box.createVerticalStrut(5));
        }

        this.radioButtons = radios;

        JButton nextBtn = new JButton("Next Question");
        nextBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        nextBtn.addActionListener(e -> handleRadioAnswer());

        questionPanel.add(Box.createVerticalStrut(15));
        questionPanel.add(nextBtn);
    }

    // ----------------------------------------------------
    //  Handle answer and move to the next question
    // ----------------------------------------------------
    private void handleRadioAnswer() {
        chosen = -1;

        for (int i = 0; i < radioButtons.length; i++) {
            if (radioButtons[i].isVisible() && radioButtons[i].isSelected()) {
                chosen = i;
                break;
            }
        }

        if (chosen == -1) {
            JOptionPane.showMessageDialog(this, "Please select an answer.");
            return;
        }

        if (chosen == currentQuestion.getCorrectIndex()) {
            currentUser.incrementScore();
            JOptionPane.showMessageDialog(this, "Correct!");
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Incorrect.\nCorrect answer: " + currentQuestion.getCorrectAnswer()
            );
        }

        currentQuestionIndex++;
        loadNextQuestionOrEnd();
    }

    // ----------------------------------------------------
    //  Load the next question or show the end screen
    // ----------------------------------------------------
    private void loadNextQuestionOrEnd() {
        List<Question> questions = lobby.getQuestions();

        if (questions == null || questions.isEmpty()) {
            System.out.println("[GameFrame] No questions in lobby, showing end screen immediately.");
            showEndScreen();
            return;
        }

        if (currentQuestionIndex >= questions.size() || currentQuestionIndex >= maxQuestions) {
            showEndScreen();
            return;
        }

        currentQuestion = questions.get(currentQuestionIndex);
        questionLabel.setText(currentQuestion.getPrompt());

        List<String> choices = currentQuestion.getChoices();

        // Reset button state
        for (JRadioButton rb : radioButtons) {
            rb.setSelected(false);
            rb.getModel().setPressed(false);
            rb.getModel().setArmed(false);
        }

        // Fill and show only the needed radio buttons
        for (int i = 0; i < radioButtons.length; i++) {
            if (i < choices.size()) {
                radioButtons[i].setText(choices.get(i));
                radioButtons[i].setVisible(true);
            } else {
                radioButtons[i].setVisible(false);
            }
        }

        cardLayout.show(cards, "QUESTION");
    }

    // ----------------------------------------------------
    //  End Screen (display only; save happens on Submit)
    // ----------------------------------------------------
    private void showEndScreen() {
        JPanel endPanel = new JPanel();
        endPanel.setLayout(new BoxLayout(endPanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Game Over!", SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        endPanel.add(Box.createVerticalStrut(10));
        endPanel.add(title);
        endPanel.add(Box.createVerticalStrut(10));

        System.out.println("[GameFrame] Final scores for lobby " + lobby.getPin() + ":");
        for (User u : lobby.getUsers()) {
            System.out.println("    " + u.getName() + " -> " + u.getScore());
            JLabel label = new JLabel(u.getName() + " : " + u.getScore());
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            endPanel.add(label);
        }

        endPanel.add(Box.createVerticalStrut(20));

        // We NO LONGER save here; save happens when Submit is clicked

        JButton playAgainButton = new JButton("Play Again");
        playAgainButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playAgainButton.addActionListener(e -> resetGame());
        endPanel.add(playAgainButton);

        JButton submitButton = new JButton("Submit");
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.addActionListener(e -> {
            if (scoreboardController != null) {
                System.out.println("[GameFrame] Submit clicked, calling saveResults + showScoreboard for pin "
                        + lobby.getPin());
                // which assigns rank and persists.
                scoreboardController.saveResults(lobby.getPin(), lobby.getUsers());
                scoreboardController.showScoreboard(lobby.getPin());
            } else {
                System.out.println("[GameFrame] WARNING: scoreboardController is null on Submit");
            }
            // dispose(); // keep commented if you want the window to stay open
        });
        endPanel.add(Box.createVerticalStrut(10));
        endPanel.add(submitButton);

        cards.add(endPanel, "END");
        cardLayout.show(cards, "END");
    }

    // ----------------------------------------------------
    //  Reset game (if you use Play Again)
    // ----------------------------------------------------
    private void resetGame() {
        currentQuestionIndex = 0;
        currentQuestion = null;

        for (User u : lobby.getUsers()) {
            u.resetScore();
        }

        loadNextQuestionOrEnd();
    }
}
