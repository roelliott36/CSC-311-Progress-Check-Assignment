package org.example.csc311progresscheckassignment;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class StartController {

    @FXML
    private VBox rootContainer;

    private final String[] themeClasses = {
            "theme-red",
            "theme-green",
            "theme-blue",
            "theme-yellow"
    };

    private int themeIndex = 0;

    @FXML
    private void initialize() {
        applyTheme();
    }

    @FXML
    private void changeTheme() {
        themeIndex = (themeIndex + 1) % themeClasses.length;
        applyTheme();
    }

    private void applyTheme() {
        rootContainer.getStyleClass().removeAll(
                "theme-red",
                "theme-green",
                "theme-blue",
                "theme-yellow"
        );

        rootContainer.getStyleClass().add(themeClasses[themeIndex]);
    }

}
