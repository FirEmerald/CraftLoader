package firemerald.api.core.plugin;

import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

public abstract class StandardTransformer implements ITransformer
{
	public final int readerFlags, writerFlags;

	public StandardTransformer(boolean computeFrames)
	{
		this(computeFrames, computeFrames, computeFrames);
	}

	public StandardTransformer(boolean computeFrames, boolean compute_maxs)
	{
		this(computeFrames, computeFrames, compute_maxs);
	}

	public StandardTransformer(boolean skipFrames, boolean computeFrames, boolean compute_maxs)
	{
		readerFlags = skipFrames ? ClassReader.SKIP_FRAMES : 0;
		writerFlags = computeFrames ? ClassWriter.COMPUTE_FRAMES | (compute_maxs ? ClassWriter.COMPUTE_MAXS : 0) : 0;
	}

	@Override
	public byte[] transform(byte[] bytes, String className)
	{
		if (bytes != null) try
		{
			logger().debug("Patching " + className);
			ClassNode classNode;
			transform(classNode = ASMUtil.getNode(bytes, readerFlags), className);
			bytes = ASMUtil.getClass(classNode, writerFlags);
			logger().debug(className + " patched.");
		}
		catch (Exception e)
		{
			logger().warn(className + " couldn't be patched.", e);
		}
		return bytes;
	}

	public abstract void transform(ClassNode classNode, String className);
	
	public abstract Logger logger();
}