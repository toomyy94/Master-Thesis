package pt.ptinovacao.arqospocket.service.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.logs.MyLogger;


public class WebRequests {
	
	private final static Logger logger = LoggerFactory.getLogger(WebRequests.class);
	
	private static final int CONNECTION_TIMEOUT = 20000;
	
	
	public static WebResponse doGET(String urlToRead) {
		final String method = "doGET";
		
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
			
			MyLogger.debug(logger, method, "response code :"+connection.getResponseCode());
			
			return new WebResponse(connection.getResponseCode(), connection.getResponseMessage(), getStringFromUTF8(result.getBytes()));
	         
		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	      
		return null;
	}
	
	public static WebResponse doPost(String endpoint, Map<String, String> params, String Body) {
		final String method = "doPost";

		try {
			MyLogger.trace(logger, method, "In");
			
			URL url = new URL(endpoint);

			StringBuilder bodyBuilder = new StringBuilder();
			Iterator<Entry<String, String>> iterator = params.entrySet()
					.iterator();
			// constructs the POST body using the parameters
			while (iterator.hasNext()) {
				Entry<String, String> param = iterator.next();
				bodyBuilder.append(param.getKey()).append('=')
						.append(param.getValue());
				if (iterator.hasNext()) {
					bodyBuilder.append('&');
				}
			}

			String body = bodyBuilder.toString()+((Body!=null)?Body:"");
			MyLogger.debug(logger, method, "body :"+body);
			byte[] bytes = body.getBytes();
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setConnectTimeout(CONNECTION_TIMEOUT);
			conn.setReadTimeout(CONNECTION_TIMEOUT);
			conn.setFixedLengthStreamingMode(bytes.length);
			MyLogger.debug(logger, method, "bytes.length :"+bytes.length);
			conn.setRequestMethod("POST");
			//conn.setRequestProperty("Content-Type","application/json");
			conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
		
			// post the request
			OutputStream out = conn.getOutputStream();
			out.write(bytes);
			out.close();

			String result = "";
			String line;
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			while ((line = rd.readLine()) != null) {
				result += line;
			}
			
			rd.close();
			conn.getInputStream().close();
			
			MyLogger.debug(logger, method, "conn.getResponseCode() :"+conn.getResponseCode());

			return new WebResponse(conn.getResponseCode(),
					conn.getResponseMessage(), result);

		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}

		return null;
	}
	
	public static WebResponse doPUT(String pUrl, String content) {
		final String method = "doPUT";
		
		try {
		
			MyLogger.debug(logger, method, "pUrl :"+pUrl);
			MyLogger.debug(logger, method, "content :"+content);
			
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
			
			MyLogger.debug(logger, method, "Response code :"+httpCon.getResponseCode());
			
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
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}
	
	public static WebResponse doPUT2(String pUrl, String content) {
		final String method = "doPUT2";
		
		try {
			
			
			MyLogger.debug(logger, method, "pUrl :"+pUrl);
			MyLogger.debug(logger, method, "content :"+content);
			
			HttpClient httpclient = new DefaultHttpClient();
			HttpPut httpPut = new HttpPut(pUrl);
			StringEntity entity = new StringEntity(content, HTTP.UTF_8);
			entity.setContentType("application/json");
			httpPut.setEntity(entity);
			
			HttpResponse response = httpclient.execute(httpPut);
			
			MyLogger.debug(logger, method, "Response code :"+response.getStatusLine().getStatusCode());
			
			
			int responseCode = response.getStatusLine().getStatusCode();
			//String responseMsg = httpCon.getResponseMessage();
			String responseMsg = null;
			
			BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	        String line, result = "";
	        while ((line = br.readLine()) != null)
	            result += line;
			
			httpclient.getConnectionManager().shutdown();
			
			return new WebResponse(responseCode, responseMsg, result);
			
		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}
	
	public int uploadFile(String upLoadServerUri, String sourceFileUri) {
		final String method = "uploadFile";

		String fileName = sourceFileUri;

		HttpURLConnection conn = null;
		DataOutputStream dos = null;  
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024; 
		File sourceFile = new File(sourceFileUri); 
		int serverResponseCode = 0;

		if (!sourceFile.isFile()) {


			//Log.e("uploadFile", "Source File not exist :"+uploadFilePath + "" + uploadFileName);
			MyLogger.debug(logger, method, "Source File not exist :" + sourceFileUri);

			return 0;

		}
		else
	      {
	           try { 

	                 // open a URL connection to the Servlet
	               FileInputStream fileInputStream = new FileInputStream(sourceFile);
	               URL url = new URL(upLoadServerUri);

	               // Open a HTTP  connection to  the URL
	               conn = (HttpURLConnection) url.openConnection(); 
	               conn.setDoInput(true); // Allow Inputs
	               conn.setDoOutput(true); // Allow Outputs
	               conn.setUseCaches(false); // Don't use a Cached Copy
	               conn.setRequestMethod("POST");
	               conn.setRequestProperty("Connection", "Keep-Alive");
	               conn.setRequestProperty("ENCTYPE", "multipart/form-data");
	               conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
	               conn.setRequestProperty("uploaded_file", fileName); 

	               dos = new DataOutputStream(conn.getOutputStream());

	               dos.writeBytes(twoHyphens + boundary + lineEnd); 
	               dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
	                                         + fileName + "\"" + lineEnd);

	               dos.writeBytes(lineEnd);

	               // create a buffer of  maximum size
	               bytesAvailable = fileInputStream.available(); 

	               bufferSize = Math.min(bytesAvailable, maxBufferSize);
	               buffer = new byte[bufferSize];

	               // read file and write it into form...
	               bytesRead = fileInputStream.read(buffer, 0, bufferSize);  

	               while (bytesRead > 0) {

	                 dos.write(buffer, 0, bufferSize);
	                 bytesAvailable = fileInputStream.available();
	                 bufferSize = Math.min(bytesAvailable, maxBufferSize);
	                 bytesRead = fileInputStream.read(buffer, 0, bufferSize);   

	                }

	               // send multipart form data necesssary after file data...
	               dos.writeBytes(lineEnd);
	               dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

	               // Responses from the server (code and message)
	               serverResponseCode = conn.getResponseCode();
	               String serverResponseMessage = conn.getResponseMessage();

	               MyLogger.debug(logger, method, "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);

	               if(serverResponseCode == 200){
	            	   MyLogger.debug(logger, method, "serverResponseCode == 200");
	               }    

	               //close the streams //
	               fileInputStream.close();
	               dos.flush();
	               dos.close();

	          } catch (Exception ex) {
	        	  MyLogger.error(logger, method, ex);
	          }    
	           
	          return serverResponseCode; 

	      } // End else block 
	} 
	

	public static byte[] getStringToUTF8(String text) {
		final String method = "getStringToUTF8";
		
		try {
			return text.getBytes("UTF-8");
			
		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}
	
	public static String getStringFromUTF8(byte[] text) {
		final String method = "getStringFromUTF8";
		
		try {
			
			return new String(text, "US-ASCII");
			
		} catch (UnsupportedEncodingException ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}
	
	public static WebResponse doPost(String endpoint, Map<String, String> params, byte [] imgData) {
		final String method = "doPost";
		
		try {
			MyLogger.trace(logger, method, "In");
			
			URL url = new URL(endpoint);

			StringBuilder bodyBuilder = new StringBuilder();
			Iterator<Entry<String, String>> iterator = params.entrySet()
					.iterator();
			// constructs the POST body using the parameters
			while (iterator.hasNext()) {
				Entry<String, String> param = iterator.next();
				bodyBuilder.append(param.getKey()).append('=')
						.append(param.getValue());
				if (iterator.hasNext()) {
					bodyBuilder.append('&');
				}
			}

			String body = bodyBuilder.toString();
			MyLogger.debug(logger, method, "body :"+body);
			byte[] bytes = body.getBytes();
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setConnectTimeout(CONNECTION_TIMEOUT);
			conn.setReadTimeout(CONNECTION_TIMEOUT);
			
			int body_length = bytes.length+imgData.length;
			conn.setFixedLengthStreamingMode(body_length);
			MyLogger.debug(logger, method, "bytes.length :"+body_length);
			conn.setRequestMethod("POST");
			//conn.setRequestProperty("Content-Type","application/json");
			conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
		
			// post the request
			OutputStream out = conn.getOutputStream();
			out.write(bytes);
			out.write(imgData);
			out.close();

			String result = "";
			String line;
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			while ((line = rd.readLine()) != null) {
				result += line;
			}
			
			rd.close();
			conn.getInputStream().close();
			
			MyLogger.debug(logger, method, "conn.getResponseCode() :"+conn.getResponseCode());

			return new WebResponse(conn.getResponseCode(),
					conn.getResponseMessage(), result);

		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}

		return null;
	}
	
	public static WebResponse getBitmapFromURL(String src) {
		final String method = "getBitmapFromURL";
		
	    try {
	    	
	    	MyLogger.trace(logger, method, "In");
	    	
	        URL url = new URL(src);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setDoInput(true);
	        connection.connect();
	        //InputStream input = connection.getInputStream();
	        //Bitmap myBitmap = BitmapFactory.decodeStream(input);
	        
	        return new WebResponse(connection.getResponseCode(),
	        		connection.getResponseMessage(), connection.getInputStream());
	        
	    } catch (Exception ex) {
	    	MyLogger.error(logger, method, ex);
		}
	    
	    return null;
	}
}
