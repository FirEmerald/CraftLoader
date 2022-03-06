package firemerald.api.core.client;

import net.minecraft.util.text.translation.I18n;

/** allows translation to local with fallback **/
@SuppressWarnings("deprecation")
public class Translator
{
	public static String translate(String key)
	{
		return I18n.canTranslate(key) ? I18n.translateToLocal(key) : I18n.translateToFallback(key);
	}

	public static String format(String key, Object... format)
	{
		return I18n.canTranslate(key) ? I18n.translateToLocalFormatted(key, format) : String.format(I18n.translateToFallback(key), format);
	}
}