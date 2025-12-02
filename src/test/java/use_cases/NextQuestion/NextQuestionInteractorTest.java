package use_cases.NextQuestion;

import entities.Lobby;
import entities.Question;
import entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NextQuestionInteractorTest {

    private static class StubLobbyDAO implements NextQuestionLobbyDataAccessInterface {
        Lobby lobby;

        @Override
        public Lobby getLobby() {
            return lobby;
        }

        @Override
        public void saveLobby(Lobby lobby) {
            this.lobby = lobby;
        }
    }

    private static class StubPresenter implements NextQuestionOutputBoundary {
        boolean failCalled = false;
        boolean successCalled = false;
        String failMessage = null;
        NextQuestionOutputData successData = null;

        @Override
        public void prepareSuccessView(NextQuestionOutputData outputData) {
            successCalled = true;
            successData = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            failCalled = true;
            failMessage = errorMessage;
        }
    }

    private StubLobbyDAO stubDAO;
    private StubPresenter stubPresenter;
    private NextQuestionInteractor interactor;

    @BeforeEach
    void setUp() {
        stubDAO = new StubLobbyDAO();
        stubPresenter = new StubPresenter();
        interactor = new NextQuestionInteractor(stubDAO, stubPresenter);
    }

    @Test
    void testLobbyNotFound() {
        stubDAO.lobby = null;

        NextQuestionInputData input = new NextQuestionInputData("1234", new ArrayList<>(), new User("Alice", 1234));
        interactor.execute(input);

        assertTrue(stubPresenter.failCalled);
        assertEquals("Lobby not found for pin: 1234", stubPresenter.failMessage);
    }

    @Test
    void testNoQuestionsProvided() {
        stubDAO.lobby = new Lobby(1234);

        NextQuestionInputData input = new NextQuestionInputData("1234", new ArrayList<>(), new User("Alice", 1234));
        interactor.execute(input);

        assertTrue(stubPresenter.failCalled);
        assertEquals("No questions provided", stubPresenter.failMessage);
    }

    @Test
    void testNormalExecution() {
        User alice = new User("Alice", 1);

        Lobby lobby = new Lobby(1234);
        lobby.addUser(alice);
        stubDAO.lobby = lobby;

        Question question = new Question("What is 2+2?", List.of("3", "4"), 1);
        NextQuestionInputData input = new NextQuestionInputData("1234", List.of(question), alice);

        interactor.execute(input);

        assertTrue(stubPresenter.successCalled);
        assertNotNull(stubPresenter.successData);
        assertEquals(question, stubPresenter.successData.getQuestion());
        assertEquals("Next question ready", stubPresenter.successData.getMessage());

        // Check scoreboard
        assertEquals(1, stubPresenter.successData.getScoreboardRows().size());
        assertEquals("Alice", stubPresenter.successData.getScoreboardRows().get(0).getName());
        assertEquals(0, stubPresenter.successData.getScoreboardRows().get(0).getScore());
    }
}
