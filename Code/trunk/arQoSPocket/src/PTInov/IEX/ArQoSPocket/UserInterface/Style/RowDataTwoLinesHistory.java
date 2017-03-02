package PTInov.IEX.ArQoSPocket.UserInterface.Style;

/**
 * Data type used for custom adapter. Single item of the adapter.
 */

public class RowDataTwoLinesHistory {

	protected String item;
	protected String description;
	protected boolean sucess;

	public RowDataTwoLinesHistory(String pitem, String pdescription, boolean psucess) {
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
