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
import use_cases.scoreboard.*;

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

    final InMemoryDataAccessObject lobbyDataAccessObject = new InMemoryDataAccessObject();
    private api_caller apiCaller;

    // StartScreen
    private StartScreenViewModel startScreenViewModel;
    private StartScreenView startScreenView;
    private StartScreenController startScreenController;

    // LobbyPrep / AddQuestion
    private LobbyPrepViewModel lobbyPrepViewModel;
    private LobbyPrepView lobbyPrepView;
    private AddQuestionController addQuestionController;

    // Scoreboard
    private ScoreboardViewModel scoreboardViewModel;
    private ScoreboardView scoreboardView;
    private ScoreboardController scoreboardController;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
        try {
            apiCaller = new api_caller();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ---------- START SCREEN ----------
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

    // ---------- SCOREBOARD ----------
    public AppBuilder addScoreboardView() {
        scoreboardViewModel = new ScoreboardViewModel();
        scoreboardView = new ScoreboardView(scoreboardViewModel);
        cardPanel.add(scoreboardView, scoreboardView.getViewName());
        return this;
    }

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

    // ---------- LOBBY PREP (VIEW ONLY) ----------
    public AppBuilder addLobbyPrepView(int lobbyPin) {
        lobbyPrepViewModel = new LobbyPrepViewModel();

        // controller is null for now; will be injected in addAddQuestionUseCase()
        lobbyPrepView = new LobbyPrepView(lobbyPrepViewModel, null, viewManagerModel);
        cardPanel.add(lobbyPrepView, lobbyPrepView.getViewName());
        return this;
    }

    // ---------- ADD QUESTION USE CASE ----------
    public AppBuilder addAddQuestionUseCase() {
        AddQuestionOutputBoundary outputBoundary =
                new AddQuestionPresenter(lobbyPrepViewModel, viewManagerModel, scoreboardViewModel);

        QuestionFactory questionFactory = new QuestionFactory();
        AddQuestionInputBoundary interactor =
                new AddQuestionInteractor(lobbyDataAccessObject, outputBoundary, questionFactory, apiCaller);

        addQuestionController = new AddQuestionController(interactor);

        // 🔵 This is the critical wiring step:
        if (lobbyPrepView != null) {
            lobbyPrepView.setAddQuestionController(addQuestionController);
        }
        return this;
    }

    // ---------- BUILD ----------
    public JFrame build() {
        final JFrame application = new JFrame("Team Project");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.add(cardPanel);

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
