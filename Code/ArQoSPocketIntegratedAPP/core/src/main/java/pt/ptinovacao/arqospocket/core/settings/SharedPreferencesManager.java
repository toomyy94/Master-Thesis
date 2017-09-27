package pt.ptinovacao.arqospocket.core.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import pt.ptinovacao.arqospocket.core.CoreApplication;
import pt.ptinovacao.arqospocket.core.utils.LanguageUtils;

/**
 * Shared Preferences Manager.
 * <p>
 * Created by pedro on 13/04/2017.
 */
public class SharedPreferencesManager {

    private final static Logger LOGGER = LoggerFactory.getLogger(SharedPreferencesManager.class);

    private static final String SHARED_PREFERENCE_NAME = "arqos-pocket-preferences";

    private static final int THREE_DAYS_TO_DATABASE = 259200;

    private static final int THREE_DAYS_TO_FILES = 259200;

    private static final int PERCENTAGE_MEMORY_OCCUPIED = 90;

    private static final int SCANLOGS_ENABLED = 0;

    private static final int RADIOLOGS_DEDICATED_MODE = 0;

    private static final int RADIOLOGS_IDLE_MODE = 0;

    private static final int RADIOLOGS_INTERVAL = 20;

    private static final int RADIOLOGS_MAXSIZE = 1;

    private static final int RADIOLOGS_MULTIEVENT = 0;

    private static final int RADIOLOGS_CELLRESELECTION = 0;

    private static final int MAX_TEMPERATURE = 60;

    private static SharedPreferencesManager instance;

    private final Map<SettingsKey, Object> cachedSettings;

    private final CoreApplication application;

    private SharedPreferencesManager(CoreApplication application) {
        this.application = application;
        cachedSettings = new HashMap<>();
    }

    public static synchronized SharedPreferencesManager getInstance(CoreApplication application) {
        if (instance == null) {
            instance = new SharedPreferencesManager(application);
        }
        return instance;
    }

    public void init() {
        SharedPreferences preferences = getSharedPreferences();
        for (SettingsKey key : SettingsKey.values()) {
            try {
                if (key.isBoolean()) {
                    cachedSettings.put(key, preferences.getBoolean(key.getKey(), false));
                } else if (key.isInteger()) {
                    cachedSettings.put(key, preferences.getInt(key.getKey(), -1));
                } else if (key.isLong()) {
                    cachedSettings.put(key, preferences.getLong(key.getKey(), -1L));
                } else if (key.isString()) {
                    cachedSettings.put(key, preferences.getString(key.getKey(), ""));
                }
            } catch (ClassCastException e) {
                LOGGER.debug("Invalid type mapping in setting for key {} [{}]", key, key.type);
                throw e;
            }
        }

        verifyKey(SettingsKey.LANGUAGE, "");
        verifyKey(SettingsKey.DATABASE_CLEANUP_INTERVAL, THREE_DAYS_TO_DATABASE);
        verifyKey(SettingsKey.DATABASE_NEXT_CLEANUP_DATE, 0L);

        verifyKey(SettingsKey.FILE_CLEANUP_INTERVAL, THREE_DAYS_TO_FILES);
        verifyKey(SettingsKey.FILE_NEXT_CLEANUP_DATE, 0L);
        verifyKey(SettingsKey.PERCENTAGE_MEMORY_OCCUPIED, PERCENTAGE_MEMORY_OCCUPIED);

        verifyKey(SettingsKey.SCANLOGS, SCANLOGS_ENABLED);
        verifyKey(SettingsKey.RADIOLOGS_DEDICATED, RADIOLOGS_DEDICATED_MODE);
        verifyKey(SettingsKey.RADIOLOGS_IDLE, RADIOLOGS_IDLE_MODE);
        verifyKey(SettingsKey.RADIOLOGS_INTERVAL, RADIOLOGS_INTERVAL);
        verifyKey(SettingsKey.RADIOLOG_CELLRESELECTION, RADIOLOGS_CELLRESELECTION);
        verifyKey(SettingsKey.RADIOLOG_MAXSIZE, RADIOLOGS_MAXSIZE);
        verifyKey(SettingsKey.RADIOLOG_MULTIEVENT, RADIOLOGS_MULTIEVENT);

        verifyKey(SettingsKey.MAX_TEMPERATURE, MAX_TEMPERATURE);

        persist();
    }

    public synchronized void saveUserSettings(UserSettings settings) {
        cachedSettings.put(SettingsKey.DASHBOARD, validate(settings.getDashboard()));
        cachedSettings.put(SettingsKey.ANOMALY, validate(settings.getAnomaly()));
        cachedSettings.put(SettingsKey.ANOMALY_HISTORY, validate(settings.getAnomalyHistoric()));
        cachedSettings.put(SettingsKey.TESTS, validate(settings.getTests()));
        cachedSettings.put(SettingsKey.TESTS_HISTORY, validate(settings.getTestsHistoric()));
        cachedSettings.put(SettingsKey.SETTINGS, validate(settings.getSettings()));
        cachedSettings.put(SettingsKey.THE_ARQOS_POCKET, validate(settings.getTheArqosPocket()));
        cachedSettings.put(SettingsKey.CONNECTION_WITH_SG, validate(settings.getManagementSystemType()));
        cachedSettings.put(SettingsKey.CONNECTION_WITH_SG_MANUAL, validate(settings.getManagementSystemInManual()));
        persist();
    }

    public synchronized UserSettings readUserSettings() {
        UserSettings settings = new UserSettings();

        settings.setDashboard(getBoolean(SettingsKey.DASHBOARD));
        settings.setAnomaly(getBoolean(SettingsKey.ANOMALY));
        settings.setAnomalyHistoric(getBoolean(SettingsKey.ANOMALY_HISTORY));
        settings.setTests(getBoolean(SettingsKey.TESTS));
        settings.setTestsHistoric(getBoolean(SettingsKey.TESTS_HISTORY));
        settings.setSettings(getBoolean(SettingsKey.SETTINGS));
        settings.setTheArqosPocket(getBoolean(SettingsKey.THE_ARQOS_POCKET));
        settings.setManagementSystemType(getBoolean(SettingsKey.CONNECTION_WITH_SG));
        settings.setManagementSystemInManual(getBoolean(SettingsKey.CONNECTION_WITH_SG_MANUAL));
        defineScreenDefault(settings);

        return settings;
    }

    /**
     * Gets the preferred UI language.
     *
     * @return the preferred UI language.
     */
    public String getPreferredLanguage() {
        if (cachedSettings.containsKey(SettingsKey.LANGUAGE)) {
            Object language = cachedSettings.get(SettingsKey.LANGUAGE);
            if (language != null && language instanceof String) {
                return (String) language;
            }
        }

        return LanguageUtils.formatLanguage(Locale.getDefault());
    }

    /**
     * Sets the preferred UI language.
     *
     * @param language the preferred UI language.
     */
    public void setPreferredLanguage(String language) {
        if (language == null) {
            if (cachedSettings.containsKey(SettingsKey.LANGUAGE)) {
                cachedSettings.remove(SettingsKey.LANGUAGE);
            }
        } else {
            cachedSettings.put(SettingsKey.LANGUAGE, language);
        }

        persist();
    }

    public int getDatabaseCleanupInterval() {
        if (cachedSettings.containsKey(SettingsKey.DATABASE_CLEANUP_INTERVAL)) {
            int value = getInteger(SettingsKey.DATABASE_CLEANUP_INTERVAL);
            return value <= 0 ? THREE_DAYS_TO_DATABASE : value;
        }

        return THREE_DAYS_TO_DATABASE;
    }

    public void setDatabaseCleanupInterval(int intervalInSeconds) {
        if (intervalInSeconds <= 0) {
            cachedSettings.put(SettingsKey.DATABASE_CLEANUP_INTERVAL, THREE_DAYS_TO_DATABASE);
        } else {
            cachedSettings.put(SettingsKey.DATABASE_CLEANUP_INTERVAL, intervalInSeconds);
        }
        persist();
    }

    public Date getNextDatabaseCleanupDate() {
        Calendar calendar = Calendar.getInstance();

        if (cachedSettings.containsKey(SettingsKey.DATABASE_NEXT_CLEANUP_DATE)) {
            long nextDate = getLong(SettingsKey.DATABASE_NEXT_CLEANUP_DATE);
            calendar.setTimeInMillis(nextDate);
        } else {
            calendar.setTimeInMillis(0L);
        }

        return calendar.getTime();
    }

    public void setNextDatabaseCleanupDate(Date date) {
        if (date == null) {
            cachedSettings.put(SettingsKey.DATABASE_NEXT_CLEANUP_DATE, 0L);
        } else {
            cachedSettings.put(SettingsKey.DATABASE_NEXT_CLEANUP_DATE, date.getTime());
        }
        persist();
    }

    public String getIpAddressManagement() {
        if (cachedSettings.containsKey(SettingsKey.IP_ADDRESS_MANAGEMENT_SYSTEM)) {
            Object ipAddressManagement = cachedSettings.get(SettingsKey.IP_ADDRESS_MANAGEMENT_SYSTEM);
            if (ipAddressManagement != null && ipAddressManagement instanceof String) {
                return (String) ipAddressManagement;
            }
        }
        return null;
    }

    public void setIpAddressManagement(String language) {
        if (language == null) {
            if (cachedSettings.containsKey(SettingsKey.IP_ADDRESS_MANAGEMENT_SYSTEM)) {
                cachedSettings.remove(SettingsKey.IP_ADDRESS_MANAGEMENT_SYSTEM);
            }
        } else {
            cachedSettings.put(SettingsKey.IP_ADDRESS_MANAGEMENT_SYSTEM, language);
        }

        persist();
    }

    public String getBaseDestinationSFTP() {
        if (cachedSettings.containsKey(SettingsKey.BASE_DESTINATION_SFTP)) {
            Object baseDestinationSFTP = cachedSettings.get(SettingsKey.BASE_DESTINATION_SFTP);
            if (baseDestinationSFTP != null && baseDestinationSFTP instanceof String) {
                return (String) baseDestinationSFTP;
            }
        }
        return null;
    }

    public void setBaseDestinationSFTP(String baseDestinationSFTP) {
        if (baseDestinationSFTP == null) {
            if (cachedSettings.containsKey(SettingsKey.BASE_DESTINATION_SFTP)) {
                cachedSettings.remove(SettingsKey.BASE_DESTINATION_SFTP);
            }
        } else {
            cachedSettings.put(SettingsKey.BASE_DESTINATION_SFTP, baseDestinationSFTP);
        }

        persist();
    }

    public String getUsernameSFTP() {
        if (cachedSettings.containsKey(SettingsKey.USERNAME_SFTP)) {
            Object username = cachedSettings.get(SettingsKey.USERNAME_SFTP);
            if (username != null && username instanceof String) {
                return (String) username;
            }
        }
        return null;
    }

    public void setAutomaticallyRunTests(Boolean automaticallyRunTests) {
        if (automaticallyRunTests == null) {
            if (cachedSettings.containsKey(SettingsKey.AUTOMATICALLY_RUN_TESTS)) {
                cachedSettings.remove(SettingsKey.AUTOMATICALLY_RUN_TESTS);
            }
        } else {
            cachedSettings.put(SettingsKey.AUTOMATICALLY_RUN_TESTS, !automaticallyRunTests);
        }

        persist();
    }

    public boolean getAutomaticallyRunTests() {
        if (cachedSettings.containsKey(SettingsKey.AUTOMATICALLY_RUN_TESTS)) {
            Object automaticallyRunTests = cachedSettings.get(SettingsKey.AUTOMATICALLY_RUN_TESTS);
            if (automaticallyRunTests != null && automaticallyRunTests instanceof Boolean) {
                return !(Boolean) automaticallyRunTests;
            }
        }
        return true;
    }

    public int getScanlogsEnable() {
        if (cachedSettings.containsKey(SettingsKey.SCANLOGS)) {
            int value = getInteger(SettingsKey.SCANLOGS);
            return value <= 0 ? SCANLOGS_ENABLED : value;
        }

        return SCANLOGS_ENABLED;
    }

    public void setScanlogsEnable(int scanlogsEnabled) {
        cachedSettings.put(SettingsKey.SCANLOGS, scanlogsEnabled);

        persist();
    }

    public int getRadiologsDedicatedMode() {
        if (cachedSettings.containsKey(SettingsKey.RADIOLOGS_DEDICATED)) {
            int value = getInteger(SettingsKey.RADIOLOGS_DEDICATED);
            return value <= 0 ? RADIOLOGS_DEDICATED_MODE : value;
        }

        return RADIOLOGS_DEDICATED_MODE;
    }

    public void setRadiologsDedicatedMode(int radiologsDedicatedMode) {
        cachedSettings.put(SettingsKey.RADIOLOGS_DEDICATED, radiologsDedicatedMode);

        persist();
    }

    public int getRadiologsIdleMode() {
        if (cachedSettings.containsKey(SettingsKey.RADIOLOGS_IDLE)) {
            int value = getInteger(SettingsKey.RADIOLOGS_IDLE);
            return value <= 0 ? RADIOLOGS_IDLE_MODE : value;
        }

        return RADIOLOGS_IDLE_MODE;
    }

    public void setRadiologsIdleMode(int radiologsIdleMode) {
        cachedSettings.put(SettingsKey.RADIOLOGS_IDLE, radiologsIdleMode);

        persist();
    }

    public int getRadiologPeriodicity() {
        if (cachedSettings.containsKey(SettingsKey.RADIOLOGS_INTERVAL)) {
            int value = getInteger(SettingsKey.RADIOLOGS_INTERVAL);
            return value <= 0 ? RADIOLOGS_INTERVAL : value;
        }

        return RADIOLOGS_INTERVAL;
    }

    public void setRadiologPeriodicity(int radiologsInterval) {
        if (radiologsInterval <= 5) {
            cachedSettings.put(SettingsKey.RADIOLOGS_INTERVAL, RADIOLOGS_INTERVAL);
        } else {
            cachedSettings.put(SettingsKey.RADIOLOGS_INTERVAL, radiologsInterval);
        }
        persist();
    }

    public int getRadiologsMaxsize() {
        if (cachedSettings.containsKey(SettingsKey.RADIOLOG_MAXSIZE)) {
            int value = getInteger(SettingsKey.RADIOLOG_MAXSIZE);
            return value <= 0 ? RADIOLOGS_MAXSIZE : value;
        }

        return RADIOLOGS_MAXSIZE;
    }

    public void setRadiologsMaxsize(int radiologsMaxsize) {
        if (radiologsMaxsize == 1) {
            cachedSettings.put(SettingsKey.RADIOLOG_MAXSIZE, RADIOLOGS_MAXSIZE);
        } else {
            cachedSettings.put(SettingsKey.RADIOLOG_MAXSIZE, radiologsMaxsize);
        }
        persist();
    }

    public int getRadiologsMultievent() {
        if (cachedSettings.containsKey(SettingsKey.RADIOLOG_MULTIEVENT)) {
            int value = getInteger(SettingsKey.RADIOLOG_MULTIEVENT);
            return value <= 0 ? RADIOLOGS_MULTIEVENT : value;
        }

        return RADIOLOGS_MULTIEVENT;
    }

    public void setRadiologsMultievent(int radiologsMultievent) {
        if (radiologsMultievent <= 0) {
            cachedSettings.put(SettingsKey.RADIOLOG_MULTIEVENT, RADIOLOGS_MULTIEVENT);
        } else {
            cachedSettings.put(SettingsKey.RADIOLOG_MULTIEVENT, radiologsMultievent);
        }
        persist();
    }

    public int getRadiologsCellreselection() {
        if (cachedSettings.containsKey(SettingsKey.RADIOLOG_CELLRESELECTION)) {
            int value = getInteger(SettingsKey.RADIOLOG_CELLRESELECTION);
            return value <= 0 ? RADIOLOGS_CELLRESELECTION : value;
        }

        return RADIOLOGS_CELLRESELECTION;
    }

    public void setRadiologsCellreselection(int radiologsCellreselection) {
        if (radiologsCellreselection <= 0) {
            cachedSettings.put(SettingsKey.RADIOLOG_CELLRESELECTION, RADIOLOGS_CELLRESELECTION);
        } else {
            cachedSettings.put(SettingsKey.RADIOLOG_CELLRESELECTION, radiologsCellreselection);
        }
        persist();
    }

    public int getMaxTemperature() {
        if (cachedSettings.containsKey(SettingsKey.MAX_TEMPERATURE)) {
            int value = getInteger(SettingsKey.MAX_TEMPERATURE);
            return value <= 0 ? MAX_TEMPERATURE : value;
        }

        return MAX_TEMPERATURE;
    }

    public void setMaxTemperature(int maxTemperature) {
        if (maxTemperature <= 0) {
            cachedSettings.put(SettingsKey.MAX_TEMPERATURE, MAX_TEMPERATURE);
        } else {
            cachedSettings.put(SettingsKey.MAX_TEMPERATURE, maxTemperature);
        }
        persist();
    }

    public void setUsernameSFTP(String username) {
        if (username == null) {
            if (cachedSettings.containsKey(SettingsKey.USERNAME_SFTP)) {
                cachedSettings.remove(SettingsKey.USERNAME_SFTP);
            }
        } else {
            cachedSettings.put(SettingsKey.USERNAME_SFTP, username);
        }

        persist();
    }

    public String getPasswordSFTP() {
        if (cachedSettings.containsKey(SettingsKey.PASSWORD_SFTP)) {
            Object password = cachedSettings.get(SettingsKey.PASSWORD_SFTP);
            if (password != null && password instanceof String) {
                return (String) password;
            }
        }
        return null;
    }

    public void setPasswordSFTP(String password) {
        if (password == null) {
            if (cachedSettings.containsKey(SettingsKey.PASSWORD_SFTP)) {
                cachedSettings.remove(SettingsKey.PASSWORD_SFTP);
            }
        } else {
            cachedSettings.put(SettingsKey.PASSWORD_SFTP, password);
        }

        persist();
    }

    public void resetIp() {

        ArrayList<String> listIps = new ArrayList<>();
        ArrayList<String> lastIp1 = getLastIp();
        if (lastIp1.size() > 0) {
            listIps.add(lastIp1.get(lastIp1.size() - 1));
        }
        cachedSettings.put(SettingsKey.HISTORIC_IPS, convertArrayListToString(listIps));

        persist();
    }

    public int getFileCleanupInterval() {
        if (cachedSettings.containsKey(SettingsKey.FILE_CLEANUP_INTERVAL)) {
            int value = getInteger(SettingsKey.FILE_CLEANUP_INTERVAL);
            return value <= 0 ? THREE_DAYS_TO_FILES : value;
        }

        return THREE_DAYS_TO_FILES;
    }

    public void setFileCleanupInterval(int fileCleanupInterval) {
        if (fileCleanupInterval <= 0) {
            cachedSettings.put(SettingsKey.FILE_CLEANUP_INTERVAL, THREE_DAYS_TO_FILES);
        } else {
            cachedSettings.put(SettingsKey.FILE_CLEANUP_INTERVAL, fileCleanupInterval);
        }
        persist();
    }

    public int getPercentageMemoryOccupied() {
        if (cachedSettings.containsKey(SettingsKey.PERCENTAGE_MEMORY_OCCUPIED)) {
            int value = getInteger(SettingsKey.PERCENTAGE_MEMORY_OCCUPIED);
            return value <= 0 ? PERCENTAGE_MEMORY_OCCUPIED : value;
        }

        return PERCENTAGE_MEMORY_OCCUPIED;
    }

    public void setPercentageMemoryOccupied(int percentageMemoryOccupied) {
        if (percentageMemoryOccupied <= 0) {
            cachedSettings.put(SettingsKey.PERCENTAGE_MEMORY_OCCUPIED, PERCENTAGE_MEMORY_OCCUPIED);
        } else {
            cachedSettings.put(SettingsKey.PERCENTAGE_MEMORY_OCCUPIED, percentageMemoryOccupied);
        }
        persist();
    }

    public Date getNextFileCleanupDate() {
        Calendar calendar = Calendar.getInstance();

        if (cachedSettings.containsKey(SettingsKey.FILE_NEXT_CLEANUP_DATE)) {
            long nextDate = getLong(SettingsKey.FILE_NEXT_CLEANUP_DATE);
            calendar.setTimeInMillis(nextDate);
        } else {
            calendar.setTimeInMillis(0L);
        }

        return calendar.getTime();
    }

    public void setNextFileCleanupDate(Date date) {
        if (date == null) {
            cachedSettings.put(SettingsKey.FILE_NEXT_CLEANUP_DATE, 0L);
        } else {
            cachedSettings.put(SettingsKey.FILE_NEXT_CLEANUP_DATE, date.getTime());
        }
        persist();
    }

    public Date getNextRadiologDate() {
        Calendar calendar = Calendar.getInstance();

        if (cachedSettings.containsKey(SettingsKey.RADIOLOGS_INTERVAL)) {
            long nextDate = getLong(SettingsKey.RADIOLOGS_INTERVAL);
            calendar.setTimeInMillis(nextDate);
        } else {
            calendar.setTimeInMillis(0L);
        }

        return calendar.getTime();
    }

    public void setNextRadiologDate(Date date) {
        if (date == null) {
            cachedSettings.put(SettingsKey.RADIOLOGS_INTERVAL, 0L);
        } else {
            cachedSettings.put(SettingsKey.RADIOLOGS_INTERVAL, date.getTime());
        }
        persist();
    }

    public ArrayList<String> getLastIp() {
        if (cachedSettings.containsKey(SettingsKey.HISTORIC_IPS)) {
            Object arrayIPs = cachedSettings.get(SettingsKey.HISTORIC_IPS);
            if (arrayIPs != null && arrayIPs instanceof String) {

                Iterable<String> results = Splitter.on('|').split((String) arrayIPs);

                if (!Strings.isNullOrEmpty((String) arrayIPs)) {
                    String[] toArray = Iterables.toArray(results, String.class);
                    return new ArrayList<>(Arrays.asList(toArray));
                }
            }
        }
        return new ArrayList<>();
    }

    public boolean getConnectionWithMSManual() {
        return getBoolean(SettingsKey.CONNECTION_WITH_SG_MANUAL);
    }

    public void setConnectionWithMSManual(boolean connectionWithMSManual) {
        cachedSettings.put(SettingsKey.CONNECTION_WITH_SG_MANUAL, connectionWithMSManual);
        persist();
    }

    public void addLastIp(String ipAddress) {

        if (ipAddress == null) {
            if (cachedSettings.containsKey(SettingsKey.HISTORIC_IPS)) {
                cachedSettings.remove(SettingsKey.HISTORIC_IPS);
            }
        } else {
            ArrayList<String> lastIp = new ArrayList<>(getLastIp());

            lastIp.remove(ipAddress);
            lastIp.add(ipAddress);

            LOGGER.debug("Historic IPs: " + lastIp);

            cachedSettings.put(SettingsKey.HISTORIC_IPS, convertArrayListToString(lastIp));
        }

        persist();
    }

    public static String convertArrayListToString(ArrayList<String> strings) {
        return Joiner.on('|').join(strings);
    }

    private void verifyKey(SettingsKey key, Object value) {
        if (!cachedSettings.containsKey(key)) {
            cachedSettings.put(key, value);
        }
    }

    private List<String> convertList(String value) {
        ArrayList<String> result = new ArrayList<>();

        if (StringUtils.isEmpty(value)) {
            return result;
        }

        Iterable<String> values = Splitter.on('|').omitEmptyStrings().trimResults().split(value);
        for (String language : values) {
            result.add(language);
        }

        return result;
    }

    /**
     * Gets the supported application languages.
     *
     * @return the supported application languages.
     */
    public String[] getSupportedLanguages() {
        List<String> languages = convertList("en-GB|fr-FR|pt-PT");
        if (languages == null || languages.isEmpty()) {
            return null;
        }
        return languages.toArray(new String[languages.size()]);

    }

    private void defineScreenDefault(UserSettings settings) {
        if (!settings.getDashboard() && !settings.getAnomaly() && !settings.getAnomalyHistoric() &&
                !settings.getTests() && !settings.getTestsHistoric() && !settings.getSettings() &&
                !settings.getTheArqosPocket()) {
            settings.setDashboard(true);
            saveUserSettings(settings);
        }
    }

    private boolean getBoolean(SettingsKey key) {
        if (!cachedSettings.containsKey(key)) {
            return false;
        }

        Object object = cachedSettings.get(key);
        if (object != null && object instanceof Boolean) {
            return (Boolean) object;
        }

        return false;
    }

    private int getInteger(SettingsKey key) {
        if (!cachedSettings.containsKey(key)) {
            return 0;
        }

        Object object = cachedSettings.get(key);
        if (object != null && object instanceof Integer) {
            return (Integer) object;
        }

        return 0;
    }

    private long getLong(SettingsKey key) {
        if (!cachedSettings.containsKey(key)) {
            return 0L;
        }

        Object object = cachedSettings.get(key);
        if (object != null && object instanceof Long) {
            return (Long) object;
        }

        return 0;
    }

    private void persist() {
        PersistTask task = new PersistTask();
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private boolean validate(Boolean value) {
        if (value == null) {
            return false;
        }

        return value;
    }

    private int validate(Integer value) {
        if (value == null) {
            return 0;
        }

        return value;
    }

    private SharedPreferences getSharedPreferences() {
        return application.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    private enum SettingType {
        BOOLEAN,
        INTEGER,
        LONG,
        STRING
    }

    private enum SettingsKey {

        //Homepage
        DASHBOARD("user_settings_dashboard", SettingType.BOOLEAN),
        ANOMALY("user_settings_anomaly", SettingType.BOOLEAN),
        ANOMALY_HISTORY("user_settings_anomaly_history", SettingType.BOOLEAN),
        TESTS("user_settings_tests", SettingType.BOOLEAN),
        TESTS_HISTORY("user_settings_tests_history", SettingType.BOOLEAN),
        SETTINGS("user_settings_settings", SettingType.BOOLEAN),
        THE_ARQOS_POCKET("user_settings_the_arqos_pocket", SettingType.BOOLEAN),

        //test arqos pocket
        AUTOMATICALLY_RUN_TESTS("user_settings_automatically_run_tests", SettingType.BOOLEAN),

        //radiologs
        SCANLOGS("user_settings_scanlogs", SettingType.INTEGER),
        RADIOLOGS_DEDICATED("user_settings_radiologs_dedicated", SettingType.INTEGER),
        RADIOLOGS_IDLE("user_settings_radiologs_idle", SettingType.INTEGER),
        RADIOLOGS_INTERVAL("user_settings_radiologs_interval", SettingType.INTEGER),
        RADIOLOG_MAXSIZE("user_settings_radiolog_maxsize", SettingType.INTEGER),
        RADIOLOG_MULTIEVENT("user_settings_radiolog_multievent", SettingType.INTEGER),
        RADIOLOG_CELLRESELECTION("user_settings_radiolog_cellreselection", SettingType.INTEGER),

        //language
        LANGUAGE("user_settings_language", SettingType.STRING),

        //ip pending
        HISTORIC_IPS("user_historic_ips", SettingType.STRING),

        //connection with management system
        CONNECTION_WITH_SG("user_connection_with_sg", SettingType.BOOLEAN),
        CONNECTION_WITH_SG_MANUAL("user_connection_with_sg_manual", SettingType.BOOLEAN),

        IP_ADDRESS_MANAGEMENT_SYSTEM("user_ip_address_m_s", SettingType.STRING),

        DATABASE_CLEANUP_INTERVAL("database_cleanup_interval", SettingType.INTEGER),
        DATABASE_NEXT_CLEANUP_DATE("database_next_cleanup_date", SettingType.LONG),

        FILE_CLEANUP_INTERVAL("file_cleanup_interval", SettingType.INTEGER),

        FILE_NEXT_CLEANUP_DATE("file_next_cleanup_date", SettingType.INTEGER),

        PERCENTAGE_MEMORY_OCCUPIED("percentage_memory_occupied", SettingType.LONG),

        MAX_TEMPERATURE("max_temperature", SettingType.INTEGER),

        BASE_DESTINATION_SFTP("base_destination_sftp", SettingType.STRING),

        USERNAME_SFTP("username_sftp", SettingType.STRING),

        PASSWORD_SFTP("password_sftp", SettingType.STRING);

        private final String key;

        private final SettingType type;

        SettingsKey(String key, SettingType type) {
            this.key = key;
            this.type = type;
        }

        String getKey() {
            return key;
        }

        boolean isBoolean() {
            return type == SettingType.BOOLEAN;
        }

        boolean isString() {
            return type == SettingType.STRING;
        }

        boolean isInteger() {
            return type == SettingType.INTEGER;
        }

        boolean isLong() {
            return type == SettingType.LONG;
        }
    }

    private class PersistTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            SharedPreferences preferences = getSharedPreferences();
            SharedPreferences.Editor editor = preferences.edit();
            for (SettingsKey key : SettingsKey.values()) {
                Object result = cachedSettings.get(key);
                if (result != null) {
                    if (key.isBoolean() && result instanceof Boolean) {
                        editor.putBoolean(key.getKey(), (Boolean) result);
                    } else if (key.isInteger() && result instanceof Integer) {
                        editor.putInt(key.getKey(), (Integer) result);
                    } else if (key.isLong() && result instanceof Long) {
                        editor.putLong(key.getKey(), (Long) result);
                    } else if (key.isString() && result instanceof String) {
                        editor.putString(key.getKey(), (String) result);
                    }
                } else {
                    editor.remove(key.getKey());
                }
            }

            editor.apply();
            return null;
        }

    }
}
