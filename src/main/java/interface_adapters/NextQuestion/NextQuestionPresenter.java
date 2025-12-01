package interface_adapters.NextQuestion;

import interface_adapters.ViewManagerModel;
import interface_adapters.scoreboard.ScoreboardState;
import interface_adapters.scoreboard.ScoreboardViewModel;
import use_cases.NextQuestion.NextQuestionOutputBoundary;
import use_cases.NextQuestion.NextQuestionOutputData;
import entities.Question;

public class NextQuestionPresenter implements NextQuestionOutputBoundary {

    private final NextQuestionViewModel viewModel;
    private  ViewManagerModel viewManagerModel;
    private  ScoreboardViewModel scoreboardViewModel;

    public NextQuestionPresenter(NextQuestionViewModel viewModel, ViewManagerModel viewManagerModel,
                                 ScoreboardViewModel scoreboardViewModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
        this.scoreboardViewModel = scoreboardViewModel;
    }

    public void prepareSuccessView(NextQuestionOutputData outputData) {
        Question nextQuestion = outputData.getQuestion();
        viewModel.setCurrentQuestion(nextQuestion);
        viewModel.setPopupMessage(outputData.getMessage());
        viewManagerModel.setState(ScoreboardViewModel.VIEW_NAME);
        viewManagerModel.firePropertyChange();

    }

    @Override
    public void prepareNextQuestion(Question question) {

    }

    @Override
    public void prepareFailView(String errorMessage) {
        // Send error message to the view model
        viewModel.setPopupMessage(errorMessage);
    }
}
