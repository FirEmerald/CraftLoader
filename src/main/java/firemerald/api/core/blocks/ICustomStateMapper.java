package firemerald.api.core.blocks;

import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ICustomStateMapper
{
	@SideOnly(Side.CLIENT)
	public IStateMapper getStateMapper();

	@SideOnly(Side.CLIENT)
	public default boolean useForItem()
	{
		return true;
	}
}