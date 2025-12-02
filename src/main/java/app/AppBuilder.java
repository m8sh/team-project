package app;

import api_caller.apiCaller;
import data_access.InMemoryDataAccessObject;
import data_access.SupabaseScoreboardDataAccess;
import entities.QuestionFactory;

import interface_adapters.AddQuestion.AddQuestionController;
import interface_adapters.AddQuestion.AddQuestionPresenter;
import interface_adapters.AddQuestion.LobbyPrepViewModel;

import interface_adapters.StartScreen.StartScreenController;
import interface_adapters.StartScreen.StartScreenPresenter;
import interface_adapters.StartScreen.StartScreenViewModel;

import interface_adapters.ViewManagerModel;

import interface_adapters.Scoreboard.ScoreboardController;
import interface_adapters.Scoreboard.ScoreboardPresenter;
import interface_adapters.Scoreboard.ScoreboardState;
import interface_adapters.Scoreboard.ScoreboardViewModel;

import use_cases.AddQuestion.AddQuestionInputBoundary;
import use_cases.AddQuestion.AddQuestionInteractor;
import use_cases.AddQuestion.AddQuestionOutputBoundary;

import use_cases.Scoreboard.ScoreboardDataAccessInterface;
import use_cases.Scoreboard.ScoreboardInputBoundary;
import use_cases.Scoreboard.ScoreboardInteractor;
import use_cases.Scoreboard.ScoreboardOutputBoundary;

import use_cases.StartScreen.StartScreenInputBoundary;
import use_cases.StartScreen.StartScreenLobbyDataAccessInterface;
import use_cases.StartScreen.StartScreenNetworkDataAccessInterface;
import use_cases.StartScreen.StartScreenOutputBoundary;

import View.LobbyPrepView;
import View.ScoreboardView;
import View.StartScreenView;
import View.ViewManager;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {

    // ---------- Shared Swing infrastructure ----------

    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    @SuppressWarnings("unused")
    private final ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    // In-memory lobby DAO used by multiple use cases
    private final InMemoryDataAccessObject lobbyDataAccessObject = new InMemoryDataAccessObject();

    // Shared network caller (WebSocket + HTTP)
    private apiCaller apiCaller;

    // ---------- START SCREEN ----------

    private StartScreenViewModel startScreenViewModel;
    private StartScreenView startScreenView;
    private StartScreenController startScreenController;

    // ---------- LOBBY PREP + ADD QUESTION ----------

    private LobbyPrepViewModel lobbyPrepViewModel;
    private LobbyPrepView lobbyPrepView;
    private AddQuestionController addQuestionController;

    // ---------- SCOREBOARD (Supabase-backed) ----------

    private ScoreboardViewModel scoreboardViewModel;
    private ScoreboardView scoreboardView;
    private ScoreboardController scoreboardController;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
        try {
            apiCaller = new apiCaller();
            // Share this apiCaller instance with the Game entrypoint (player side)
            Game.setApiCaller(apiCaller);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ---------- START SCREEN ----------

    public AppBuilder addStartScreenView() {
        startScreenViewModel = new StartScreenViewModel();
        startScreenView = new StartScreenView(startScreenViewModel,
                viewManagerModel, lobbyDataAccessObject);
        cardPanel.add(startScreenView, startScreenView.getViewName());
        return this;
    }

    public AppBuilder addStartScreenUseCase() {
        // note: lobbyPrepViewModel will be created later when a lobby is made
        StartScreenOutputBoundary outputBoundary =
                new StartScreenPresenter(startScreenViewModel, lobbyPrepViewModel);

        StartScreenLobbyDataAccessInterface lobbyDAI = lobbyDataAccessObject;
        StartScreenNetworkDataAccessInterface networkDAI = apiCaller;

        StartScreenInputBoundary interactor =
                new use_cases.StartScreen.StartScreenInteractor(lobbyDAI, networkDAI, outputBoundary);

        startScreenController = new StartScreenController(interactor);
        startScreenView.setStartScreenController(startScreenController);
        return this;
    }

    // ---------- SCOREBOARD (Supabase-backed) ----------

    public AppBuilder addScoreboardView() {
        scoreboardViewModel = new ScoreboardViewModel();
        scoreboardView = new ScoreboardView(scoreboardViewModel);
        cardPanel.add(scoreboardView, scoreboardView.getViewName());
        return this;
    }

    public AppBuilder addScoreboardUseCase() {
        ScoreboardOutputBoundary outputBoundary =
                new ScoreboardPresenter(scoreboardViewModel, viewManagerModel);

        ScoreboardDataAccessInterface dao = new SupabaseScoreboardDataAccess();

        ScoreboardInputBoundary interactor =
                new ScoreboardInteractor(dao, outputBoundary);

        scoreboardController = new ScoreboardController(interactor);
        scoreboardView.setScoreboardController(scoreboardController);

        // IMPORTANT: inject the same controller into apiCaller so the player GameFrame can use it
        apiCaller.setScoreboardController(scoreboardController);

        return this;
    }

    // ---------- LOBBY PREP + ADD QUESTION (HOST SIDE) ----------

    /**
     * Called when a new lobby is created on the host side.
     * @param lobbyPin the lobby PIN assigned to this lobby
     */
    public AppBuilder addLobbyPrepView(int lobbyPin) {
        // Create + configure LobbyPrepViewModel
        lobbyPrepViewModel = new LobbyPrepViewModel();
        lobbyPrepViewModel.setLobbyPin(lobbyPin);

        // Seed the HOST's scoreboard state with this lobby pin
        // so the host's ScoreboardView refresh knows which pin to use
        if (scoreboardViewModel != null) {
            ScoreboardState state = scoreboardViewModel.getState();
            if (state == null) {
                state = new ScoreboardState();
            }
            state.setLobbyPin(lobbyPin);
            // rows + error will be filled when showScoreboard() is actually called
            scoreboardViewModel.setState(state);
            scoreboardViewModel.firePropertyChange();
        }

        AddQuestionOutputBoundary presenter =
                new AddQuestionPresenter(lobbyPrepViewModel, viewManagerModel, scoreboardViewModel);

        QuestionFactory questionFactory = new QuestionFactory();
        AddQuestionInputBoundary interactor =
                new AddQuestionInteractor(lobbyDataAccessObject, presenter, questionFactory, apiCaller);

        addQuestionController = new AddQuestionController(interactor);

        lobbyPrepView = new LobbyPrepView(
                lobbyPrepViewModel,
                addQuestionController,
                viewManagerModel,
                scoreboardController  // host can go to scoreboard & refresh
        );
        cardPanel.add(lobbyPrepView, lobbyPrepView.getViewName());
        return this;
    }

    // ---------- BUILD ----------

    public JFrame build() {
        final JFrame application = new JFrame("QuizMe");
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
