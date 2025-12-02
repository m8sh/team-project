package interface_adapters.scoreboard;

import interface_adapters.ViewManagerModel;
import use_cases.Scoreboard.ScoreboardOutputBoundary;
import use_cases.Scoreboard.ScoreboardOutputData;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardPresenter implements ScoreboardOutputBoundary {

    private final ScoreboardViewModel scoreboardViewModel;
    private final ViewManagerModel viewManagerModel;

    public ScoreboardPresenter(ScoreboardViewModel scoreboardViewModel,
                               ViewManagerModel viewManagerModel) {
        this.scoreboardViewModel = scoreboardViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(ScoreboardOutputData outputData) {
        ScoreboardState state = scoreboardViewModel.getState();
        if (state == null) {
            state = new ScoreboardState();
        }

        // Convert use-case rows -> view-model rows
        List<ScoreboardRowViewModel> viewRows = new ArrayList<>();
        for (ScoreboardOutputData.Row r : outputData.getRows()) {
            viewRows.add(new ScoreboardRowViewModel(
                    r.getRank(),
                    r.getName(),  // or getUsername() depending on your Row class
                    r.getScore()
            ));
        }

        state.setRows(viewRows);
        state.setErrorMessage(null);
        state.setLobbyPin(outputData.getLobbyPin());

        scoreboardViewModel.setState(state);
        scoreboardViewModel.firePropertyChange();

        viewManagerModel.setState(ScoreboardViewModel.VIEW_NAME);
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        ScoreboardState state = scoreboardViewModel.getState();
        if (state == null) {
            state = new ScoreboardState();
        }

        state.setErrorMessage(errorMessage);

        scoreboardViewModel.setState(state);
        scoreboardViewModel.firePropertyChange();
    }

    @Override
    public void endSession() {
        viewManagerModel.setState("start");
        viewManagerModel.firePropertyChange();
    }
}
