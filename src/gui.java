import javax.swing.*;
import java.awt.event.*;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
public class gui {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // --- Session creation panel ---
            JPanel sesPanel = new JPanel();
            JButton session = new JButton("Create Session");
            sesPanel.add(session);

            // --- PIN entry panel ---
            JPanel pinPanel = new JPanel();
            JTextField pinField = new JTextField(10);
            pinPanel.add(new JLabel("PIN:"));
            pinPanel.add(pinField);

            JPanel namePanel = new JPanel();
            JTextField nameField = new JTextField(10);
            pinPanel.add(new JLabel("Username:"));
            pinPanel.add(nameField);

            // --- Join button panel ---
            JPanel buttonPanel = new JPanel();
            JButton submit = new JButton("Join");
            buttonPanel.add(submit);


            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(sesPanel);
            mainPanel.add(pinPanel);
            mainPanel.add(buttonPanel);

            JFrame mainframe = new JFrame("Session Join");
            mainframe.setContentPane(mainPanel);
            mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainframe.pack();
            mainframe.setVisible(true);

            // --- Action for Join button ---
            submit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String enteredPin = pinField.getText();

                    // You can change this PIN check
                    if (enteredPin.equals("1234")) {
                        JFrame roomFrame = new JFrame("Room");
                        JLabel label = new JLabel("Welcome to the Room, waiting to start", SwingConstants.CENTER);
                        roomFrame.add(label);
                        roomFrame.setSize(300, 200);
                        roomFrame.setLocationRelativeTo(null);
                        roomFrame.setVisible(true);
                        mainframe.setVisible(false);
                    } else {
                        JOptionPane.showMessageDialog(mainframe, "Incorrect PIN!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            // --- Action for create session button ---
            session.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFrame createFrame = new JFrame("Create Session");
                    createFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    createFrame.setSize(800, 600);
                    mainframe.setVisible(false);

                    // Create a panel for the session window
                    JPanel createPanel = new JPanel();
                    createPanel.setLayout(new BoxLayout(createPanel, BoxLayout.Y_AXIS));

                    // Label and Add Question button
                    String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
                    SecureRandom random = new SecureRandom();
                    StringBuilder pin = new StringBuilder();

                    for (int i = 0; i < 6; i++) {
                        int index = random.nextInt(letters.length());
                        pin.append(letters.charAt(index));
                    }

                    JLabel label = new JLabel("Session Window, PIN: " + pin, SwingConstants.CENTER);
                    JButton addQuestion = new JButton("Add Question");
                    JButton Launch = new JButton("Launch Session");

                    createPanel.add(label);
                    createPanel.add(Launch);
                    createPanel.add(Box.createVerticalStrut(10));
                    createPanel.add(addQuestion);

                    Question[] questions = new Question[] {
                            new Question("What is 2 + 2?", Arrays.asList("3", "4", "5"), 1),
                            new Question("What is the capital of France?", Arrays.asList("Paris", "Berlin", "Rome"), 0),
                            new Question("Which language runs on the JVM?", Arrays.asList("Python", "Java", "C++"), 1)
                    };

                    // Example action for the Add Question button
                    addQuestion.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {

                            JFrame editFrame = new JFrame("Edit Question");
                            editFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                            // --- picking Answer area ---
                            JPanel panel = new JPanel();
                            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                            JTextArea questionArea = new JTextArea(3, 40);
                            panel.add(new JScrollPane(questionArea));


                            // --- Answer area ---
                            panel.add(Box.createVerticalStrut(10));
                            panel.add(new JLabel("Answer1:"));
                            JTextArea answer1Area = new JTextArea(3, 25);
                            panel.add(new JScrollPane(answer1Area));

                            panel.add(Box.createVerticalStrut(10));
                            panel.add(new JLabel("Answer2:"));
                            JTextArea answer2Area = new JTextArea(3, 25);
                            panel.add(new JScrollPane(answer2Area));

                            panel.add(Box.createVerticalStrut(10));
                            panel.add(new JLabel("Answer3:"));
                            JTextArea answer3Area = new JTextArea(3, 25);
                            panel.add(new JScrollPane(answer3Area));

                            panel.add(Box.createVerticalStrut(10));
                            panel.add(new JLabel("Answer4:"));
                            JTextArea answer4Area = new JTextArea(3, 25);
                            panel.add(new JScrollPane(answer4Area));

                            JFrame answerframe = new JFrame("Pick the correct answer");
                            answerframe.setSize(100, 100);
                            answerframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                            String[] choices = {"Answer 1", "Answer 2", "Answer 3", "Answer 4"};
                            JComboBox<String> pickBox = new JComboBox<>(choices);
                            answerframe.setLocation(400, 500);
                            answerframe.add(pickBox);
                            answerframe.setVisible(true);
                            // --- Save button ---

                            JButton saveButton = new JButton("Save");
                            panel.add(Box.createVerticalStrut(20));
                            panel.add(saveButton);


                            editFrame.add(panel);
                            editFrame.pack();
                            editFrame.setLocationRelativeTo(null);
                            editFrame.setVisible(true);

                            // ---action listener for Save button ---
                            final String[] savedQuestion = new String[1];
                            final String[] savedA1 = new String[1];
                            final String[] savedA2 = new String[1];
                            final String[] savedA3 = new String[1];
                            final String[] savedA4 = new String[1];
                            final String[] savedCorrect = new String[1];

                            saveButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {

                                    // Save everything
                                    savedQuestion[0] = questionArea.getText();
                                    savedA1[0] = answer1Area.getText();
                                    savedA2[0] = answer2Area.getText();
                                    savedA3[0] = answer3Area.getText();
                                    savedA4[0] = answer4Area.getText();



                                    String correctAnswer = "";
                                    switch ((String) pickBox.getSelectedItem()) {
                                        case "Answer 1":
                                            correctAnswer = savedA1[0];
                                            break;
                                        case "Answer 2":
                                            correctAnswer = savedA2[0];
                                            break;
                                        case "Answer 3":
                                            correctAnswer = savedA3[0];
                                            break;
                                        case "Answer 4":
                                            correctAnswer = savedA4[0];
                                            break;
                                    }
                                    questions.add(new Question(savedQuestion[0], Arrays.asList(savedA1[0], savedA2[0], savedA3[0], savedA4[0]), pickBox.getSelectedIndex()));
                                    JLabel Qlabel = new JLabel("Q: " + savedQuestion[0] + ",  A: " + correctAnswer, SwingConstants.CENTER);
                                    createPanel.add(Qlabel);
                                    createPanel.revalidate();
                                    createPanel.repaint();

                                    // Close the Edit Frame
                                    editFrame.dispose();
                                    answerframe.dispose();
                                }
                            });
                        }
                    });

                    createFrame.setContentPane(createPanel);
                    createFrame.setSize(300, 200);
                    createFrame.setLocationRelativeTo(null);
                    createFrame.setVisible(true);
                }
            });
        });
    }
}

