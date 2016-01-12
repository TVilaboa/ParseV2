package views;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Class that takes care of internationalization and provides the Strings in the different languages.
 *
 * @author Nicolas Burroni
 * @since 1/31/14
 */
public class LanguageManager {

	private static Locale locale;
	private static transient ResourceBundle resourceBundle;

	/**
	 * Initializes the Locale and ResourceBoundle with the system's default language.
	 */
	static{
		locale = Locale.getDefault();
		resourceBundle = ResourceBundle.getBundle("TextBundle", locale);
	}

	/**
	 * Returns a String in the current language specified by a key.
	 * @param key Key to reference to the String.
	 * @return String in the current language.
	 */
	public static String getString(String key){
		return resourceBundle.getString(key);
	}

	/**
	 * Changes the language to the desired one.
	 * @param language Language to set.
	 * @param country Country to set.
	 */
	public static void changeLanguageTo(String language, String country){
		locale = new Locale(language, country);
		resourceBundle = ResourceBundle.getBundle("TextBundle", locale);
	}

	/**
	 *
	 * @return Current language.
	 */
	public static String getLanguage() {
		return resourceBundle.getLocale().getLanguage();
	}

	/**
	 *
	 * @return Current country.
	 */
	public static String getCountry() {
		return resourceBundle.getLocale().getCountry();
	}
}
