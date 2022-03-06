package firemerald.api.core;

import net.minecraftforge.common.ForgeChunkManager.Ticket;

public interface IChunkLoader
{
	public boolean setTicket(Ticket ticket);
}