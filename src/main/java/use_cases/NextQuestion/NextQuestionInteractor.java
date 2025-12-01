package use_cases.NextQuestion;

import entities.Lobby;
import entities.Question;
import entities.User;
import use_cases.scoreboard.ScoreboardOutputData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class NextQuestionInteractor implements NextQuestionInputBoundary{

    private final NextQuestionLobbyDataAccessInterface lobbyDataAccess;
    private final NextQuestionOutputBoundary presenter;

    public NextQuestionInteractor(NextQuestionLobbyDataAccessInterface lobbyDataAccess,
                                  NextQuestionOutputBoundary presenter) {
        this.lobbyDataAccess = lobbyDataAccess;
        this.presenter = presenter;
    }

    public void execute(NextQuestionInputData inputData) {
        String lobbyPin = inputData.getLobbyPin();
        User currentPlayer = inputData.getCurrentPlayer();

        Lobby lobby = lobbyDataAccess.getLobby();
        if (lobby == null) {
            presenter.prepareFailView("Lobby not found for pin: " + lobbyPin);
            return;
        }

        List<Question> questions = inputData.getQuestions();
        if (questions == null || questions.isEmpty()) {
            presenter.prepareFailView("No questions provided");
            return;
        }

        // Example: pick first question
        Question nextQuestion = questions.get(0);

        // Wrap it in OutputData
        List<ScoreboardOutputData.Row> rows = buildScoreboardRows(lobby);

        NextQuestionOutputData outputData = new NextQuestionOutputData(nextQuestion,
                "Next question ready", rows);
    }
    // Helper: scoreboard rows
    private List<ScoreboardOutputData.Row> buildScoreboardRows(Lobby lobby) {
        List<ScoreboardOutputData.Row> rows = new ArrayList<>();

        List<User> users = lobby.getUsers();
        // sort by score descending
        users.sort(Comparator.comparingInt(User::getScore).reversed());

        int rank = 1;
        for (User u : users) {
            rows.add(new ScoreboardOutputData.Row(rank, u.getName(), u.getScore()));
            rank++;
        }

        return rows;
    }

}
