package pt.ptinovacao.arqospocket.core.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.Nullable;

import com.google.common.base.Strings;

import java.util.Locale;
import java.util.MissingResourceException;

/**
 * Utility classes to handle locales and language codes.
 *
 * Created by emilio on 24-09-2015.
 */
public class LanguageUtils {

    /**
     * Tries to create a locale from a string representation in the format ll-CC.
     *
     * @param language the language to get the locale.
     *
     * @return the create locale, or {@code null} if the language does not represent a valid locale.
     */
    @Nullable
    public static Locale createLocale(String language) {
        Locale locale;
        if (Strings.isNullOrEmpty(language)) {
            return null;
        }

        if (language.indexOf('-') > 0) {
            String[] values = language.split("-");
            locale = new Locale(values[0], values[1]);
        } else {
            locale = new Locale(language);
        }

        if (!isValid(locale)) {
            locale = null;
        }

        return locale;
    }

    /**
     * Formats a language in the ll-CC format.
     *
     * @param locale the locale with the language to format.
     *
     * @return the formatted language.
     */
    public static String formatLanguage(Locale locale) {
        if (locale == null) {
            return null;
        }

        if (Strings.isNullOrEmpty(locale.getCountry())) {
            return locale.getLanguage();
        }

        return locale.getLanguage() + "-" + locale.getCountry();
    }

    /**
     * Changes the application language to the selected one.
     *
     * @param context the current context.
     * @param locale the selected language.
     */
    public static void updateLanguage(Context context, Locale locale) {
        Locale.setDefault(locale);

        Resources resources = context.getApplicationContext().getResources();
        Configuration configuration = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        } else {
            //noinspection deprecation
            configuration.locale = locale;
        }

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    private static boolean isValid(Locale locale) {
        try {
            return locale.getISO3Language() != null && locale.getISO3Country() != null;
        } catch (MissingResourceException e) {
            return false;
        }
    }
}
