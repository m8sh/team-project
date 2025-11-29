package app;

import data_access.InMemoryDataAccessObject;
import entities.QuestionFactory;
import interface_adapters.AddQuestion.AddQuestionController;
import interface_adapters.AddQuestion.AddQuestionPresenter;
import interface_adapters.AddQuestion.LobbyPrepViewModel;
import interface_adapters.ViewManagerModel;

import interface_adapters.scoreboard.ScoreboardController;
import interface_adapters.scoreboard.ScoreboardPresenter;
import interface_adapters.scoreboard.ScoreboardViewModel;

import use_cases.addQuestion.AddQuestionInputBoundary;
import use_cases.addQuestion.AddQuestionInteractor;
import use_cases.addQuestion.AddQuestionOutputBoundary;
import use_cases.scoreboard.ScoreboardDataAccessInterface;
import use_cases.scoreboard.ScoreboardInputBoundary;
import use_cases.scoreboard.ScoreboardInteractor;
import use_cases.scoreboard.ScoreboardOutputBoundary;

import view.LobbyPrepView;
import view.ScoreboardView;
import view.ViewManager;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {

    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    // Shared DAO
    final InMemoryDataAccessObject lobbyDataAccessObject = new InMemoryDataAccessObject();

    // Scoreboard
    private ScoreboardViewModel scoreboardViewModel;
    private ScoreboardView scoreboardView;
    // (controller kept as a field in case another view needs it later)
    private ScoreboardController scoreboardController;

    // AddQuestion
    private LobbyPrepViewModel lobbyPrepViewModel;
    private LobbyPrepView lobbyPrepView;
    private AddQuestionController addQuestionController;



    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    // ----------SCOREBOARD VIEWS ----------
    public AppBuilder addScoreboardView() {
        scoreboardViewModel = new ScoreboardViewModel();
        scoreboardView = new ScoreboardView(scoreboardViewModel);
        cardPanel.add(scoreboardView, scoreboardView.getViewName());
        return this;
    }

    // ---------- SCOREBOARD USE CASES ----------
    public AppBuilder addScoreboardUseCase() {
        ScoreboardOutputBoundary outputBoundary =
                new ScoreboardPresenter(scoreboardViewModel);

        ScoreboardDataAccessInterface dao = lobbyDataAccessObject;

        ScoreboardInputBoundary interactor =
                new ScoreboardInteractor(dao, outputBoundary);

        scoreboardController = new ScoreboardController(interactor);
        scoreboardView.setScoreboardController(scoreboardController);
        cardPanel.add(scoreboardView, scoreboardView.getViewName());
        return this;
    }


    // ---------- ADDQUESTION VIEWS ----------
    public AppBuilder addLobbyPrepView(int lobbyPin) {

        lobbyPrepViewModel = new LobbyPrepViewModel(123456);


        lobbyPrepView = new LobbyPrepView(lobbyPrepViewModel, addQuestionController, viewManagerModel ); // controller set below

        cardPanel.add(lobbyPrepView, lobbyPrepView.getViewName());

        return this;
    }


    // ---------- ADDQUESTION USE CASES ----------

    public AppBuilder addAddQuestionUseCase() {

        AddQuestionOutputBoundary outputBoundary =
                new AddQuestionPresenter(lobbyPrepViewModel, viewManagerModel, scoreboardViewModel);
        // ^ If you later extend AddQuestionPresenter to also take
        //   ViewManagerModel + ScoreboardViewModel, you can switch
        //   to scoreboard after saving a question.
        //   For now, this keeps his existing behaviour only.
        QuestionFactory questionFactory = new QuestionFactory();
        AddQuestionInputBoundary interactor =
                new AddQuestionInteractor(lobbyDataAccessObject, outputBoundary, questionFactory);

        addQuestionController = new AddQuestionController(interactor);

        // Give controller to LobbyPrepView so its "Add Question"
        // button can open AddQuestionView and call controller.execute()
        lobbyPrepView.setAddQuestionController(addQuestionController);

        return this;                                             // QUESTION
    }








    // ---------- BUILD FRAME ----------

    public JFrame build() {
        final JFrame application = new JFrame("Team Project");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        // Start on scoreboard for now
        viewManagerModel.setState(lobbyPrepView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }
}
