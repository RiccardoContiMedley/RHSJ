import java.util.List;

public class SlideAction extends Action {

    private RushHourShiftGame game;
    private Player player;
    private char vehicleLetter;
    private String direction;
    private int movements;

    public SlideAction(RushHourShiftGame game, Player player, char vehicleLetter, String direction, int movements) {
        this.game = game;
        this.player = player;
        this.vehicleLetter = vehicleLetter;
        this.direction = direction;
        this.movements = movements;
    }

    @Override
    public boolean execute() {
        return handleSlideAction();
    }

    public boolean handleSlideAction() {

        List<int[]> originalCarPosition = game.getVehiclePositions(vehicleLetter);

        boolean moveSuccessful = true;
        for (int i = 0; i < movements; i++) {
            // Attempt to move the vehicle. moveVehicle() should return a boolean indicating
            // success
            boolean result = handleMoveAction();

            if (State.isGameOver()) {
                return true;
            }

            if (!result) { // If any move is unsuccessful
                moveSuccessful = false;
                break; // Exit the loop if movement fails
            }
        }

        if (!moveSuccessful) {
            // If the move was not successful, reset the vehicle to its original positions
            game.setVehiclePositions(vehicleLetter, originalCarPosition);
            return false; // Indicate the action was not successful
        }

        // If this point is reached, all movements were successful
        game.printGrid();
        return true;
    }

    // this is really bad and redundant
    // change to self.
    public boolean handleMoveAction() {

        if (crossesPlayerBound(player, vehicleLetter, direction)) {
            System.out.println("You cannot move out from your starting side.");
            game.printGrid();
            return false;
        }

        if (!isOpponentVehicle(player, vehicleLetter)) {
            System.out.println("Moving vehicle " + vehicleLetter + " towards " + direction);
            boolean moveSuccessful = game.moveVehicle(vehicleLetter, direction);
            game.printGrid();
            return moveSuccessful;
        }

        System.out.println("You cannot move the opponnents car");
        game.printGrid();
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
