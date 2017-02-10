package pt.ptinovacao.arqospocket.activities;

import pt.ptinovacao.arqospocket.service.interfaces.IService;
import pt.ptinovacao.arqospocket.ArqosActivity;
import pt.ptinovacao.arqospocket.MyApplication;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.util.Homepage;
import pt.ptinovacao.arqospocket.util.MenuOption;
import android.os.Bundle;

public class ActivityAjuda extends ArqosActivity {
	
	IService iService;
	MyApplication MyApplicationRef;
	MenuOption HomeP;
	Homepage home;
	
	protected void onCreate(Bundle arg0) { 
		super.onCreate(arg0);
		super.onActionBarSetTitle(getString(R.string.help));
		super.setMenuOption(MenuOption.Ajuda);
		setContentView(R.layout.activity_ajuda);	  
		
		home = new Homepage (this);
	}

	public void onBackPressed() {
		if (slidingMenuIsOpen()) {
			onSlidingMenuToggle();
		} else {
			HomeP=home.ReadHome();
			if(HomeP==MenuOption.Ajuda){
				finish();
			}else{
			home.TesteTipe(HomeP);
			finish();
		}
		}
		}


}
