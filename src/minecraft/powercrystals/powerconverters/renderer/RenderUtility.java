package powercrystals.powerconverters.renderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.List;

/**
 * @author samrg472
 */
@SideOnly(Side.CLIENT)
public class RenderUtility {

    private ExposedGuiContainer container;

    public RenderUtility(ExposedGuiContainer container) {
        this.container = container;
    }

    public void drawTooltips(List<String> lines, int x, int y) {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glDisable(GL11.GL_LIGHTING);

        int tooltipWidth = 0;
        int tempWidth;
        int xStart;
        int yStart;

        for (String line : lines) {
            tempWidth = container.fontRenderer.getStringWidth(line);
            if (tempWidth > tooltipWidth)
                tooltipWidth = tempWidth;
        }

        xStart = x + 12;
        yStart = y - 12;
        int tooltipHeight = 8;

        if (lines.size() > 1) {
            tooltipHeight += 2 + (lines.size() - 1) * 10;
        }

        if (container.getGuiTop() + yStart + tooltipHeight + 6 > container.height) {
            yStart = container.height - tooltipHeight - container.getGuiTop() - 6;
        }

        container.setZLevel(300.0F);
        container.getItemRenderer().zLevel = 300.0F;
        int color1 = -267386864;
        container.drawGradientRect(xStart - 3, yStart - 4, xStart + tooltipWidth + 3, yStart - 3, color1, color1);
        container.drawGradientRect(xStart - 3, yStart + tooltipHeight + 3, xStart + tooltipWidth + 3, yStart + tooltipHeight + 4, color1, color1);
        container.drawGradientRect(xStart - 3, yStart - 3, xStart + tooltipWidth + 3, yStart + tooltipHeight + 3, color1, color1);
        container.drawGradientRect(xStart - 4, yStart - 3, xStart - 3, yStart + tooltipHeight + 3, color1, color1);
        container.drawGradientRect(xStart + tooltipWidth + 3, yStart - 3, xStart + tooltipWidth + 4, yStart + tooltipHeight + 3, color1, color1);
        int color2 = 1347420415;
        int color3 = (color2 & 16711422) >> 1 | color2 & -16777216;
        container.drawGradientRect(xStart - 3, yStart - 3 + 1, xStart - 3 + 1, yStart + tooltipHeight + 3 - 1, color2, color3);
        container.drawGradientRect(xStart + tooltipWidth + 2, yStart - 3 + 1, xStart + tooltipWidth + 3, yStart + tooltipHeight + 3 - 1, color2, color3);
        container.drawGradientRect(xStart - 3, yStart - 3, xStart + tooltipWidth + 3, yStart - 3 + 1, color2, color2);
        container.drawGradientRect(xStart - 3, yStart + tooltipHeight + 2, xStart + tooltipWidth + 3, yStart + tooltipHeight + 3, color3, color3);

        for (int stringIndex = 0; stringIndex < lines.size(); ++stringIndex) {
            String line = lines.get(stringIndex);

            if (stringIndex == 0) {
                line = "\u00a7" + Integer.toHexString(15) + line;
            } else {
                line = "\u00a77" + line;
            }

            container.fontRenderer.drawStringWithShadow(line, xStart, yStart, -1);

            if (stringIndex == 0)
                yStart += 2;
            yStart += 10;
        }

        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        container.setZLevel(0.0F);
        container.getItemRenderer().zLevel = 0.0F;
    }

}
