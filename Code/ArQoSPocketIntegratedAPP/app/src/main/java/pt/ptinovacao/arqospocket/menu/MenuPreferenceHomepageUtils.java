package pt.ptinovacao.arqospocket.menu;

import pt.ptinovacao.arqospocket.ArQosApplication;
import pt.ptinovacao.arqospocket.core.settings.SharedPreferencesManager;
import pt.ptinovacao.arqospocket.core.settings.UserSettings;
import pt.ptinovacao.arqospocket.views.anomaly.AnomalyActivity;
import pt.ptinovacao.arqospocket.views.anomalyhistoric.AnomalyHistoricActivity;
import pt.ptinovacao.arqospocket.views.dashboard.DashboardActivity;
import pt.ptinovacao.arqospocket.views.help.HelpActivity;
import pt.ptinovacao.arqospocket.views.settings.SettingsActivity;
import pt.ptinovacao.arqospocket.views.tests.TestsActivity;
import pt.ptinovacao.arqospocket.views.testshistoric.TestsHistoricActivity;

/**
 * MenuPreferenceHomepageUtils.
 * <p>
 * Created by pedro on 14/04/2017.
 */
public class MenuPreferenceHomepageUtils {

    public static Class getHomepage(ArQosApplication application) {

        UserSettings userSettings = SharedPreferencesManager.getInstance(application).readUserSettings();

        if (userSettings.getDashboard()) {
            return DashboardActivity.class;
        } else if (userSettings.getAnomaly()) {
            return AnomalyActivity.class;
        } else if (userSettings.getAnomalyHistoric()) {
            return AnomalyHistoricActivity.class;
        } else if (userSettings.getTests()) {
            return TestsActivity.class;
        } else if (userSettings.getTestsHistoric()) {
            return TestsHistoricActivity.class;
        } else if (userSettings.getSettings()) {
            return SettingsActivity.class;
        } else if (userSettings.getTheArqosPocket()) {
            return HelpActivity.class;
        } else {
            return DashboardActivity.class;
        }
    }
}
