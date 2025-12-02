package interface_adapters.Scoreboard;
import java.util.List;

public class ScoreboardState {

    private List<ScoreboardRowViewModel> rows;
    private String errorMessage;
    private int lobbyPin;

    public List<ScoreboardRowViewModel> getRows() {
        return rows;
    }

    public void setRows(List<ScoreboardRowViewModel> rows) {
        this.rows = rows;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    public int getLobbyPin() {
        return lobbyPin;
    }

    public void setLobbyPin(int lobbyPin) {
        this.lobbyPin = lobbyPin;
    }
}
