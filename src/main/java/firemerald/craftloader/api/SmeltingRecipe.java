package firemerald.craftloader.api;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

/**
 * Smelting recipe implementation, used by the smelting loader
 * 
 * @author FirEmerald
 *
 */
public class SmeltingRecipe
{
	/**
	 * The smelting input
	 */
	public final Ingredient input;
	/**
	 * The smelting output
	 */
	public final ItemStack output;
	/**
	 * The amount of experience given upon smelting the item
	 */
	public final float experience;
	
	public SmeltingRecipe(Ingredient input, ItemStack output, float experience)
	{
		this.input = input;
		this.output = output;
		this.experience = experience;
	}
}