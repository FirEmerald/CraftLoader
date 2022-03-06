package firemerald.craftloader.api;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;

public class CraftingUtil
{
	public static final Field INGREDIENTS_FIELD;

	static
	{
		Field f = null;
		try
		{
			f = CraftingHelper.class.getDeclaredField("ingredients");
			f.setAccessible(true);
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException("Unable to retrieve CraftingHelper.ingredients field", e);
		}
		INGREDIENTS_FIELD = f;
	}

    @Nonnull
    public static ItemStack getResult(JsonElement json, JsonContext context)
    {
    	List<ItemStack> output = new ArrayList<>();
    	addResults(json, context, output);
        if (output.size() < 1) throw new JsonSyntaxException("Result is empty");
        if (output.size() > 1) CraftLoaderAPI.LOGGER.warn(new JsonSyntaxException("Result has multiple itemstack matches, only the first match will be used"));
        return output.get(0);
    }

    @SuppressWarnings("unchecked")
	@Nonnull
    public static void addResults(JsonElement json, JsonContext context, List<ItemStack> list)
    {
        if (json == null || json.isJsonNull()) throw new JsonSyntaxException("Json cannot be null");
        if (context == null) throw new IllegalArgumentException("addResults Context cannot be null");
        if (json.isJsonArray()) json.getAsJsonArray().forEach((ele) -> addResults(ele, context, list));
        if (!json.isJsonObject()) throw new JsonSyntaxException("Expcted result to be a object or array");
        JsonObject obj = (JsonObject) json;
        String type = context.appendModId(JsonUtils.getString(obj, "type", "minecraft:item"));
        if (type.isEmpty()) throw new JsonSyntaxException("Result type can not be an empty string");
        Ingredient ing = null;
        if (type.equals("minecraft:item"))
        {
            String item = JsonUtils.getString(obj, "item");
            if (item.startsWith("#"))
            {
                Ingredient constant = context.getConstant(item.substring(1));
                if (constant == null) throw new JsonSyntaxException("Result referenced invalid constant: " + item);
                ing = constant;
            }
        }
        if (ing == null)
        {
            IIngredientFactory factory = null;
    		try
    		{
    			factory = ((Map<ResourceLocation, IIngredientFactory>) INGREDIENTS_FIELD.get(null)).get(new ResourceLocation(type));
    		}
    		catch (IllegalArgumentException | IllegalAccessException e)
    		{
    			throw new JsonSyntaxException("Unable to retrieve CraftingHelper.ingredients value", e);
    		}
            if (factory == null) throw new JsonSyntaxException("Unknown result type: " + type);
            ing = factory.parse(context, obj);
        }
        int count = 1;
        JsonElement element = obj.get("count");
        if (element != null)
        {
        	if (!element.isJsonPrimitive()) throw new JsonSyntaxException("Result count must be a number");
			count = element.getAsInt();
			if (count < 1) throw new JsonSyntaxException("Invalid result count: " + element.getAsString());
        }
        ItemStack[] output = ing.getMatchingStacks();
        for (ItemStack out : output)
        {
            (out = output[0].copy()).setCount(count);
            list.add(out);
        }
    }

    public static Ingredient merge(Stream<Ingredient> input)
    {
    	List<ItemStack> stacks = new ArrayList<>();
    	input.forEach(ing -> {
    		for (ItemStack stack : ing.getMatchingStacks()) stacks.add(stack);
    	});
    	return Ingredient.fromStacks(stacks.toArray(new ItemStack[stacks.size()]));
    }

    @Nonnull
    public static SizedIngredient getSizedIngredient(JsonElement json, JsonContext context)
    {
    	List<SizedIngredient> output = new ArrayList<>();
    	addSizedIngredients(json, context, output);
    	if (output.isEmpty()) return new SizedIngredient(Ingredient.EMPTY, 1);
    	else if (output.size() == 1) return output.get(0);
    	else
    	{
    		int count = output.get(0).count;
    		List<Ingredient> ingredients = new ArrayList<>(output.size());
    		output.forEach(ingredient -> ingredients.add(ingredient.ingredient));
    		return new SizedIngredient(merge(ingredients.stream()), count);
    	}
    }

    @SuppressWarnings("unchecked")
	@Nonnull
    public static void addSizedIngredients(JsonElement json, JsonContext context, List<SizedIngredient> list)
    {
        if (json == null || json.isJsonNull()) throw new JsonSyntaxException("Json cannot be null");
        if (context == null) throw new IllegalArgumentException("addSizedIngredients Context cannot be null");
        if (json.isJsonArray())
        {
        	json.getAsJsonArray().forEach(ele -> addSizedIngredients(ele, context, list));
        	//list.forEach(ing -> System.out.println(ing.ingredient.getMatchingStacks()));
        	return;
        }
        if (!json.isJsonObject()) throw new JsonSyntaxException("Expcted result to be a object or array");
        JsonObject obj = json.getAsJsonObject();
        String type = context.appendModId(JsonUtils.getString(obj, "type", "minecraft:item"));
        if (type.isEmpty()) throw new JsonSyntaxException("Result type can not be an empty string");
        Ingredient ing = null;
        if (type.equals("minecraft:item"))
        {
            String item = JsonUtils.getString(obj, "item");
            if (item.startsWith("#"))
            {
                Ingredient constant = context.getConstant(item.substring(1));
                if (constant == null) throw new JsonSyntaxException("Result referenced invalid constant: " + item);
                ing = constant;
            }
        }
        if (ing == null)
        {
            IIngredientFactory factory = null;
    		try
    		{
    			factory = ((Map<ResourceLocation, IIngredientFactory>) INGREDIENTS_FIELD.get(null)).get(new ResourceLocation(type));
    		}
    		catch (IllegalArgumentException | IllegalAccessException e)
    		{
    			throw new JsonSyntaxException("Unable to retrieve CraftingHelper.ingredients value", e);
    		}
            if (factory == null) throw new JsonSyntaxException("Unknown result type: " + type);
            ing = factory.parse(context, obj);
        }
        int count = 1;
        JsonElement element = obj.get("count");
        if (element != null)
        {
        	if (!element.isJsonPrimitive()) throw new JsonSyntaxException("Result count must be a number");
			count = element.getAsInt();
			if (count < 1) throw new JsonSyntaxException("Invalid result count: " + element.getAsString());
        }
        if (!list.isEmpty() && list.get(0).count != count) throw new JsonSyntaxException("Mismatched result count: has" + list.get(0).count + ", new ingredient wants " + count);
        list.add(new SizedIngredient(ing, count));
    }

    @Nonnull
    public static Ingredient getIngredient(JsonElement json, JsonContext context)
    {
    	List<Ingredient> output = new ArrayList<>();
    	addIngredients(json, context, output);
    	if (output.isEmpty()) return Ingredient.EMPTY;
    	else if (output.size() == 1) return output.get(0);
    	else return merge(output.stream());
    }

    @SuppressWarnings("unchecked")
	@Nonnull
    public static void addIngredients(JsonElement json, JsonContext context, List<Ingredient> list)
    {
        if (json == null || json.isJsonNull()) throw new JsonSyntaxException("Json cannot be null");
        if (context == null) throw new IllegalArgumentException("addResults Context cannot be null");
        if (json.isJsonArray())
        {
        	json.getAsJsonArray().forEach((ele) -> addIngredients(ele, context, list));
        	return;
        }
        if (!json.isJsonObject()) throw new JsonSyntaxException("Expcted result to be a object or array");
        JsonObject obj = (JsonObject) json;
        String type = context.appendModId(JsonUtils.getString(obj, "type", "minecraft:item"));
        if (type.isEmpty()) throw new JsonSyntaxException("Result type can not be an empty string");
        Ingredient ing = null;
        if (type.equals("minecraft:item"))
        {
            String item = JsonUtils.getString(obj, "item");
            if (item.startsWith("#"))
            {
                Ingredient constant = context.getConstant(item.substring(1));
                if (constant == null) throw new JsonSyntaxException("Result referenced invalid constant: " + item);
                ing = constant;
            }
        }
        if (ing == null)
        {
            IIngredientFactory factory = null;
    		try
    		{
    			factory = ((Map<ResourceLocation, IIngredientFactory>) INGREDIENTS_FIELD.get(null)).get(new ResourceLocation(type));
    		}
    		catch (IllegalArgumentException | IllegalAccessException e)
    		{
    			throw new JsonSyntaxException("Unable to retrieve CraftingHelper.ingredients value", e);
    		}
            if (factory == null) throw new JsonSyntaxException("Unknown result type: " + type);
            ing = factory.parse(context, obj);
        }
        list.add(ing);
    }
}