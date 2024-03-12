
import java.util.ArrayList;

public class Map2 implements Map {

    private ArrayList<VehicleAlignment> vehicleAlignment;

    public Map2() {
        vehicleAlignment = new ArrayList<>();
    }

    @Override
    public ArrayList<VehicleAlignment> getVehiclesAlignment() {
        // Adding vehicles with different positions and alignments
        vehicleAlignment.add(new VehicleAlignment(RushHourShiftGame.CARA, VehicleAlignment.Alignment.HORIZONTAL, 2, 2));
        vehicleAlignment.add(new VehicleAlignment(RushHourShiftGame.CARB, VehicleAlignment.Alignment.VERTICAL, 4, 1));
        vehicleAlignment.add(new VehicleAlignment(RushHourShiftGame.CARC, VehicleAlignment.Alignment.HORIZONTAL, 3, 4));
        vehicleAlignment.add(new VehicleAlignment(RushHourShiftGame.CARD, VehicleAlignment.Alignment.HORIZONTAL, 1, 6));
        vehicleAlignment.add(new VehicleAlignment(RushHourShiftGame.CARE, VehicleAlignment.Alignment.VERTICAL, 0, 7));
        vehicleAlignment.add(new VehicleAlignment(RushHourShiftGame.CARF, VehicleAlignment.Alignment.HORIZONTAL, 5, 5));
        vehicleAlignment.add(new VehicleAlignment(RushHourShiftGame.CARG, VehicleAlignment.Alignment.VERTICAL, 7, 3));
        vehicleAlignment.add(new VehicleAlignment(RushHourShiftGame.CARH, VehicleAlignment.Alignment.HORIZONTAL, 6, 0));
        vehicleAlignment.add(new VehicleAlignment(RushHourShiftGame.CARI, VehicleAlignment.Alignment.VERTICAL, 8, 5));
        vehicleAlignment.add(new VehicleAlignment(RushHourShiftGame.CARJ, VehicleAlignment.Alignment.VERTICAL, 2, 7));
        vehicleAlignment.add(new VehicleAlignment(RushHourShiftGame.CAR1, VehicleAlignment.Alignment.VERTICAL, 4, 8));
        vehicleAlignment.add(new VehicleAlignment(RushHourShiftGame.CAR2, VehicleAlignment.Alignment.HORIZONTAL, 1, 9));

        return vehicleAlignment;
    }
}
