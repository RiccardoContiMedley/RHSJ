import java.util.ArrayList;

public interface Map {
    public ArrayList<VehicleAlignment> getVehiclesAlignment();

    public char[] getPossibleVehicles();

    public String[] getPossibleDirections();
}
