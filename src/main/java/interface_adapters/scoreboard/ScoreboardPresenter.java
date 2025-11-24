package interface_adapters.scoreboard;

import interface_adapters.ViewModel; // if needed elsewhere
import use_cases.scoreboard.ScoreboardOutputBoundary;
import use_cases.scoreboard.ScoreboardOutputData;

import java.util.List;
import java.util.stream.Collectors;

public class ScoreboardPresenter implements ScoreboardOutputBoundary {

    // 1) Talk to the *view model*, not directly to the state
    private final ScoreboardViewModel viewModel;

    public ScoreboardPresenter(ScoreboardViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(ScoreboardOutputData data) {
        List<ScoreboardRowViewModel> rows = data.getRows().stream()
                .map(row -> new ScoreboardRowViewModel(
                        row.getRank(),
                        row.getName(),
                        row.getScore()
                ))
                .collect(Collectors.toList());

        ScoreboardState state = viewModel.getState();
        state.setRows(rows);
        state.setErrorMessage(null);

        viewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        ScoreboardState state = viewModel.getState();
        state.setRows(List.of());
        state.setErrorMessage(error);

        viewModel.firePropertyChange();
    }
}

