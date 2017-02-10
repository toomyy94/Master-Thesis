package pt.ptinovacao.arqospocket.core;

public class RowItem {
	private String Text;
	private int Image;
	private boolean Select;

	public RowItem(int Image, String Text, Boolean Select) {
		this.Image = Image;
		this.Text = Text;
		this.Select = Select;
	}

	public int getImage() {
		return Image;
	}

	public void setImage(int Image) {
		this.Image = Image;
	}

	public boolean getSelect() {
		return Select;
	}

	public void setSelect(boolean Select) {
		this.Select = Select;
	}

	public String getDesc() {
		return Text;
	}

	public void setDesc(String Text) {
		this.Text = Text;
	}

}
