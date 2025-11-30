package app;

import javax.swing.*;
import java.net.MalformedURLException;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AppBuilder builder = new AppBuilder();

            try {
                builder.addStartScreenView()
                        .addStartScreenUseCase()
                        .addScoreboardView()
                        .addScoreboardUseCase();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
// Add your View and UseCase here

            JFrame app = builder.build();
            app.pack();
            app.setVisible(true);
        });
    }
}
