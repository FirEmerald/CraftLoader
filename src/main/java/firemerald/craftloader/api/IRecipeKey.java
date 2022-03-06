package firemerald.craftloader.api;

/**
 * An interface that all recipe key types MUST implement!
 * 
 * @author FirEmerald
 *
 */
public interface IRecipeKey
{
	/**
	 * get the recipe owner's mod ID
	 * 
	 * @return the mod ID
	 */
	public String getModDomain();
}