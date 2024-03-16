import java.util.ArrayList;

import Cards.Card;

public class AIPlayerAction {
    private Card card;
    private ArrayList<Action> actions;
    private char[][] gameGrid;
    private RushHourShiftGame game;

    public AIPlayerAction(Card card, ArrayList<Action> actions, RushHourShiftGame game) {
        this.card = card;
        this.actions = actions;
        this.game = game;
        gameGrid = RushHourShiftGame.getGridCopy(game.getGameGrid());
    }

    public Card getCard() {
        return card;
    }

    public void execute() {
        for (Action action : actions) {
            action.execute();
        }
    }

    public void undoMoves() {
        game.setGameGrid(gameGrid);
    }

}
