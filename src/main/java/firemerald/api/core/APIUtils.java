package firemerald.api.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class APIUtils
{
	@SideOnly(Side.CLIENT)
    public static URL getURLForResource(final ResourceLocation resource)
    {
        String s = String.format("resource:%s:%s", resource.getResourceDomain(), resource.getResourcePath());
        URLStreamHandler urlstreamhandler = new URLStreamHandler()
        {
            @Override
			protected URLConnection openConnection(URL p_openConnection_1_)
            {
                return new URLConnection(p_openConnection_1_)
                {
                    @Override
					public void connect() throws IOException {}
                    
                    @Override
					public InputStream getInputStream() throws IOException
                    {
                        return Minecraft.getMinecraft().getResourceManager().getResource(resource).getInputStream();
                    }
                };
            }
        };
        try
        {
            return new URL((URL)null, s, urlstreamhandler);
        }
        catch (MalformedURLException var4)
        {
            throw new Error("TODO: Sanely handle url exception! :D");
        }
    }
}