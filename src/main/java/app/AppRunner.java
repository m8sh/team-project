package app;

//import data_access.InMemoryDataAccessObject;
//import entities.Lobby;
//import entities.QuestionFactory;
//import interface_adapters.AddQuestion.AddQuestionController;
//import interface_adapters.AddQuestion.AddQuestionPresenter;
//import interface_adapters.AddQuestion.LobbyPrepViewModel;
//import use_cases.addQuestion.AddQuestionLobbyDataAccessInterface;
//import view.LobbyPrepView;
//
//import use_cases.addQuestion.AddQuestionInteractor;
//
//import javax.swing.*;
//
//public class AppRunner {
//    //Go through each use case, set everything up
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            AddQuestionLobbyDataAccessInterface lobbyDataAccess = new InMemoryDataAccessObject();
//
//            Lobby lobby = new Lobby(123);
//            lobbyDataAccess.saveLobby(lobby);
//
//
//            LobbyPrepViewModel viewModel = new LobbyPrepViewModel(123);
//            AddQuestionPresenter presenter = new AddQuestionPresenter(viewModel,);
//
//
//            QuestionFactory questionFactory = new QuestionFactory();
//            AddQuestionInteractor interactor = new AddQuestionInteractor(lobbyDataAccess, presenter, questionFactory);
//            AddQuestionController controller = new AddQuestionController(interactor);
//
//            LobbyPrepView lobbyPrepView = new LobbyPrepView(viewModel, controller);
//            lobbyPrepView.setVisible(true);
//        });
//    }
//
//
//}
