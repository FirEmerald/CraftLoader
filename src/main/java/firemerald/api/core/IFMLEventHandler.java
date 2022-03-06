package firemerald.api.core;

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

public interface IFMLEventHandler
{
    default public void onPreInitialization(FMLPreInitializationEvent event) {}

    default public void onInitialization(FMLInitializationEvent event) {}

    default public void onIMC(IMCEvent event) {}

    default public void onPostInitialization(FMLPostInitializationEvent event) {}

    default public void onLoadComplete(FMLLoadCompleteEvent event) {}

    default public void onModIdMapping(FMLModIdMappingEvent event) {}

    default public void onServerAboutToStart(FMLServerAboutToStartEvent event) {}

    default public void onServerStarting(FMLServerStartingEvent event) {}

    default public void onServerStarted(FMLServerStartedEvent event) {}

    default public void onServerStopping(FMLServerStoppingEvent event) {}

    default public void onServerStopped(FMLServerStoppedEvent event) {}
}