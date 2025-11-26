package interfaceAdapters.NextQuestion;

import entities.Question;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class NextQuestionViewModel {

    private Question currentQuestion;
    private String popupMessage;

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public NextQuestionViewModel() {
        // start with no question
        this.currentQuestion = null;
        this.popupMessage = "";
    }

    public void setCurrentQuestion(Question question) {
        Question old = this.currentQuestion;
        this.currentQuestion = question;
        support.firePropertyChange("currentQuestion", old, this.currentQuestion);
    }

    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    public void setPopupMessage(String message) {
        String old = this.popupMessage;
        this.popupMessage = message;
        support.firePropertyChange("popup", old, this.popupMessage);
    }

    public String getPopupMessage() {
        return popupMessage;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
