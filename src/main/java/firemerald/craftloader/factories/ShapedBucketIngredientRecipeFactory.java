package firemerald.craftloader.factories;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

public class ShapedBucketIngredientRecipeFactory extends ShapedIngredientRecipeFactory
{
	@Override
	public IRecipe bake(String group, int width, int height, NonNullList<Ingredient> ingredients, ItemStack result)
	{
        return new ShapedFluidRecipes(group, width, height, ingredients, result);
	}

	public static class ShapedFluidRecipes extends ShapedRecipes
	{
		public ShapedFluidRecipes(String group, int width, int height, NonNullList<Ingredient> ingredients, ItemStack result)
		{
			super(group, width, height, ingredients, result);
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