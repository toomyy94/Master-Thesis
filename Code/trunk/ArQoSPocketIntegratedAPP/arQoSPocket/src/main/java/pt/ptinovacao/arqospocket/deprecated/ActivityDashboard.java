package pt.ptinovacao.arqospocket.deprecated;

import pt.ptinovacao.arqospocket.ArqosActivity;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.fragments.FragmentActionBar;
import pt.ptinovacao.arqospocket.util.MenuOption;
import pt.ptinovacao.arqospocket.views.GaugeView;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ActivityDashboard extends ArqosActivity
{
	private static final String EXTRA_TITLE = "title";
	ImageView btn,btn2;
	LinearLayout ll;
	FragmentActionBar actionBar;
	int screenWidth, screenHeight;
	
	ImageView wifiDetail,mobileNetworkDetail;
	
	@Override
	protected void onCreate(Bundle arg0)
	{
		super.onCreate(arg0);
		super.onActionBarSetTitle(getString(R.string.dashboard));
		super.setMenuOption(MenuOption.Dashboard);
		setContentView(R.layout.activity_dash);
		
		
		
		wifiDetail = (ImageView) findViewById(R.id.activity_dashboard_wifi_detail);
		wifiDetail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Bundle mBundle = new Bundle();
				mBundle.putString(EXTRA_TITLE, getString(R.string.wifi));
				startActivity(ActivityDashboardDetail.class,mBundle);
				
			}
		});
		mobileNetworkDetail = (ImageView) findViewById(R.id.activity_dashboard_mobile_network_detail);
		mobileNetworkDetail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Bundle mBundle = new Bundle();
				mBundle.putString(EXTRA_TITLE, getString(R.string.mobile_network));
				startActivity(ActivityDashboardDetail.class,mBundle);
				
			}
		});
		
		
		DisplayMetrics displayMetrics = new DisplayMetrics();
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(displayMetrics);
		screenWidth = displayMetrics.widthPixels;
		screenHeight = displayMetrics.heightPixels;
		
		int graphicSize = (screenWidth / 2);
		
		ll = (LinearLayout) findViewById(R.id.gaugeNeedleLay);

		btn = (ImageView) findViewById(R.id.btn1);
		final GaugeView gv = (GaugeView) findViewById(R.id.gv1);
		gv.setDiameter(graphicSize);
		gv.createNeedle(graphicSize, graphicSize, 0, -graphicSize/2);
		gv.setAngleRange(0, 225);
		gv.setNeedleAnimation(true);
		gv.setAnimationTime(250,250);
		
		btn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				gv.setCurrentAngle(gv.getCurrentAngle()+40);
			}
		});
		
		
		
		btn2 = (ImageView) findViewById(R.id.btn2);
		final GaugeView gv2 = (GaugeView) findViewById(R.id.gv2);
		gv2.setDiameter(graphicSize);
		gv2.createNeedle(graphicSize, graphicSize, 0, -graphicSize/2);
		gv2.setAngleRange(0, 180);
		gv2.setNeedleAnimation(true);
		gv2.setAnimationTime(1000,1000);
		gv2.setGaugeBackgroundResource(R.drawable.velocimetro_wi_fi);
		
		btn2.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				gv2.setCurrentAngle(gv2.getCurrentAngle()+40);
			}
		});
		
	}

	@Override
	public boolean actionBarEnabled()
	{
		return true;
	}

}
