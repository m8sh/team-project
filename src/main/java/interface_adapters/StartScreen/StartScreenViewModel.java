package interface_adapters.StartScreen;

import interface_adapters.ViewModel;

public class StartScreenViewModel extends ViewModel<StartScreenState> {
    public static final String VIEW_NAME = "start screen";
    public StartScreenViewModel() {
        super(VIEW_NAME);
        this.setState(new StartScreenState());
    }
}
