package firemerald.api.core.networking;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ClientPacket implements IMessage
{
	@SideOnly(Side.CLIENT)
	public abstract void handleClientSide();

	public static class Handler<REQ extends ClientPacket> implements IMessageHandler<REQ, IMessage>
	{
		@Override
		public IMessage onMessage(REQ message, MessageContext ctx)
		{
			if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) message.handleClientSide();
			return null;
		}
	}
}