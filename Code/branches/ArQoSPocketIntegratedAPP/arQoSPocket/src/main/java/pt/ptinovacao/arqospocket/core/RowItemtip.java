package pt.ptinovacao.arqospocket.core;


	public class RowItemtip {
		private String Text;
		private String Num;
		private boolean Select;

		public RowItemtip(String Num, String Text, Boolean Select) {
			this.Num = Num;
			this.Text = Text;
			this.Select = Select;
		}

		public String getNum() {
			return Num;
		}

		public void setNum(String Num) {
			this.Num = Num;
		}

		public boolean getSelect() {
			return Select;
		}

		public String getDesc() {
			return Text;
		}

		public void setDesc(String Text) {
			this.Text = Text;
		}

		public void setSelect(boolean Select) {
			this.Select = Select;
		}

	

}