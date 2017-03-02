package PT.PTInov.ArQoSPocketWiFi.Utils;

/**
 * Data type used for custom adapter. Single item of the adapter.
 */

public class RowDataTwoLines {

	protected String item;
	protected String description;
	protected boolean sucess;

	public RowDataTwoLines(String pitem, String pdescription, boolean psucess) {
		item = pitem;
		description = pdescription;
		sucess = psucess;
	}

	public String mItem() {
		return item;
	}

	public String mDescription() {
		return description;
	}
	
	public boolean mSucess() {
		return sucess;
	}

	@Override
	public String toString() {
		return item + " " + description;
	}
}
