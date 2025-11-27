package app;

import data_access.InMemoryDataAccessObject;
import entities.Lobby;
import entities.QuestionFactory;
import use_cases.AddQuestion.AddQuestionLobbyDataAccessInterface;
import use_cases.view.LobbyPrepView;
import interfaceAdapters.AddQuestion.*;

import use_cases.AddQuestion.AddQuestionInteractor;

import javax.swing.*;

public class AppRunner {
    //Go through each use case, set everything up
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AddQuestionLobbyDataAccessInterface lobbyDataAccess = new InMemoryDataAccessObject();

            Lobby lobby = new Lobby(123);
            lobbyDataAccess.saveLobby(lobby);


            LobbyPrepViewModel viewModel = new LobbyPrepViewModel(123);
            AddQuestionPresenter presenter = new AddQuestionPresenter(viewModel);


            QuestionFactory questionFactory = new QuestionFactory();
            AddQuestionInteractor interactor = new AddQuestionInteractor(lobbyDataAccess, presenter, questionFactory);
            AddQuestionController controller = new AddQuestionController(interactor);

            LobbyPrepView lobbyPrepView = new LobbyPrepView(viewModel, controller);
            lobbyPrepView.setVisible(true);
        });
    }


}
