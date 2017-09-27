package pt.ptinovacao.arqospocket.views.settings;

import java.util.Locale;

/**
 * Provides a callback method to pass the selected language to a listener.
 *
 * Created by emilio on 24-09-2015.
 */
public interface LanguageChangeListener {

    /**
     * Is raised when the language selection changes.
     *
     * @param language the selected language.
     */
    void onLanguageChanged(Locale language);
}
