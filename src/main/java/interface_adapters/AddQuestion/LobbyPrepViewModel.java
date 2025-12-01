package interface_adapters.AddQuestion;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class LobbyPrepViewModel {
    private int lobbyPin;
    private int questionCount = 0;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private String popupMessage;

    public LobbyPrepViewModel() {
        this.lobbyPin = 0;
    }
    public void setPopupMessage(String message) {
        this.popupMessage = message;
        support.firePropertyChange("popup", null, popupMessage);
    }
    public String getPopupMessage() {
        return popupMessage;
    }
    public void incrementQuestionCount() {
        questionCount++;
        support.firePropertyChange("questionCount", null, questionCount);
    }


    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
    public void setLobbyPin(int lobbyPin) {
        this.lobbyPin = lobbyPin;
    }

    public int getLobbyPin() { return lobbyPin; }
    public int getQuestionCount() { return questionCount; }


}