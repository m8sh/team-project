package app;

import api_caller.api_caller;
import data_access.InMemoryDataAccessObject;
import entities.Lobby;
import entities.QuestionFactory;
import interface_adapters.AddQuestion.AddQuestionController;
import interface_adapters.AddQuestion.AddQuestionPresenter;
import interface_adapters.AddQuestion.LobbyPrepViewModel;
import use_cases.addQuestion.AddQuestionLobbyDataAccessInterface;
import view.LobbyPrepView;

import use_cases.addQuestion.AddQuestionInteractor;

import javax.swing.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class AddQuestionAppRunner {
    //Go through each use case, set everything up
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            api_caller caller = null;
            try {
                caller = new api_caller();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            try {
                caller.createRoom("123");
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            AddQuestionLobbyDataAccessInterface lobbyDataAccess = new InMemoryDataAccessObject();


            Lobby lobby = new Lobby(123);
            lobbyDataAccess.saveLobby(lobby);


            LobbyPrepViewModel viewModel = new LobbyPrepViewModel(123);
            AddQuestionPresenter presenter = new AddQuestionPresenter(viewModel);


            QuestionFactory questionFactory = new QuestionFactory();
            AddQuestionInteractor interactor = new AddQuestionInteractor(lobbyDataAccess, presenter, questionFactory, caller);
            AddQuestionController controller = new AddQuestionController(interactor);

            LobbyPrepView lobbyPrepView = new LobbyPrepView(viewModel, controller);
            //Need to figure out best way to give this a lobbyPin
            lobbyPrepView.setVisible(true);

        });
    }


}
