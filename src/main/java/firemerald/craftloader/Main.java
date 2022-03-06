package firemerald.craftloader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import firemerald.api.core.CoreModMainClass;
import firemerald.api.core.IFMLEventHandler;
import firemerald.craftloader.client.ClientProxy;
import firemerald.craftloader.common.CommonProxy;
import firemerald.craftloader.plugin.Core;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Main extends CoreModMainClass<CommonProxy>
{
    public static final Logger LOGGER = LogManager.getLogger("Crafting Loader"); //has to be static to prevent a crash

	private static Main instance;

	public static Main instance()
	{
		return instance;
	}

	public static Logger logger()
	{
		return LOGGER;
	}

	public static CommonProxy proxy()
	{
		return instance.proxy;
	}

	public static SimpleNetworkWrapper network()
	{
		return instance.network;
	}

	public static void registerFMLEventHandler(IFMLEventHandler handler)
	{
		instance.addFMLEventHandler(handler);
	}

	public Main()
	{
		super();
		instance = this;
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected CommonProxy makeClientProxy()
	{
		return new ClientProxy();
	}

	@Override
	@SideOnly(Side.SERVER)
	protected CommonProxy makeServerProxy()
	{
		return new CommonProxy();
	}

	@Override
	public ModContainer getModContainer()
	{
		return Core.getInstance();
	}

	@Override
	public LoadController getLoadController()
	{
		return Core.loadController;
	}
}
