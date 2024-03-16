package Cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> cards;

    public Deck() {
        this.cards = new ArrayList<>();
        initializeDeck(); // Populate the deck upon construction
        shuffle(); // Shuffle the deck
    }

    private void initializeDeck() {
        // Add Move cards according to the specified quantities
        addMoveCards(1, 6); // 6 cards of Move 1
        addMoveCards(2, 6); // 6 cards of Move 2
        addMoveCards(3, 4); // 4 cards of Move 3
        addMoveCards(4, 3); // 3 cards of Move 4

        // Add Shift cards
        addShiftCards(1, 4); // 4 cards of Shift 1

        // Add Slide cards
        addSlideCards(3); // 3 cards of Slide

        // Add Shift and Move cards
        addShiftAndMoveCards(1, 1, 3); // 3 cards of Shift 1 and Move 1
        addShiftAndMoveCards(1, 1, 3); // 3 cards of Shift 1 and Move 2
    }

    private void addMoveCards(int movements, int count) {
        for (int i = 0; i < count; i++) {
            this.cards.add(new Move(movements));
        }
    }

    private void addShiftCards(int squares, int count) {
        for (int i = 0; i < count; i++) {
            this.cards.add(new Shift(squares));
        }
    }

    private void addShiftAndMoveCards(int squares, int movements, int count) {
        for (int i = 0; i < count; i++) {
            this.cards.add(new ShiftAndMove(squares, movements));
        }
    }

    private void addSlideCards(int count) {
        for (int i = 0; i < count; i++) {
            this.cards.add(new Slide());
        }
    }

    public void shuffle() {
        Collections.shuffle(this.cards);
    }

    public Card drawCard() {
        if (this.cards.isEmpty()) {
            return null; // Consider throwing an exception if this is a critical issue
        }
        return this.cards.remove(this.cards.size() - 1);
    }

    public void addCard(Card card) {
        this.cards.add(card);
    }

    /**
     * methjod that returns the cards in the deck
     */
    public List<Card> getCards() {
        return cards;
    }
}
