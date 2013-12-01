package powercrystals.powerconverters.power;

/**
 * Controls how the energy bridge should handle power production
 *
 * @author samrg472
 */
public interface ICustomHandler {

    /**
     * Controls whether the tile entity should handle power production itself
     *
     * @return whether to handle the tile entity or not
     */
    public boolean shouldHandle();

    /**
     * Only called if the tile entity is self handling
     *
     * @return output rate in converted units
     */
    public double getOutputRate();
}
