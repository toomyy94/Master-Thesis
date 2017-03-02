package pt.ptinovacao.arqospocket;


public class DialogRowItem {

	private String name;
	private int icon;

	public DialogRowItem(String fName, int fValue) {
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
