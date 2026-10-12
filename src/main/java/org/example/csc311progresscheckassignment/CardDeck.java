package org.example.csc311progresscheckassignment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardDeck {
    private List<Card> cards = new ArrayList<>();

    public CardDeck() {
        String[] suits = {"clubs", "diamonds", "hearts", "spades"};
        String[] ranks = {
                "ace", "2", "3", "4", "5", "6", "7",
                "8", "9", "10", "jack", "queen", "king"
        };

        for (String suit : suits) {
            for (int i = 0; i < ranks.length; i++) {
                int value = i + 1;

                String imageLocation =
                        "/card-photos/" + ranks[i] + "_of_" + suit + ".png";

                cards.add(new Card(value, imageLocation));
            }
        }
    }

    public List<Card> getCards() {
        return cards;
    }

    public List<Card> drawFourCards() {
        List<Card> shuffledCards = new ArrayList<>(cards);
        Collections.shuffle(shuffledCards);

        return new ArrayList<>(shuffledCards.subList(0, 4));
    }
}
