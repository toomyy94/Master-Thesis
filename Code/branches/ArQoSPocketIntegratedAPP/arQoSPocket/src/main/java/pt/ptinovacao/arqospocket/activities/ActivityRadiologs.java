package pt.ptinovacao.arqospocket.activities;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import org.json.JSONObject;

import pt.ptinovacao.arqospocket.ArqosActivity;
import pt.ptinovacao.arqospocket.MyApplication;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.adapters.AdapterFragmentAnomaliasView;
import pt.ptinovacao.arqospocket.adapters.AdapterFragmentRadiologsView;
import pt.ptinovacao.arqospocket.fragments.FragmentIdtlocal;
import pt.ptinovacao.arqospocket.fragments.FragmentStepsAnomalias;
import pt.ptinovacao.arqospocket.fragments.FragmentTipfalha;
import pt.ptinovacao.arqospocket.service.interfaces.IService;
import pt.ptinovacao.arqospocket.util.Homepage;
import pt.ptinovacao.arqospocket.util.I_OnDataPass;
import pt.ptinovacao.arqospocket.util.MenuOption;

public class ActivityRadiologs extends ArqosActivity{
	
	private static final String TAG = "ActivityRadiologs";

	ViewPager mViewPager;
    AdapterFragmentRadiologsView adapter;
	String Text_feedback, Text_radiolog;
	boolean Fed = false;
	IService iService;
	double Long,Lat;
	MenuOption HomeP;
	Homepage home;
    JSONObject snapshot = new JSONObject();
	
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		super.onActionBarSetTitle(getString(R.string.radiologs));
		super.setMenuOption(MenuOption.Radiologs);
		setContentView(R.layout.activity_radio);
		
		home = new Homepage (this);
		mViewPager = (ViewPager) findViewById(R.id.activity_anomalias_viewpager);
		adapter = new AdapterFragmentRadiologsView(getSupportFragmentManager());
		mViewPager.setAdapter(adapter);

		MyApplication MyApplicationRef = (MyApplication) getApplicationContext();
		iService = MyApplicationRef.getEngineServiceRef();

	}

	public ViewPager getpager() {

		return mViewPager;
	}

	public void setTextFed(String Text_feedback) {
		this.Text_feedback = Text_feedback;
		Log.i(TAG, "setText_feedback: " + Text_feedback);
	}

	public String getText_feedback() {
		return Text_feedback;
	}

    public void setRadiologFed(JSONObject snapshot) {
        this.snapshot = snapshot;
        this.Text_radiolog = snapshot.toString();
        Log.i(TAG, "snapshot: " + snapshot);
    }

    public String getText_Event() {
        return null;
    }

	public void setEnvFed(boolean Fed) {
        this.Fed = Fed;
		
		Location loc = iService.get_location();
		iService.send_report_radio(Text_radiolog, loc, Text_feedback, null);

		Log.i(TAG, "envio final: " + Text_feedback);
		Log.i(TAG, "envio final: " + Text_radiolog);
		Log.i(TAG, "envio final: " + loc);
		finish();
	}

	public boolean getEnvFed() {
		return false;
	}

	public void onBackPressed() {
		if (slidingMenuIsOpen()) {
			onSlidingMenuToggle();
		} else {
			HomeP=home.ReadHome();
			if(HomeP==MenuOption.Radiologs){
				finish();
			}else{
				home.TesteTipe(HomeP);
				finish();
			}
		}
	}

	public void setLong(double Long) {
		this.Long=Long;
	}

	public Double getLong() {
		return null;
	}

	public void setLat(double Lat) { this.Lat=Lat; }

	public Double getLat() {
		return null;
	}

	public void hideKeyboard(View view) {
		InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
}
