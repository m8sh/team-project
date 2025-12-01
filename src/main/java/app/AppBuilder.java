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
import use_cases.AddQuestion.AddQuestionInputBoundary;
import use_cases.AddQuestion.AddQuestionInteractor;
import use_cases.AddQuestion.AddQuestionOutputBoundary;
import use_cases.Scoreboard.*;

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
        startScreenView = new StartScreenView(startScreenViewModel, viewManagerModel,
                lobbyDataAccessObject,
                scoreboardController);
        cardPanel.add(startScreenView, startScreenView.getViewName());
        return this;
    }

    public AppBuilder addStartScreenUseCase() {
        // 🔵 Pass LobbyPrepViewModel so presenter can push the pin into it
        StartScreenOutputBoundary outputBoundary =
                new StartScreenPresenter(startScreenViewModel, lobbyPrepViewModel);

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
                new ScoreboardPresenter(scoreboardViewModel, viewManagerModel);

        ScoreboardDataAccessInterface dao = lobbyDataAccessObject;

        ScoreboardInputBoundary interactor =
                new ScoreboardInteractor(dao, outputBoundary);

        scoreboardController = new ScoreboardController(interactor);
        scoreboardView.setScoreboardController(scoreboardController);
        return this;
    }

    // ---------- LOBBY PREP (VIEW + ADD QUESTION) ----------
    public AppBuilder addLobbyPrepView(int lobbyPin) {
        lobbyPrepViewModel = new LobbyPrepViewModel();

        AddQuestionOutputBoundary presenter =
                new AddQuestionPresenter(lobbyPrepViewModel, viewManagerModel, scoreboardViewModel);

        QuestionFactory questionFactory = new QuestionFactory();
        AddQuestionInputBoundary interactor =
                new AddQuestionInteractor(lobbyDataAccessObject, presenter, questionFactory, apiCaller);

        addQuestionController = new AddQuestionController(interactor);

        lobbyPrepView = new LobbyPrepView(lobbyPrepViewModel, addQuestionController, viewManagerModel);
        cardPanel.add(lobbyPrepView, lobbyPrepView.getViewName());
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
