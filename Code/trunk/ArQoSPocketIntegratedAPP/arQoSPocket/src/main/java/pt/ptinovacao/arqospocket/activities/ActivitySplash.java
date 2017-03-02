package pt.ptinovacao.arqospocket.activities;

import pt.ptinovacao.arqospocket.service.interfaces.IService;
import pt.ptinovacao.arqospocket.ArqosActivity;
import pt.ptinovacao.arqospocket.MyApplication;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.core.CurrentConfiguration;

import pt.ptinovacao.arqospocket.interfaces.IServiceBound;
import pt.ptinovacao.arqospocket.util.MenuOption;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class ActivitySplash extends ArqosActivity implements IServiceBound {


	Intent it;
	MenuOption homlogs;
	IService iService;
	MyApplication MyApplicationRef;
	
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_splash); 
		
		//GregorianCalendar maxDate = new GregorianCalendar (2014, Calendar.DECEMBER, 30, 23, 59);
		//if (System.currentTimeMillis() > maxDate.getTimeInMillis())
		//	this.finish();
		
		MyApplicationRef = (MyApplication) getApplicationContext();
		iService = MyApplicationRef.getEngineServiceRef(); 
		
		if(iService == null)
			MyApplicationRef.registerOnServiceBound(this);
		else
			onServiceBound();
	}

	@Override
	public boolean actionBarEnabled() {
		return false;
	}

	 public void TesteTipe(MenuOption home) {
	 switch (home) {
	
	 case Dashboard:
	 it = new Intent(ActivitySplash.this,
	 ActivityDashboardMain.class);
	 startActivity(it);
	 finish();
	 break;
	 case Anomalias:
	 it = new Intent(ActivitySplash.this,
	 ActivityAnomalias.class);
	 startActivity(it);
	 finish();
	 break;
	 case HistoricoAnomalias:
	 it = new Intent(ActivitySplash.this,
	 ActivityAnomaliasHistorico.class);
	 startActivity(it);
	 finish();
	 break;
	 case Testes:
	 it = new Intent(ActivitySplash.this,
	 ActivityTestes.class);
	 startActivity(it);
	 finish();
	 break;
	
	 case HistoricoTestes:
	 it = new Intent(ActivitySplash.this,
	 ActivityTestesHistorico.class);
	 startActivity(it);
	 finish();
	 break;
	
	 case Configuracoes:
	 it = new Intent(ActivitySplash.this,
	 ActivityConfiguracoes.class);
	 startActivity(it);
	 finish();
	 break;
	 case Ajuda:
	 it = new Intent(ActivitySplash.this,
	 ActivityAjuda.class);
	 startActivity(it);
	 finish();
	 break;
	
	 case Info:
	 it = new Intent(ActivitySplash.this,
	 Activityo_arQos.class);
	 startActivity(it);
	 finish();
	 break;
	 }
	 }

	@Override
	public void onServiceBound() {  
		
		//GregorianCalendar maxDate = new GregorianCalendar (2014, Calendar.DECEMBER, 30, 23, 59);
		//if (System.currentTimeMillis() > maxDate.getTimeInMillis())
		//	return;

		iService = MyApplicationRef.getEngineServiceRef();  

		pt.ptinovacao.arqospocket.service.store.CurrentConfiguration currentConfiguration = iService
				.get_current_configuration();
		if (currentConfiguration != null) {
			CurrentConfiguration.set_configuration(currentConfiguration);
			homlogs = CurrentConfiguration.getHomepageid();
		}
				new Handler().postDelayed(new Runnable() {

			@Override
			public void run() { 
				if(homlogs != null)
					TesteTipe(homlogs);
			}
		}, 1000);
	}

}
