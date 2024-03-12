package Cards;

public class Move extends Card {
    private int movements;

    public Move(int movements) {
        super("Move");
        this.movements = movements;
    }

    public int getMovements() {
        return movements;
    }

    @Override
    public String actionDescription() {
        return "Allows " + movements + " movements in one round.";
    }


}
