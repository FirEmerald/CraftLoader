package firemerald.craftloader.plugin.transformers;

import org.apache.logging.log4j.Logger;

import firemerald.api.core.plugin.StandardTransformer;
import firemerald.craftloader.plugin.Plugin;

public abstract class CLTransformer extends StandardTransformer
{
	public CLTransformer(boolean computeFrames)
	{
		super(computeFrames);
	}

	public CLTransformer(boolean computeFrames, boolean compute_maxs)
	{
		super(computeFrames, compute_maxs);
	}

	public CLTransformer(boolean skipFrames, boolean computeFrames, boolean compute_maxs)
	{
		super(computeFrames, computeFrames, compute_maxs);
	}

	@Override
	public Logger logger()
	{
		return Plugin.logger();
	}
}