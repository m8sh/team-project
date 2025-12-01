package app;

import api_caller.api_caller;
import data_access.InMemoryDataAccessObject;
import entities.GameFrame;
import entities.Lobby;
import entities.Question;
import entities.User;
import interface_adapters.NextQuestion.NextQuestionController;
import interface_adapters.NextQuestion.NextQuestionPresenter;
import interface_adapters.NextQuestion.NextQuestionViewModel;
import interface_adapters.ViewManagerModel;
import interface_adapters.scoreboard.ScoreboardController;
import interface_adapters.scoreboard.ScoreboardPresenter;
import interface_adapters.scoreboard.ScoreboardViewModel;
import use_cases.NextQuestion.NextQuestionInteractor;
import use_cases.NextQuestion.NextQuestionLobbyDataAccessInterface;
import use_cases.scoreboard.ScoreboardDataAccessInterface;
import use_cases.scoreboard.ScoreboardInputBoundary;
import use_cases.scoreboard.ScoreboardInteractor;
import use_cases.scoreboard.ScoreboardOutputBoundary;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;

public class Game {

    private static Question parseQuestionJSON(String json) {
        String promptKey = "\"prompt\":\"";
        int pStart = json.indexOf(promptKey);
        String prompt = "";
        if (pStart != -1) {
            pStart += promptKey.length();
            int pEnd = json.indexOf("\"", pStart);
            if (pEnd > pStart) {
                prompt = json.substring(pStart, pEnd);
            }
        }

        String choicesKey = "\"choices\":[";
        int cStart = json.indexOf(choicesKey);
        List<String> choices = new ArrayList<>();
        if (cStart > -1) {
            cStart += choicesKey.length();
            int cEnd = json.indexOf("]", cStart);
            if (cEnd > cStart) {
                String body = json.substring(cStart, cEnd);
                if (!body.isEmpty()) {
                    String[] parts = body.split("\",\"");
                    for (String raw : parts) {
                        String good = raw.replace("\"", "");
                        if (!good.isEmpty()) {
                            choices.add(good);
                        }
                    }
                }
            }
        }

        String indexKey = "\"correctIndex\":";
        int i = json.indexOf(indexKey);
        int correctIndex = 0;
        if (i > -1) {
            i +=  indexKey.length();
            int iEnd = json.indexOf("}", i);
            if (iEnd == -1) {
                iEnd = json.length();
            }
            String string = json.substring(i, iEnd);
            correctIndex = Integer.parseInt(string);
        }

        return new Question(prompt, choices, correctIndex);

    }

    public static void start(String username, String lobbyPin) {

        SwingUtilities.invokeLater(() -> {
            try {
                System.out.println("1");
                api_caller api = new api_caller();
                System.out.println("2");
                api.joinRoom(lobbyPin, username);
                System.out.println("3");
                Object[] questionObjects = api.recieveQuestions(lobbyPin);
                System.out.println("4");

                System.out.println(questionObjects == null);
                if (questionObjects != null) {
                    System.out.println(questionObjects.length);
                }


                Lobby lobby = new Lobby(Integer.parseInt(lobbyPin));
                for (Object qRaw : questionObjects) {
                    String json = qRaw.toString();
                    Question q = parseQuestionJSON(json);
                    lobby.addQuestion(q);
                }
                User player = new User(username, Integer.parseInt(lobbyPin));
                lobby.addUser(player);

                InMemoryDataAccessObject dao = new InMemoryDataAccessObject();
                dao.saveLobby(lobby);

                // Use it via both interfaces
                NextQuestionLobbyDataAccessInterface nextQDao = dao;
                ScoreboardDataAccessInterface scoreboardDao = dao;

                NextQuestionViewModel viewModel = new NextQuestionViewModel();
                ViewManagerModel viewManagerModel = new ViewManagerModel();
                ScoreboardViewModel scoreboardViewModel = new ScoreboardViewModel();

                NextQuestionPresenter presenter = new NextQuestionPresenter(viewModel, viewManagerModel,
                        scoreboardViewModel);
                NextQuestionInteractor interactor =
                        new NextQuestionInteractor(nextQDao, presenter);
                NextQuestionController controller =
                        new NextQuestionController(interactor);

                ScoreboardOutputBoundary scoreboardPresenter =
                        new ScoreboardPresenter(scoreboardViewModel);
                ScoreboardInputBoundary scoreboardInteractor =
                        new ScoreboardInteractor(scoreboardDao, scoreboardPresenter);
                ScoreboardController scoreboardController =
                        new ScoreboardController(scoreboardInteractor);
                GameFrame frame = new GameFrame(
                        lobby,
                        player,
                        lobby.getQuestions().size(),
                        scoreboardController
                );
                frame.setVisible(true);

                controller.execute(lobbyPin, lobby.getQuestions(), player);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
