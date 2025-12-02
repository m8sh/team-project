package use_cases.Scoreboard;

import entities.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScoreboardInteractorTest {

    // ---------- Fake DAO ----------

    private static class FakeDao implements use_cases.Scoreboard.ScoreboardDataAccessInterface {

        boolean saveCalled = false;
        int lastSaveLobbyPin = -1;
        List<User> lastSavedUsers = null;

        int lastLoadLobbyPin = -1;
        List<User> usersToReturn = new ArrayList<>();

        @Override
        public void saveResults(int lobbyPin, List<User> users) {
            saveCalled = true;
            lastSaveLobbyPin = lobbyPin;
            lastSavedUsers = users;
        }

        @Override
        public List<User> loadResults(int lobbyPin) {
            lastLoadLobbyPin = lobbyPin;
            return usersToReturn;
        }
    }

    // ---------- Fake Presenter ----------

    private static class FakePresenter implements ScoreboardOutputBoundary {

        boolean successCalled = false;
        boolean endSessionCalled = false;
        String lastErrorMessage = null;
        ScoreboardOutputData lastOutput = null;

        @Override
        public void prepareSuccessView(ScoreboardOutputData data) {
            successCalled = true;
            lastOutput = data;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            lastErrorMessage = errorMessage;
        }

    }

    // ---------- Tests ----------

    @Test
    void saveResults_delegatesToDao() {
        FakeDao dao = new FakeDao();
        FakePresenter presenter = new FakePresenter();
        ScoreboardInteractor interactor = new ScoreboardInteractor(dao, presenter);

        int pin = 1111;
        User u1 = new User("Alice", pin);
        User u2 = new User("Bob", pin);
        List<User> users = Arrays.asList(u1, u2);

        ScoreboardSaveInputData input =
                new ScoreboardSaveInputData(pin, users);

        interactor.saveResults(input);

        assertTrue(dao.saveCalled);
        assertEquals(pin, dao.lastSaveLobbyPin);
        assertEquals(users, dao.lastSavedUsers);
    }

    @Test
    void showScoreboard_sortsUsersAndBuildsRows() {
        FakeDao dao = new FakeDao();
        FakePresenter presenter = new FakePresenter();
        ScoreboardInteractor interactor = new ScoreboardInteractor(dao, presenter);

        int pin = 2222;

        // Users with scores out of order to test sorting
        User low = new User("Low", pin);      // 0
        User high = new User("High", pin);    // 2
        User mid = new User("Mid", pin);      // 1

        high.incrementScore();
        high.incrementScore();
        mid.incrementScore();

        // Put them in a weird order in DAO so we can see sorting
        dao.usersToReturn = Arrays.asList(low, high, mid);

        ScoreboardInputData input =
                new ScoreboardInputData(pin);

        interactor.showScoreboard(input);

        // DAO was queried with the correct pin
        assertEquals(pin, dao.lastLoadLobbyPin);

        // Presenter was called with success output
        assertTrue(presenter.successCalled);
        assertNull(presenter.lastErrorMessage);
        assertNotNull(presenter.lastOutput);

        ScoreboardOutputData out = presenter.lastOutput;
        assertEquals(pin, out.getLobbyPin());

        List<ScoreboardOutputData.Row> rows = out.getRows();
        assertEquals(3, rows.size());

        // Should be ordered by score: high (2), mid (1), low(0)
        ScoreboardOutputData.Row r0 = rows.get(0);
        assertEquals("High", r0.getName());
        assertEquals(2, r0.getScore());
        assertEquals(1, r0.getRank());

        ScoreboardOutputData.Row r1 = rows.get(1);
        assertEquals("Mid", r1.getName());
        assertEquals(1, r1.getScore());
        assertEquals(2, r1.getRank());

        ScoreboardOutputData.Row r2 = rows.get(2);
        assertEquals("Low", r2.getName());
        assertEquals(0, r2.getScore());
        assertEquals(3, r2.getRank());
    }
    @Test
    void showScoreboard_noUsers_callsFailView() {
        FakeDao dao = new FakeDao();
        FakePresenter presenter = new FakePresenter();
        ScoreboardInteractor interactor = new ScoreboardInteractor(dao, presenter);

        int pin = 3333;
        // dao.usersToReturn is empty by default

        ScoreboardInputData input = new ScoreboardInputData(pin);

        interactor.showScoreboard(input);

        // DAO was queried with correct pin
        assertEquals(pin, dao.lastLoadLobbyPin);

        assertFalse(presenter.successCalled);
        assertNull(presenter.lastOutput);

        assertEquals(
                "No scores have been submitted for this lobby yet.",
                presenter.lastErrorMessage
        );
    }
}
