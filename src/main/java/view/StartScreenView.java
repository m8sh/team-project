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

        // Create Session
        sesPanel = new JPanel();
        session = new JButton("Create Session");
        sesPanel.add(session);

        // PIN input
        pinPanel = new JPanel();
        pinField = new JTextField(10);
        pinPanel.add(new JLabel("PIN:"));
        pinPanel.add(pinField);

        // Username input
        namePanel = new JPanel();
        nameField = new JTextField(10);
        namePanel.add(new JLabel("Username:"));
        namePanel.add(nameField);

        // Join button
        buttonPanel = new JPanel();
        submit = new JButton("Join");
        buttonPanel.add(submit);

        this.add(sesPanel);
        this.add(pinPanel);
        this.add(namePanel);
        this.add(buttonPanel);

        // JOIN SESSION: delegate to controller
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

        // CREATE SESSION: run use case, then navigate to LobbyPrepView
        session.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (startScreenController == null) {
                    return;
                }
                System.out.println("Create Session button clicked");

                // CA use case (create room, set pin in state, etc.)
                startScreenController.onCreateSessionRequested();

                // UI navigation: switch to LobbyPrepView card
                viewManagerModel.setState("lobby prep"); // must match LobbyPrepView.getViewName()
                viewManagerModel.firePropertyChange();
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

        // Client JOIN flow – when both are filled successfully, start the game
        if (pin != null && !pin.isEmpty()
                && username != null && !username.isEmpty()
                && (error == null || error.isEmpty())) {

            Game.start(username, pin);
            return;
        }

        // Show/log errors if any
        if (error != null && !error.isEmpty()) {
            System.out.println("StartScreen error: " + error);
            // Optionally:
            // JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
