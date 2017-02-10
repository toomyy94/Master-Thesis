package pt.ptinovacao.arqospocket.service.tasks.results;

import java.io.Serializable;

public class HttpServiceResponse implements Serializable {
	
	private final static String tag = "HttpResponse";
	
	private ResponseHttpServiceEnum ExecState = ResponseHttpServiceEnum.NA;
	
	private String url = "";
	private double accessTime = -1;
	private double totalTime = -1;
	private long totalbytes = -1;
	private double debito = -1;
	private String received_data = null;
	
	public HttpServiceResponse() {
		
		this.ExecState = ResponseHttpServiceEnum.NA;
		
		this.accessTime = -1;
		this.totalTime = -1;
		this.totalbytes = -1;
		this.debito = -1;
		this.received_data = null;
	}
	
	public HttpServiceResponse(ResponseHttpServiceEnum pExecState, double pDebito, String url) {
		this.ExecState = pExecState;
		this.accessTime = -1;
		this.totalTime = -1;
		this.totalbytes = -1;
		this.debito = pDebito;
		this.received_data = null;
		this.url = url;
	}
	
	public HttpServiceResponse(ResponseHttpServiceEnum pExecState, double pAccessTime, double pTotalTime, long pTotalbytes, double pDebito, String received_data, String url) {
		this.ExecState = pExecState;
		this.accessTime = pAccessTime;
		this.totalTime = pTotalTime;
		this.totalbytes = pTotalbytes;
		this.debito = pDebito;
		this.received_data = received_data;
		this.url = url;
	}
	
	public ResponseHttpServiceEnum getExecState() {
		return ExecState;
	}
	
	public String getUrl() {
		return url;
	}
	
	public double getAccessTime() {
		return accessTime;
	}

	public double getTotalTime() {
		return totalTime;
	}
	
	public long getTotalbytes() {
		return totalbytes;
	}
	
	public double getDebito() {
		return debito;
	}
	
	public String getReceived_data() {
		return received_data;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("ExecState :"+ExecState+"\n");
		sb.append("url :"+url+"\n");
		sb.append("accessTime :"+accessTime+"\n");
		sb.append("totalTime :"+totalTime+"\n");
		sb.append("totalbytes :"+totalbytes+"\n");
		sb.append("debito :"+debito+"\n");
		sb.append("received_data :"+received_data+"\n");
		
		return sb.toString();
	}
	
	public enum ResponseHttpServiceEnum {
		OK, NOCONNECTION, NA, FAIL
	}
}