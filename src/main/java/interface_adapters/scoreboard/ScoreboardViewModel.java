package interface_adapters.scoreboard;

import interface_adapters.ViewModel;

public class ScoreboardViewModel extends ViewModel<ScoreboardState> {

    public static final String VIEW_NAME = "scoreboard";

    public ScoreboardViewModel() {
        super(VIEW_NAME);
        this.setState(new ScoreboardState());
    }
}