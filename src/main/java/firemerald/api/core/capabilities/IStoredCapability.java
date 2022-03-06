package firemerald.api.core.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public interface IStoredCapability extends ICapabilitySerializable<NBTBase>
{
	public NBTBase writeNBT();

	public void readNBT(NBTBase nbt);

	@Override
	public default NBTBase serializeNBT()
	{
		return writeNBT();
	}

	@Override
	public default void deserializeNBT(NBTBase nbt)
	{
		readNBT(nbt);
	}

	public Capability<?> getCapability();

	@Override
	public default boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == getCapability();
	}

	@SuppressWarnings("unchecked")
	@Override
	public default <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		return hasCapability(capability, facing) ? (T) this : null;
	}
}