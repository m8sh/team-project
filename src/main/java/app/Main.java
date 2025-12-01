package app;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            AppBuilder builder = new AppBuilder();

            builder.addStartScreenView()
                    .addStartScreenUseCase()
                    .addLobbyPrepView(123)
                    .addScoreboardView()
                    .addScoreboardUseCase();
//                    .addAddQuestionUseCase();  // <- this is where controller gets injected

            JFrame app = builder.build();
            app.pack();
            app.setVisible(true);
        });
    }
}
