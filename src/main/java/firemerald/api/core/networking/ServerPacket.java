package firemerald.api.core.networking;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public abstract class ServerPacket implements IMessage
{
	public abstract void handleServerSide(EntityPlayerMP player);

	public static class Handler<REQ extends ServerPacket> implements IMessageHandler<REQ, IMessage>
	{
		@Override
		public IMessage onMessage(REQ message, MessageContext ctx)
		{
			if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
			{
				EntityPlayerMP player = ((NetHandlerPlayServer) ctx.netHandler).player;
				message.handleServerSide(player);
			}
			return null;
		}
	}
}