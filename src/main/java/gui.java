import javax.swing.*;
import java.awt.event.*;

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

            // --- Join button panel ---
            JPanel buttonPanel = new JPanel();
            JButton submit = new JButton("Join");
            buttonPanel.add(submit);

            // --- Main panel ---
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(sesPanel);
            mainPanel.add(pinPanel);
            mainPanel.add(buttonPanel);

            // --- Frame setup ---
            JFrame frame = new JFrame("Session Join");
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);

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
                    } else {
                        JOptionPane.showMessageDialog(frame, "Incorrect PIN!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            // --- Action for create session button ---
            session.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFrame createFrame = new JFrame("Create Session");
                    createFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                    // Create a panel for the session window
                    JPanel createPanel = new JPanel();
                    createPanel.setLayout(new BoxLayout(createPanel, BoxLayout.Y_AXIS));

                    // Label and Add Question button
                    int pin = 100000 + (int)(Math.random() * 900000);
                    JLabel label = new JLabel("Session Window, PIN: " + pin, SwingConstants.CENTER);
                    JButton addQuestion = new JButton("Add Question");

                    createPanel.add(label);
                    createPanel.add(Box.createVerticalStrut(10));
                    createPanel.add(addQuestion);

                    // Example action for the Add Question button
                    addQuestion.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            JFrame editFrame = new JFrame("Edit Question");
                            editFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                            JPanel panel = new JPanel();
                            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                            JTextArea questionArea = new JTextArea(3, 40);
                            panel.add(new JScrollPane(questionArea));

                            // --- Answer area ---
                            panel.add(Box.createVerticalStrut(10)); // spacing
                            panel.add(new JLabel("Answer1:"));
                            JTextArea answer1Area = new JTextArea(3, 25);
                            panel.add(new JScrollPane(answer1Area));

                            panel.add(Box.createVerticalStrut(10)); // spacing
                            panel.add(new JLabel("Answer2:"));
                            JTextArea answer2Area = new JTextArea(3, 25);
                            panel.add(new JScrollPane(answer2Area));

                            panel.add(Box.createVerticalStrut(10)); // spacing
                            panel.add(new JLabel("Answer3:"));
                            JTextArea answer3Area = new JTextArea(3, 25);
                            panel.add(new JScrollPane(answer3Area));

                            panel.add(Box.createVerticalStrut(10)); // spacing
                            panel.add(new JLabel("Answer4:"));
                            JTextArea answer4Area = new JTextArea(3, 25);
                            panel.add(new JScrollPane(answer4Area));
                            // --- Save button ---
                            JButton saveButton = new JButton("Save");
                            panel.add(Box.createVerticalStrut(10));
                            panel.add(saveButton);

                            editFrame.add(panel);
                            editFrame.pack();
                            editFrame.setLocationRelativeTo(null);
                            editFrame.setVisible(true);
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