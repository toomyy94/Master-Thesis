package com.serverlogs.connector;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import PT.PTInov.ArQoSPocketEDP.Utils.LogType;
import PT.PTInov.ArQoSPocketEDP.Utils.Logger;

public class WebPost {

	private final static String tag = "WebPost";

	private static final int CONNECTION_TIMEOUT = 5000;

	public static WebResponse doPost(String endpoint, Map<String, String> params, String Body) {

		try {
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
			byte[] bytes = body.getBytes();
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setConnectTimeout(CONNECTION_TIMEOUT);
			conn.setFixedLengthStreamingMode(bytes.length);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type","application/json");
			
			// post the request
			OutputStream out = conn.getOutputStream();
			out.write(bytes);
			out.close();

			String result = "";
			String line;
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			while ((line = rd.readLine()) != null) {
				result += line;
			}
			rd.close();
			conn.getInputStream().close();

			return new WebResponse(conn.getResponseCode(),
					conn.getResponseMessage(), result);

		} catch (Exception ex) {
			Logger.v(tag, "doPost", LogType.Error, ex.toString());
		}

		return null;
	}
	
}
