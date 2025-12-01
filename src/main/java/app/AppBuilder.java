package app;

import api_caller.api_caller;
import data_access.InMemoryDataAccessObject;
import entities.QuestionFactory;
import interface_adapters.AddQuestion.AddQuestionController;
import interface_adapters.AddQuestion.AddQuestionPresenter;
import interface_adapters.AddQuestion.LobbyPrepViewModel;
import interface_adapters.StartScreen.StartScreenController;
import interface_adapters.StartScreen.StartScreenPresenter;
import interface_adapters.StartScreen.StartScreenViewModel;
import interface_adapters.ViewManagerModel;

import interface_adapters.scoreboard.ScoreboardController;
import interface_adapters.scoreboard.ScoreboardPresenter;
import interface_adapters.scoreboard.ScoreboardViewModel;

import use_cases.StartScreen.*;
import use_cases.addQuestion.AddQuestionInputBoundary;
import use_cases.addQuestion.AddQuestionInteractor;
import use_cases.addQuestion.AddQuestionOutputBoundary;
import use_cases.addQuestion.SendQuestionsDataAccess;
import use_cases.scoreboard.ScoreboardDataAccessInterface;
import use_cases.scoreboard.ScoreboardInputBoundary;
import use_cases.scoreboard.ScoreboardInteractor;
import use_cases.scoreboard.ScoreboardOutputBoundary;

import view.LobbyPrepView;
import view.ScoreboardView;
import view.StartScreenView;
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

    // API caller shared between use cases
    private api_caller apiCaller;

    // StartScreen
    private StartScreenViewModel startScreenViewModel;
    private StartScreenView startScreenView;
    private StartScreenController startScreenController;

    // Scoreboard
    private ScoreboardViewModel scoreboardViewModel;
    private ScoreboardView scoreboardView;
    private ScoreboardController scoreboardController;

    // Lobby prep / AddQuestion
    private LobbyPrepViewModel lobbyPrepViewModel;
    private LobbyPrepView lobbyPrepView;
    private AddQuestionController addQuestionController;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);

        try {
            api_caller caller = new api_caller();
            // Example: create a dummy room; the real PINs come from StartScreen / Lobby logic
            caller.createRoom("123");
            this.apiCaller = caller;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // -------- START SCREEN VIEW ----------
    public AppBuilder addStartScreenView() {
        startScreenViewModel = new StartScreenViewModel();
        startScreenView = new StartScreenView(startScreenViewModel, viewManagerModel);
        cardPanel.add(startScreenView, startScreenView.getViewName());
        return this;
    }

    public AppBuilder addStartScreenUseCase() {
        StartScreenOutputBoundary outputBoundary =
                new StartScreenPresenter(startScreenViewModel);

        StartScreenLobbyDataAccessInterface lobbyDAI = lobbyDataAccessObject;
        StartScreenNetworkDataAccessInterface networkDAI = apiCaller;

        StartScreenInputBoundary interactor =
                new StartScreenInteractor(lobbyDAI, networkDAI, outputBoundary);

        startScreenController = new StartScreenController(interactor);
        startScreenView.setStartScreenController(startScreenController);
        return this;
    }

    // ---------- SCOREBOARD VIEWS ----------
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
        return this;
    }

    // ---------- LOBBYPREP / ADDQUESTION VIEWS ----------
    public AppBuilder addLobbyPrepView(int lobbyPin) {
        lobbyPrepViewModel = new LobbyPrepViewModel(lobbyPin);

        AddQuestionOutputBoundary addQPresenter =
                new AddQuestionPresenter(lobbyPrepViewModel, viewManagerModel, scoreboardViewModel);
        QuestionFactory questionFactory = new QuestionFactory();
        AddQuestionInputBoundary interactor =
                new AddQuestionInteractor(lobbyDataAccessObject, addQPresenter, questionFactory, apiCaller);

        addQuestionController = new AddQuestionController(interactor);

        lobbyPrepView = new LobbyPrepView(lobbyPrepViewModel, addQuestionController, viewManagerModel);
        cardPanel.add(lobbyPrepView, lobbyPrepView.getViewName());
        return this;
    }

    // ---------- ADDQUESTION USE CASES ----------
    public AppBuilder addAddQuestionUseCase() {

        AddQuestionOutputBoundary outputBoundary =
                new AddQuestionPresenter(lobbyPrepViewModel, viewManagerModel, scoreboardViewModel);

        QuestionFactory questionFactory = new QuestionFactory();
        AddQuestionInputBoundary interactor =
                new AddQuestionInteractor(lobbyDataAccessObject, outputBoundary, questionFactory, apiCaller);

        addQuestionController = new AddQuestionController(interactor);

        lobbyPrepView.setAddQuestionController(addQuestionController);

        return this;
    }

    // ---------- BUILD FRAME ----------
    public JFrame build() {
        final JFrame application = new JFrame("Team Project");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        // Start on the start screen if present, otherwise fall back to lobby prep or scoreboard
        String initialViewName = null;
        if (startScreenView != null) {
            initialViewName = startScreenView.getViewName();
        } else if (lobbyPrepView != null) {
            initialViewName = lobbyPrepView.getViewName();
        } else if (scoreboardView != null) {
            initialViewName = scoreboardView.getViewName();
        }

        if (initialViewName != null) {
            viewManagerModel.setState(initialViewName);
            viewManagerModel.firePropertyChange();
        }

        return application;
    }
}
