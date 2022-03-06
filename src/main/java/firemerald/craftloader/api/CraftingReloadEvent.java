package firemerald.craftloader.api;

import net.minecraftforge.fml.common.eventhandler.Event;

public class CraftingReloadEvent extends Event
{
	public static class Pre extends CraftingReloadEvent {}

	public static class Post extends CraftingReloadEvent {}
}