package PT.PTInov.ArQoSPocket.JSONConnector;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import PT.PTInov.ArQoSPocket.Utils.LogType;
import PT.PTInov.ArQoSPocket.Utils.Logger;
import PT.PTInov.ArQoSPocket.Utils.MyWatchDog;
import PT.PTInov.ArQoSPocket.Utils.WacthDogInterface;
import android.util.Log;


public class JSONConnector implements WacthDogInterface{

	private final String tag = "JSONConnector";
	
	private String connectHost = null;
	
	private URLConnection conn = null;
	
	private MyWatchDog myWatchDog = null;
	
	private InputStream is = null;
	private InputStreamReader isr = null;
	private BufferedReader rd = null;
	
	public JSONConnector(String host) {
		connectHost = host;
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
			
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(10000);
			
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
