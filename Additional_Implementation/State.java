import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Cards.Card;
import Cards.Move;
import Cards.Slide;

public class State {

    private static RushHourShiftGame game;
    private Scanner scanner;
    private static List<Player> players;

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
            currentPlayer.makeMove(game);

            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }
    }

    public static boolean handleMoveAction(Player player, char vehicleLetter, String direction) {

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

    private static boolean isOpponentVehicle(Player currentPlayer, char vehicleLetter) {
        // Iterate over all players to check if the vehicleLetter belongs to an opponent
        for (Player player : players) {
            if (player != currentPlayer && player.getHeroCar() == vehicleLetter) {
                return true; // Found the vehicle letter belongs to an opponent
            }
        }
        return false; // The vehicle does not belong to an opponent
    }

    private static boolean crossesPlayerBound(Player player, char vehicleLetter, String direction) {
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

    public static boolean handleShiftAction(char gridPart, String direction, int amount) {
        boolean moveSuccessful = game.shiftGrid(Integer.parseInt(String.valueOf(gridPart)), direction, amount);
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
            player1 = new Player(player1Name, '1', RushHourShiftGame.getGridCols() - 1);
        }
        players.add(player1);

        System.out.println("Enter name for Player 2:");
        String player2Name = scanner.nextLine();
        players.add(new Player(player2Name, '2', 0));
    }

    public static boolean isGameOver() {
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
