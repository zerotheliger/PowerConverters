package powercrystals.powerconverters.common;

import net.minecraftforge.common.ForgeDirection;
import powercrystals.powerconverters.power.PowerSystem;

public class BridgeSideData {
    public ForgeDirection side;
    public PowerSystem powerSystem;
    public boolean isConsumer;
    public boolean isProducer;
    public boolean isConnected;
    public int voltageNameIndex;
    public double outputRate;
}
