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

    public void setGame(RushHourShiftGame game) {
        this.game = game;
    }

    public boolean handleShiftAction() {
        boolean moveSuccessful = game.shiftGrid(Integer.parseInt(String.valueOf(gridPart)), direction, amount);
        return moveSuccessful;
    }

    public String actionDescription() {
        return "Shifting " + gridPart + " towards " + direction;
    }

}
