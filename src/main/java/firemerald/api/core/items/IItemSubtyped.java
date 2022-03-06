package firemerald.api.core.items;

public interface IItemSubtyped
{
	public String getSubtype(int meta);

	public int getMeta(String subtype);
}