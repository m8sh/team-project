package app;

import interface_adapters.scoreboard.ScoreboardRowViewModel;
import interface_adapters.scoreboard.ScoreboardState;
import interface_adapters.scoreboard.ScoreboardViewModel;
import view.ScoreboardView;

import javax.swing.*;
import java.util.List;

public class ScoreboardDemoMain {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            ScoreboardViewModel viewModel = new ScoreboardViewModel();
            ScoreboardState state = viewModel.getState();

            state.setRows(List.of(
                    new ScoreboardRowViewModel(1, "Alice", 5),
                    new ScoreboardRowViewModel(2, "Bob", 4),
                    new ScoreboardRowViewModel(3, "Charlie", 3)
            ));
            state.setErrorMessage(null);

            // Notify view that the state is ready
            viewModel.firePropertyChange();

            // 3. Create the ScoreboardView (our panel)
            ScoreboardView scoreboardView = new ScoreboardView(viewModel);

            // 4. Create a window (JFrame) and set the view as content
            JFrame frame = new JFrame("Scoreboard Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(scoreboardView);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
