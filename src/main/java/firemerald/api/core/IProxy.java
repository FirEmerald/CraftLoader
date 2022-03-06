package firemerald.api.core;

import net.minecraftforge.fml.common.event.FMLConstructionEvent;

public interface IProxy extends IFMLEventHandler
{
    default public void onConstruction(FMLConstructionEvent event) {}
}