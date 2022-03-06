package firemerald.craftloader;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class ElementTransformer
{
	public ElementTransformer parent;
	public final Map<String, JsonElement[]> toReplace = new HashMap<>();
	public int i = 0;
	public final int length;

	public ElementTransformer(int length)
	{
		this.length = length;
	}

	public ElementTransformer setParent(ElementTransformer parent)
	{
		this.parent = parent;
		return this;
	}

	public ElementTransformer addReplacements(String key, JsonElement... values)
	{
		if (values.length != this.length) throw new IllegalArgumentException("String transformer needed " + this.length + " replacement values, got " + values.length);
		toReplace.put(key, values);
		return this;
	}

	public int getTotalIterations()
	{
		return parent == null ? length : (length * parent.getTotalIterations());
	}

	public void reset()
	{
		i = 0;
		if (parent != null) parent.reset();
	}

	public boolean next() //returns true if iteration ended
	{
		if ((++i) >= length)
		{
			i = 0;
			return parent == null ? false : parent.next();
		}
		else return true;
	}

	public JsonObject transform(JsonObject element, String name)
	{
		return transform(element, null);
	}

	private JsonObject transform(JsonObject from)
	{
		JsonObject to = new JsonObject();
		transformObject(from, to);
		return to;
	}

	private void transformObject(JsonObject from, JsonObject to)
	{
		from.entrySet().forEach(entry -> {
			String name = entry.getKey();
			String newName = transform(name);
			JsonElement value = entry.getValue();
			JsonElement newValue = transformElement(value);
			to.add(newName, newValue);
		});
	}
	
	private JsonElement transformElement(JsonElement from)
	{
		if (from.isJsonObject())
		{
			JsonObject obj = from.getAsJsonObject(), newObj = new JsonObject();
			transformObject(obj, newObj);
			return newObj;
		}
		else if (from.isJsonArray())
		{
			JsonArray array = from.getAsJsonArray(), newArray = new JsonArray();
			for (int i = 0; i < array.size(); i++) newArray.add(transformElement(array.get(i)));
			return newArray;
		}
		else if (from.isJsonPrimitive())
		{
			JsonPrimitive primitive = from.getAsJsonPrimitive();
			if (primitive.isString())
			{
				String val = primitive.getAsString();
				if (val.charAt(0) == '{' && val.charAt(val.length() - 1) == '}')
				{
					JsonElement el = replace(primitive.getAsString().substring(1, val.length() - 1));
					if (el != null) return el;
				}
				return new JsonPrimitive(transform(primitive.getAsString()));
			}
			else return primitive;
		}
		else return from;
	}

	public String transform(String toTransform)
	{
		if (parent != null) toTransform = parent.transform(toTransform);
		for (Map.Entry<String, JsonElement[]> replacement : toReplace.entrySet()) toTransform = toTransform.replaceAll("\\{" + replacement.getKey() + "\\}", replacement.getValue()[i].getAsString());
		return toTransform;
	}
	
	private JsonElement replace(String toMatch)
	{
		JsonElement el = null;
		if (parent != null) el = parent.replace(toMatch);
		if (el == null)
		{
			JsonElement[] arr = toReplace.get(toMatch);
			if (arr != null) el = arr[i];
		}
		return el;
	}

	public void forEach(JsonObject base, Consumer<JsonObject> action)
	{
		reset();
		do
		{
			JsonObject transformed = transform(base);
			action.accept(transformed);
		}
		while (next());
	}

	public static void forEach(ElementTransformer transformer, JsonObject base, Consumer<JsonObject> action)
	{
		if (transformer != null) transformer.forEach(base, action);
		else action.accept(base);
	}
}