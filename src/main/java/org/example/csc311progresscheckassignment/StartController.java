package org.example.csc311progresscheckassignment;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.net.URL;
import java.util.List;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;
import javafx.scene.control.TextField;

public class StartController {

    @FXML
    private Button startButton;

    @FXML
    private ImageView cardOneDisplay;

    @FXML
    private ImageView cardTwoDisplay;

    @FXML
    private ImageView cardThreeDisplay;

    @FXML
    private ImageView cardFourDisplay;

    @FXML
    private Label pickingFirstCardText;

    @FXML
    private Label pickingSecondCardText;

    @FXML
    private Label pickingThirdCardText;

    @FXML
    private Label pickingFourthCardText;

    @FXML
    private VBox rootContainer;

    @FXML
    private TextField enterExpression;

    @FXML
    private TextField displayExpression;

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

    @FXML
    private void startGame() {
        displayFourCards();

        pickingFirstCardText.setVisible(false);
        pickingSecondCardText.setVisible(false);
        pickingThirdCardText.setVisible(false);
        pickingFourthCardText.setVisible(false);

        startButton.setDisable(true);
    }


    private void displayFourCards() {
        CardDeck deck = new CardDeck();
        List<Card> selectedCards = deck.drawFourCards();

        animateCard(cardOneDisplay, selectedCards.get(0), 0);
        animateCard(cardTwoDisplay, selectedCards.get(1), 150);
        animateCard(cardThreeDisplay, selectedCards.get(2), 300);
        animateCard(cardFourDisplay, selectedCards.get(3), 450);
    }

    private void animateCard(ImageView imageView, Card card, int delayMillis) {
        URL imageURL = getClass().getResource(card.getImageLocation());

        if (imageURL == null) {
            System.err.println(
                    "Card image not found: " + card.getImageLocation()
            );
            return;
        }

        imageView.setImage(new Image(imageURL.toExternalForm()));

        imageView.setOpacity(0);
        imageView.setScaleX(0.7);
        imageView.setScaleY(0.7);

        PauseTransition delay =
                new PauseTransition(Duration.millis(delayMillis));

        delay.setOnFinished(event -> {
            FadeTransition fade = new FadeTransition(
                    Duration.millis(350), imageView
            );
            fade.setFromValue(0);
            fade.setToValue(1);

            ScaleTransition scale = new ScaleTransition(
                    Duration.millis(350), imageView
            );
            scale.setFromX(0.7);
            scale.setFromY(0.7);
            scale.setToX(1);
            scale.setToY(1);

            ParallelTransition animation =
                    new ParallelTransition(fade, scale);

            animation.play();
        });

        delay.play();
    }

    @FXML
    private void reshuffleCards() {
        enterExpression.clear();
        displayExpression.clear();

        pickingFirstCardText.setVisible(false);
        pickingSecondCardText.setVisible(false);
        pickingThirdCardText.setVisible(false);
        pickingFourthCardText.setVisible(false);

        displayFourCards();
    }

}
