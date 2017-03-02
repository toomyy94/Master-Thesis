package PT.PTInov.ArQoSPocket.structs;

public interface ECellInfoCdma {

	public int getBasestationId();
	public int getLatitude();
	public int getLongitude();
	public int getNetworkId();
	public int getSystemId();
	
	public int getAsuLevel();
	public int getCdmaDbm();
	public int getCdmaEcio();
	public int getCdmaLevel();
	public int getDbm();
	public int getEvdoDbm();
	public int getEvdoEcio();
	public int getEvdoLevel();
	public int getEvdoSnr();
	public int getLevel();
}
