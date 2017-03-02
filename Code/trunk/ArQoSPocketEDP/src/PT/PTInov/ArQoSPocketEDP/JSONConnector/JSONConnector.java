package PT.PTInov.ArQoSPocketEDP.JSONConnector;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import PT.PTInov.ArQoSPocketEDP.JSONConnector.Util.WebResponse;
import PT.PTInov.ArQoSPocketEDP.Utils.LogType;
import PT.PTInov.ArQoSPocketEDP.Utils.Logger;
import PT.PTInov.ArQoSPocketEDP.Utils.MyWatchDog;
import PT.PTInov.ArQoSPocketEDP.Utils.WacthDogInterface;
import android.annotation.SuppressLint;
import android.util.Log;


@SuppressLint("ParserError")
public class JSONConnector implements WacthDogInterface{

	private final String tag = "JSONConnector";
	
	private String connectHost = null;
	
	private URLConnection conn = null;
	
	private MyWatchDog myWatchDog = null;
	
	private InputStream is = null;
	private InputStreamReader isr = null;
	private BufferedReader rd = null;
	
	private final static int READ_TIMEOUT = 60000;
	private final static int CONNECTION_TIMEOUT = 60000;
	
	public JSONConnector(String host) {
		connectHost = host;
	}
	
	protected WebResponse SendXmlPost(String bodyData) {
		String metodo = "SendXmlPost";
		
		try {
			
			Logger.v(tag, metodo, LogType.Trace, "......................connectHost :"+connectHost);
			
			// Send data
			URL url = new URL(connectHost);
			conn = url.openConnection();
			
			conn.setDoOutput(true);
			
			conn.setConnectTimeout(CONNECTION_TIMEOUT);
			conn.setReadTimeout(READ_TIMEOUT);
			
			conn.setRequestProperty("Content-Type", "application/xml");

			OutputStreamWriter wr = new OutputStreamWriter(
					conn.getOutputStream());
			
			wr.write(bodyData);
			
			wr.flush();
			
			// Get the response
			StringBuilder sb = new StringBuilder();			
			
			is = conn.getInputStream();
			isr = new InputStreamReader(is);
			
			rd = new BufferedReader(isr);
			
			String line;
			while ((line = rd.readLine()) != null) {
				// Process line...
				sb.append(line);
			}
			
			wr.close();
			rd.close();
			
			Logger.v(tag, metodo, LogType.Debug, "......................response :"+sb.toString());
			
			String cookie = null;
			String headerName = null;
			for (int i=1; (headerName = conn.getHeaderFieldKey(i))!=null; i++) {
				if (headerName.equals("Set-Cookie")) {                  
					cookie = conn.getHeaderField(i);
				}
			}
			
			Logger.v(tag, metodo, LogType.Debug, "Vou devolver o resultado!");
			
			return new WebResponse(200, sb.toString(), null, cookie);

		} catch (Exception e) {
			Logger.v(tag, LogType.Error, "SendPost ERROR :" + e.toString());
		}
		
		return null;
	}

	protected String SendPost(String bodyData) {
		
		String metodo = "SendPost";
		
		try {
			
			Logger.v(tag, metodo, LogType.Trace, "......................connectHost :"+connectHost);
			
			// Send data
			URL url = new URL(connectHost);
			
			Logger.v(tag, metodo, LogType.Trace, "...................... 1");
			
			conn = url.openConnection();
			
			Logger.v(tag, metodo, LogType.Trace, "...................... 2");
			
			conn.setDoOutput(true);
			
			Logger.v(tag, metodo, LogType.Trace, "...................... 3");
			
			conn.setReadTimeout(READ_TIMEOUT);
			conn.setConnectTimeout(CONNECTION_TIMEOUT);
			Logger.v(tag, metodo, LogType.Trace, "......................readTime out :"+conn.getReadTimeout());
			Logger.v(tag, metodo, LogType.Trace, "......................readTime out :"+conn.getConnectTimeout());
			
			Logger.v(tag, metodo, LogType.Trace, "...................... 4");
			
			conn.setRequestProperty("Content-Type", "application/json");
			
			Logger.v(tag, metodo, LogType.Trace, "...................... 5");

			OutputStreamWriter wr = new OutputStreamWriter(
					conn.getOutputStream());
			
			Logger.v(tag, metodo, LogType.Trace, "...................... 6");
			
			wr.write(bodyData);
			
			Logger.v(tag, metodo, LogType.Trace, "...................... 7");
			
			wr.flush();

			Logger.v(tag, metodo, LogType.Trace, "...................... 8");
			
			// Get the response
			StringBuilder sb = new StringBuilder();
			
			Logger.v(tag, metodo, LogType.Trace, "...................... 9");
			
			// Active watch dog
			//myWatchDog = new MyWatchDog(5000, this);
			//myWatchDog.start();
			
			is = conn.getInputStream();
			
			Logger.v(tag, metodo, LogType.Trace, "...................... 9.1");
			
			isr = new InputStreamReader(is);
			
			Logger.v(tag, metodo, LogType.Trace, "...................... 9.2");
			
			rd = new BufferedReader(isr);
			
			Logger.v(tag, metodo, LogType.Trace, "...................... 9.3");
			
			//myWatchDog.cancelWatchDog();
			
			Logger.v(tag, metodo, LogType.Trace, "...................... 10");
			
			String line;
			while ((line = rd.readLine()) != null) {
				// Process line...
				sb.append(line);
			}
			
			Logger.v(tag, metodo, LogType.Trace, "...................... 11");
			
			wr.close();
			rd.close();
			
			Logger.v(tag, metodo, LogType.Debug, "......................response :"+sb.toString());
			
			return sb.toString();

		} catch (Exception e) {
			Logger.v(tag, LogType.Error, "SendPost ERROR :" + e.toString());
		}
		
		return null;
	}

	public void WacthDog() {
		// TODO Auto-generated method stub
		Logger.v(tag, LogType.Error, "watch dog!");
		
		conn = null;
		
		is = null;
		isr = null;
		
		rd = null;
	}
}
