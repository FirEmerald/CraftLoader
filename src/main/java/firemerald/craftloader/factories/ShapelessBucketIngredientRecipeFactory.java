package firemerald.craftloader.factories;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

public class ShapelessBucketIngredientRecipeFactory extends ShapelessIngredientRecipeFactory
{
	@Override
	public IRecipe bake(String group, ItemStack output, NonNullList<Ingredient> ingredients)
	{
		return new ShapelessFluidRecipes(group, output, ingredients);
	}

	public static class ShapelessFluidRecipes extends ShapelessRecipes
	{
		public ShapelessFluidRecipes(String group, ItemStack output, NonNullList<Ingredient> ingredients)
		{
			super(group, output, ingredients);
		}

		@Override
		public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
		{
			NonNullList<ItemStack> ret = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
			for (int i = 0; i < ret.size(); i++)
			{
				ItemStack stack = inv.getStackInSlot(i);
				ItemStack stack2;
				if (stack.getCount() != 1) (stack2 = stack.copy()).setCount(1);
				else stack2 = stack;
				IFluidHandlerItem handler = FluidUtil.getFluidHandler(stack2);
				if (handler == null)
				{
					ret.set(i, ForgeHooks.getContainerItem(stack));
				}
				else
				{
					handler.drain(Fluid.BUCKET_VOLUME, true);
					ret.set(i, handler.getContainer().copy());
				}
			}
			return ret;
		}

		@Override
		public boolean isDynamic()
		{
			return true;
		}
	}
}