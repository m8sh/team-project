package use_cases.Scoreboard;

import entities.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

    @Override
    public void showScoreboard(ScoreboardInputData inputData)
    {
        int lobbyPin = inputData.getLobbyPin();

        List<User> loaded = dao.loadResults(lobbyPin);
        List<User> users = new ArrayList<>(loaded);

        System.out.println("[ScoreboardInteractor] showScoreboard for pin "
                + lobbyPin + ", users=" + users.size());

        users.sort(Comparator.comparingInt(User::getScore).reversed());

        List<ScoreboardOutputData.Row> rows = new ArrayList<>();
        int rank = 1;
        for (User u : users) {
            System.out.println("    rank " + rank + " -> " + u.getName()
                    + " : " + u.getScore());
            rows.add(new ScoreboardOutputData.Row(rank++, u.getName(), u.getScore()));
        }

        // ★ Important: pass lobbyPin through so presenter/state can store it
        ScoreboardOutputData outputData = new ScoreboardOutputData(rows, lobbyPin);
        presenter.prepareSuccessView(outputData);
    }

    public void endSession() {
        presenter.endSession();
    }
}
