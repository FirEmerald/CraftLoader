package firemerald.craftloader.api;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class SmeltingRecipe
{
	public final Ingredient input;
	public final ItemStack output;
	public final float experience;
	
	public SmeltingRecipe(Ingredient input, ItemStack output, float experience)
	{
		this.input = input;
		this.output = output;
		this.experience = experience;
	}
}