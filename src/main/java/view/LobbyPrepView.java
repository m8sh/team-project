package view;

import interface_adapters.AddQuestion.AddQuestionController;
import interface_adapters.AddQuestion.LobbyPrepViewModel;
import interface_adapters.ViewManagerModel;
import interface_adapters.scoreboard.ScoreboardState;

import javax.swing.*;
import java.net.MalformedURLException;


public class LobbyPrepView extends JPanel {
    // Change for AppBuilder, make your view a JPanel

    private final LobbyPrepViewModel viewModel;
    private final ViewManagerModel viewManagerModel;
    private AddQuestionController controller;

    private JLabel pinLabel;
    private JLabel questionCountLabel;
    private JButton addQuestionButton;
    private JButton nextButton;

    public LobbyPrepView(LobbyPrepViewModel viewModel,
                          AddQuestionController controller, ViewManagerModel viewManagerModel) {


        this.viewModel = viewModel;
        this.viewManagerModel= viewManagerModel;
        this.controller = controller;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        pinLabel = new JLabel("Lobby PIN: " + viewModel.getLobbyPin(),
                SwingConstants.CENTER);

        // Question count display
        questionCountLabel = new JLabel(
                "Questions Added: " + viewModel.getQuestionCount(),
                SwingConstants.CENTER
        );

        // Button to open AddQuestionView
        addQuestionButton = new JButton("Add Question");
        addQuestionButton.addActionListener(e -> {
            // Open the AddQuestionView popup with the lobby PIN
            AddQuestionView popup = new AddQuestionView(viewModel.getLobbyPin(), controller);
            popup.setVisible(true);
        });
        // Button to start the session
        JButton startSessionButton = new JButton("Start Session");

        startSessionButton.addActionListener(e2 -> {

            if(viewModel.getQuestionCount() == 0){
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
            try {
                controller.sendQuestions(viewModel.getLobbyPin());
            } catch (MalformedURLException e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Failed to start session",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
            JOptionPane.showMessageDialog(this,
                    "Session starting... (not implemented yet)");
        });
        nextButton = new JButton("Next");
        nextButton.addActionListener(e -> {
            // here we just tell the global ViewManagerModel to switch
            viewManagerModel.setState("scoreboard");
            viewManagerModel.firePropertyChange();
        });

        add(pinLabel);
        add(Box.createVerticalStrut(10));
        add(questionCountLabel);
        add(Box.createVerticalStrut(10));
        add(addQuestionButton);
        add(startSessionButton);
        add(nextButton);

        //Listener here
        viewModel.addPropertyChangeListener(evt -> {
            switch(evt.getPropertyName()) {
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
            }
        });
    }

    public String getViewName() {
        return "lobby prep";
    }
    // Change for AppBuilder, add a get ViewName method

    public void setAddQuestionController(AddQuestionController addQuestionController) {
        this.controller = addQuestionController;
    }
}


