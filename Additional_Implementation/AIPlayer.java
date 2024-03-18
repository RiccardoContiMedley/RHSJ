import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import Cards.Card;
import Cards.Move;
import Cards.ShiftAndMove;

public class AIPlayer extends Player {

	private static final int MAX_DEPTH = 3;

	public AIPlayer() {
		super("AI", '1', RushHourShiftGame.getGridCols() - 1);
	}

	@Override
	public void makeMove(RushHourShiftGame game) {
		System.out.println("AI is taking it's turn");
		AIPlayerAction bestMove = selectBestMove(game, this);
		for (Action action : bestMove.getActions()) {
			System.out.println("Executing action:  " + action.actionDescription());
		}
		bestMove.execute(game);
		removeCardFromHand(this, bestMove.getCard());
		this.getPlayerHand().add(game.getDeck().drawCard());
		game.printGrid();
		System.out.println("Done executing move");
	}

	public static boolean removeCardFromHand(Player currentPlayer, Card card) {
		if (card.getName().equals("Move")) {
			Move movecard = (Move) card;
			return ActionHandler.removeCardFromHand(currentPlayer,
					new String[] { card.getName(), String.valueOf(movecard.getMovements()) });
		}
		if (card.getName().equals("MoveAndShift")) {
			Move movecard = (Move) card;
			return ActionHandler.removeCardFromHand(currentPlayer,
					new String[] { card.getName(), String.valueOf(movecard.getMovements()) });
		}
		return ActionHandler.removeCardFromHand(currentPlayer, new String[] { card.getName() });
	}

	private AIPlayerAction selectBestMove(RushHourShiftGame game, Player player) {
		int bestScore = Integer.MIN_VALUE;
		AIPlayerAction bestMove = null;

		ArrayList<AIPlayerAction> playerActions = generateAllPossibleActionsForPlayer(game, player);

		for (AIPlayerAction action : playerActions) {
			action.execute(game);
			int score = minimax(game, 0, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
			action.undoMoves(game);
			System.out.println("Action this" + action.getActions().get(0).actionDescription() + " has score " + score);
			if (score > bestScore) {
				bestScore = score;
				bestMove = action;
			}

		}

		return bestMove;
	}

	private int minimax(RushHourShiftGame game, int depth, boolean isMaximizingPlayer, int alpha, int beta) {
		if (depth == MAX_DEPTH || State.isGameOver()) {
			return game.evaluate(game);
		}

		if (isMaximizingPlayer) {
			int maxEval = Integer.MIN_VALUE;
			for (AIPlayerAction playerAction : generateAllPossibleActionsForPlayer(game, State.players.get(0))) {
				playerAction.execute(game); // You need to implement this method
				int eval = minimax(game, depth + 1, false, alpha, beta);
				playerAction.undoMoves(game); // And this one, to revert the move after evaluation
				maxEval = Math.max(maxEval, eval);
				alpha = Math.max(alpha, eval);
				if (beta <= alpha)
					break; // Alpha-beta pruning
			}
			return maxEval;
		} else {
			int minEval = Integer.MAX_VALUE;
			for (AIPlayerAction playerAction : generateAllPossibleActionsForPlayer(game, State.players.get(1))) {
				playerAction.execute(game);
				int eval = minimax(game, depth + 1, true, alpha, beta);
				playerAction.undoMoves(game);
				minEval = Math.min(minEval, eval);
				beta = Math.min(beta, eval);
				if (beta <= alpha)
					break; // Alpha-beta pruning
			}
			return minEval;
		}
	}

	private ArrayList<AIPlayerAction> generateAllPossibleActionsForPlayer(RushHourShiftGame game, Player player) {

		char[] possibleVehicles = { 'A', '1', '2' };
		String[] possibleDirection = { "S", "E", "W" };
		char[] possibleGridParts = { '0', '1' };
		int[] possibleMovements = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 };

		ArrayList<AIPlayerAction> playerActions = new ArrayList<AIPlayerAction>();

		for (Card card : player.getCardsInHand()) {
			switch (card.getName().toLowerCase()) {
				case "move":
					playerActions.addAll(getAllPossibleMoveActionsForPlayer(game, (Move) card, player, possibleVehicles,
							possibleDirection));
					break;
				case "shift":
					playerActions.addAll(getAllPossibleShiftActionsForPlayer(game, card, player, possibleGridParts,
							possibleDirection, possibleMovements));
					break;
				case "slide":
					playerActions.addAll(getAllPossibleSlideActionsForPlayer(game, card, player, possibleVehicles,
							possibleDirection, possibleMovements));
					break;
				case "shiftandmove":
					playerActions.addAll(getAllPossibleMoveAndShiftActionsForPlayer(game, card, player,
							possibleVehicles, possibleDirection, possibleGridParts, possibleMovements));
					break;
				default:
					break;
			}
		}
		return playerActions;
	}

	public ArrayList<AIPlayerAction> getAllPossibleMoveAndShiftActionsForPlayer(
			RushHourShiftGame game,
			Card card, // Assuming this is a generic Card that can represent both moves and shifts
			Player player,
			char[] possibleVehicles,
			String[] possibleDirections,
			char[] possibleGridParts,
			int[] possibleMovements) {

		ArrayList<AIPlayerAction> combinedActions = new ArrayList<>();

		// Generate all move actions first, then a shift
		for (char vehicle : possibleVehicles) {
			for (String moveDirection : possibleDirections) {
				for (int movement : possibleMovements) {
					for (char gridPart : possibleGridParts) {
						for (String shiftDirection : possibleDirections) {
							combinedActions.addAll(generateActionSequence(
									game, card, player, vehicle, moveDirection, movement,
									gridPart, shiftDirection, true));
							combinedActions.addAll(generateActionSequence(
									game, card, player, vehicle, moveDirection, movement,
									gridPart, shiftDirection, false));
						}
					}
				}
			}
		}

		return combinedActions;
	}

	private static ArrayList<AIPlayerAction> generateActionSequence(
			RushHourShiftGame game,
			Card card,
			Player player,
			char vehicle,
			String moveDirection,
			int movement,
			char gridPart,
			String shiftDirection,
			boolean moveFirst) {

		ArrayList<AIPlayerAction> actionSequences = new ArrayList<>();
		RushHourShiftGame dummyGameMoveFirst = new RushHourShiftGame(game.getMap());
		RushHourShiftGame dummyGameShiftFirst = new RushHourShiftGame(game.getMap());
		dummyGameMoveFirst.setGameGrid(RushHourShiftGame.getGridCopy(game.getGameGrid()));
		dummyGameShiftFirst.setGameGrid(RushHourShiftGame.getGridCopy(game.getGameGrid()));

		// Execute move then shift
		if (moveFirst) {
			executeMoveThenShift(dummyGameMoveFirst, player, vehicle, moveDirection, movement, gridPart, shiftDirection,
					actionSequences, card);
		} else {
			executeShiftThenMove(dummyGameShiftFirst, player, vehicle, moveDirection, movement, gridPart,
					shiftDirection, actionSequences, card);
		}

		return actionSequences;
	}

	private static void executeMoveThenShift(
			RushHourShiftGame game,
			Player player,
			char vehicle,
			String moveDirection,
			int movement,
			char gridPart,
			String shiftDirection,
			ArrayList<AIPlayerAction> actionSequences,
			Card card) {

		// Here you would execute a move followed by a shift in the game, then if
		// successful, add to actionSequences

		MoveAction moveAction = new MoveAction(game, player, vehicle, moveDirection);
		boolean moveSuccessful = moveAction.execute();
		if (moveSuccessful) {
			ShiftGridAction shiftAction = new ShiftGridAction(game, gridPart, shiftDirection, movement);
			boolean shiftSuccessful = shiftAction.execute();
			if (shiftSuccessful) {
				AIPlayerAction aiPlayerAction = new AIPlayerAction(card, new ArrayList<Action>());
				aiPlayerAction.addActions(moveAction);
				aiPlayerAction.addActions(shiftAction);
			}
		}
	}

	private static void executeShiftThenMove(
			RushHourShiftGame game,
			Player player,
			char vehicle,
			String moveDirection,
			int movement,
			char gridPart,
			String shiftDirection,
			ArrayList<AIPlayerAction> actionSequences,
			Card card) {

		ShiftGridAction shiftAction = new ShiftGridAction(game, gridPart, shiftDirection, movement);
		boolean shiftSuccessful = shiftAction.execute();
		if (shiftSuccessful) {
			MoveAction moveAction = new MoveAction(game, player, vehicle, moveDirection);
			boolean moveSuccessful = moveAction.execute();
			if (moveSuccessful) {
				AIPlayerAction aiPlayerAction = new AIPlayerAction(card, new ArrayList<Action>());
				aiPlayerAction.addActions(moveAction);
				aiPlayerAction.addActions(shiftAction);
			}
		}
	}

	private ArrayList<AIPlayerAction> getAllPossibleSlideActionsForPlayer(
			RushHourShiftGame game,
			Card card, // Assuming SlideCard is a type of Card specific to slide actions
			Player player,
			char[] possibleVehicles,
			String[] possibleDirections,
			int[] possibleMovements) {

		ArrayList<AIPlayerAction> possibleSlideActions = new ArrayList<>();

		// Iterate through all provided variables to test each combination
		for (char vehicle : possibleVehicles) {
			for (String direction : possibleDirections) {
				for (int movement : possibleMovements) {
					RushHourShiftGame dummyGame = new RushHourShiftGame(game.getMap());
					dummyGame.setGameGrid(RushHourShiftGame.getGridCopy(game.getGameGrid()));

					SlideAction slideAction = new SlideAction(dummyGame, player, vehicle, direction, movement);
					boolean moveSuccessful = slideAction.execute();

					if (moveSuccessful) {
						AIPlayerAction playerAction = new AIPlayerAction(
								card,
								new ArrayList<>(Arrays.asList(slideAction)));
						possibleSlideActions.add(playerAction);
					}
				}
			}
		}

		return possibleSlideActions;
	}

	private ArrayList<AIPlayerAction> getAllPossibleShiftActionsForPlayer(
			RushHourShiftGame game,
			Card card, // Assuming ShiftCard is a type of Card specific for shifting actions
			Player player, char[] possibleGridParts, String[] possibleDirections, int[] possibleMovements) {

		ArrayList<AIPlayerAction> possibleShiftActions = new ArrayList<>();

		// Iterate through all combinations of grid parts and directions
		for (char gridPart : possibleGridParts) {
			for (String direction : possibleDirections) {
				for (int movement : possibleMovements) {
					// Create a dummy game instance to simulate the shift action
					RushHourShiftGame dummyGame = new RushHourShiftGame(game.getMap());
					dummyGame.setGameGrid(RushHourShiftGame.getGridCopy(game.getGameGrid()));

					// Create and execute the shift action
					ShiftGridAction shiftAction = new ShiftGridAction(dummyGame, gridPart, direction, movement);
					boolean moveSuccessful = shiftAction.execute();

					// If the move is successful, add it to the list of possible actions
					if (moveSuccessful) {
						ArrayList<Action> actions = new ArrayList<>();
						actions.add(shiftAction);
						AIPlayerAction playerAction = new AIPlayerAction(card, actions);
						possibleShiftActions.add(playerAction);
					}
				}
			}
		}

		return possibleShiftActions;

	}

	private ArrayList<AIPlayerAction> getAllPossibleMoveActionsForPlayer(
			RushHourShiftGame game,
			Move card,
			Player player,
			char[] possibleVehicles,
			String[] possibleDirections) {

		ArrayList<AIPlayerAction> possiblePlayerMoveActions = new ArrayList<>();
		char[][] originalGrid = RushHourShiftGame.getGridCopy(game.getGameGrid());

		// Recursive function to generate and execute move combinations
		generateMoveCombinations(
				possiblePlayerMoveActions,
				new ArrayList<>(),
				originalGrid,
				card,
				player,
				possibleVehicles,
				possibleDirections,
				0,
				game,
				card.getMovements());

		return possiblePlayerMoveActions;
	}

	private void generateMoveCombinations(
			ArrayList<AIPlayerAction> possiblePlayerMoveActions,
			ArrayList<MoveAction> currentMoveSequence,
			char[][] originalGrid,
			Move card,
			Player player,
			char[] possibleVehicles,
			String[] possibleDirections,
			int currentDepth,
			RushHourShiftGame game,
			int maxDepth) {

		if (currentDepth == maxDepth) {
			AIPlayerAction playerAction = new AIPlayerAction(card, new ArrayList<>(currentMoveSequence));
			possiblePlayerMoveActions.add(playerAction);
			return;
		}

		for (char vehicleLetter : possibleVehicles) {
			for (String direction : possibleDirections) {
				RushHourShiftGame dummyGame = new RushHourShiftGame(game.getMap());
				dummyGame.setGameGrid(RushHourShiftGame.getGridCopy(originalGrid));
				MoveAction moveAction = new MoveAction(dummyGame, player, vehicleLetter, direction);
				boolean moveSuccessful = moveAction.execute();

				if (moveSuccessful) {
					ArrayList<MoveAction> newMoveSequence = new ArrayList<>(currentMoveSequence);
					moveAction.setGame(game);
					newMoveSequence.add(moveAction);
					generateMoveCombinations(
							possiblePlayerMoveActions,
							newMoveSequence,
							dummyGame.getGameGrid(),
							card,
							player,
							possibleVehicles,
							possibleDirections,
							currentDepth + 1,
							game,
							maxDepth);
				}
			}
		}
	}

	public void performAction(AIPlayerAction playerAction, RushHourShiftGame game) {
		playerAction.execute(game);
	}

	public void undoMoves(AIPlayerAction playerAction, RushHourShiftGame game) {
		playerAction.undoMoves(game);
	}

}