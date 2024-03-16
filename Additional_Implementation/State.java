import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Cards.Card;
import Cards.Move;
import Cards.Slide;

public class State {

    private static RushHourShiftGame game;
    private Scanner scanner;
    public static List<Player> players = new ArrayList<>();
    private static Player winner;

    public State() {
        this.scanner = new Scanner(System.in);
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

        // Print out the initial distances from the exit
        System.out.println("Initial AI distance from exit: " + game.calculateDistanceFromExitAI(game));
        System.out.println("Initial player distance from exit: " + game.calculateDistanceFromExitPlayer(game));
        System.out.println("evaluating the game state: " + game.evaluate(game));

        int currentPlayerIndex = 0;
        while (!isGameOver()) {
            Player currentPlayer = players.get(currentPlayerIndex);
            currentPlayer.makeMove(game);
            System.out.println("Initial AI distance from exit: " + game.calculateDistanceFromExitAI(game));
            System.out.println("Initial player distance from exit: " + game.calculateDistanceFromExitPlayer(game));
            System.out.println("evaluating the game state: " + game.evaluate(game));
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();

        }

        System.out.println(winner.getName() + " wins!");
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
                winner = players.get(0);
            } else {
                winner = players.get(1);
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
