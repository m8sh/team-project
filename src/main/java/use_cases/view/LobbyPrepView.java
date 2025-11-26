package use_cases.view;

import interfaceAdapters.AddQuestion.AddQuestionController;
import interfaceAdapters.AddQuestion.LobbyPrepViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;


public class LobbyPrepView extends JFrame {


    private final LobbyPrepViewModel viewModel;
    private AddQuestionController controller = null;

    private JLabel pinLabel;
    private JLabel questionCountLabel;
    private JButton addQuestionButton;

    public LobbyPrepView(LobbyPrepViewModel viewModel, AddQuestionController controller) {
        super("Lobby Prep");
        this.viewModel = viewModel;
        this.controller = controller;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Lobby PIN display
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

        panel.add(pinLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(questionCountLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(addQuestionButton);
        panel.add(startSessionButton);

        //Listener here
        viewModel.addPropertyChangeListener(evt -> {
            switch(evt.getPropertyName()) {
                case "popup":
                    String msg = viewModel.getPopupMessage();
                    System.out.println("View: popup message = " + msg);
                    if (msg != null && !msg.isEmpty()) {
                        JOptionPane.showMessageDialog(this, msg);
                    }
                    break;
                case "questionCount":
                    System.out.println("View: updating question count label to " + viewModel.getQuestionCount());
                    questionCountLabel.setText("Questions Added: " + viewModel.getQuestionCount());
                    break;
            }
        });

        add(panel);
        pack();
        setLocationRelativeTo(null);
    }

    private void refreshQuestionCount() {
        questionCountLabel.setText(
                "Questions Added: " + viewModel.getQuestionCount());
    }

    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            LobbyPrepViewModel vm = new LobbyPrepViewModel(123456);
//
//            LobbyPrepView view = new LobbyPrepView(vm);
//            view.setVisible(true);
//        });


    }


}

