package pt.ptinovacao.arqospocket.util;

import pt.ptinovacao.arqospocket.activities.ActivityRadiologs;
import pt.ptinovacao.arqospocket.activities.ActivityRadiologsHistorico;
import pt.ptinovacao.arqospocket.service.interfaces.IService;
import pt.ptinovacao.arqospocket.MyApplication;
import pt.ptinovacao.arqospocket.activities.ActivityAjuda;
import pt.ptinovacao.arqospocket.activities.ActivityAnomalias;
import pt.ptinovacao.arqospocket.activities.ActivityAnomaliasHistorico;
import pt.ptinovacao.arqospocket.activities.ActivityConfiguracoes;
import pt.ptinovacao.arqospocket.activities.ActivityDashboardMain;
import pt.ptinovacao.arqospocket.activities.ActivityTestes;
import pt.ptinovacao.arqospocket.activities.ActivityTestesHistorico;
import pt.ptinovacao.arqospocket.activities.Activityo_arQos;
import pt.ptinovacao.arqospocket.activities.Activity_Development;
import pt.ptinovacao.arqospocket.core.CurrentConfiguration;

import android.app.Activity;
import android.content.Intent;


public class Homepage {
	static Intent it;

	
	Activity act;
	static MenuOption HomeP = MenuOption.Dashboard;

	public Homepage(Activity act) {
		this.act = act;
	}
	
	public MenuOption ReadHome() {

		MyApplication MyApplicationRef = (MyApplication) act
				.getApplicationContext();
		IService iService = MyApplicationRef.getEngineServiceRef();
		if (iService != null) {

			pt.ptinovacao.arqospocket.service.store.CurrentConfiguration currentConfiguration = iService
					.get_current_configuration();

			if (currentConfiguration != null) {
				CurrentConfiguration.set_configuration(currentConfiguration);
				HomeP = CurrentConfiguration.getHomepageid();
			}
		}
		return HomeP;
	}

	public Intent TesteTipe(MenuOption home) {
		switch (home) {

			case Dashboard:
				it = new Intent(act, ActivityDashboardMain.class);
				act.startActivity(it);
				break;

			case Anomalias:
				it = new Intent(act, ActivityAnomalias.class);
				act.startActivity(it);
				break;

			case HistoricoAnomalias:
				it = new Intent(act, ActivityAnomaliasHistorico.class);
				act.startActivity(it);
				break;

			case Testes:
				it = new Intent(act, ActivityTestes.class);
				act.startActivity(it);
				break;

			case HistoricoTestes:
				it = new Intent(act, ActivityTestesHistorico.class);
				act.startActivity(it);
				break;

			case Radiologs:
				it = new Intent(act, ActivityRadiologs.class);
				act.startActivity(it);
				break;

			case HistoricoRadiologs:
				it = new Intent(act, ActivityRadiologsHistorico.class);
				act.startActivity(it);
				break;

			case Desenvolvimento:
				it = new Intent(act, Activity_Development.class);
				act.startActivity(it);
				break;

			case Configuracoes:
				it = new Intent(act, ActivityConfiguracoes.class);
				act.startActivity(it);
				break;

			case Ajuda:
				it = new Intent(act, ActivityAjuda.class);
				act.startActivity(it);
				break;

			case Info:
				it = new Intent(act, Activityo_arQos.class);
				act.startActivity(it);
				break;
		}
		return it;
	}
}
