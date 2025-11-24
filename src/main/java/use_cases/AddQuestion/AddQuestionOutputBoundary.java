package use_cases.AddQuestion;

public interface AddQuestionOutputBoundary {

    void prepareSuccessView(AddQuestionOutputData outputData);


    void prepareFailView(String errorMessage);
}