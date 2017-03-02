package pt.ptinovacao.arqospocket.service.structs;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.logs.MyLogger;
import pt.ptinovacao.arqospocket.service.service.GPSInformation;

public class MyLocation implements Serializable{
	
	private final static Logger logger = LoggerFactory.getLogger(MyLocation.class);
	
	private double lat;
	private double lgt;
	
	public MyLocation(GPSInformation gps_information) {
		final String method = "MyLocation";
		
		try {
			
			MyLogger.trace(logger, method, "MyLocation(GPSInformation gps_information)");
			
			String latitude = gps_information.getLatitude();
			String longitude = gps_information.getLongitude();
			
			StringBuilder sblatitude = new StringBuilder();
			StringBuilder sblongitude = new StringBuilder();
			
			for (char c :latitude.toCharArray())
				if (Character.isDigit(c) || c=='.' || c=='-')
					sblatitude.append(c);
			
			for (char c :longitude.toCharArray())
				if (Character.isDigit(c) || c=='.' || c=='-')
					sblongitude.append(c);
			
			MyLogger.trace(logger, method, "sblatitude.toString() :"+sblatitude.toString());
			MyLogger.trace(logger, method, "sblongitude.toString() :"+sblongitude.toString());
			
			this.lat = Double.parseDouble(sblatitude.toString());
			this.lgt = Double.parseDouble(sblongitude.toString());
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	public MyLocation(double lat, double lgt) {
		final String method = "MyLocation";
		
		try {
			
			MyLogger.trace(logger, method, "MyLocation(double lat, double lgt)");
			
			MyLogger.trace(logger, method, "lat :"+lat);
			MyLogger.trace(logger, method, "lgt :"+lgt);
			
			this.lat = lat;
			this.lgt = lgt;
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}

	public double get_lat() {
		return lat;
	}
	
	public double get_lgt() {
		return lgt;
	}
	
	public String toString() {
		final String method = "toString";
		
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("\nlat :"+lat);
			sb.append("\nlgt :"+lgt);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	}
	
	public Object clone() {
		final String method = "MyLocation";
		
		try {
			
			return new MyLocation(lat, lgt);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}
}
