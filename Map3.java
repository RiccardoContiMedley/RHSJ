import java.util.ArrayList;

public class Map3 implements Map {

    private ArrayList<VehicleAlignment> vehicleAlignment;

    public Map3() {
        vehicleAlignment = new ArrayList<>();
    }

    @Override
    public ArrayList<VehicleAlignment> getVehiclesAlignment() {
        // Adding vehicles with different positions and alignments
        vehicleAlignment
                .add(new VehicleAlignment(RushHourShiftGame.CARC, VehicleAlignment.Alignment.VERTICAL, 6, 3));
        vehicleAlignment.add(new VehicleAlignment(RushHourShiftGame.CAR1, VehicleAlignment.Alignment.HORIZONTAL, 6, 0));
        vehicleAlignment
                .add(new VehicleAlignment(RushHourShiftGame.CAR2, VehicleAlignment.Alignment.HORIZONTAL, 9, 12));

        return vehicleAlignment;
    }

    public char[] getPossibleVehicles() {
        char[] possibleVehicles = { 'C', '1', '2' };
        return possibleVehicles;
    }

    public String[] getPossibleDirections() {
        String[] possibleDirection = { "S", "E", "W" };
        return possibleDirection;
    }
}
