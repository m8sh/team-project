package interface_adapters.scoreboard;

import interface_adapters.ViewManagerModel;
import interface_adapters.ViewModel;
import use_cases.scoreboard.ScoreboardOutputBoundary;
import use_cases.scoreboard.ScoreboardOutputData;

import java.util.List;
import java.util.stream.Collectors;

public class ScoreboardPresenter implements ScoreboardOutputBoundary {

    private final ScoreboardViewModel viewModel;
    private final ViewManagerModel viewManagerModel;

    public ScoreboardPresenter(ScoreboardViewModel viewModel,
                               ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
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
        state.setLobbyPin(data.getLobbyPin());

        viewModel.firePropertyChange();
        viewManagerModel.setState(viewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        ScoreboardState state = viewModel.getState();
        state.setRows(List.of());
        state.setErrorMessage(error);

        viewModel.firePropertyChange();
        viewManagerModel.setState(viewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }
}

