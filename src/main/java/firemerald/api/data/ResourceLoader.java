package firemerald.api.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.server.FMLServerHandler;

/** to facilitate the loading of resources that must also persist on servers.
 * follows the same rules as the Loot Table loader for in-world resources. **/
public class ResourceLoader
{
	public static MinecraftServer getServer()
	{
		return FMLCommonHandler.instance().getSide().isClient() ? getClientServer() : getServerServer();
	}

	@SideOnly(Side.CLIENT)
	public static MinecraftServer getClientServer()
	{
		return Minecraft.getMinecraft().getIntegratedServer();
	}

	@SideOnly(Side.SERVER)
	public static MinecraftServer getServerServer()
	{
		return FMLServerHandler.instance().getServer();
	}
	
	public static InputStream getResource(ResourceLocation resource, String subfolder) throws IOException
	{
		try //get from world
		{
			MinecraftServer server = getServer();
			if (server != null)
			{
				File file = new File(server.getEntityWorld().getSaveHandler().getWorldDirectory(), "data/" + subfolder + "/" + resource.getResourceDomain() + "/" + resource.getResourcePath());				return new FileInputStream(file);
			}
			else throw new IOException("MinecraftServer not found!");
		}
		catch (Throwable e)
		{
			if (FMLCommonHandler.instance().getSide().isClient()) //get from resource packs
			{
				InputStream in = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(resource.getResourceDomain(), subfolder + "/" + resource.getResourcePath())).getInputStream();
				if (in != null) return in;
				else throw (FileNotFoundException) new FileNotFoundException("Resource not found: " + resource).initCause(e);
			}
			else //get from classpath
			{
		        URL url = ResourceLoader.class.getResource("/assets/" + resource.getResourceDomain() + "/" + subfolder + "/" + resource.getResourcePath());
		        if (url != null) return url.openStream();
				else throw (FileNotFoundException) new FileNotFoundException("Resource not found: " + resource).initCause(e);
			}
		}
	}

	public static InputStream getResource(ResourceLocation resource) throws IOException
	{
		try //get from world
		{
			MinecraftServer server = getServer();
			if (server != null)
			{
				if (server.worlds.length == 0) throw new IOException("entity world not found!");
				File file = new File(server.getEntityWorld().getSaveHandler().getWorldDirectory(), "data/" + resource.getResourceDomain() + "/" + resource.getResourcePath());
				return new FileInputStream(file);
			}
			else throw new IOException("MinecraftServer not found!");
		}
		catch (Throwable e)
		{
			if (FMLCommonHandler.instance().getSide().isClient()) //get from resource packs
			{
				InputStream in = Minecraft.getMinecraft().getResourceManager().getResource(resource).getInputStream();
				if (in != null) return in;
				else throw (FileNotFoundException) new FileNotFoundException("Resource not found: " + resource).initCause(e);
			}
			else //get from classpath
			{
		        URL url = ResourceLoader.class.getResource("/assets/" + resource.getResourceDomain() + "/" + resource.getResourcePath());
		        if (url != null) return url.openStream();
				else throw (FileNotFoundException) new FileNotFoundException("Resource not found: " + resource).initCause(e);
			}
		}
	}

	public static InputStream getResourceWithoutWorld(ResourceLocation resource) throws IOException
	{
		if (FMLCommonHandler.instance().getSide().isClient()) //get from resource packs
		{
			InputStream in = Minecraft.getMinecraft().getResourceManager().getResource(resource).getInputStream();
			if (in != null) return in;
			else throw new FileNotFoundException("Resource not found: " + resource);
		}
		else //get from classpath
		{
	        URL url = ResourceLoader.class.getResource("/assets/" + resource.getResourceDomain() + "/" + resource.getResourcePath());
	        if (url != null) return url.openStream();
	        else throw new FileNotFoundException("Resource not found: " + resource);
		}
	}

	public static List<InputStream> getResources(ResourceLocation resource, String subfolder)
	{
		List<InputStream> list = new ArrayList<>();
		if (FMLCommonHandler.instance().getSide().isClient()) //get from resource packs
		{
			try
			{
				Minecraft.getMinecraft().getResourceManager().getAllResources(new ResourceLocation(resource.getResourceDomain(), subfolder + "/" + resource.getResourcePath())).forEach(iResource -> list.add(iResource.getInputStream()));
			}
			catch (Throwable e) {}
		}
		else //get from classpath
		{
	        URL url = ResourceLoader.class.getResource("/assets/" + resource.getResourceDomain() + "/" + subfolder + "/" + resource.getResourcePath());
	        if (url != null) try
	        {
	        	list.add(url.openStream());
	        }
	        catch (Throwable e) {}
		}
		try //get from world
		{
			MinecraftServer server = getServer();
			if (server != null) try
			{
				File file = new File(server.getEntityWorld().getSaveHandler().getWorldDirectory(), "data/" + subfolder + "/" + resource.getResourceDomain() + "/" + resource.getResourcePath());
				list.add(new FileInputStream(file));
			}
			catch (ArrayIndexOutOfBoundsException e) {} //no entity world
		}
		catch (Throwable e) {}
		return list;
	}

	public static List<InputStream> getResources(ResourceLocation resource)
	{
		List<InputStream> list = new ArrayList<>();
		if (FMLCommonHandler.instance().getSide().isClient()) //get from resource packs
		{
			try
			{
				Minecraft.getMinecraft().getResourceManager().getAllResources(resource).forEach(iResource -> list.add(iResource.getInputStream()));
			}
			catch (Throwable e) {}
		}
		else //get from classpath
		{
	        URL url = ResourceLoader.class.getResource("/assets/" + resource.getResourceDomain() + "/" + resource.getResourcePath());
	        if (url != null) try
	        {
	        	list.add(url.openStream());
	        }
	        catch (Throwable e) {}
		}
		try //get from world
		{
			MinecraftServer server = getServer();
			if (server != null)
			{
				File file = new File(server.getEntityWorld().getSaveHandler().getWorldDirectory(), "data/" + resource.getResourceDomain() + "/" + resource.getResourcePath());
				list.add(new FileInputStream(file));
			}
		}
		catch (Throwable e) {}
		return list;
	}

	public static List<InputStream> getResourcesWithoutWorld(ResourceLocation resource)
	{
		List<InputStream> list = new ArrayList<>();
		if (FMLCommonHandler.instance().getSide().isClient()) //get from resource packs
		{
			try
			{
				Minecraft.getMinecraft().getResourceManager().getAllResources(resource).forEach(iResource -> list.add(iResource.getInputStream()));
			}
			catch (Throwable e) {}
		}
		else //get from classpath
		{
	        URL url = ResourceLoader.class.getResource("/assets/" + resource.getResourceDomain() + "/" + resource.getResourcePath());
	        if (url != null) try
	        {
	        	list.add(url.openStream());
	        }
	        catch (Throwable e) {}
		}
		return list;
	}

	public static Map<String, List<InputStream>> getResources(String resourcePath, String subfolder)
	{
		final Map<String, List<InputStream>> map = new HashMap<>();
		FileUtil.getDomains().forEach(domain -> {
			List<InputStream> list = getResources(new ResourceLocation(domain, resourcePath), subfolder);
			if (list != null && !list.isEmpty()) map.put(domain, list);
		});
		return map;
	}

	public static Map<String, List<InputStream>> getResources(String resourcePath)
	{
		final Map<String, List<InputStream>> map = new HashMap<>();
		FileUtil.getDomains().forEach(domain -> {
			List<InputStream> list = getResources(new ResourceLocation(domain, resourcePath));
			if (list != null && !list.isEmpty()) map.put(domain, list);
		});
		return map;
	}

	public static Map<String, List<InputStream>> getResourcesWithoutWorld(String resourcePath)
	{
		final Map<String, List<InputStream>> map = new HashMap<>();
		FileUtil.getDomains().forEach(domain -> {
			List<InputStream> list = getResourcesWithoutWorld(new ResourceLocation(domain, resourcePath));
			if (list != null && !list.isEmpty()) map.put(domain, list);
		});
		return map;
	}

}