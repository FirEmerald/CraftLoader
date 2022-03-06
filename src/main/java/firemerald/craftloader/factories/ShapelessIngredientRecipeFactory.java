package firemerald.craftloader.factories;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import firemerald.craftloader.api.CraftingUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;

public class ShapelessIngredientRecipeFactory implements IRecipeFactory
{
	@Override
	public IRecipe parse(JsonContext context, JsonObject json)
	{
        String group = JsonUtils.getString(json, "group", "");

        NonNullList<Ingredient> ings = NonNullList.create();
        for (JsonElement ele : JsonUtils.getJsonArray(json, "ingredients")) ings.add(CraftingHelper.getIngredient(ele, context));

        if (ings.isEmpty()) throw new JsonParseException("No ingredients for shapeless recipe");
        if (ings.size() > 9) throw new JsonParseException("Too many ingredients for shapeless recipe");

        ItemStack result = CraftingUtil.getResult(JsonUtils.getJsonObject(json, "result"), context);
        return bake(group, result, ings);
    }

	public IRecipe bake(String group, ItemStack output, NonNullList<Ingredient> ingredients)
	{
		return new ShapelessRecipes(group, output, ingredients);
	}
}