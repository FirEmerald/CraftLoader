package firemerald.craftloader.api;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;

import net.minecraftforge.common.crafting.JsonContext;

/**
 * Interface that recipe factories MUST implement!
 * 
 * @author FirEmerald
 *
 * @param <T> the recipe's key type
 * @param <U> the recipe's output type
 */
@FunctionalInterface
public interface ICraftingFactory<T extends IRecipeKey, U>
{
	/**
	 * Compile a recipe
	 * 
	 * @param key the recipe's key. Most of the time this is not needed.
	 * @param context the loading mod's JsonContext
	 * @param obj the recipe definition
	 * @return a compiled recipe. If the compilation fails, throw a JsonParseException or JsonSyntaxException.
	 */
	@Nonnull
	public U parse(T key, JsonContext context, JsonObject obj);
}