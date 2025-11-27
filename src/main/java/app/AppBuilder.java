package app;

import data_access.InMemoryDataAccessObject;
import interface_adapters.ViewManagerModel;
import interface_adapters.scoreboard.ScoreboardController;
import interface_adapters.scoreboard.ScoreboardPresenter;
import interface_adapters.scoreboard.ScoreboardViewModel;
import use_cases.scoreboard.ScoreboardDataAccessInterface;
import use_cases.scoreboard.ScoreboardInputBoundary;
import use_cases.scoreboard.ScoreboardInteractor;
import use_cases.scoreboard.ScoreboardOutputBoundary;
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

    // ---------- BUILD FRAME ----------

    public JFrame build() {
        final JFrame application = new JFrame("Team Project");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        // Start on scoreboard for now (later you’ll switch to lobby, etc.)
        viewManagerModel.setState(scoreboardView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }
}
