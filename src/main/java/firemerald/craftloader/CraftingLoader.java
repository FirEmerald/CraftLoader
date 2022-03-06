package firemerald.craftloader;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.Triple;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

import firemerald.api.data.FileUtil;
import firemerald.api.data.ResourceLoader;
import firemerald.craftloader.api.CraftingReloadEvent;
import firemerald.craftloader.api.ICraftingFactory;
import firemerald.craftloader.api.IRecipeKey;
import firemerald.craftloader.api.RecipeKey;
import firemerald.craftloader.api.SmeltingRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;

public class CraftingLoader
{
	private static final Map<String, ElementTransformer> TRANSFORMERS = new HashMap<>();
	private static final Map<String, JsonContext> MOD_CONTEXTS = new HashMap<>();
	private static final Map<String, Crafter<?, ?>> LOADERS = new LinkedHashMap<>();
	private static final Map<ResourceLocation, IRecipeFactory> CRAFTING_FACTORIES;
	private static final List<ItemStack> ADDED_SMELTING = new ArrayList<>();
	private static final List<Triple<ItemStack, ItemStack, Float>> CHANGED_SMELTING = new ArrayList<>();
	public static final List<ResourceLocation> recipes = new ArrayList<>();
		
	static
	{
		try
		{
			Field recipes = CraftingHelper.class.getDeclaredField("recipes");
			recipes.setAccessible(true);
			CRAFTING_FACTORIES = uncheckedCast(recipes.get(null));
		}
		catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e)
		{
			throw new IllegalStateException("Unable to get CraftingHelper.recipes field!", e);
		}
		registerLoader(new ResourceLocation("minecraft", "smelting"), () -> {
        	FurnaceRecipes furnaceRecipes = FurnaceRecipes.instance();
        	Map<ItemStack, ItemStack> recipes = furnaceRecipes.getSmeltingList();
			ADDED_SMELTING.forEach(recipes::remove); //remove added recipe
			CHANGED_SMELTING.forEach(triple -> {
				recipes.remove(triple.getLeft()); //remove old recipe
				furnaceRecipes.addSmeltingRecipe(triple.getLeft(), triple.getMiddle(), triple.getRight().floatValue()); //re-add original recipe
			});
			ADDED_SMELTING.clear();
			CHANGED_SMELTING.clear();
		}, (name, obj) -> new RecipeKey(name), (RecipeKey name, SmeltingRecipe recipe) -> {
        	FurnaceRecipes furnaceRecipes = FurnaceRecipes.instance();
        	Map<ItemStack, ItemStack> recipes = furnaceRecipes.getSmeltingList();
        	for (ItemStack input : recipe.input.getMatchingStacks())
        	{
            	if (recipes.containsKey(input)) CHANGED_SMELTING.add(Triple.of(input, recipes.get(input), furnaceRecipes.getSmeltingExperience(input)));
            	else ADDED_SMELTING.add(input);
            	furnaceRecipes.addSmeltingRecipe(input, recipe.output, recipe.experience);
        	}
		}, name -> {}, disabled -> {});
	}
	
	@SuppressWarnings("unchecked")
	private static <T> T uncheckedCast(Object value)
	{
		return (T) value;
	}
	
	public static <T extends IRecipeKey, U> void registerLoader(ResourceLocation id, Runnable preLoader, BiFunction<ResourceLocation, JsonObject, T> makeKey, BiConsumer<T, U> addRecipe, Consumer<T> recipeDisabled, Consumer<Set<T>> postLoader)
	{
		String name = id.getResourceDomain() + "/" + id.getResourcePath();
		if (LOADERS.containsKey(name)) throw new IllegalStateException("Attempted to register a crafting loader for a name that already has a crafting loader: " + name);
		LOADERS.put(name, new Crafter<T, U>(preLoader, makeKey, addRecipe, recipeDisabled, postLoader));
	}

	public static void putContext(JsonContext context)
	{
    	Main.LOGGER.debug("Adding mod recipe context for " + context.getModId());
		MOD_CONTEXTS.put(context.getModId(), context);
	}

	public static JsonContext getContext(String modId)
	{
		JsonContext context = MOD_CONTEXTS.get(modId);
		if (context == null) MOD_CONTEXTS.put(modId, context = new JsonContext(modId));
		return context;
	}

	public static void loadCraftingRecipes(ForgeRegistry<IRecipe> registry)
	{
        { //crafting table recipes
        	Main.logger().info("Loading crafting table recipes");
        	recipes.forEach(registry::remove); //remove all existing added recipes
        	recipes.clear();
    		parseRecipes("minecraft/crafting.json", 
    				(name, obj) -> new RecipeKey(name), 
    				(name) -> {
    					IRecipeFactory factory = CRAFTING_FACTORIES.get(name);
    					return factory == null ? null : (name2, context, object) -> factory.parse(context, object);
    				}, 
    				(key, recipe) -> {
    					try
    					{
    						registry.register(recipe.setRegistryName(key.name));
    						recipes.add(key.name);
    					}
    					catch (JsonParseException e)
    					{
    						Main.LOGGER.error("Parsing error loading recipe " + key, e);
    					}
    				}, id -> registry.remove(id.name));
        }
	}
	
	public static void loadTransformers()
	{
    	TRANSFORMERS.clear();
		Map<ResourceLocation, JsonObject> stringTransformers = new LinkedHashMap<>(); //reduce amount of parsing required
    	ResourceLoader.getResources("transformers.json", "crafting").forEach((domain, files) -> {
        	Main.logger().info("Loading transformers from a file in " + domain);
    		files.forEach(file -> {
    			try
    			{
    				JsonObject root = FileUtil.loadTrueJSON(file).getAsJsonObject();
    				root.entrySet().forEach(entry -> {
    					try
    					{
    						stringTransformers.put(new ResourceLocation(domain, entry.getKey()), entry.getValue().getAsJsonObject());
    					}
    					catch (JsonParseException e)
    					{
    						Main.LOGGER.warn("Failed to load transformer with name " + entry.getKey() + " under mod domain " + domain + " and data domain crafting from transformers.json", e);
    					}
    				});
    			}
				catch (IOException | JsonParseException e)
				{
					Main.LOGGER.warn("Failed to load transformers under mod domain " + domain + " and data domain crafting from transformers.json", e);
				}
    		});
    	});
    	stringTransformers.forEach((id, transformerObj) -> {
    		int count;
    		ElementTransformer transformer = new ElementTransformer(count = JsonUtils.getInt(transformerObj, "count", 1));
    		transformerObj.entrySet().forEach(entry -> {
    			if (!entry.getKey().equals("count"))
    			{
    				JsonArray array = entry.getValue().getAsJsonArray();
    				if (array.size() != count) throw new JsonSyntaxException("Invalide JSON array size: expected " + count + ", got " + array.size());
    				JsonElement[] vals = new JsonElement[count];
    				for (int i = 0; i < count; i++) vals[i] = array.get(i);
    				transformer.addReplacements(entry.getKey(), vals);
    			}
    		});
    		TRANSFORMERS.put(id.toString(), transformer);
    	});
	}

	public static <T extends IRecipeKey, U> void parseRecipes(String filePath, BiFunction<ResourceLocation, JsonObject, T> makeKey, Function<ResourceLocation, ICraftingFactory<T, U>> factories, BiConsumer<T, U> action, Consumer<T> onDisabled)
	{
		Map<T, JsonObject> recipeElements = new LinkedHashMap<>(); //reduce amount of parsing required
		final Set<T> disabled = new HashSet<>();
		ResourceLoader.getResources(filePath, "crafting").forEach((modId, files) -> {
			files.forEach(file -> {
				try
				{
					JsonElement json = FileUtil.loadTrueJSON(file, true);
					if (json.isJsonObject())
					{
						JsonObject root = json.getAsJsonObject();
						root.entrySet().forEach(entry -> {
							String name = entry.getKey();
							JsonElement value = entry.getValue();
							JsonObject[] recipes;
							if (value.isJsonObject()) recipes = new JsonObject[] {value.getAsJsonObject()};
							else if (value.isJsonArray())
							{
								JsonArray array = value.getAsJsonArray();
								recipes = new JsonObject[array.size()];
								for (int i = 0; i < array.size(); i++)
								{
									JsonElement el = array.get(i);
									if (el.isJsonObject()) recipes[i] = el.getAsJsonObject();
									else throw new JsonSyntaxException("Invalid recipe " + name + ", expected to find a JsonObject or JsonArray of JsonObject");
								}
							}
							else throw new JsonSyntaxException("Invalid recipe " + name + ", expected to find a JsonObject or JsonArray of JsonObject");
							for (JsonObject obj : recipes) try
							{
								int emptySize = 1;
								ElementTransformer transformer = null;
								if (obj.has("transformers"))
								{
									emptySize++;
									JsonArray arr = JsonUtils.getJsonArray(obj, "transformers");
									JsonContext context = getContext(modId);
									for (int i = 0; i < arr.size(); i++)
									{
										JsonElement el = arr.get(i);
										if (el.isJsonPrimitive())
										{
											JsonPrimitive prim = el.getAsJsonPrimitive();
											if (prim.isString())
											{
												String id = prim.getAsString();
												ElementTransformer trans = TRANSFORMERS.get(context.appendModId(id));
												if (trans != null) transformer = trans.setParent(transformer);
												else throw new JsonSyntaxException("Invalid recipe string transformer id " + id);
											}
											else throw new JsonSyntaxException("Invalid transformers, expected to find a JsonArray of String");
										}
										else throw new JsonSyntaxException("Invalid transformers, expected to find a JsonArray of String");
									}
								}
								final ElementTransformer transform = transformer;
								final int emptySizeF = emptySize;
								ElementTransformer.forEach(transformer, obj, object -> {
									String name2 = transform == null ? name : transform.transform(name);
									ResourceLocation id = new ResourceLocation(modId, name2);
									T key = makeKey.apply(id, object);
									if (object.has("disabled"))
									{
										if (JsonUtils.getBoolean(object.get("disabled"), "disabled")) disabled.add(key);
										else disabled.remove(key);
										if (object.entrySet().size() == emptySizeF) return;
									}
									recipeElements.put(key, object);
								});
							}
							catch (JsonParseException e)
							{
								Main.LOGGER.warn("Failed to load recipe " + name + " under mod domain " + modId + " and data domain crafting from " + filePath, e);
							}
						});
					}
				}
				catch (IOException | JsonParseException e)
				{
					Main.LOGGER.warn("Failed to load recipes under mod domain " + modId + " and data domain crafting from " + filePath, e);
				}
			});
		});
		recipeElements.forEach((id, obj) -> {
			if (disabled.contains(id)) onDisabled.accept(id);
			else try
			{
				JsonContext context = getContext(id.getModDomain());
				if (CraftingHelper.processConditions(obj, "conditions", context))
				{
			        String type = context.appendModId(JsonUtils.getString(obj, "type"));
			        if (type.isEmpty()) throw new JsonSyntaxException("Recipe type can not be an empty string");
			        ICraftingFactory<T, U> factory = factories.apply(new ResourceLocation(type));
			        if (factory == null) throw new JsonSyntaxException("Unknown recipe type: " + type);
			        action.accept(id, factory.parse(id, context, obj));
				}
				else Main.LOGGER.info("Skipping recipe " + id + " as required conditions were not met.");
			}
			catch (JsonParseException e)
			{
				Main.LOGGER.warn("Failed to load recipe with key " + id, e);
			}
		});
	}

	public static void loadRecipes()
	{
        ForgeRegistry<IRecipe> reg = (ForgeRegistry<IRecipe>)ForgeRegistries.RECIPES;
		loadTransformers();
		MinecraftForge.EVENT_BUS.post(new CraftingReloadEvent.Pre());
        reg.unfreeze();
        CraftingLoader.loadCraftingRecipes(reg);
		reg.freeze();
		LOADERS.forEach(CraftingLoader::loadRecipes);
		MinecraftForge.EVENT_BUS.post(new CraftingReloadEvent.Post());
	}
	
	private static <T extends IRecipeKey, U> void loadRecipes(String name, Crafter<T, U> loader)
	{
    	Main.logger().info("Loading " + name);
		loader.loadFactories(name);
		loader.preLoader.run();
		Set<T> disabled = new HashSet<>();
		parseRecipes(name + ".json", loader.makeKey, loader.factories::get, loader.addRecipe, ((Consumer<T>) disabled::add).andThen(loader.onDisabled));
		loader.postLoader.accept(disabled);
	}

}