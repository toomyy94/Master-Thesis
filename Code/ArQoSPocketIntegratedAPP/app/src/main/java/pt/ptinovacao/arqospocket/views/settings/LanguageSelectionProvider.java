package pt.ptinovacao.arqospocket.views.settings;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.core.CoreApplication;
import pt.ptinovacao.arqospocket.core.settings.SharedPreferencesManager;
import pt.ptinovacao.arqospocket.core.utils.LanguageUtils;

/**
 * Utility class to wrap the language selection code.
 * <p>
 * Created by emilio on 24-09-2015.
 */
public class LanguageSelectionProvider implements DialogInterface.OnClickListener {

    private Context context;

    private LanguageChangeListener listener;

    private Locale[] options;

    private String[] messages;

    private SharedPreferencesManager manager;

    /**
     * Constructor for the language selection provider.
     *
     * @param context the current context.
     * @param listener the language selection listener.
     */
    public LanguageSelectionProvider(CoreApplication context, LanguageChangeListener listener) {
        this.context = context;
        this.listener = listener;

        manager = SharedPreferencesManager.getInstance(context);
        options = getLanguages(manager.getSupportedLanguages());

        if (hasLanguages()) {
            createOptions();
        }
    }

    /**
     * Displays the select language dialog that will allow the user to select an option an routes the answer to the
     * registered listener, if one is available.
     */
    public void selectLanguage(Activity context) {
        if (!hasLanguages()) {
            return;
        }

        String language = manager.getPreferredLanguage();
        Locale locale = LanguageUtils.createLocale(language);

        SpannableString str = new SpannableString(context.getString(R.string.language));
        str.setSpan(new ForegroundColorSpan(Color.WHITE), 0, context.getString(R.string.language).length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        int selected = findSelectedLanguage(locale);

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.App_Theme_Dialog);
        builder.setTitle(str).setAdapter(new LanguageListArrayAdapter(context, options, selected), this).show();

    }

    /**
     * Clears any references contained by the current instance.
     */
    public void reset() {
        context = null;
        listener = null;

        manager = null;
        options = null;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (listener == null) {
            return;
        }

        Locale locale = null;
        if (which >= 0) {
            locale = options[which];
        }

        manager.setPreferredLanguage(LanguageUtils.formatLanguage(locale));
        listener.onLanguageChanged(locale);
        dialog.dismiss();
    }

    private int findSelectedLanguage(Locale locale) {

        if (locale == null) {
            locale = Locale.getDefault();
        }

        for (int i = 0; i < options.length; i++) {
            if (options[i].equals(locale)) {
                return i;
            }
        }

        return -1;
    }

    private Locale[] getLanguages(String[] languages) {
        if (languages == null || languages.length == 0) {
            return new Locale[0];
        }

        List<Locale> locales = new ArrayList<>();

        for (String language : languages) {
            Locale locale = LanguageUtils.createLocale(language);

            if (locale != null) {
                locales.add(locale);
            }
        }

        return locales.toArray(new Locale[locales.size()]);
    }

    private void createOptions() {
        List<String> names = new ArrayList<>();
        for (Locale locale : options) {
            names.add(locale.getDisplayLanguage(locale));
        }
        messages = names.toArray(new String[names.size()]);
    }

    private boolean hasLanguages() {
        return options != null && options.length > 0;
    }

}
