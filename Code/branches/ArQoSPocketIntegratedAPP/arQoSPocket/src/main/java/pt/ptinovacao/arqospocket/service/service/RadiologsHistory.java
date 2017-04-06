package pt.ptinovacao.arqospocket.service.service;

import android.graphics.Bitmap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pt.ptinovacao.arqospocket.service.http.IWebRequestCallBack;
import pt.ptinovacao.arqospocket.service.http.WebRequestThreadWrapper;
import pt.ptinovacao.arqospocket.service.http.WebResponse;
import pt.ptinovacao.arqospocket.service.interfaces.IAnomaliesHistory;
import pt.ptinovacao.arqospocket.service.interfaces.IRadiologsHistory;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;
import pt.ptinovacao.arqospocket.service.structs.Anomalies;
import pt.ptinovacao.arqospocket.service.structs.Radiologs;
import pt.ptinovacao.arqospocket.service.utils.SerializablePair;
import pt.ptinovacao.arqospocket.service.utils.WebActionRequestInfo;

public class RadiologsHistory implements Serializable, IWebRequestCallBack {

	private static final long serialVersionUID = 1L;

	private final static Logger logger = LoggerFactory.getLogger(RadiologsHistory.class);

	//private Map<Anomalies, Boolean> anomalies_history_list = null;
	private List<SerializablePair<Radiologs, Boolean>> radiologs_history_list = null;
	private Map<String, Radiologs> pending_requests = null;

	public RadiologsHistory() {
		final String method = "RadiologsHistory";
		
		try {
			
			//anomalies_history_list = new TreeMap<Anomalies, Boolean>();
			radiologs_history_list = new ArrayList<SerializablePair<Radiologs, Boolean>>();
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	public boolean add_radiolog_to_history(Radiologs radiologs) {
		final String method = "add_radiolog_to_history";
		
		try {

			radiologs_history_list.add(new SerializablePair<Radiologs, Boolean>(radiologs, false));
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
			ex.printStackTrace();
		}
		
		return false;
	}
	
	public List<SerializablePair<Radiologs, Boolean>> get_all_radiologs_history() {
		final String method = "get_all_radiologs_history";
		
		return radiologs_history_list;
	}
	
	public List<IRadiologsHistory> get_all_radiologs_history_only() {
		final String method = "get_all_radiologs_history_only";
		
		try {
			
			List<IRadiologsHistory> list =  new ArrayList<IRadiologsHistory>();
			
			for (SerializablePair<Radiologs, Boolean> ia :radiologs_history_list)
				list.add(ia.first);
			
			return list;
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}
	
	public void send_all_radiologs_to_server() {
		final String method = "send_all_radiologs_to_server";
		
		try {
			
			pending_requests = new TreeMap<String, Radiologs>();
			
			for (SerializablePair<Radiologs, Boolean> ia :radiologs_history_list) {
				if (ia.second == false){

					String request_code = WebRequestThreadWrapper.generateOperationCode();
					WebRequestThreadWrapper webRequestThreadWrapper = new WebRequestThreadWrapper(request_code, this);
					webRequestThreadWrapper.doPOSTAsync(WebActionRequestInfo.REPORT_RADIOLOG_LINK, null, ia.first.buildJson());
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
			
			sb.append("\nradiologs_history_list :"+radiologs_history_list.toString());
			
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
