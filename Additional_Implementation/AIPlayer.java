public class AIPlayer extends Player {
	public AIPlayer() {
		super("AI", '1', 0);
	}

	@Override
	public void makeMove(RushHourShiftGame game) {
		// Move the vehicle 1 to the East
		State.handleMoveAction(this, '1', "E");
	}

}
