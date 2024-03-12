public class AIPlayer extends Player {
	public AIPlayer() {
		super("AI", '1', 0);
	}

	public void makeMove(RushHourShiftGame game) {
		// Move the vehicle 1 to the East
		game.moveVehicle('1', "E");
	}

}