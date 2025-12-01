package interface_adapters.StartScreen;

import interface_adapters.AddQuestion.LobbyPrepViewModel;
import interface_adapters.ViewManagerModel;
import use_cases.StartScreen.StartScreenOutputBoundary;
import use_cases.StartScreen.StartScreenOutputData;

public class StartScreenPresenter implements StartScreenOutputBoundary {

    private final StartScreenViewModel viewModel;

    public StartScreenPresenter(StartScreenViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void prepareJoinSuccessView(StartScreenOutputData outputData) {
        StartScreenState state = viewModel.getState();
        state.setPin(String.valueOf(outputData.getLobbyPin()));
        state.setUsername(outputData.getUsername());
        state.setErrorMessage(null);
        viewModel.firePropertyChange();
    }

    public void prepareJoinFailureView(String errorMessage){
        StartScreenState state = viewModel.getState();
        state.setErrorMessage(errorMessage);
        viewModel.firePropertyChange();
    }

    public void prepareCreateSessionSuccessView(StartScreenOutputData outputData){
        StartScreenState state = viewModel.getState();
        state.setCreatedSessionPin(String.valueOf(outputData.getLobbyPin()));
        state.setErrorMessage(null);
        viewModel.firePropertyChange();
    }

    public void prepareCreateSessionFailureView(String errorMessage){
        StartScreenState state = viewModel.getState();
        state.setErrorMessage(errorMessage);
        viewModel.firePropertyChange();
    }

}
