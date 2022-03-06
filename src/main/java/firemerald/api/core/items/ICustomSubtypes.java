package firemerald.api.core.items;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ICustomSubtypes
{
	@SideOnly(Side.CLIENT)
	public void registerModels();
}