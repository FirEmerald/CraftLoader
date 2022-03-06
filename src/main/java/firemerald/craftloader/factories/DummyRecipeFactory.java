package firemerald.craftloader.factories;

import com.google.gson.JsonObject;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class DummyRecipeFactory implements IRecipeFactory
{
	@Override
	public IRecipe parse(JsonContext context, JsonObject json)
	{
		return new DummyRecipe();
    }

	public static class DummyRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe
	{
	    @Override
	    public boolean matches(final InventoryCrafting inv, final World worldIn)
	    {
	        return false;
	    }

	    @Override
	    public ItemStack getCraftingResult(final InventoryCrafting inv)
	    {
	        return ItemStack.EMPTY;
	    }

	    @Override
	    public boolean canFit(final int width, final int height)
	    {
	        return false;
	    }

	    @Override
	    public ItemStack getRecipeOutput()
	    {
	        return ItemStack.EMPTY;
	    }
	}
}