package PT.PTInov.ArQoSPocketEDP.Service;

import PT.PTInov.ArQoSPocketEDP.Utils.LogType;
import PT.PTInov.ArQoSPocketEDP.Utils.Logger;
import PT.PTInov.ArQoSPocketEDP.Utils.MyWatchDog;
import PT.PTInov.ArQoSPocketEDP.Utils.WacthDogInterface;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class GPSInformation implements LocationListener, WacthDogInterface {

	private final static String tag = "GPSInformation";

	// Do update in 30.... 30 secs
	private final static long updateTimeInMillisec = 30000;

	// Save the last Location update
	private Location myLocation = null;
	private LocationManager mlocManager = null;

	// watchdog update
	MyWatchDog watchDogUpdate = null;

	// Application Context
	Context myContext = null;

	public GPSInformation(Context c) {

		try {

			myContext = c;
			mlocManager = (LocationManager) myContext
					.getSystemService(Context.LOCATION_SERVICE);
			
			updateMyLocation();
			
			// Maybe we need to stop this options, depends the battery life
			//activeLocationAutoUpdate();

		} catch (Exception ex) {
			Logger.v(tag, "GPSInformation", LogType.Error, ex.toString());
		}
	}
	
	public boolean gotLocation() {
		
		if (myLocation != null)
			return true;
		
		return false;
	}
	
	public Location getLocation() {

		// update actual location
		updateMyLocation();

		return myLocation;
	}
	
	public String getLatitude() {
		if (myLocation != null) {
			
			return convertGausDecimaisToGPSCoordLat( myLocation.getLatitude());
			
			//return myLocation.getLatitude()+"";
		} else {
			return "NA";
		}
	}
	
	public String convertGausDecimaisToGPSCoordLat(double grausDecimal) {
		
		Logger.v(tag, "testCoordenadas", LogType.Debug, "grausDecimal :"+grausDecimal);
		
		String tail = null;
		
		if (grausDecimal > 0) {
			tail = "N";
		} else {
			grausDecimal = grausDecimal*(-1);
			tail = "S";
		}
		
		int partIn = (int) (grausDecimal/1);
		double partFrac = grausDecimal - (partIn*1.0);
		
		double gpsmin = partFrac * 60;
		
		//Logger.v(tag, "testCoordenadas", LogType.Debug, "partIn :"+partIn);
		//Logger.v(tag, "testCoordenadas", LogType.Debug, "partFrac :"+partFrac);
		//Logger.v(tag, "testCoordenadas", LogType.Debug, "gpsmin :"+gpsmin);
		
		String gpsminString = gpsmin+"";
		if (gpsminString.contains(".") && ((gpsminString.indexOf(".")+6)<gpsminString.length())) {
			gpsminString = gpsminString.substring(0, gpsminString.indexOf(".")+6);
		}
		
		String result = partIn+""+gpsminString+" "+tail;
		
		Logger.v(tag, "testCoordenadas", LogType.Debug, "result :"+result);
		
		return result;
	}
	
	public String getLongitude() {
		if (myLocation != null) {
			
			return convertGausDecimaisToGPSCoordLong(myLocation.getLongitude());
			
			//return myLocation.getLongitude()+"";
		} else {
			return "NA";
		}
	}
	
	public String convertGausDecimaisToGPSCoordLong(double grausDecimal) {
		
		Logger.v(tag, "testCoordenadas", LogType.Debug, "grausDecimal :"+grausDecimal);
		
		String tail = null;
		
		if (grausDecimal > 0) {
			tail = "E";
		} else {
			grausDecimal = grausDecimal*(-1);
			tail = "W";
		}
		
		int partIn = (int) (grausDecimal/1);
		double partFrac = grausDecimal - partIn;
		
		double gpsmin = partFrac * 60;
		
		//Logger.v(tag, "testCoordenadas", LogType.Debug, "partIn :"+partIn);
		//Logger.v(tag, "testCoordenadas", LogType.Debug, "partFrac :"+partFrac);
		//Logger.v(tag, "testCoordenadas", LogType.Debug, "gpsmin :"+gpsmin);
		
		String gpsminString = gpsmin+"";
		if (gpsminString.contains(".") && ((gpsminString.indexOf(".")+6)<gpsminString.length())) {
			gpsminString = gpsminString.substring(0, gpsminString.indexOf(".")+6);
		}
			
		String result = partIn+""+gpsminString+" "+tail;
		
		Logger.v(tag, "testCoordenadas", LogType.Debug, "result :"+result);
		
		return result;
	}
	
	

	public void updateMyLocation() {

		try {

			if (mlocManager != null) {
				
				
				Criteria criteria = new Criteria();
			    criteria.setAccuracy(Criteria.ACCURACY_FINE);
			    criteria.setAltitudeRequired(false);
			    criteria.setBearingRequired(false);
			    criteria.setSpeedRequired(false);
			    criteria.setPowerRequirement(Criteria.POWER_LOW);
				
			    String bestLocationProvider = mlocManager.getBestProvider(criteria, true);
			    Logger.v(tag, "updateMyLocation", LogType.Debug, "bestLocationProvider :"+bestLocationProvider);
			    
				mlocManager.requestLocationUpdates(bestLocationProvider, 0, 0, this);
				
				//mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
				
			} else {
				Logger.v(tag, "updateMyLocation", LogType.Debug,
						"No LocationManager available!");
			}

		} catch (Exception ex) {
			Logger.v(tag, "updateMyLocation", LogType.Error, ex.toString());
		}
	}

	public void activeLocationAutoUpdate() {
		try {
			watchDogUpdate = new MyWatchDog(updateTimeInMillisec, this);
			watchDogUpdate.start();
		} catch (Exception ex) {
			Logger.v(tag, "activeLocationAutoUpdate", LogType.Error,
					ex.toString());
		}
	}

	public void stopLocationAutoUpdate() {
		try {
			if (watchDogUpdate != null)
				watchDogUpdate.cancelWatchDog();
		} catch (Exception ex) {
			Logger.v(tag, "stopLocationAutoUpdate", LogType.Error,
					ex.toString());
		}
	}

	public void onLocationChanged(Location location) {

		Logger.v(tag, "onLocationChanged", LogType.Trace, "location :"
				+ location.toString());

		myLocation = location;
	}

	public void onProviderDisabled(String provider) {
		Logger.v(tag, "onProviderDisabled", LogType.Trace, "provider :"
				+ provider);
	}

	public void onProviderEnabled(String provider) {
		Logger.v(tag, "onProviderEnabled", LogType.Trace, "provider :"
				+ provider);

	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		Logger.v(tag, "onStatusChanged", LogType.Trace, "provider :" + provider
				+ " status :" + status);

	}

	public void WacthDog() {
		updateMyLocation();
	}

}
