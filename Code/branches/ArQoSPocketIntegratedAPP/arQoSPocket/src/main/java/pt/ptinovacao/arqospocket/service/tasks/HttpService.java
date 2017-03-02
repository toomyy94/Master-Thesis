package pt.ptinovacao.arqospocket.service.tasks;



import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.logs.MyLogger;
import pt.ptinovacao.arqospocket.service.tasks.results.BrowsingTestResult;
import pt.ptinovacao.arqospocket.service.tasks.results.HttpServiceResponse;
import pt.ptinovacao.arqospocket.service.tasks.structs.WebResponse;
import pt.ptinovacao.arqospocket.service.tasks.speedTestFacebook.ConnectionQuality;
import pt.ptinovacao.arqospocket.service.tasks.speedTestFacebook.DeviceBandwidthSampler;
import pt.ptinovacao.arqospocket.service.tasks.speedTestFacebook.TrafficType;

import android.content.Context;
import android.net.wifi.WifiManager;

public class HttpService {
	
	private final static Logger logger = LoggerFactory.getLogger(HttpService.class);
	
	private final static String linkDownload = "http://connectionclass.parseapp.com/m100_hubble_4060.jpg";
	private final static String linkUpload = "http://www.speedtest.co.bw/speedtest/upload.php";
	
	private WifiManager wifi = null;
	private Context serviceContext = null;
	
	private DeviceBandwidthSampler mDeviceBandwidthSampler;
	private ConnectionQuality mConnectionQuality = ConnectionQuality.UNKNOWN;
	private double mConnectionBandwidth = -1;
	private int mTries = 0;
	private final int maxTries = 1;
	
	public HttpService(Context c) {
		final String method = "HttpService";
		
		MyLogger.trace(logger, method, "IN");
		
		try {
			
			serviceContext = c;
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	
	
	public HttpServiceResponse DoSimpleHTTPDownload() {
		final String method = "DoSimpleHTTPDownload";
		
		HttpServiceResponse response = new HttpServiceResponse();
		
		try {
			
			mTries = 0;
			
			mDeviceBandwidthSampler = DeviceBandwidthSampler.getInstance(TrafficType.Download);
		    
			MyLogger.debug(logger, method, "CurrentBandwidth :"+mDeviceBandwidthSampler.getCurrentBandwidth());
		    
			do{
				
				mDeviceBandwidthSampler.startSampling();
			try {
				
				MyLogger.debug(logger, method, "before URLConnection");
				// Open a stream to download the image from our URL.
				URLConnection connection = new URL(linkDownload).openConnection();
				connection.setUseCaches(false);
				connection.connect();
				InputStream input = connection.getInputStream();
				try {
					byte[] buffer = new byte[1024];

					// Do some busy waiting while the stream is open.
					while (input.read(buffer) != -1) {
					}
				} finally {
					input.close();

					MyLogger.debug(logger, method, "End URLConnection");
				}
			} catch (IOException ex) {
				MyLogger.error(logger, method, ex);
			}
			
			mDeviceBandwidthSampler.stopSampling();
			
			mConnectionBandwidth = mDeviceBandwidthSampler.getCurrentBandwidth();
			MyLogger.debug(logger, method, "CurrentBandwidth :"+mConnectionBandwidth);
			
			mTries++;
		    
		    //wait for the result
			} while(mConnectionBandwidth == -1 && mTries < maxTries);
		    	//Thread.sleep(1000);
			
			long roundedInt = Math.round(mConnectionBandwidth * 100);
			double mConnectionBandwidth = (double) roundedInt/100;
			MyLogger.debug(logger, method, "CurrentBandwidth :"+mConnectionBandwidth);
		    
		    if (mConnectionBandwidth != -1)
		    	response = new HttpServiceResponse(HttpServiceResponse.ResponseHttpServiceEnum.OK, mConnectionBandwidth, linkDownload);
			
		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return response;
	}
	
	
	public HttpServiceResponse DoSimpleHTTPUpload() {
		final String method = "DoHTTPUpload";

		HttpServiceResponse responseService = new HttpServiceResponse();

		try {

			mTries = 0;

			mDeviceBandwidthSampler = DeviceBandwidthSampler
					.getInstance(TrafficType.Upload);

			MyLogger.debug(logger, method, "CurrentBandwidth :"
					+ mDeviceBandwidthSampler.getCurrentBandwidth());

			do {

				mDeviceBandwidthSampler.startSampling();

				try {

					long totalbytes = 1000000;

					StringBuilder sb = new StringBuilder();

					final String alphabet = "0123456789qwertyuioplkjhgfdsazxcvbnm";
					final int N = alphabet.length();
					Random r = new Random();

					for (int i = 0; i < totalbytes; i++) {
						sb.append(alphabet.charAt(r.nextInt(N)));
					}

					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost(linkUpload);

					InputStream stream = new ByteArrayInputStream(sb.toString()
							.getBytes());
					InputStreamEntity reqEntity = new InputStreamEntity(stream,
							-1);
					reqEntity
							.setContentType("multipart/form-data;boundary=*****");
					reqEntity.setChunked(true); // Send in multiple parts if
												// needed
					httppost.setEntity(reqEntity);
					HttpResponse response = httpclient.execute(httppost);

				} catch (Exception ex) {
					MyLogger.error(logger, method, ex);
				}

				mDeviceBandwidthSampler.stopSampling();

				mConnectionBandwidth = mDeviceBandwidthSampler
						.getCurrentBandwidth();
				MyLogger.debug(logger, method, "CurrentBandwidth :"
						+ mConnectionBandwidth);

				mTries++;

				// wait for the result
			} while (mConnectionBandwidth == -1 && mTries < maxTries);
			
			long roundedInt = Math.round(mConnectionBandwidth * 100);
			double mConnectionBandwidth = (double) roundedInt/100;
			MyLogger.debug(logger, method, "CurrentBandwidth :"+mConnectionBandwidth);

			if (mConnectionBandwidth != -1)
				responseService = new HttpServiceResponse(
						HttpServiceResponse.ResponseHttpServiceEnum.OK, mConnectionBandwidth,
						linkUpload);

		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}

		return responseService;
	}
	
	
	
	public BrowsingTestResult DoBrowsingAllPageTest(String urlToRead) {
		final String method = "DoBrowsingAllPageTest";
		
		long responseTime = -1;
		long totalResponseTime = -1;
		long responseSize = 0;
		String requestURL = urlToRead;
		WebResponse response = null;
		
		try {
			
			long startRequestTime = System.currentTimeMillis();
			
			Document doc = Jsoup.connect(urlToRead).get();
			
			responseTime = System.currentTimeMillis() - startRequestTime;
			responseSize = doc.data().length();
			
			Elements links = doc.select("a[href]");
	        Elements media = doc.select("[src]");
	        Elements imports = doc.select("link[href]");
	        
	        for (Element src : media) {
	            if (src.tagName().equals("img")) {
	            	//print(" * %s: <%s> %sx%s (%s)",src.tagName(), src.attr("abs:src"), src.attr("width"), src.attr("height"),trim(src.attr("alt"), 20));
	            	MyLogger.debug(logger, method, "Vou fazer download do link :"+src.attr("abs:src"));
	            	responseSize += DoBrowsingTest(src.attr("abs:src")).get_response_size();
	            } else {
	                //print(" * %s: <%s>", src.tagName(), src.attr("abs:src"));
	            	MyLogger.debug(logger, method, "Vou fazer download do link :"+src.attr("abs:src"));
	            	responseSize += DoBrowsingTest(src.attr("abs:src")).get_response_size();
	            }
	        }
	        
	        for (Element link : imports) {
	            //print(" * %s <%s> (%s)", link.tagName(),link.attr("abs:href"), link.attr("rel"));
	        	MyLogger.debug(logger, method, "Vou fazer download do link :"+link.attr("abs:href"));
	        	responseSize += DoBrowsingTest(link.attr("abs:href")).get_response_size();
	        }
	        
	        for (Element link : links) {
	            //print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
	        	MyLogger.debug(logger, method, "Vou fazer download do link :"+link.attr("abs:href"));
	        	responseSize += DoBrowsingTest(link.attr("abs:href")).get_response_size();
	        }
	        
	        totalResponseTime = System.currentTimeMillis() - startRequestTime;
	        
	        response = new WebResponse(200, "", doc.data());
			return new BrowsingTestResult(responseTime, totalResponseTime, responseSize, requestURL, response);	
			
		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}
	
	private static final int CONNECTION_TIMEOUT = 20000;
	public BrowsingTestResult DoBrowsingTest(String urlToRead) {
		final String method = "DoBrowsingTest";
		
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
			
			MyLogger.debug(logger, method, "esponse code :"+connection.getResponseCode());
			
			
			//TODO: falta ler a resposta e procurar todos os links da resposta para descarregar esses links............................................................... 
			
			
			response = new WebResponse(connection.getResponseCode(), connection.getResponseMessage(), getStringFromUTF8(result.getBytes()));
			return new BrowsingTestResult(responseTime, totalResponseTime, responseSize, requestURL, response);	
	         
		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	      
		return new BrowsingTestResult(responseTime, totalResponseTime, responseSize, requestURL, response);	
	}
	
	public HttpServiceResponse DoHTTPUpload(String urllink, String proxy, String content) {
		final String method = "DoHTTPUpload";
		
		MyLogger.trace(logger, method, "DoHTTPUpload :: urllink :"+urllink);
		MyLogger.trace(logger, method, "DoHTTPUpload :: proxy :"+proxy);
		MyLogger.trace(logger, method, "DoHTTPUpload :: content :"+content);
		
		HttpServiceResponse response = new HttpServiceResponse();
		
		double accessTime = -1;
		double totalTime = -1;
		//long totalbytes = 200000;
		long totalbytes = 30000000;
		double debito = -1;
		
		long totalSendBytes = 0;
		
		String oldproxyaddress = null;
		String oldproxyport = null;
		
		try {
			
			//int CONNECTION_TIMEOUT = 10000;
			int CONNECTION_TIMEOUT = 60000;
			int CONNECTION_READ_TIMEOUT = 16000;
			
			String payloadFixed = "content0=";
			
			HttpURLConnection connection = null;
			DataOutputStream outputStream = null;
			String boundary = "*****";
			

			if (proxy != null) {
				
				if (!proxy.equals("")) {
					
					//get proxy address and port
					String address = proxy.substring(0,proxy.indexOf(":"));
					String port = proxy.substring(proxy.indexOf(":")+1,proxy.length());
			
					
					oldproxyaddress = System.getProperty("https.proxyHost");
					oldproxyport = System.getProperty("https.proxyPort");
					
					System.setProperty("https.proxyHost", address);
					System.setProperty("https.proxyPort", port);
				}
			}
			
			
			URL url = new URL(urllink);

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
			
				if (content == null) {
				
					final String alphabet = "0123456789qwertyuioplkjhgfdsazxcvbnm";
					final int N = alphabet.length();
					Random r = new Random();
		    
					for(int i=0;i<totalbytes;i++) {
					
						if ((System.currentTimeMillis() - startTime) > CONNECTION_READ_TIMEOUT) break;
					
						outputStream.writeBytes(alphabet.charAt(r.nextInt(N))+"");
						totalSendBytes++;
					}
				
				} else {
					
					outputStream.writeUTF(content);
					totalSendBytes = content.length();
					
				}
				
				totalTime = (System.currentTimeMillis() - startTime);
				
			//} catch(Exception ex ) {
				
			//}

			MyLogger.trace(logger, method, "DoHTTPUpload :: ask for response!");

				
			// Responses from the server (code and message)
			String serverResponseCode = Integer.toString(connection
					.getResponseCode());
			
			MyLogger.debug(logger, method, "serverResponseCode :"+serverResponseCode);
			
			String serverResponseMessage = connection.getResponseMessage();
			
			MyLogger.debug(logger, method, "serverResponseMessage :"+serverResponseMessage);

			outputStream.flush();
			outputStream.close();
			
			
			//totalTime = (System.currentTimeMillis() - startTime) / 1000;
			//debito = totalbytes / totalTime;
			
			long localBytesToBits = totalSendBytes * 8;
			
			//debito = (totalSendBytes * 1000) / (System.currentTimeMillis() - startTime);
			debito = ((localBytesToBits/ (totalTime/1000))/1000);  // Kbps
			
			MyLogger.debug(logger, method, "totalbytes :" + totalbytes);
			MyLogger.debug(logger, method, "totalTime :" + totalTime);
			MyLogger.debug(logger, method, "accessTime :" + accessTime);
			MyLogger.debug(logger, method, "debito :" + debito);
			
			if (oldproxyaddress != null && oldproxyport != null) {
				System.setProperty("https.proxyHost", oldproxyaddress);
				System.setProperty("https.proxyPort", oldproxyport);
			}
			
			response = new HttpServiceResponse(HttpServiceResponse.ResponseHttpServiceEnum.OK, accessTime, totalTime, (totalSendBytes/1000), debito, serverResponseMessage, urllink);
			
		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		if (oldproxyaddress != null && oldproxyport != null) {
			System.setProperty("https.proxyHost", oldproxyaddress);
			System.setProperty("https.proxyPort", oldproxyport);
		}
		
		return response;
	}
	
	public HttpServiceResponse DoHTTPDownload(String url, String proxy, String user_agent) {
		final String method = "DoHTTPDownload";
		
		MyLogger.trace(logger, method, "DoHTTPUpload :: url :"+url);
		MyLogger.trace(logger, method, "DoHTTPUpload :: proxy :"+proxy);
		MyLogger.trace(logger, method, "DoHTTPUpload :: user_agent :"+user_agent);
		
		HttpServiceResponse response = new HttpServiceResponse();
		
		double accessTime = -1;
		double totalTime = -1;
		long totalbytes = 0;
		double debito = -1;
		
		String oldproxyaddress = null;
		String oldproxyport = null;
		String olduseragent = null;

		try {
			
			//int CONNECTION_TIMEOUT = 10000;
			int CONNECTION_TIMEOUT = 60000;
			int CONNECTION_READ_TIMEOUT = 16000;

			long startTime = System.currentTimeMillis();
			
			if (user_agent != null) {
				if (!user_agent.equals("")) { 
					
					olduseragent = System.getProperty("http.agent");
					
					System.setProperty("http.agent", user_agent);
				}
			}

			if (proxy != null) {
				
				if (!proxy.equals("")) {
					
					//get proxy address and port
					String address = proxy.substring(0,proxy.indexOf(":"));
					String port = proxy.substring(proxy.indexOf(":")+1,proxy.length());
			
					
					oldproxyaddress = System.getProperty("https.proxyHost");
					oldproxyport = System.getProperty("https.proxyPort");
					
					System.setProperty("https.proxyHost", address);
					System.setProperty("https.proxyPort", port);
				}
			}
			
			 URLConnection con = new URL(url).openConnection();

			 accessTime = (System.currentTimeMillis() - startTime) / 1000;

			 //try {
				 //con.setRequestMethod("HEAD");
				 con.setConnectTimeout(CONNECTION_TIMEOUT);
				 con.setReadTimeout(CONNECTION_READ_TIMEOUT);

				 InputStream is = con.getInputStream();

				 BufferedInputStream bis = new BufferedInputStream(is);

				 StringBuilder sb = new StringBuilder();
				 try {		
					 int current = 0;
					 while ((current = bis.read()) != -1) {
						 
						 sb.append((char) current);
						 if ((System.currentTimeMillis() - startTime) > CONNECTION_READ_TIMEOUT) break;
						 
						 totalbytes++;
					 }
				 } catch (Exception ex) {
					 MyLogger.error(logger, method+" Error reading the streaming", ex);
				 }
			 //} catch(Exception ex) {
				 
			 //}
			
			//totalTime = (System.currentTimeMillis() - startTime) / 1000;
			//debito = totalbytes / totalTime;
				 
		
		    totalTime = (System.currentTimeMillis() - startTime);
				 
			long localBytesToBits = totalbytes * 8;
				 
			// totalTime está em millisec
			//debito = ((localBytesToBits * 1000) / (totalTime/1000))/1000;
			debito = ((localBytesToBits/ (totalTime/1000))/1000);  // Kbps
			
			MyLogger.debug(logger, method, "totalbytes :" + totalbytes);
			MyLogger.debug(logger, method, "totalTime :" + totalTime);
			MyLogger.debug(logger, method, "accessTime :" + accessTime);
			MyLogger.debug(logger, method, "debito :" + debito);
			MyLogger.debug(logger, method, "received_data :" + sb.toString());
			
			if (oldproxyaddress != null && oldproxyport != null) {
				System.setProperty("https.proxyHost", oldproxyaddress);
				System.setProperty("https.proxyPort", oldproxyport);
			}
			
			if (olduseragent != null) {
				System.setProperty("http.agent", olduseragent);
			}
			
			response = new HttpServiceResponse(HttpServiceResponse.ResponseHttpServiceEnum.OK, accessTime, totalTime, (totalbytes/1000), debito, sb.toString(), url);
			
		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		if (oldproxyaddress != null && oldproxyport != null) {
			System.setProperty("https.proxyHost", oldproxyaddress);
			System.setProperty("https.proxyPort", oldproxyport);
		}
		
		if (olduseragent != null) {
			System.setProperty("http.agent", olduseragent);
		}
		
		return response;
	}
	
	/*
	public HttpServiceResponse DoHTTPUpload(String urllink, String proxy, String content) {
		final String method = "DoHTTPUpload";
		
		HttpServiceResponse response = new HttpServiceResponse();
		
		double accessTime = -1;
		double totalTime = -1;
		//long totalbytes = 200000;
		long totalbytes = 30000000;
		double debito = -1;
		
		long startTime = -1;
		long endTime = -1;
		
		String oldproxyaddress = null;
		String oldproxyport = null;
		String serverResponseMessage = null;
		
		try {
			
			
			if (proxy != null) {
				
				if (!proxy.equals("")) {
					
					//get proxy address and port
					String address = proxy.substring(0,proxy.indexOf(":"));
					String port = proxy.substring(proxy.indexOf(":")+1,proxy.length());
			
					
					oldproxyaddress = System.getProperty("https.proxyHost");
					oldproxyport = System.getProperty("https.proxyPort");
					
					System.setProperty("https.proxyHost", address);
					System.setProperty("https.proxyPort", port);
				}
			}
			

				mTries = 0;

				mDeviceBandwidthSampler = DeviceBandwidthSampler.getInstance(TrafficType.Upload);
				MyLogger.debug(logger, method, "CurrentBandwidth :"+ mDeviceBandwidthSampler.getCurrentBandwidth());

				do {

					mDeviceBandwidthSampler.startSampling();

					try {

						StringBuilder sb = new StringBuilder();

						HttpClient httpclient = new DefaultHttpClient();
						HttpPost httppost = new HttpPost(linkUpload);
						
						byte[] listBytes = content.getBytes();
						InputStream stream = new ByteArrayInputStream(listBytes);
						InputStreamEntity reqEntity = new InputStreamEntity(stream,-1);
						reqEntity.setContentType("multipart/form-data;boundary=*****");
						reqEntity.setChunked(true); // Send in multiple parts if
													// needed
						httppost.setEntity(reqEntity);
						
						startTime = System.currentTimeMillis();
						HttpResponse responseHttp = httpclient.execute(httppost);
						endTime = System.currentTimeMillis();

						totalbytes = listBytes.length;
						totalTime = endTime - startTime;
						
					} catch (Exception ex) {
						MyLogger.error(logger, method, ex);
					}

					mDeviceBandwidthSampler.stopSampling();

					mConnectionBandwidth = mDeviceBandwidthSampler.getCurrentBandwidth();
					MyLogger.debug(logger, method, "CurrentBandwidth :"+ mConnectionBandwidth);

					mTries++;

					// wait for the result
				} while (mConnectionBandwidth == -1 && mTries < maxTries);
				
				long roundedInt = Math.round(mConnectionBandwidth * 100);
				double mConnectionBandwidth = (double) roundedInt/100;
				MyLogger.debug(logger, method, "CurrentBandwidth :"+mConnectionBandwidth);
			
				debito = mConnectionBandwidth;
				accessTime = mDeviceBandwidthSampler.getAccessTime();
			
				MyLogger.debug(logger, method, "totalbytes :" + totalbytes);
				MyLogger.debug(logger, method, "totalTime :" + totalTime);
				MyLogger.debug(logger, method, "accessTime :" + accessTime);
				MyLogger.debug(logger, method, "debito :" + debito);
			
				if (oldproxyaddress != null && oldproxyport != null) {
					System.setProperty("https.proxyHost", oldproxyaddress);
					System.setProperty("https.proxyPort", oldproxyport);
				}
			
				response = new HttpServiceResponse(ResponseHttpServiceEnum.OK, accessTime, totalTime, (totalbytes/1000), debito, serverResponseMessage, urllink);
			
		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		if (oldproxyaddress != null && oldproxyport != null) {
			System.setProperty("https.proxyHost", oldproxyaddress);
			System.setProperty("https.proxyPort", oldproxyport);
		}
		
		return response;
	}
	
	
	
	public HttpServiceResponse DoHTTPDownload(String url, String proxy, String user_agent) {
		final String method = "DoHTTPDownload";
		
		HttpServiceResponse response = new HttpServiceResponse();
		
		double accessTime = -1;
		double totalTime = -1;
		long totalbytes = 0;
		double debito = -1;
		
		String oldproxyaddress = null;
		String oldproxyport = null;
		String olduseragent = null;
		
		long startTime = -1;
		long endTime = -1;

		try {
			
			if (user_agent != null) {
				if (!user_agent.equals("")) { 
					
					olduseragent = System.getProperty("http.agent");
					
					System.setProperty("http.agent", user_agent);
				}
			}

			if (proxy != null) {
				
				if (!proxy.equals("")) {
					
					//get proxy address and port
					String address = proxy.substring(0,proxy.indexOf(":"));
					String port = proxy.substring(proxy.indexOf(":")+1,proxy.length());
			
					
					oldproxyaddress = System.getProperty("https.proxyHost");
					oldproxyport = System.getProperty("https.proxyPort");
					
					System.setProperty("https.proxyHost", address);
					System.setProperty("https.proxyPort", port);
				}
			}
			
			
			mTries = 0;
			
			mDeviceBandwidthSampler = DeviceBandwidthSampler.getInstance(TrafficType.Download);
		    
			do{
				
				mDeviceBandwidthSampler.startSampling();
				
				StringBuilder sb = new StringBuilder();
				
				try {
				
					// Open a stream to download the image from our URL.
					startTime = System.currentTimeMillis();
					URLConnection connection = new URL(linkDownload).openConnection();
					connection.setUseCaches(false);
					connection.connect();
					InputStream input = connection.getInputStream();
					try {
						byte[] buffer = new byte[1024];

						// Do some busy waiting while the stream is open.
						int readByte = 0;
						do {
							readByte = input.read(buffer);
							totalbytes += readByte;
						}
						while (readByte != -1);
						totalbytes++;
						
					} finally {
						input.close();
					}
					endTime = System.currentTimeMillis();
					
					totalTime = endTime - startTime;
			
				} catch (IOException ex) {
					MyLogger.error(logger, method, ex);
				}
			
			mDeviceBandwidthSampler.stopSampling();
			
			mConnectionBandwidth = mDeviceBandwidthSampler.getCurrentBandwidth();
			//Logger.v(className, method, LogType.Trace,"CurrentBandwidth :"+mConnectionBandwidth);
			
			mTries++;
		    
		    //wait for the result
			} while(mConnectionBandwidth == -1 && mTries < maxTries);
		    	//Thread.sleep(1000);
			
			long roundedInt = Math.round(mConnectionBandwidth * 100);
			double mConnectionBandwidth = (double) roundedInt/100;
			//Logger.v(className, method, LogType.Trace,"CurrentBandwidth :"+mConnectionBandwidth);
			
			
			MyLogger.debug(logger, method, "totalbytes :" + totalbytes);
			MyLogger.debug(logger, method, "totalTime :" + totalTime);
			MyLogger.debug(logger, method, "accessTime :" + accessTime);
			MyLogger.debug(logger, method, "debito :" + debito);
			MyLogger.debug(logger, method, "received_data :" + "");
			
			if (oldproxyaddress != null && oldproxyport != null) {
				System.setProperty("https.proxyHost", oldproxyaddress);
				System.setProperty("https.proxyPort", oldproxyport);
			}
			
			if (olduseragent != null) {
				System.setProperty("http.agent", olduseragent);
			}
			
			response = new HttpServiceResponse(ResponseHttpServiceEnum.OK, accessTime, totalTime, (totalbytes/1000), debito, "", url);
			
		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		if (oldproxyaddress != null && oldproxyport != null) {
			System.setProperty("https.proxyHost", oldproxyaddress);
			System.setProperty("https.proxyPort", oldproxyport);
		}
		
		if (olduseragent != null) {
			System.setProperty("http.agent", olduseragent);
		}
		
		return response;
	}
	*/
	
	
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
	

	public HttpServiceResponse DoHTTPMAxUpload2() {
		final String method = "DoHTTPMAxUpload";

		HttpServiceResponse response = new HttpServiceResponse();
		
		startDate = 0;
		stopDate = 0;
		internalByteCounter = 0;

		ArrayList<Thread> thread = new ArrayList<Thread>();
		
		final StringBuilder sb = new StringBuilder();

		for (int i = 0; i < numThread; i++) {
			final int index = i;
			Thread threadLocal = new Thread(new Runnable() {
				public void run() {

					MyLogger.debug(logger, method, "thread" + (index + 1) + " init time :" + System.currentTimeMillis());
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

						MyLogger.trace(logger, method, "DoHTTPUpload :: ask for response!");
							
						// Responses from the server (code and message)
						String serverResponseCode = Integer.toString(connection
								.getResponseCode());
						
						MyLogger.debug(logger, method, "serverResponseCode :"+serverResponseCode);
						
						String serverResponseMessage = connection.getResponseMessage();
						sb.append(serverResponseMessage);
						
						MyLogger.debug(logger, method, "serverResponseMessage :"+serverResponseMessage);

						outputStream.flush();
						outputStream.close();

					} catch (Exception ex) {
						MyLogger.error(logger, method, ex);
					}

					threadStopped();
					MyLogger.debug(logger, method, "thread" + (index + 1) + " end time :" + System.currentTimeMillis());
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
			MyLogger.debug(logger, method, "totalBytes :" + totalBytes);

			long totalTime = stopDate - startDate;
			MyLogger.debug(logger, method, "totalTime :" + totalTime);

			// totalTime está em millisec
			double debito = (totalBytes * 1000) / totalTime;

			MyLogger.debug(logger, method, "debito :" + debito);

			response = new HttpServiceResponse(HttpServiceResponse.ResponseHttpServiceEnum.OK, 0, totalTime,
					totalBytes / 8, debito, sb.toString(), linkUpload);
		}

		MyLogger.debug(logger, method, "startDate :" + startDate);
		MyLogger.debug(logger, method, "stopDate :" + stopDate);

		return response;
	}

	public HttpServiceResponse DoHTTPMAxDownload2() {
		final String method = "DoHTTPMAxDownload";
		
		HttpServiceResponse response = new HttpServiceResponse();
		
		startDate = 0;
		stopDate = 0;
		internalByteCounter = 0;
		
		// Vou arrancar 10 threads e quando tiver as 10 threads a fazer download em simultaneo, vou ver qual é a largura de banda nesse instante
		
		ArrayList<Thread> thread = new ArrayList<Thread>();
		
		final StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < numThread; i++) {
			final int index = i;
			Thread threadLocal = new Thread(new Runnable() {
				public void run() {
					
					MyLogger.debug(logger, method, "thread"+(index+1)+" init time :" + System.currentTimeMillis());
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
									 
									 sb.append((char) current);
									 if ((System.currentTimeMillis() - startTime) > CONNECTION_READ_TIMEOUT) break;
									 
									 incCounter();
								 }
							 } catch (Exception ex) {
								 MyLogger.error(logger, method+" :: Error reading the streaming", ex);
								 
							 }
					
					} catch(Exception ex) {
						MyLogger.error(logger, method, ex);
					}
					
					threadStopped();
					MyLogger.debug(logger, method, "thread"+(index+1)+" end time :" + System.currentTimeMillis());
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
			MyLogger.debug(logger, method, "totalBytes :" + totalBytes);
			
			long totalTime = stopDate - startDate;
			MyLogger.debug(logger, method, "totalTime :" + totalTime);
				 
			// totalTime está em millisec
			double debito = (totalBytes * 1000) / totalTime;
			
			MyLogger.debug(logger, method, "debito :" + debito);
			
			response = new HttpServiceResponse(HttpServiceResponse.ResponseHttpServiceEnum.OK, 0, totalTime, totalBytes/8, debito, sb.toString(), linkDownload);
		}
		
		MyLogger.debug(logger, method, "startDate :" + startDate);
		MyLogger.debug(logger, method, "stopDate :" + stopDate);
		
		return response;
	}
	
	public static byte[] getStringToUTF8(String text) {
		final String method = "getStringToUTF8_string";
		
		try {
			return text.getBytes("UTF-8");
			
		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}
	
	public static String getStringFromUTF8(byte[] text) {
		final String method = "getStringFromUTF8_byte";
		
		try {
			
			return new String(text, "US-ASCII");
			
		} catch (UnsupportedEncodingException ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}
	
}

