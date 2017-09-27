package pt.ptinovacao.arqospocket.views.testshistoric;


public class SpinnerRowItem {

	private String name;
	private int icon;

	public SpinnerRowItem(String fName, int fValue) {
		name = fName;
		icon = fValue;
	}
	

	public String getFieldName() {
		return name;
	}

	public void setFieldName(String fieldName) {
		this.name = fieldName;
	}

	public int getFieldValue() {
		return icon;
	}

	public void setFieldValue(int fieldValue) {
		this.icon = fieldValue;
	}
	
}
