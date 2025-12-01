package view;

import entities.Lobby;
import entities.Question;
import entities.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GameFrameView extends JFrame {

    private final Lobby lobby;
    private final User currentUser;  // single player for now
    private final CardLayout cardLayout;
    private final JPanel cards;
    private int chosen = -1;
    private JPanel questionPanel;
    private JLabel questionLabel;
    private JButton[] choiceButtons;
    private JRadioButton[] radioButtons;
    private int currentQuestionIndex = 0;
    private Question currentQuestion;

    private final int maxQuestions; // how many questions until end

    public GameFrameView(Lobby lobby, User currentUser, int maxQuestions) {
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

        JPanel root = new JPanel(new BorderLayout());


        JPanel infoBar = new JPanel(new BorderLayout());
        infoBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        String infoText = "PIN: " + lobby.getPin() + "   |   User: " + currentUser.getName();
        JLabel infoLabel = new JLabel(infoText, SwingConstants.RIGHT);

        infoBar.add(infoLabel, BorderLayout.EAST);

        root.add(infoBar, BorderLayout.NORTH);
        root.add(cards, BorderLayout.CENTER);


        setContentPane(root);


        loadNextQuestionOrEnd();
    }


    private void createQuestionPanel() {
        questionPanel = new JPanel();
        questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));

        questionLabel = new JLabel("Question text", SwingConstants.CENTER);
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        questionPanel.add(Box.createVerticalStrut(10));
        questionPanel.add(questionLabel);
        questionPanel.add(Box.createVerticalStrut(10));

        // --- RADIO BUTTONS ---
        ButtonGroup group = new ButtonGroup();
        JRadioButton[] radios = new JRadioButton[4];

        for (int i = 0; i < radios.length; i++) {
            radios[i] = new JRadioButton("Choice " + i);
            radios[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            radios[i].setVisible(false);
            group.add(radios[i]);
            questionPanel.add(radios[i]);
            questionPanel.add(Box.createVerticalStrut(5));
        }


        choiceButtons = new JButton[0]; // disable old buttons
        this.radioButtons = radios;     // ADD THIS ARRAY FIELD TO CLASS

        JButton nextBtn = new JButton("Next Question");
        nextBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        nextBtn.addActionListener(e -> handleRadioAnswer());
        questionPanel.add(Box.createVerticalStrut(15));
        questionPanel.add(nextBtn);
    }

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
            JOptionPane.showMessageDialog(this,
                    "Incorrect.\nCorrect answer: " + currentQuestion.getCorrectAnswer());
        }

        currentQuestionIndex++;
        loadNextQuestionOrEnd();
    }


    private void loadNextQuestionOrEnd() {
        List<Question> questions = lobby.getQuestions();

        if (currentQuestionIndex >= questions.size() || currentQuestionIndex >= maxQuestions) {
            showEndScreen();
            return;
        }

        currentQuestion = questions.get(currentQuestionIndex);

        questionLabel.setText(currentQuestion.getPrompt());
        List<String> choices1 = currentQuestion.getChoices();

        for (JRadioButton rb : radioButtons) {
            rb.setSelected(false);
            rb.getModel().setPressed(false);
            rb.getModel().setArmed(false);
        }

        for (int i = 0; i < radioButtons.length; i++) {
            if (i < choices1.size()) {
                radioButtons[i].setText(choices1.get(i));
                radioButtons[i].setVisible(true);
            } else {
                radioButtons[i].setVisible(false);
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

        JButton submitButton = new JButton("Submit");
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.addActionListener(e -> {

        });
        endPanel.add(Box.createVerticalStrut(10));
        endPanel.add(submitButton);

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
