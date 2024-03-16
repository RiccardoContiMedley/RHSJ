import java.util.ArrayList;

import Cards.Card;

public class AIPlayerAction {
    private Card card;
    private ArrayList<Action> actions;
    private char[][] gameGrid;

    public AIPlayerAction(Card card, ArrayList<Action> actions) {
        this.card = card;
        this.actions = actions;

    }

    public Card getCard() {
        return card;
    }

    public ArrayList<Action> getActions() {
        return actions;
    }

    public void addActions(Action action) {
        actions.add(action);
    }

    public void execute(RushHourShiftGame game) {
        gameGrid = RushHourShiftGame.getGridCopy(game.getGameGrid());
        for (Action action : actions) {
            action.execute();
        }
    }

    public void undoMoves(RushHourShiftGame game) {
        game.setGameGrid(gameGrid);
    }

}
