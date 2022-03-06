package firemerald.craftloader.plugin;

import firemerald.api.core.plugin.TransformerBase;
import firemerald.craftloader.plugin.transformers.TransformCraftingHelper;

public class Transformer extends TransformerBase
{
	@Override
	public void addCommonTransformers()
	{
		transformers.put("net.minecraftforge.common.crafting.CraftingHelper", TransformCraftingHelper.INSTANCE);
	}

	@Override
	public void addClientTransformers()
	{
	}
}