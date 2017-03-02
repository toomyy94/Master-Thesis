package PT.PTInov.ArQoSPocket.structs;

public interface ECellInfoLte {

	public int getCi();
	public int getMcc();
	public int getMnc();
	public int getPci();
	public int getTac();
	
	public int getAsuLevel();
	public int getDbm();
	public int getLevel();
	public int getTimingAdvance();
}
