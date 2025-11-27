package use_cases.scoreboard;

import entities.Lobby;
import entities.User;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ScoreboardInteractor implements ScoreboardInputBoundary {
    private final ScoreboardOutputBoundary presenter;
    private final ScoreboardDataAccessInterface DAO;
    public  ScoreboardInteractor(ScoreboardDataAccessInterface DAO,
                                 ScoreboardOutputBoundary presenter) {
        this.presenter = presenter;
        this.DAO = DAO;
    }

    @Override
    public void showScoreboard(ScoreboardInputData scoreboardInputData) {
        int lobbyPin = scoreboardInputData.getLobbyPin();
        Lobby lobby = DAO.getLobby(lobbyPin);
        if (lobby == null) {
            presenter.prepareFailView("Lobby not found");
            return;
        }
        List<User> users = lobby.getUsers();
        users.sort(Comparator.comparingInt(User::getScore).reversed());

        List<ScoreboardOutputData.Row> rows = new ArrayList<>();
        int rank = 1;
        for (User user : users) {
            rows.add(new ScoreboardOutputData.Row(
                    rank++,
                    user.getName(),
                    user.getScore()
            ));
        }
        ScoreboardOutputData outputData = new ScoreboardOutputData(rows, lobbyPin);
        presenter.prepareSuccessView(outputData);
    }
}
