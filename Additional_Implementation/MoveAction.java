import java.util.List;

public class MoveAction extends Action {

    private Player player;
    private char vehicleLetter;
    private String direction;
    private RushHourShiftGame game;

    public MoveAction(RushHourShiftGame game, Player player, char vehicleLetter, String direction) {
        this.game = game;
        this.player = player;
        this.vehicleLetter = vehicleLetter;
        this.direction = direction;
    }

    @Override
    public boolean execute() {
        return handleMoveAction();
    }

    public void setGame(RushHourShiftGame game) {
        this.game = game;
    }

    public String actionDescription() {
        return "Moving " + vehicleLetter + " towards " + direction;
    }

    public boolean handleMoveAction() {

        if (crossesPlayerBound(player, vehicleLetter, direction)) {
            return false;
        }

        if (!isOpponentVehicle(player, vehicleLetter)) {
            boolean moveSuccessful = game.moveVehicle(vehicleLetter, direction);
            return moveSuccessful;
        }

        return false;
    }

    private boolean isOpponentVehicle(Player currentPlayer, char vehicleLetter) {
        // Iterate over all players to check if the vehicleLetter belongs to an opponent
        for (Player player : State.players) {
            if (player != currentPlayer && player.getHeroCar() == vehicleLetter) {
                return true; // Found the vehicle letter belongs to an opponent
            }
        }

        return false; // The vehicle does not belong to an opponent
    }

    private boolean crossesPlayerBound(Player player, char vehicleLetter, String direction) {
        // Get the vehicle's current positions
        List<int[]> vehiclePositions = game.getVehiclePositions(vehicleLetter);

        boolean isMovingTowardsRestrictedSide = false;
        if (player.getHorizontalBound() > RushHourShiftGame.getGridCols() / 2) { // Assuming Player 1 starts from the
                                                                                 // left
            if ("W".equals(direction)) { // Trying to move west from the left side
                // Check if any vehicle part is at the leftmost edge
                for (int[] position : vehiclePositions) {
                    if (position[1] == 0) {
                        isMovingTowardsRestrictedSide = true;
                        break;
                    }
                }
            }
        } else { // Assuming Player 2 starts from the right
            if ("E".equals(direction)) { // Trying to move east from the right side
                // Check if any vehicle part is at the rightmost edge
                for (int[] position : vehiclePositions) {
                    if (position[1] == RushHourShiftGame.getGridCols() - 1) {
                        isMovingTowardsRestrictedSide = true;
                        break;
                    }
                }
            }
        }

        return isMovingTowardsRestrictedSide;
    }

}
