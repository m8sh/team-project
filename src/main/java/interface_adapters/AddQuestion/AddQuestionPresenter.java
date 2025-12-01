package interface_adapters.AddQuestion;

import interface_adapters.ViewManagerModel;
import interface_adapters.scoreboard.ScoreboardViewModel;
import use_cases.addQuestion.AddQuestionOutputBoundary;
import use_cases.addQuestion.AddQuestionOutputData;



public class AddQuestionPresenter implements AddQuestionOutputBoundary {
    private final LobbyPrepViewModel lobbyPrepViewModel;
    private final ViewManagerModel viewManagerModel;
    private final ScoreboardViewModel scoreboardViewModel;

    public AddQuestionPresenter(LobbyPrepViewModel lobbyPrepViewModel,
                                ViewManagerModel viewManagerModel,
                                ScoreboardViewModel scoreboardViewModel
    ) {
        this.lobbyPrepViewModel = lobbyPrepViewModel;
        this.viewManagerModel = viewManagerModel;
        this.scoreboardViewModel = scoreboardViewModel;
    }

    @Override
    public void prepareSuccessView(AddQuestionOutputData outputData) {
        // Increment number of questions
//        System.out.println("Presenter: prepareSuccessView called with message: " + outputData.getMessage());
        lobbyPrepViewModel.setPopupMessage(outputData.getMessage());
        lobbyPrepViewModel.incrementQuestionCount();

//        viewManagerModel.setState(ScoreboardViewModel.VIEW_NAME);
//        viewManagerModel.firePropertyChange();


    }

    @Override
    public void prepareFailView(String errorMessage) {
//        System.out.println("Presenter: prepareFailview called with message: " + errorMessage);
        lobbyPrepViewModel.setPopupMessage(errorMessage);


    }
}
//prepare success view and prepa;re fail view
// Tell view manager to show them next view?