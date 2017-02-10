package pt.ptinovacao.arqospocket.service.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.graphics.Bitmap;

import pt.ptinovacao.arqospocket.service.http.IWebRequestCallBack;
import pt.ptinovacao.arqospocket.service.utils.SerializablePair;
import pt.ptinovacao.arqospocket.service.http.WebRequestThreadWrapper;
import pt.ptinovacao.arqospocket.service.http.WebResponse;
import pt.ptinovacao.arqospocket.service.interfaces.IAnomaliesHistory;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;
import pt.ptinovacao.arqospocket.service.structs.Anomalies;
import pt.ptinovacao.arqospocket.service.utils.WebActionRequestInfo;

public class AnomaliesHistory implements Serializable, IWebRequestCallBack {

	private static final long serialVersionUID = 1L;

	private final static Logger logger = LoggerFactory.getLogger(AnomaliesHistory.class);
	
	//private Map<Anomalies, Boolean> anomalies_history_list = null;
	private List<SerializablePair<Anomalies, Boolean>> anomalies_history_list = null;
	private Map<String, Anomalies> pending_requests = null;
	
	public AnomaliesHistory() {
		final String method = "AnomaliesHistory"; 
		
		try {
			
			//anomalies_history_list = new TreeMap<Anomalies, Boolean>();
			anomalies_history_list = new ArrayList<SerializablePair<Anomalies, Boolean>>();
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	public boolean add_anomalie_to_history(Anomalies anomalies) {
		final String method = "add_anomalie_to_history";
		
		try {
			
			anomalies_history_list.add(new SerializablePair<Anomalies, Boolean>(anomalies, false));
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
			ex.printStackTrace();
		}
		
		return false;
	}
	
	public List<SerializablePair<Anomalies, Boolean>> get_all_anomalies_history() {
		final String method = "get_all_anomalies_history"; 
		
		return anomalies_history_list;
	}
	
	public List<IAnomaliesHistory> get_all_anomalies_history_only() {
		final String method = "get_all_anomalies_history_only";
		
		try {
			
			List<IAnomaliesHistory> list =  new ArrayList<IAnomaliesHistory>();
			
			for (SerializablePair<Anomalies, Boolean> ia :anomalies_history_list)
				list.add(ia.first);
			
			return list;
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}
	
	public void send_all_anomalies_to_server() {
		final String method = "send_all_anomalies_to_server";
		
		try {
			
			pending_requests = new TreeMap<String, Anomalies>();
			
			for (SerializablePair<Anomalies, Boolean> ia :anomalies_history_list) {
				if (ia.second == false){
				
					
					String request_code = WebRequestThreadWrapper.generateOperationCode();
					WebRequestThreadWrapper webRequestThreadWrapper = new WebRequestThreadWrapper(request_code, this);
					webRequestThreadWrapper.doPOSTAsync(WebActionRequestInfo.REPORT_ANOMALIA_LINK, null, ia.first.buildJson());
					pending_requests.put(request_code, ia.first);
				}
			}
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	public String toString() {
		final String method = "toString";
		
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("\nanomalies_history_list :"+anomalies_history_list.toString());
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	}

	@Override
	public void WebRequestComplete(WebResponse response, String requestCode) {
		final String method = "WebRequestComplete";
		
		try {
		
			// dependendo da respota tenho de remover da lista de pedidos e atulizar o estado de entrega da anomalia
			
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}

	@Override
	public void PhotoDownloadComplete(Bitmap photo, String requestCode) {

	}
}
