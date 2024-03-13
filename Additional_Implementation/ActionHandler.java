import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Cards.Card;
import Cards.Move;
import Cards.Shift;
import Cards.ShiftAndMove;

public class ActionHandler {

    private static Card playedCard = null;

    public static void makeMove(Player currentPlayer, RushHourShiftGame game) {
        boolean moveSuccessful = false;
        Scanner scanner = new Scanner(System.in);
        while (!moveSuccessful) {
            printPlayersCards(currentPlayer);
            printUserCardsInstruction(currentPlayer);
            String[] actionType = HandlePlayerCardInput(scanner, currentPlayer);
            if (actionType == null) {
                continue;
            }
            moveSuccessful = handleCardAction(actionType, currentPlayer, scanner, game);
            if (!moveSuccessful) {
                addCardBackForIllegalMove(currentPlayer);
            } else {
                currentPlayer.getPlayerHand().add(game.getDeck().drawCard());
            }
        }
    }

    private static void addCardBackForIllegalMove(Player currentPlayer) {
        System.out.println("Illegal move. Please try again.");
        currentPlayer.getPlayerHand().add(playedCard);
    }

    private static String[] HandlePlayerCardInput(Scanner scanner, Player currentPlayer) {
        boolean cardInHand = false;
        String[] actionType = getCardActionPartsFromPlayer(scanner);
        cardInHand = removeCardFromHand(currentPlayer, actionType);
        if (!cardInHand) {
            System.out.println("You don't have that card in your hand");
            return null;
        }
        return actionType;
    }

    private static boolean removeCardFromHand(Player currentPlayer, String[] actionParts) {
        for (int i = 0; i < 4; i++) {
            if (currentPlayer.getPlayerHand().get(i).getName().toLowerCase()
                    .equals(actionParts[0].toLowerCase())) {
                if (actionParts[0].toLowerCase().equals("move".toLowerCase())) {
                    Move playerCard = (Move) currentPlayer.getPlayerHand().get(i);
                    try {
                        if (playerCard.getMovements() == Integer.parseInt(actionParts[1])) {
                            playedCard = currentPlayer.getPlayerHand().get(i);
                            currentPlayer.getPlayerHand().remove(i);
                            return true;
                        }
                    } catch (Exception e) {
                        System.out.println("Not proper input format");
                        return false;
                    }
                } else if (actionParts[0].toLowerCase().equals("shiftandmove".toLowerCase())) {
                    ShiftAndMove playerCard = (ShiftAndMove) currentPlayer.getPlayerHand().get(i);
                    try {
                        if (playerCard.getMovements() == Integer.parseInt(actionParts[1])) {
                            playedCard = currentPlayer.getPlayerHand().get(i);
                            currentPlayer.getPlayerHand().remove(i);
                            return true;
                        }
                    } catch (Exception e) {
                        System.out.println("Not proper input format");
                        return false;
                    }

                } else {
                    playedCard = currentPlayer.getPlayerHand().get(i);
                    currentPlayer.getPlayerHand().remove(i);
                    return true;
                }
            }
        }
        System.out.println("card does not exist in hand!");
        return false;
    }

    private static void printUserCardsInstruction(Player currentPlayer) {
        System.out.println(currentPlayer.getName()
                + "'s turn. play the card the following way: move(spaces) or shift(side,dir,spaces), slide(car,dir,spaces), shiftandmove(spaces)");
    }

    private static String[] getCardActionPartsFromPlayer(Scanner scanner) {
        String actionType = scanner.next();
        String[] parts = splitStringToParts(actionType);
        System.out.println("You played: " + actionType);
        return parts;
    }

    private static void printPlayersCards(Player currentPlayer) {
        System.out.println(currentPlayer.getName()
                + " has the following cards:");
        for (int i = 0; i < 4; i++) {
            if (currentPlayer.getPlayerHand().get(i).getName() == "Move") {
                System.out.println("Move" + ((Move) currentPlayer.getPlayerHand().get(i)).getMovements());
            } else if (currentPlayer.getPlayerHand().get(i).getName() == "ShiftAndMove") {
                System.out
                        .println("ShiftAndMove" + ((ShiftAndMove) currentPlayer.getPlayerHand().get(i)).getMovements());
            } else {
                System.out.println(currentPlayer.getPlayerHand().get(i).getName());
            }
        }
    }

    private static boolean handleCardAction(String[] actionType, Player currentPlayer, Scanner scanner,
            RushHourShiftGame game) {
        switch (actionType[0].toLowerCase()) {
            case "move":
                return handleMoveCardAction(currentPlayer, scanner);
            case "shift":
                return handleShiftCardAction(actionType);
            case "slide":
                return handleSlideCardAction(currentPlayer, actionType, game);
            case "shiftandmove":
                return handleShiftAndMoveCardAction(currentPlayer, scanner);
        }
        return false;
    }

    private static boolean handleMoveCardAction(Player player, Scanner scanner) {
        Move moveCard = (Move) playedCard;
        int movements = moveCard.getMovements();

        int moveNumber = 0;

        while (moveNumber != movements) {
            System.out.println(player.getName()
                    + "'s move Number " + (moveNumber + 1) + ". What would you like to do? (car,dir)");
            boolean moveSuccessful = handleMovement(player, scanner);
            if (!moveSuccessful) {
                System.out.println("Not succesfull move");
                continue;
            }
            moveNumber++;
        }

        return true;
    }

    private static boolean handleSlideCardAction(Player player, String[] actionString, RushHourShiftGame game) {
        try {
            char vehicleLetter = actionString[1].trim().charAt(0);
            String direction = actionString[2];
            int movements = Integer.parseInt(actionString[3]);

            // Original positions before any movements
            List<int[]> originalCarPosition = game.getVehiclePositions(vehicleLetter);

            boolean moveSuccessful = true;
            for (int i = 0; i < movements; i++) {
                // Attempt to move the vehicle. moveVehicle() should return a boolean indicating
                // success
                boolean result = State.handleMoveAction(player, vehicleLetter, direction);

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
        } catch (Exception e) {
            System.out.println("Improper format");
            return false;
        }
    }

    private static boolean handleMovement(Player player, Scanner scanner) {

        String moveAction = scanner.next();
        char valueLetter;
        String direction;

        String[] parts = splitStringToParts(moveAction);
        try {
            valueLetter = parts[0].trim().charAt(0);
            direction = parts[1].trim();
        } catch (Exception E) {
            System.out.println("Please use the apppropriate input format.");
            return false;
        }
        boolean moveSuccessful = State.handleMoveAction(player, Character.toUpperCase(valueLetter),
                direction.toUpperCase());

        if (!moveSuccessful) {
            System.out.println("Move was not successfull please try again.");
            return false;
        }

        return true;
    }

    private static boolean handleShiftCardAction(String[] actionType) {
        try {
            return State.handleShiftAction(actionType[1].trim().charAt(0), actionType[2],
                    Integer.parseInt(actionType[3]));
        } catch (Exception e) {
            System.out.println("Improper format parsed to the input");
            return false;
        }
    }

    private static boolean handleShiftAndMoveCardAction(Player player, Scanner scanner) {
        ShiftAndMove moveCard = (ShiftAndMove) playedCard;
        int movements = moveCard.getMovements();
        int moveNumber = 0;
        boolean hasShiftedGrid = false;

        while (!(moveNumber == movements && hasShiftedGrid)) {
            System.out.println("Please choose (move) or (shift) to perform action");

            String action = scanner.next();
            if (action.toLowerCase().equals("move")) {
                if (!(moveNumber == movements)) {
                    System.out.println(player.getName()
                            + "'s move Number " + (moveNumber + 1) + ". What would you like to do? (car,dir)");
                    boolean hasMoved = handleMovement(player, scanner);
                    if (hasMoved) {
                        moveNumber++;
                    }
                } else {
                    System.out.println("You have already used your move action");
                    continue;
                }
            } else if (action.toLowerCase().equals("shift")) {
                while (!hasShiftedGrid) {
                    System.out.println("Please specify which grid you want to shift using shift(spaces,dir, side)");
                    String actionCommand = scanner.next();
                    String[] actionType = splitStringToParts(actionCommand);
                    hasShiftedGrid = handleShiftCardAction(actionType);
                }
                if (hasShiftedGrid) {
                    System.out.println("You have used your shift action!");
                    continue;
                }
            }
        }

        return true;
    }

    private static String[] splitStringToParts(String actionString) {
        return actionString.split("\\W+");
    }

}
