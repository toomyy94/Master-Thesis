package pt.ptinovacao.arqospocket.core;

import java.util.Date;
import java.util.List;
import pt.ptinovacao.arqospocket.service.enums.ERunTestTaskState;
import pt.ptinovacao.arqospocket.service.enums.ETestTaskState;
import pt.ptinovacao.arqospocket.service.enums.ETestType;
import pt.ptinovacao.arqospocket.service.interfaces.ITaskResult;
import pt.ptinovacao.arqospocket.service.structs.MyLocation;
import pt.ptinovacao.arqospocket.util.ColorPercent;

public class RowItemTestes {
	private int TextPerc;
	private int PercImg;
	private String TextName;
	private int State;
	private int Button;
	private int Color;
	private int Ntestes = 1;
	private int NtestesDone = 0;
	int value = 0;
	
	ColorPercent colorPercent = new ColorPercent();

	public RowItemTestes(int PercImg, int TextPerc, String TextName, int State,
			int Button, int Color) {
		this.PercImg = PercImg;
		this.TextPerc = TextPerc;
		this.TextName = TextName;
		this.State = State;
		this.Button = Button;
		this.Color = Color;

	}

	public RowItemTestes(String get_test_id, Date get_execution_date,
			String get_test_name, ETestType get_test_type,int State) {
		
		colorPercent.teste_inactivo(0);
		this.TextName = get_test_name;
		this.PercImg = colorPercent.getimage();
		this.Color = colorPercent.getColor();
		this.TextPerc = colorPercent.getpercent();
		this.State = State;
	}

	public RowItemTestes(String get_test_id, int get_number_of_tests,
			int get_number_of_tests_done, ERunTestTaskState get_run_test_state,
			List<ITaskResult> get_task_list,
			MyLocation get_test_execution_location, String get_test_name,
			ETestTaskState get_test_state, ETestType get_test_type,int State) {
		this.TextName = get_test_name;
		this.Ntestes = get_number_of_tests;
		this.NtestesDone = get_number_of_tests_done;
		value = calc();
		
		colorPercent.teste_em_curso(value);
		this.PercImg = colorPercent.getimage();
		this.Color = colorPercent.getColor();
		this.TextPerc = colorPercent.getpercent();
		this.State=State;
		
	}

	public int calc() {
		int Percent = (NtestesDone * 100) / Ntestes;
		if (Percent >= 0 && Percent < 10) {
			value = 0;
		}
		if (Percent >= 10 && Percent < 20) {
			value = 10;
		}
		if (Percent >= 20 && Percent < 30) {
			value = 20;
		}
		if (Percent >= 30 && Percent < 40) {
			value = 30;
		}
		if (Percent >= 40 && Percent < 50) {
			value = 40;

		}
		if (Percent >= 50 && Percent < 60) {
			value = 50;
		}
		if (Percent >= 60 && Percent < 70) {
			value = 60;
		}
		if (Percent >= 70 && Percent < 80) {
			value = 70;
		}
		if (Percent >= 80 && Percent < 90) {
			value = 80;
		}
		if (Percent >= 90 && Percent < 100) {
			value = 90;
		}
		if (Percent == 100) {
			value = 100;
		}
		return value;
	}

	
	public int getPercImg() {
		return PercImg;
	}

	public void setPercImg(int PercImg) {
		this.PercImg = PercImg;
	}

	public int getTextPerc() {
		return TextPerc;
	}

	public void setTextPerc(int TextPerc) {
		this.TextPerc = TextPerc;
	}

	public String getTextName() {
		return TextName;
	}

	public void setTextName(String TextName) {
		this.TextName = TextName;
	}

	public int getState() {
		return State;
	}

	public void setState(int State) {
		this.State = State;
	}

	public int getButton() {
		return Button;
	}

	public void setButton(int Button) {
		this.Button = Button;
	}

	public int getColor() {
		return Color;
	}

	public void setColor(int Color) {
		this.Color = Color;
	}
}
