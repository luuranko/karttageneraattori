package karttageneraattori.Logic;
/**
 * TileType is an enum class that contains all allowed types for Tiles.
 * Empty types are Tiles that have not yet been processed.
 * All TileTypes contain values for the chance of 
 * generating certain types of tiles nearby.
 * <p>
 */
public enum TileType {
    EMPTY(0.6, 0.4),
    SEA(0.7, 0.3),
    SAND(0, 1);

    private final double seaChance;
    private final double sandChance;
    
    TileType(double seaChance, double sandChance) {
        this.seaChance = seaChance;
        this.sandChance = sandChance;
    }

    double seaChance() {
        return seaChance;
    }

    double sandChance() {
        return sandChance;
    }
}
