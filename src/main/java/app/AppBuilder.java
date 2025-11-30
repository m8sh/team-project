package app;

import api_caller.api_caller;
import data_access.InMemoryDataAccessObject;
import interface_adapters.StartScreen.StartScreenController;
import interface_adapters.StartScreen.StartScreenPresenter;
import interface_adapters.StartScreen.StartScreenViewModel;
import interface_adapters.ViewManagerModel;
import interface_adapters.scoreboard.ScoreboardController;
import interface_adapters.scoreboard.ScoreboardPresenter;
import interface_adapters.scoreboard.ScoreboardViewModel;
import use_cases.StartScreen.*;
import use_cases.scoreboard.ScoreboardDataAccessInterface;
import use_cases.scoreboard.ScoreboardInputBoundary;
import use_cases.scoreboard.ScoreboardInteractor;
import use_cases.scoreboard.ScoreboardOutputBoundary;
import view.ScoreboardView;
import view.StartScreenView;
import view.ViewManager;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;

public class AppBuilder {

    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    // Shared DAO
    final InMemoryDataAccessObject lobbyDataAccessObject = new InMemoryDataAccessObject();

    // StartScreen
    private StartScreenViewModel startScreenViewModel;
    private StartScreenView startScreenView;
    private StartScreenController startScreenController;

    // Scoreboard
    private ScoreboardViewModel scoreboardViewModel;
    private ScoreboardView scoreboardView;
    // (controller kept as a field in case another view needs it later)
    private ScoreboardController scoreboardController;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    // ---------- VIEWS ----------
    public AppBuilder addScoreboardView() {
        scoreboardViewModel = new ScoreboardViewModel();
        scoreboardView = new ScoreboardView(scoreboardViewModel);
        cardPanel.add(scoreboardView, scoreboardView.getViewName());
        return this;
    }

    public AppBuilder addStartScreenView() {
        startScreenViewModel = new StartScreenViewModel();
        startScreenView = new StartScreenView(startScreenViewModel);
        cardPanel.add(startScreenView, startScreenView.getViewName());
        return this;
    }

    // ---------- USE CASES ----------
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

    public AppBuilder addStartScreenUseCase() throws MalformedURLException {
        StartScreenOutputBoundary outputBoundary =
                new StartScreenPresenter(startScreenViewModel);

        StartScreenLobbyDataAccessInterface lobbyDAI = lobbyDataAccessObject;
        StartScreenNetworkDataAccessInterface networkDAI;
        networkDAI = new api_caller();

        StartScreenInputBoundary interactor = new StartScreenInteractor(lobbyDAI, networkDAI, outputBoundary);
        startScreenController = new StartScreenController(interactor);
        startScreenView.setStartScreenController(startScreenController);
        return this;
    }

    // ---------- BUILD FRAME ----------

    public JFrame build() {
        final JFrame application = new JFrame("Team Project");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        // Starting on start screen now
        viewManagerModel.setState(startScreenView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }
}
