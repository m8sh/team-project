package view;

import interface_adapters.AddQuestion.AddQuestionController;

import javax.swing.*;
import java.util.ArrayList;

public class AddQuestionView extends JFrame {
    private JTextArea questionArea;
    private JTextArea answer1Area;
    private JTextArea answer2Area;
    private JTextArea answer3Area;
    private JTextArea answer4Area;
    private JButton saveButton;
    private JRadioButton correct1;
    private JRadioButton correct2;
    private JRadioButton correct3;
    private JRadioButton correct4;
    private ButtonGroup correctGroup;

    private final AddQuestionController controller;
    private final int lobbyPin;

    public AddQuestionView(int lobbyPin, AddQuestionController controller) {
        super("Edit Question");

        this.controller = controller;
        this.lobbyPin = lobbyPin;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Question box
        questionArea = new JTextArea(3, 40);
        panel.add(new JScrollPane(questionArea));

        // Answers
        panel.add(Box.createVerticalStrut(10));
        panel.add(new JLabel("Answer1:"));
        answer1Area = new JTextArea(3, 25);
        panel.add(new JScrollPane(answer1Area));

        correct1 = new JRadioButton("Correct");
        panel.add(correct1);

        panel.add(Box.createVerticalStrut(10));
        panel.add(new JLabel("Answer2:"));
        answer2Area = new JTextArea(3, 25);
        panel.add(new JScrollPane(answer2Area));

        correct2 = new JRadioButton("Correct");
        panel.add(correct2);

        panel.add(Box.createVerticalStrut(10));
        panel.add(new JLabel("Answer3:"));
        answer3Area = new JTextArea(3, 25);
        panel.add(new JScrollPane(answer3Area));

        correct3 = new JRadioButton("Correct");
        panel.add(correct3);

        panel.add(Box.createVerticalStrut(10));
        panel.add(new JLabel("Answer4:"));
        answer4Area = new JTextArea(3, 25);
        panel.add(new JScrollPane(answer4Area));

        correct4 = new JRadioButton("Correct");
        panel.add(correct4);

        correctGroup = new ButtonGroup();
        correctGroup.add(correct1);
        correctGroup.add(correct2);
        correctGroup.add(correct3);
        correctGroup.add(correct4);

        // Save button
        saveButton = new JButton("Save");
        panel.add(Box.createVerticalStrut(10));
        panel.add(saveButton);

        saveButton.addActionListener(e -> {
            // Collect the data
            String prompt = questionArea.getText();
            ArrayList<String> choices = new ArrayList<>();
            ArrayList<String> answerInput = new ArrayList<>();
            int correctIndex = getCorrectAnswerIndex();
            // Need to figure out how to get lobbyPin from the actual lobby

            String ans1 = answer1Area.getText().trim();
            String ans2 = answer2Area.getText().trim();
            String ans3 = answer3Area.getText().trim();
            String ans4 = answer4Area.getText().trim();

            answerInput.add(ans1);
            answerInput.add(ans2);
            answerInput.add(ans3);
            answerInput.add(ans4);

            boolean gapFound = false;
            if (!ans1.isEmpty()){choices.add(ans1);}
            if (!ans2.isEmpty()){choices.add(ans2);;}
            if (!ans3.isEmpty()){choices.add(ans3);}
            if (!ans4.isEmpty()){choices.add(ans4);}

            for(int i = 0; i < choices.size();i++){
                if (answerInput.get(i).isEmpty()){
                    gapFound = true;
                }

            }

            if (prompt.isEmpty()){
                JFrame noQuestionFrame = new JFrame("No question entered");
                JPanel noQuestionPanel = new JPanel();
                noQuestionPanel.add(new JLabel("Please enter a question prompt before saving"));

                noQuestionFrame.add(noQuestionPanel);
                noQuestionFrame.setMinimumSize(new java.awt.Dimension(300, 150));
                noQuestionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                noQuestionFrame.pack();
                noQuestionFrame.setLocationRelativeTo(this);
                noQuestionFrame.setVisible(true);

                return;
            }
            if (choices.size() < 2){
                JFrame missingAnswerFrame = new JFrame("Not enough answers");
                JPanel missingAnswerPanel = new JPanel();
                missingAnswerPanel.add(new JLabel("Please add at least two answers"));

                missingAnswerFrame.add(missingAnswerPanel);
                missingAnswerFrame.setMinimumSize(new java.awt.Dimension(300, 200));
                missingAnswerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                missingAnswerFrame.setLocationRelativeTo(this);
                missingAnswerFrame.pack();
                missingAnswerFrame.setVisible(true);

                return;
            }

            if  (correctIndex == -1) {
                JFrame noCorrectFrame = new JFrame("No correct answer selected");
                JPanel noCorrectPanel = new JPanel();
                noCorrectPanel.add(new JLabel("Please select which answer is correct"));

                noCorrectFrame.add(noCorrectPanel);
                noCorrectFrame.setMinimumSize(new java.awt.Dimension(300, 200));
                noCorrectFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                noCorrectFrame.setLocationRelativeTo(this);
                noCorrectFrame.pack();
                noCorrectFrame.setVisible(true);

                return;
            }
            if ((correct1.isSelected() && ans1.isEmpty()) ||
                    (correct2.isSelected() && ans2.isEmpty()) ||
                    (correct3.isSelected() && ans3.isEmpty()) ||
                    (correct4.isSelected() && ans4.isEmpty())) {

                JFrame emptyCorrectFrame = new JFrame("Invalid correct answer");
                JPanel emptyCorrectPanel = new JPanel();
                emptyCorrectPanel.add(new JLabel("You marked an empty answer as correct. Please enter text for that answer."));

                emptyCorrectFrame.add(emptyCorrectPanel);
                emptyCorrectFrame.setMinimumSize(new java.awt.Dimension(350, 180));
                emptyCorrectFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                emptyCorrectFrame.setLocationRelativeTo(this);
                emptyCorrectFrame.pack();
                emptyCorrectFrame.setVisible(true);

                return;
            }
            if(gapFound) {
                JFrame emptyCorrectFrame = new JFrame("Invalid answer sequence");
                JPanel emptyCorrectPanel = new JPanel();
                emptyCorrectPanel.add(new JLabel("Please fill in the answers in order without skipping any options"));

                emptyCorrectFrame.add(emptyCorrectPanel);
                emptyCorrectFrame.setMinimumSize(new java.awt.Dimension(350, 180));
                emptyCorrectFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                emptyCorrectFrame.setLocationRelativeTo(this);
                emptyCorrectFrame.pack();
                emptyCorrectFrame.setVisible(true);

                return;
            }
            System.out.println("Save button clicked and passed tests, controller called");

            this.controller.execute(prompt, choices, correctIndex, lobbyPin);
            this.dispose();
            // Close the panel/AddQuestion Popup


        });

        add(panel);
        pack();
        setLocationRelativeTo(null);


    }
    public int getCorrectAnswerIndex() {
        if (correct1.isSelected()) return 0;
        if (correct2.isSelected()) return 1;
        if (correct3.isSelected()) return 2;
        if (correct4.isSelected()) return 3;
        return -1; // none selected
    }


}

