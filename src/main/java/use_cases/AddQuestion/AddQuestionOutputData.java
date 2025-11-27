package use_cases.AddQuestion;

import java.util.List;

public class AddQuestionOutputData {
    private final String message;


    public AddQuestionOutputData(String message) {
        this.message = message;

    }

    public String getMessage() {
        return message;
    }
}


//
//    private final String message;
//    private final List<QuestionData> questions;
//
//    public AddQuestionOutputData(String message, List<QuestionData> questions) {
//        this.message = message;
//        this.questions = questions;
//    }
//    public String getMessage() {return message;}
//
//    public List<QuestionData> getQuestions() {
//        return questions;
//    }
//
//    public static class QuestionData {
//        private final String prompt;
//        private final List<String> choices;
//        private final int correctIndex;
//
//        public QuestionData(String prompt, List<String> choices, int correctIndex) {
//            this.prompt = prompt;
//            this.choices = choices;
//            this.correctIndex = correctIndex;
//        }
//    public String getPrompt() {
//            return prompt;
//    }
//    public List<String> getChoices() {
//            return choices;
//    }
//    public int getCorrectIndex() {
//            return correctIndex;
//    }
//    public String getCorrectAnswer(){
//        if(correctIndex < 0 || correctIndex >= choices.size()) {
//            return choices.get(correctIndex);
//        }
//        return "Unknown";
//        //Return the string unknown if the correct index is out of bounds
//
//        }
//
//    }
//}