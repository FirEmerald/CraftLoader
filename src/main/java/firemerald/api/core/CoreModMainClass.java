package firemerald.api.core;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.eventbus.Subscribe;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLModIdMappingEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms.IMCEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class CoreModMainClass<T extends IProxy> implements IFMLEventHandler
{
	public SimpleNetworkWrapper network;
	public final T proxy;
	private final ArrayList<IFMLEventHandler> eventHandlers = new ArrayList<>();

	public CoreModMainClass()
	{
		addFMLEventHandler(proxy = FMLCommonHandler.instance().getSide().isClient() ? makeClientProxy() : makeServerProxy());
	}
	
	@SideOnly(Side.CLIENT)
	protected abstract T makeClientProxy();
	
	@SideOnly(Side.SERVER)
	protected abstract T makeServerProxy();

	public void addFMLEventHandler(IFMLEventHandler handler)
	{
		eventHandlers.add(handler);
	}

	@Subscribe
    public void handleModStateEvent(FMLEvent event)
    {
    	if (event instanceof FMLConstructionEvent) proxy.onConstruction((FMLConstructionEvent) event);
    	else if (event instanceof FMLPreInitializationEvent) onPreInitialization((FMLPreInitializationEvent) event);
    	else if (event instanceof FMLInitializationEvent) onInitialization((FMLInitializationEvent) event);
    	else if (event instanceof IMCEvent) onIMC((IMCEvent) event);
    	else if (event instanceof FMLPostInitializationEvent) onPostInitialization((FMLPostInitializationEvent) event);
    	else if (event instanceof FMLLoadCompleteEvent) onLoadComplete((FMLLoadCompleteEvent) event);
    	else if (event instanceof FMLModIdMappingEvent) onModIdMapping((FMLModIdMappingEvent) event);
    	else if (event instanceof FMLServerAboutToStartEvent) onServerAboutToStart((FMLServerAboutToStartEvent) event);
    	else if (event instanceof FMLServerStartingEvent) onServerStarting((FMLServerStartingEvent) event);
    	else if (event instanceof FMLServerStartedEvent) onServerStarted((FMLServerStartedEvent) event);
    	else if (event instanceof FMLServerStoppingEvent) onServerStopping((FMLServerStoppingEvent) event);
    	else if (event instanceof FMLServerStoppedEvent) onServerStopped((FMLServerStoppedEvent) event);
    }
	
	@SuppressWarnings("unchecked")
	@Override
	public void onPreInitialization(FMLPreInitializationEvent event)
	{
		Field modObjectList = null;
		for (Field f : LoadController.class.getDeclaredFields()) if (f.getName().equals("modObjectList"))
		{
			modObjectList = f;
			break;
		}
		modObjectList.setAccessible(true);
		try
		{
			modObjectList.set(getLoadController(), appendModObjectList((BiMap<ModContainer, Object>) modObjectList.get(getLoadController())));
		}
		catch (IllegalArgumentException | IllegalAccessException e1)
		{
			throw new IllegalStateException(e1);
		}
		eventHandlers.forEach(handler -> handler.onPreInitialization(event));
	}

    public BiMap<ModContainer, Object> appendModObjectList(BiMap<ModContainer, Object> prev)
    {
        ImmutableBiMap.Builder<ModContainer, Object> builder = ImmutableBiMap.builder();
        builder.putAll(prev);
        builder.put(getModContainer(), this);
        return builder.build();
    }
    
    public abstract ModContainer getModContainer();
    
    public abstract LoadController getLoadController();

	@Override
	public void onInitialization(FMLInitializationEvent event)
	{
		eventHandlers.forEach(handler -> handler.onInitialization(event));
	}

	@Override
	public void onIMC(IMCEvent event)
	{
		eventHandlers.forEach(handler -> handler.onIMC(event));
	}

	@Override
	public void onPostInitialization(FMLPostInitializationEvent event)
	{
		eventHandlers.forEach(handler -> handler.onPostInitialization(event));
	}

	@Override
	public void onLoadComplete(FMLLoadCompleteEvent event)
	{
		eventHandlers.forEach(handler -> handler.onLoadComplete(event));
	}

	@Override
	public void onModIdMapping(FMLModIdMappingEvent event)
	{
		eventHandlers.forEach(handler -> handler.onModIdMapping(event));
	}

	@Override
	public void onServerAboutToStart(FMLServerAboutToStartEvent event)
	{
		eventHandlers.forEach(handler -> handler.onServerAboutToStart(event));
	}

	@Override
	public void onServerStarting(FMLServerStartingEvent event)
	{
		eventHandlers.forEach(handler -> handler.onServerStarting(event));
	}

	@Override
	public void onServerStarted(FMLServerStartedEvent event)
	{
		eventHandlers.forEach(handler -> handler.onServerStarted(event));
	}

	@Override
	public void onServerStopping(FMLServerStoppingEvent event)
	{
		eventHandlers.forEach(handler -> handler.onServerStopping(event));
	}

	@Override
	public void onServerStopped(FMLServerStoppedEvent event)
	{
		eventHandlers.forEach(handler -> handler.onServerStopped(event));
	}
}
