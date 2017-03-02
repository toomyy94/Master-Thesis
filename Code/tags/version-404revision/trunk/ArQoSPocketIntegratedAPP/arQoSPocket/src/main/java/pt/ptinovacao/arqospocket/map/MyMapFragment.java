/***
  Copyright (c) 2013 CommonsWare, LLC
  Licensed under the Apache License, Version 2.0 (the "License"); you may not
  use this file except in compliance with the License. You may obtain a copy
  of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
  by applicable law or agreed to in writing, software distributed under the
  License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
  OF ANY KIND, either express or implied. See the License for the specific
  language governing permissions and limitations under the License.

  From _The Busy Coder's Guide to Android Development_
    http://commonsware.com/Android
 */

package pt.ptinovacao.arqospocket.map;

import java.util.ArrayList;
import java.util.List;

import pt.ptinovacao.arqospocket.R;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MyMapFragment extends ArqOsSupportMapFragment {

    private static final String TAG = MyMapFragment.class.getCanonicalName();

    private Context context;
    private View view;
    private GoogleMap map;
    private boolean ndriveVisible;
    private LinearLayout ndriveBar;
    private Location spotLocation = null;
    private List<MarkerOptions> markersList;
    public static String tag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	tag = getTag();
	markersList = new ArrayList<MarkerOptions>();
    }

    @Override
    public void onDestroyView() {
	super.onDestroyView();
	((ViewGroup) view.getParent()).removeView(view);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view != null) 
			return view;
	
		super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.map_layout, null);
		map = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		
		return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);

	this.context = getActivity();

	if (map != null) {
	    Log.d(getClass().getSimpleName(), "Map ready for use!");
	    map.setMyLocationEnabled(true);
	    
	    map.setInfoWindowAdapter(new InfoWindowAdapter() {	    	
			// Use default InfoWindow frame
			@Override
			public View getInfoWindow(Marker m) {
			    // Getting view from the layout file info_window_layout
			    View v = getActivity().getLayoutInflater().inflate(R.layout.custom_balloon, null);
	
			    TextView titleTextView = (TextView) v.findViewById(R.id.balloon_item_title);
			    titleTextView.setText(m.getTitle());
			    
			    TextView snippetTextView = (TextView) v.findViewById(R.id.balloon_item_snippet);
			    snippetTextView.setText(m.getSnippet());		    
			    
			    return v;
			}
	
			// Defines the contents of the InfoWindow
			@Override
			public View getInfoContents(Marker arg0) {
			    return null;
			}
	    });
	    
		/* KO coordinates: 40.625854,-8.64741 */
		LatLng cur_Latlng = new LatLng(40.625854, -8.64741);
//		BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_logo_black);
		MarkerOptions markerOps = new MarkerOptions().position(cur_Latlng)
				.title("Knock-Out").snippet("Health Club");
		map.addMarker(markerOps);
		Marker marker = map.addMarker(markerOps);
		marker.showInfoWindow();
		map.moveCamera(CameraUpdateFactory.newLatLng(cur_Latlng));
		map.animateCamera(CameraUpdateFactory.zoomTo(16));

//	    new LongOperation(map).execute();

	    map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

		@Override
		public void onInfoWindowClick(Marker marker) {

//		    ndriveBar.setVisibility(View.VISIBLE);
//		    map.getUiSettings().setZoomControlsEnabled(false);
//		    ndriveVisible = true;
			if(marker.isInfoWindowShown()) {
				marker.hideInfoWindow();
			} else {
				marker.showInfoWindow();
			}
		}
	    });
//	    map.setOnMapClickListener(new OnMapClickListener() {
//
//		@Override
//		public void onMapClick(LatLng point) {
//		    if (ndriveVisible) {
//			ndriveBar.setVisibility(View.GONE);
//			map.getUiSettings().setZoomControlsEnabled(true);
//			ndriveVisible = false;
//		    }
//		}
//	    });
		    
	}

    }

    /**
     * @param query
     *            query string used to filter markers
     */
	public void filterMarkers(String query) {
		if (query != null)
			query = query.toLowerCase();

		if (map != null) {
			map.clear();
			for (MarkerOptions m : markersList)
				if (query == null || m.getTitle().toLowerCase().contains(query)
						|| m.getSnippet().toLowerCase().contains(query))
					map.addMarker(m);
		}
	}

    // ////////////////////////////////////////////////////
    // Load Points
    // ////////////////////////////////////////////////////

//    private class LongOperation extends AsyncTask<String, Void, String> {
//
//	private List<GeoPoint> listGeoPoints = new ArrayList<GeoPoint>();
//	private GoogleMap map;
//	private String line;
//
//	BufferedReader r;
//	
//
//	public LongOperation(GoogleMap map) {
//
//	    this.map = map;
//
//	}
//
//	@Override
//	protected String doInBackground(String... params) {
//
//	    AssetManager am = getActivity().getApplicationContext().getAssets();
//
//	    try {
//
//		InputStream is = am.open("pt_wifi_spots.txt");
//		r = new BufferedReader(new InputStreamReader(is, "ISO-8859-15"));
//
//	    } catch (IOException e) {
//		e.printStackTrace();
//	    }
//
//	    return "Executed";
//	}
//
//	@Override
//	protected void onPostExecute(String result) {
//
//	    GeoPoint point = null;
//
//	    try {
//
//		while ((line = r.readLine()) != null) {
//
//		    String[] linesplit = line.split(";");
//
//		    // CustomOverlayItem overlayItem3 = new
//		    // CustomOverlayItem(point, linesplit[0], linesplit[3] +
//		    // "\n" + linesplit[5], null);
//		    // itemizedOverlay2.addOverlay(overlayItem3);
//
//		    point = new GeoPoint((int) (Double.parseDouble(linesplit[1])),
//			    (int) (Double.parseDouble(linesplit[2])));
//		    listGeoPoints.add(point);
//		    
//		    MarkerOptions marker = new MarkerOptions()
//			    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_location_google_maps))
//			    .position(
//				    new LatLng(Double.parseDouble(linesplit[1]), Double
//					    .parseDouble(linesplit[2]))).title(linesplit[0])
//			    .snippet(linesplit[3]);
//		    map.addMarker(marker);
//		    markersList.add(marker);
//
//		}
//
//	    } catch (IOException e) {
//		e.printStackTrace();
//	    }
//
//	    // mapOverlays.add(itemizedOverlay2);
//
//	    if (point != null) {
//
//		LatLng zoomPoint = new LatLng(point.getLatitudeE6(), point.getLongitudeE6());
//
//		map.moveCamera(CameraUpdateFactory.newLatLngZoom(zoomPoint, 5));
//		// mc.animateTo(point);
//	    }
//
//	}
//
//	@Override
//	protected void onPreExecute() {
//	}
//
//	@Override
//	protected void onProgressUpdate(Void... values) {
//	}
//    }

    private OnClickListener ndriveListener = new OnClickListener() {

	@Override
	public void onClick(View v) {
	    android.content.pm.PackageManager mPm = context.getPackageManager(); // 1
	    PackageInfo info = null;

	    /* Checks if TMN drive application is installed in the device */
	    try {
		info = mPm.getPackageInfo("com.ndrive.androidtmndrive", 0);
	    } catch (NameNotFoundException e) {
		e.printStackTrace();
		Log.e("NDrive download", "NDrive download threw exception");
	    }
	    if (info == null) {
		downloadNDrive(context);
	    } else {
		try {
		    Intent pji = new Intent();

		    pji.setAction(Intent.ACTION_VIEW);
		    pji.setComponent(new android.content.ComponentName("com.ndrive.androidtmndrive",
			    "com.ndrive.androidtmndrive.MainActivity"));
		    pji.setData(android.net.Uri.parse("geo:" + locationToString(spotLocation)));
		    context.startActivity(pji);
		} catch (Exception aBoing) {
		    aBoing.printStackTrace();
		}
	    }

	}
    };

    private String locationToString(Location location) {

	if (spotLocation != null) {
	    return location.getLatitude() + "," + spotLocation.getLongitude();
	} else {
	    return "";
	}
    }

    public static void downloadNDrive(final Context context) {
	Intent downloadIntent = new Intent(Intent.ACTION_VIEW).setData(Uri
		.parse("https://play.google.com/store/apps/details?id=com.ndrive.androidtmndrive"));
	try {
	    context.startActivity(downloadIntent);
	    Toast.makeText(context, R.string.install_application, Toast.LENGTH_LONG).show();
	} catch (ActivityNotFoundException e) {
	    // Market app is not available in this device.
	    // Show download page of this project.
	    // try {
	    // downloadIntent.setData(Uri.parse("http://code.google.com/p/android-wifi-connecter/downloads/list"));
	    // activity.startActivity(downloadIntent);
	    // Toast.makeText(activity,
	    // "Please download the apk and install it manully.",
	    // Toast.LENGTH_LONG).show();
	    // } catch (ActivityNotFoundException e2) {
	    // // Even the Browser app is not available!!!!!
	    // // Show a error message!
	    Toast.makeText(context, R.string.error_msg_no_web_browser, Toast.LENGTH_LONG)
		    .show();
	    // }
	}
    }

}
