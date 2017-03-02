package pt.ptinovacao.arqospocket.core;

import java.util.Map;

import pt.ptinovacao.arqospocket.service.enums.EConnectionTechnology;
import pt.ptinovacao.arqospocket.service.enums.ERunTestTaskState;
import pt.ptinovacao.arqospocket.service.enums.ETestTaskState;
import pt.ptinovacao.arqospocket.service.structs.MyLocation;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.util.ColorPercent;

public class RowItemTestesDetalhes {
	private int TextPerc, PercImg, State, Color;
	private ERunTestTaskState Progress;
	private int value = 0;
	private String TextName;
	int TextTipe,imageTipe;
	boolean Button;
	ColorPercent colorPercent = new ColorPercent();
	ETestTaskState textipe;


	public RowItemTestesDetalhes(int PercImg, int TextPerc, int State,
			String TextName, boolean Button, int Color) {
		this.PercImg = PercImg;
		this.TextPerc = TextPerc;
		this.TextName = TextName;
		this.State = State;
		this.Button = Button;
		this.Color = Color;

	}

	public RowItemTestesDetalhes(ERunTestTaskState get_run_task_state,
			String get_task_id, String get_task_name,
			Map<String, String> get_task_results,
			ETestTaskState get_task_state,
			EConnectionTechnology get_task_technology,
			MyLocation get_test_execution_location, boolean button) {
		this.TextName = get_task_name;
		this.Progress = get_run_task_state;
		calc(Progress);
		this.textipe = get_task_state;
		CalcColor(textipe);
		Calcstate(textipe);
		this.State=imageTipe;		
		colorPercent.TestType(TextTipe, value);
		this.Color = colorPercent.getColor();
		this.TextPerc = colorPercent.getpercent();
		this.PercImg = colorPercent.getimage();
		this.Button=button;
	}
	

	public RowItemTestesDetalhes(int getimage, int getpercent,
			String get_task_id, String get_task_name, int color2, int State, boolean button) {

		this.PercImg = getimage;
		this.TextPerc = getpercent;
		this.TextName = get_task_name;
		this.State = State;
		// this.Button = testBotMore;
		this.Color = color2;
		this.Button=button;
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

	public boolean getButton() {
		return Button;
	}

	public void setButton(boolean Button) {
		this.Button = Button;
	}

	public int getColor() {
		return Color;
	}

	public void setColor(int Color) {
		this.Color = Color;
	}

	public int calc(ERunTestTaskState progress) {
		// ERunTestTaskState state=null;
		if (progress == ERunTestTaskState.WAITING) {
			value = 0;
		}
		if (progress == ERunTestTaskState.RUNNING) {
			value = 50;
		}
		if (progress == ERunTestTaskState.DONE) {
			value = 100;
		}

		return value;
	}

	public int CalcColor(ETestTaskState texttype) {

		if (texttype == ETestTaskState.NOK) {
			TextTipe = R.string.test_failed;
		}
		if (texttype == ETestTaskState.OK) {
			TextTipe = R.string.test_completed;
		}
		if (texttype == ETestTaskState.Running) {
			TextTipe = R.string.test_running;
		}

		return TextTipe;

	}
	public int Calcstate(ETestTaskState texttype) {

		if (texttype == ETestTaskState.NOK) {
			imageTipe = R.drawable.icon_erro;
		}
		if (texttype == ETestTaskState.OK) {
			imageTipe = R.drawable.icon_sucesso;
		}
		if (texttype == ETestTaskState.Running) {
			imageTipe = R.drawable.icon_em_curso;
		}

		return imageTipe;

	}
	// public int getNumero() {
	// return Numero;
	// }
	//
	// public void setNumero(int Numero) {
	// this.Numero = Numero;
	// }
}