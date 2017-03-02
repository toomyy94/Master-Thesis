package pt.ptinovacao.arqospocket.fragments;


import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.activities.ActivityAnomalias;
import pt.ptinovacao.arqospocket.map.MapUtils;
import pt.ptinovacao.arqospocket.util.I_OnDataPass;
import pt.ptinovacao.arqospocket.util.Utils;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;

public class FragmentIdtlocal extends Fragment {
	
	int selectTip, posselected;
	TextView textlogoIdt;
	ImageView logoIdt;
	private Fragment activity_anomalias;
	private View view;
	private MapView mapView;
	private static GoogleMap map;

	private boolean mapStretched;
	private LinearLayout occurrencesLayout;
	private ImageView mapZoomer;
	static ActivityAnomalias activity;
	public static final Integer[] Voz = { R.string.anomaly_type_dropped_call,
			R.string.anomaly_type_call_not_established, R.string.anomaly_type_lost_call, R.string.anomaly_type_bad_call_audio,
			R.string.anomaly_type_another_anomaly_type};

	public static final Integer[] Internet = { R.string.anomaly_type_no_data_access,
			R.string.anomaly_type_intermittent_access, R.string.anomaly_type_slow_connection, R.string.anomaly_type_another_anomaly_type};

	public static final Integer[] Messaging = { R.string.anomaly_type_message_not_sent,
			R.string.anomaly_type_message_not_received, R.string.anomaly_type_message_slow_dispatch, R.string.anomaly_type_message_delayed_recepetion,
			R.string.anomaly_type_another_anomaly_type};

	public static final Integer[] Cobertura = { R.string.anomaly_type_no_indoor_coverage,
			R.string.anomaly_type_no_outdoor_coverage};

	public static final Integer[] Outra = { R.string.anomaly_type_another_anomaly_type};

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_idtlocal, container, false);
		// view_activity = inflater.inflate(R.layout.activity_anom, container,
		// false);
		textlogoIdt = (TextView) view.findViewById(R.id.textIdt);
		logoIdt = (ImageView) view.findViewById(R.id.imageIdt);
		activity = (ActivityAnomalias) getActivity();
		occurrencesLayout = (LinearLayout) view
				.findViewById(R.id.occurrencesLayout);
		activity_anomalias = getFragmentManager().findFragmentById(
				R.id.activity_anomalias);
		LogoImg_Name(posselected, selectTip);
		
		mapZoomer = (ImageView) view.findViewById(R.id.icon_zoom_mapa);
		mapZoomer.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				LatLng center = map.getCameraPosition().target;
				Log.i("Map center", "Lat: " + center.latitude + "\nLong: "
						+ center.longitude);
				((I_OnDataPass) activity).setLong(center.longitude);
				((I_OnDataPass) activity).setLat(center.latitude);
				
				mapStretched = !mapStretched;

				if (mapStretched) {
					occurrencesLayout.setVisibility(View.GONE);
					activity_anomalias.getView().setVisibility(View.GONE);
				} else {
					occurrencesLayout.setVisibility(View.VISIBLE);
					activity_anomalias.getView().setVisibility(View.VISIBLE);
				}

			}

		});

		mapView = (MapView) view.findViewById(R.id.mapview);
		mapView.onCreate(savedInstanceState);

		map = mapView.getMap();
		
		if(map != null) {
			map.getUiSettings().setMyLocationButtonEnabled(true);
			map.setMyLocationEnabled(true);
		
			// Needs to call MapsInitializer before doing any CameraUpdateFactory
			// calls
			MapsInitializer.initialize(this.getActivity());
			
			/* Location manager */
			LocationManager locationMgr = 
			          (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
			
			/* Get best last known location */
			Location lastKnownLocation = MapUtils.getBestLastKnownLocation(locationMgr);
			if(lastKnownLocation != null) {
				/* Center map on best last known location */
				//por vezes da erro no nexus5
				LatLng cur_Latlng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
				CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(cur_Latlng, Utils.ZOOM_VALUE);
				map.animateCamera(cameraUpdate);				
				
//				/* Register listener to receive location updates */
//				map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
//					@Override
//					public void onMyLocationChange(Location arg0) {
//						/* Center map on updated current location */
//						LatLng cur_Latlng = new LatLng(arg0.getLatitude(), arg0.getLongitude());
//						CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(cur_Latlng, Utils.ZOOM_VALUE);
//						map.animateCamera(cameraUpdate);
//					}
//				});
			}
		}


		return view;
	}
		

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);


	}

	@Override
	public void onResume() {
		mapView.onResume();
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mapView.onLowMemory();
	}

	public void LogoImg_Name(int position, int name) {
		switch (position) {
		case 0:
			logoIdt.setImageResource(R.drawable.icon_menu_voz);

			textlogoIdt.setText(Voz[name]);

			break;
		case 1:
			logoIdt.setImageResource(R.drawable.icon_menu_internet);

			textlogoIdt.setText(Internet[name]);

			break;
		case 2:
			logoIdt.setImageResource(R.drawable.icon_menu_messaging);

			textlogoIdt.setText(Messaging[name]);

			break;
		case 3:
			logoIdt.setImageResource(R.drawable.icon_menu_cobertura);

			textlogoIdt.setText(Cobertura[name]);

			break;
		case 4:
			logoIdt.setImageResource(R.drawable.icon_menu_outra);

			textlogoIdt.setText(Outra[name]);

			break;
		default:

			break;

		}
	}

	public void refresh() {

		posselected = ((I_OnDataPass) getActivity()).getPositionSelected();
		selectTip = ((I_OnDataPass) getActivity()).getSelectTip();
		LogoImg_Name(posselected, selectTip);
		
		
	}

	public static void Localizacao() {
		if(map != null) {
			LatLng center = map.getCameraPosition().target;
			Log.i("Map center", "Lat: " + center.latitude + "\nLong: "
					+ center.longitude);
			((I_OnDataPass) activity).setLong(center.longitude);
			((I_OnDataPass) activity).setLat(center.latitude);
		}
	}


}
