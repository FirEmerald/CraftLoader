package firemerald.craftloader.plugin;

import java.io.File;
import java.util.ArrayList;

import com.google.common.eventbus.EventBus;

import firemerald.craftloader.Main;
import net.minecraftforge.fml.client.FMLFileResourcePack;
import net.minecraftforge.fml.client.FMLFolderResourcePack;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;

public class Core extends DummyModContainer
{
	public static LoadController loadController;
	public static final ModMetadata METADATA = new ModMetadata();
	static
	{
		METADATA.authorList = new ArrayList<>();
    	METADATA.authorList.add("FirEmerald");
    	METADATA.credits = "FirEmerald";
    	METADATA.description = "Crafting recipe loader framework, for mods to add in their own recipe loaders. Also includes it's own crafting table recipe loader and furnace smelting loader.";
    	METADATA.modId = Plugin.MOD_ID;
    	METADATA.name = "CraftLoader";
    	METADATA.version = Plugin.MOD_VERSION;
    	METADATA.logoFile = "assets/craftloader/textures/logo.png";
	}
	private static Core instance;

	public static Core getInstance()
	{
		return instance;
	}

    public Core()
    {
        super(METADATA);
        instance = this;
    }

    @Override
    public File getSource()
    {
        return Plugin.instance().getLocation();
    }

    @Override
    public Class<?> getCustomResourcePackClass()
    {
        return getSource().isDirectory() ? FMLFolderResourcePack.class : FMLFileResourcePack.class;
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller)
    {
    	loadController = controller;
    	bus.register(new Main());
        return true;
    }

    @Override
    public Object getMod()
    {
        return Main.instance();
    }
}