package PT.PTInov.ArQoSPocketEDP.DataStructs;

public class MyNeighboringCellInfo {
	
	private final static String tag = "MyNeighboringCellInfo";
	
	private int cid;
	private int lac;
	private int networkType;
	private int psc;
	private int rssi;
	
	public MyNeighboringCellInfo(int cid, int lac, int networkType, int psc, int rssi) {
		this.cid = cid;
		this.lac = lac;
		this.networkType = networkType;
		this.psc = psc;
		this.rssi = rssi;
	}
	
	public int getCid() {
		return this.cid;
	}
	
	public int getLac() {
		return this.lac;
	}
	
	public int getNetworkType() {
		return this.networkType;
	}
	
	public int getPsc() {
		return this.psc;
	}
	
	public int getRssi() {
		return this.rssi;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("\ncid :"+cid);
		sb.append("\nlac :"+lac);
		sb.append("\nnetworkType :"+networkType);
		sb.append("\npsc :"+psc);
		sb.append("\nrssi :"+rssi);
		
		return sb.toString();
	}

}
