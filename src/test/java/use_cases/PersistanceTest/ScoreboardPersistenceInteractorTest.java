package use_cases.Scoreboard;

import entities.User;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ScoreboardPersistenceInteractorTest {

    // ---------- Persistent Fake DAO ----------

    /**
     * This FakeDao simulates a persistent data store by keeping
     * results in a Map keyed by lobbyPin. That way, if we call
     * saveResults and then later call loadResults (even from a
     * different interactor instance), the scores are still there.
     */
    private static class PersistentFakeDao implements ScoreboardDataAccessInterface {

        boolean saveCalled = false;
        int lastSaveLobbyPin = -1;
        int lastLoadLobbyPin = -1;

        // Simulated "database": lobbyPin -> users
        private final Map<Integer, List<User>> storage = new HashMap<>();

        @Override
        public void saveResults(int lobbyPin, List<User> users) {
            saveCalled = true;
            lastSaveLobbyPin = lobbyPin;

            // store a copy to simulate persistence
            storage.put(lobbyPin, new ArrayList<>(users));
        }

        @Override
        public List<User> loadResults(int lobbyPin) {
            lastLoadLobbyPin = lobbyPin;
            return storage.getOrDefault(lobbyPin, new ArrayList<>());
        }
    }

    // ---------- Fake Presenter ----------

    private static class FakePresenter implements ScoreboardOutputBoundary {

        boolean successCalled = false;
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

    // ---------- Test for "persistent" behaviour ----------

    @Test
    void saveResults_thenReloadWithNewInteractor_persistsDataForLobby() {
        PersistentFakeDao dao = new PersistentFakeDao();
        FakePresenter firstPresenter = new FakePresenter();
        ScoreboardInteractor firstInteractor = new ScoreboardInteractor(dao, firstPresenter);

        int pin = 4444;

        User u1 = new User("Alice", pin);
        User u2 = new User("Bob", pin);

        u1.incrementScore();
        u1.incrementScore(); // 2
        u2.incrementScore(); // 1

        List<User> users = Arrays.asList(u1, u2);

        // 1) Save results once – this simulates finishing a game
        ScoreboardSaveInputData saveInput =
                new ScoreboardSaveInputData(pin, users);

        firstInteractor.saveResults(saveInput);

        assertTrue(dao.saveCalled);
        assertEquals(pin, dao.lastSaveLobbyPin);

        // 2) "New run" of the app: create a NEW interactor but reuse the same DAO
        FakePresenter secondPresenter = new FakePresenter();
        ScoreboardInteractor secondInteractor = new ScoreboardInteractor(dao, secondPresenter);

        // 3) Show scoreboard for the same lobby pin
        ScoreboardInputData loadInput =
                new ScoreboardInputData(pin);

        secondInteractor.showScoreboard(loadInput);

        // DAO was queried with the correct pin
        assertEquals(pin, dao.lastLoadLobbyPin);

        // Presenter should have been called with the saved results
        assertTrue(secondPresenter.successCalled);
        assertNull(secondPresenter.lastErrorMessage);
        assertNotNull(secondPresenter.lastOutput);

        ScoreboardOutputData out = secondPresenter.lastOutput;
        assertEquals(pin, out.getLobbyPin());

        List<ScoreboardOutputData.Row> rows = out.getRows();
        assertEquals(2, rows.size());

        // Data should reflect the scores we originally saved
        ScoreboardOutputData.Row r0 = rows.get(0);
        ScoreboardOutputData.Row r1 = rows.get(1);

        // Ordered by score descending: Alice(2), Bob(1)
        assertEquals("Alice", r0.getName());
        assertEquals(2, r0.getScore());

        assertEquals("Bob", r1.getName());
        assertEquals(1, r1.getScore());
    }
}
