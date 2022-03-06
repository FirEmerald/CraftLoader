package firemerald.craftloader.api;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import firemerald.craftloader.CraftingLoader;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.JsonContext;

public class CraftLoaderAPI
{
	public static final String MOD_ID = "craftloader";
    public static final String API_VERSION = "1.0.0";
	public static final Logger LOGGER = LogManager.getLogger("CraftLoader API");
	
	public static <T extends IRecipeKey, U> void registerLoader(ResourceLocation name, Runnable preLoader, BiFunction<ResourceLocation, JsonObject, T> makeKey, BiConsumer<T, U> loadRecipe, Consumer<T> recipeDisabled, Consumer<Set<T>> postLoader)
	{
		CraftingLoader.registerLoader(name, preLoader, makeKey, loadRecipe, recipeDisabled, postLoader);
	}
	
	public static <T extends IRecipeKey, U> void registerLoader(ResourceLocation name, Runnable preLoader, BiFunction<ResourceLocation, JsonObject, T> makeKey, BiConsumer<T, U> loadRecipe, Consumer<T> recipeDisabled)
	{
		registerLoader(name, preLoader, makeKey, loadRecipe, recipeDisabled, disabled -> {});
	}
	
	public static <T extends IRecipeKey, U> void registerLoader(ResourceLocation name, BiFunction<ResourceLocation, JsonObject, T> makeKey, BiConsumer<T, U> loadRecipe, Consumer<T> recipeDisabled, Consumer<Set<T>> postLoader)
	{
		registerLoader(name, () -> {}, makeKey, loadRecipe, recipeDisabled, postLoader);
	}
	
	public static <T extends IRecipeKey, U> void registerLoader(ResourceLocation name, BiFunction<ResourceLocation, JsonObject, T> makeKey, BiConsumer<T, U> loadRecipe, Consumer<T> recipeDisabled)
	{
		registerLoader(name, () -> {}, makeKey, loadRecipe, recipeDisabled, disabled -> {});
	}
	
	public static <T> void registerLoader(ResourceLocation name, Runnable preLoader, BiConsumer<ResourceLocation, T> loadRecipe, Consumer<ResourceLocation> recipeDisabled, Consumer<Set<ResourceLocation>> postLoader)
	{
		registerLoader(name, () -> {}, (name2, obj) -> new RecipeKey(name2), (RecipeKey key, T recipe) -> loadRecipe.accept(key.name, recipe), disabled -> recipeDisabled.accept(disabled.name), disabled -> postLoader.accept(disabled.stream().map(key -> key.name).collect(Collectors.toSet())));
	}
	
	public static <T> void registerLoader(ResourceLocation name, Runnable preLoader, BiConsumer<ResourceLocation, T> loadRecipe, Consumer<ResourceLocation> recipeDisabled)
	{
		registerLoader(name, () -> {}, (name2, obj) -> new RecipeKey(name2), (RecipeKey key, T recipe) -> loadRecipe.accept(key.name, recipe), disabled -> recipeDisabled.accept(disabled.name), disabled -> {});
	}
	
	public static <T> void registerLoader(ResourceLocation name, BiConsumer<ResourceLocation, T> loadRecipe, Consumer<ResourceLocation> recipeDisabled, Consumer<Set<ResourceLocation>> postLoader)
	{
		registerLoader(name, () -> {}, (name2, obj) -> new RecipeKey(name2), (RecipeKey key, T recipe) -> loadRecipe.accept(key.name, recipe), disabled -> recipeDisabled.accept(disabled.name), disabled -> postLoader.accept(disabled.stream().map(key -> key.name).collect(Collectors.toSet())));
	}
	
	public static <T> void registerLoader(ResourceLocation name, BiConsumer<ResourceLocation, T> loadRecipe, Consumer<ResourceLocation> recipeDisabled)
	{
		registerLoader(name, () -> {}, (name2, obj) -> new RecipeKey(name2), (RecipeKey key, T recipe) -> loadRecipe.accept(key.name, recipe), disabled -> recipeDisabled.accept(disabled.name), disabled -> {});
	}

	public static JsonContext getContext(String modId)
	{
		return CraftingLoader.getContext(modId);
	}

    public static JsonElement getJsonElement(JsonObject json, String memberName)
    {
        if (json.has(memberName))
        {
            return json.get(memberName);
        }
        else
        {
            throw new JsonSyntaxException("Missing " + memberName + ", expected to find a JsonElement");
        }
    }
}