package View;

import interface_adapters.AddQuestion.AddQuestionController;
import interface_adapters.AddQuestion.LobbyPrepViewModel;
import interface_adapters.ViewManagerModel;
import interface_adapters.Scoreboard.ScoreboardController;

import javax.swing.*;
import java.net.MalformedURLException;

public class LobbyPrepView extends JPanel {

    private final LobbyPrepViewModel viewModel;
    private final ViewManagerModel viewManagerModel;
    private AddQuestionController controller;
    private final ScoreboardController scoreboardController;

    private JLabel pinLabel;
    private JLabel questionCountLabel;
    private JButton addQuestionButton;
    private JButton nextButton;

    public LobbyPrepView(LobbyPrepViewModel viewModel,
                         AddQuestionController controller,
                         ViewManagerModel viewManagerModel,
                         ScoreboardController scoreboardController) {

        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
        this.controller = controller;
        this.scoreboardController = scoreboardController;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        pinLabel = new JLabel("Lobby PIN: " + viewModel.getLobbyPin(),
                SwingConstants.CENTER);

        questionCountLabel = new JLabel(
                "Questions Added: " + viewModel.getQuestionCount(),
                SwingConstants.CENTER
        );

        addQuestionButton = new JButton("Add Question");
        addQuestionButton.addActionListener(e -> {
            if (this.controller == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Internal error: AddQuestionController is not wired in LobbyPrepView.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            AddQuestionView popup = new AddQuestionView(viewModel.getLobbyPin(), this.controller);
            popup.setVisible(true);
        });

        JButton startSessionButton = new JButton("Start Session");
        startSessionButton.addActionListener(e2 -> {
            if (viewModel.getQuestionCount() == 0) {
                JFrame noQuestionFrame = new JFrame("No questions added");
                JPanel noQuestionPanel = new JPanel();
                noQuestionPanel.add(new JLabel("Please add at least one question before starting a session"));

                noQuestionFrame.add(noQuestionPanel);
                noQuestionFrame.setMinimumSize(new java.awt.Dimension(300, 150));
                noQuestionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                noQuestionFrame.pack();
                noQuestionFrame.setLocationRelativeTo(this);
                noQuestionFrame.setVisible(true);
                return;
            }

            if (this.controller == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Internal error: AddQuestionController is not wired in LobbyPrepView for Start Session.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            try {
                controller.sendQuestions(viewModel.getLobbyPin());
            } catch (MalformedURLException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Failed to start session",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

            JOptionPane.showMessageDialog(this,
                    "Session starting!");
        });

        nextButton = new JButton("Next");
        nextButton.addActionListener(e -> {
            if (scoreboardController != null) {
                // When host clicks Next, load scoreboard only for this lobbyPin
                scoreboardController.showScoreboard(viewModel.getLobbyPin());
            } else {
                // Fallback: old behaviour
                viewManagerModel.setState("scoreboard");
                viewManagerModel.firePropertyChange();
            }
        });

        add(pinLabel);
        add(Box.createVerticalStrut(10));
        add(questionCountLabel);
        add(Box.createVerticalStrut(10));
        add(addQuestionButton);
        add(startSessionButton);
        add(nextButton);

        // Listen to view model updates
        viewModel.addPropertyChangeListener(evt -> {
            switch (evt.getPropertyName()) {
                case "popup":
                    String msg = viewModel.getPopupMessage();
                    if (msg != null && !msg.isEmpty()) {
                        JOptionPane.showMessageDialog(this, msg);
                    }
                    break;
                case "questionCount":
                    System.out.println("View: updating question count label to " + viewModel.getQuestionCount());
                    questionCountLabel.setText(
                            "Questions Added: " + viewModel.getQuestionCount());
                    break;
                case "lobbyPin":
                    System.out.println("View: updating lobby pin label to " + viewModel.getLobbyPin());
                    pinLabel.setText("Lobby PIN: " + viewModel.getLobbyPin());
                    break;
            }
        });
    }

    public String getViewName() {
        return "lobby prep";
    }

    public void setAddQuestionController(AddQuestionController addQuestionController) {
        this.controller = addQuestionController;
    }
}
