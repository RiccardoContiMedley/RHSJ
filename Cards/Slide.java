package Cards;

public class Slide extends Card {
    public Slide() {
        super("Slide");
    }

    @Override
    public String actionDescription() {
        return "Allows to slide a car as much as you want in the direction it's facing, or backwards.";
    }
}
