package view;

import app.Game;
import interface_adapters.StartScreen.StartScreenController;
import interface_adapters.StartScreen.StartScreenState;
import interface_adapters.StartScreen.StartScreenViewModel;
import interface_adapters.ViewManagerModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.security.SecureRandom;

public class StartScreenView extends JPanel implements PropertyChangeListener {

    private final StartScreenViewModel viewModel;
    private final ViewManagerModel viewManagerModel;
    private StartScreenController startScreenController;

    private JPanel sesPanel;
    private JButton session;

    private JPanel pinPanel;
    private JTextField pinField;

    private JPanel namePanel;
    private JTextField nameField;

    private JPanel buttonPanel;
    private JButton submit;

    public StartScreenView(StartScreenViewModel viewModel, ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
        this.viewModel.addPropertyChangeListener(this);
        buildUI();
    }

    public void setStartScreenController(StartScreenController controller) {
        this.startScreenController = controller;
    }

    public String getViewName() {
        return StartScreenViewModel.VIEW_NAME; // e.g., "start screen"
    }

    private void buildUI() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        sesPanel = new JPanel();
        session = new JButton("Create Session");
        sesPanel.add(session);

        pinPanel = new JPanel();
        pinField = new JTextField(10);
        pinPanel.add(new JLabel("PIN:"));
        pinPanel.add(pinField);

        namePanel = new JPanel();
        nameField = new JTextField(10);
        namePanel.add(new JLabel("Username:"));
        namePanel.add(nameField);

        buttonPanel = new JPanel();
        submit = new JButton("Join");
        buttonPanel.add(submit);

        this.add(sesPanel);
        this.add(pinPanel);
        this.add(namePanel);
        this.add(buttonPanel);

        // JOIN button -> use controller
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (startScreenController == null) {
                    return;
                }
                String enteredPin = pinField.getText();
                String username = nameField.getText();
                startScreenController.onJoinRequested(enteredPin, username);
            }
        });

        // CREATE SESSION button -> use controller
        session.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (startScreenController == null) {
                    return;
                }
                System.out.println("Create Session button clicked");
                startScreenController.onCreateSessionRequested();
            }
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        StartScreenState state = viewModel.getState();

        String pin = state.getPin();
        String username = state.getUsername();
        String error = state.getErrorMessage();

        if (pin != null) {
            pinField.setText(pin);
        }

        if (username != null) {
            nameField.setText(username);
        }

        // If we have both pin and username (client join path), start the game
        if (pin != null && !pin.isEmpty()
                && username != null && !username.isEmpty()
                && (error == null || error.isEmpty())) {

            Game.start(username, pin);
            return;
        }

        // If we have a pin but no username and no error -> likely host created a room
        if (pin != null && !pin.isEmpty()
                && (username == null || username.isEmpty())
                && (error == null || error.isEmpty())) {

            // Navigate to LobbyPrepView
            viewManagerModel.setState("lobby prep");
            viewManagerModel.firePropertyChange();
            return;
        }

        // If there's an error, you can show it (log or dialog)
        if (error != null && !error.isEmpty()) {
            System.out.println("StartScreen error: " + error);
            // Optionally:
            // JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
