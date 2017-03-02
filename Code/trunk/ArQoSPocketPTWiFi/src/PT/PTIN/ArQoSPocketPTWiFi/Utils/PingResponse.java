package PT.PTIN.ArQoSPocketPTWiFi.Utils;

import PT.PTIN.ArQoSPocketPTWiFi.Enums.ResponseEnum;

public class PingResponse {

	private final static String tag = "PingResponse";
	
	private ResponseEnum enumResponse = ResponseEnum.NA;
	
	private String max;
	private String min;
	private String media;
	private String packetTransmitted;
	private String packetReceived;
	private String percentPacketLost;
	private long packetSize;
	
	private String resultINPUT;
	private String resultERROR;
	
	public PingResponse() {
		this.enumResponse = ResponseEnum.NA;
		
		this.max = "-1";
		this.min = "-1";
		this.media = "-1";
		this.packetTransmitted = "-1";
		this.packetReceived = "-1";
		this.percentPacketLost = "-1";
		
		this.resultINPUT = "NA";
		this.resultERROR = "NA";
	}
	
	public PingResponse(ResponseEnum penumResponse, String pmax, String pmin, String pmedia, String ppacketTransmitted, String ppacketReceived, String ppercentPacketLost, String presultINPUT, String presultERROR, long packetSize) {
		
		this.enumResponse = penumResponse;
		
		this.max = pmax;
		this.min = pmin;
		this.media = pmedia;
		this.packetTransmitted = ppacketTransmitted;
		this.packetReceived = ppacketReceived;
		this.percentPacketLost = ppercentPacketLost;
		
		this.resultINPUT = presultINPUT;
		this.resultERROR = presultERROR;
		
		this.packetSize = packetSize;
	}
	
	public ResponseEnum getResponseEnum() {
		return enumResponse;
	}
	
	public long getPacketSize() {
		return packetSize;
	}
	
	public String getMax() {
		return max;
	}
	
	public String getMin() {
		return min;
	}
	
	public String getMedia() {
		return media;
	}
	
	public String getPacketTransmitted() {
		return packetTransmitted;
	}
	
	public String getPacketReceived() {
		return packetReceived;
	}
	
	public String getPercentPacketLost() {
		return percentPacketLost;
	}
	
	public String getResultINPUT() {
		return resultINPUT;
	}
	
	public String getResultERROR() {
		return resultERROR;
	}
}
