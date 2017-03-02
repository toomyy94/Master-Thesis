package pt.ptinovacao.arqospocket.service.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;
import pt.ptinovacao.arqospocket.service.utils.MyWatchDog;
import pt.ptinovacao.arqospocket.service.utils.WacthDogInterface;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class GPSInformation implements LocationListener, WacthDogInterface {
	
	private final static Logger logger = LoggerFactory.getLogger(GPSInformation.class);
	private final static String ABNF_SEPARATOR = " ";

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
		final String method = "GPSInformation";

		try {

			myContext = c;
			mlocManager = (LocationManager) myContext
					.getSystemService(Context.LOCATION_SERVICE);
			
			updateMyLocation();
			
			// Maybe we need to stop this options, depends the battery life
			//activeLocationAutoUpdate();

		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}

	public String getFormattedLocation(){
		StringBuilder sb = new StringBuilder();
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
		otherSymbols.setDecimalSeparator('.');
		DecimalFormat df = new DecimalFormat("0.000000", otherSymbols);


		//FORMAT: latitude SP latitude_orientation SP longitude SP longitude_orientation SP speed SP direction
		//EXAMPLE: 4036.18703 N 839.86579 W 1.199000 93.020000

		try {
			Location location = myLocation;
			if (location == null) {
				MyLogger.debug(logger, "getFormattedLocation", "No location available! Getting LastKnowLocation");
				myLocation = location = getLocation();
			}

			if (location != null) {
				MyLogger.debug(logger, "getFormattedLocation", "location: " + location);
				sb.append(location.convert(location.getLatitude(), location.FORMAT_MINUTES)
						.replace("-", "")
						.replace(":", "")
						.replace(",", "."));
				sb.append(ABNF_SEPARATOR);
				sb.append(location.getLatitude() >= 0 ? "N" : "S");
				sb.append(ABNF_SEPARATOR);

				sb.append(location.convert(location.getLongitude(), location.FORMAT_MINUTES)
						.replace("-", "")
						.replace(":", "")
						.replace(",", "."));

				sb.append(ABNF_SEPARATOR);
				sb.append(location.getLongitude() >= 0 ? "E" : "W");
				sb.append(ABNF_SEPARATOR);

				sb.append(df.format(location.getSpeed() * 2.2369));
				sb.append(ABNF_SEPARATOR);

				sb.append(df.format(location.getBearing()));


			}
		} catch (Exception e){
			MyLogger.error(logger, "getFormattedLocation", e);
		}

		return sb.toString();
	}
	
	public boolean gotLocation() {

		if (myLocation != null) {
			return true;
		}
		return false;
	}
	
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
	
	public Location getLocation() {

		// update actual location
		//updateMyLocation();

		//return myLocation;
		
		LocationManager mlocManager = (LocationManager) myContext
				.getSystemService(Context.LOCATION_SERVICE);
		
		return getBestLastKnownLocation(mlocManager);
	}
	
	public String getLatitude() {
        if (myLocation == null) {
            myLocation = getLocation();
        }

        if (myLocation != null) {
			
//			return convertGausDecimaisToGPSCoordLat( myLocation.getLatitude());
			
			return myLocation.getLatitude()+"";
		} else {
			return myContext.getString(R.string.na);
		}
	}
	
	public String convertGausDecimaisToGPSCoordLat(double grausDecimal) {
		final String method = "convertGausDecimaisToGPSCoordLat";
		
		MyLogger.debug(logger, method, "grausDecimal :"+grausDecimal);
		
		String tail = null;
		
		if (grausDecimal > 0) {
			tail = myContext.getString(R.string.gps_heading_north);
		} else {
			grausDecimal = grausDecimal*(-1);
			tail = myContext.getString(R.string.gps_heading_south);
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
		
		MyLogger.debug(logger, method, "result :"+result);
		
		return result;
	}
	
	public String getLongitude() {
        if (myLocation == null) {
            myLocation = getLocation();
        }
		if (myLocation != null) {
			
//			return convertGausDecimaisToGPSCoordLong(myLocation.getLongitude());
			
			return myLocation.getLongitude()+"";
		} else {
			return myContext.getString(R.string.na);
		}
	}
	
	public String convertGausDecimaisToGPSCoordLong(double grausDecimal) {
		final String method = "convertGausDecimaisToGPSCoordLong";
		
		MyLogger.debug(logger, method, "grausDecimal :"+grausDecimal);
		
		String tail = null;
		
		if (grausDecimal > 0) {
			tail = myContext.getString(R.string.gps_heading_east);
		} else {
			grausDecimal = grausDecimal*(-1);
			tail = myContext.getString(R.string.gps_heading_west);
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
		
		MyLogger.debug(logger, method, "result :"+result);
		
		return result;
	}
	
	

	public void updateMyLocation() {
		final String method = "updateMyLocation";

		try {

			if (mlocManager != null) {
				
				
				Criteria criteria = new Criteria();
			    criteria.setAccuracy(Criteria.ACCURACY_FINE);
			    criteria.setAltitudeRequired(false);
			    criteria.setBearingRequired(false);
			    criteria.setSpeedRequired(false);
			    criteria.setPowerRequirement(Criteria.POWER_LOW);
				
			    String bestLocationProvider = mlocManager.getBestProvider(criteria, true);
			    MyLogger.debug(logger, method, "bestLocationProvider :"+bestLocationProvider);
			    
				mlocManager.requestLocationUpdates(bestLocationProvider, 0, 0, this);
				
				//mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
				
			} else {
				MyLogger.debug(logger, method, "No LocationManager available!");
			}

		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}

	public void activeLocationAutoUpdate() {
		final String method = "activeLocationAutoUpdate";
		
		try {
			watchDogUpdate = new MyWatchDog(updateTimeInMillisec, this);
			watchDogUpdate.start();
		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}

	public void stopLocationAutoUpdate() {
		final String method = "stopLocationAutoUpdate";
		
		try {
			if (watchDogUpdate != null)
				watchDogUpdate.cancelWatchDog();
		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}

	public void onLocationChanged(Location location) {
		final String method = "onLocationChanged";
		
		MyLogger.debug(logger, method, "location :" + location.toString());

		myLocation = location;
	}

	public void onProviderDisabled(String provider) {
		final String method = "onProviderDisabled";
		
		MyLogger.debug(logger, method, "provider :" + provider);
	}

	public void onProviderEnabled(String provider) {
		final String method = "onProviderEnabled";
		
		MyLogger.debug(logger, method, "provider :" + provider);
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		final String method = "onStatusChanged";
		
		MyLogger.debug(logger, method, "provider :" + provider + " status :" + status);
	}

	public void WacthDog() {
		updateMyLocation();
	}



}
