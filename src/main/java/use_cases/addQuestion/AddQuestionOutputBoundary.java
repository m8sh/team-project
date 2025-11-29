package use_cases.addQuestion;

public interface AddQuestionOutputBoundary {

    void prepareSuccessView(AddQuestionOutputData outputData);


    void prepareFailView(String errorMessage);
}