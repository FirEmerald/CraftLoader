package firemerald.api.core.plugin;

import org.objectweb.asm.Opcodes;

import net.minecraftforge.fml.relauncher.FMLLaunchHandler;

public interface ITransformer extends Opcodes
{
	public static final boolean IS_DEOBFUSCATED = FMLLaunchHandler.isDeobfuscatedEnvironment();

	public byte[] transform(byte[] bytes, String className);
}