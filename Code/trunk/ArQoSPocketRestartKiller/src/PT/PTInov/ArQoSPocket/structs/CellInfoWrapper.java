package PT.PTInov.ArQoSPocket.structs;

public class CellInfoWrapper {

	int BasestationId;
	int Latitude;
	int Longitude;
	int NetworkId;
	int SystemId;
	int AsuLevel;
	int CdmaDbm;
	int CdmaEcio;
	int CdmaLevel;
	int Dbm;
	int EvdoDbm;
	int EvdoEcio;
	int EvdoLevel;
	int EvdoSnr;
	int Level;
	int Cid;
	int Lac;
	int Mcc;
	int Mnc;
	int Psc;
	int Ci;
	int Pci;
	int Tac;
	int TimingAdvance;
	
	
	public CellInfoWrapper(int BasestationId, int Latitude, int Longitude, int NetworkId, int SystemId, int AsuLevel, int CdmaDbm, int CdmaEcio, int CdmaLevel, int Dbm, int EvdoDbm, int EvdoEcio, int EvdoLevel, int EvdoSnr, int Level) {
		this.BasestationId = BasestationId;
		this.Latitude = Latitude;
		this.Longitude = Longitude;
		this.NetworkId = NetworkId;
		this.SystemId = SystemId;
		this.AsuLevel = AsuLevel;
		this.CdmaDbm = CdmaDbm;
		this.CdmaEcio = CdmaEcio;
		this.CdmaLevel = CdmaLevel;
		this.Dbm = Dbm;
		this.EvdoDbm = EvdoDbm;
		this.EvdoEcio = EvdoEcio;
		this.EvdoLevel = EvdoLevel;
		this.EvdoSnr = EvdoSnr;
		this.Level = Level;
		
	}
	
	public CellInfoWrapper(int Cid, int Lac, int Mcc, int Mnc, int Psc, int AsuLevel, int Dbm, int Level) {
		this.Cid = Cid;
		this.Lac = Lac;
		this.Mcc = Mcc;
		this.Mnc = Mnc;
		this.Psc = Psc;
		this.AsuLevel = AsuLevel;
		this.Dbm = Dbm;
		this.Level = Level;
	}
	
	public CellInfoWrapper(int Ci, int Mcc, int Mnc, int Pci, int Tac, int AsuLevel, int Dbm, int Level, int TimingAdvance) {
		this.Ci = Ci;
		this.Mcc = Mcc;
		this.Mnc = Mnc;
		this.Pci = Pci;
		this.Tac = Tac;
		this.AsuLevel = AsuLevel;
		this.Dbm = Dbm;
		this.Level = Level;
		this.TimingAdvance = TimingAdvance;
	}
}
