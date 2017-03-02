package PT.PTIN.ArQoSPocketPTWiFi.Services;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

import org.apache.http.util.ByteArrayBuffer;

import PT.PTIN.ArQoSPocketPTWiFi.Enums.ResponseEnum;
import PT.PTIN.ArQoSPocketPTWiFi.Logs.LogType;
import PT.PTIN.ArQoSPocketPTWiFi.Logs.Logger;
import PT.PTIN.ArQoSPocketPTWiFi.Utils.HttpServiceResponse;
import android.content.Context;
import android.net.wifi.WifiManager;

public class HttpDownloadUpload {
	
	private final static String tag = "HttpDownloadUpload";
	
	private final static String linkDownload = "http://speedtest.nfsi.pt/speedtest/random1500x1500.jpg";
	private final static String linkUpload = "http://speedtest.nfsi.pt/speedtest/upload.php";
	
	private WifiManager wifi = null;
	private Context serviceContext = null;
	
	public HttpDownloadUpload(Context c) {
		
		Logger.v(tag, LogType.Trace,"HttpDownloadUpload :: Creat a instance of ManageWiFiConnection");
		
		try {
			
			serviceContext = c;
			
		} catch(Exception ex) {
			Logger.v(tag, LogType.Error,"HttpDownloadUpload :: Error :"+ex.toString());
		}
	}
	
	public HttpServiceResponse DoHTTPUpload() {
		
		HttpServiceResponse response = new HttpServiceResponse();
		
		double accessTime = -1;
		double totalTime = -1;
		long totalbytes = 200000;
		double debito = -1;
		
		try {
			
			int CONNECTION_TIMEOUT = 10000;
			int CONNECTION_READ_TIMEOUT = 60000;
			
			String payloadFixed = "content0=";
			
			HttpURLConnection connection = null;
			DataOutputStream outputStream = null;
			String boundary = "*****";
			
			URL url = new URL(linkUpload);

			long startTime = System.currentTimeMillis();

			connection = (HttpURLConnection) url.openConnection();

			accessTime = (System.currentTimeMillis() - startTime) / 1000;
			
			connection.setConnectTimeout(CONNECTION_TIMEOUT);
			
			// Allow Inputs & Outputs
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);

			// Enable POST method
			connection.setRequestMethod("POST");
			connection.setReadTimeout(CONNECTION_READ_TIMEOUT);

			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			try {
				
				outputStream = new DataOutputStream(connection.getOutputStream());
				outputStream.writeBytes(payloadFixed);
			
				final String alphabet = "0123456789qwertyuioplkjhgfdsazxcvbnm";
				final int N = alphabet.length();
				Random r = new Random();
		    
				for(int i=0;i<totalbytes;i++) {
					outputStream.writeBytes(alphabet.charAt(r.nextInt(N))+"");
				}
				
			} catch(Exception ex ) {
				
			}

			// Responses from the server (code and message)
			String serverResponseCode = Integer.toString(connection
					.getResponseCode());
			
			Logger.v(tag, LogType.Debug,"DoHTTPUpload :: serverResponseCode :"+serverResponseCode);
			
			String serverResponseMessage = connection.getResponseMessage();
			
			Logger.v(tag, LogType.Debug,"DoHTTPUpload :: serverResponseMessage :"+serverResponseMessage);

			outputStream.flush();
			outputStream.close();
			
			totalTime = (System.currentTimeMillis() - startTime) / 1000;

			debito = totalbytes / totalTime;
			
			Logger.v(tag, LogType.Debug, "DoHTTPUpload :: totalbytes :" + totalbytes);
			Logger.v(tag, LogType.Debug, "DoHTTPUpload :: totalTime :" + totalTime);
			Logger.v(tag, LogType.Debug, "DoHTTPUpload :: accessTime :" + accessTime);
			Logger.v(tag, LogType.Debug, "DoHTTPUpload :: debito :" + debito);
			
			response = new HttpServiceResponse(ResponseEnum.OK, accessTime, totalTime, totalbytes, debito);
			
		} catch (Exception ex) {
			Logger.v(tag, LogType.Error,"DoHTTPUpload :: Error :"+ex.toString());
		}
		
		
		return response;
	}
	
	public HttpServiceResponse DoHTTPDownload() {
		
		HttpServiceResponse response = new HttpServiceResponse();
		
		double accessTime = -1;
		double totalTime = -1;
		long totalbytes = 0;
		double debito = -1;

		try {
			
			int CONNECTION_TIMEOUT = 10000;
			int CONNECTION_READ_TIMEOUT = 60000;

			long startTime = System.currentTimeMillis();

			 URLConnection con = new URL(linkDownload).openConnection();

			 accessTime = (System.currentTimeMillis() - startTime) / 1000;

			 try {
				 //con.setRequestMethod("HEAD");
				 con.setConnectTimeout(CONNECTION_TIMEOUT);
				 con.setReadTimeout(CONNECTION_READ_TIMEOUT);

				 InputStream is = con.getInputStream();

				 BufferedInputStream bis = new BufferedInputStream(is);

				 try {		
					 int current = 0;
					 while ((current = bis.read()) != -1) {
						 totalbytes++;
					 }
				 } catch (Exception ex) {
					 Logger.v(tag, LogType.Error,
						"DoHTTPDownload :: Error reading the streaming :" + ex.toString());
				 }
			 } catch(Exception ex) {
				 
			 }
			
			totalTime = (System.currentTimeMillis() - startTime) / 1000;
			
			debito = totalbytes / totalTime;
			
			Logger.v(tag, LogType.Debug, "DoHTTPDownload :: totalbytes :" + totalbytes);
			Logger.v(tag, LogType.Debug, "DoHTTPDownload :: totalTime :" + totalTime);
			Logger.v(tag, LogType.Debug, "DoHTTPDownload :: accessTime :" + accessTime);
			Logger.v(tag, LogType.Debug, "DoHTTPDownload :: debito :" + debito);
			
			response = new HttpServiceResponse(ResponseEnum.OK, accessTime, totalTime, totalbytes, debito);
			
		} catch (Exception ex) {
			Logger.v(tag, LogType.Error,"DoHTTPDownload :: Error :"+ex.toString());
		}
		
		return response;
	}
	

}

