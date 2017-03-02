package PTInov.IEX.ArQoSPocket.UserInterface;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;

import PTInov.IEX.ArQoSPocket.ApplicationSettings.MainInterfaceState;
import PTInov.IEX.ArQoSPocket.ApplicationSettings.MainInterfaceStateStore;
import PTInov.IEX.ArQoSPocket.ApplicationSettings.SettingsStore;
import PTInov.IEX.ArQoSPocket.EngineService.EngineThread;
import PTInov.IEX.ArQoSPocket.History.HistoryCircularStore;
import PTInov.IEX.ArQoSPocket.ParserFileTasks.FileNameStructObject;
import PTInov.IEX.ArQoSPocket.ParserFileTasks.ParseTest;
import PTInov.IEX.ArQoSPocket.ParserFileTasks.TaskStruct;
import PTInov.IEX.ArQoSPocket.ParserFileTasks.TestHeadStruct;
import PTInov.IEX.ArQoSPocket.ServicesInstance.MyTimer;
import PTInov.IEX.ArQoSPocket.ServicesInterfaces.EngineInterface;
import PTInov.IEX.ArQoSPocket.ServicesInterfaces.MyTimerInterface;
import PTInov.IEX.ArQoSPocket.TaskList.TaskList;
import PTInov.IEX.ArQoSPocket.TaskStore.TaskStore;
import PTInov.IEX.ArQoSPocket.TaskStore.TaskStoreStruct;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class EngineService extends Service implements EngineInterface, MyTimerInterface {

	private final static String tag = "#### EngineService ####";
	
	public static int historySize = 20;
	private boolean keepaliveState =  true;
	private int keepaliveTimerCode = 0001;
	
	private MyTimer myTimerKeepAlive = null;

	// Semaforo utilizado para controlar a concorrencia na thread de engine
	private Semaphore s = null;
	private boolean runState = false; // false - parado, true - a correr
	
	private final int tryUpService =  10;

	// listas de testes
	private TaskStore myRuningTaskStore;
	private TaskList userTaskList;
	private TaskList pausedTaskList;
	private TaskList SuspendTaskList;

	// Lista com o historico dos resultados dos testes
	private HistoryCircularStore historyStore = null;
	
	// Lista com o historico dos resultados dos testes
	private HistoryCircularStore errorHistoryStore = null;

	private Thread myThread;
	private EngineThread threadObject = null;

	public static MainInterfaceState mis = null;

	// interface handlers
	private static Handler handlerActionState = null;
	private static Handler handlerTaskFail = null;
	private static Handler handlerTaskState = null;
	private static Handler handlerDateLastTask = null;
	private static Handler handlerQueueChanged = null;

	public final static String fileLogsPath = "/sdcard/TestRes/";

	private static Context myRef = null;

	// para testes
	private Date NextTaskDate = null;
	
	public static NotificationManager mNM;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		// Avisa a interface que já começei a correr
		try {
			MainInterface.serviceAlreadyStart(this, runState);
		} catch(Exception ex) {
			Log.v(tag, "onStartCommand :: ERROR :"+ex.toString());
		}
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	public void printf(String s) {
		Log.v(tag, s);
	}

	@Override
	public void onCreate() {
		Log
				.v(
						tag,
						"Service Started!.................................................................................");
		
		// Colocar a interface a avisar que estou a espera que o sim fique pronto
		mis = new MainInterfaceState(false, "NA", 3, "NA", 0, 5);
		
		// A aplicação so avança quando o sim estiver pronto para iniciar os testes
		
		TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		
		int tryingsReady = 0;
		while (tm.getSimState() != TelephonyManager.SIM_STATE_READY && tryingsReady<tryUpService) {
			Log.v(tag, "Wait for TelephonyManager.SIM_STATE_READY");
			try {
				Thread.sleep(1000);
			} catch(Exception e) {
				
			}
			tryingsReady++;
		}

		s = new Semaphore(1);

		// lista que contem as tarefas a serem executadas
		myRuningTaskStore = new TaskStore();

		// lista que contem as tarefas k o utilizador pode correr por vontade
		// propria
		userTaskList = new TaskList();

		// lista de tarefas que estavam a correr quando foi feito pause pelo
		// tecnico
		pausedTaskList = new TaskList();

		// lista de tarefas bloqueadas até o utilizador desligar a chamada
		SuspendTaskList = new TaskList();
		
		SettingsStore sStore = new SettingsStore((Context) this);
		historySize = SettingsStore.getHistorySize();
		keepaliveState = SettingsStore.getkeepaliveState();
		
		if (keepaliveState) {
			// iniciar timer que arranca execução de testes caso estejam parados
			
			//myTimerKeepAlive = new MyTimer(10000,this,keepaliveTimerCode);
			myTimerKeepAlive = new MyTimer(3600000,this,keepaliveTimerCode);
		}

		// inicializa a lista de historico
		historyStore = new HistoryCircularStore(EngineService.historySize);
		
		// inicializa a lista de historico de erros
		errorHistoryStore = new HistoryCircularStore(EngineService.historySize);

		// processa do ficheiro (de momento) as tarefas a ser executadas
		setTaks("Tab_00139501C92D#T308739_20110905133642_20110905134142_R2");

		myRef = this;
		
		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		
		// Recupera as definições gravada
		MainInterfaceStateStore miss = new MainInterfaceStateStore((Context) myRef);
		mis = MainInterfaceStateStore.getSavedMainInterfaceState();
		
		if (tryingsReady==tryUpService){
			mis.setActualState(5); //sem serviço
		}
		
		//Change interface
		try {
			MainInterface.refreshInterface();
		} catch(Exception ex) {
			Log.v(tag, "MainInterface.refreshInterface :: ERROR :"+ex.toString());
		}
		
		Log.v(tag, "vou testar se é para iniciar a thread!!!");
		if (MainInterfaceStateStore.getEngineState() && tryingsReady!=tryUpService) {
			Log.v(tag, "Start a thread!!!");
			startThread();
		}
		
	}

	/**
	 * 
	 * 
	 * Manipulação da thread de tasks
	 * 
	 * 
	 */

	public synchronized void startThread() {

		Log.v(tag, "startThread - In");

		try {

			// copy all pausedTaskList to myRuningTaskStore
			myRuningTaskStore.addTestList(pausedTaskList.removeAllList());
			queueChanged();

			runState = true;
			threadObject = new EngineThread(myRuningTaskStore, historyStore, errorHistoryStore,
					this, s);
			myThread = new Thread(threadObject);
			myThread.start();
			
			//save changed state
			MainInterfaceStateStore.saveMainInterfaceState(mis, runState);
			
		} catch (Exception ex) {
			Log.v(tag, "ERRORFOUND :: startThread::ERROR: " + ex.toString());
		}
	}

	public synchronized void stopThread() {

		Log.v(tag, "stopThread - In");

		try {
			runState = false;
			threadObject.shutdown();
			myThread = null;
			
			//save changed state
			if (mis != null) {
				mis.setActualState(1);
			}
			MainInterfaceStateStore.saveMainInterfaceState(mis, runState);
			
		} catch (Exception ex) {
			Log
					.v(tag, "ERRORFOUND :: interruptThread::ERROR: "
							+ ex.toString());
		}

	}

	/**
	 * 
	 * 
	 * Carregamentos dos testes para a queue de execução
	 * 
	 * 
	 */

	public synchronized void setTaks(String fileName) {
		
		Log.v(tag, "setTaks - In");

		try {
			
			boolean ThreadStart = false;

			// faz parser do ficheiro com as tarefas a realizar
			// ParseTest p = new
			// ParseTest("newTab_00139501C92D#T000001_20110803104114_20110803104314_R2","/sdcard/");
			//ParseTest p = new ParseTest("Tab_00139501C92D#T308739_20110905133642_20110905134142_R2","/sdcard/");
			ParseTest p = new ParseTest(fileName,"/sdcard/");

			FileNameStructObject myTextFile = p.parserFileName();
			TestHeadStruct HeadStruct = p.parserHead();
			ArrayList<TaskStruct> taskList = p.parserTestTask();

			if (myTextFile==null || HeadStruct==null || taskList==null) {
				Log.v(tag, "setTaks:: ERROR Não foi possivel carregar o novo teste");
				//Toast.makeText(MainInterface.refreshInterface(), "Não foi possível carregar o teste!", Toast.LENGTH_LONG);
				return;
			}
			
			//Limpa estado anterior
			if (runState) {
				stopThread();
				ThreadStart = true;
			}
			
			TaskStore tsAux = myRuningTaskStore;
			tsAux = null;
			myRuningTaskStore = new TaskStore();
			List<TaskStoreStruct> t =  userTaskList.removeAllList();
			t = null;
			t = pausedTaskList.removeAllList();
			t = null;
			

			// Adicionar aqui um debug para mostrar toda a estrutura carregada
			// do ficheiro
			// logAllFileStruct(myTextFile,HeadStruct,taskList);

			// TaskStruct ts = taskList.get(0);
			// Date date1 = ts.getDataInicio();

			Date date1 = myTextFile.getDataInicio();

			Calendar c = Calendar.getInstance();
			c.setTime(date1);
			c.add(Calendar.SECOND, (int) taskList.get(0).getDataInicio());
			date1 = c.getTime();

			TaskStoreStruct tss1 = new TaskStoreStruct(date1, myTextFile,
					HeadStruct, taskList);
			pausedTaskList.addTest(tss1);

			// Cria um novo objecto para os testes k o utilizador pode correr
			// manualmente

			/*
			 * FileNameStructObject myTextFile2 = p.parserFileName();
			 * TestHeadStruct HeadStruct2 = p.parserHead();
			 * ArrayList<TaskStruct> taskList2 = p.parserTestTask(); Date date2
			 * = new Date(); TaskStoreStruct tss2 = new TaskStoreStruct(date2,
			 * myTextFile2,HeadStruct2,taskList2);
			 */

			userTaskList.addTest(tss1.clone());
			
			if (ThreadStart) {
				startThread();
			}

		} catch (Exception ex) {
			Log.v(tag, "ERRORFOUND :: setTaks::ERROR: " + ex.toString());
		}

		// Nota : O teste so inicia quando o utilizador clicar no botão de start
	}

	private void logAllFileStruct(FileNameStructObject myTextFile,
			TestHeadStruct HeadStruct, ArrayList<TaskStruct> taskList) {

		Log
				.v(tag,
						"###################################################################");
		Log
				.v(tag,
						"-------------------------------------------------------------------");
		Log.v(tag, "-- ## FileNameStructObject ##");
		Log
				.v(tag,
						"-------------------------------------------------------------------");
		Log.v(tag, myTextFile.toString());
		Log
				.v(tag,
						"-------------------------------------------------------------------");
		Log
				.v(tag,
						"-------------------------------------------------------------------");
		Log.v(tag, "-- ## TestHeadStruct ##");
		Log
				.v(tag,
						"-------------------------------------------------------------------");
		Log.v(tag, HeadStruct.toString());
		Log
				.v(tag,
						"-------------------------------------------------------------------");
		Log
				.v(tag,
						"-------------------------------------------------------------------");
		Log.v(tag, "-- ## ArrayList<TaskStruct> ##");
		Log
				.v(tag,
						"-------------------------------------------------------------------");
		Log.v(tag, taskList.toString());
		Log
				.v(tag,
						"-------------------------------------------------------------------");
		Log
				.v(tag,
						"###################################################################");
	}

	@Override
	public void onDestroy() {

	}

	/**
	 * 
	 * Callbacks
	 * 
	 * 
	 */

	public synchronized void ThreadStopCopyAllTestToPauseStore() {
		// TODO Auto-generated method stub
		try {
			if (!runState) {
				// Se foi parado pelo utiliador, copiar os testes para a queue
				// de pause
				pausedTaskList.addTestList(myRuningTaskStore.removeAllTests());
			}
		} catch (Exception ex) {
			Log.v(tag, "ERRORFOUND :: ThreadStopCopyAllTestToPauseStore :"
					+ ex.toString());
		}
	}

	public static void setHandlers(Handler phandlerActionState,
			Handler phandlerTaskFail, Handler phandlerTaskState,
			Handler phandlerDateLastTask, Handler phandlerQueueChanged) {
		try {
			handlerActionState = phandlerActionState;
			handlerTaskFail = phandlerTaskFail;
			handlerTaskState = phandlerTaskState;
			handlerDateLastTask = phandlerDateLastTask;
			handlerQueueChanged = phandlerQueueChanged;
		} catch (Exception ex) {
			Log.v(tag, "ERRORFOUND :: setHandlers :" + ex.toString());
		}
	}

	public static void clearAllHandlers() {
		try {
			handlerActionState = null;
			handlerTaskFail = null;
			handlerTaskState = null;
			handlerDateLastTask = null;
			handlerQueueChanged = null;
		} catch (Exception ex) {
			Log.v(tag, "ERRORFOUND :: clearAllHandlers :" + ex.toString());
		}
	}

	public void EmptyQueueTest() {
		Log.v(tag, "EmptyQueueTest - In");

		try {
			runState = false;
			
			// Actualiza o estado da interface
			if (mis != null) {
				mis.setActualState(4);
			}

			if (handlerActionState != null) {
				Message msg = new Message();
				msg.arg1 = 4;
				handlerActionState.sendMessage(msg);
			}
			
			//save changed state
			MainInterfaceStateStore.saveMainInterfaceState(mis, runState);
			
		} catch (Exception ex) {
			Log.v(tag, "ERRORFOUND :: EmptyQueueTest :" + ex.toString());
		}
	}

	public void ActionRun() {
		// TODO Auto-generated method stub
		Log.v(tag, "ActionRun - In");

		try {
			runState = true;
			
			// Actualiza o estado da interface
			if (mis != null) {
				mis.setActualState(3);
			}

			if (handlerActionState != null) {
				Message msg = new Message();
				msg.arg1 = 3;
				handlerActionState.sendMessage(msg);
			}
			
			//save changed state
			MainInterfaceStateStore.saveMainInterfaceState(mis, runState);
			
		} catch (Exception ex) {
			Log.v(tag, "ERRORFOUND :: ActionRun :" + ex.toString());
		}
	}

	public void ActionStop() {
		// TODO Auto-generated method stub
		Log.v(tag, "ActionStop - In");

		try {
			runState = false;
			
			// Actualiza o estado da interface
			if (mis != null) {
				mis.setActualState(1);
			}

			if (handlerActionState != null) {
				Message msg = new Message();
				msg.arg1 = 1;
				handlerActionState.sendMessage(msg);
			}
			
			//save changed state
			MainInterfaceStateStore.saveMainInterfaceState(mis, runState);
			
		} catch (Exception ex) {
			Log.v(tag, "ERRORFOUND :: ActionStop :" + ex.toString());
		}
	}

	public void dateLastTask(String s) {
		// TODO Auto-generated method stub
		Log.v(tag, "dateLastTask - In");

		try {
			
			// Actualiza o estado da interface
			if (mis != null) {
				mis.setLastTest(s);
			}
			
			if (handlerDateLastTask != null) {
				Message msg = new Message();
				msg.obj = s;
				handlerDateLastTask.sendMessage(msg);
			}
			
			//save changed state
			MainInterfaceStateStore.saveMainInterfaceState(mis, runState);
			
		} catch (Exception ex) {
			Log.v(tag, "ERRORFOUND :: dateLastTask :" + ex.toString());
		}
	}

	public void lastTaskState(int i) {
		// TODO Auto-generated method stub
		Log.v(tag, "lastTaskState - In");

		try {
			
			// Actualiza o estado da interface
			if (mis != null) {
				mis.setStateLastTest(i);
			}
			
			if (handlerTaskState != null) {

				Message msg = new Message();

				switch (i) {
				case 1: // OK
					msg.arg1 = 1;
					break;
				case 2: // NOK
					msg.arg1 = 2;
					break;
				default: // NA
					msg.arg1 = 0;
					break;
				}

				handlerTaskState.sendMessage(msg);
			}
			
			//save changed state
			MainInterfaceStateStore.saveMainInterfaceState(mis, runState);
			
		} catch (Exception ex) {
			Log.v(tag, "ERRORFOUND :: lastTaskState :" + ex.toString());
		}
	}

	public void queueChanged() {
		// TODO Auto-generated method stub
		Log.v(tag, "queueChanged - In");

		try {
			NextTaskDate = myRuningTaskStore.getDateFirstTask();
			
			// Actualiza o estado da interface
			if (mis != null) {
				mis.setActualState(2);
				
				if (NextTaskDate != null) {
					String dataProximoTeste = NextTaskDate.toString();
					mis.setNextTest(dataProximoTeste);
				}
			}

			if (handlerQueueChanged != null) {
				Message msg = new Message();
				handlerQueueChanged.sendMessage(msg);
			}
			
			//save changed state
			MainInterfaceStateStore.saveMainInterfaceState(mis, runState);
			
		} catch (Exception ex) {
			Log.v(tag, "ERRORFOUND :: queueChanged :" + ex.toString());
		}
	}

	public void sendIntent() {
		// TODO Auto-generated method stub

	}

	public void taskFail() {
		// TODO Auto-generated method stub
		Log.v(tag, "taskFail - In");

		try {
			if (handlerTaskFail != null) {
				Message msg = new Message();
				handlerTaskFail.sendMessage(msg);
			}
			
			mis.seterrors(mis.getErrors()+1);
			
			//save changed state
			MainInterfaceStateStore.saveMainInterfaceState(mis, runState);
			
		} catch (Exception ex) {
			Log.v(tag, "ERRORFOUND :: taskFail :" + ex.toString());
		}
		
		
	}

	/**
	 * 
	 * 
	 * Callbacks para os eventos dos serviços
	 * 
	 * 
	 */

	public void initCallEvent() {
		Log
				.v(
						tag,
						"............................................Chamada iniciada pelo utilizador!!");

		try {
			// Nesta versão como não temos os eventos nos vossos testes, e so
			// temos
			// chamadas, por defeito vamos retirar sempre todos os testes
			SuspendTaskList.addTestList(myRuningTaskStore.removeAllTests());
		} catch (Exception ex) {
			Log.v(tag, "ERRORFOUND :: initCallEvent :" + ex.toString());
		}
	}

	public void hungUpCallEvent() {

		Log
				.v(
						tag,
						"............................................Chamada terminada pelo utilizador!!");

		try {
			// tenho de verificar se esta em execução
			if (runState) {

				stopThread();
				myRuningTaskStore.addTestList(SuspendTaskList.removeAllList());
				startThread();
			} else {
				myRuningTaskStore.addTestList(SuspendTaskList.removeAllList());
			}
		} catch (Exception ex) {
			Log.v(tag, "ERRORFOUND :: hungUpCallEvent :" + ex.toString());
		}
	}

	/**
	 * 
	 * Function to interface get some information
	 * 
	 * 
	 */

	public List<TaskStoreStruct> getMyRuningTaskStore() {
		try {
			return myRuningTaskStore.getTestAllList();
		} catch (Exception ex) {
			Log.v(tag, "ERRORFOUND :: getMyRuningTaskStore :" + ex.toString());
			return null;
		}
	}

	public List<TaskStoreStruct> getMyPausedTaskStore() {
		try {
			return pausedTaskList.getAllTask();
		} catch (Exception ex) {
			Log.v(tag, "ERRORFOUND :: getMyPausedTaskStore :" + ex.toString());
			return null;
		}
	}

	public List<TaskStoreStruct> getMySuspendedTaskStore() {
		try {
			return SuspendTaskList.getAllTask();
		} catch (Exception ex) {
			Log.v(tag, "ERRORFOUND :: getMySuspendedTaskStore :"
					+ ex.toString());
			return null;
		}
	}

	public Date getDateOfFirstTaskInRuningQueue() {
		// return myRuningTaskStore.getDateFirstTask();
		try {
			return NextTaskDate;
		} catch (Exception ex) {
			Log.v(tag, "ERRORFOUND :: getDateOfFirstTaskInRuningQueue :"
					+ ex.toString());
			return null;
		}
	}

	public HistoryCircularStore getHistoryCircularStore() {
		try {
			return historyStore;
		} catch (Exception ex) {
			Log.v(tag, "ERRORFOUND :: getHistoryCircularStore :"
					+ ex.toString());
			return null;
		}
	}
	
	public HistoryCircularStore getErrorHistoryCircularStore() {
		try {
			return errorHistoryStore;
		} catch (Exception ex) {
			Log.v(tag, "ERRORFOUND :: getErrorHistoryCircularStore :"
					+ ex.toString());
			return null;
		}
	}

	public TaskList getUserTaskList() {
		try {
			return userTaskList;
		} catch (Exception ex) {
			Log.v(tag, "ERRORFOUND :: getUserTaskList :" + ex.toString());
			return null;
		}
	}

	public void runTestNow(TaskStoreStruct tss) {

		try {
			// Verificar se os testes estão a correr
			if (runState) { // esta a correr

				// parar testes em execução
				stopThread();

				// adicionar teste a queue de execução
				myRuningTaskStore.addTask(tss);

				// correr testes novamente
				startThread();

			} else { // nao esta a correr

				// adicionar o teste a queue de execução
				myRuningTaskStore.addTask(tss);

				// activar a thread de execução
				threadObject = new EngineThread(myRuningTaskStore,
						historyStore, errorHistoryStore, this, s);
				myThread = new Thread(threadObject);
				myThread.start();
				ActionRun();
			}
			
			//save changed state
			MainInterfaceStateStore.saveMainInterfaceState(mis, runState);
			
		} catch (Exception ex) {
			Log.v(tag, "ERRORFOUND :: runTestNow :" + ex.toString());
		}
	}

	public static MainInterfaceState getMainInterfaceState() {
		try {
			return mis;
		} catch (Exception ex) {
			Log.v(tag, "ERRORFOUND :: getMainInterfaceState :" + ex.toString());
			return null;
		}
	}

	public static Context getMyRef() {
		try {
			return myRef;
		} catch (Exception ex) {
			Log.v(tag, "ERRORFOUND :: getMyRef :" + ex.toString());
			return null;
		}
	}

	public void timeOut(int timerCode) {
		// TODO Auto-generated method stub
		
		// se estiver parado, arranca o serviço
		if (!runState) {
			startThread();
		}
		
		// Inicia novamente o timer 
		myTimerKeepAlive = new MyTimer(3600000,this,keepaliveTimerCode);
	}
	
	

}
