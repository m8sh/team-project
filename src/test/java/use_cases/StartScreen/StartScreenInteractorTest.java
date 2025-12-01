package use_cases.StartScreen;

import entities.Lobby;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StartScreenInteractorTest {

    private static class TestLobbyDAO implements StartScreenLobbyDataAccessInterface {
        Lobby lastLobby;
        Integer firstDoublePin = null;

        public Lobby getLobby(int pin) {

            if (firstDoublePin == null) {
                firstDoublePin = pin;
                return new Lobby(pin);
            }
                return null;
        }

        public void saveLobby(Lobby lobby) {
            this.lastLobby = lobby;
        }

    }

    private static class TestNetworkDAO implements StartScreenNetworkDataAccessInterface {
        boolean nextCreate = true;
        boolean joinCalled = false;
        int lastCreatePin = -1;
        int lastJoinPin = -1;
        String lastJoinUsername = null;

        @Override
        public boolean createRemoteRoom(int pin) {
            this.lastCreatePin = pin;
            return nextCreate;
        }

        @Override
        public boolean joinRemoteRoom(int pin, String username) {
            joinCalled = true;
            this.lastJoinPin = pin;
            this.lastJoinUsername = username;
            return true;
        }
    }

    private static class TestPresenter implements StartScreenOutputBoundary {
        boolean joinSuccess = false;
        boolean joinFailed = false;
        boolean createSuccess = false;
        boolean createFailed = false;

        StartScreenOutputData lastJoinSuccess;
        StartScreenOutputData lastCreateSuccess;

        String lastJoinFailureMsg;
        String lastCreateFailureMsg;


        @Override
        public void prepareJoinSuccessView(StartScreenOutputData outputData) {
            joinSuccess = true;
            lastJoinSuccess = outputData;
        }

        @Override
        public void prepareJoinFailureView(String errorMessage) {
            joinFailed = true;
            lastJoinFailureMsg = errorMessage;
        }

        @Override
        public void prepareCreateSessionSuccessView(StartScreenOutputData outputData) {
            createSuccess = true;
            lastCreateSuccess = outputData;
        }

        @Override
        public void prepareCreateSessionFailureView(String errorMessage) {
            createFailed = true;
            lastCreateFailureMsg = errorMessage;
        }
    }

    @Test
    void emptyPinJoin() {
        TestLobbyDAO lobbyDAO = new TestLobbyDAO();
        TestNetworkDAO networkDAO = new TestNetworkDAO();
        TestPresenter presenter = new TestPresenter();

        StartScreenInteractor interactor = new StartScreenInteractor(lobbyDAO, networkDAO, presenter);

        JoinSessionInputData input = new JoinSessionInputData("", "bob");

        interactor.joinSession(input);

        assertTrue(presenter.joinFailed);
        assertEquals("PIN cannot be empty.", presenter.lastJoinFailureMsg);
    }

    @Test
    void emptyUserJoin() {
        TestLobbyDAO lobbyDAO = new TestLobbyDAO();
        TestNetworkDAO networkDAO = new TestNetworkDAO();
        TestPresenter presenter = new TestPresenter();

        StartScreenInteractor interactor = new StartScreenInteractor(lobbyDAO, networkDAO, presenter);

        JoinSessionInputData input = new JoinSessionInputData("123", "");

        interactor.joinSession(input);

        assertTrue(presenter.joinFailed);
        assertEquals("Username cannot be empty.", presenter.lastJoinFailureMsg);
    }

    @Test
    void stringPinJoin() {
        TestLobbyDAO lobbyDAO = new TestLobbyDAO();
        TestNetworkDAO networkDAO = new TestNetworkDAO();
        TestPresenter presenter = new TestPresenter();

        StartScreenInteractor interactor = new StartScreenInteractor(lobbyDAO, networkDAO, presenter);

        JoinSessionInputData input = new JoinSessionInputData("bob", "bob");

        interactor.joinSession(input);

        assertTrue(presenter.joinFailed);
        assertEquals("Pin must be an integer.", presenter.lastJoinFailureMsg);
    }

    @Test
    void validJoin() {
        TestLobbyDAO lobbyDAO = new TestLobbyDAO();
        TestNetworkDAO networkDAO = new TestNetworkDAO();
        TestPresenter presenter = new TestPresenter();

        StartScreenInteractor interactor = new StartScreenInteractor(lobbyDAO, networkDAO, presenter);

        JoinSessionInputData input = new JoinSessionInputData("123", "bob");

        interactor.joinSession(input);

        assertTrue(presenter.joinSuccess);
        assertEquals(123, presenter.lastJoinSuccess.getLobbyPin());
        assertEquals("bob", presenter.lastJoinSuccess.getUsername());
    }

    @Test
    void createSession() {
        TestLobbyDAO lobbyDAO = new TestLobbyDAO();
        TestNetworkDAO networkDAO = new TestNetworkDAO();
        TestPresenter presenter = new TestPresenter();

        networkDAO.nextCreate = true;

        StartScreenInteractor interactor = new StartScreenInteractor(lobbyDAO, networkDAO, presenter);

        CreateSessionInputData input = new CreateSessionInputData();
        interactor.createSession(input);

        assertNotNull(lobbyDAO.lastLobby);
        assertEquals(lobbyDAO.lastLobby.getPin(), networkDAO.lastCreatePin);

        assertTrue(presenter.createSuccess);
        assertNotNull(presenter.lastCreateSuccess);

        }

        @Test
    void createSessionFailed() {
        TestLobbyDAO lobbyDAO = new TestLobbyDAO();
        TestNetworkDAO networkDAO = new TestNetworkDAO();
        TestPresenter presenter = new TestPresenter();

        networkDAO.nextCreate = false;

        StartScreenInteractor interactor = new StartScreenInteractor(lobbyDAO, networkDAO, presenter);
        CreateSessionInputData input = new CreateSessionInputData();
        interactor.createSession(input);

        assertNotNull(lobbyDAO.lastLobby);
        assertEquals(lobbyDAO.lastLobby.getPin(), networkDAO.lastCreatePin);

        assertTrue(presenter.createFailed);
        assertEquals("Couldn't create a room.", presenter.lastCreateFailureMsg);

    }

    @Test
    void nullPINJoin() {
        TestLobbyDAO lobbyDAO = new TestLobbyDAO();
        TestNetworkDAO networkDAO = new TestNetworkDAO();
        TestPresenter presenter = new TestPresenter();

        StartScreenInteractor interactor = new StartScreenInteractor(lobbyDAO, networkDAO, presenter);
        JoinSessionInputData input = new JoinSessionInputData(null, "bob");

        interactor.joinSession(input);

        assertTrue(presenter.joinFailed);
        assertEquals("PIN cannot be empty.", presenter.lastJoinFailureMsg);
    }

    @Test
    void nullUserJoin() {
        TestLobbyDAO lobbyDAO = new TestLobbyDAO();
        TestNetworkDAO networkDAO = new TestNetworkDAO();
        TestPresenter presenter = new TestPresenter();

        StartScreenInteractor interactor = new StartScreenInteractor(lobbyDAO, networkDAO, presenter);
        JoinSessionInputData input = new JoinSessionInputData("123", null);

        interactor.joinSession(input);

        assertTrue(presenter.joinFailed);
        assertEquals("Username cannot be empty.", presenter.lastJoinFailureMsg);
    }


}


