package PT.PTInov.ArQoSPocket.Service;

import java.io.Serializable;

import PT.PTInov.ArQoSPocket.Enums.ResponseEnum;

public class HttpServiceResponse implements Serializable {
	
	private final static String tag = "HttpResponse";
	
	private ResponseEnum ExecState = ResponseEnum.NA;
	
	private double accessTime = -1;
	private double totalTime = -1;
	private long totalbytes = 230;
	private double debito = -1;
	
	public HttpServiceResponse() {
		
		this.ExecState = ResponseEnum.NA;
		
		this.accessTime = -1;
		this.totalTime = -1;
		this.totalbytes = 230;
		this.debito = -1;
	}
	
	public HttpServiceResponse(ResponseEnum pExecState, double pAccessTime, double pTotalTime, long pTotalbytes, double pDebito) {
		this.ExecState = pExecState;
		this.accessTime = pAccessTime;
		this.totalTime = pTotalTime;
		this.totalbytes = pTotalbytes;
		this.debito = pDebito;
	}
	
	public ResponseEnum getExecState() {
		return ExecState;
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
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("ExecState :"+ExecState+"\n");
		sb.append("accessTime :"+accessTime+"\n");
		sb.append("totalTime :"+totalTime+"\n");
		sb.append("totalbytes :"+totalbytes+"\n");
		sb.append("debito :"+debito+"\n");
		
		return sb.toString();
	}
}
