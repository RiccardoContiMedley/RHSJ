public class Vehicle {
    private char letter; // Letter to represent the vehicle
    private int size; // Size of the vehicle

    // Constructor to initialize the vehicle properties
    public Vehicle(char letter, int size) {
        this.letter = letter;
        this.size = size;
    }

    // Getter for the letter
    public char getLetter() {
        return letter;
    }

    // Getter for the size
    public int getSize() {
        return size;
    }

    // Setter for the letter
    public void setLetter(char letter) {
        this.letter = letter;
    }

    // Setter for the size
    public void setSize(int size) {
        this.size = size;
    }

    // Method to display vehicle information
    @Override
    public String toString() {
        return "Vehicle{" +
                "letter=" + letter +
                ", size=" + size +
                '}';
    }
}