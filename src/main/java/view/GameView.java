package view;

import entities.Lobby;
import entities.Question;
import entities.User;
import interface_adapters.ViewManagerModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GameView extends JPanel {

    public static final String VIEW_NAME = "game";

    private final ViewManagerModel viewManagerModel;

    // Game state
    private Lobby lobby;
    private User currentUser;
    private int maxQuestions;

    private final CardLayout cardLayout;
    private final JPanel cards;

    private int chosen = -1;
    private JPanel questionPanel;
    private JLabel questionLabel;
    private JRadioButton[] radioButtons;
    private JLabel infoLabel;

    private int currentQuestionIndex = 0;
    private Question currentQuestion;

    public GameView(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;

        setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        createQuestionPanel();
        cards.add(questionPanel, "QUESTION");

        // top info bar
        JPanel infoBar = new JPanel(new BorderLayout());
        infoBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        infoLabel = new JLabel("", SwingConstants.RIGHT);
        infoBar.add(infoLabel, BorderLayout.EAST);

        add(infoBar, BorderLayout.NORTH);
        add(cards, BorderLayout.CENTER);
    }

    public String getViewName() {
        return VIEW_NAME;
    }

    /**
     * Call this when you know the Lobby, User and maxQuestions.
     */
    public void startGame(Lobby lobby, User currentUser, int maxQuestions) {
        this.lobby = lobby;
        this.currentUser = currentUser;
        this.maxQuestions = maxQuestions;

        if (lobby != null && currentUser != null) {
            String infoText = "PIN: " + lobby.getPin() + "   |   User: " + currentUser.getName();
            infoLabel.setText(infoText);
        } else {
            infoLabel.setText("");
        }

        currentQuestionIndex = 0;
        currentQuestion = null;

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

        ButtonGroup group = new ButtonGroup();
        radioButtons = new JRadioButton[4];

        for (int i = 0; i < radioButtons.length; i++) {
            JRadioButton rb = new JRadioButton("Choice " + i);
            rb.setAlignmentX(Component.CENTER_ALIGNMENT);
            rb.setVisible(false);
            group.add(rb);
            radioButtons[i] = rb;
            questionPanel.add(rb);
            questionPanel.add(Box.createVerticalStrut(5));
        }

        JButton nextBtn = new JButton("Next Question");
        nextBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        nextBtn.addActionListener(e -> handleRadioAnswer());
        questionPanel.add(Box.createVerticalStrut(15));
        questionPanel.add(nextBtn);
    }

    private void handleRadioAnswer() {
        if (currentQuestion == null || lobby == null || currentUser == null) {
            return;
        }

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

    private void loadNextQuestionOrEnd() {
        if (lobby == null) {
            return;
        }

        List<Question> questions = lobby.getQuestions();

        if (currentQuestionIndex >= questions.size()
                || (maxQuestions > 0 && currentQuestionIndex >= maxQuestions)) {
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

    private void showEndScreen() {
        JPanel endPanel = new JPanel();
        endPanel.setLayout(new BoxLayout(endPanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Game Over!", SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        endPanel.add(Box.createVerticalStrut(10));
        endPanel.add(title);
        endPanel.add(Box.createVerticalStrut(10));

        if (lobby != null) {
            for (User u : lobby.getUsers()) {
                JLabel label = new JLabel(u.getName() + " : " + u.getScore());
                label.setAlignmentX(Component.CENTER_ALIGNMENT);
                endPanel.add(label);
            }
        }

        endPanel.add(Box.createVerticalStrut(20));

        JButton playAgainButton = new JButton("Play Again");
        playAgainButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playAgainButton.addActionListener(e -> resetGame());
        endPanel.add(playAgainButton);

        JButton submitButton = new JButton("Submit");
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.addActionListener(e -> {
            // later: navigate or send scores
            // e.g. viewManagerModel.setState(ScoreboardViewModel.VIEW_NAME);
            //      viewManagerModel.firePropertyChange();
        });
        endPanel.add(Box.createVerticalStrut(10));
        endPanel.add(submitButton);

        cards.add(endPanel, "END");
        cardLayout.show(cards, "END");
    }

    private void resetGame() {
        currentQuestionIndex = 0;
        currentQuestion = null;

        if (lobby != null) {
            for (User u : lobby.getUsers()) {
                u.resetScore();
            }
        }

        loadNextQuestionOrEnd();
    }
}
