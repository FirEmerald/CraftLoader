package firemerald.craftloader.common;

import firemerald.api.core.IProxy;
import firemerald.craftloader.CraftingLoader;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;

public class CommonProxy implements IProxy
{
	@Override
    public void onServerStarted(FMLServerStartedEvent event)
    {
    	CraftingLoader.loadRecipes(); //reload recipes
    }
}