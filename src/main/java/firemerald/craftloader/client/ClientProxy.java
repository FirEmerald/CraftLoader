package firemerald.craftloader.client;

import firemerald.craftloader.common.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy
{
	@Override
    public void onPreInitialization(FMLPreInitializationEvent event)
    {
		super.onPreInitialization(event);
		((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(new ReloadListener());
    }
}