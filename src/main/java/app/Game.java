package app;

import api_caller.api_caller;
import data_access.InMemoryDataAccessObject;
import entities.GameFrame;
import entities.Lobby;
import entities.Question;
import entities.User;
import interfaceAdapters.NextQuestion.NextQuestionController;
import interfaceAdapters.NextQuestion.NextQuestionPresenter;
import interfaceAdapters.NextQuestion.NextQuestionViewModel;
import use_cases.NextQuestion.NextQuestionInteractor;
import use_cases.NextQuestion.NextQuestionLobbyDataAccessInterface;

import javax.swing.*;
import java.util.Arrays;

public class Game {

    public static void start(String username, String lobbyPin) {

        SwingUtilities.invokeLater(() -> {
            try {
                api_caller api = new api_caller();

                api.joinRoom(lobbyPin, username);
                Object[] questionObjects = api.recieveQuestions(lobbyPin);

                Lobby lobby = new Lobby(Integer.parseInt(lobbyPin));
                for (Object q : questionObjects) {
                    lobby.addQuestion((Question) q);
                }
                User player = new User(username, Integer.parseInt(lobbyPin));
                lobby.addUser(player);

                NextQuestionLobbyDataAccessInterface lobbyData = new InMemoryDataAccessObject();
                lobbyData.saveLobby(lobby);

                NextQuestionViewModel viewModel = new NextQuestionViewModel();
                NextQuestionPresenter presenter = new NextQuestionPresenter(viewModel);

                NextQuestionInteractor interactor =
                        new NextQuestionInteractor(lobbyData, presenter);
                NextQuestionController controller =
                        new NextQuestionController(interactor);

                GameFrame frame = new GameFrame(
                        lobby,
                        player,
                        lobby.getQuestions().size()
                );
                frame.setVisible(true);

                controller.execute(lobbyPin, lobby.getQuestions(), player);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
