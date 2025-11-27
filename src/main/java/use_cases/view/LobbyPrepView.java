package use_cases.view;

import interfaceAdapters.AddQuestion.AddQuestionController;
import interfaceAdapters.AddQuestion.LobbyPrepViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

        panel.add(pinLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(questionCountLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(addQuestionButton);

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

