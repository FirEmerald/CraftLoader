package firemerald.api.core.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ICustomInventoryState
{
	@SideOnly(Side.CLIENT)
	public IBlockState getState(int damage);
}