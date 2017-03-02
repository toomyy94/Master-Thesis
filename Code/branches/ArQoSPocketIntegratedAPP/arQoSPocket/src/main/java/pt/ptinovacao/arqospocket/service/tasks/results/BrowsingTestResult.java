package pt.ptinovacao.arqospocket.service.tasks.results;

import pt.ptinovacao.arqospocket.service.tasks.structs.WebResponse;


public class BrowsingTestResult {

	// tempo que o servidor demora a começar a responder ao request
	private long responseTime;
	
	// tempo total ate que seja lida toda a resposta
	private long totalResponseTime;
	
	// dimensão da resposta
	private long responseSize;
	
	// link utilizado no pedido
	private String requestURL;
	
	// resposta dada pelo servidor
	private WebResponse response;
	
	public BrowsingTestResult(long responseTime, long totalResponseTime, long responseSize, String requestURL, WebResponse response) {
		this.responseTime = responseTime;
		this.totalResponseTime = totalResponseTime;
		this.responseSize = responseSize;
		this.requestURL = requestURL;
		this.response = response;
	}
	
	public long get_response_time() {
		return responseTime;
	}
	
	public long get_total_response_time() {
		return totalResponseTime;
	}
	
	public long get_response_size() {
		return responseSize;
	}
	
	public String get_request_url() {
		return requestURL;
	}
	
	public WebResponse get_response() {
		return response;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("responseTime :"+responseTime);
		sb.append("\ntotalResponseTime :"+totalResponseTime);
		sb.append("\nresponseSize :"+responseSize);
		sb.append("\nrequestURL :"+requestURL);
		sb.append("\nresponse :"+response.toString());
		
		return sb.toString();
	}
}
