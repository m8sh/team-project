package use_cases.Scoreboard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import entities.User;

public class ScoreboardInteractor implements ScoreboardInputBoundary {

    private final ScoreboardDataAccessInterface dao;
    private final ScoreboardOutputBoundary presenter;

    public ScoreboardInteractor(ScoreboardDataAccessInterface dao,
                                ScoreboardOutputBoundary presenter) {
        this.dao = dao;
        this.presenter = presenter;
    }

    @Override
    public void saveResults(ScoreboardSaveInputData inputData) {
        dao.saveResults(inputData.getLobbyPin(), inputData.getUsers());
    }

    @SuppressWarnings({"checkstyle:ReturnCount", "checkstyle:SuppressWarnings"})
    @Override
    public void showScoreboard(ScoreboardInputData inputData) {

        final int lobbyPin = inputData.getLobbyPin();

        final List<User> loaded = dao.loadResults(lobbyPin);
        final List<User> users = new ArrayList<>(loaded);

        if (users.isEmpty()) {
            presenter.prepareFailView("No scores have been submitted for this lobby yet.");
            return;
        }

        System.out.println("[ScoreboardInteractor] showScoreboard for pin "
                + lobbyPin + ", users=" + users.size());

        users.sort(Comparator.comparingInt(User::getScore).reversed());

        final List<ScoreboardOutputData.Row> rows = new ArrayList<>();
        int rank = 1;
        for (User u : users) {
            System.out.println("    rank " + rank + " -> " + u.getName()
                    + " : " + u.getScore());
            rows.add(new ScoreboardOutputData.Row(rank++, u.getName(), u.getScore()));
        }

        // ★ Important: pass lobbyPin through so presenter/state can store it
        final ScoreboardOutputData outputData = new ScoreboardOutputData(rows, lobbyPin);
        presenter.prepareSuccessView(outputData);
    }

}
