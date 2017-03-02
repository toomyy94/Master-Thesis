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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ArQosMapFragment extends ArqOsSupportMapFragment {

    private static final String TAG = ArQosMapFragment.class.getCanonicalName();

    private Context context;
    private View view;
    private GoogleMap map;
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
	    
//	    map.setInfoWindowAdapter(new InfoWindowAdapter() {
//			// Use default InfoWindow frame
//			@Override
//			public View getInfoWindow(Marker m) {
//			    // Getting view from the layout file info_window_layout
//			    View v = getActivity().getLayoutInflater().inflate(R.layout.custom_balloon, null);
//	
//			    TextView titleTextView = (TextView) v.findViewById(R.id.balloon_item_title);
//			    titleTextView.setText(m.getTitle());
//			    
//			    TextView snippetTextView = (TextView) v.findViewById(R.id.balloon_item_snippet);
//			    snippetTextView.setText(m.getSnippet());		    
//			    
//			    return v;
//			}
//	
//			// Defines the contents of the InfoWindow
//			@Override
//			public View getInfoContents(Marker arg0) {
//			    return null;
//			}
//	    });
	    
		/* KO coordinates: 40.625854,-8.64741 */
		LatLng cur_Latlng = new LatLng(40.625854, -8.64741);
		BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pin_mapa_voz);
		MarkerOptions markerOps = new MarkerOptions().position(cur_Latlng)
//				.title("Knock-Out").snippet("Health Club")
				.icon(icon);
		map.addMarker(markerOps);
		Marker marker = map.addMarker(markerOps);
		marker.showInfoWindow();
		map.moveCamera(CameraUpdateFactory.newLatLng(cur_Latlng));
		map.animateCamera(CameraUpdateFactory.zoomTo(10));

//	    new LongOperation(map).execute();

	    map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

		@Override
		public void onInfoWindowClick(Marker marker) {
			if(marker.isInfoWindowShown()) {
				marker.hideInfoWindow();
			} else {
				marker.showInfoWindow();
			}
		}
	    });
		    
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

    private String locationToString(Location location) {

	if (spotLocation != null) {
	    return location.getLatitude() + "," + spotLocation.getLongitude();
	} else {
	    return "";
	}
    }

}
