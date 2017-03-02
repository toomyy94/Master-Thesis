package pt.ptinovacao.arqospocket.fragments;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import pt.ptinovacao.arqospocket.adapters.AdapterListViewTaskDetails;
import pt.ptinovacao.arqospocket.adapters.AdapterListViewTestesDetalhes;
import pt.ptinovacao.arqospocket.service.enums.EMobileNetworkMode;
import pt.ptinovacao.arqospocket.service.enums.EMobileState;
import pt.ptinovacao.arqospocket.service.enums.ENetworkAction;
import pt.ptinovacao.arqospocket.service.enums.ERunTestTaskState;
import pt.ptinovacao.arqospocket.service.enums.ETestType;
import pt.ptinovacao.arqospocket.service.interfaces.IService;
import pt.ptinovacao.arqospocket.service.interfaces.ITask;
import pt.ptinovacao.arqospocket.service.interfaces.ITaskResult;
import pt.ptinovacao.arqospocket.service.interfaces.ITest;
import pt.ptinovacao.arqospocket.service.interfaces.ITestResult;
import pt.ptinovacao.arqospocket.service.interfaces.IUI;
import pt.ptinovacao.arqospocket.DetailItem;
import pt.ptinovacao.arqospocket.MyApplication;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.activities.ActivityTestes;
import pt.ptinovacao.arqospocket.core.RowItemTestesDetalhes;
import pt.ptinovacao.arqospocket.dialog.TestDetailsDialog;
import pt.ptinovacao.arqospocket.util.ColorPercent;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentTestesDetalhes extends Fragment implements IUI {

	private static final String ITEM_ID = "ItemID";
	private static final String ITEM_ID_2 = "ItemID2";
	private static final String TAG = "testsHistory";
	AdapterListViewTestesDetalhes adapter;
	private ActivityTestes activity;
	ColorPercent colorPercent = new ColorPercent();
	MyApplication MyApplicationRef;
	int imagetipe, botplay, NtestesDone = 0, Ntestes = 0, TipoTeste, value = 0,
			contador = 0;
	ImageView imagemPercent, IMGTipoTeste, button, play;
	TextView textPercent, textnome;
	String name, testeid, PlayId;
	ListView listView;
	List<RowItemTestesDetalhes> TasksListIn, TasksListToDo;
	ITestResult teste;
	IService iService;
	private List<ITaskResult> Tasks;
	private List<ITask> TasksToDo;
	private ArrayList<RowItemTestesDetalhes> taskList, taskListtodo;
	ERunTestTaskState run_state;
	RowItemTestesDetalhes item;
	Boolean click, execution_state, Play = false;
	ETestType State;
	Bundle bundle;
	FragmentTestes f;
	String testId = null;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		this.activity = (ActivityTestes) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Dados();

	}
	public void onPause() {
		super.onPause();
		if (iService != null) {			
			iService.remove_registry_update_test_info(this);			
		}		
		}
		
	public void onResume() {		
		super.onResume();
				
		if (iService != null) {
			iService.registry_update_test_info(this);		
		}
		
		}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_testes_detalhes, container,
				false);

		listView = (ListView) v.findViewById(R.id.listTestes_deta);
		



		// Cabecalho
		colorPercent.TestType(TipoTeste, value);
		play = (ImageView) v.findViewById(R.id.botPlay);
		play.setOnClickListener(onClickListener_play);
		imagemPercent = (ImageView) v.findViewById(R.id.imagepercent_deta);
		IMGTipoTeste = (ImageView) v.findViewById(R.id.tipodeteste);
		textPercent = (TextView) v.findViewById(R.id.textpercent_deta);
		textnome = (TextView) v.findViewById(R.id.text_nome_deta);

		Cabecalho();
		tasks();
		taskstodo();
		TasksListToDo = new ArrayList<RowItemTestesDetalhes>();
		TasksListToDo.clear();
		if (taskList != null) {
			TasksListIn = new ArrayList<RowItemTestesDetalhes>(taskList);
		} else {
			TasksListIn = new ArrayList<RowItemTestesDetalhes>();
		}

		TasksListToDo.addAll(TasksListIn);
		TasksListToDo.addAll(taskListtodo);
		adapter = new AdapterListViewTestesDetalhes(getActivity(), 0,
				TasksListToDo);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(onClickListener);
		
		return v;
	}

	OnClickListener onClickListener_play = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (iService != null) {
				Pair<Boolean, String> pair = iService.run_test(testeid);
				if (pair.first == true) {
					play.setImageResource(R.drawable.bot_play_indisponivel);
					Play = true;
					activity.getInfo();
					PlayId = pair.second;
					PlayTeste();

				}
			}
		}

	};
	OnItemClickListener onClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {

			List<DetailItem> taskDetails = new ArrayList<DetailItem>();
			ITaskResult taskResult;

			if(teste == null)
				return;		
			if (Tasks == null)
				Tasks = teste.get_task_list();

			if (Tasks != null && Tasks.size() > 0 && position < Tasks.size()) {
				Log.i(TAG, "Task_list size: "
						+ teste.get_task_list().size());
				taskResult = Tasks.get(position);
				for (Entry<String, String> taskEntry : taskResult
						.get_task_results(activity).entrySet()) {
					taskDetails.add(new DetailItem(taskEntry.getKey(),
							taskEntry.getValue()));
				}
			}
		
			AdapterListViewTaskDetails taskDetailsAdapter = new AdapterListViewTaskDetails(
					activity, taskDetails, null);
			if(taskList == null)
				return;		
			
			if (position < taskList.size()) {
				/* Test details dialog builder. */
				TestDetailsDialog dialog = new TestDetailsDialog(activity,
						R.layout.dialog_list_testes_detalhes,
						taskDetailsAdapter, taskList.get(position));
				dialog.show();
			}

		}
	};

	// carregar dados apos clicar no Play
	public void PlayTeste() {
		teste = activity.getTestResult(PlayId);
		this.Tasks = teste.get_task_list();
		this.TasksToDo = teste.get_task_list_to_do();
		this.name = teste.get_test_name();
		this.NtestesDone = teste.get_number_of_tests_done();
		this.Ntestes = teste.get_number_of_tests();
		ETestType State = teste.get_test_type();
		value = calc();
		this.TipoTeste = R.string.test_running;
		Test_type(State);
		this.botplay = R.drawable.bot_play_indisponivel;
		click = false;
		this.testeid = teste.get_test_id();
		this.run_state = teste.get_run_test_state();
	}

	public void Dados() {
		Bundle args = getArguments();

		MyApplicationRef = (MyApplication) activity.getApplicationContext();
		iService = MyApplicationRef.getEngineServiceRef();

		if (args != null && args.containsKey(ITEM_ID)) {
			testId = args.getString(ITEM_ID);
			teste = activity.getTestResult(testId);

			this.Tasks = teste.get_task_list();
			this.TasksToDo = teste.get_task_list_to_do();
			this.name = teste.get_test_name();
			this.NtestesDone = teste.get_number_of_tests_done();
			this.Ntestes = teste.get_number_of_tests();
			ETestType State = teste.get_test_type();
			value = calc();
			this.TipoTeste = R.string.test_running;
			Test_type(State);
			this.botplay = R.drawable.bot_play_indisponivel;
			click = false;
			this.testeid = teste.get_test_id();
			this.run_state = teste.get_run_test_state();
		}

		if (args != null && args.containsKey(ITEM_ID_2)) {
			// ITest testetask = (ITest) args.getSerializable("Item2");
			testId = args.getString(ITEM_ID_2);
			ITest testetask = activity.getTest(testId);

			this.name = testetask.get_test_name();
			this.TipoTeste = R.string.test_inactive;
			this.TasksToDo = testetask.get_task_list();
			ETestType State2 = null;
			Test_type(State2);
			execution_state = iService.get_execution_state();

			if (execution_state == false) {
				this.botplay = R.drawable.bot_play_teste;
				this.click = true;
			} else {
				this.botplay = R.drawable.bot_play_indisponivel;
				this.click = false;
			}
			this.testeid = testetask.get_test_id();
			value = 0;

		}
//-----------------------------------------------------------------------------------
//		if (iService != null) {
//			iService.registry_update_test_info(this);
//		}
	}

	public void Cabecalho() {
		colorPercent.TestType(TipoTeste, value);
		this.IMGTipoTeste.setImageResource(imagetipe);
		this.play.setImageResource(botplay);
		this.play.setClickable(click);
		this.imagemPercent.setImageResource(colorPercent.getimage());
		this.textPercent.setText(colorPercent.getpercent());
		this.textPercent.setTextColor(getResources().getColor(
				colorPercent.getColor()));
		this.textnome.setText(name);
		this.textnome.setTextColor(getResources().getColor(
				colorPercent.getColor()));
	}

	public boolean hasTasks() {
		return Tasks != null && Tasks.size() > 0;
	}

	public ArrayList<RowItemTestesDetalhes> tasks() {
		taskList = null;
		if (hasTasks()) {
			taskList = new ArrayList<RowItemTestesDetalhes>();

			for (ITaskResult task : Tasks) {
				RowItemTestesDetalhes item = new RowItemTestesDetalhes(
						task.get_run_task_state(), task.get_task_id(),
						task.get_task_name(), task.get_task_results(activity),
						task.get_task_state(), task.get_task_technology(),
						task.get_test_execution_location(), true);
				taskList.add(item);
				contador++;
			
			}
		}
		return taskList;

	}

	public boolean hasTasksToDo() {
		return TasksToDo != null && TasksToDo.size() > 0;
	}

	public ArrayList<RowItemTestesDetalhes> taskstodo() {
		taskListtodo = null;

		if (hasTasksToDo()) {
			taskListtodo = new ArrayList<RowItemTestesDetalhes>();
			colorPercent.TestType(R.string.test_inactive, 0);
			if (hasTasks()) {

				for (int i = contador; i < TasksToDo.size(); i++) {
					ITask task = TasksToDo.get(i);
					RowItemTestesDetalhes item = new RowItemTestesDetalhes(
							colorPercent.getimage(), colorPercent.getpercent(),

							task.get_task_id(), task.get_task_name(),
							colorPercent.getColor(), R.drawable.icon_inativo,
							false);
					taskListtodo.add(item);
					
				}
			} else {
				for (ITask task : TasksToDo) {
					RowItemTestesDetalhes item = new RowItemTestesDetalhes(
							colorPercent.getimage(), colorPercent.getpercent(),
							task.get_task_id(), task.get_task_name(),
							colorPercent.getColor(), R.drawable.icon_inativo,
							false);
					taskListtodo.add(item);
					
				}
			}
		}
		return taskListtodo;

	}

	// verificar tipo de teste
	public int Test_type(ETestType state) {
		if (state == ETestType.SCHEDULED) {
			imagetipe = R.drawable.icon_agendado;
		} else {
			imagetipe = R.drawable.icon_on_demand_litle;
		}
		return imagetipe;

	}

	// calcular % de teste concluido
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

	@Override
	public void update_mobile_information(EMobileState mobile_state,
			EMobileNetworkMode network_mode, String operator_code,
			String rx_level, String cid, String lac) {

	}

	@Override
	public void update_wifi_information(String link_speed, String ssid,
			String rx_level, String channel) {

	}

	@Override
	public void update_mobile_params(TreeMap<String, String> keyValueParams) {

	}

	@Override
	public void update_wifi_params(TreeMap<String, String> keyValueParams) {

	}

	@Override
	public void update_test_info() {

		FragmentManager fragMgr = activity.getSupportFragmentManager();
		fragMgr.popBackStack();
	}

	@Override
	public void update_test_task(String test_id) {
		listView.setEnabled(true);
		contador = 0;
		TasksListToDo.clear();
		TasksListIn.clear();
		activity.getInfo();
		if (Play == true) {
			PlayTeste();
		} else {
			Dados();
		}
		Cabecalho();
		tasks();
		taskstodo();

		TasksListToDo.addAll(taskList);
		TasksListToDo.addAll(taskListtodo);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void send_pending_tests_ack(ENetworkAction action_state) {

	}

	@Override
	public void send_report_ack(ENetworkAction action_state) {

	}

}
