import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class RushHourShiftGame {

    // specify the grid
    private static final int GRID_ROWS = 16; // 3 grids of 4 rows each
    private static final int GRID_COLS = 14; // Each grid 5 by 4 by 5
    private char[][] gameGrid;
    private HashMap<Character, VehicleAlignment> vehicleAlignments;

    // Vehicles of the game
    public static final Vehicle CARA = new Vehicle('A', 3);
    public static final Vehicle CARB = new Vehicle('B', 3);
    public static final Vehicle CARC = new Vehicle('C', 2);
    public static final Vehicle CARD = new Vehicle('D', 2);
    public static final Vehicle CARE = new Vehicle('E', 2);
    public static final Vehicle CARF = new Vehicle('F', 2);
    public static final Vehicle CARG = new Vehicle('G', 2);
    public static final Vehicle CARH = new Vehicle('H', 2);
    public static final Vehicle CARI = new Vehicle('I', 3);
    public static final Vehicle CARJ = new Vehicle('J', 3);
    public static final Vehicle CAR1 = new Vehicle('1', 2);
    public static final Vehicle CAR2 = new Vehicle('2', 2);

    List<VehicleAlignment> vehicles = new ArrayList<>();

    private Map map;

    public RushHourShiftGame(Map map) {
        this.gameGrid = new char[GRID_ROWS][GRID_COLS];
        this.map = map;
        this.vehicleAlignments = new HashMap<>();
        initializeGrid();
    }

    /**
     * 
     * @return the number of cols in the grid
     */
    public static int getGridCols() {
        return GRID_COLS;
    }

    private void initializeGrid() {
        setUpGridWithEmptySpaces(gameGrid);
        setUpGridWithVehicles(map);
        System.out.println(getVehiclesInRows());
        System.out.println(getVehiclesInColumns());
        System.out.println(vehicles);
        // for every element in veichles print the letter, and the aligmnet and the size
        for (VehicleAlignment vh : vehicles) {
            System.out.println(vh.getVehicle().getLetter() + " " + vh.getAlignment() + " " + vh.getVehicle().getSize());
        }
    }

    public char[][] getGameGrid() {
        return this.gameGrid;
    }

    /**
     * sets up the grid wth dots on the playable part of the grid, and
     * # in parts of the grid that are not playable
     * 
     * @param gameGrid
     */
    private void setUpGridWithEmptySpaces(char[][] gameGrid) {
        // Fill the first 5 and last 5 rows with '#'
        for (int i = 0; i < GRID_ROWS; i++) {
            for (int j = 0; j < GRID_COLS; j++) {
                if (i < 5 || i >= GRID_ROWS - 5) {
                    gameGrid[i][j] = '#';
                } else {
                    gameGrid[i][j] = '.'; // Represent empty spaces in the middle rows
                }
            }
        }
    }

    /**
     * puts the vehicles on the grid
     * 
     * @param map
     */
    private void setUpGridWithVehicles(Map map) {
        for (VehicleAlignment vh : map.getVehiclesAlignment()) {
            vehicleAlignments.put(vh.getVehicle().getLetter(), vh);
            Vehicle vehicle = vh.getVehicle();
            // This specifies if it is horizontally or vertically aligned
            VehicleAlignment.Alignment alignment = vh.getAlignment();
            int vehicleRow = vh.getRow();
            int vehicleColumn = vh.getColumn();

            addVehicleToGrid(vehicle, alignment, vehicleRow, vehicleColumn);
            vehicles.add(vh);
        }
    }

    private void addVehicleToGrid(Vehicle vehicle, VehicleAlignment.Alignment vehicleAlignment, int row, int col) {
        for (int i = 0; i < vehicle.getSize(); i++) {
            if (vehicleAlignment == VehicleAlignment.Alignment.HORIZONTAL) {
                gameGrid[row][col + i] = vehicle.getLetter();
            } else {
                gameGrid[row + i][col] = vehicle.getLetter();
            }
        }
    }

    public void printGrid() {
        for (int i = 0; i < GRID_ROWS; i++) {
            for (int j = 0; j < GRID_COLS; j++) {
                System.out.print(gameGrid[i][j] + " ");
            }
            System.out.println();
        }
    }

    public boolean moveVehicle(char vehicleLetter, String direction) {
        List<int[]> vehiclePositions = getVehiclePositions(vehicleLetter);
        if (vehiclePositions.isEmpty()) {
            System.out.println("Vehicle not found.");
            return false;
        }

        VehicleAlignment vehicleAlignment = vehicleAlignments.get(vehicleLetter);
        if (vehicleAlignment == null) {
            System.out.println("Vehicle alignment not found.");
            return false;
        }

        // Check if the movement direction is compatible with the vehicle's alignment
        if (!isMoveCompatibleWithAlignment(vehicleAlignment.getAlignment(), direction)) {
            System.out.println("Vehicle cannot move against its alignment.");
            return false;
        }

        // Check if the vehicle is a player's car (marked with numbers)
        boolean isPlayerCar = Character.isDigit(vehicleLetter);

        // Sort vehiclePositions to find the leading edge
        sortVehiclePositions(vehiclePositions, direction);

        // Determine movement deltas
        int rowDelta = 0, colDelta = 0;
        switch (direction) {
            case "N":
                rowDelta = -1;
                break;
            case "S":
                rowDelta = 1;
                break;
            case "E":
                colDelta = 1;
                break;
            case "W":
                colDelta = -1;
                break;
            default:
                System.out.println("Invalid direction.");
                return false;
        }

        // Identify the leading edge based on direction
        int[] leadingEdge = vehiclePositions.get(0); // After sorting, the first element is the leading edge

        // Calculate the new position of the leading edge
        int newRow = leadingEdge[0] + rowDelta;
        int newCol = leadingEdge[1] + colDelta;

        // Additional rule for non-player cars: they should not move off the grid
        // horizontally
        if (!isPlayerCar && colDelta != 0 && (newCol < 0 || newCol >= GRID_COLS)) {
            System.out.println("Non-player vehicles cannot move off the grid.");
            return false;
        }

        // Check if the move is legal
        if (isMoveLegal(newRow, newCol, colDelta, isPlayerCar)) {
            // Update the vehicle position on the grid
            updateVehiclePosition(vehiclePositions, rowDelta, colDelta, vehicleLetter);
            return true;
        } else {
            System.out.println("Move is blocked or out of bounds.");
            return false;
        }
    }

    private boolean isMoveCompatibleWithAlignment(VehicleAlignment.Alignment alignment, String direction) {
        if (alignment == VehicleAlignment.Alignment.HORIZONTAL && (direction.equals("N") || direction.equals("S"))) {
            return false; // Horizontal vehicles cannot move vertically
        } else if (alignment == VehicleAlignment.Alignment.VERTICAL
                && (direction.equals("E") || direction.equals("W"))) {
            return false; // Vertical vehicles cannot move horizontally
        }
        return true; // Move is compatible with the vehicle's alignment
    }

    private void sortVehiclePositions(List<int[]> vehiclePositions, String direction) {
        if ("E".equals(direction)) {
            vehiclePositions.sort((a, b) -> b[1] - a[1]); // East: Sort descending by column
        } else if ("W".equals(direction)) {
            vehiclePositions.sort(Comparator.comparingInt(a -> a[1])); // West: Sort ascending by column
        } else if ("N".equals(direction)) {
            vehiclePositions.sort(Comparator.comparingInt(a -> a[0])); // North: Sort ascending by row
        } else if ("S".equals(direction)) {
            vehiclePositions.sort((a, b) -> b[0] - a[0]); // South: Sort descending by row
        }
    }

    private boolean isMoveLegal(int newRow, int newCol, int colDelta, boolean isPlayerCar) {
        // Check for horizontal movement off the grid
        if (isPlayerCar && colDelta != 0 && (newCol < 0 || newCol >= GRID_COLS)) {
            return true;
        }
        // Ensure the new position is within grid bounds and is free
        return newRow >= 0 && newRow < GRID_ROWS && newCol >= 0 && newCol < GRID_COLS
                && gameGrid[newRow][newCol] == '.';
    }

    private void updateVehiclePosition(List<int[]> vehiclePositions, int rowDelta, int colDelta, char vehicleLetter) {
        // First, clear the current positions of the vehicle
        for (int[] position : vehiclePositions) {
            gameGrid[position[0]][position[1]] = '.';
        }
        // Then, update to the new positions
        for (int[] position : vehiclePositions) {
            int newRow = position[0] + rowDelta;
            int newCol = position[1] + colDelta;
            if (newRow >= 0 && newRow < GRID_ROWS && newCol >= 0 && newCol < GRID_COLS) { // Check bounds for safety
                gameGrid[newRow][newCol] = vehicleLetter;
            }
        }
    }

    public List<int[]> getVehiclePositions(char vehicleLetter) {
        List<int[]> positions = new ArrayList<>();
        for (int i = 0; i < GRID_ROWS; i++) {
            for (int j = 0; j < GRID_COLS; j++) {
                if (gameGrid[i][j] == vehicleLetter) {
                    positions.add(new int[] { i, j });
                }
            }
        }
        return positions;
    }

    public boolean shiftGrid(int gridPart, String direction) {
        // Determine the columns range to shift.
        int startCol = determineStartColumn(gridPart);
        int endCol = determineEndColumn(gridPart);

        // Check for horizontally aligned vehicles in the shift range.
        if (isVehicleAtShiftingEdge(gridPart)) {
            System.out.println("Shift aborted: horizontally aligned vehicle detected.");
            return false;
        }

        // Check if the shift is feasible before attempting it.
        if (!isShiftFeasible(gridPart, direction)) {
            return false;
        }

        // Perform the shift based on the direction.
        if ("N".equals(direction)) {
            shiftUp(startCol, endCol);
        } else if ("S".equals(direction)) {
            shiftDown(startCol, endCol);
        }

        return true;
    }

    private boolean isVehicleAtShiftingEdge(int gridPart) {
        int checkCol = (gridPart == 0) ? 3 : GRID_COLS - 4; // Last col of grid part 0 or first col of grid part 1
        int adjacentCol = (gridPart == 0) ? 4 : GRID_COLS - 5; // Next or previous column depending on the grid part

        for (int row = 0; row < GRID_ROWS; row++) {
            char edgeCell = gameGrid[row][checkCol];
            char adjacentCell = gameGrid[row][adjacentCol];

            // Check if there's a horizontally aligned vehicle at the edge
            if (edgeCell != '#' && edgeCell != '.' && edgeCell == adjacentCell) {
                // Found a horizontally aligned vehicle at the edge
                return true;
            }
        }
        return false;
    }

    private int determineStartColumn(int gridPart) {
        return (gridPart == 0) ? 0 : GRID_COLS - 4;
    }

    private int determineEndColumn(int gridPart) {
        return (gridPart == 0) ? 4 : GRID_COLS;
    }

    private boolean isShiftFeasible(int gridPart, String direction) {
        int checkRow = (direction.equals("N")) ? 6 : GRID_ROWS - 7;
        int checkCol = (gridPart == 0) ? 0 : GRID_COLS - 1;

        // Check for a barrier condition that prevents shifting.
        return !(gameGrid[checkRow][checkCol] == '#'
                && gameGrid[checkRow + (direction.equals("N") ? -1 : 1)][checkCol] != '#');
    }

    private void shiftUp(int startCol, int endCol) {
        for (int col = startCol; col < endCol; col++) {
            for (int row = 1; row < GRID_ROWS; row++) {
                gameGrid[row - 1][col] = gameGrid[row][col];
            }
            gameGrid[GRID_ROWS - 1][col] = '#'; // Optionally clear the last cell, depending on your grid's rules.
        }
    }

    private void shiftDown(int startCol, int endCol) {
        for (int col = startCol; col < endCol; col++) {
            for (int row = GRID_ROWS - 2; row >= 0; row--) {
                gameGrid[row + 1][col] = gameGrid[row][col];
            }
            gameGrid[0][col] = '#'; // Clear the first navigable cell.
        }
    }

    /**
     * returns an array containing for each row of the grid a map of the vehicles in
     * it
     */
    private HashMap<Integer, List<Vehicle>> getVehiclesInRows() {
        HashMap<Integer, List<Vehicle>> vehiclesInRows = new HashMap<>();
        for (int i = 0; i < GRID_ROWS; i++) {
            vehiclesInRows.put(i, new ArrayList<>());
        }
        for (int i = 0; i < GRID_ROWS; i++) {
            for (int j = 0; j < GRID_COLS; j++) {
                if (gameGrid[i][j] != '.' && gameGrid[i][j] != '#') {
                    vehiclesInRows.get(i).add(new Vehicle(gameGrid[i][j], 0));
                }
            }
        }
        return vehiclesInRows;
    }

    /**
     * returns an array containing for each column of the grid a map of the vehicles
     * in it
     */
    private HashMap<Integer, List<Vehicle>> getVehiclesInColumns() {
        HashMap<Integer, List<Vehicle>> vehiclesInColumns = new HashMap<>();
        for (int i = 0; i < GRID_COLS; i++) {
            vehiclesInColumns.put(i, new ArrayList<>());
        }
        for (int i = 0; i < GRID_ROWS; i++) {
            for (int j = 0; j < GRID_COLS; j++) {
                if (gameGrid[i][j] != '.' && gameGrid[i][j] != '#') {
                    vehiclesInColumns.get(j).add(new Vehicle(gameGrid[i][j], 0));
                }
            }
        }
        return vehiclesInColumns;
    }
}