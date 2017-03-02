package PT.PTInov.ArQoSPocket.structs;

public interface ECellInfoGsm {

	public int getCid();
	public int getLac();
	public int getMcc();
	public int getMnc();
	public int getPsc();
	
	public int getAsuLevel();
	public int getDbm();
	public int getLevel();
}
