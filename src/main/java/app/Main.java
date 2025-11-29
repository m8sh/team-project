package app;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AppBuilder builder = new AppBuilder();

            builder.addLobbyPrepView(123)
                    .addScoreboardView()
                    .addScoreboardUseCase();
// Add your View and UseCase here

            JFrame app = builder.build();
            app.pack();
            app.setVisible(true);
        });
    }
}
