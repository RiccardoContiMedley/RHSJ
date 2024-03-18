import java.util.ArrayList;

public class Map4 implements Map {

    private ArrayList<VehicleAlignment> vehicleAlignment;

    public Map4() {
        vehicleAlignment = new ArrayList<>();
    }

    @Override
    public ArrayList<VehicleAlignment> getVehiclesAlignment() {
        // Adding vehicles with different positions and alignments
        vehicleAlignment
                .add(new VehicleAlignment(RushHourShiftGame.CARA, VehicleAlignment.Alignment.VERTICAL, 5, 0));
        vehicleAlignment.add(new VehicleAlignment(RushHourShiftGame.CAR1, VehicleAlignment.Alignment.HORIZONTAL, 6, 5));
        vehicleAlignment
                .add(new VehicleAlignment(RushHourShiftGame.CAR2, VehicleAlignment.Alignment.HORIZONTAL, 9, 5));

        return vehicleAlignment;
    }

    public char[] getPossibleVehicles() {
        char[] possibleVehicles = { 'A', '1', '2' };
        return possibleVehicles;
    }

    public String[] getPossibleDirections() {
        String[] possibleDirection = { "S", "E", "W" };
        return possibleDirection;
    }
}
