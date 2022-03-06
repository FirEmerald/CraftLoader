package firemerald.api.core.plugin;

import net.minecraftforge.fml.common.versioning.DefaultArtifactVersion;
import net.minecraftforge.fml.common.versioning.VersionRange;

public class URLArtifactVersion extends DefaultArtifactVersion
{
	public final String url;
	public final String name;

    public URLArtifactVersion(String label, String url)
    {
    	this(label, label, url);
    }

    public URLArtifactVersion(String label, VersionRange range, String url)
    {
    	this(label, label, range, url);
    }

    public URLArtifactVersion(String label, boolean unbounded, String url)
    {
    	this(label, label, unbounded, url);
    }

    public URLArtifactVersion(String name, String label, String url)
    {
    	super(label);
    	this.name = name;
    	this.url = url;
    }

    public URLArtifactVersion(String name, String label, VersionRange range, String url)
    {
    	super(label, range);
    	this.name = name;
    	this.url = url;
    }

    public URLArtifactVersion(String name, String label, String version, String url)
    {
    	super(label, version);
    	this.name = name;
    	this.url = url;
    }

    public URLArtifactVersion(String name, String label, boolean unbounded, String url)
    {
    	super(label, unbounded);
    	this.name = name;
    	this.url = url;
    }
}