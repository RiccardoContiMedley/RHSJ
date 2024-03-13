import java.util.ArrayList;
import java.util.Scanner;

import Cards.Card;
import Cards.Move;

public class ActionHandler {

    private static void makeMove(Player currentPlayer, RushHourShiftGame game) {
        boolean moveSuccessful = false;
        Scanner scanner = new Scanner(System.in);
        Card playedCard = null;
        while (!moveSuccessful) {
            printPlayersCards(currentPlayer);
            printUserCardsInstruction(currentPlayer);
            String[] actionType = HandlePlayerCardInput(scanner, currentPlayer, playedCard);
            if (actionType == null) {
                continue;
            }
            moveSuccessful = handleCardAction(playedCard, actionType, currentPlayer);
            addCardBackIfIllegalMove(currentPlayer, moveSuccessful, playedCard);
        }
    }

    private static void addCardBackIfIllegalMove(Player currentPlayer, boolean moveSuccessful, Card playedCard) {
        if (!moveSuccessful) {
            System.out.println("Illegal move. Please try again.");
            currentPlayer.getPlayerHand().add(playedCard);
        }
    }

    private static String[] HandlePlayerCardInput(Scanner scanner, Player currentPlayer, Card playedCard) {
        boolean cardInHand = false;
        String[] actionType = getCardActionPartsFromPlayer(scanner);
        printPlayersCardsBeforePlaying(currentPlayer);
        cardInHand = removeCardFromHand(currentPlayer, actionType, playedCard);
        if (!cardInHand) {
            System.out.println("You don't have that card in your hand");
            return null;
        }
        return actionType;
    }

    private static boolean removeCardFromHand(Player currentPlayer, String[] actionParts, Card playedCard) {
        for (int i = 0; i < 4; i++) {
            if (currentPlayer.getPlayerHand().get(i).getName().toLowerCase()
                    .equals(actionParts[0].toLowerCase())) {
                if (actionParts[0].toLowerCase().equals("move".toLowerCase())) {
                    Move playerCard = (Move) currentPlayer.getPlayerHand().get(i);
                    if (playerCard.getMovements() == Integer.parseInt(actionParts[1])) {
                        playedCard = currentPlayer.getPlayerHand().get(i);
                        currentPlayer.getPlayerHand().remove(i);
                        return true;
                    }
                } else {
                    playedCard = currentPlayer.getPlayerHand().get(i);
                    currentPlayer.getPlayerHand().remove(i);
                    return true;
                }
            }
        }
        return false;
    }

    private static void printUserCardsInstruction(Player currentPlayer) {
        System.out.println(currentPlayer.getName()
                + "'s turn. play the card the following way: move(spaces) or shift(spaces,dir, side), slide(car,dir,spaces), moveandsfhift??");
    }

    private static void printPlayersCardsBeforePlaying(Player currentPlayer) {
        System.out.println("Your hand before playing is: ");
        for (int i = 0; i < 4; i++) {
            System.out.println(currentPlayer.getPlayerHand().get(i).getName());
        }
    }

    private static String[] getCardActionPartsFromPlayer(Scanner scanner) {
        String actionType = scanner.next();
        String[] parts = actionType.split("\\W+");
        System.out.println("You played: " + actionType);
        return parts;
    }

    private static void printPlayersCards(Player currentPlayer) {
        System.out.println(currentPlayer.getName()
                + "has the following cards:");
        for (int i = 0; i < 4; i++) {
            if (currentPlayer.getPlayerHand().get(i).getName() == "Move") {
                System.out.println("Move" + ((Move) currentPlayer.getPlayerHand().get(i)).getMovements());
            } else {
                System.out.println(currentPlayer.getPlayerHand().get(i).getName());
            }
        }
    }

    private static boolean handleCardAction(Card card, String[] actionType, Player currentPlayer) {
        switch (card.getName()) {
            case "move":
                handleMoveCardAction(currentPlayer, actionType, card);
            case "shift":
                handleShiftAction();
            case "slide":
                handleSlideAction();
            case "ShiftAndMove":
                handleShiftAndMoveAction();
        }
    }

    private static void handleMoveCardAction(Player player, String[] actionType, Card card) {
        boolean stillHasMoves = false;

        for (int i = 0; i < actionType.length; i++) {

        }

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

}
