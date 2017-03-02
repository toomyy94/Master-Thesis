package PT.PTInov.ArQoSPocket.Utils;

public class ObservedCellObject {
	
	private int asuLevel;
	private int signalStrengthDbm;
	private int cid;
	private int lac;
	private int mcc;
	private int mnc;
	private int psc;
	
	public ObservedCellObject(int asuLevel, int signalStrengthDbm, int cid, int lac, int mcc, int mnc, int psc) {
		
		this.asuLevel = asuLevel;
		this.signalStrengthDbm = signalStrengthDbm;
		this.cid = cid;
		this.lac = lac;
		this.mcc = mcc;
		this.mnc = mnc;
		this.psc = psc;
	}
	
	public int getAsuLevel(){
		return asuLevel;
	}
	
	public int getSignalStrengthDbm() {
		return signalStrengthDbm;
	}
	
	public int getCid() {
		return cid;
	}
	
	public int getLac() {
		return lac;
	}
	
	public int getMcc() {
		return mcc;
	}
	
	public int getMnc() {
		return mnc;
	}
	
	public int getPsc() {
		return psc;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("asuLevel :"+asuLevel);
		sb.append("signalStrengthDbm :"+signalStrengthDbm);
		sb.append("cid :"+cid);
		sb.append("lac :"+lac);
		sb.append("mcc :"+mcc);
		sb.append("mnc :"+mnc);
		sb.append("psc :"+psc);
		
		return sb.toString();
	}
}
