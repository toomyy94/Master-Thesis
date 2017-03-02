package PT.PTInov.ArQoSPocketEDP.JSONConnector.Util;

import android.annotation.SuppressLint;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.ByteArrayBuffer;

import PT.PTInov.ArQoSPocketEDP.Utils.LogType;
import PT.PTInov.ArQoSPocketEDP.Utils.Logger;

public class WebRequests {
	
	private final static String tag = "WebRequests";
	
	private static final int CONNECTION_TIMEOUT = 15000;
	
	
	@SuppressLint("ParserError")
	public static WebResponse doGET(String urlToRead, String cookie) {

		BufferedReader rd;
		String line;
		String result = "";
	      
		try {
			URL url = new URL(urlToRead);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	         
			//connection.setDoInput(true);
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(CONNECTION_TIMEOUT);
			connection.setReadTimeout(CONNECTION_TIMEOUT);
			
			if (cookie != null)
				connection.setRequestProperty("Cookie", cookie);
			
			//connection.connect();
	         
			rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			line = rd.readLine();
			while (line != null) {
				result += line;
				line = rd.readLine();
			}
	         
			if (rd != null)
				rd.close();
			
			try {
				//connection.disconnect();
			} catch(Exception ex) {}
			
			/*
			long bytecounter = 0;
			BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
		     ByteArrayBuffer baf = new ByteArrayBuffer(50);
		     int read = 0;
		     int bufSize = 10*1024;
		     byte[] buffer = new byte[bufSize];
		     while(true){
		          read = bis.read(buffer);
		          if(read==-1){
		               break;
		          }
		          baf.append(buffer, 0, read);
		          bytecounter += read;
		     }
		     result = new String(baf.toByteArray());
		     */
			
		     Logger.v(tag, "doGET", LogType.Error, "bytecounter :"+result.length());
			Logger.v(tag, "doGET", LogType.Error, "response code :"+connection.getResponseCode());
			
			return new WebResponse(connection.getResponseCode(), connection.getResponseMessage(), getStringFromUTF8(result.getBytes()));
	         
		} catch (Exception ex) {
			Logger.v(tag, "doGET", LogType.Error, ex.toString());
		}
	      
		return null;
	}
	
	public static WebResponse doPUT(String pUrl, String content) {
		
		// devia de tesatr se tem ligação a internet antes de tentar fazer o put
		
		try {
		
			Logger.v(tag, "doPUT", LogType.Debug, "pUrl :"+pUrl);
			Logger.v(tag, "doPUT", LogType.Debug, "content :"+content);
			
			URL url = new URL(pUrl);
			HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
			
			httpCon.setDoOutput(true);
			httpCon.setDoInput(true);
			httpCon.setConnectTimeout(CONNECTION_TIMEOUT);
			httpCon.setRequestMethod("PUT");
			
			httpCon.setRequestProperty("Content-Type", "application/json");
			httpCon.setRequestProperty("Content-Length", Integer.toString(content.length()));
			httpCon.getOutputStream().write(content.getBytes("UTF-8"));
			httpCon.getOutputStream().flush();
			httpCon.getOutputStream().close();
			
			Logger.v(tag, "doPUT", LogType.Debug, "Response code :"+httpCon.getResponseCode());
			
			String result = "";
			String line;
			BufferedReader rd = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
			while ((line = rd.readLine()) != null) {
				result += line;
			}
			rd.close();
			httpCon.getInputStream().close();
			
			int responseCode = httpCon.getResponseCode();
			String responseMsg = httpCon.getResponseMessage();
			
			httpCon.disconnect();
			
			return new WebResponse(responseCode, responseMsg, result);
			
		} catch (Exception ex) {
			Logger.v(tag, "doPUT", LogType.Error, ex.toString());
		}
		
		return null;
	}
	
	public static WebResponse doPUT2(String pUrl, String content) {
		
		// devia de tesatr se tem ligação a internet antes de tentar fazer o put
		
		try {
			
			
			Logger.v(tag, "doPUT2", LogType.Debug, "pUrl :"+pUrl);
			Logger.v(tag, "doPUT2", LogType.Debug, "content :"+content);
			
			HttpClient httpclient = new DefaultHttpClient();
			HttpPut httpPut = new HttpPut(pUrl);
			StringEntity entity = new StringEntity(content, HTTP.UTF_8);
			entity.setContentType("application/json");
			httpPut.setEntity(entity);
			
			HttpResponse response = httpclient.execute(httpPut);
			
			Logger.v(tag, "doPUT2", LogType.Debug, "Response code :"+response.getStatusLine().getStatusCode());
			
			
			int responseCode = response.getStatusLine().getStatusCode();
			//String responseMsg = httpCon.getResponseMessage();
			String responseMsg = null;
			
			BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	        String line, result = "";
	        line = br.readLine();
	        while (line != null) {
	            result += line;
	            line = br.readLine();
	        }
			
			httpclient.getConnectionManager().shutdown();
			
			return new WebResponse(responseCode, responseMsg, result);
			
		} catch (Exception ex) {
			Logger.v(tag, "doPUT2", LogType.Error, ex.toString());
		}
		
		return null;
	}

	public static byte[] getStringToUTF8(String text) {
		
		try {
			return text.getBytes("UTF-8");
			
		} catch (Exception ex) {
			Logger.v(tag, "getStringToUTF8", LogType.Error, ex.toString());
		}
		
		return null;
	}
	
	public static String getStringFromUTF8(byte[] text) {
		
		try {
			
			return new String(text, "US-ASCII");
			
		} catch (UnsupportedEncodingException e) {
			Logger.v(tag, "getStringFromUTF8", LogType.Error, e.toString());
		}
		
		return null;
	}
}