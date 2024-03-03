package Cards;

public class ShiftAndMove extends Card {
    private int movements;
    private int shifts;

    public ShiftAndMove(int movements, int shifts) {
        super("ShiftAndMove");
        this.movements = movements;
        this.shifts = shifts;
    }

    public int getMovements() {
        return movements;
    }

    public int getShifts() {
        return shifts;
    }

    @Override
    public String actionDescription() {
        return "Allows " + movements + " movements and " + shifts + " board shifts in the same round.";
    }
}
