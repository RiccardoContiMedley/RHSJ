public class AIPlayer extends Player {
	public AIPlayer() {
		super("AI", '1', 0);
	}

	@Override
	public void makeMove(RushHourShiftGame game) {
		System.out.println("AI is taking it's turn");
		Action bestMove = selectBestMove();
		bestMove.execute(game);
	}

	private Action selectBestMove() {
		return null;
	}
}