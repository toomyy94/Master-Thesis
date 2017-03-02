package PT.PTInov.ArQoSPocket.Service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Random;

import org.apache.http.util.ByteArrayBuffer;

import PT.PTInov.ArQoSPocket.Enums.ResponseEnum;
import PT.PTInov.ArQoSPocket.Utils.LogType;
import PT.PTInov.ArQoSPocket.Utils.Logger;
import PT.PTInov.ArQoSPocket.Utils.WebResponse;
import PT.PTInov.ArQoSPocket.structs.BrowsingTestResult;
import android.content.Context;
import android.net.wifi.WifiManager;

public class HttpService {
	
	private final static String tag = "HttpService";
	
	//private final static String linkDownload = "http://speedtest.nfsi.pt/speedtest/random4000x4000.jpg";
	private final static String linkDownload = "http://speedtest.nfsi.pt/speedtest/random2000x2000.jpg";
	private final static String linkUpload = "http://speedtest.nfsi.pt/speedtest/upload.php";
	
	private WifiManager wifi = null;
	private Context serviceContext = null;
	
	public HttpService(Context c) {
		
		Logger.v(tag, LogType.Trace,"HttpDownloadUpload :: Creat a instance of ManageWiFiConnection");
		
		try {
			
			serviceContext = c;
			
		} catch(Exception ex) {
			Logger.v(tag, LogType.Error,"HttpDownloadUpload :: Error :"+ex.toString());
		}
	}
	
	private static final int CONNECTION_TIMEOUT = 20000;
	public BrowsingTestResult DoBrowsingTest(String urlToRead) {
		
		BufferedReader rd;
		String line;
		String result = "";
		
		long responseTime = -1;
		long totalResponseTime = -1;
		long responseSize = 0;
		String requestURL = urlToRead;
		WebResponse response = null;
	      
		try {
						
			URL url = new URL(urlToRead);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	         
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(CONNECTION_TIMEOUT);
			
			long startRequestTime = System.currentTimeMillis();
	         
			rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			while ((line = rd.readLine()) != null) {
				
				if (responseTime == -1)
					responseTime = System.currentTimeMillis() - startRequestTime;
				
				result += line;
			}
	        
			totalResponseTime = System.currentTimeMillis() - startRequestTime;
			
			rd.close();
			
			responseSize = result.length();
			
			Logger.v(tag, "doGET", LogType.Error, "response code :"+connection.getResponseCode());
			
			response = new WebResponse(connection.getResponseCode(), connection.getResponseMessage(), getStringFromUTF8(result.getBytes()));
			return new BrowsingTestResult(responseTime, totalResponseTime, responseSize, requestURL, response);	
	         
		} catch (Exception ex) {
			Logger.v(tag, "doGET", LogType.Error, ex.toString());
		}
	      
		return new BrowsingTestResult(responseTime, totalResponseTime, responseSize, requestURL, response);	
	}
	
	public HttpServiceResponse DoHTTPUpload() {
		
		HttpServiceResponse response = new HttpServiceResponse();
		
		double accessTime = -1;
		double totalTime = -1;
		//long totalbytes = 200000;
		long totalbytes = 30000000;
		double debito = -1;
		
		long totalSendBytes = 0;
		
		try {
			
			int CONNECTION_TIMEOUT = 10000;
			int CONNECTION_READ_TIMEOUT = 16000;
			
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

			//try {
				
				outputStream = new DataOutputStream(connection.getOutputStream());
				outputStream.writeBytes(payloadFixed);
			
				final String alphabet = "0123456789qwertyuioplkjhgfdsazxcvbnm";
				final int N = alphabet.length();
				Random r = new Random();
		    
				for(int i=0;i<totalbytes;i++) {
					
					if ((System.currentTimeMillis() - startTime) > CONNECTION_READ_TIMEOUT) break;
					
					outputStream.writeBytes(alphabet.charAt(r.nextInt(N))+"");
					totalSendBytes++;
				}
				
				totalTime = (System.currentTimeMillis() - startTime);
				
			//} catch(Exception ex ) {
				
			//}

			Logger.v(tag, LogType.Debug,"DoHTTPUpload :: ask for response!");
				
			// Responses from the server (code and message)
			String serverResponseCode = Integer.toString(connection
					.getResponseCode());
			
			Logger.v(tag, LogType.Debug,"DoHTTPUpload :: serverResponseCode :"+serverResponseCode);
			
			String serverResponseMessage = connection.getResponseMessage();
			
			Logger.v(tag, LogType.Debug,"DoHTTPUpload :: serverResponseMessage :"+serverResponseMessage);

			outputStream.flush();
			outputStream.close();
			
			
			//totalTime = (System.currentTimeMillis() - startTime) / 1000;
			//debito = totalbytes / totalTime;
			
			long localBytesToBits = totalSendBytes * 8;
			
			//debito = (totalSendBytes * 1000) / (System.currentTimeMillis() - startTime);
			debito = (localBytesToBits * 1000) / totalTime;
			
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
			int CONNECTION_READ_TIMEOUT = 16000;

			long startTime = System.currentTimeMillis();

			 URLConnection con = new URL(linkDownload).openConnection();

			 accessTime = (System.currentTimeMillis() - startTime) / 1000;

			 //try {
				 //con.setRequestMethod("HEAD");
				 con.setConnectTimeout(CONNECTION_TIMEOUT);
				 con.setReadTimeout(CONNECTION_READ_TIMEOUT);

				 InputStream is = con.getInputStream();

				 BufferedInputStream bis = new BufferedInputStream(is);

				 try {		
					 int current = 0;
					 while ((current = bis.read()) != -1) {
						 
						 if ((System.currentTimeMillis() - startTime) > CONNECTION_READ_TIMEOUT) break;
						 
						 totalbytes++;
					 }
				 } catch (Exception ex) {
					 Logger.v(tag, LogType.Error,
						"DoHTTPDownload :: Error reading the streaming :" + ex.toString());
				 }
			 //} catch(Exception ex) {
				 
			 //}
			
			//totalTime = (System.currentTimeMillis() - startTime) / 1000;
			//debito = totalbytes / totalTime;
				 
		
		    totalTime = (System.currentTimeMillis() - startTime);
				 
			long localBytesToBits = totalbytes * 8;
				 
			// totalTime está em millisec
			debito = (localBytesToBits * 1000) / totalTime;
			
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
	
	
	private int internalRunningThreads = 0;
	private int numThread = 10;
	
	private long startDate = 0;
	private long stopDate = 0;
	
	private long internalByteCounter = 0;
	
	private synchronized void threadStarted() {
		internalRunningThreads++;
		if (internalRunningThreads == numThread) {
			startDate = System.currentTimeMillis();
			internalByteCounter = 0;
		}
	}
	
	private synchronized void threadStopped() {
		internalRunningThreads--;
		stopDate = System.currentTimeMillis();
	}
	
	private synchronized void incCounter() {
		if ((startDate != 0) && (stopDate == 0))
			internalByteCounter++;
	}
	
	public HttpServiceResponse DoHTTPMAxUpload() {
		final String method = "DoHTTPMAxUpload";

		HttpServiceResponse response = new HttpServiceResponse();
		
		startDate = 0;
		stopDate = 0;
		internalByteCounter = 0;

		ArrayList<Thread> thread = new ArrayList<Thread>();

		for (int i = 0; i < numThread; i++) {
			final int index = i;
			Thread threadLocal = new Thread(new Runnable() {
				public void run() {

					Logger.v(tag, LogType.Debug, method + ":: thread" + (index + 1) + " init time :" + System.currentTimeMillis());
					threadStarted();

					try {
						
						long totalbytes = 30000000;
						
						int CONNECTION_TIMEOUT = 10000;
						int CONNECTION_READ_TIMEOUT = 16000;
						
						String payloadFixed = "content0=";
						
						HttpURLConnection connection = null;
						DataOutputStream outputStream = null;
						String boundary = "*****";
						
						URL url = new URL(linkUpload);

						long startTime = System.currentTimeMillis();

						connection = (HttpURLConnection) url.openConnection();
						
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

						//try {
							
							outputStream = new DataOutputStream(connection.getOutputStream());
							outputStream.writeBytes(payloadFixed);
						
							final String alphabet = "0123456789qwertyuioplkjhgfdsazxcvbnm";
							final int N = alphabet.length();
							Random r = new Random();
					    
							for(int i=0;i<totalbytes;i++) {
								
								if ((System.currentTimeMillis() - startTime) > CONNECTION_READ_TIMEOUT) break;
								
								outputStream.writeBytes(alphabet.charAt(r.nextInt(N))+"");
								incCounter();
							}

						Logger.v(tag, LogType.Debug,"DoHTTPUpload :: ask for response!");
							
						// Responses from the server (code and message)
						String serverResponseCode = Integer.toString(connection
								.getResponseCode());
						
						Logger.v(tag, LogType.Debug,"DoHTTPUpload :: serverResponseCode :"+serverResponseCode);
						
						String serverResponseMessage = connection.getResponseMessage();
						
						Logger.v(tag, LogType.Debug,"DoHTTPUpload :: serverResponseMessage :"+serverResponseMessage);

						outputStream.flush();
						outputStream.close();

					} catch (Exception ex) {
						Logger.v(tag, LogType.Error, "DoHTTPDownload :: Error :" + ex.toString());
					}

					threadStopped();
					Logger.v(tag, LogType.Debug, method + ":: thread" + (index + 1) + " end time :" + System.currentTimeMillis());
				}
			});

			thread.add(threadLocal);
		}

		for (Thread t : thread)
			t.start();

		long startTimeWatchDog = System.currentTimeMillis();
		while ((stopDate == 0)
				&& ((System.currentTimeMillis() - startTimeWatchDog) < 20000)) {
			try {
				Thread.sleep(1000);
			} catch (Exception ex) {
			}
		}

		if (startDate != 0 && stopDate != 0) {
			long totalBytes = internalByteCounter * 8;
			Logger.v(tag, LogType.Debug, method + " :: totalBytes :"
					+ totalBytes);

			long totalTime = stopDate - startDate;
			Logger.v(tag, LogType.Debug, method + " :: totalTime :" + totalTime);

			// totalTime está em millisec
			double debito = (totalBytes * 1000) / totalTime;

			Logger.v(tag, LogType.Debug, method + " :: debito :" + debito);

			response = new HttpServiceResponse(ResponseEnum.OK, 0, totalTime,
					totalBytes / 8, debito);
		}

		Logger.v(tag, LogType.Debug, method + " :: startDate :" + startDate);
		Logger.v(tag, LogType.Debug, method + " :: stopDate :" + stopDate);

		return response;
	}

	public HttpServiceResponse DoHTTPMAxDownload() {
		final String method = "DoHTTPMAxDownload";
		
		HttpServiceResponse response = new HttpServiceResponse();
		
		startDate = 0;
		stopDate = 0;
		internalByteCounter = 0;
		
		// Vou arrancar 10 threads e quando tiver as 10 threads a fazer download em simultaneo, vou ver qual é a largura de banda nesse instante
		
		ArrayList<Thread> thread = new ArrayList<Thread>();
		
		for (int i = 0; i < numThread; i++) {
			final int index = i;
			Thread threadLocal = new Thread(new Runnable() {
				public void run() {
					
					Logger.v(tag, LogType.Debug, method + ":: thread"+(index+1)+" init time :" + System.currentTimeMillis());
					threadStarted();
					
					try {
						
						int CONNECTION_TIMEOUT = 10000;
						int CONNECTION_READ_TIMEOUT = 16000;

						long startTime = System.currentTimeMillis();

						 URLConnection con = new URL(linkDownload).openConnection();

						 //try {
							 //con.setRequestMethod("HEAD");
							 con.setConnectTimeout(CONNECTION_TIMEOUT);
							 con.setReadTimeout(CONNECTION_READ_TIMEOUT);

							 InputStream is = con.getInputStream();

							 BufferedInputStream bis = new BufferedInputStream(is);

							 try {		
								 int current = 0;
								 while ((current = bis.read()) != -1) {
									 
									 if ((System.currentTimeMillis() - startTime) > CONNECTION_READ_TIMEOUT) break;
									 
									 incCounter();
								 }
							 } catch (Exception ex) {
								 Logger.v(tag, LogType.Error,
									"DoHTTPDownload :: Error reading the streaming :" + ex.toString());
							 }
					
					} catch(Exception ex) {
						Logger.v(tag, LogType.Error,"DoHTTPDownload :: Error :"+ex.toString());
					}
					
					threadStopped();
					Logger.v(tag, LogType.Debug, method + ":: thread"+(index+1)+" end time :" + System.currentTimeMillis());
				}
			});
			
			thread.add(threadLocal);
		}
		
		for (Thread t :thread)
			t.start();
		
		long startTimeWatchDog = System.currentTimeMillis();
		while ((stopDate==0) && ((System.currentTimeMillis()-startTimeWatchDog)<20000)) {
			try {
			Thread.sleep(1000);
			} catch(Exception ex) {}
		}
		
		if (startDate != 0 && stopDate != 0) {
			long totalBytes = internalByteCounter * 8;
			Logger.v(tag, LogType.Debug, method + " :: totalBytes :" + totalBytes);
			
			long totalTime = stopDate - startDate;
			Logger.v(tag, LogType.Debug, method + " :: totalTime :" + totalTime);
				 
			// totalTime está em millisec
			double debito = (totalBytes * 1000) / totalTime;
			
			Logger.v(tag, LogType.Debug, method + " :: debito :" + debito);
			
			response = new HttpServiceResponse(ResponseEnum.OK, 0, totalTime, totalBytes/8, debito);
		}
		
		Logger.v(tag, LogType.Debug, method + " :: startDate :" + startDate);
		Logger.v(tag, LogType.Debug, method + " :: stopDate :" + stopDate);
		
		return response;
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

