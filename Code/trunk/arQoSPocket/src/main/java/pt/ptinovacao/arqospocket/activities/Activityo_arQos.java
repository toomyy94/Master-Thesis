package pt.ptinovacao.arqospocket.activities;

import pt.ptinovacao.arqospocket.ArqosActivity;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.util.Homepage;
import pt.ptinovacao.arqospocket.util.MenuOption;
import android.os.Bundle;

public class Activityo_arQos extends ArqosActivity {
	MenuOption HomeP;
	Homepage home;
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		super.onActionBarSetTitle(getString(R.string.about));
		super.setMenuOption(MenuOption.Info);
		setContentView(R.layout.activityarqos);
		home = new Homepage (this);

	}
	
	public void onBackPressed() {
		if (slidingMenuIsOpen()) {
			onSlidingMenuToggle();
		} else {
			HomeP=home.ReadHome();
			if(HomeP==MenuOption.Info){
				finish();
			}else{
			home.TesteTipe(HomeP);
			finish();
		}
		}
	}
}

