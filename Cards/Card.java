package Cards;
public abstract class Card {
    private String name;

    public Card(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // Method to describe the action of the card; subclasses will provide the implementation
    public abstract String actionDescription();
}
