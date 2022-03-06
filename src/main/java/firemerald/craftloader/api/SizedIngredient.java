package firemerald.craftloader.api;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

/**
 * Ingredient that has a specified item count.
 * 
 * @author FirEmerald
 *
 */
public class SizedIngredient
{
	/**
	 * The wrapped Ingredient
	 */
	public final Ingredient ingredient;
	/**
	 * The stack size
	 */
	public final int count;

	public SizedIngredient(Ingredient ingredient, int count)
	{
		this.ingredient = ingredient;
		this.count = count;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == this) return true;
		else if (o.getClass() == this.getClass())
		{
			SizedIngredient ingredient = (SizedIngredient) o;
			ItemStack[] thisStacks = this.ingredient.getMatchingStacks();
			ItemStack[] otherStacks = ingredient.ingredient.getMatchingStacks();
			if (thisStacks.length == otherStacks.length)
			{
				for (int i = 0; i < thisStacks.length; i++)
				{
					ItemStack thisStack = thisStacks[i];
					ItemStack otherStack = otherStacks[i];
					if (!ItemStack.areItemsEqual(thisStack, otherStack) || !ItemStack.areItemStackTagsEqual(thisStack, otherStack)) return false;
				}
				return true;
			}
			else return false;
		}
		else return false;
	}
}