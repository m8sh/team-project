package use_cases.AddQuestion;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddQuestionDataHolderTests {

    @Test
    public void testInputDataGetters() {
        String prompt = "What is 2+2?";
        List<String> choices = Arrays.asList("3", "4", "5");
        int correctIndex = 1;
        int lobbyPin = 123;

        AddQuestionInputData inputData = new AddQuestionInputData(prompt, choices, correctIndex, lobbyPin);

        assertEquals(prompt, inputData.getPrompt(), "Prompt should match");
        assertEquals(choices, inputData.getChoices(), "Choices should match");
        assertEquals(correctIndex, inputData.getCorrectIndex(), "Correct index should match");
        assertEquals(lobbyPin, inputData.getLobbyPin(), "Lobby pin should match");
    }


}



