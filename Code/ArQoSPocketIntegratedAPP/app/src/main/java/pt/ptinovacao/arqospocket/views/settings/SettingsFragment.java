package pt.ptinovacao.arqospocket.views.settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.SeekBar;

import com.google.common.base.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

import pt.ptinovacao.arqospocket.ArQoSBaseFragment;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.boot.BootAlarmRestoreService;
import pt.ptinovacao.arqospocket.core.TestAlarmManager;
import pt.ptinovacao.arqospocket.core.keepalive.KeepAliveManager;
import pt.ptinovacao.arqospocket.core.notify.TaskProgress;
import pt.ptinovacao.arqospocket.core.notify.TestNotificationManager;
import pt.ptinovacao.arqospocket.core.notify.TestProgress;
import pt.ptinovacao.arqospocket.core.notify.TestProgressNotifier;
import pt.ptinovacao.arqospocket.core.radiologs.RadiologsManager;
import pt.ptinovacao.arqospocket.core.settings.SharedPreferencesManager;
import pt.ptinovacao.arqospocket.core.settings.UserSettings;
import pt.ptinovacao.arqospocket.core.ssh.AttachmentsProcessManager;
import pt.ptinovacao.arqospocket.core.ssh.RadiologsAttachmentsProcessManager;
import pt.ptinovacao.arqospocket.core.ssh.ScanlogsAttachmentsProcessManager;
import pt.ptinovacao.arqospocket.core.utils.LanguageUtils;
import pt.ptinovacao.arqospocket.persistence.ExecutingEventDao;
import pt.ptinovacao.arqospocket.persistence.models.ScheduledEvent;
import pt.ptinovacao.arqospocket.views.ArQosTextView;

import static android.view.View.VISIBLE;

/**
 * SettingsFragment.
 * <p>
 * Created by pedro on 12/04/2017.
 */
public class SettingsFragment extends ArQoSBaseFragment implements LanguageChangeListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(SettingsFragment.class);

    private RadioButton rbDashboard;

    private RadioButton rbAnomaly;

    private RadioButton rbAnomalyHistoric;

    private RadioButton rbTests;

    private RadioButton rbTestsHistoric;

    private RadioButton rbSettings;

    private RadioButton rbTheArqosPoocket;

    private RadioButton rbTypeMG;

    private RadioButton rbTypeManualMG;

    private CheckBox chRadiologDedicated;

    private CheckBox chRadiologIdle;

    private CheckBox chScanlogs;

    private LanguageSelectionProvider languageSelectionProvider;

    private ArQosTextView tvLanguage;

    private ArQoSSwitch sgSwitch;

    private ArQoSSwitch radiologSwitch;

    private ArQoSProgressBar radiologProgressBar;

    private Integer radiologPeriodicity;

    private ArQoSeek arQoSeek;

    private ArQoSSwitch autoByDedug;

    private BroadcastReceiver broadcastReceiverStateKeepAlive = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switchStateConnectionMS();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_setting, container, false);

        initializeViews(rootView);
        addListeners();
        writeValues();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        languageSelectionProvider = new LanguageSelectionProvider(getArQosApplication(), this);

        IntentFilter filter = new IntentFilter(KeepAliveManager.BROADCAST_RECEIVER_KEEP_ALIVE_RECEIVER);
        getActivity().registerReceiver(broadcastReceiverStateKeepAlive, filter);

        TestNotificationManager.register(getContext(), testProgressNotifier);

        switchStateConnectionMS();
    }

    @Override
    public void onLanguageChanged(Locale language) {
        updateLanguage(language, true);
    }

    @Override
    public void onPause() {
        super.onPause();
        languageSelectionProvider.reset();
        languageSelectionProvider = null;

        getActivity().unregisterReceiver(broadcastReceiverStateKeepAlive);

        TestNotificationManager.unregister(getContext(), testProgressNotifier);
    }

    private class ChangeLanguageClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (languageSelectionProvider != null) {
                languageSelectionProvider.selectLanguage(getActivity());
            }
        }
    }

    public void initializeViews(ViewGroup rootView) {
        rbDashboard = (RadioButton) rootView.findViewById(R.id.radio_dashboard);
        rbAnomaly = (RadioButton) rootView.findViewById(R.id.radio_anomalias);
        rbAnomalyHistoric = (RadioButton) rootView.findViewById(R.id.radio_historico_anomalias);
        rbTests = (RadioButton) rootView.findViewById(R.id.radio_testes);
        rbTestsHistoric = (RadioButton) rootView.findViewById(R.id.radio_historico_testes);
        rbSettings = (RadioButton) rootView.findViewById(R.id.radio_configuracoes);
        rbTheArqosPoocket = (RadioButton) rootView.findViewById(R.id.radio_info);

        tvLanguage = (ArQosTextView) rootView.findViewById(R.id.tvLanguage);

        rbTypeMG = (RadioButton) rootView.findViewById(R.id.radio_date_automatic_sg);
        rbTypeManualMG = (RadioButton) rootView.findViewById(R.id.radio_date_manual_sg);

        sgSwitch = (ArQoSSwitch) rootView.findViewById(R.id.switch_mode_manual);

        radiologSwitch = (ArQoSSwitch) rootView.findViewById(R.id.switch_radiologs_enable);

        chRadiologDedicated = (CheckBox) rootView.findViewById(R.id.radio_radiolog_dedicated);
        chRadiologIdle = (CheckBox) rootView.findViewById(R.id.radio_radiolog_idle);
        chScanlogs = (CheckBox) rootView.findViewById(R.id.radio_scanlog);

        radiologProgressBar = (ArQoSProgressBar) rootView.findViewById(R.id.radiologs_progressbar);

        arQoSeek = (ArQoSeek) rootView.findViewById(R.id.seek_bar_percentage);

        autoByDedug = (ArQoSSwitch) rootView.findViewById(R.id.switch_auto_tests);
    }

    public void addListeners() {

        SettingsOnCheckedChangeListener settingsOnCheckedChangeListener = new SettingsOnCheckedChangeListener();
        SGSwitchChangeListener sgSwitchChangeListener = new SGSwitchChangeListener();
        SGSwitchChangeAutoRunTests sgSwitchChangeAutoRunTests = new SGSwitchChangeAutoRunTests();
        RadiologSwitchChangeListener radiologSwitchChangeListener = new RadiologSwitchChangeListener();
        RadiologsModeChangeListener radiologsModeOnCheckedChangeListener = new RadiologsModeChangeListener();

        rbDashboard.setOnCheckedChangeListener(settingsOnCheckedChangeListener);
        rbAnomaly.setOnCheckedChangeListener(settingsOnCheckedChangeListener);
        rbAnomalyHistoric.setOnCheckedChangeListener(settingsOnCheckedChangeListener);
        rbTests.setOnCheckedChangeListener(settingsOnCheckedChangeListener);
        rbTestsHistoric.setOnCheckedChangeListener(settingsOnCheckedChangeListener);
        rbSettings.setOnCheckedChangeListener(settingsOnCheckedChangeListener);
        rbTheArqosPoocket.setOnCheckedChangeListener(settingsOnCheckedChangeListener);

        chRadiologDedicated.setOnCheckedChangeListener(radiologsModeOnCheckedChangeListener);
        chRadiologIdle.setOnCheckedChangeListener(radiologsModeOnCheckedChangeListener);
        chScanlogs.setOnCheckedChangeListener(radiologsModeOnCheckedChangeListener);

        tvLanguage.setOnClickListener(new ChangeLanguageClickListener());

        rbTypeMG.setOnCheckedChangeListener(settingsOnCheckedChangeListener);
        rbTypeManualMG.setOnCheckedChangeListener(settingsOnCheckedChangeListener);

        sgSwitch.getSwitch().setOnCheckedChangeListener(sgSwitchChangeListener);

        radiologSwitch.getSwitch().setOnCheckedChangeListener(radiologSwitchChangeListener);

        autoByDedug.getSwitch().setOnCheckedChangeListener(sgSwitchChangeAutoRunTests);

        arQoSeek.getSeekBar().setOnSeekBarChangeListener(new ArqosOnSeekBarChangeListener());
    }

    private class ArqosOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            arQoSeek.setLabel(getContext().getString(R.string.settings_max) + " - " + progress + "%");
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            SharedPreferencesManager sharedPreferencesManager =
                    SharedPreferencesManager.getInstance(getArQosApplication());
            sharedPreferencesManager.setPercentageMemoryOccupied(seekBar.getProgress());
        }
    }

    public void writeValues() {
        autoByDedug.getIcon().setVisibility(View.GONE);
        autoByDedug.setLabel(R.string.auto_run_date_tests);

        SharedPreferencesManager sharedPreferencesManager = SharedPreferencesManager.getInstance(getArQosApplication());
        UserSettings userSettings = sharedPreferencesManager.readUserSettings();

        rbDashboard.setChecked(userSettings.getDashboard());
        rbAnomaly.setChecked(userSettings.getAnomaly());
        rbAnomalyHistoric.setChecked(userSettings.getAnomalyHistoric());
        rbTests.setChecked(userSettings.getTests());
        rbTestsHistoric.setChecked(userSettings.getTestsHistoric());
        rbSettings.setChecked(userSettings.getSettings());

        //Storage
        int nowPercentageMaxOccupied = sharedPreferencesManager.getPercentageMemoryOccupied();

        arQoSeek.getSeekBar().setProgress(nowPercentageMaxOccupied);
        arQoSeek.setLabel(getContext().getString(R.string.settings_max) + " - " + nowPercentageMaxOccupied + "%");
        rbTheArqosPoocket.setChecked(userSettings.getTheArqosPocket());

        //Radiologs
        if (sharedPreferencesManager.getRadiologsDedicatedMode() == 1 ||
                sharedPreferencesManager.getRadiologsIdleMode() == 1 ||
                sharedPreferencesManager.getScanlogsEnable() == 1) {
            radiologSwitch.getSwitch().setChecked(true);
        } else {
            radiologSwitch.getSwitch().setChecked(false);
        }

        if (radiologSwitch.getSwitch().isChecked()) {
            chRadiologDedicated.setVisibility(VISIBLE);
            chRadiologIdle.setVisibility(VISIBLE);
            chScanlogs.setVisibility(VISIBLE);
            radiologProgressBar.setVisibility(VISIBLE);
        }

        if (sharedPreferencesManager.getRadiologsDedicatedMode() == 1) {
            chRadiologDedicated.setChecked(true);
        }
        if (sharedPreferencesManager.getRadiologsIdleMode() == 1) {
            chRadiologIdle.setChecked(true);
        }
        if (sharedPreferencesManager.getScanlogsEnable() == 1) {
            chScanlogs.setChecked(true);
        }

        radiologProgressBar.getSeek_bar().setProgress(sharedPreferencesManager.getRadiologPeriodicity());

        //Management System Connection
        rbTypeMG.setChecked(userSettings.getManagementSystemType());
        rbTypeManualMG.setChecked(!userSettings.getManagementSystemType());
        sgSwitch.getSwitch().setChecked(userSettings.getManagementSystemInManual());

        autoByDedug.getSwitch().setChecked(sharedPreferencesManager.getAutomaticallyRunTests());

        //Language
        updateLanguage(LanguageUtils.createLocale(sharedPreferencesManager.getPreferredLanguage()), false);
    }

    private void updateLanguage(Locale language, boolean restart) {
        if (language == null || Strings.isNullOrEmpty(language.toString())) {
            tvLanguage.setText(R.string.app_language);
        } else {
            tvLanguage.setText(language.getDisplayLanguage());
            LanguageUtils.updateLanguage(getContext(), language);

            if (restart) {
                restartApplication();
            }
        }
    }

    private void switchStateConnectionMS() {
        Boolean isConnections = KeepAliveManager.getInstance(getArQosApplication()).isStateConnection();
        ExecutingEventDao executingEventDao = getArQosApplication().getDatabaseHelper().createExecutingEventDao();

        int countPendingReport = executingEventDao.countPendingExecutedEvents();

        StringBuilder stringPendingReport = new StringBuilder();
        if (countPendingReport > 0) {
            stringPendingReport.append(" - ")
                    .append(getContext().getResources().getString(R.string.settings_pending_test)).append(": ")
                    .append(countPendingReport);
        }

        if (isConnections) {
            sgSwitch.setLabel(
                    getContext().getResources().getString(R.string.connected) + stringPendingReport.toString());
            sgSwitch.setIcon(R.mipmap.icon_sucesso);
        } else {
            sgSwitch.setIcon(R.mipmap.icon_erro);
            sgSwitch.setLabel(
                    getContext().getResources().getString(R.string.not_connected) + stringPendingReport.toString());
        }
    }

    private void restartApplication() {
        Intent intent = new Intent(getActivity(), SettingsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void readAndPersistValues(CompoundButton buttonView) {

        if (!buttonView.isChecked()) {
            return;
        }

        if (buttonView.getTag() != null) {
            switch (buttonView.getTag().toString()) {
                case "3":
                    rbDashboard.setChecked(false);
                    rbAnomaly.setChecked(false);
                    rbAnomalyHistoric.setChecked(false);
                    rbTests.setChecked(false);
                    rbTestsHistoric.setChecked(false);
                    rbSettings.setChecked(false);
                    rbTheArqosPoocket.setChecked(false);
                    break;
                case "4":
                    rbTypeMG.setChecked(false);
                    rbTypeManualMG.setChecked(false);
                    break;
                default:
            }

            buttonView.setChecked(true);
        }

        UserSettings userSettings = new UserSettings();

        userSettings.setDashboard(rbDashboard.isChecked());
        userSettings.setAnomaly(rbAnomaly.isChecked());
        userSettings.setAnomalyHistoric(rbAnomalyHistoric.isChecked());
        userSettings.setTests(rbTests.isChecked());
        userSettings.setTestsHistoric(rbTestsHistoric.isChecked());
        userSettings.setSettings(rbSettings.isChecked());
        userSettings.setTheArqosPocket(rbTheArqosPoocket.isChecked());

        userSettings.setManagementSystemType(rbTypeMG.isChecked());

        SharedPreferencesManager.getInstance(getArQosApplication()).saveUserSettings(userSettings);
    }

    private class SettingsOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            readAndPersistValues(buttonView);
        }
    }

    private class RadiologsModeChangeListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView.getTag() != null) {
                switch (buttonView.getTag().toString()) {
                    case "0":
                        if (isChecked) {
                            SharedPreferencesManager.getInstance(getArQosApplication()).setRadiologsDedicatedMode(1);
                            callPeriodicRadiologs();
                        } else {
                            SharedPreferencesManager.getInstance(getArQosApplication()).setRadiologsDedicatedMode(0);
                        }
                        buttonView.setChecked(isChecked);
                        break;
                    case "1":
                        if (isChecked) {
                            SharedPreferencesManager.getInstance(getArQosApplication()).setRadiologsIdleMode(1);
                            callPeriodicRadiologs();
                        } else {
                            SharedPreferencesManager.getInstance(getArQosApplication()).setRadiologsIdleMode(0);
                        }
                        buttonView.setChecked(isChecked);
                        break;
                    case "2":
                        if (isChecked) {
                            SharedPreferencesManager.getInstance(getArQosApplication()).setScanlogsEnable(1);
                            callPeriodicRadiologs();
                        } else {
                            SharedPreferencesManager.getInstance(getArQosApplication()).setScanlogsEnable(0);
                        }
                        buttonView.setChecked(isChecked);
                        break;
                    default:
                }

            }
        }
    }

    private class SGSwitchChangeListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            SharedPreferencesManager.getInstance(getArQosApplication()).setConnectionWithMSManual(isChecked);

            if (isChecked) {
                KeepAliveManager.getInstance(getArQosApplication()).start();
                AttachmentsProcessManager.startSendAttachment(getArQosApplication());
                RadiologsAttachmentsProcessManager.startSendAttachment(getArQosApplication());
                ScanlogsAttachmentsProcessManager.startSendAttachment(getArQosApplication());
            } else {
                KeepAliveManager.getInstance(getArQosApplication()).stop();
                AttachmentsProcessManager.getInstance(getArQosApplication()).stop();
                RadiologsAttachmentsProcessManager.getInstance(getArQosApplication()).stop();
                ScanlogsAttachmentsProcessManager.getInstance(getArQosApplication()).stop();
            }
        }
    }

    private class SGSwitchChangeAutoRunTests implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            setStateRunTest(isChecked);
        }
    }

    private int countChange = 0;

    private synchronized void setStateRunTest(boolean isChecked) {
        LOGGER.debug("State: " + isChecked);
        SharedPreferencesManager.getInstance(getArQosApplication()).setAutomaticallyRunTests(isChecked);

        if (countChange > 0) {
            LOGGER.debug("State real: " + isChecked);
            if (isChecked) {
                BootAlarmRestoreService.startActionRestoreAlarms(getContext());
            } else {
                for (ScheduledEvent scheduledEvent : getArQosApplication().getDatabaseHelper().createScheduledEventDao()
                        .readAllScheduledEventList()) {
                    TestAlarmManager testAlarmManager = new TestAlarmManager(getArQosApplication());
                    testAlarmManager.cancelAlarm(scheduledEvent.getId());
                }
            }
        }
        countChange++;
    }

    private class RadiologSwitchChangeListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                radiologSwitch.setLabel(getResources().getString(R.string.connected));
                radiologSwitch.setIcon(R.mipmap.icon_sucesso);

                chRadiologDedicated.setVisibility(VISIBLE);
                chRadiologIdle.setVisibility(VISIBLE);
                chScanlogs.setVisibility(VISIBLE);

                radiologProgressBar.setVisibility(VISIBLE);

                radiologProgressBar.getSeek_bar().setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                    // When Progress value changed.
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                        radiologPeriodicity = progressValue;
                        SharedPreferencesManager.getInstance(getArQosApplication())
                                .setRadiologPeriodicity(radiologPeriodicity);
                        radiologProgressBar.getProgress_text().setText("A cada " + radiologPeriodicity + " seg.");
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    // Notification that the user has finished a touch gesture
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        radiologProgressBar.getProgress_text().setText("A cada " + radiologPeriodicity + " seg.");
                    }
                });
            } else {
                SharedPreferencesManager.getInstance(getArQosApplication()).setRadiologsDedicatedMode(0);
                SharedPreferencesManager.getInstance(getArQosApplication()).setRadiologsIdleMode(0);
                SharedPreferencesManager.getInstance(getArQosApplication()).setScanlogsEnable(0);

                chRadiologDedicated.setChecked(false);
                chRadiologIdle.setChecked(false);
                chScanlogs.setChecked(false);

                radiologSwitch.setLabel(getResources().getString(R.string.not_connected));
                radiologSwitch.setIcon(R.mipmap.icon_erro);

                chRadiologDedicated.setVisibility(View.GONE);
                chRadiologIdle.setVisibility(View.GONE);
                chScanlogs.setVisibility(View.GONE);
                radiologProgressBar.setVisibility(View.GONE);
            }
        }
    }

    public void callPeriodicRadiologs(){
        if (!RadiologsManager.getInstance(getArQosApplication()).isGenerating) {
            RadiologsManager.getInstance(getArQosApplication()).generatePeriodicRadiologs();
        }
    }

    private TestProgressNotifier testProgressNotifier = new TestProgressNotifier() {
        @Override
        public void onTestExecutionStarted(TestProgress testProgress) {
        }

        @Override
        public void onTaskExecutionStarted(TaskProgress taskProgress) {
        }

        @Override
        public void onTaskExecutionFinished(TaskProgress taskProgress) {
        }

        @Override
        public void onTestExecutionFinished(TestProgress testProgress) {
            switchStateConnectionMS();
        }

        @Override
        public void onTestDataChanged(TestProgress testProgress) {
        }
    };

}