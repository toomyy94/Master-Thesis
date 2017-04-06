package pt.ptinovacao.arqospocket.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

import pt.ptinovacao.arqospocket.fragments.FragmentTestesHistorico;
import pt.ptinovacao.arqospocket.service.interfaces.IService;
import pt.ptinovacao.arqospocket.ArqosActivity;
import pt.ptinovacao.arqospocket.MyApplication;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.core.CurrentConfiguration;

import pt.ptinovacao.arqospocket.fragments.FragmentActionBar;
import pt.ptinovacao.arqospocket.util.Homepage;
import pt.ptinovacao.arqospocket.util.LocaleHelper;
import pt.ptinovacao.arqospocket.util.MenuConToManagement;
import pt.ptinovacao.arqospocket.util.MenuLogs;
import pt.ptinovacao.arqospocket.util.MenuOption;
import pt.ptinovacao.arqospocket.util.MenuTimer;
import pt.ptinovacao.arqospocket.util.SharedPreferencesHelper;
import pt.ptinovacao.arqospocket.views.ImageSwitch_Layout;
import pt.ptinovacao.arqospocket.views.SuperTextView;

import static pt.ptinovacao.arqospocket.util.SharedPreferencesHelper.RUN_DATE_TESTS_KEY;

public class ActivityConfiguracoes extends ArqosActivity {

	private static final String TAG = "ActivityConfiguracoes";
    private static final int LANGUAGE_SELECTION_REQUEST_CODE = 327;

	RadioGroup enviarlogs, timelogs, homepage, management_system;
	RadioButton wifi, prefwifi, qualquer, horario, diario, dashboard,
			anomalias, histanomalias, testes, histtestes, config, ajuda, info, radioRunDateTests, development, automatic, manual;
	SuperTextView tvLanguage;
	ImageSwitch_Layout switch_layout;
	FragmentActionBar actionBar;
	MenuOption HomeP;
	Homepage home;
	int idxlogs;
	private IService iService = null;
	MenuOption homlogs;
	MenuTimer timlogs;
	MenuLogs ckelogs;
	MenuConToManagement conlogs;
	String languageCode;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		super.onActionBarSetTitle(getString(R.string.settings));
		setContentView(R.layout.activity_conf);
		super.setMenuOption(MenuOption.Configuracoes);
		home = new Homepage (this);
		
		// vai buscar o ficheiro de configurações ao serviço
		MyApplication myApplicationRef = (MyApplication) getApplicationContext();
		iService = myApplicationRef.getEngineServiceRef();

		pt.ptinovacao.arqospocket.service.store.CurrentConfiguration currentConfiguration = iService
				.get_current_configuration();

		if (currentConfiguration != null)
			CurrentConfiguration.set_configuration(currentConfiguration);

		// atualiza o ficheiro de configurações da UI

		addListenerOnButton();
		MenuLogs ckelogs = CurrentConfiguration.getlogsid();
		Chekedlogs(ckelogs);
		MenuTimer timlogs = CurrentConfiguration.gettimelogsid();
		Timelogs(timlogs);
		MenuOption homlogs = CurrentConfiguration.getHomepageid();
		Homepage(homlogs);
		MenuConToManagement connlogs = CurrentConfiguration.getconnlogsid();
		Connectionlogs(connlogs);

		setRunDateTests(SharedPreferencesHelper.getPersistedData(this, RUN_DATE_TESTS_KEY, true));

		tvLanguage = (SuperTextView) findViewById(R.id.tvLanguage);
        final Intent languageActivityIntent = new Intent(this, LanguageSelectionActivity.class);
        tvLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(languageActivityIntent, LANGUAGE_SELECTION_REQUEST_CODE);

            }
        });
		//tvLanguage.setCompoundDrawablesWithIntrinsicBounds(0, 0, LocaleHelper.flag(this), 0);

	}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LANGUAGE_SELECTION_REQUEST_CODE && resultCode == Activity.RESULT_OK){
			languageCode = data.getStringExtra(LanguageSelectionActivity.RESULT_LANGUAGE_CODE);
            //Toast.makeText(this, "You selected countrycode: " + languageCode, Toast.LENGTH_LONG).show();
            LocaleHelper.setLocale(this, languageCode);
            this.recreate();
        }
    }

	public String getLanguageCode(){return languageCode;};

	public void onPause() {
		super.onPause();
//		CurrentConfiguration conf=new CurrentConfiguration();
//		CurrentConfiguration conf=new CurrentConfiguration(idxhome,idxlogs,idxtimelogs);
//		iService.set_current_configuration(conf);		
//		Log.i("arqos", "On pause  " +"Homepage:"+idxhome+" Tipo:"+idxlogs+" Tempo:"+idxtimelogs );
		iService.set_current_configuration(CurrentConfiguration
				.get_service_configuration());
	}

	public void Chekedlogs(MenuLogs selectlogs) {
		//
		switch (selectlogs) {
		case ApensWifi:
			enviarlogs.check(R.id.radio_wifi);
			break;
		case prefeWiFi:
			enviarlogs.check(R.id.radio_pref_wifi);
			break;
		case qualquer:
			enviarlogs.check(R.id.radio_qualquer);
			break;
		default:

			break;
		}
	}

	public void Timelogs(MenuTimer selectlogs) {
		//
		switch (selectlogs) {
		case Horario:
			timelogs.check(R.id.radio_horario);
			break;
		case Diario:
			timelogs.check(R.id.radio_diario);
			break;

		default:
			break;
		}
	}

	public void Connectionlogs(MenuConToManagement connlogs) {
		//
		switch (connlogs) {
			case Automatic:
				management_system.check(R.id.radio_automatic);
				break;
			case Manual:
				management_system.check(R.id.radio_manual);
				break;
			default:
				break;
		}
	}

	public void Homepage(MenuOption selecthome) {

		switch (selecthome) {
			case Dashboard:
				homepage.check(R.id.radio_dashboard);
				break;
			case Anomalias:
				homepage.check(R.id.radio_anomalias);
				break;
			case HistoricoAnomalias:
				homepage.check(R.id.radio_historico_anomalias);
				break;
			case Testes:
				homepage.check(R.id.radio_testes);
				break;
			case HistoricoTestes:
				homepage.check(R.id.radio_historico_testes);
				break;
			case Configuracoes:
				homepage.check(R.id.radio_configuracoes);
				break;
//			case Ajuda:
//				homepage.check(R.id.radio_ajuda);
//				break;
//			case Info:
//				homepage.check(R.id.radio_info);
//				break;
			case Desenvolvimento:
				homepage.check(R.id.radio_development);
				break;
			default:
				break;
		}

	}

	public void setRunDateTests(boolean value) {
		radioRunDateTests.setChecked(value);
	}

	public void addListenerOnButton() {

		enviarlogs = (RadioGroup) findViewById(R.id.radio_envio_logs);
		timelogs = (RadioGroup) findViewById(R.id.radio_timeLogs);
		homepage = (RadioGroup) findViewById(R.id.radio_homepage);
		management_system = (RadioGroup) findViewById(R.id.radio_managment_connection);

		wifi = (RadioButton) findViewById(R.id.radio_wifi);
		prefwifi = (RadioButton) findViewById(R.id.radio_pref_wifi);
		qualquer = (RadioButton) findViewById(R.id.radio_qualquer);
		horario = (RadioButton) findViewById(R.id.radio_horario);
		diario = (RadioButton) findViewById(R.id.radio_diario);
		dashboard = (RadioButton) findViewById(R.id.radio_dashboard);
		anomalias = (RadioButton) findViewById(R.id.radio_anomalias);
		histanomalias = (RadioButton) findViewById(R.id.radio_historico_anomalias);
		testes = (RadioButton) findViewById(R.id.radio_testes);
		histtestes = (RadioButton) findViewById(R.id.radio_historico_testes);
		config = (RadioButton) findViewById(R.id.radio_configuracoes);
//		ajuda = (RadioButton) findViewById(R.id.radio_ajuda);
//		info = (RadioButton) findViewById(R.id.radio_info);
		radioRunDateTests = (RadioButton) findViewById(R.id.radio_date_tests);
		development = (RadioButton) findViewById(R.id.radio_development);
		automatic = (RadioButton) findViewById(R.id.radio_development);
		manual = (RadioButton) findViewById(R.id.radio_development);
        switch_layout = (ImageSwitch_Layout) findViewById(R.id.image_switch_activity_conf);

		enviarlogs.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				View view = group.findViewById(group
						.getCheckedRadioButtonId());
				idxlogs = group.indexOfChild(view);

				setMenulogs(idxlogs);

				iService.set_current_configuration(CurrentConfiguration
						.get_service_configuration());

			}
		});

		timelogs.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				View view = group.findViewById(group
						.getCheckedRadioButtonId());
				idxlogs = group.indexOfChild(view);

				setTimelogs(idxlogs);

				iService.set_current_configuration(CurrentConfiguration
						.get_service_configuration());
			}
		});

		homepage.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				View view = group.findViewById(group
						.getCheckedRadioButtonId());
				idxlogs = group.indexOfChild(view);

				setMenuHomepage(idxlogs);

				iService.set_current_configuration(CurrentConfiguration
						.get_service_configuration());
			}
		});

		management_system.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				final View view = group.findViewById(group
						.getCheckedRadioButtonId());
				idxlogs = group.indexOfChild(view);

				setConnlogs(idxlogs);

				switch (idxlogs) {
					case 0:
						//Automatic Connection
						switch_layout.setVisibility(View.GONE);
						//TODO LIGAR AO SISTEMA DE GESTÃO...



						//----
						//if(ligação_bem_sucedida && have_pending_tests)

						//if(send_pending_tests_bem_sucedida)
						final AlertDialog.Builder al_send_tests = new  AlertDialog.Builder(view.getContext());
						LayoutInflater inflater = getLayoutInflater();
						View myview2 = inflater.inflate(R.layout.alertdialog_custom, null);
						al_send_tests.setView(myview2);

						//al_send_tests.setContentView(R.layout.alertdialog_custom);
						TextView title = (TextView) myview2.findViewById(R.id.title);
						title.setText(R.string.alert_connection);
						TextView subtitle = (TextView) myview2.findViewById(R.id.subtitle);
						subtitle.setText(R.string.alert_connection_done_subtitle);
						TextView message = (TextView) myview2.findViewById(R.id.mensagem);
						message.setText(R.string.alert_connection_done_message);
						ImageView icon = (ImageView) myview2.findViewById(R.id.icon);
						icon.setImageResource(R.drawable.icon_info);
						final AlertDialog alertDialog2 = al_send_tests.show();

						Button yes = (Button) myview2.findViewById (R.id.button_yes);
						yes.setText(R.string.alert_send_tests_now);
						yes.setOnClickListener(new View.OnClickListener() {

							public void onClick(View v) {
                                alertDialog2.dismiss();
								//TODO SEND PENDING TESTS...
								//activity.getServiceInterface().send_pending_tests(FragmentTestesHistorico.this);

								//if(send_pending_tests_bem_sucedida)
								AlertDialog.Builder al_tests_sucess = new  AlertDialog.Builder(view.getContext());
								LayoutInflater inflater = getLayoutInflater();
								View myview = inflater.inflate(R.layout.alertdialog_custom_no_buttons, null);
								al_tests_sucess.setView(myview);
								//al_tests_sucess.setContentView(R.layout.alertdialog_custom_no_buttons);
								TextView title = (TextView) myview.findViewById(R.id.title);
								title.setText(R.string.alert_pending_tests);
								TextView subtitle = (TextView) myview.findViewById(R.id.subtitle);
								subtitle.setText(R.string.alert_send_tests_sucess);
								al_tests_sucess.show();


							}
						});
						Button no = (Button) myview2.findViewById (R.id.button_no);
						no.setOnClickListener(new View.OnClickListener() {

							public void onClick(View v) {
								//do some stuff
								alertDialog2.dismiss();
							}
						});

						break;
					case 2:
						//Manual Connection
						switch_layout.setVisibility(View.VISIBLE);
                        switch_layout.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

							public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
								if(isChecked){
                                    switch_layout.setIconVisibility(2);
                                    switch_layout.setLabel(getResources().getString(R.string.establishing_connection));
                                    //TODO Fazer a ligação ao Sistema de Gestão

									//TODO if(connection_made/success())
                                    switch_layout.setIconVisibility(0);
                                    switch_layout.setLabel(getResources().getString(R.string.connected));
                                    switch_layout.setIcon(getResources().getDrawable(R.drawable.icon_sucesso));

                                        //TODO if(a receber configurações)
                                        switch_layout.setLabel(getResources().getString(R.string.connected) +": "+ getResources().getString(R.string.receveing_configurations) );

                                        //TODO else if(a enviar dados)
                                        switch_layout.setLabel(getResources().getString(R.string.connected) +": "+ getResources().getString(R.string.receveing_configurations) );

                                    //TODO else(erro na ligação)
                                    switch_layout.setIcon(getResources().getDrawable(R.drawable.icon_erro));
                                    switch_layout.setLabel(getResources().getString(R.string.not_connected) +": "+ getResources().getString(R.string.try_again_connect));



                                }else{
                                    switch_layout.setIconVisibility(0);
                                    switch_layout.setLabel(getResources().getString(R.string.not_connected));
                                    switch_layout.setIcon(getResources().getDrawable(R.drawable.icon_erro));

                                    //TODO (TRY CONNECTION AGAIN IN 12 SEC)
								}
							}
						});

				}

				iService.set_current_configuration(CurrentConfiguration
						.get_service_configuration());
			}
		});

		radioRunDateTests.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean newState = !(SharedPreferencesHelper.getPersistedData(getApplicationContext(), RUN_DATE_TESTS_KEY, true));
				Log.d(TAG, "Click Run Date Tests: " + newState);
				radioRunDateTests.setChecked(newState);
				SharedPreferencesHelper.persist(getApplicationContext(), RUN_DATE_TESTS_KEY, newState);
			}
		});



	}

	public void setMenulogs(int selectedlogs) {

		switch (selectedlogs) {
		case 0:
			CurrentConfiguration.setLogsid(MenuLogs.ApensWifi);
			break;
		case 2:
			CurrentConfiguration.setLogsid(MenuLogs.prefeWiFi);
			break;
		case 4:
			CurrentConfiguration.setLogsid(MenuLogs.qualquer);
			break;

		default:
			Log.d(TAG, "Unknown value: " + selectedlogs);
			break;
		}

	}

	public void setTimelogs(int selectedtimelogs) {

		switch (selectedtimelogs) {
		case 0:
			CurrentConfiguration.setTimelogsid(MenuTimer.Horario);
			break;
		case 2:
			CurrentConfiguration.setTimelogsid(MenuTimer.Diario);
			break;

		default:
			Log.d(TAG, "Unknown value: " + selectedtimelogs);
			break;
		}

	}

	public void setConnlogs(int selectedconnlogs) {

		switch (selectedconnlogs) {
			case 0:
				CurrentConfiguration.setConnlogsid(MenuConToManagement.Automatic);
				break;
			case 2:
				CurrentConfiguration.setConnlogsid(MenuConToManagement.Manual);
				break;

			default:
				Log.d(TAG, "Unknown value: " + selectedconnlogs);
				break;
		}

	}


	public void setMenuHomepage(int selectedhomepage) {

		switch (selectedhomepage) {
		case 0:
			CurrentConfiguration.setHomepageid(MenuOption.Dashboard);
			break;
		case 2:
			CurrentConfiguration.setHomepageid(MenuOption.Anomalias);
			break;
		case 4:
			CurrentConfiguration.setHomepageid(MenuOption.HistoricoAnomalias);
			break;
		case 6:
			CurrentConfiguration.setHomepageid(MenuOption.Testes);
			break;
		case 8:
			CurrentConfiguration.setHomepageid(MenuOption.HistoricoTestes);
			break;
		case 10:
			CurrentConfiguration.setHomepageid(MenuOption.Configuracoes);
			break;
//		case 12:
//			CurrentConfiguration.setHomepageid(MenuOption.Ajuda);
//			break;
//		case 14:
//			CurrentConfiguration.setHomepageid(MenuOption.Info);
//			break;
		case 12:
			CurrentConfiguration.setHomepageid(MenuOption.Desenvolvimento);
			break;
		default:
			Log.d(TAG, "Unknown value: " + selectedhomepage);
			break;
		}

	}

	public void onBackPressed() {
		iService.set_current_configuration(CurrentConfiguration
				.get_service_configuration());
		if (slidingMenuIsOpen()) {
			onSlidingMenuToggle();
		} else {
		
			HomeP=home.ReadHome();	
			if(HomeP==MenuOption.Configuracoes){
				finish();
			}else{
			home.TesteTipe(HomeP);
			finish();
			}
		}
	}


}
