package use_cases.view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.SecureRandom;
import api_caller.api_caller;

public class StartScreenView {
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
                    String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
                    SecureRandom random = new SecureRandom();
                    StringBuilder pin = new StringBuilder();

                    for (int i = 0; i < 6; i++) {
                        int index = random.nextInt(letters.length());
                        pin.append(letters.charAt(index));
                    }
                    try {
                        api_caller apiCaller = new api_caller();
                        apiCaller.createRoom(pin.toString());
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }

                    // Take the api into the itamar's screen


                    // Create a panel for the session window
                    JPanel createPanel = new JPanel();
                    createPanel.setLayout(new BoxLayout(createPanel, BoxLayout.Y_AXIS));

                    JLabel label = new JLabel("Session Window, PIN: " + pin, SwingConstants.CENTER);
                    JButton addQuestion = new JButton("Add Question");
                    JButton Launch = new JButton("Launch Session");

                    createPanel.add(label);
                    createPanel.add(Launch);
                    createPanel.add(Box.createVerticalStrut(10));
                    createPanel.add(addQuestion);
                }
            });
        });
    }
}
