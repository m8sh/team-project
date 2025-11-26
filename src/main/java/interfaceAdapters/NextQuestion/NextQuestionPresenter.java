package interfaceAdapters.NextQuestion;

import use_cases.NextQuestion.NextQuestionOutputBoundary;
import use_cases.NextQuestion.NextQuestionOutputData;
import entities.Question;

public class NextQuestionPresenter implements NextQuestionOutputBoundary {

    private final NextQuestionViewModel viewModel;

    public NextQuestionPresenter(NextQuestionViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void prepareSuccessView(NextQuestionOutputData outputData) {
        Question nextQuestion = outputData.getQuestion();
        // Update the view model with the next question
        viewModel.setCurrentQuestion(nextQuestion);
        viewModel.setPopupMessage(outputData.getMessage()); // optional message
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
