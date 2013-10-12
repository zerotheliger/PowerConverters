package powercrystals.powerconverters.gui;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.ForgeDirection;
import org.lwjgl.opengl.GL11;
import powercrystals.powerconverters.PowerConverterCore;
import powercrystals.powerconverters.common.BridgeSideData;
import powercrystals.powerconverters.common.TileEntityEnergyBridge;
import powercrystals.powerconverters.renderer.ExposedGuiContainer;
import powercrystals.powerconverters.renderer.RenderUtility;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class GuiEnergyBridge extends ExposedGuiContainer {
    private static final ResourceLocation loc = new ResourceLocation(PowerConverterCore.guiFolder + "energybridge.png");
    private static final int _barColor = 255 | (165 << 8) | (255 << 24);
    private static final DecimalFormat format = new DecimalFormat("###.##");

    protected TileEntityEnergyBridge _bridge;
    private RenderUtility utility = new RenderUtility(this);

    public GuiEnergyBridge(ContainerEnergyBridge container, TileEntityEnergyBridge te) {
        super(container);
        ySize = 195;
        _bridge = te;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float gameTicks) {
        super.drawScreen(mouseX, mouseY, gameTicks);
        if (isPointInRegion(46, 90, 122, 9, mouseX, mouseY)) {
            ArrayList<String> tooltips = new ArrayList<String>(ForgeDirection.VALID_DIRECTIONS.length);
            {
                float percentage = 100F * (((float) _bridge.getEnergyStored()) / ((float) _bridge.getEnergyStoredMax()));
                tooltips.add(format.format(percentage) + "% " + StatCollector.translateToLocal("powerconverters.percentfull"));
            }
            for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                BridgeSideData data = _bridge.getDataForSide(dir);
                if (data != null && data.powerSystem != null) {
                    String unit = data.powerSystem.getUnit();
                    if (unit.length() > 2)
                        unit = unit.substring(0, unit.length() - 2);
                    String toAdd = (_bridge.getEnergyStored() / data.powerSystem.getInternalEnergyPerOutput()) + " " + unit;
                    if (!tooltips.contains(toAdd))
                        tooltips.add(toAdd);
                }
            }
            utility.drawTooltips(tooltips, mouseX, mouseY);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString(StatCollector.translateToLocal("powerconverters.common.bridge.name"), 8, 6, 4210752);

        if (_bridge.isInputLimited()) {
            fontRenderer.drawString(StatCollector.translateToLocal("powerconverters.inputlimited"), 98, 6, -1);
        } else {
            fontRenderer.drawString(StatCollector.translateToLocal("powerconverters.outputlimited"), 90, 6, -1);
        }

        for (int i = 0; i < 6; i++) {
            ForgeDirection dir = ForgeDirection.getOrientation(i);

            fontRenderer.drawString(StatCollector.translateToLocal("powerconverters." + dir.toString().toLowerCase()), 10, 6 + 12 * (i + 1), -1);
            BridgeSideData data = _bridge.getDataForSide(dir);

            if ((data.isConsumer || data.isProducer) && data.powerSystem != null) {
                String name = data.powerSystem.getAbbreviation();
                if (data.powerSystem.getVoltageNames() != null) {
                    name += " " + data.powerSystem.getVoltageNames()[data.voltageNameIndex];
                }
                fontRenderer.drawString(name, 49, 6 + 12 * (i + 1), -1);
                fontRenderer.drawString(data.isConsumer ? StatCollector.translateToLocal("powerconverters.in") : StatCollector.translateToLocal("powerconverters.out"), 92, 6 + 12 * (i + 1), -1);
                fontRenderer.drawString(getOutputRateString(data), 119, 6 + 12 * (i + 1), -1);
            } else {
                fontRenderer.drawString(StatCollector.translateToLocal("powerconverters.none"), 49, 6 + 12 * (i + 1), -1);
            }
        }

        fontRenderer.drawString("% CHG", 10, 90, -1);

        GL11.glDisable(GL11.GL_LIGHTING);
        drawRect(46, 97, 46 + _bridge.getEnergyScaled(), 89, _barColor);
        GL11.glEnable(GL11.GL_LIGHTING);

        fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float gameTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(loc);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

        for (int i = 0; i < 6; i++) {
            ForgeDirection dir = ForgeDirection.getOrientation(i);
            BridgeSideData data = _bridge.getDataForSide(dir);

            if ((data.isConsumer || data.isProducer) && data.powerSystem != null) {
                if (!data.isConnected) {
                    drawTexturedModalRect(x + 7, y + 15 + 12 * i, 0, 208, 162, 12);
                } else if (data.outputRate == 0) {
                    drawTexturedModalRect(x + 7, y + 15 + 12 * i, 0, 234, 162, 12);
                } else {
                    drawTexturedModalRect(x + 7, y + 15 + 12 * i, 0, 195, 162, 12);
                }
            } else {
                drawTexturedModalRect(x + 7, y + 15 + 12 * i, 0, 221, 162, 12);
            }
        }
    }

    private String getOutputRateString(BridgeSideData data) {
        if (!data.isConnected) return StatCollector.translateToLocal("powerconverters.nolink");
        double rate = data.outputRate;
        if (rate > 1000) {
            double rateThousand = (rate / 1000.0);
            return String.format("%.1f %s%s", rateThousand, "k", data.powerSystem.getUnit());
        }
        return rate + " " + data.powerSystem.getUnit();
    }
}
