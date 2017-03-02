package pt.ptinovacao.arqospocket;


public class DetailItem {

	private String fieldName;
	private String fieldValue;
	private boolean isLastRow;

	public DetailItem(String fName, String fValue) {
		fieldName = fName;
		fieldValue = fValue;
		isLastRow = false;
	}
	
	public DetailItem(boolean isLastRow) {
		this.isLastRow = isLastRow;
	}
	
	public DetailItem() {
		this.isLastRow = true;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	public boolean isLastRow() {
		return isLastRow;
	}

	public void setLastRow(boolean isLastRow) {
		this.isLastRow = isLastRow;
	}
	
	
}
