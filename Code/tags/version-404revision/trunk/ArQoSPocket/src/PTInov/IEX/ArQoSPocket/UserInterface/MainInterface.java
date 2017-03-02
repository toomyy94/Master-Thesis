package PTInov.IEX.ArQoSPocket.UserInterface;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import PTInov.IEX.ArQoSPocket.ApplicationSettings.MainInterfaceState;
import PTInov.IEX.ArQoSPocket.History.HistoryCircularStore;
import PTInov.IEX.ArQoSPocket.TaskList.TaskList;
import PTInov.IEX.ArQoSPocket.TaskStore.TaskStoreStruct;
import PTInov.IEX.ArQoSPocket.UserInterface.R;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainInterface extends Activity implements OnClickListener {

	private static final int HISTORY_ID = Menu.FIRST;
	private static final int DOTEST_ID = Menu.FIRST + 1;
	private static final int CONFIG_ID = Menu.FIRST + 2;
	private static final int AGENDA_ID = Menu.FIRST + 3;

	private final static String tag = "MainInterface";
	private static MainInterface myStaticRef = null;

	public static final String stateLastTestOK = "OK";
	public static final String stateLastTestFail = "Falhou";
	public static final String stateLastTestNA = "NA";

	public static final String appStateStop = "Parado";
	public static final String appStateWaiting = "Teste agendado";
	public static final String appStateRun = "A efectuar teste";
	public static final String appStateVazio = "Sem testes para execução";
	public static final String appStateSemServiço = "SEM SERVIÇO";

	private static TextView ultimoTeste = null;
	private static TextView estadoUltimoTeste = null;
	private static TextView proximoTeste = null;
	private static TextView falhas = null;
	private static long nfalhas = 0;
	private static TextView estadoActual = null;

	private static boolean botaoStart = false;

	private static ToggleButton toggle = null;

	private static MainInterfaceState mis = null;

	// Referencia para o serviço
	private static EngineService engineService = null;
	//public static NotificationManager mNM;

	// ///////////////////////////////////////
	
	
	// meter o metodo de resume a actualizar os valores da interface
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		try {
			toggle = (ToggleButton) findViewById(R.id.servicetoggle);
			toggle.setOnClickListener((OnClickListener) this);

			ultimoTeste = (TextView) findViewById(R.id.lasttest);
			estadoUltimoTeste = (TextView) findViewById(R.id.rlasttest);
			proximoTeste = (TextView) findViewById(R.id.nexttest);
			falhas = (TextView) findViewById(R.id.nfalhas);
			estadoActual = (TextView) findViewById(R.id.estado);

			// Start service
			Intent i = new Intent();
			i.setAction("PTInov.IEX.ArQoSPocket.EngineService.EngineService");
			startService(i);

			myStaticRef = this;

			//mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

			// Creat with default Values
			if (EngineService.mis == null) {
				mis = EngineService.mis = new MainInterfaceState(botaoStart,
						"NA", 3, "NA", 0, 1);
			} else {

				// recupera definições
				refreshInterface();
				
			}
		} catch (Exception ex) {
			Log.v(tag, "ERRORFOUND :: onCreate :" + ex.toString());
		}

		// se o serviço ja estiver arrancado tenho de actualizar os
		// valores...............................................
		// ---------------- o ideal era guardar o estado da interface no serviço
		// para que este fosse recuperado sempre k arranco a interface
	}
	
	public static void refreshInterface() {
		
		if (EngineService.mis != null) {
			mis = EngineService.getMainInterfaceState();
			setUltimoTeste(mis.getLastTest());
			setEstadoActual(mis.getActualState());
			setEstadoUltimoTeste(mis.getStateLastTest());
			setProximoTeste(mis.getNextTest());
			falhas.setText(mis.getErrors() + "");
		}
	}

	public static void serviceAlreadyStart(EngineService es, boolean runing) {

		try {
			// devia meter a interface de loading enquanto a interface se liga
			// ao
			// serviço.........................................

			Log.v(tag, "O serviço já começou a correr.................");
			engineService = es;

			// E preciso devolver qual o estado do serviço
			// se o serviço já estiver a correr e preciso mostrar o botão como
			// activo

			if (runing) {
				// se estiver a correr a preciso meter o botão como a correr

				botaoStart = true;
				toggle.setChecked(true);

				// se o serviço ja estiver arrancado tenho de actualizar os
				// valores..................................................

			} else {
				// se estvier parado manter o botão como parado
			}

			// Registar handlers para actualizar a interface
			EngineService.setHandlers(handlerActionState, handlerTaskFail,
					handlerTaskState, handlerDateLastTask, handlerQueueChanged);

		} catch (Exception ex) {
			Log.v(tag, "ERRORFOUND :: serviceAlreadyStart :" + ex.toString());
		}

		// nao esquecer de limpar os handlers quando a interface
		// sair......................................................
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub

		try {

			// Testa se já consegui arrancar o servico
			if (engineService == null) {
				Log
						.v(tag,
								"O serviço ainda não arrancou ou não esta a ser localizado pelo sistema!");

				Toast.makeText(this, "O serviço ainda não inicou!", 3).show();
			}

			if (!botaoStart) {

				nfalhas = 0;
				// actualiza o numero de falhas
				falhas.setText(nfalhas + "");

				// Altera os dados do ecrã (proximo teste e estado)
				setEstadoActual(2);

				Date d = engineService.getDateOfFirstTaskInRuningQueue();
				if (d != null) {
					String dataProximoTeste = d.toString();
					setProximoTeste(dataProximoTeste);
				}

				// Inica a thread que vai fazer os testes
				engineService.startThread();

				Toast.makeText(this, "Iniciou execução dos testes!", 3).show();
				botaoStart = true;
			} else {

				engineService.stopThread();

				// Altera os dados do ecrã (estado)
				setEstadoActual(1);

				Toast.makeText(this, "Parou execução dos testes!", 3).show();
				botaoStart = false;
			}
		} catch (Exception ex) {
			Log.v(tag, "ERRORFOUND :: onClick :" + ex.toString());
		}
	}

	private static Handler handlerActionState = new Handler() {

		public void handleMessage(Message msg) {
			try {
				switch (msg.arg1) {
				case 1: // Stop
					setEstadoActual(1);
					botaoStart = false;
					toggle.setChecked(false);
					break;
				case 2: // Wait
					setEstadoActual(2);
					break;
				case 3: // Run
					setEstadoActual(3);
					botaoStart = true;
					toggle.setChecked(true);
					break;
				case 4: // no tests
					setEstadoActual(4);
					botaoStart = false;
					toggle.setChecked(false);
				case 5: // sem serviço
					setEstadoActual(5);
					botaoStart = false;
					toggle.setChecked(false);
				default: // Invalid state
					break;
				}
			} catch (Exception ex) {
				Log.v(tag, "ERRORFOUND :: handlerActionState :" + ex.toString());
			}
		}
	};

	private static Handler handlerTaskFail = new Handler() {

		public void handleMessage(Message msg) {
			addFalha();
		}
	};

	private static Handler handlerTaskState = new Handler() {

		public void handleMessage(Message msg) {
			try {
				switch (msg.arg1) {
				case 1: // OK
					setEstadoUltimoTeste(1);
					break;
				case 2: // ERROR
					setEstadoUltimoTeste(2);
					break;
				default: // NA
					break;
				}
			} catch (Exception ex) {
				Log.v(tag, "ERRORFOUND :: handlerTaskState :" + ex.toString());
			}
		}
	};

	private static Handler handlerDateLastTask = new Handler() {

		public void handleMessage(Message msg) {
			setUltimoTeste((String) msg.obj);
		}
	};

	private static Handler handlerQueueChanged = new Handler() {

		public void handleMessage(Message msg) {

			try {
				setEstadoActual(2);

				Date d = engineService.getDateOfFirstTaskInRuningQueue();
				if (d != null) {
					String dataProximoTeste = d.toString();
					setProximoTeste(dataProximoTeste);
				}
			} catch (Exception ex) {
				Log.v(tag, "ERRORFOUND :: handlerQueueChanged :" + ex.toString());
			}
		}
	};

	private static void setUltimoTeste(String dataUltimoTeste) {
		try {
			ultimoTeste.setText(dataUltimoTeste);
			//mis.setLastTest(dataUltimoTeste);
		} catch (Exception ex) {
			Log.v(tag, "ERRORFOUND :: setUltimoTeste :" + ex.toString());
		}
	}

	/**
	 * 
	 * 1 - OK 0 - falhou
	 */
	private static boolean setEstadoUltimoTeste(int state) {

		try {
			//mis.setStateLastTest(state);

			switch (state) {
			case 1:
				estadoUltimoTeste.setText(MainInterface.stateLastTestOK);
				break;
			case 2:
				estadoUltimoTeste.setText(MainInterface.stateLastTestFail);
				break;
			default:
				estadoUltimoTeste.setText(MainInterface.stateLastTestNA);
				return false;
			}
		} catch (Exception ex) {
			Log.v(tag, "ERRORFOUND :: setEstadoUltimoTeste :" + ex.toString());
		}

		return true;
	}

	public static void setProximoTeste(String dataProximoTeste) {
		try {
			proximoTeste.setText(dataProximoTeste);
			//mis.setNextTest(dataProximoTeste);
		} catch (Exception ex) {
			Log.v(tag, "ERRORFOUND :: setProximoTeste :" + ex.toString());
		}
	}

	private static void addFalha() {
		try {
			nfalhas++;
			falhas.setText(nfalhas + "");
			//mis.seterrors(nfalhas);
		} catch (Exception ex) {
			Log.v(tag, "ERRORFOUND :: addFalha :" + ex.toString());
		}
	}

	/**
	 * 
	 * 1 - appStateStop 2 - appStateWaiting 3 - appStateRun
	 * 
	 */
	private static boolean setEstadoActual(int estado) {

		try {
			//mis.setActualState(estado);

			switch (estado) {
			case 1:
				estadoActual.setText(MainInterface.appStateStop);
				break;
			case 2:
				estadoActual.setText(MainInterface.appStateWaiting);
				break;
			case 3:
				estadoActual.setText(MainInterface.appStateRun);
				break;
			case 4:
				estadoActual.setText(MainInterface.appStateVazio);
				break;
			case 5:
				estadoActual.setText(MainInterface.appStateSemServiço);
				break;
			default:
				return false;
			}
		} catch (Exception ex) {
			Log.v(tag, "ERRORFOUND :: setEstadoActual :" + ex.toString());
		}

		return true;
	}

	// ---------------------- Create Options menu

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(0, HISTORY_ID, 0, "Histórico");
		menu.add(0, DOTEST_ID, 0, "Fazer Teste");
		menu.add(0, CONFIG_ID, 0, "Configurações");
		menu.add(0, AGENDA_ID, 0, "Testes Agendados");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case HISTORY_ID:
			startActivity(new Intent(this, HistoryInterface.class));
			break;
		case DOTEST_ID:
			startActivity(new Intent(this, UserTestListInterface.class));
			break;
		case CONFIG_ID:
			startActivity(new Intent(this, ConfigInterface.class));
			break;
		case AGENDA_ID:
			startActivity(new Intent(this, SchedulingInterface.class));
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 
	 * 
	 * Metodos para as interfaces
	 * 
	 * 
	 */

	public static List<TaskStoreStruct> getMySuspendedTaskStore() {

		try {
			if (engineService == null) {
				Log
						.v(tag,
								"O serviço ainda não arrancou ou não esta a ser localizado pelo sistema!");

				Toast.makeText(myStaticRef, "O serviço ainda não inicou!", 3)
						.show();
				return null;
			}

			return engineService.getMySuspendedTaskStore();
		} catch (Exception ex) {
			Log.v(tag, "ERRORFOUND :: getMySuspendedTaskStore :" + ex.toString());
			return new ArrayList<TaskStoreStruct>();
		}
	}

	public static List<TaskStoreStruct> getMyRuningTaskStore() {
		try {
			if (engineService == null) {
				Log
						.v(tag,
								"O serviço ainda não arrancou ou não esta a ser localizado pelo sistema!");

				Toast.makeText(myStaticRef, "O serviço ainda não inicou!", 3)
						.show();
				return null;
			}

			return engineService.getMyRuningTaskStore();
		} catch (Exception ex) {
			Log.v(tag, "ERRORFOUND :: getMyRuningTaskStore :" + ex.toString());
			return new ArrayList<TaskStoreStruct>();
		}
	}

	public static List<TaskStoreStruct> getMyPausedTaskStore() {
		try {
			if (engineService == null) {
				Log
						.v(tag,
								"O serviço ainda não arrancou ou não esta a ser localizado pelo sistema!");

				Toast.makeText(myStaticRef, "O serviço ainda não inicou!", 3)
						.show();
				return null;
			}

			return engineService.getMyPausedTaskStore();
		} catch (Exception ex) {
			Log.v(tag, "ERRORFOUND :: getMyPausedTaskStore :" + ex.toString());
			return new ArrayList<TaskStoreStruct>();
		}
	}

	public static HistoryCircularStore getHistoryCircularStore() {
		try {
			if (engineService == null) {
				Log
						.v(tag,
								"O serviço ainda não arrancou ou não esta a ser localizado pelo sistema!");

				Toast.makeText(myStaticRef, "O serviço ainda não inicou!", 3)
						.show();
				return null;
			}

			return engineService.getHistoryCircularStore();
		} catch (Exception ex) {
			Log.v(tag, "ERRORFOUND :: getHistoryCircularStore :" + ex.toString());
			return new HistoryCircularStore(EngineService.historySize);
		}
	}
	
	public static HistoryCircularStore getErrorHistoryCircularStore() {
		try {
			if (engineService == null) {
				Log
						.v(tag,
								"O serviço ainda não arrancou ou não esta a ser localizado pelo sistema!");

				Toast.makeText(myStaticRef, "O serviço ainda não inicou!", 3)
						.show();
				return null;
			}

			return engineService.getErrorHistoryCircularStore();
		} catch (Exception ex) {
			Log.v(tag, "ERRORFOUND :: getHistoryCircularStore :" + ex.toString());
			return new HistoryCircularStore(EngineService.historySize);
		}
	}

	public static TaskList getTestTaskList() {

		try {
			if (engineService == null) {
				Log
						.v(tag,
								"O serviço ainda não arrancou ou não esta a ser localizado pelo sistema!");

				Toast.makeText(myStaticRef, "O serviço ainda não inicou!", 3)
						.show();
				return null;
			}

			return engineService.getUserTaskList();

		} catch (Exception ex) {
			Log.v(tag, "ERRORFOUND :: getTestTaskList :" + ex.toString());
			return null;
		}
	}

	public static void runTestNow(TaskStoreStruct tss) {

		try {
			if (engineService == null) {
				Log
						.v(tag,
								"O serviço ainda não arrancou ou não esta a ser localizado pelo sistema!");

				Toast.makeText(myStaticRef, "O serviço ainda não inicou!", 3)
						.show();
				return;
			}

			engineService.runTestNow(tss);
		} catch (Exception ex) {
			Log.v(tag, "ERRORFOUND :: runTestNow :" + ex.toString());
		}
	}
	
	/**
	 * 
	 * Metodos de saida para limpar as referencias de callback no serviço
	 * 
	 */
	
	@Override
	public void onPause() {
		try {
			engineService.clearAllHandlers();
		} catch (Exception ex) {
			Log.v(tag, "ERRORFOUND :: onPause :" + ex.toString());
		}
		super.onPause();
	}
	
	@Override
	public void onStop() {
		try {
			engineService.clearAllHandlers();
		} catch (Exception ex) {
			Log.v(tag, "ERRORFOUND :: onStop() :" + ex.toString());
		}
		super.onStop();
	}
	
	@Override
	public void onDestroy() {
		try {
			engineService.clearAllHandlers();
		} catch (Exception ex) {
			Log.v(tag, "ERRORFOUND :: onDestroy() :" + ex.toString());
		}
		super.onDestroy();
	}
	
	@Override
	public void onResume() {
		try {
			
			// Creat with default Values
			if (EngineService.mis == null) {
				mis = EngineService.mis = new MainInterfaceState(botaoStart,
						"NA", 3, "NA", 0, 1);
			} else {

				// recupera definições

				mis = EngineService.getMainInterfaceState();
				setUltimoTeste(mis.getLastTest());
				setEstadoActual(mis.getActualState());
				setEstadoUltimoTeste(mis.getStateLastTest());
				setProximoTeste(mis.getNextTest());
				falhas.setText(mis.getErrors() + "");
			}
			
		} catch (Exception ex) {
			Log.v(tag, "ERRORFOUND :: onDestroy() :" + ex.toString());
		}
		super.onResume();
	}
	
}