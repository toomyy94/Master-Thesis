package pt.ptinovacao.arqospocket.activities;

import pt.ptinovacao.arqospocket.adapters.AdapterFragmentAnomaliasView;
import pt.ptinovacao.arqospocket.service.interfaces.IService;
import pt.ptinovacao.arqospocket.ArqosActivity;
import pt.ptinovacao.arqospocket.MyApplication;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.fragments.FragmentIdtlocal;
import pt.ptinovacao.arqospocket.fragments.FragmentStepsAnomalias;
import pt.ptinovacao.arqospocket.fragments.FragmentTipfalha;
import pt.ptinovacao.arqospocket.util.Homepage;
import pt.ptinovacao.arqospocket.util.I_OnDataPass;
import pt.ptinovacao.arqospocket.util.MenuOption;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class ActivityAnomalias extends ArqosActivity implements I_OnDataPass {
	
	private static final String TAG = "ActivityAnomalias";

	int selectTip, posselected;
	ViewPager mViewPager;
	AdapterFragmentAnomaliasView adapter;
	String Textsrt, TextAbt, TextTdf;
	boolean Fed = false;
	IService iService;
	double Long,Lat;
	MenuOption HomeP;
	Homepage home;
	
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		super.onActionBarSetTitle(getString(R.string.anomalies));
		super.setMenuOption(MenuOption.Anomalias);
		setContentView(R.layout.activity_anom);
		
		home = new Homepage (this);
		mViewPager = (ViewPager) findViewById(R.id.activity_anomalias_viewpager);
		adapter = new AdapterFragmentAnomaliasView(getSupportFragmentManager());
		mViewPager.setAdapter(adapter);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {

				if (position == 1) {
					((FragmentTipfalha) adapter.getItem(position)).refresh();

				}
				if (position == 2) {
					((FragmentIdtlocal) adapter.getItem(position)).refresh();

				}

				Fragment f = getSupportFragmentManager().findFragmentById(R.id.activity_anomalias);

				if (f != null & f instanceof FragmentStepsAnomalias) {

					((FragmentStepsAnomalias) f).setItemPosition(position);

				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		MyApplication MyApplicationRef = (MyApplication) getApplicationContext();
		iService = MyApplicationRef.getEngineServiceRef();

	}

	public ViewPager getpager() {

		return mViewPager;
	}

	public void hideKeyboard(View view) {
		InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	@Override
	public void setTextAbt(String TextAbt) {
		this.TextAbt = TextAbt;
		Log.i(TAG, "StringAbt: " + TextAbt);

	}

	@Override
	public String getTextABT() {
		return null;
	}

	@Override
	public void setTextTdf(String TextTdf) {
		this.TextTdf = TextTdf;
		Log.i(TAG, "StringTdf: " + TextTdf);

	}

	@Override
	public String getTextTdf() {
		return null;
	}

	@Override
	public void setPositionSelected(Integer posselected) {
		this.posselected = posselected;
		Log.i(TAG, "posselected: " + posselected);

	}

	@Override
	public Integer getPositionSelected() {
		return this.posselected;
	}

	@Override
	public void setSelectTip(Integer selectTip) {
		this.selectTip = selectTip;
		Log.i(TAG, "TipFragmentACtivity: " + selectTip);
	}

	@Override
	public Integer getSelectTip() {
		return selectTip;
	}

	@Override
	public void setTextFed(String Textsrt) {
		this.Textsrt = Textsrt;
		Log.i(TAG, "setTextFed: " + Textsrt);
	}

	@Override
	public String getTextFed() {
		return Textsrt;
	}

	@Override
	public void setEnvFed(boolean Fed) {
		this.Fed = Fed;
		
		Location loc = new Location("");
		loc.setLatitude(Lat);
		loc.setLongitude(Long);
		
		iService.send_report(TextAbt, TextTdf, loc, Textsrt, null);
		Log.i(TAG, "envio final: " + Textsrt);
		Log.i(TAG, "envio final: " + TextAbt);
		Log.i(TAG, "envio final: " + TextTdf);
		Log.i(TAG, "envio final: " + Lat);
		Log.i(TAG, "envio final: " + Long);
		finish();
	}

	@Override
	public boolean getEnvFed() {
		return false;
	}

	public void onBackPressed() {
		if (slidingMenuIsOpen()) {
			onSlidingMenuToggle();
		} else {
			HomeP=home.ReadHome();
			if(HomeP==MenuOption.Anomalias){
				finish();
			}else{
			home.TesteTipe(HomeP);
			finish();
		}
		}
	}

	@Override
	public void setLong(double Long) {
		this.Long=Long;
	}

	@Override
	public Double getLong() {
		return null;
	}

	@Override
	public void setLat(double Lat) {
		this.Lat=Lat;
		
	}

	@Override
	public Double getLat() {
		return null;
	}

}
