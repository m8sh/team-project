package view;

import api_caller.api_caller;
import app.Game;
import interface_adapters.ViewManagerModel;

import javax.swing.*;
import java.awt.*;
import java.security.SecureRandom;

public class StartScreenView extends JPanel {

    private final ViewManagerModel viewManagerModel;
    public static final String VIEW_NAME = "start screen";

    public StartScreenView(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;

        // --- Panels ---
        JPanel sessionPanel = new JPanel();
        JButton createSessionButton = new JButton("Create Session");
        sessionPanel.add(createSessionButton);

        JPanel pinPanel = new JPanel();
        JTextField pinField = new JTextField(10);
        pinPanel.add(new JLabel("Enter PIN:"));
        pinPanel.add(pinField);

        JPanel namePanel = new JPanel();
        JTextField nameField = new JTextField(10);
        namePanel.add(new JLabel("Username:"));
        namePanel.add(nameField);

        JPanel joinPanel = new JPanel();
        JButton joinButton = new JButton("Join");
        joinPanel.add(joinButton);

        // Layout for whole view
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(sessionPanel);
        add(pinPanel);
        add(namePanel);
        add(joinPanel);

        // --- JOIN SESSION ---
        joinButton.addActionListener(e -> {
            String enteredPin = pinField.getText();
            String username = nameField.getText();

            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(StartScreenView.this);

            if (enteredPin.equals("123456")) {
                if (parentFrame != null) parentFrame.setVisible(false);
                Game.start(username, enteredPin);
            } else {
                JOptionPane.showMessageDialog(
                        parentFrame != null ? parentFrame : this,
                        "Incorrect PIN!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // --- CREATE SESSION BUTTON ---
        createSessionButton.addActionListener(e -> {

            // (Optional) hide main window, your project handles this differently depending on setup
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(StartScreenView.this);
            if (parentFrame != null) parentFrame.setVisible(true);

            // ==== Generate PIN ====
            String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            SecureRandom random = new SecureRandom();
            StringBuilder pin = new StringBuilder();
            for (int i = 0; i < 6; i++) {
                pin.append(letters.charAt(random.nextInt(letters.length())));
            }

            // ==== API CALL ====
            try {
                api_caller caller = new api_caller();
                caller.createRoom(pin.toString());
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                        this,
                        "Failed to create room.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // ==== SWITCH TO EXISTING LOBBYPREPVIEW ====
            viewManagerModel.setState("lobby prep");  // must match LobbyPrepView.getViewName()
            viewManagerModel.firePropertyChange();
        });
    }

    public String getViewName() {
        return VIEW_NAME;
    }
}
