package firemerald.api.core.plugin;

import java.util.HashMap;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.Side;

public abstract class TransformerBase implements IClassTransformer
{
	public final HashMap<String, ITransformer> transformers = new HashMap<>();
	public ITransformer globalTransformer = null;

	public TransformerBase()
	{
		addCommonTransformers();
		if (FMLLaunchHandler.side() == Side.CLIENT) addClientTransformers();
	}

	public abstract void addCommonTransformers();

	public abstract void addClientTransformers();

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes)
	{
		ITransformer transformer = transformers.get(transformedName);
		if (transformer != null) bytes = transformer.transform(bytes, transformedName);
		if (globalTransformer != null) bytes = globalTransformer.transform(bytes, transformedName);
		//saveClass(transformedName, bytes);
		return bytes;
	}
	/** /
	public static void saveClass(String name, byte[] bytes)
	{
		try
		{
			File file = new File("classes/" + name.replace('.', '/') + ".class");
			file.getParentFile().mkdirs();
			FileOutputStream out = new FileOutputStream(file);
			out.write(bytes);
			out.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	/**/
}