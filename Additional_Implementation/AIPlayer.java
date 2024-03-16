import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import Cards.Card;
import Cards.Move;

public class AIPlayer extends Player {

	public AIPlayer() {
		super("AI", '1', 0);
	}

	@Override
	public void makeMove(RushHourShiftGame game) {
		System.out.println("AI is taking it's turn");
		Action bestMove = selectBestMove();
		// bestMove.execute(game);
	}

	private Action selectBestMove() {
		return null;
	}

	private ArrayList<AIPlayerAction> generateAllPossibleActionsForPlayer(RushHourShiftGame game, Player player) {

		char[] possibleVehicles = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', '1', '2' };
		String[] possibleDirection = { "N", "S", "E", "W" };
		int[] possibleGridParts = { 0, 1 };
		int[] possibleMovements = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 };

		ArrayList<AIPlayerAction> playerActions = new ArrayList<AIPlayerAction>();

		for (Card card : player.getCardsInHand()) {
			switch (card.getName()) {
				case "move":
					playerActions.addAll(getAllPossibleMoveActionsForPlayer(game, (Move) card, player, possibleVehicles,
							possibleDirection));
					break;
				case "shift":
				case "slide":
				case "shiftandmove":
				default:
					break;
			}
		}
		return playerActions;
	}

	private ArrayList<AIPlayerAction> getAllPossibleMoveActionsForPlayer(RushHourShiftGame game, Move card,
			Player player,
			char[] possibleVehicles, String[] possibleDirection) {

		char[][] dummyGrid = RushHourShiftGame.getGridCopy(game.getGameGrid());
		ArrayList<AIPlayerAction> possiblePlayerMoveAction = new ArrayList<>();

		for (int i = 0; i < card.getMovements(); i++) {

		}

		for (char vehicleLetter : possibleVehicles) {
			for (String vehicleDirection : possibleDirection) {
				RushHourShiftGame dummyGame = new RushHourShiftGame(null);
				dummyGame.setGameGrid(dummyGrid);
				MoveAction moveAction = new MoveAction(dummyGame, player, vehicleLetter, vehicleDirection);
				boolean moveSuccessful = moveAction.execute();
				if (moveSuccessful) {
					AIPlayerAction playerAction = new AIPlayerAction(card, new ArrayList<>(Arrays.asList(moveAction)),
							dummyGame);
					possiblePlayerMoveAction.add(playerAction);
				}
			}
		}
		return possiblePlayerMoveAction;
	}

	public void performAction(AIPlayerAction playerAction) {
		playerAction.execute();
	}

	public void undoMoves(AIPlayerAction playerAction) {
		playerAction.undoMoves();
	}

}
