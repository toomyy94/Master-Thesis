package pt.ptinovacao.arqospocket.activities;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.adapters.AdapterDashboardPager;
import pt.ptinovacao.arqospocket.service.enums.EMobileNetworkMode;
import pt.ptinovacao.arqospocket.service.enums.EMobileState;
import pt.ptinovacao.arqospocket.service.interfaces.IService;
import pt.ptinovacao.arqospocket.service.interfaces.IUI;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;
import pt.ptinovacao.arqospocket.ArqosActivity;
import pt.ptinovacao.arqospocket.MyApplication;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.fragments.FragmentDashboardDetailMobile;
import pt.ptinovacao.arqospocket.fragments.FragmentDashboardDetailWifi;
import pt.ptinovacao.arqospocket.interfaces.IUpdateUI;
import pt.ptinovacao.arqospocket.util.ApplicationController;
import pt.ptinovacao.arqospocket.util.Homepage;
import pt.ptinovacao.arqospocket.util.MenuOption;
import pt.ptinovacao.arqospocket.util.MyObject;
import pt.ptinovacao.arqospocket.util.Utils;
import pt.ptinovacao.arqospocket.views.DashboardViewPager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class ActivityDashboardMain extends ArqosActivity
{
	private final static Logger logger = LoggerFactory.getLogger(ActivityDashboardMain.class);

	public static final int PAGE_NUMBER = 3;

	public DashboardViewPager mViewPager;
	private AdapterDashboardPager mPageAdater;
	boolean Move=false;
	int screenHeight;
	String sTitle;
	boolean isMobileNetwork = true;
	MenuOption HomeP;
	Homepage home;
	private ArrayList<MyObject> adapterItems;
	boolean down = false;
	
	private ConnectivityManager connManager;
	private boolean is3gOn, isWifiOn;
	
	/* Wi-Fi info */
	String link_speed;
	String ssid;
	String rx_level_wifi;
	String channel;
	/* Wi-Fi params */
	TreeMap<String, String> wifiParams;
	
	/* Mobile info */
	EMobileState mobile_state;
	EMobileNetworkMode network_mode;
	String operator_code;
	String rx_level_mobile;
	String cid;
	String lac;
	/* Mobile params */
	TreeMap<String, String> mobileParams;

	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			if (hasConnectionsStateChanged())
				updateUI();
		}
	};
	
	private List<IUpdateUI> update_dashboard_information_listeners = null;
	private IService iService;

	public void onCreate(Bundle savedInstance)
	{
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_dashboard_main);
		super.setMenuOption(MenuOption.Dashboard);
		setActionBar(getString(R.string.dashboard));
		sTitle = getString(R.string.mobile_network);
		home = new Homepage (this);
		
		MyApplication MyApplicationRef = (MyApplication) getApplicationContext();
		iService = MyApplicationRef.getEngineServiceRef();
		
		DisplayMetrics displayMetrics = new DisplayMetrics();
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(displayMetrics);
		screenHeight = displayMetrics.heightPixels;

		connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		
		updateConnectionsState();
		
		//registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
				
		update_dashboard_information_listeners = new ArrayList<IUpdateUI>();
		
		adapterItems = new ArrayList<MyObject>();
		adapterItems.add(new MyObject(Utils.DASH_ID));
		
		/* Check 3G and Wi-Fi before adding a "frag token" */
		if(is3gOn || isWifiOn) {		
			if(is3gOn) {
				adapterItems.add(new MyObject(Utils.NETWORK_DASH_ID));
			} else {
				if(isWifiOn) {
					adapterItems.add(new MyObject(Utils.WIFI_DASH_ID));
				}
			}
		}

		//THIS IS A TRY!!!
        //
        //GET (try!)
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://httpbin.org/ip";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("GET Response: ",response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Get error!","That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        //END OF GET (try!)
        SystemClock.sleep(1000);
        ////////////////////////////
        //
		//POST
//		final String URL = "http://putsreq.com/ZQ92fvizkw9g6EAUaOtm";
//		// Post params to be sent to the server
//		HashMap<String, String> params = new HashMap<String, String>();
//		params.put("name", "hello");
////		params.put("longitude", 8.2);
////		params.put("radius", 5.0);
//
//		JsonObjectRequest req = new JsonObjectRequest(URL, new JSONObject(params),
//				new Response.Listener<JSONObject>() {
//					@Override
//					public void onResponse(JSONObject response) {
//						Log.d("GET Response: ","estou aqui");
//						Log.d("GET Response: ",""+response.toString());
//					}
//				}, new Response.ErrorListener() {
//			@Override
//			public void onErrorResponse(VolleyError error) {
//
//				Log.d("GET Response: ","erro aqui");
//				VolleyLog.e("Error: ", error.getMessage());
//			}
//		});
//
//		// add the request object to the queue to be executed
//		ApplicationController.getInstance().addToRequestQueue(req);
		//END OF POST
		/////////-//////-////////
        //
		//WRITE LOGFILE
		try {
			Process process = Runtime.getRuntime().exec("logcat -d");
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(process.getInputStream()));

			StringBuilder log = new StringBuilder();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				log.append(line);
			}

            saveLogcatToFile(getApplicationContext());
		} catch (IOException e) {
		}


	///////////////-///////-//////////

		
		final OnTouchListener tListener = new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				if(is3gOn || isWifiOn) {
					if (event.getAction() == MotionEvent.ACTION_DOWN 
							&& mViewPager.getCurrentItem() == 0) {
//						MyObject item = adapterItems.get(1);
						down = true;
						
						if (event.getY() < (screenHeight / 2))
						{
							if(is3gOn) {
								sTitle = getString(R.string.mobile_network);
								isMobileNetwork = true;
								
								if(adapterItems.size() > 1) {
									MyObject item = adapterItems.get(1);
									if(item.getUniqueId() != Utils.NETWORK_DASH_ID) {
										adapterItems.remove(item);
										adapterItems.add(new MyObject(Utils.NETWORK_DASH_ID));
										mPageAdater.notifyDataSetChanged();
									}
								} else {
									adapterItems.add(new MyObject(Utils.NETWORK_DASH_ID));
									mPageAdater.notifyDataSetChanged();
								}
							} else if(isWifiOn) {
								sTitle = getString(R.string.wifi);
								isMobileNetwork = false;
								
								if(adapterItems.size() > 1) {
									MyObject item = adapterItems.get(1);
									if(item.getUniqueId() != Utils.WIFI_DASH_ID) {
										adapterItems.remove(item);
										adapterItems.add(new MyObject(Utils.WIFI_DASH_ID));
										mPageAdater.notifyDataSetChanged();
									}
								} else {
									adapterItems.add(new MyObject(Utils.WIFI_DASH_ID));
									mPageAdater.notifyDataSetChanged();
								}
							}
						} else {
							if(isWifiOn) {
								sTitle = getString(R.string.wifi);
								isMobileNetwork = false;
								
								if(adapterItems.size() > 1) {
									MyObject item = adapterItems.get(1);
									if(item.getUniqueId() != Utils.WIFI_DASH_ID) {
										adapterItems.remove(item);
										adapterItems.add(new MyObject(Utils.WIFI_DASH_ID));
										mPageAdater.notifyDataSetChanged();
									}
								} else {
									adapterItems.add(new MyObject(Utils.NETWORK_DASH_ID));
									mPageAdater.notifyDataSetChanged();
								}
							} else if(is3gOn) {
								sTitle = getString(R.string.mobile_network);
								isMobileNetwork = true;
								
								if(adapterItems.size() > 1) {
									MyObject item = adapterItems.get(1);
									if(item.getUniqueId() != Utils.NETWORK_DASH_ID) {
										adapterItems.remove(item);
										adapterItems.add(new MyObject(Utils.NETWORK_DASH_ID));
										mPageAdater.notifyDataSetChanged();
									}
								} else {
									adapterItems.add(new MyObject(Utils.WIFI_DASH_ID));
									mPageAdater.notifyDataSetChanged();
								}
							}
						}
					}
					
					if ((event.getAction() == MotionEvent.ACTION_MOVE 
							|| event.getAction() == MotionEvent.ACTION_UP) && mViewPager.getCurrentItem() == 0)
					{
						if(event.getAction() == MotionEvent.ACTION_MOVE) {
							Move = true;
							down = false;
							if (event.getY() < (screenHeight / 2))
							{
								if(is3gOn) {
									sTitle = getString(R.string.mobile_network);
									isMobileNetwork = true;
		//							mPageAdater.notifyDataSetChanged();
									
									if(!down) {
										if(adapterItems.size() > 1) {
											MyObject item = adapterItems.get(1);
											if(item.getUniqueId() != Utils.NETWORK_DASH_ID) {
												adapterItems.remove(item);
												adapterItems.add(new MyObject(Utils.NETWORK_DASH_ID));
												mPageAdater.notifyDataSetChanged();
											}
										} else {
											adapterItems.add(new MyObject(Utils.NETWORK_DASH_ID));
											mPageAdater.notifyDataSetChanged();
										}
									}
								}
								
							} else {
								if(isWifiOn) {
									sTitle = getString(R.string.wifi);
									isMobileNetwork = false;
		//							mPageAdater.notifyDataSetChanged();
									
									if(!down) {
										if(adapterItems.size() > 1) {
											MyObject item = adapterItems.get(1);
											if(item.getUniqueId() != Utils.WIFI_DASH_ID) {
												adapterItems.remove(item);
												adapterItems.add(new MyObject(Utils.WIFI_DASH_ID));
												mPageAdater.notifyDataSetChanged();
											}
										} else {
											adapterItems.add(new MyObject(Utils.WIFI_DASH_ID));
											mPageAdater.notifyDataSetChanged();
										}
									}
								}
							}
						}
						
						if (event.getAction() == MotionEvent.ACTION_UP
								&& mViewPager.getCurrentItem() == 0) {
							if (event.getY() < (screenHeight / 2)) {
								if(is3gOn)
									sTitle = getString(R.string.mobile_network);
								else
									down = false;
							} else {
								if(isWifiOn)
									sTitle = getString(R.string.wifi);
								else
									down = false;
							}
							
							if(down) {
								mViewPager.setCurrentItem(1);
								setActionBar(sTitle);
							}
							
							down = false;
							Move = false;
						}
					}
				}

				return false;	
			}			
		};
		
		mViewPager = (DashboardViewPager) findViewById(R.id.activity_dashboard_main_viewpaper);
		mViewPager.setOffscreenPageLimit(2);
		mPageAdater = new AdapterDashboardPager(getSupportFragmentManager(), isMobileNetwork, adapterItems);
		mViewPager.setAdapter(mPageAdater);

		mViewPager.setOnTouchListener(tListener);

		mViewPager.setOnPageChangeListener(new OnPageChangeListener()
		{

			@Override
			public void onPageSelected(int position)
			{
				if (position == 0)
				{
//					Move = false;
					setActionBar(getString(R.string.dashboard));
//					mViewPager.setPagingEnabled(true);
					
					/* Leave only dashboard main page in pageviewer */
					if(!is3gOn && !isWifiOn && adapterItems.size() > 1) {
						adapterItems.remove(1);
					}
				}
				else {
					try {
						if (isMobileNetwork) {
							if(is3gOn)
								((FragmentDashboardDetailMobile) mPageAdater.getItem(1)).getCurrentValues();
							else if(isWifiOn)
								((FragmentDashboardDetailWifi) mPageAdater.getItem(1)).getCurrentValues();
	//						mViewPager.setCurrentItem(1);
	//						mViewPager.setPagingEnabled(false);
						}
						else {
							if(isWifiOn)
								((FragmentDashboardDetailWifi) mPageAdater.getItem(1)).getCurrentValues();
							else if(is3gOn)
								((FragmentDashboardDetailMobile) mPageAdater.getItem(1)).getCurrentValues();
	//						mViewPager.setCurrentItem(2);
	//						mViewPager.setPagingEnabled(false);
						}
						setActionBar(sTitle);
					} catch(ClassCastException cce) {
						cce.printStackTrace();
						mViewPager.setCurrentItem(0);
					} catch(Exception e) {
						e.printStackTrace();
					}
				}

			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
			{
//				Log.d("arqos", "POSITION: " + position + "   OFFSET" + positionOffset);
//				if (position == 1 && positionOffset == 0.66F)
//				{
//					mViewPager.setCurrentItem(0);
//				}

			}

			@Override
			public void onPageScrollStateChanged(int arg0)
			{

			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(broadcastReceiver);
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
	}

	protected void updateUI() {
		/*
		 * Checks whether the current fragment is a details one, and if so,
		 * checks if it is to be updated or not.
		 */
		updateDetails();
		
		final String method = "updateUI";
		for (IUpdateUI ref : update_dashboard_information_listeners) {
			try {
				ref.updateUI();
			} catch (Exception ex) {
				MyLogger.error(logger, method, ex);
			}
		}
	}

	private void updateConnectionsState() {  
		// Mobile check
//		is3gOn = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
//				.isAvailable();
		is3gOn = iService.isMobileAvailable();
		// Wi-Fi check
//		isWifiOn = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
//				.isConnectedOrConnecting();
		isWifiOn = iService.isWiFiAvailable();
	}
	
	/**
	 * Checks whether the state of MOBILE and WIFI connections changed or not.
	 * 
	 * @return {@code true} if the state of at least a connection changed,
	 *         {@code false} if none has changed.
	 * */
	private boolean hasConnectionsStateChanged() {
		boolean prev3gState = is3gOn, prevWiFiState = isWifiOn;
		
		updateConnectionsState();
		
		return !((prev3gState == is3gOn) && (prevWiFiState == isWifiOn));
	}
	
	public boolean registry_update_dashboard_information(IUpdateUI ui_ref) {
		final String method = "registry_update_dashboard_information";
		
		try {
			 return update_dashboard_information_listeners.add(ui_ref);
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return false;
	}
	
	public boolean remove_registry_dashboard_mobile_information(IUpdateUI ui_ref) {
		final String method = "remove_registry_dashboard_mobile_information";
		
		try {
			return update_dashboard_information_listeners.remove(ui_ref);
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return false;
	}
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK && mViewPager.getCurrentItem() != 0)
		{
			mViewPager.setCurrentItem(0, true);
			down = false;
			Move = false;
			return true;
		}
		else
		{
			return super.onKeyDown(keyCode, event);
		}
	}
	
	public void goToDashboardMainPage() {
		mViewPager.setCurrentItem(0);
//		setActionBar("Dashboard");
	}
	
	public void goToDashboardDetailsPage(long detailsPageId) {
		goToDashboardDetailsPage(detailsPageId == Utils.NETWORK_DASH_ID);
	}
	
	public void goToDashboardDetailsPage(boolean isNetwork) {
		if (isNetwork) {
			sTitle = getString(R.string.mobile_network);
			isMobileNetwork = true;

			if(adapterItems.size() > 1) {
				MyObject item = adapterItems.get(1);
				if (item.getUniqueId() != Utils.NETWORK_DASH_ID) {
					adapterItems.remove(item);
					adapterItems.add(new MyObject(Utils.NETWORK_DASH_ID));
					mPageAdater.notifyDataSetChanged();
				}
			} else {
				adapterItems.add(new MyObject(Utils.NETWORK_DASH_ID));
				mPageAdater.notifyDataSetChanged();
			}
			
		} else {
			sTitle = getString(R.string.wifi);
			isMobileNetwork = false;

			if(adapterItems.size() > 1) {
				MyObject item = adapterItems.get(1);
				if (item.getUniqueId() != Utils.WIFI_DASH_ID) {
					adapterItems.remove(item);
					adapterItems.add(new MyObject(Utils.WIFI_DASH_ID));
					mPageAdater.notifyDataSetChanged();
				}
			} else {
				adapterItems.add(new MyObject(Utils.WIFI_DASH_ID));
				mPageAdater.notifyDataSetChanged();
			}
		}
		
		mViewPager.setCurrentItem(1);
		setActionBar(sTitle);
	}

	/**
	 * Checks whether the current fragment is a details one, and if so,
	 * checks if it is to be updated or not.
	 */
	public void updateDetails() {
		boolean updatePage = false;
		if(adapterItems.size() > 1) {
			MyObject item = adapterItems.get(1);
			/* Check if current fragment is not "FragmentDashboard" */
			if(item.getUniqueId() != Utils.DASH_ID) {
				/* if mobile network is off */
				if(!is3gOn) {
					/* if current (displaying) fragment is
					 *  "FragmentDashboardDetailMobile" */
					if (item.getUniqueId() == Utils.NETWORK_DASH_ID) {
						adapterItems.remove(item);
						mPageAdater.notifyDataSetChanged();
						updatePage = true;
					}
				}
				/* if mobile network is off */
				if(!isWifiOn && !updatePage) {
					/* if current (displaying) fragment is
					 *  "FragmentDashboardDetailWifi" */
					if (item.getUniqueId() == Utils.WIFI_DASH_ID) {
						adapterItems.remove(item);
						mPageAdater.notifyDataSetChanged();
						updatePage = true;
					}
				}
			}
		}
		
		/* If 'updatePage', set current (displaying) fragment to
		 *  "FragmentDashboard" */
		if(updatePage)
			mViewPager.setCurrentItem(0);
		
		return;
	}
	
	public void setActionBar(String title)
	{
		super.onActionBarSetTitle(title);
	}

//	public void setCurrentMobileValues(String link_speed, String ssid,
//			String rx_level, String channel) {
//		this.link_speed = link_speed;
//		this.ssid = ssid;
//		this.rx_level_wifi = rx_level;
//		this.channel = channel;
//	}
//	
//	public void setWithCurrentWifiValues(EMobileState mobile_state,
//			EMobileNetworkMode network_mode, String operator_code,
//			String rx_level, String cid, String lac) {
//		this.mobile_state = mobile_state;
//		this.network_mode = network_mode;
//		this.operator_code = operator_code;
//		this.rx_level_mobile = rx_level;
//		this.cid = cid;
//		this.lac = lac;
//	}
			
	public boolean hasMobileInfo() {
		return mobile_state != null && rx_level_mobile != null;
	}
	
	public boolean is3gOn() {
		return is3gOn;
	}

	public void set3gOn(boolean is3gOn) {
		this.is3gOn = is3gOn;
	}

	public boolean isWifiOn() {
		return isWifiOn;
	}

	public void setWifiOn(boolean isWifiOn) {
		this.isWifiOn = isWifiOn;
	}

	public void getCurrentMobileInfo(IUI iui) {
		iui.update_mobile_information(mobile_state, network_mode, operator_code, rx_level_mobile, cid, lac);
	}
	
	public boolean hasWifiInfo() {
		return link_speed != null && ssid != null && rx_level_wifi != null;
	}
	
	public void getCurrentWifiInfo(IUI iui) {
		iui.update_wifi_information(link_speed, ssid, rx_level_wifi, channel);
	}
	
	public boolean hasMobileParams() {
		return mobileParams != null;
	}
	
	public void getCurrentMobileParams(IUI iui) {
		iui.update_mobile_params(mobileParams);
	}
	
	public boolean hasWifiParams() {
		return wifiParams != null;
	}
	
	public void getCurrentWifiParams(IUI iui) {
		iui.update_wifi_params(wifiParams);
	}

	public void setMobileInformation(EMobileState mobile_state,
			EMobileNetworkMode network_mode, String operator_code,
			String rx_level, String cid, String lac) {
		this.mobile_state = mobile_state;
		this.network_mode = network_mode;
		this.operator_code = operator_code;
		this.rx_level_mobile = rx_level;
		this.cid = cid;
		this.lac = lac;
	}

	public void setWifiInformation(String link_speed, String ssid,
			String rx_level, String channel) {
		this.link_speed = link_speed;
		this.ssid = ssid;
		this.rx_level_wifi = rx_level;
		this.channel = channel;
	}

	public void setMobileParams(TreeMap<String, String> keyValueParams) {
		this.mobileParams = keyValueParams;		
	}

	public void setWifiParams(TreeMap<String, String> keyValueParams) {
		this.wifiParams = keyValueParams;
		
	}
	public void onBackPressed() {
		if (slidingMenuIsOpen()) {
			onSlidingMenuToggle();
		} else {
			HomeP=home.ReadHome();
			if(HomeP==MenuOption.Dashboard){
				finish();
			}else{
			home.TesteTipe(HomeP);
			finish();
		}
		}
	}

    public static void saveLogcatToFile(Context context) {
        String fileName = "logcat_"+System.currentTimeMillis()+".txt";
        File outputFile = new File(context.getExternalCacheDir(),fileName);

        try{
            Process process = Runtime.getRuntime().exec("logcat -f "+outputFile.getAbsolutePath());
        }
        catch (IOException e){
            Log.e("erro","erro saving log");
        }
    }


}
