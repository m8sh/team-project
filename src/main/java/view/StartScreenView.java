package view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.security.SecureRandom;
import api_caller.api_caller;
import app.Game;
import interface_adapters.StartScreen.StartScreenController;
import interface_adapters.StartScreen.StartScreenState;
import interface_adapters.StartScreen.StartScreenViewModel;

public class StartScreenView extends JPanel implements PropertyChangeListener {

    private final StartScreenViewModel viewModel;
    private StartScreenController startScreenController;

    private JPanel sesPanel;
    private JButton session;

    private JPanel pinPanel;
    private JTextField pinField;

    private JPanel namePanel;
    private JTextField nameField;

    private JPanel buttonPanel;
    private JButton submit;

    public StartScreenView(StartScreenViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);
        buildUI();
    }

    public void setStartScreenController(StartScreenController controller) {
        this.startScreenController = controller;
    }

    public String getViewName() {
        return StartScreenViewModel.VIEW_NAME;
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

    public void propertyChange(PropertyChangeEvent evt) {
        StartScreenState state = viewModel.getState();

        if (state.getPin() != null){
            pinField.setText(state.getPin());
        }

        if (state.getUsername() != null){
            nameField.setText(state.getUsername());
        }

        if (state.getPin() != null
                && state.getUsername() != null
                && !state.getPin().isEmpty()
                && !state.getUsername().isEmpty()) {
            Game.start(state.getUsername(), state.getPin());
        }

        if (state.getErrorMessage() == null || state.getErrorMessage().isEmpty()) {
            return;
            // go to next page or wtv
        }

        else {
            System.out.println("Error message: " + state.getErrorMessage());
        }

    }
}
