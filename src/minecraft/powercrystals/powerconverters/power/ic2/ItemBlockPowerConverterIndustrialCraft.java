package powercrystals.powerconverters.power.ic2;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import powercrystals.powerconverters.mods.IndustrialCraft;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockPowerConverterIndustrialCraft extends ItemBlock
{
    public ItemBlockPowerConverterIndustrialCraft(int id)
    {
	super(id);
	setHasSubtypes(true);
	setMaxDamage(0);
    }

    @Override
    public int getMetadata(int i)
    {
	return i;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
	par3List.add(EnumChatFormatting.BLUE + (par1ItemStack.getItemDamage() % 2 == 0 ? "Max EU in: " : "Max EU out: ") + IndustrialCraft.INSTANCE.powerSystem.getVoltageValues()[par1ItemStack.getItemDamage() / 2] + (par1ItemStack.getItemDamage() >= 8 ? "+" : ""));
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
	int md = itemstack.getItemDamage();
	if (md == 0)
	    return "powerconverters.ic2.lv.consumer";
	if (md == 1)
	    return "powerconverters.ic2.lv.producer";
	if (md == 2)
	    return "powerconverters.ic2.mv.consumer";
	if (md == 3)
	    return "powerconverters.ic2.mv.producer";
	if (md == 4)
	    return "powerconverters.ic2.hv.consumer";
	if (md == 5)
	    return "powerconverters.ic2.hv.producer";
	if (md == 6)
	    return "powerconverters.ic2.ev.consumer";
	if (md == 7)
	    return "powerconverters.ic2.ev.producer";
	if (md == 8)
	    return "powerconverters.ic2.uv.consumer";
	if (md == 9)
	    return "powerconverters.ic2.uv.producer";
	return "unknown";
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubItems(int itemId, CreativeTabs creativeTab, List subTypes)
    {
	for (int i = 0; i <= 9; i++)
	{
	    subTypes.add(new ItemStack(itemId, 1, i));
	}
    }
}
