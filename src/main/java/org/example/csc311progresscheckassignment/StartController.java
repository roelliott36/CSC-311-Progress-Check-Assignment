
package org.example.csc311progresscheckassignment;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    private List<Card> selectedCards = new ArrayList<>();

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
        enterExpression.clear();
        displayExpression.clear();

        pickingFirstCardText.setVisible(false);
        pickingSecondCardText.setVisible(false);
        pickingThirdCardText.setVisible(false);
        pickingFourthCardText.setVisible(false);

        startButton.setDisable(true);

        displayFourCards();
    }

    private void displayFourCards() {
        CardDeck deck = new CardDeck();
        selectedCards = deck.drawFourCards();

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

        startButton.setDisable(true);

        displayFourCards();
    }

    @FXML
    private void verifyExpression() {
        String expression = enterExpression.getText().trim();

        displayExpression.setText(expression);

        if (selectedCards.size() != 4) {
            showMessage(
                    Alert.AlertType.INFORMATION,
                    "No Cards",
                    "Please click START to deal four cards first."
            );
            return;
        }

        if (expression.isEmpty()) {
            showMessage(
                    Alert.AlertType.INFORMATION,
                    "Empty Expression",
                    "Please enter an arithmetic expression."
            );
            enterExpression.requestFocus();
            return;
        }

        try {
            ExpressionEvaluator.Evaluation evaluation =
                    ExpressionEvaluator.evaluate(expression);

            List<Integer> expectedValues = new ArrayList<>();

            for (Card card : selectedCards) {
                expectedValues.add(card.getValue());
            }

            List<Integer> enteredValues = evaluation.getNumbers();

            Collections.sort(expectedValues);
            Collections.sort(enteredValues);

            if (!expectedValues.equals(enteredValues)) {
                showVerificationResult(
                        false,
                        "Your expression must use each displayed card value exactly once."
                );
                return;
            }

            double result = evaluation.getValue();

            if (!Double.isFinite(result)) {
                showVerificationResult(
                        false,
                        "Your expression does not produce a valid result."
                );
                return;
            }

            if (Math.abs(result - 24.0) < 0.000000001) {
                showVerificationResult(
                        true,
                        "Correct! Your expression evaluates to 24."
                );
            } else {
                showVerificationResult(
                        false,
                        String.format(
                                "Your expression evaluates to %s, not 24.",
                                result
                        )
                );
            }

        } catch (IllegalArgumentException exception) {
            showVerificationResult(
                    false,
                    "Your expression is invalid. Use numbers, +, -, *, /, " +
                            "and parentheses. Division by zero is not allowed."
            );
        }
    }

    private void showVerificationResult(boolean correct, String message) {
        ButtonType firstButton = new ButtonType(
                correct ? "New Combo" : "Try Again",
                ButtonBar.ButtonData.OK_DONE
        );

        ButtonType secondButton = new ButtonType(
                correct ? "Keep Current Cards" : "New Combo",
                ButtonBar.ButtonData.CANCEL_CLOSE
        );

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(correct ? "Correct!" : "Try Again");
        alert.setHeaderText(correct ? "You made 24!" : "Not quite!");
        alert.setContentText(message);
        alert.getButtonTypes().setAll(firstButton, secondButton);

        Optional<ButtonType> choice = alert.showAndWait();

        if (!choice.isPresent()) {
            return;
        }

        if (choice.get() == firstButton) {
            if (correct) {
                reshuffleCards();
            } else {
                enterExpression.requestFocus();
                enterExpression.selectAll();
            }
        } else if (choice.get() == secondButton) {
            if (correct) {
                enterExpression.requestFocus();
                enterExpression.selectAll();
            } else {
                reshuffleCards();
            }
        }
    }

    private void showMessage(
            Alert.AlertType type,
            String title,
            String message
    ) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
