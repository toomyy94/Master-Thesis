package pt.ptinovacao.arqospocket.dialog;

public class Instruction_Entry {

	private int text;
	private int subText;
	private int move_icon_id;
	
	public Instruction_Entry(int text, int move_icon_id) {
		super();
		this.text = text;
		this.move_icon_id = move_icon_id;
	}
	
	public int getText() {
		return text;
	}
	public void setText(int text) {
		this.text = text;
	}
	public int getSubText() {
		return subText;
	}
	public void setSubText(int subText) {
		this.subText = subText;
	}
	public int getMoveIcon_id() {
		return move_icon_id;
	}
	public void setMoveIcon_id(int move_icon_id) {
		this.move_icon_id = move_icon_id;
	}	
}
