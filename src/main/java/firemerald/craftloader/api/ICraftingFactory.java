package firemerald.craftloader.api;

import com.google.gson.JsonObject;

import net.minecraftforge.common.crafting.JsonContext;

@FunctionalInterface
public interface ICraftingFactory<T extends IRecipeKey, U>
{
	public U parse(T key, JsonContext context, JsonObject obj);
}