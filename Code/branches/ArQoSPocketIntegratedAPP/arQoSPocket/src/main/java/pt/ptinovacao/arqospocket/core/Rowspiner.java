package pt.ptinovacao.arqospocket.core;

public class Rowspiner {
	private String Text;
	private int Image;

public Rowspiner(int Image, String Text) {
	this.Image = Image;
	this.Text = Text;
}
public int getImage() {
	return Image;
}

public void setImage(int Image) {
	this.Image = Image;
}
public String getDesc() {
	return Text;
}

public void setDesc(String Text) {
	this.Text = Text;
}



}