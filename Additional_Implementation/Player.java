import java.util.ArrayList;

import Cards.Card;

public class Player {
    private String name;
    private char heroCar;
    private int horizontalBound;
    private ArrayList<Card> playerHand;

    public Player(String name, char heroCar, int horizontalBound) {
        this.name = name;
        this.heroCar = heroCar;
        this.horizontalBound = horizontalBound;
    }

    public String getName() {
        return name;
    }

    public char getHeroCar() {
        return heroCar;
    }

    public void setHeroCar(char heroCar) {
        this.heroCar = heroCar;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHorizontalBound() {
        return horizontalBound;
    }

    public void setHorizontalBound(int horizontalBound) {
        this.horizontalBound = horizontalBound;
    }

    public ArrayList<Card> getPlayerHand() {
        return playerHand;
    }

    public void setPlayerHand(ArrayList<Card> cards) {
        this.playerHand = cards;
    }


    
}
