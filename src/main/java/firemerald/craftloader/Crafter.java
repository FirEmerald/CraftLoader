package firemerald.craftloader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

import firemerald.api.data.FileUtil;
import firemerald.api.data.ResourceLoader;
import firemerald.craftloader.api.ICraftingFactory;
import firemerald.craftloader.api.IRecipeKey;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class Crafter<T extends IRecipeKey, U>
{
	public final Runnable preLoader;
	public final BiFunction<ResourceLocation, JsonObject, T> makeKey;
	public final BiConsumer<T, U> addRecipe;
	public final Consumer<T> onDisabled;
	public final Consumer<Set<T>> postLoader;
	public final Map<ResourceLocation, ICraftingFactory<T, U>> factories = new HashMap<>();

	public Crafter(Runnable preLoader, BiFunction<ResourceLocation, JsonObject, T> makeKey, BiConsumer<T, U> addRecipe, Consumer<T> onDisabled, Consumer<Set<T>> postLoader)
	{
		this.preLoader = preLoader;
		this.makeKey = makeKey;
		this.addRecipe = addRecipe;
		this.onDisabled = onDisabled;
		this.postLoader = postLoader;
	}

	@SuppressWarnings("unchecked")
	public void loadFactories(String name)
	{
		factories.clear();
    	ResourceLoader.getResources(name + "_factories.json", "crafting").forEach((domain, files) -> files.forEach(in -> {
    		try
    		{
				JsonElement el = FileUtil.loadTrueJSON(in);
				JsonUtils.getJsonObject(el, domain);
				if (el.isJsonObject())
				{
					JsonObject root = el.getAsJsonObject();
					root.entrySet().forEach(entry -> {
						if (entry.getValue().isJsonPrimitive())
						{
							JsonPrimitive primitive = entry.getValue().getAsJsonPrimitive();
							if (primitive.isString())
							{
								try
								{
									Class<?> clazz = Class.forName(primitive.getAsString());
									Object obj = clazz.newInstance();
									factories.put(new ResourceLocation(domain, entry.getKey()), (ICraftingFactory<T, U>) obj);
								}
								catch (ClassNotFoundException | InstantiationException | IllegalAccessException e)
								{
									Main.LOGGER.warn("Error parsing factory entry " + entry.getKey() + " from " + name + ".factories.json" + " in " + domain + ":crafting", e);
								}
							}
							else Main.LOGGER.warn("Error parsing factory entry " + entry.getKey() + " from " + name + ".factories.json" + " in " + domain + ":crafting : must be a collection of name-class pairs, got " + JsonUtils.toString(primitive));
						}
						else Main.LOGGER.warn("Error parsing factory entry " + entry.getKey() + " from " + name + ".factories.json" + " in " + domain + ":crafting : must be a collection of name-class pairs, got " + JsonUtils.toString(entry.getValue()));
					});
				}
				else throw new JsonSyntaxException("Expected " + name + ".factories.json" + " in " + domain + ":crafting to be a JsonObject, was " + JsonUtils.toString(el));
			}
    		catch (IOException e)
    		{
				e.printStackTrace();
			}
    	}));
	}
}
