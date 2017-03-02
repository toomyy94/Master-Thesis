package PT.PTInov.ArQoSPocket.Service;

import java.io.Serializable;

import PT.PTInov.ArQoSPocket.Enums.ResponseEnum;

public class PingResponse implements Serializable{

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
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("enumResponse :"+enumResponse+"\n");
		sb.append("max :"+max+"\n");
		sb.append("min :"+min+"\n");
		sb.append("media :"+media+"\n");
		sb.append("packetTransmitted :"+packetTransmitted+"\n");
		sb.append("packetReceived :"+packetReceived+"\n");
		sb.append("percentPacketLost :"+percentPacketLost+"\n");
		sb.append("packetSize :"+packetSize+"\n");
		sb.append("resultINPUT :"+resultINPUT+"\n");
		sb.append("resultERROR :"+resultERROR+"\n");
		
		return sb.toString();
	}
}
