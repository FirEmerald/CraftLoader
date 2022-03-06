package firemerald.craftloader.api;

import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * a set of events to fire before and after recipes have been loaded
 * 
 * @author FirEmerald
 *
 */
public class CraftingReloadEvent extends Event
{
	public static class Pre extends CraftingReloadEvent {}

	public static class Post extends CraftingReloadEvent {}
}