package interface_adapters.StartScreen;

import interface_adapters.AddQuestion.LobbyPrepViewModel;
import use_cases.StartScreen.StartScreenOutputBoundary;
import use_cases.StartScreen.StartScreenOutputData;

public class StartScreenPresenter implements StartScreenOutputBoundary {

    private final StartScreenViewModel viewModel;
    private final LobbyPrepViewModel lobbyPrepViewModel;

    public StartScreenPresenter(StartScreenViewModel viewModel,
                                LobbyPrepViewModel lobbyPrepViewModel) {
        this.viewModel = viewModel;
        this.lobbyPrepViewModel = lobbyPrepViewModel;
    }

    @Override
    public void prepareJoinSuccessView(StartScreenOutputData outputData) {
        StartScreenState state = viewModel.getState();
        state.setPin(String.valueOf(outputData.getLobbyPin()));
        state.setUsername(outputData.getUsername());
        state.setErrorMessage(null);
        viewModel.firePropertyChange();
    }

    @Override
    public void prepareJoinFailureView(String errorMessage) {
        StartScreenState state = viewModel.getState();
        state.setErrorMessage(errorMessage);
        viewModel.firePropertyChange();
    }

    @Override
    public void prepareCreateSessionSuccessView(StartScreenOutputData outputData) {
        int pin = outputData.getLobbyPin();

        // Update StartScreen state (created session pin)
        StartScreenState state = viewModel.getState();
        state.setCreatedSessionPin(String.valueOf(pin));
        state.setErrorMessage(null);
        viewModel.firePropertyChange();

        // 🔵 Also push this PIN into LobbyPrepViewModel
        if (lobbyPrepViewModel != null) {
            lobbyPrepViewModel.setLobbyPin(pin);
        }
    }

    @Override
    public void prepareCreateSessionFailureView(String errorMessage) {
        StartScreenState state = viewModel.getState();
        state.setErrorMessage(errorMessage);
        viewModel.firePropertyChange();
    }
}
