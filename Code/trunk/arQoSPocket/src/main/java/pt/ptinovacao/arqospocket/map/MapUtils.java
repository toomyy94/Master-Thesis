package pt.ptinovacao.arqospocket.map;

import android.location.Location;
import android.location.LocationManager;

public class MapUtils {

	public static Location getBestLastKnownLocation(LocationManager locationMgr) {		
		Location lastKnownLocation_byGps = locationMgr
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		Location lastKnownLocation_byNetwork = locationMgr
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		// Network Last Location not available
		if (lastKnownLocation_byGps == null
				&& lastKnownLocation_byNetwork != null) {

			return lastKnownLocation_byNetwork;

		}// GPS Last Location not available
		else if (lastKnownLocation_byGps != null
				&& lastKnownLocation_byNetwork == null) {
			return lastKnownLocation_byGps;

		} // Both Location provider have last location
			// decide location base on accuracy
		else if (lastKnownLocation_byGps != null
				&& lastKnownLocation_byNetwork != null) {

			if (lastKnownLocation_byGps.getAccuracy() <= lastKnownLocation_byNetwork.getAccuracy()) {
				return lastKnownLocation_byGps;
			} else {
				return lastKnownLocation_byNetwork;
			}
		} else {
			return null;
		}
	}
}
