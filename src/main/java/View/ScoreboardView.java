package View;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import interface_adapters.Scoreboard.ScoreboardController;
import interface_adapters.Scoreboard.ScoreboardRowViewModel;
import interface_adapters.Scoreboard.ScoreboardState;
import interface_adapters.Scoreboard.ScoreboardViewModel;

/**
 * Swing view that displays the scoreboard to the user.
 *
 * <p>
 * It listens to changes in the {@link ScoreboardViewModel} and updates
 * the table accordingly, and forwards button actions to the
 * {@link ScoreboardController}.
 * </p>
 */
@SuppressWarnings({"checkstyle:ClassDataAbstractionCoupling", "checkstyle:SuppressWarnings"})
public class ScoreboardView extends JPanel implements PropertyChangeListener {

    private final ScoreboardViewModel scoreboardViewModel;

    private final JTable table;
    private final ScoreboardTableModel tableModel;
    private final JLabel errorLabel;
    // private final JButton endGameButton;
    private final JButton refreshButton;
    private final JLabel pinLabel;

    private ScoreboardController scoreboardController;
    /**
     * Create a new scoreboard view.
     *
     * @param scoreboardViewModel the view model that exposes scoreboard state
     */

    public ScoreboardView(ScoreboardViewModel scoreboardViewModel) {
        this.scoreboardViewModel = scoreboardViewModel;
        this.scoreboardViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());

        // ---------- Top panel: Title + Lobby PIN ----------
        final JPanel topPanel = new JPanel(new BorderLayout());

        final JLabel title = new JLabel("Scoreboard");
        topPanel.add(title, BorderLayout.WEST);

        pinLabel = new JLabel("");
        topPanel.add(pinLabel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // ---------- Center: table ----------
        tableModel = new ScoreboardTableModel();
        table = new JTable(tableModel);
        final JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // ---------- Bottom panel: error + buttons ----------
        final JPanel bottomPanel = new JPanel(new BorderLayout());

        errorLabel = new JLabel("");
        bottomPanel.add(errorLabel, BorderLayout.CENTER);

        final JPanel buttonPanel = new JPanel();
        refreshButton = new JButton("Refresh");
        // endGameButton = new JButton("End Game");
        buttonPanel.add(refreshButton);
        // buttonPanel.add(endGameButton);

        bottomPanel.add(buttonPanel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        // Wire button actions
        wireActions();

        // Initial render
        refreshFromState();
    }

    public String getViewName() {
        return ScoreboardViewModel.VIEW_NAME;
    }

    public void setScoreboardController(ScoreboardController scoreboardController) {
        this.scoreboardController = scoreboardController;
    }

    @SuppressWarnings({"checkstyle:AvoidInlineConditionals", "checkstyle:LambdaBodyLength", "checkstyle:ReturnCount"})
    private void wireActions() {
        refreshButton.addActionListener(e -> {
            if (scoreboardController == null) {
                return;
            }
            ScoreboardState state = scoreboardViewModel.getState();
            int pin = (state != null) ? state.getLobbyPin() : 0;

            // If we don't know the lobby pin yet (typical for host first time),
            // ask the user for it and store it in the state.
            if (pin == 0) {
                System.out.println("[ScoreboardView] Refresh clicked but lobbyPin is not set in state");

                final String input = JOptionPane.showInputDialog(
                        this,
                        "Enter Lobby PIN to load the scoreboard:",
                        "Lobby PIN",
                        JOptionPane.QUESTION_MESSAGE
                );
                if (input == null || input.isBlank()) {
                    return;
                    // user cancelled or empty
                }

                try {
                    pin = Integer.parseInt(input.trim());
                }
                catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Invalid lobby PIN. Please enter a number.",
                            "Invalid PIN",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                // Save this pin into the state so next refresh doesn't ask again
                if (state == null) {
                    state = new ScoreboardState();
                }
                state.setLobbyPin(pin);
                scoreboardViewModel.setState(state);
                scoreboardViewModel.firePropertyChange();
            }

            System.out.println("[ScoreboardView] Refresh clicked, reloading lobby " + pin);
            scoreboardController.showScoreboard(pin);
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            refreshFromState();
        }
    }

    @SuppressWarnings({"checkstyle:AvoidInlineConditionals", "checkstyle:ReturnCount"})
    private void refreshFromState() {
        final ScoreboardState state = scoreboardViewModel.getState();
        if (state == null) {
            return;
        }

        final List<ScoreboardRowViewModel> rows =
                state.getRows() != null ? state.getRows() : List.of();
        tableModel.setRows(rows);

        final String error = state.getErrorMessage();
        errorLabel.setText(error == null ? "" : error);

        final int pin = state.getLobbyPin();
        if (pin != 0) {
            pinLabel.setText("Lobby PIN: " + pin);
        }
        else {
            pinLabel.setText("");
        }
    }

    private static final class ScoreboardTableModel extends AbstractTableModel {

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

        @SuppressWarnings("checkstyle:ReturnCount")
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            final ScoreboardRowViewModel row = rows.get(rowIndex);
            switch (columnIndex) {
                case 0: return row.getRank();
                case 1: return row.getName();
                case 2: return row.getScore();
                default: return "";
            }
        }
    }
}
