package view;

import interface_adapters.scoreboard.ScoreboardController;
import interface_adapters.scoreboard.ScoreboardRowViewModel;
import interface_adapters.scoreboard.ScoreboardState;
import interface_adapters.scoreboard.ScoreboardViewModel;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class ScoreboardView extends JPanel implements PropertyChangeListener {

    private final ScoreboardViewModel scoreboardViewModel;

    private final JTable table;
    private final ScoreboardTableModel tableModel;
    private final JLabel errorLabel;
    private final JButton endGameButton;
    private final JLabel pinLabel;
    private ScoreboardController scoreboardController;

    public ScoreboardView(ScoreboardViewModel scoreboardViewModel) {
        this.scoreboardViewModel = scoreboardViewModel;
        this.scoreboardViewModel.addPropertyChangeListener(this);

        // Layout for the whole panel
        setLayout(new BorderLayout());

        // Title and Pin panel
        JPanel topPanel = new JPanel(new BorderLayout());

        JLabel title = new JLabel("Scoreboard");
        topPanel.add(title, BorderLayout.WEST);

        pinLabel = new JLabel("");
        topPanel.add(pinLabel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Table + scroll pane
        tableModel = new ScoreboardTableModel();
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom panel: error label + End Game button
        JPanel bottomPanel = new JPanel(new BorderLayout());

        errorLabel = new JLabel("");
        bottomPanel.add(errorLabel, BorderLayout.CENTER);

        endGameButton = new JButton("End Game");
        bottomPanel.add(endGameButton, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        // Connect the button to its action
        wireActions();

        // Make sure view shows the current state right away
        refreshFromState();
    }

    public String getViewName() {
        return ScoreboardViewModel.VIEW_NAME;
    }

    public void setScoreboardController(ScoreboardController scoreboardController) {
        this.scoreboardController = scoreboardController;
    }

    // Called once in constructor to attach behaviour to the button.
    private void wireActions() {
        endGameButton.addActionListener(e -> {
            if (scoreboardController != null) {
                    scoreboardController.endSession();
            }
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            refreshFromState();
        }
    }

    // Pull data out of ScoreboardState and show it in the table + error label.
    private void refreshFromState() {
        ScoreboardState state = scoreboardViewModel.getState();
        if (state == null) {
            return;
        }

        List<ScoreboardRowViewModel> rows =
                state.getRows() != null ? state.getRows() : List.of();
        tableModel.setRows(rows);

        String error = state.getErrorMessage();
        errorLabel.setText(error == null ? "" : error);
    }

    private static class ScoreboardTableModel extends AbstractTableModel {

        private final String[] columns = {"Rank", "Name", "Score"};
        private List<ScoreboardRowViewModel> rows = new ArrayList<>();

        public void setRows(List<ScoreboardRowViewModel> rows) {
            this.rows = new ArrayList<>(rows);
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return rows.size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public String getColumnName(int column) {
            return columns[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            ScoreboardRowViewModel row = rows.get(rowIndex);
            switch (columnIndex) {
                case 0: return row.getRank();
                case 1: return row.getName();
                case 2: return row.getScore();
                default: return "";
            }
        }
    }
}