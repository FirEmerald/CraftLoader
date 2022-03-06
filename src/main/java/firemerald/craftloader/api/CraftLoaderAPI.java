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

/**
 * API calls for registering loaders as well as getting a mod's JSON context
 * 
 * @author FirEmerald
 *
 */
public class CraftLoaderAPI
{
	public static final String MOD_ID = "craftloader";
    public static final String API_VERSION = "1.0.0";
	public static final Logger LOGGER = LogManager.getLogger("CraftLoader API");
	
	/**
	 * Registers a loader
	 * 
	 * @param <T> the type of key used for the recipe 
	 * @param <U> the type of recipe output - for instance, SmeltingRecipe for the smelting loader.
	 * @param name the loader name.
	 * @param preLoader a function to be called immediately before loading recipes. use this to clear your recipe registry if needed, among other things.
	 * @param makeKey a function to construct a recipe key from a recipe definition's name and contents.
	 * @param loadRecipe a function to register a constructed recipe in the recipe registry.
	 * @param recipeDisabled a function to run on all disabled recipes - for instance, to prevent runtime-generated recipes from registering them if their key has been disabled.
	 * @param postLoader a function to run after loading all recipes - for instance, to generate dynamic recipes from ore dictionary definitions. a set of the disabled keys is provided for convenience.
	 */
	public static <T extends IRecipeKey, U> void registerLoader(ResourceLocation name, Runnable preLoader, BiFunction<ResourceLocation, JsonObject, T> makeKey, BiConsumer<T, U> loadRecipe, Consumer<T> recipeDisabled, Consumer<Set<T>> postLoader)
	{
		CraftingLoader.registerLoader(name, preLoader, makeKey, loadRecipe, recipeDisabled, postLoader);
	}

	/**
	 * Registers a loader
	 * 
	 * @param <T> the type of key used for the recipe 
	 * @param <U> the type of recipe output - for instance, SmeltingRecipe for the smelting loader.
	 * @param name the loader name.
	 * @param preLoader a function to be called immediately before loading recipes. use this to clear your recipe registry if needed, among other things.
	 * @param makeKey a function to construct a recipe key from a recipe definition's name and contents.
	 * @param loadRecipe a function to register a constructed recipe in the recipe registry.
	 * @param recipeDisabled a function to run on all disabled recipes - for instance, to prevent runtime-generated recipes from registering them if their key has been disabled.
	 */
	public static <T extends IRecipeKey, U> void registerLoader(ResourceLocation name, Runnable preLoader, BiFunction<ResourceLocation, JsonObject, T> makeKey, BiConsumer<T, U> loadRecipe, Consumer<T> recipeDisabled)
	{
		registerLoader(name, preLoader, makeKey, loadRecipe, recipeDisabled, disabled -> {});
	}

	/**
	 * Registers a loader
	 * 
	 * @param <T> the type of key used for the recipe 
	 * @param <U> the type of recipe output - for instance, SmeltingRecipe for the smelting loader.
	 * @param name the loader name.
	 * @param makeKey a function to construct a recipe key from a recipe definition's name and contents.
	 * @param loadRecipe a function to register a constructed recipe in the recipe registry.
	 * @param recipeDisabled a function to run on all disabled recipes - for instance, to prevent runtime-generated recipes from registering them if their key has been disabled.
	 * @param postLoader a function to run after loading all recipes - for instance, to generate dynamic recipes from ore dictionary definitions. a set of the disabled keys is provided for convenience.
	 */
	public static <T extends IRecipeKey, U> void registerLoader(ResourceLocation name, BiFunction<ResourceLocation, JsonObject, T> makeKey, BiConsumer<T, U> loadRecipe, Consumer<T> recipeDisabled, Consumer<Set<T>> postLoader)
	{
		registerLoader(name, () -> {}, makeKey, loadRecipe, recipeDisabled, postLoader);
	}

	/**
	 * Registers a loader
	 * 
	 * @param <T> the type of key used for the recipe 
	 * @param <U> the type of recipe output - for instance, SmeltingRecipe for the smelting loader.
	 * @param name the loader name.
	 * @param makeKey a function to construct a recipe key from a recipe definition's name and contents.
	 * @param loadRecipe a function to register a constructed recipe in the recipe registry.
	 * @param recipeDisabled a function to run on all disabled recipes - for instance, to prevent runtime-generated recipes from registering them if their key has been disabled.
	 */
	public static <T extends IRecipeKey, U> void registerLoader(ResourceLocation name, BiFunction<ResourceLocation, JsonObject, T> makeKey, BiConsumer<T, U> loadRecipe, Consumer<T> recipeDisabled)
	{
		registerLoader(name, () -> {}, makeKey, loadRecipe, recipeDisabled, disabled -> {});
	}

	/**
	 * Registers a loader
	 * 
	 * @param <T> the type of key used for the recipe 
	 * @param <U> the type of recipe output - for instance, SmeltingRecipe for the smelting loader.
	 * @param name the loader name.
	 * @param preLoader a function to be called immediately before loading recipes. use this to clear your recipe registry if needed, among other things.
	 * @param loadRecipe a function to register a constructed recipe in the recipe registry.
	 * @param recipeDisabled a function to run on all disabled recipes - for instance, to prevent runtime-generated recipes from registering them if their key has been disabled.
	 * @param postLoader a function to run after loading all recipes - for instance, to generate dynamic recipes from ore dictionary definitions. a set of the disabled keys is provided for convenience.
	 */
	public static <T> void registerLoader(ResourceLocation name, Runnable preLoader, BiConsumer<ResourceLocation, T> loadRecipe, Consumer<ResourceLocation> recipeDisabled, Consumer<Set<ResourceLocation>> postLoader)
	{
		registerLoader(name, () -> {}, (name2, obj) -> new RecipeKey(name2), (RecipeKey key, T recipe) -> loadRecipe.accept(key.name, recipe), disabled -> recipeDisabled.accept(disabled.name), disabled -> postLoader.accept(disabled.stream().map(key -> key.name).collect(Collectors.toSet())));
	}

	/**
	 * Registers a loader
	 * 
	 * @param <T> the type of key used for the recipe 
	 * @param <U> the type of recipe output - for instance, SmeltingRecipe for the smelting loader.
	 * @param name the loader name.
	 * @param preLoader a function to be called immediately before loading recipes. use this to clear your recipe registry if needed, among other things.
	 * @param loadRecipe a function to register a constructed recipe in the recipe registry.
	 * @param recipeDisabled a function to run on all disabled recipes - for instance, to prevent runtime-generated recipes from registering them if their key has been disabled.
	 */
	public static <T> void registerLoader(ResourceLocation name, Runnable preLoader, BiConsumer<ResourceLocation, T> loadRecipe, Consumer<ResourceLocation> recipeDisabled)
	{
		registerLoader(name, () -> {}, (name2, obj) -> new RecipeKey(name2), (RecipeKey key, T recipe) -> loadRecipe.accept(key.name, recipe), disabled -> recipeDisabled.accept(disabled.name), disabled -> {});
	}

	/**
	 * Registers a loader
	 * 
	 * @param <T> the type of key used for the recipe 
	 * @param <U> the type of recipe output - for instance, SmeltingRecipe for the smelting loader.
	 * @param name the loader name.
	 * @param loadRecipe a function to register a constructed recipe in the recipe registry.
	 * @param recipeDisabled a function to run on all disabled recipes - for instance, to prevent runtime-generated recipes from registering them if their key has been disabled.
	 * @param postLoader a function to run after loading all recipes - for instance, to generate dynamic recipes from ore dictionary definitions. a set of the disabled keys is provided for convenience.
	 */
	public static <T> void registerLoader(ResourceLocation name, BiConsumer<ResourceLocation, T> loadRecipe, Consumer<ResourceLocation> recipeDisabled, Consumer<Set<ResourceLocation>> postLoader)
	{
		registerLoader(name, () -> {}, (name2, obj) -> new RecipeKey(name2), (RecipeKey key, T recipe) -> loadRecipe.accept(key.name, recipe), disabled -> recipeDisabled.accept(disabled.name), disabled -> postLoader.accept(disabled.stream().map(key -> key.name).collect(Collectors.toSet())));
	}

	/**
	 * Registers a loader
	 * 
	 * @param <T> the type of key used for the recipe 
	 * @param <U> the type of recipe output - for instance, SmeltingRecipe for the smelting loader.
	 * @param name the loader name.
	 * @param loadRecipe a function to register a constructed recipe in the recipe registry.
	 * @param recipeDisabled a function to run on all disabled recipes - for instance, to prevent runtime-generated recipes from registering them if their key has been disabled.
	 */
	public static <T> void registerLoader(ResourceLocation name, BiConsumer<ResourceLocation, T> loadRecipe, Consumer<ResourceLocation> recipeDisabled)
	{
		registerLoader(name, () -> {}, (name2, obj) -> new RecipeKey(name2), (RecipeKey key, T recipe) -> loadRecipe.accept(key.name, recipe), disabled -> recipeDisabled.accept(disabled.name), disabled -> {});
	}

	/**
	 * Gets the JsonContext for a specific mod.
	 * 
	 * @param modId the mod's ID
	 * @return the mod's context. returns an empty one if it did not exist.
	 */
	public static JsonContext getContext(String modId)
	{
		return CraftingLoader.getContext(modId);
	}

    /**
     * Gets a JsonElement from a member of the JsonObject, or throws an error if none exists.
     * 
     * @param json the JsonObject
     * @param memberName the member name
     * @return the element
     */
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