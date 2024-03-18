
import java.util.ArrayList;

public class Map1 implements Map {

        private ArrayList<VehicleAlignment> vehicleAlignment;

        public Map1() {
                vehicleAlignment = new ArrayList<>();
        }

        @Override
        public ArrayList<VehicleAlignment> getVehiclesAlignment() {
                vehicleAlignment.add(new VehicleAlignment(RushHourShiftGame.CARA,
                                VehicleAlignment.Alignment.VERTICAL, 8, 3));
                vehicleAlignment.add(new VehicleAlignment(RushHourShiftGame.CARB,
                                VehicleAlignment.Alignment.HORIZONTAL, 9, 0));
                vehicleAlignment.add(new VehicleAlignment(RushHourShiftGame.CARC,
                                VehicleAlignment.Alignment.VERTICAL, 5, 4));
                vehicleAlignment.add(new VehicleAlignment(RushHourShiftGame.CARD,
                                VehicleAlignment.Alignment.VERTICAL, 6, 5));
                vehicleAlignment.add(new VehicleAlignment(RushHourShiftGame.CARE,
                                VehicleAlignment.Alignment.VERTICAL, 7, 6));
                vehicleAlignment.add(new VehicleAlignment(RushHourShiftGame.CARF,
                                VehicleAlignment.Alignment.VERTICAL, 7, 7));
                vehicleAlignment.add(new VehicleAlignment(RushHourShiftGame.CARG,
                                VehicleAlignment.Alignment.VERTICAL, 8, 8));
                vehicleAlignment.add(new VehicleAlignment(RushHourShiftGame.CARH,
                                VehicleAlignment.Alignment.VERTICAL, 9, 9));
                vehicleAlignment.add(new VehicleAlignment(RushHourShiftGame.CARI,
                                VehicleAlignment.Alignment.VERTICAL, 5, 10));
                vehicleAlignment
                                .add(new VehicleAlignment(RushHourShiftGame.CARJ,
                                                VehicleAlignment.Alignment.HORIZONTAL, 6, 11));
                vehicleAlignment
                                .add(new VehicleAlignment(RushHourShiftGame.CAR1, VehicleAlignment.Alignment.HORIZONTAL,
                                                6, 0));
                vehicleAlignment
                                .add(new VehicleAlignment(RushHourShiftGame.CAR2, VehicleAlignment.Alignment.HORIZONTAL,
                                                9, 12));
                return vehicleAlignment;
        }

        public char[] getPossibleVehicles() {
                char[] possibleVehicles = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', '1', '2' };
                return possibleVehicles;
        }

        public int getDepth() {
                return 0;
        }

        public String[] getPossibleDirections() {
                String[] possibleDirection = { "N", "S", "E", "W" };
                return possibleDirection;
        }
}
