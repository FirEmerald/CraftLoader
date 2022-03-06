package firemerald.craftloader.factories;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import firemerald.craftloader.api.CraftLoaderAPI;
import firemerald.craftloader.api.CraftingUtil;
import firemerald.craftloader.api.ICraftingFactory;
import firemerald.craftloader.api.RecipeKey;
import firemerald.craftloader.api.SmeltingRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.JsonContext;

public class SmeltingFactory implements ICraftingFactory<RecipeKey, SmeltingRecipe>
{
	@Override
	public SmeltingRecipe parse(RecipeKey name, JsonContext context, JsonObject obj)
	{
    	float exp = JsonUtils.getFloat(obj, "exp", 0);
    	if (exp < 0) throw new JsonSyntaxException("experience must be non-negative, got " + exp);
    	Ingredient input = CraftingUtil.getIngredient(CraftLoaderAPI.getJsonElement(obj, "ingredient"), context);
    	ItemStack output = CraftingUtil.getResult(JsonUtils.getJsonObject(obj, "result"), context);
    	return new SmeltingRecipe(input, output, exp);
	}
}
