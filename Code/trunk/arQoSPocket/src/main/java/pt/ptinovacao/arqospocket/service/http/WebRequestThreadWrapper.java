package pt.ptinovacao.arqospocket.service.http;

import java.net.URL;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.logs.MyLogger;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;

public class WebRequestThreadWrapper extends AsyncTask<URL, Integer, WebResponse> {
	
	private final static Logger logger = LoggerFactory.getLogger(WebRequestThreadWrapper.class);
	
	private long delayBeforeStartThread = 100;
	
	// my internal ref
	private AsyncTask<URL, Integer, WebResponse> myInternalRef = null;
	
	private String urlToRead = null;
	private Map<String, String> params = null;
	private String body = null;
	byte [] imgData = null;
	private Bitmap myBitmap = null;
	
	private String operationCode = null;
	private IWebRequestCallBack callbackRef = null;
	private WebActionEnum webActionEnum = WebActionEnum.NA;
	
	public WebRequestThreadWrapper(String operationCode, IWebRequestCallBack callbackRef) {
		myInternalRef = this;
		
		this.operationCode = operationCode;
		this.callbackRef = callbackRef;
	}

	public void doGETAsync(String urlToRead) {
		final String method = "doGETAsync";
		
		this.urlToRead = urlToRead;
		webActionEnum = WebActionEnum.GET;
		
		startThread();
	}
	
	public void doPOSTAsync(String urlToRead, Map<String, String> params, String body) {
		final String method = "doPOSTAsync";
		
		this.urlToRead = urlToRead;
		this.params = params;
		this.body = body;
		webActionEnum = WebActionEnum.POST;
		
		startThread();
	}
	
	public void doPOSTAsync(String urlToRead, Map<String, String> params, byte [] imgData) {
		final String method = "doPOStBinaryAsync";
		
		this.urlToRead = urlToRead;
		this.params = params;
		this.imgData = imgData;
		webActionEnum = WebActionEnum.BINARYPOST;
		
		startThread();
	}
	
	public void doPUTAsync(String urlToRead, String body) {
		final String method = "doPUTAsync";
		
		this.urlToRead = urlToRead;
		this.body = body;
		webActionEnum = WebActionEnum.PUT;
		
		startThread();
	}
	
	public void doDownloadPhotoAsync(String url) {
		final String method = "doDownloadPhotoAsync";
		
		this.urlToRead = url;
		webActionEnum = WebActionEnum.PHOTO_DOWNLOADING;
		
		startThread();
	}
	
	private void startThread() {
		final String method = "startThread";
		
		MyLogger.trace(logger, method, "In");
		
		try {
			
			Handler myHandler = new Handler();
			Runnable r = new Runnable()
			{
				public void run() 
				{					
					try {
		    	
						MyLogger.trace(logger, method, "Runnable - run - In");
						
						if (myInternalRef != null) {
							myInternalRef.execute(null, null, null);
							MyLogger.trace(logger, method, "myInternalRef.executed!");
						}
		    		
					} catch(Exception ex) {
						MyLogger.error(logger, method, ex);
					}
				}
			};
		
			MyLogger.trace(logger, method, "Start handler");
			myHandler.postDelayed(r, delayBeforeStartThread);
			//myHandler.post(r);
			
			MyLogger.trace(logger, method, "Out");
		
		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	@Override
	protected WebResponse doInBackground(URL... arg0) {
		
		final String method = "doInBackground";
		WebResponse response = null;
		
		try {
			
			MyLogger.trace(logger, method, "In");
			
			switch(webActionEnum) {
				case GET:
					MyLogger.trace(logger, method, "GET");
					response = WebRequests.doGET(urlToRead);
					break;
				case POST:
					MyLogger.trace(logger, method, "POST");
					response = WebRequests.doPost(urlToRead, params, body);
					break;
				case BINARYPOST:
					MyLogger.trace(logger, method, "BINARYPOST");
					response = WebRequests.doPost(urlToRead, params, imgData);
					break;
				case PUT:
					MyLogger.trace(logger, method, "PUT");
					response = WebRequests.doPUT(urlToRead, body);
					break;
				case PHOTO_DOWNLOADING:
					MyLogger.trace(logger, method, "PHOTO_DOWNLOADING");
					response = WebRequests.getBitmapFromURL(urlToRead);
					myBitmap = BitmapFactory.decodeStream(response.getResponseStream());
					break;
			}
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		MyLogger.trace(logger, method, "Out");
		return response;
	}
	
	protected void onPostExecute(WebResponse response){
		
		String method = "onPostExecute";
		MyLogger.trace(logger, method, "In");
		
		try {
			
			if (callbackRef != null) {
				switch(webActionEnum) {
					case PHOTO_DOWNLOADING:
						callbackRef.PhotoDownloadComplete(myBitmap, operationCode);
						break;
					default:
						callbackRef.WebRequestComplete(response, operationCode);
						break;
							
				}
				
			}
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	        
		MyLogger.trace(logger, method, "Out");
	}
	
	public static String generateOperationCode() {
		
		StringBuilder sb = new StringBuilder();
		
		// TODO: falta fazer isto com letras e mais eficiente..... usar um hmac
		Random rnd = new Random(System.currentTimeMillis());
		for (int i=0;i<rnd.nextInt(10)+1;i++)
			sb.append(rnd.nextInt(9));
		
		return sb.toString();
	}
}
