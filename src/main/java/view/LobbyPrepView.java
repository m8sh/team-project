package view;

import interface_adapters.AddQuestion.AddQuestionController;
import interface_adapters.AddQuestion.LobbyPrepViewModel;
import interface_adapters.ViewManagerModel;
import interface_adapters.scoreboard.ScoreboardState;

import javax.swing.*;

//public class LobbyPrepView extends JFrame {
//
//    private static final String VIEW_NAME = "lobby_prep";
//
//    public String getViewName() {
//        return VIEW_NAME;
//    }
//    private final LobbyPrepViewModel viewModel;
//    private AddQuestionController controller = null;
//
//    private JLabel pinLabel;
//    private JLabel questionCountLabel;
//    private JButton addQuestionButton;
//
//    public LobbyPrepView(LobbyPrepViewModel viewModel, AddQuestionController controller) {
//        super("Lobby Prep");
//        this.viewModel = viewModel;
//        this.controller = controller;
//
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//
//        JPanel panel = new JPanel();
//        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//
//        // Lobby PIN display
//        pinLabel = new JLabel("Lobby PIN: " + viewModel.getLobbyPin(),
//                SwingConstants.CENTER);
//
//        // Question count display
//        questionCountLabel = new JLabel(
//                "Questions Added: " + viewModel.getQuestionCount(),
//                SwingConstants.CENTER
//        );
//
//        // Button to open AddQuestionView
//        addQuestionButton = new JButton("Add Question");
//        addQuestionButton.addActionListener(e -> {
//            // Open the AddQuestionView popup with the lobby PIN
//            AddQuestionView popup = new AddQuestionView(viewModel.getLobbyPin(), controller);
//            popup.setVisible(true);
//        });
//
//        panel.add(pinLabel);
//        panel.add(Box.createVerticalStrut(10));
//        panel.add(questionCountLabel);
//        panel.add(Box.createVerticalStrut(10));
//        panel.add(addQuestionButton);
//
//        //Listener here
//        viewModel.addPropertyChangeListener(evt -> {
//            switch(evt.getPropertyName()) {
//                case "popup":
//                    String msg = viewModel.getPopupMessage();
//                    System.out.println("View: popup message = " + msg);
//                    if (msg != null && !msg.isEmpty()) {
//                        JOptionPane.showMessageDialog(this, msg);
//                    }
//                    break;
//                case "questionCount":
//                    System.out.println("View: updating question count label to " + viewModel.getQuestionCount());
//                    questionCountLabel.setText("Questions Added: " + viewModel.getQuestionCount());
//                    break;
//            }
//        });
//
//        add(panel);
//        pack();
//        setLocationRelativeTo(null);
//    }
//
//    private void refreshQuestionCount() {
//        questionCountLabel.setText(
//                "Questions Added: " + viewModel.getQuestionCount());
//    }
//
//    public static void main(String[] args) {
////        SwingUtilities.invokeLater(() -> {
////            LobbyPrepViewModel vm = new LobbyPrepViewModel(123456);
////
////            LobbyPrepView view = new LobbyPrepView(vm);
////            view.setVisible(true);
////        });
//
//
//    }

public class LobbyPrepView extends JPanel {

    private final LobbyPrepViewModel viewModel;
    private final ViewManagerModel viewManagerModel;
    private AddQuestionController addquestioncontroller;

    private JLabel pinLabel;
    private JLabel questionCountLabel;
    private JButton addQuestionButton;
    private JButton nextButton;

    public LobbyPrepView(LobbyPrepViewModel viewModel,
                          AddQuestionController controller, ViewManagerModel viewManagerModel) {

        this.viewModel = viewModel;
        this.viewManagerModel= viewManagerModel;
        this.addquestioncontroller = controller;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        pinLabel = new JLabel("Lobby PIN: " + viewModel.getLobbyPin(),
                SwingConstants.CENTER);

        questionCountLabel = new JLabel(
                "Questions Added: " + viewModel.getQuestionCount(),
                SwingConstants.CENTER
        );

        addQuestionButton = new JButton("Add Question");
        addQuestionButton.addActionListener(e -> {
            AddQuestionView popup =
                    new AddQuestionView(viewModel.getLobbyPin(), controller);
            popup.setVisible(true);
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
        add(nextButton);

        // listen to view model
        viewModel.addPropertyChangeListener(evt -> {
            switch (evt.getPropertyName()) {
                case "popup":
                    String msg = viewModel.getPopupMessage();
                    if (msg != null && !msg.isEmpty()) {
                        JOptionPane.showMessageDialog(this, msg);
                    }
                    break;
                case "questionCount":
                    questionCountLabel.setText(
                            "Questions Added: " + viewModel.getQuestionCount());
                    break;
            }
        });
    }

    public String getViewName() {
        return "lobby prep";
    }

    public void setAddQuestionController(AddQuestionController addQuestionController) {
        this.addquestioncontroller = addQuestionController;
    }
}


