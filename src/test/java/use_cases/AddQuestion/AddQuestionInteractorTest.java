package use_cases.AddQuestion;

import api_caller.api_caller;
import data_access.InMemoryDataAccessObject;
import entities.Lobby;
import entities.QuestionFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AddQuestionInteractorTest {
    private InMemoryDataAccessObject dao;
    private QuestionFactory factory;
    private TestPresenter testPresenter;
    private AddQuestionInteractor interactor;
    private use_cases.AddQuestion.SendQuestionsDataAccess apiCaller;

    static class TestPresenter implements AddQuestionOutputBoundary {
        boolean successCalled = false;
        boolean failCalled = false;
        String failMessage = null;

        @Override
        public void prepareSuccessView(AddQuestionOutputData outputData) {
            successCalled = true;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            failCalled = true;
            failMessage = errorMessage;
        }
    }

    @BeforeEach
    void setUp() throws MalformedURLException {
        Lobby testLobby = new Lobby(123);
        dao = new InMemoryDataAccessObject();
        dao.saveLobby(testLobby);
        factory = new QuestionFactory();
        testPresenter = new TestPresenter();
        apiCaller = new api_caller();
        interactor = new AddQuestionInteractor(dao, testPresenter, factory, apiCaller);

    }
    @Test
    void testAddQuestionUpdatesLobby() {

        List<String> choices = Arrays.asList("1", "2", "3");
        AddQuestionInputData inputData = new AddQuestionInputData("What is 2+1?", choices, 2, 123);

        interactor.execute(inputData);


        Lobby lobby = dao.getLobby(); // should return the same lobby
        assertEquals(1, lobby.getQuestions().size(), "Lobby should have one question");
        assertEquals("What is 2+1?", lobby.getQuestions().get(0).getPrompt(), "Question prompt should match");
    }
    @Test
    void testFailIfLobbyMissing() {
        dao.saveLobby((null));
        AddQuestionInputData inputData = new AddQuestionInputData("Q?", Arrays.asList("A","B"), 0, 999);
        interactor.execute(inputData);

        assertTrue(testPresenter.failCalled);
        assertEquals("Lobby not found", testPresenter.failMessage);
    }
}



