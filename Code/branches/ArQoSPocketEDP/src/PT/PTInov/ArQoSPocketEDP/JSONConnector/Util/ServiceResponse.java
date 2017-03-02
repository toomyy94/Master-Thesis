package PT.PTInov.ArQoSPocketEDP.JSONConnector.Util;

import java.util.ArrayList;

public class ServiceResponse {

	private String timeoend;
	private String macroresultid;
	private String executionstatus;
	private String servicecode;
	private ArrayList<ExecutionResults> executionResults;
	
	public ServiceResponse(String timeoend, String macroresultid, String executionstatus, String servicecode, ArrayList<ExecutionResults> executionResults) {
		this.timeoend = timeoend;
		this.macroresultid = macroresultid;
		this.executionstatus = executionstatus;
		this.executionResults = executionResults;
		this.servicecode = servicecode;
	}

	public String getTimeoend() {
		return timeoend;
	}

	public void setTimeoend(String timeoend) {
		this.timeoend = timeoend;
	}

	public String getMacroresultid() {
		return macroresultid;
	}

	public void setMacroresultid(String macroresultid) {
		this.macroresultid = macroresultid;
	}

	public String getExecutionstatus() {
		return executionstatus;
	}

	public void setExecutionstatus(String executionstatus) {
		this.executionstatus = executionstatus;
	}

	public ArrayList<ExecutionResults> getExecutionResults() {
		return executionResults;
	}

	public void setExecutionResults(ArrayList<ExecutionResults> executionResults) {
		this.executionResults = executionResults;
	}
	
	public String getServicecode() {
		return servicecode;
	}
	
	public ExecutionResults getLastResult() {
		return executionResults.get(executionResults.size()-1);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("timeoend :"+timeoend+"\n");
		sb.append("macroresultid :"+macroresultid+"\n");
		sb.append("executionstatus :"+executionstatus+"\n");
		sb.append("servicecode :"+servicecode+"\n");
		sb.append("executionResults :"+executionResults.toString()+"\n");
		
		return sb.toString();
	}
}
