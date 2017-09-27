package pt.ptinovacao.arqospocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Locale;

import pt.ptinovacao.arqospocket.core.CoreApplication;
import pt.ptinovacao.arqospocket.core.maintenance.DatabaseMaintenanceManager;
import pt.ptinovacao.arqospocket.core.maintenance.FileMaintenanceManager;
import pt.ptinovacao.arqospocket.core.settings.SharedPreferencesManager;
import pt.ptinovacao.arqospocket.core.utils.LanguageUtils;
import pt.ptinovacao.arqospocket.logging.ArQosLogger;

/**
 * The application class. Provides global initializations for the application.
 * <p>
 * Created by Emílio Simões on 06-04-2017.
 */
public class ArQosApplication extends CoreApplication {

    private final static Logger LOGGER = LoggerFactory.getLogger(ArQosApplication.class);

    static {
        if (BuildConfig.DEBUG) {
            ArQosLogger.setCurrentLogLevel(ArQosLogger.LogLevel.TRACE);
        } else {
            ArQosLogger.setCurrentLogLevel(ArQosLogger.LogLevel.WARN);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferencesManager manager = SharedPreferencesManager.getInstance(this);
        Locale locale = LanguageUtils.createLocale(manager.getPreferredLanguage());

        if (locale != null) {
            LanguageUtils.updateLanguage(this, locale);
        }

        long nextDateDatabase = manager.getNextDatabaseCleanupDate().getTime();
        long nowDatabase = Calendar.getInstance().getTimeInMillis();
        if (nextDateDatabase <= nowDatabase) {
            DatabaseMaintenanceManager.getInstance(this).cleanOldExecutingEntries();
        }

        long nextDateFile = manager.getNextFileCleanupDate().getTime();
        long nowFile = Calendar.getInstance().getTimeInMillis();
        if (nextDateFile <= nowFile) {
            FileMaintenanceManager.getInstance(this).cleanOldExecutingEntries();
        }
    }
}
