package entities;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


public class QuestionTest {

    @Test
    void testQuestionCreation(){
        List<String> choices = Arrays.asList("1", "2", "3");
        Question q = new Question("What is 1+1?", choices, 0);

        assertEquals("What is 1+1?", q.getPrompt());
        assertEquals(choices, q.getChoices());
        assertEquals(0, q.getCorrectIndex());
        assertEquals("1", q.getCorrectAnswer());
    }
    @Test
    void testGetChoicesImmutable() {
        List<String> choices = Arrays.asList("A", "B");
        Question q = new Question("Pick one", choices, 0);

        List<String> retrieved = q.getChoices();
        retrieved.add("C"); // should not affect original choices
        assertEquals(2, q.getChoices().size());
    }
    @Test
    void testZeroChoicesThrowsExeption() {
        List<String> choices = Arrays.asList("OnlyOne");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Question("Invalid question", choices, 0);
        });
        assertTrue(exception.getMessage().contains("at least two choices"));
    }
    @Test
    void testEmptyPromptThrowsException() {
        String emptyPrompt = "";
        List<String> choices = Arrays.asList("A", "B");
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Question(emptyPrompt, choices, 0)
        );
        assertTrue(exception.getMessage().contains("Prompt is empty"));
    }

    @Test
    void testExceptionForInvalidCorrectIndex() {
        List<String> choices = Arrays.asList("A", "B");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Question("Invalid index", choices, 2);
        });
        assertTrue(exception.getMessage().contains("out of range"));
    }

    @Test
    void testFactoryCreatesQuestion() {
        List<String> choices = Arrays.asList("X", "Y");
        QuestionFactory factory = new QuestionFactory();
        Question q = factory.createQuestion("Factory question", choices, 0);

        assertEquals("Factory question", q.getPrompt());
        assertEquals("X", q.getCorrectAnswer());
    }



}
