package app;

import api_caller.api_caller;
import data_access.InMemoryDataAccessObject;
import entities.Lobby;
import entities.QuestionFactory;
import interface_adapters.AddQuestion.AddQuestionController;
import interface_adapters.AddQuestion.AddQuestionPresenter;
import interface_adapters.AddQuestion.LobbyPrepViewModel;
import interface_adapters.ViewManagerModel;
import interface_adapters.Scoreboard.ScoreboardViewModel;
import use_cases.AddQuestion.AddQuestionLobbyDataAccessInterface;
import use_cases.AddQuestion.AddQuestionInteractor;
import View.LobbyPrepView;

import javax.swing.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class AddQuestionAppRunner {

    // Go through each use case, set everything up
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            api_caller caller;
            try {
                caller = new api_caller();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

            try {
                caller.createRoom("123");
            } catch (IOException | URISyntaxException | InterruptedException e) {
                throw new RuntimeException(e);
            }

            AddQuestionLobbyDataAccessInterface lobbyDataAccess = new InMemoryDataAccessObject();

            // Create a lobby with PIN 123 and save it
            Lobby lobby = new Lobby(123);
            lobbyDataAccess.saveLobby(lobby);

            ViewManagerModel viewManagerModel = new ViewManagerModel();
            ScoreboardViewModel scoreboardViewModel = new ScoreboardViewModel();

            LobbyPrepViewModel viewModel = new LobbyPrepViewModel();
            viewModel.setLobbyPin(123);

            AddQuestionPresenter presenter =
                    new AddQuestionPresenter(viewModel, viewManagerModel, scoreboardViewModel);

            QuestionFactory questionFactory = new QuestionFactory();
            AddQuestionInteractor interactor =
                    new AddQuestionInteractor(lobbyDataAccess, presenter, questionFactory, caller);
            AddQuestionController controller = new AddQuestionController(interactor);

            // 👇 Pass null for ScoreboardController in this standalone runner
            LobbyPrepView lobbyPrepView =
                    new LobbyPrepView(viewModel, controller, viewManagerModel, null);

            // Show it in a frame
            JFrame frame = new JFrame("Add Question Runner - Lobby Prep");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setContentPane(lobbyPrepView);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
