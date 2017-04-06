package pt.ptinovacao.arqospocket.core;

import pt.ptinovacao.arqospocket.util.MenuConToManagement;
import pt.ptinovacao.arqospocket.util.MenuLogs;
import pt.ptinovacao.arqospocket.util.MenuOption;
import pt.ptinovacao.arqospocket.util.MenuTimer;
import android.util.Log;

public class CurrentConfiguration {

	private static final String TAG = "CurrentConfiguration";
	private static MenuOption homepageid = MenuOption.Dashboard;
	private static MenuLogs logsid = MenuLogs.ApensWifi;
	private static MenuTimer Timelogsid = MenuTimer.Horario;
	private static String languagecode;
	private static MenuConToManagement Connid = MenuConToManagement.Automatic;

	
	public static pt.ptinovacao.arqospocket.service.store.CurrentConfiguration get_service_configuration() {
		int start_page = -1;
		int send_technology = -1;
		int report_frequency = -1;
		int management_connection = -1;

		switch (logsid) {
			case ApensWifi:
				send_technology = 0;
				break;
			case prefeWiFi:
				send_technology = 1;
				break;
			case qualquer:
				send_technology = 2;
				break;
		}
		
		switch(Timelogsid) {
			case Diario:
				report_frequency = 0;
				break;
			case Horario:
				report_frequency = 1;
				break;
		}

		switch(Connid) {
			case Automatic:
				management_connection = 0;
				break;
			case Manual:
				management_connection = 1;
				break;
		}
		
		switch (homepageid) {
			case Dashboard:
				start_page = 3;
				break;
			case Anomalias:
				start_page = 1;
				break;
			case HistoricoAnomalias:
				start_page = 4;
				break;
			case Testes:
				start_page = 8;
				break;
			case HistoricoTestes:
				start_page = 5;
				break;
			case Desenvolvimento:
				start_page = 2;
				break;
			case Configuracoes:
				start_page = 0;
				break;
			case Ajuda:
				start_page = 7;
				break;
			case Info:
				start_page = 6;
				break;

			}
		
		return new pt.ptinovacao.arqospocket.service.store.CurrentConfiguration(start_page, send_technology, report_frequency, management_connection);
	}
	
	
	public static void set_configuration(pt.ptinovacao.arqospocket.service.store.CurrentConfiguration currentConfiguration) {
		
		switch (currentConfiguration.get_send_technology()) {
			case 0:
				logsid = MenuLogs.ApensWifi;
				break;
			case 1:
				logsid = MenuLogs.prefeWiFi;
				break;
			case 2:
				logsid = MenuLogs.qualquer;
				break;
		}
		
		switch(currentConfiguration.get_report_frequency()) {
			case 0:
				Timelogsid = MenuTimer.Diario;
				break;
			case 1:
				Timelogsid = MenuTimer.Horario;
				break;
		}


		switch(currentConfiguration.get_management_connection()) {
			case 0:
				Connid = MenuConToManagement.Automatic;
				break;
			case 1:
                Connid = MenuConToManagement.Manual;
				break;
		}

		switch(currentConfiguration.get_start_page()) {
			case 0:
				homepageid = MenuOption.Ajuda;
				break;
			case 1:
				homepageid = MenuOption.Anomalias;
				break;
			case 2:
				homepageid = MenuOption.Configuracoes;
				break;
			case 3:
				homepageid = MenuOption.Dashboard;
				break;
			case 4:
				homepageid = MenuOption.HistoricoAnomalias;
				break;
			case 5:
				homepageid = MenuOption.HistoricoTestes;
				break;
			case 6:
				homepageid = MenuOption.Desenvolvimento;
				break;
			case 7:
				homepageid = MenuOption.Info;
				break;
			case 8:
				homepageid = MenuOption.Testes;
				break;

		}
	}

	public static MenuLogs getlogsid() {
		return logsid;
	}

	public static void setLogsid(MenuLogs logsid) {
		Log.d(TAG, "Current home page: " + logsid.toString());
		CurrentConfiguration.logsid = logsid;
	}

	public static MenuTimer gettimelogsid() {
		return Timelogsid;
	}

	public static MenuConToManagement getconnlogsid() {
		return Connid;
	}

	public static String getLanguagecode() {
		return languagecode;
	}

	public static void setTimelogsid(MenuTimer Timelogsid) {
		Log.d(TAG, "Current home page: " + Timelogsid.toString());
		CurrentConfiguration.Timelogsid = Timelogsid;
	}

	public static void setConnlogsid(MenuConToManagement Connlogsid) {
		CurrentConfiguration.Connid = Connlogsid;
	}

	public static MenuOption getHomepageid() {
		return homepageid;
	}

	public static void setHomepageid(MenuOption homepageid) {
		Log.d(TAG, "Current home page: " + homepageid.toString());
		CurrentConfiguration.homepageid = homepageid;
	}



}
