package PT.PTIN.ArQoSPocketPTWiFi.Services;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.TreeMap;

import PT.PTIN.ArQoSPocketPTWiFi.Logs.LogType;
import PT.PTIN.ArQoSPocketPTWiFi.Logs.Logger;
import PT.PTIN.ArQoSPocketPTWiFi.Utils.PTWiFiAuthResult;
import android.content.Context;
import android.net.wifi.WifiManager;

public class AuthPTWiFI {

	private final static String tag = "AuthPTWiFI";
	
	private WifiManager wifi = null;
	private Context serviceContext = null;
	
	private static final String username = "ngin";
	private static final String password = "ngin";
	private static final String linkAuthPTWiFi = "https://hotspot.ptwifi.pt/login?lg=pt";
	private static final String linkPTWiFi = "https://hotspot.ptwifi.pt/home?lg=pt";
	
	private static final int CONNECTION_TIMEOUT = 5000;
	
	public AuthPTWiFI(Context c) {
		Logger.v(tag, LogType.Trace,"AuthPTWiFI :: Creat a instance of ManageWiFiConnection");
		
		try {
			
			serviceContext = c;
			
		} catch(Exception ex) {
			Logger.v(tag, LogType.Error,"AuthPTWiFI :: Error :"+ex.toString());
		}
	}
	
	public PTWiFiAuthResult DoAuthPTWIFI() {
		
		PTWiFiAuthResult result = new PTWiFiAuthResult();
		
		// Faz o POST de autenticação
        TreeMap<String,String> params = new TreeMap<String,String>();
        params.put("username", username);
        params.put("password", password);
        params.put("cpurl", "");
        params.put("lg", "pt");
        params.put("id_check_b", "id_check_b");
        
        long PostStartTime = System.currentTimeMillis();
        String postResult = doPost(linkAuthPTWiFi,params);
        long PostEndTime = System.currentTimeMillis();
        
        result.setPostResponseTime(PostEndTime - PostStartTime);
        
		Logger.v(tag, LogType.Error,"DoAuthPTWIFI :: POST Result :"+postResult);
		
		long GetStartTime = System.currentTimeMillis();
		String getResult = doGET(linkPTWiFi);
		long GetEndTime = System.currentTimeMillis();
		
		result.setGetResponseTime(GetEndTime - GetStartTime);
		
		Logger.v(tag, LogType.Error,"DoAuthPTWIFI :: GET Result :"+getResult);
		
		if (getResult.contains(username)) {
			
			result.setAuthPTWifiStateDONE();
			result.setPostServerResponse(postResult);
			result.setPostResponseStateDONE();
			result.setGetServerResponse(getResult);
			result.setGetResponseStateDONE();
			
			return result;
		}
		
		return result;
	}
	
	public String doGET(String urlToRead) {

	      BufferedReader rd;
	      String line;
	      String result = "";
	      
	      try {
	    	  URL url = new URL(urlToRead);
	    	  HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	         
	         connection.setRequestMethod("GET");
	         connection.setConnectTimeout(CONNECTION_TIMEOUT);
	         
	         rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	         while ((line = rd.readLine()) != null) {
	            result += line;
	         }
	         
	         rd.close();
	         
	      } catch (Exception ex) {
	    	  Logger.v(tag, LogType.Error,"doGET :: Error :"+ex.toString());
	      }
	      
	      // fazer parser para ver se o login foi efectuado com sucesso
	      
	      
	      return result;
	   }
	
	public String doPost(String link, TreeMap<String,String> params) {
		
		try {
			
			// build the params string
			String paramsString = "";
			
			if (params != null) {
				
				for(String key :params.keySet()){
					if (paramsString.length() == 0) {
						paramsString = key+"="+params.get(key);
					} else {
						paramsString = paramsString + "&" + key+"="+params.get(key);
					}
				}
			}
			
			Logger.v(tag, LogType.Debug,"doPost :: paramsString :"+paramsString);
			
			
			URL url = new URL(link);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			
			connection.setConnectTimeout(CONNECTION_TIMEOUT);
			connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:11.0) Gecko/20100101 FireFox/11.0");
			connection.setRequestProperty("Content-Length", "" + paramsString.length());
			
			// Enable POST method
			connection.setRequestMethod("POST");
			
			connection.setUseCaches (false);
			//connection.setDoInput(true);
			connection.setDoOutput(true);
			
			//Send request
		    DataOutputStream wr = new DataOutputStream (connection.getOutputStream());
		    
		    wr.writeBytes (paramsString);
		    wr.flush ();
		    wr.close ();
		    
		    // Responses from the server (code and message)
		    String serverResponseCode = Integer.toString(connection.getResponseCode());
		 			
		    Logger.v(tag, LogType.Debug,"doPost :: serverResponseCode :"+serverResponseCode);
		 			
		    String serverResponseMessage = connection.getResponseMessage();
		 			
		    Logger.v(tag, LogType.Debug,"doPost :: serverResponseMessage :"+serverResponseMessage);
		    
		    return serverResponseCode;
		
		} catch(Exception ex) {
			Logger.v(tag, LogType.Error,"doPost :: Error :"+ex.toString());
		}
		
		return "000";
	}
}
