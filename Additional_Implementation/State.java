import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Cards.Card;
import Cards.Move;

public class State {

    private RushHourShiftGame game;
    private Scanner scanner;
    private List<Player> players;

    /**
     * Constructor for the State class called by main
     * first initializes the scanner object to read user input
     * and request for the palyers names
     */
    public State() {
        this.scanner = new Scanner(System.in);
        this.players = new ArrayList<>();
        initializePlayers();
    }

    public static void main(String[] args) {
        State gameState = new State();
        gameState.startGame();
        gameState.scanner.close();
    }

    private void setPlayersHand() {
        for (Player player : players) {
            // forr loop that iterates 4 times
            player.setPlayerHand(new ArrayList<Card>());
            for (int i = 0; i < 4; i++) {
                player.getPlayerHand().add(game.getDeck().drawCard());
            }
        }
    }

    private Map getMapFromUserInput() {
        Map chosenMap = null;
        while (chosenMap == null) {
            System.out.println("Please select your map e.g map1:");
            String mapName = scanner.nextLine();
            chosenMap = GetSelectedMapFromString(mapName);
            if (chosenMap == null) {
                System.out.println("Invalid map name. Please try again.");
            }
        }
        return chosenMap;
    }

    /**
     * This method is called to start the game by the main after state
     */
    public void startGame() {
        System.out.println("Welcome to Rush Hour Shift Game...");
        Map chosenMap = getMapFromUserInput();

        game = new RushHourShiftGame(chosenMap);

        setPlayersHand();

        game.printGrid();

        int currentPlayerIndex = 0;
        while (!isGameOver()) {
            Player currentPlayer = players.get(currentPlayerIndex);
            boolean moveSuccessful = false;
            Card playedCard = null;

            if (currentPlayer instanceof AIPlayer) {
                // AI move
                System.out.println("AI's turn...");
                ((AIPlayer) currentPlayer).makeMove(game);
                game.printGrid();
            } else {
                // human move
                while (!moveSuccessful) {

                    System.out.println(currentPlayer.getName()
                            + "has the following cards:");
                    for (int i = 0; i < 4; i++) {
                        if (currentPlayer.getPlayerHand().get(i).getName() == "Move") {
                            System.out.println("Move" + ((Move) currentPlayer.getPlayerHand().get(i)).getMovements());
                        } else {
                            System.out.println(currentPlayer.getPlayerHand().get(i).getName());
                        }
                    }

                    System.out.println(currentPlayer.getName()
                            + "'s turn. play the card the following way: move(spaces) or shift(spaces,dir, side), slide(car,dir,spaces), moveandsfhift??");
                    String actionType = scanner.next();
                    String[] parts = actionType.split("\\W+");
                    // check if actiontype corrisponds to one of the cards in the player's hand
                    // and if so remove it from the hand
                    System.out.println("You played: " + actionType);
                    System.out.println("Your hand before playing is: ");
                    for (int i = 0; i < 4; i++) {
                        System.out.println(currentPlayer.getPlayerHand().get(i).getName());
                    }
                    boolean cardInHand = false;
                    for (int i = 0; i < 4; i++) {
                        if (currentPlayer.getPlayerHand().get(i).getName().toLowerCase()
                                .equals(parts[0].toLowerCase())) {
                            playedCard = currentPlayer.getPlayerHand().get(i);
                            currentPlayer.getPlayerHand().remove(i);
                            cardInHand = true;
                            break;
                        }
                    }

                    for (int i = 0; i < currentPlayer.getPlayerHand().size(); i++) {
                        System.out.println(currentPlayer.getPlayerHand().get(i).getName() + "$$$$$$$$$$$");
                    }

                    if (!cardInHand) {
                        System.out.println("You don't have that card in your hand");
                        continue;
                    }
                    moveSuccessful = handleAction(currentPlayer, actionType);
                    System.out.println(moveSuccessful + "###########");
                    if (!moveSuccessful) {
                        System.out.println("Illegal move. Please try again.");
                        currentPlayer.getPlayerHand().add(playedCard);
                    }
                }
            }

            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }
    }

    private boolean handleAction(Player player, String actionCommand) {

        String[] parts = actionCommand.split("\\W+"); // splits at all "non-word" characters: move(1,E) -> [move, 1, E]
        // if (parts.length < 3) {
        // System.out.println("Invalid command format. Please try again.");
        // return false;
        // }
        String actionType = parts[0].trim();
        // char valueLetter = parts[1].trim().charAt(0);
        // String direction = parts[2].trim();

        switch (actionType.toLowerCase()) {
            case "move":
                return handleMoveCard(player, parts[1].trim().charAt(0));
            case "shift":
                // return handleShiftAction(player, valueLetter, direction);
            default:
                System.out.println("Invalid action. Please try again.");
                return false;
        }
    }

    /**
     * this method is gonna ask to the player numberOfMoevs times what move he wants
     * to do
     * and then it will call the handleMoveAction method after each move it receives
     * 
     * @param player
     * @param numberOfMoves
     * @return
     */
    private boolean handleMoveCard(Player player, char numberOfMoves) {
        boolean moveSuccessful = false;
        for (int i = 1; i <= numberOfMoves; i++) {
            System.out.println(player.getName()
                    + "'s move N" + i + ". What would you like to do? car,dir)");
            String move = scanner.next();

            String[] parts = move.split("\\W+"); // splits at all "non-word" characters: move(1,E) -> [move, 1, E]

            char valueLetter = parts[0].trim().charAt(0);
            String direction = parts[1].trim();
            moveSuccessful = handleMoveAction(player, Character.toUpperCase(valueLetter), direction.toUpperCase());
            if (!moveSuccessful) {
                return false;
            }
            if (i == numberOfMoves) {
                return true;
            }
        }

        System.out.println("porcodio");
        return moveSuccessful;
    }

    private boolean handleMoveAction(Player player, char vehicleLetter, String direction) {

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
        for (Player player : players) {
            if (player != currentPlayer && player.getHeroCar() == vehicleLetter) {
                return true; // Found the vehicle letter belongs to an opponent
            }
        }
        return false; // The vehicle does not belong to an opponent
    }

    private boolean crossesPlayerBound(Player player, char vehicleLetter, String direction) {
        // Get the vehicle's current positions
        List<int[]> vehiclePositions = game.getVehiclePositions(vehicleLetter);

        // Determine if the move is towards the player's restricted side
        // This requires knowing the player's starting side, which we deduce from their
        // horizontalBound
        // For simplicity, let's assume:
        // - Player starting from left (Player 1) has a higher horizontalBound (right
        // side of the grid)
        // - Player starting from right (Player 2) has a lower horizontalBound (left
        // side of the grid)

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

    private boolean handleShiftAction(Player player, char gridPart, String direction) {
        boolean moveSuccessful = game.shiftGrid(Integer.parseInt(String.valueOf(gridPart)), direction);
        game.printGrid();
        return moveSuccessful;
    }

    private void initializePlayers() {
        System.out.println("Enter name for Player 1:");
        String player1Name = scanner.nextLine();
        Player player1 = null;
        if (player1Name.isEmpty() || player1Name.isBlank() || player1Name == null || player1Name.equals("AI")) {
            player1Name = "AI";
            player1 = new AIPlayer();
        } else {
            player1 = new Player(player1Name, '1', 0);
        }
        players.add(player1);

        System.out.println("Enter name for Player 2:");
        String player2Name = scanner.nextLine();
        players.add(new Player(player2Name, '2', 0));
    }

    private boolean isGameOver() {
        char[][] gameGrid = game.getGameGrid(); // Get the current state of the game grid
        boolean player1InGame = false;
        boolean player2InGame = false;

        // Iterate through the game grid to check for the presence of player vehicles
        for (int row = 0; row < gameGrid.length; row++) {
            for (int col = 0; col < gameGrid[row].length; col++) {
                char cell = gameGrid[row][col];
                if (cell == players.get(0).getHeroCar()) { // Assuming '1' for Player 1
                    player1InGame = true;
                } else if (cell == players.get(1).getHeroCar()) { // Assuming '2' for Player 2
                    player2InGame = true;
                }
            }
        }

        // Check if a player's vehicle is no longer on the grid
        if (!player1InGame || !player2InGame) {
            // Declare the winner
            if (!player1InGame) {
                System.out.println(players.get(0).getName() + " wins!");
            } else {
                System.out.println(players.get(1).getName() + " wins!");
            }
            return true; // The game is over
        }

        return false; // The game is not over
    }

    private Map GetSelectedMapFromString(String mapName) {
        if ("map1".equalsIgnoreCase(mapName)) {
            return new Map1();
        } else if ("map2".equalsIgnoreCase(mapName)) {
            // return new Map2();
        }
        return null;
    }

}
