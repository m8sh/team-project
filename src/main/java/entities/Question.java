package entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Question {
    private final String prompt;
    private final List<String> choices;
    private final int correctIndex;

    /**
     *
     * @param prompt the text of the question
     * @param choices a list of at least two answer choices
     * @param correctIndex index of the correct answer choice in choices
     * @throws IllegalArgumentException if choices has less than two items or
     * if correctIndex is out of bounds
     */
    //We could potentially make an "answer" class with a "correct" boolean attribute

    public Question(String prompt, List<String> choices, int correctIndex) {
        // What happens when the user doesn't input all 4 answers?
        if (choices == null || choices.size() < 2){
            throw new IllegalArgumentException("A question must have at least two choices.");
            //Each Question needs to have at least two choices, we could possibly change this
        }
        if (correctIndex < 0 || correctIndex >= choices.size()){
            throw new IllegalArgumentException("Correct index is out of range");
        }
        if(prompt.isEmpty()){
            throw new IllegalArgumentException("Prompt is empty");
        }
        this.prompt = prompt;
        this.choices = new ArrayList<>(choices);
        this.correctIndex = correctIndex;
    }
    public String getPrompt() {
        return prompt;
    }
    public List<String> getChoices() {
        return new ArrayList<>(choices);
    }
    public int getCorrectIndex() {
        return correctIndex;
    }
    public String getCorrectAnswer() {
        if(correctIndex < 0 || correctIndex >= choices.size()) {
            return "Unknown";
        }
        return choices.get(correctIndex);
        //Return the string unknown if the correct index is out of bounds

    }

}
