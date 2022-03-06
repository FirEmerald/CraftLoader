package firemerald.craftloader.client;

import java.util.function.Predicate;

import firemerald.craftloader.CraftingLoader;
import net.minecraft.client.resources.IResourceManager;
import net.minecraftforge.client.resource.IResourceType;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;

public class ReloadListener implements ISelectiveResourceReloadListener
{
	@Override
	public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate)
	{
		if (Loader.instance().hasReachedState(LoaderState.AVAILABLE))
		{
			CraftingLoader.loadRecipes();
		}
	}
}
