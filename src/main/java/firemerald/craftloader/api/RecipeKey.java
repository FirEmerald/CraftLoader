package firemerald.craftloader.api;

import net.minecraft.util.ResourceLocation;

public class RecipeKey implements IRecipeKey
{
	public final ResourceLocation name;
	
	public RecipeKey(ResourceLocation name)
	{
		this.name = name;
	}

	@Override
	public String getModDomain()
	{
		return name.getResourceDomain();
	}
	
	@Override
	public String toString()
	{
		return name.toString();
	}
	
	@Override
	public int hashCode()
	{
		return name.hashCode();
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o == null) return false;
		if (o == this) return true;
		if (o.getClass() == this.getClass()) return name.equals(((RecipeKey) o).name);
		return false;
	}
}