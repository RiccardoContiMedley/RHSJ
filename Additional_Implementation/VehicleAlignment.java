public class VehicleAlignment {

    public enum Alignment {
        VERTICAL, HORIZONTAL
    }

    private Vehicle vehicle;
    private Alignment alignment;
    private int row;
    private int column;

    public VehicleAlignment(Vehicle vehicle, Alignment alignment, int row, int column) {
        this.vehicle = vehicle;
        this.alignment = alignment;
        this.row = row;
        this.column = column;
    }

    public int getColumn() {
        return column;
    }

    public Alignment getAlignment() {
        return alignment;
    }

    public int getRow() {
        return row;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }
}
