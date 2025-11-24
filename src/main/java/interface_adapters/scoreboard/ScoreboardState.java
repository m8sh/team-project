package interface_adapters.scoreboard;
import java.util.List;

public class ScoreboardState {

    private List<ScoreboardRowViewModel> rows;
    private String errorMessage;

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
}
