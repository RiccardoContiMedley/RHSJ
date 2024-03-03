package Cards;

public class Shift extends Card {
    private int squares;

    public Shift(int squares) {
        super("Shift");
        this.squares = squares;
    }

    public int getSquares() {
        return squares;
    }

    @Override
    public String actionDescription() {
        return "Allows you to move one side of the board by " + squares + " squares.";
    }
}
