package interfaceAdapters.AddQuestion;

import java.beans.PropertyChangeSupport;

public class AddQuestionViewModel {
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private String message = "";

    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }




}
