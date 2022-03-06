package firemerald.api.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import firemerald.api.data.Element.NumberedAttribute;
import firemerald.api.data.Element.NumberedElement;
import firemerald.api.data.attributes.*;

public abstract class AbstractElement
{
	public abstract String getName();

	public abstract String getValue();

	public abstract void setValue(String value);

	public abstract Map<String, IAttribute> getAttributes();

	public abstract IAttribute getAttribute(String name);

	public abstract void setAttribute(String name, IAttribute value);

	public abstract boolean hasAttribute(String name);

	public abstract List<? extends AbstractElement> getChildren();

	public abstract AbstractElement addChild(String name);

	public void setString(String attr, String value)
	{
		setAttribute(attr, new AttributeString(value));
	}

	public String getString(String attr) throws Exception
	{
		return getAttribute(attr).getString();
	}

	public String getString(String attr, String def)
	{
		try
		{
			return getString(attr);
		}
		catch (Exception e)
		{
			return def;
		}
	}

	public void setBoolean(String attr, boolean value)
	{
		setAttribute(attr, new AttributeBoolean(value));
	}

	public boolean getBoolean(String attr) throws Exception
	{
		return getAttribute(attr).getBoolean();
	}

	public boolean getBoolean(String attr, boolean def)
	{
		try
		{
			return getBoolean(attr);
		}
		catch (Exception e)
		{
			return def;
		}
	}

	public void setByte(String attr, byte value)
	{
		setAttribute(attr, new AttributeByte(value));
	}

	public byte getByte(String attr) throws Exception
	{
		return getAttribute(attr).getByte();
	}

	public byte getByte(String attr, byte min, byte max) throws Exception
	{
		byte val = getByte(attr);
		return val <= min ? min : val >= max ? max : val;
	}

	public byte getByte(String attr, byte def)
	{
		try
		{
			return getByte(attr);
		}
		catch (Exception e)
		{
			return def;
		}
	}

	public byte getByte(String attr, byte min, byte max, byte def)
	{
		try
		{
			return getByte(attr, min, max);
		}
		catch (Exception e)
		{
			return def;
		}
	}

	public void setShort(String attr, short value)
	{
		setAttribute(attr, new AttributeShort(value));
	}

	public short getShort(String attr) throws Exception
	{
		return getAttribute(attr).getShort();
	}

	public short getShort(String attr, short min, short max) throws Exception
	{
		short val = getShort(attr);
		return val <= min ? min : val >= max ? max : val;
	}

	public short getShort(String attr, short def)
	{
		try
		{
			return getShort(attr);
		}
		catch (Exception e)
		{
			return def;
		}
	}

	public short getShort(String attr, short min, short max, short def)
	{
		try
		{
			return getShort(attr, min, max);
		}
		catch (Exception e)
		{
			return def;
		}
	}

	public void setInt(String attr, int value)
	{
		setAttribute(attr, new AttributeInt(value));
	}

	public int getInt(String attr) throws Exception
	{
		return getAttribute(attr).getInt();
	}

	public int getInt(String attr, int min, int max) throws Exception
	{
		int val = getInt(attr);
		return val <= min ? min : val >= max ? max : val;
	}

	public int getInt(String attr, int def)
	{
		try
		{
			return getInt(attr);
		}
		catch (Exception e)
		{
			return def;
		}
	}

	public int getInt(String attr, int min, int max, int def)
	{
		try
		{
			return getInt(attr, min, max);
		}
		catch (Exception e)
		{
			return def;
		}
	}

	public void setLong(String attr, long value)
	{
		setAttribute(attr, new AttributeLong(value));
	}

	public long getLong(String attr) throws Exception
	{
		return getAttribute(attr).getLong();
	}

	public long getLong(String attr, long min, long max) throws Exception
	{
		long val = getLong(attr);
		return val <= min ? min : val >= max ? max : val;
	}

	public long getLong(String attr, long def)
	{
		try
		{
			return getLong(attr);
		}
		catch (Exception e)
		{
			return def;
		}
	}

	public long getLong(String attr, long min, long max, long def)
	{
		try
		{
			return getLong(attr, min, max);
		}
		catch (Exception e)
		{
			return def;
		}
	}

	public void setFloat(String attr, float value)
	{
		setAttribute(attr, new AttributeFloat(value));
	}

	public float getFloat(String attr) throws Exception
	{
		return getAttribute(attr).getFloat();
	}

	public float getFloat(String attr, float min, float max) throws Exception
	{
		float val = getFloat(attr);
		return val <= min ? min : val >= max ? max : val;
	}

	public float getFloat(String attr, float def)
	{
		try
		{
			return getFloat(attr);
		}
		catch (Exception e)
		{
			return def;
		}
	}

	public float getFloat(String attr, float min, float max, float def)
	{
		try
		{
			return getFloat(attr, min, max);
		}
		catch (Exception e)
		{
			return def;
		}
	}

	public void setDouble(String attr, double value)
	{
		setAttribute(attr, new AttributeDouble(value));
	}

	public double getDouble(String attr) throws Exception
	{
		return getAttribute(attr).getDouble();
	}

	public double getDouble(String attr, double min, double max) throws Exception
	{
		double val = getDouble(attr);
		return val <= min ? min : val >= max ? max : val;
	}

	public double getDouble(String attr, double def)
	{
		try
		{
			return getDouble(attr);
		}
		catch (Exception e)
		{
			return def;
		}
	}

	public double getDouble(String attr, double min, double max, double def)
	{
		try
		{
			return getDouble(attr, min, max);
		}
		catch (Exception e)
		{
			return def;
		}
	}

	public void setEnum(String attr, Enum<?> value)
	{
		setString(attr, value.name());
	}

	public <T extends Enum<?>> T getEnum(String attr, T[] values) throws Exception
	{
		return getAttribute(attr).getEnum(values);
	}

	public <T extends Enum<?>> T getEnum(String attr, T[] values, T def)
	{
		try
		{
			return getEnum(attr, values);
		}
		catch (Exception e)
		{
			return def;
		}
	}

	@Override
	public String toString()
	{
		return toString("");
	}

	public String toString(String prefix)
	{
		StringBuilder str = new StringBuilder(prefix);
		str.append("<");
		str.append(getName());
		getAttributes().forEach((name, value) -> {
			str.append(" ");
			str.append(name);
			str.append("=\"");
			str.append(value.getString());
			str.append("\"");
		});
		if (getValue() != null)
		{
			str.append(">");
			String val = getValue();
			String[] split = val.split("\n");
			String pre;
			if (split.length == 1) pre = "    ";
			else
			{
				pre = split[split.length - 1];
				char[] chr = new char[pre.length()];
				for (int i = 0; i < chr.length; i++) chr[i] = ' ';
				pre = String.valueOf(chr);
				val = val.substring(0, val.length() - chr.length);
			}
			str.append(val);
			for (AbstractElement el: getChildren())
			{
				str.append(el.toString(pre));
				str.append('\n');
			}
			str.append("</");
			str.append(getName());
			str.append(">");
		}
		else str.append("/>");
		return str.toString().replaceAll("\n", "\n" + prefix);
	}

	public void addToList(List<AbstractElement> list)
	{
		list.add(this);
	}

	public Element toElement()
	{
		Element el = new Element(this.getName());
		copyTo(el);
		return el;
	}

	protected Element toElement(Element parent)
	{
		Element el = parent.addChild(getName());
		copyTo(el);
		return el;
	}

	protected void copyTo(Element el)
	{
		el.setValue(getValue());
		this.getAttributes().forEach((name, value) -> el.setAttribute(name, value));
		this.getChildren().forEach(child -> child.toElement(el));
	}

	public JsonElement makeJSONElement(boolean needsName)
	{
		String value = this.getValue();
		Map<String, IAttribute> attributes = this.getAttributes();
		List<? extends AbstractElement> children = this.getChildren();
		if (attributes.isEmpty())
		{
			if (children.isEmpty())
			{
				if (value == null) return new JsonObject();
				else return new JsonPrimitive(value);
			}
			else
			{
				if (value == null || value.trim().length() == 0)
				{
					boolean flag = false;
					TreeSet<NumberedElement> s = new TreeSet<>();
					for (AbstractElement child : children)
					{
						try
						{
							NumberedElement el;
							String name = child.getName();
							if (name.startsWith("_") && name.length() > 1)
							{
								Integer i = Integer.parseInt(name.substring(1));
								if (i < 0 || s.contains(el = new NumberedElement(child, i)))
								{
									flag = true;
									break;
								}
								else s.add(el);
							}
							else
							{
								flag = true;
								break;
							}
						}
						catch (NumberFormatException e)
						{
							flag = true;
							break;
						}
					}
					if (!flag)
					{
						JsonArray array = new JsonArray();
						int targetNum = 0;
						for (NumberedElement el : s)
						{
							if (el.num == targetNum)
							{
								array.add(el.el.makeJSONElement(false));
								targetNum++;
							}
							else
							{
								flag = true;
								break;
							}
						}
						if (!flag) return array;
					}
				}
			}
		}
		else
		{
			if (children.isEmpty())
			{
				if (value == null)
				{
					boolean flag = false;
					TreeSet<NumberedAttribute> s = new TreeSet<>();
					for (Entry<String, IAttribute> entry : attributes.entrySet())
					{
						try
						{
							String key = entry.getKey();
							if (key.startsWith("_") && key.length() > 1)
							{
								NumberedAttribute el;
								Integer i = Integer.parseInt(entry.getKey().substring(1));
								if (i < 0 || s.contains(el = new NumberedAttribute(entry.getValue(), i)))
								{
									flag = true;
									break;
								}
								else s.add(el);
							}
							else
							{
								flag = true;
								break;
							}
						}
						catch (NumberFormatException e)
						{
							flag = true;
							break;
						}
					}
					if (!flag)
					{
						JsonArray array = new JsonArray();
						int targetNum = 0;
						for (NumberedAttribute attr : s)
						{
							if (attr.num == targetNum)
							{
								array.add(attr.attr.makeElement());
								targetNum++;
							}
							else
							{
								flag = true;
								break;
							}
						}
						if (!flag) return array;
					}
				}
			}
		}
		List<String> childNames = new ArrayList<>();
		JsonObject obj = new JsonObject();
		if (needsName) obj.addProperty("\"#name\"", getName());
		if (value != null && value.trim().length() > 0) obj.addProperty("\"#value\"", value);
		attributes.forEach((name, attr) -> {
			childNames.add(name);
			obj.add(name, attr.makeElement());
		});
		int i = 0;
		for (AbstractElement child : children)
		{
			String name = child.getName();
			if (childNames.contains(name))
			{
				String oldName = name;
				while (childNames.contains(name = (0 == i++) ? (oldName + " (duplicate)") : (oldName + " (duplicate " + i + ")"))) {}
				childNames.add(name);
				obj.add(name, child.makeJSONElement(true));
			}
			else
			{
				childNames.add(name);
				obj.add(name, child.makeJSONElement(false));
			}
		}
		return obj;
	}
}