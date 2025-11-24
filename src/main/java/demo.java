import data_access.InMemoryDataAccessObject;
import entities.*;

import java.util.Arrays;
import java.util.List;

public class demo {
    public static void main(String[] args) {
        Lobby l =  new Lobby(123);
        InMemoryDataAccessObject LobbyStorage = new InMemoryDataAccessObject(l);
        UserFactory userFactory = new UserFactory();
        User bob = userFactory.makeUser("bob", 123);
        User john = userFactory.makeUser("john", 123);
        l.addUser(bob);
        l.addUser(john);
        QuestionFactory questionFactory = new QuestionFactory();
        List<String> choices = Arrays.asList("A", "B");
        Question Q1 = questionFactory.createQuestion("A is answer", choices, 0);
        Question Q2 = questionFactory.createQuestion("B is answer", choices, 1);
        l.addQuestion(Q1);
        l.addQuestion(Q2);
        System.out.println(l.getQuestions());
        System.out.println(l.getPin());


    }
}
