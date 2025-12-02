package use_cases.AddQuestion;

import api_caller.api_caller;
import data_access.InMemoryDataAccessObject;
import entities.Lobby;
import entities.Question;
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
    private TestSendQuestionsGateway testGateway;


    static class TestSendQuestionsGateway implements SendQuestionsDataAccess {
        boolean sendCalled = false;
        String capturedPin = null;
        Question[] capturedQuestions = null;


        @Override
        public void sendQuestions(String lobbyPin, Question[] questions) {
            sendCalled = true;
            capturedPin = lobbyPin;
            capturedQuestions = questions;
        }
    }

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
        testGateway = new TestSendQuestionsGateway();
        interactor = new AddQuestionInteractor(dao, testPresenter, factory, testGateway);

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
    @Test
    void testSendQuestionsSendsCorrectData() throws MalformedURLException {
        // Add some questions to the lobby
        Lobby lobby = dao.getLobby();
        lobby.addQuestion(factory.createQuestion("Q1", Arrays.asList("A", "B"), 0));
        lobby.addQuestion(factory.createQuestion("Q2", Arrays.asList("C", "D"), 1));

        // Call the method under test
        interactor.sendQuestions(123);

        // Verify the gateway was called
        assertTrue(testGateway.sendCalled, "sendQuestions should be called");

        // Verify pin
        assertEquals("123", testGateway.capturedPin, "Lobby pin should match");

        // Verify questions
        Question[] sentQuestions = testGateway.capturedQuestions;
        assertEquals(2, sentQuestions.length, "Should send 2 questions");
        assertEquals("Q1", sentQuestions[0].getPrompt());
        assertEquals("Q2", sentQuestions[1].getPrompt());
    }


}



