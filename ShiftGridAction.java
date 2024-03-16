public class ShiftGridAction extends Action {

    private RushHourShiftGame game;
    private char gridPart;
    private String direction;
    private int amount;

    public ShiftGridAction(RushHourShiftGame game, char gridPart, String direction, int amount) {
        this.game = game;
        this.gridPart = gridPart;
        this.direction = direction;
        this.amount = amount;
    }

    @Override
    public boolean execute() {
        return handleShiftAction();
    }

    public boolean handleShiftAction() {
        boolean moveSuccessful = game.shiftGrid(Integer.parseInt(String.valueOf(gridPart)), direction, amount);
        game.printGrid();
        return moveSuccessful;
    }

}
