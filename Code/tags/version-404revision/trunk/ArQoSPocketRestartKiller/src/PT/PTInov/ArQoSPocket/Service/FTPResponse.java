package PT.PTInov.ArQoSPocket.Service;

import java.io.Serializable;

import PT.PTInov.ArQoSPocket.Enums.ResponseEnum;

public class FTPResponse implements Serializable {
	
	private final static String tag = "HttpResponse";
	
	private ResponseEnum ExecState = ResponseEnum.NA;
	
	private double totalTime = -1;
	private long totalbytes = 230;
	private double debito = -1;
	
	public FTPResponse() {
		
		this.ExecState = ResponseEnum.NA;
		
		this.totalTime = -1;
		this.totalbytes = 230;
		this.debito = -1;
	}
	
	public FTPResponse(ResponseEnum pExecState, double pTotalTime, long pTotalbytes, double pDebito) {
		this.ExecState = pExecState;
		this.totalTime = pTotalTime;
		this.totalbytes = pTotalbytes;
		this.debito = pDebito;
	}
	
	public ResponseEnum getExecState() {
		return ExecState;
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
		sb.append("totalTime :"+totalTime+"\n");
		sb.append("totalbytes :"+totalbytes+"\n");
		sb.append("debito :"+debito+"\n");
		
		return sb.toString();
	}
}
