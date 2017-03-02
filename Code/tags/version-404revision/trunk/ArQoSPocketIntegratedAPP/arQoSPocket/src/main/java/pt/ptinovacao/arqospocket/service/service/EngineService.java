package pt.ptinovacao.arqospocket.service.service;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.util.Pair;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.service.interfaces.IAnomaliesHistory;
import pt.ptinovacao.arqospocket.service.interfaces.IService;
import pt.ptinovacao.arqospocket.service.interfaces.ITask;
import pt.ptinovacao.arqospocket.service.interfaces.ITaskResult;
import pt.ptinovacao.arqospocket.service.interfaces.ITestResult;
import pt.ptinovacao.arqospocket.service.interfaces.ITestSchedulerReport;
import pt.ptinovacao.arqospocket.service.jsonparser.AnomaliesTopics;
import pt.ptinovacao.arqospocket.service.jsonparser.MainTestStruct;
import pt.ptinovacao.arqospocket.service.jsonparser.TestStruct;
import pt.ptinovacao.arqospocket.service.jsonparser.enums.ETestEvent;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;
import pt.ptinovacao.arqospocket.service.store.CurrentConfiguration;
import pt.ptinovacao.arqospocket.service.store.PrivateStore;
import pt.ptinovacao.arqospocket.service.structs.Anomalies;
import pt.ptinovacao.arqospocket.service.structs.TestExecutionStruct;
import pt.ptinovacao.arqospocket.service.tasks.Mobile;
import pt.ptinovacao.arqospocket.service.tasks.Wifi;
import pt.ptinovacao.arqospocket.service.tasks.interfaces.IMobileCallback;
import pt.ptinovacao.arqospocket.service.tasks.structs.MobileAdvancedInfoStruct;
import pt.ptinovacao.arqospocket.service.tasks.structs.MobileBasicInfoStruct;
import pt.ptinovacao.arqospocket.service.tasks.structs.WiFiAdvancedInfoStruct;
import pt.ptinovacao.arqospocket.service.tasks.structs.WifiBasicInfoStruct;
import pt.ptinovacao.arqospocket.service.enums.ETestType;
import pt.ptinovacao.arqospocket.service.interfaces.IServiceNetworksInfo;
import pt.ptinovacao.arqospocket.service.interfaces.ITest;
import pt.ptinovacao.arqospocket.service.interfaces.IUI;
import pt.ptinovacao.arqospocket.service.jsonresult.TaskJsonResult;
import pt.ptinovacao.arqospocket.service.tasks.interfaces.IWifiCallback;

import static pt.ptinovacao.arqospocket.service.utils.Constants.BASE_DIR;
import static pt.ptinovacao.arqospocket.service.utils.Constants.TESTS_DIR;
import static pt.ptinovacao.arqospocket.service.utils.Constants.TESTS_FILE;

public class EngineService extends Service implements IService, IWifiCallback, IMobileCallback, IServiceNetworksInfo, ITestSchedulerReport {
	
	private final static String TAG = EngineService.class.getSimpleName();
    private final static Logger logger = LoggerFactory.getLogger(EngineService.class);
	private static EngineService myRef = null;
	
	
	
	/*
	 * 
	 * Metodos para estabelecer o bind de comunicação com a UI
	 * 
	 */
	private final IBinder mBinder = new LocalBinder();
	
	public class LocalBinder extends Binder {
        public IService getService() {
            return EngineService.this;
        }
    }

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		//ui = null;
		return true;
	}
	
	
	/*
	 * 
	 * Listeners lists
	 * 
	 */
	private List<IUI> update_mobile_information_listeners = null;
	private List<IUI> update_mobile_params_listeners = null;
	
	private List<IUI> update_wifi_information_listeners = null;
	private List<IUI> update_wifi_params_listeners = null;
	
	private TreeMap<String,IUI> send_report_listeners = null;
	private TreeMap<String,IUI> send_pending_info_listeners = null;
	
	private List<IUI> update_test_info_listeners = null;
	
	private AvailableTests availableTests = null;
	private AnomaliesTopics anomaliesTopics = null;
	private AnomaliesHistory anomaliesHistory = null;
	private TestHistory testHistory = null;
	private TestScheduler testScheduler = null;
	
	
	private PrivateStore private_store = null;
	
	private Mobile mobile_task = null;
	private Wifi wifi_task = null;
	
	private GPSInformation gps_information = null;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		final String method = "onStartCommand";
		
		// warning the UI that already started
		try {
			
			MyLogger.trace(logger, method, "In");

		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		return START_STICKY;
	}
	
	@Override
	public void onCreate() {
		final String method = "onCreate";
		
		MyLogger.trace(logger, method, "In");
		
		try {
			
			myRef = this;
			
			// Instancia a memoria privada
			private_store = new PrivateStore(myRef);
			
			gps_information = new GPSInformation(myRef);
			
			// Instancia o objecto que erá guarda a lista de testes disponiveis ao utilizador
			availableTests = new AvailableTests();
			
			MyLogger.trace(logger, method, "load_anomalies_history");
			
			// Inicia o registo do historico de anomalias
			anomaliesHistory = private_store.load_anomalies_history();
			if (anomaliesHistory == null)
				anomaliesHistory = new AnomaliesHistory();
			
			MyLogger.debug(logger, method, "anomaliesHistory :"+anomaliesHistory.toString());
			
			// Inicia o registo do historico de testes
			testHistory = private_store.load_test_history();
			if (testHistory == null)
				testHistory = new TestHistory();
			
			// Instancia o scheduler dos testes
			testScheduler = new TestScheduler(myRef, gps_information, myRef, myRef);
			
			// inicializa os arrays dos listeners
			init_listeners_lists();
			
			// inicializa o testes base (informaçao de rede movel e rede wifi)
			init_base_tests();
			
			// Inicia o carregamento da lista de anomalias
			anomaliesTopics = new AnomaliesTopics(this, "Dados dummy");
			
			MyLogger.trace(logger, method, "Vou ler do ficheiro!!");
			
			// le os testes do ficheiro 
			//FileIO fileIO = new FileIO("/sdcard/AgendamentoArQoS.txt");
	        //MainTestStruct mainTestStruct = new MainTestStruct(fileIO.read_text_file()); 

			MainTestStruct mainTestStruct = new MainTestStruct(getJsonTestList());
	        
	        MyLogger.trace(logger, method, "mainTestStruct.toString() :"+mainTestStruct.toString());
			
			
	        // adiciona os teste o gestor de execução
	        for (TestStruct testStruct : mainTestStruct.get_test_list()) {
	        	
	        	if (testStruct.get_state() != 0) {
					if (testStruct.get_testtype() != ETestEvent.USER_REQUEST) {
						testScheduler.add_test_to_scheduler(testStruct, ETestType.SCHEDULED);
					} else {
						availableTests.add_test(testStruct);
					}

					MyLogger.trace(logger, method, "Teste adicionado!");
				} else {
					MyLogger.trace(logger, method, "Teste desactivado!");
				}
	        	
	        }
			
	        MyLogger.trace(logger, method, "--------------------------------------------------------"); 
			
			MyLogger.trace(logger, method, "availableTests :"+availableTests.toString());
			
			MyLogger.trace(logger, method, "---------------------- testScheduler ----------------------------------");
			
			MyLogger.trace(logger, method, "testScheduler :"+testScheduler.toString());
			
			MyLogger.trace(logger, method, "--------------------------------------------------------");

        	
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}


	private String getJsonTestList() {

		String path = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ File.separator
				+ BASE_DIR
				+ File.separator
				+ TESTS_DIR;
		final File file = new File(path, TESTS_FILE);
		if(file.exists()){
            Log.d(TAG, "getJsonTestList :: Reading tests from external file: " + file.getAbsolutePath());
			StringBuilder text = new StringBuilder();

			try {
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line = null;
				while ((line = br.readLine()) != null) {
					text.append(line);
				}

				br.close();
				return text.toString();
			} catch (IOException e) {
                Log.d(TAG, "getJsonTestList :: Exception reading from external file: " + e.getMessage());
            }
		}
//TODO remove this
        Log.d(TAG, "getJsonTestList :: Reading tests from asset manager" );

		AssetManager assetManager = getAssets();
		ByteArrayOutputStream outputStream = null;
		InputStream inputStream = null;
		try {
			inputStream = assetManager.open("tests.json");
			outputStream = new ByteArrayOutputStream();
			byte buf[] = new byte[8192];
			int len;
			try {
				while ((len = inputStream.read(buf)) != -1) {
					outputStream.write(buf, 0, len);
				}
				outputStream.close();
				inputStream.close();
			} catch (IOException e) {
			}
		} catch (IOException e) {
		}
		return outputStream.toString();


	}
	
	private void init_listeners_lists() {
		final String method = "init_listeners_lists";
		
		try {
			
			update_mobile_information_listeners = new ArrayList<IUI>();
			update_mobile_params_listeners = new ArrayList<IUI>();
			update_wifi_information_listeners = new ArrayList<IUI>();
			update_wifi_params_listeners = new ArrayList<IUI>();
			update_test_info_listeners = new ArrayList<IUI>();
			
			send_report_listeners = new TreeMap<String,IUI>();
			send_pending_info_listeners = new TreeMap<String,IUI>();
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	private void init_base_tests() {
		final String method = "init_base_tests";
		
		try {
			
			mobile_task = new Mobile(myRef, myRef);
			wifi_task = new Wifi(myRef, myRef);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	
	
	/************
	 * 
	 * 		IService
	 * 
	 * ***********************/
	
	public boolean isMobileAvailable() {
		final String method = "isMobileAvailable";
		
		try {
			return mobile_task.isMobileAvailable();
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return false;
	}
	
	public boolean isWiFiAvailable() {
		final String method = "isWiFiAvailable";
		
		try {
			return wifi_task.isWiFiAvailable();
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return false;
	}
	
	@Override
	public boolean get_execution_state() {
		final String method = "get_execution_state";
		
		try {
			return testScheduler.get_in_execution();
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return false;
	}
	
	@Override
	public Pair<Boolean, String> run_test(String test_id) {
		final String method = "run_test";
		
		try {
			
			MyLogger.debug(logger, method, "In");
			
			// procura o teste na lista de testes disponiveis
			TestStruct testStruct = availableTests.get_test_by_id(test_id);
			
			if (testStruct != null) {
				
				
				//MyLogger.debug(logger, method, "-------------   testStruct :"+testStruct.toString());
				//MyLogger.debug(logger, method, "----------------------------------------------------");
				
				testStruct = (TestStruct) testStruct.clone();
				
				//MyLogger.debug(logger, method, "-------------  clone testStruct :"+testStruct.toString());
				//MyLogger.debug(logger, method, "----------------------------------------------------");
				
				// atualiza a data de execução
				//MyLogger.debug(logger, method, "-------------   System.currentTimeMillis() :"+System.currentTimeMillis());
				testStruct.set_dataini(System.currentTimeMillis());
				String newTestID = test_id+"_"+System.currentTimeMillis();
				testStruct.set_testeid(newTestID);
				
				//MyLogger.debug(logger, method, "------------- testStruct 2 :"+testStruct.toString());
				//MyLogger.debug(logger, method, "----------------------------------------------------");
			
				// verifica se já existem teste em execução
				
				boolean alreadyInExecution = testScheduler.get_in_execution();
				MyLogger.debug(logger, method, "alreadyInExecution :"+alreadyInExecution);
				
				if (!alreadyInExecution) {
			
					// manda correr o teste se nao existir nenhum teste em execução
					boolean result = testScheduler.add_test_to_scheduler(testStruct, ETestType.USER_REQUEST);
					
					//MyLogger.debug(logger, method, "---------- add_test_to_scheduler :"+result);
					//MyLogger.debug(logger, method, "----------------------------------------------------");
					
					//MyLogger.debug(logger, method, "---------- testScheduler :"+testScheduler.toString());
					//MyLogger.debug(logger, method, "----------------------------------------------------");
					
					MyLogger.debug(logger, method, "Return :"+result+" with newTestID :"+newTestID);
					return new Pair<Boolean, String>(result, newTestID);
					
				} else {
					
					MyLogger.debug(logger, method, "Return false with newTestID :"+newTestID);
					return new Pair<Boolean, String>(false, newTestID);
				}
				
			} else {
				
				MyLogger.debug(logger, method, "Return false with newTestID null");
				return new Pair<Boolean, String>(false, null);
			}
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		MyLogger.debug(logger, method, "Return false,null");
		return new Pair<Boolean, String>(false, null);
	}
	
	
	@Override
	public Pair<Bitmap, String> get_mobile_operator_branding() {
		
		// TODO: fazer numa versão mais à frente. Pedir logo do operador a um website
		return null;
	}
	
	public synchronized List<IUI> get_update_mobile_information_listeners() {
		return update_mobile_information_listeners; 
	}

	@Override
	public synchronized boolean registry_update_mobile_information(IUI ui_ref) {
		final String method = "registry_update_mobile_information";
		
		try {
			 return update_mobile_information_listeners.add(ui_ref);
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return false;
	}
	
	@Override
	public synchronized boolean remove_registry_update_mobile_information(IUI ui_ref) {
		final String method = "remove_registry_update_mobile_information";
		
		try {
			return update_mobile_information_listeners.remove(ui_ref);
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return false;
	}

	@Override
	public Pair<Bitmap, String> get_wifi_operator_branding() {
		
		// TODO: fazer numa próxima versão
		return null;
	}
	
	public synchronized List<IUI> get_update_wifi_information_listeners() {
		return update_wifi_information_listeners;
	}
	

	@Override
	public synchronized boolean registry_update_wifi_information(IUI ui_ref) {
		final String method = "registry_update_wifi_information";
		
		try {
			 return update_wifi_information_listeners.add(ui_ref);
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return false;
	}
	
	@Override
	public synchronized boolean remove_registry_update_wifi_information(IUI ui_ref) {
		final String method = "remove_registry_update_wifi_information";
		
		try {
			 return update_wifi_information_listeners.remove(ui_ref);
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return false;
	}
	
	public synchronized List<IUI> get_update_mobile_params_listeners() {
		return update_mobile_params_listeners;
	}

	@Override
	public synchronized boolean registry_update_mobile_params(IUI ui_ref) {
		final String method = "registry_update_mobile_params";
		
		try {
			 return update_mobile_params_listeners.add(ui_ref);
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return false;
	}
	
	@Override
	public synchronized boolean remove_registry_update_mobile_params(IUI ui_ref) {
		final String method = "remove_registry_update_mobile_params";
		
		try {
			 return update_mobile_params_listeners.remove(ui_ref);
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return false;
	}
	
	public synchronized List<IUI> get_update_wifi_params_listeners() {
		return update_wifi_params_listeners;
	}
	
	@Override
	public synchronized boolean registry_update_wifi_params(IUI ui_ref) {
		final String method = "registry_update_wifi_params";
		
		try {
			 return update_wifi_params_listeners.add(ui_ref);
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return false;
	}
	
	@Override
	public synchronized boolean remove_registry_update_wifi_params(IUI ui_ref) {
		final String method = "remove_registry_update_wifi_params";
		
		try {
			 return update_wifi_params_listeners.remove(ui_ref);
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return false;
	}

	@Override
	public boolean restart_mobile_interface() {
		final String method = "restart_mobile_interface";
		
		try {
			
			mobile_task.setAirPlaneMode(true);
			mobile_task.setAirPlaneMode(false);
			
			return true;
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return false;
	}

	@Override
	public boolean restart_wifi_interface() {
		final String method = "restart_wifi_interface";
		
		try {
			
			final int maxTrys = 3;
			
			int count = 0;
			while (!wifi_task.disableWifi() && count++<maxTrys);
			
			if (count >= maxTrys)
				return false;
			
			count = 0;
			while (!wifi_task.enableWifi() && count++<maxTrys);
			
			if (count >= maxTrys)
				return false;
			
			return true;
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return false;
	}

	@Override
	public TreeMap<String, String> get_anomalies() {
		final String method = "get_anomalies";
		
		try {
			
			return anomaliesTopics.get_anomalies();
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}

	@Override
	public TreeMap<String, String> get_anomalies_details(String anomaly) {
		final String method = "get_anomalies_details";
		
		try {
			
			return anomaliesTopics.get_anomalies_details(anomaly);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}

	@Override
	public Location get_location() {
		final String method = "get_anomalies_details";
		
		Location location = null;
		
		try {
			
			location = gps_information.getLocation();
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return location;
	}

	@Override
	public void send_report(String anomaly, String anomaly_details,
			Location location, String anomaly_report, IUI ui_ref) {
		final String method = "send_report";
		
		try {
		
			MyLogger.trace(logger, method, "In");
			
			// TODO: apos adicionar as anormalias, temos de confirmar a interface que foi enviar atraves do callback....................
			anomaliesHistory.add_anomalie_to_history(new Anomalies(new Date(System.currentTimeMillis()), anomaliesTopics.get_logo_id_from_anomalie(anomaly), anomaly, anomaly_details, location, anomaly_report));
			
			MyLogger.debug(logger, method, "Addicionar a store :"+anomaliesHistory.toString());
			
			private_store.save_anomalies_history(anomaliesHistory);
			
			MyLogger.trace(logger, method, "Done");
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}		
	}

	@Override
	public List<IAnomaliesHistory> get_all_anomalies_history() {
		final String method = "get_all_anomalies_history";
		
		try {
			
			return anomaliesHistory.get_all_anomalies_history_only();
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}

	@Override
	public List<ITest> get_all_available_tests() {
		final String method = "get_all_available_tests";
		
		try {
			
			// Dos teste que estão disponiveis vou filtrar os que já estão em execução a agendados para execução
			
			List<ITest> resultTestList = new ArrayList<ITest>();
			
			List<TestExecutionStruct> execution_tests_list = merge_TestExecutionStruct_lists(testScheduler.get_running_tests(), testScheduler.get_waiting_tests());
			
			boolean founded = false;
			for (ITest iTest :availableTests.get_available_test()) {
				
				founded = false;
				for (TestExecutionStruct testExecutionStruct :execution_tests_list)
					if (testExecutionStruct.get_test_id().contains(iTest.get_test_id()))
						founded = true;
				
				if (!founded)
					resultTestList.add(iTest);
			}
			
			return resultTestList;
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}

		return null;
	}
	
	private List<TestExecutionStruct> merge_TestExecutionStruct_lists(List<TestExecutionStruct> l1, List<TestExecutionStruct> l2) {
		final String method = "merge_TestExecutionStruct_lists";
		
		try {
			
			List<TestExecutionStruct> resultList = new ArrayList<TestExecutionStruct>();
			
			for (TestExecutionStruct testExecutionStruct :l1)
				resultList.add(testExecutionStruct);
			
			for (TestExecutionStruct testExecutionStruct :l2)
				resultList.add(testExecutionStruct);
			
			return resultList;
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}
	
	@Override
	public List<ITestResult> get_all_tests_in_execution() {
		final String method = "get_all_tests_in_execution";
		
		List<ITestResult> result = null;
		
		try {
			
			result = new ArrayList<ITestResult>();
			
			
			List<TestExecutionStruct> run_tests = testScheduler.get_running_tests();
			if (run_tests == null)
				MyLogger.debug(logger, method, "run_tests == null");
			else
				MyLogger.debug(logger, method, "run_tests :"+run_tests.toString());
			
			
			
			
			List<TestExecutionStruct> wait_tests = testScheduler.get_waiting_tests();
			if (wait_tests == null) {
				MyLogger.debug(logger, method, "wait_tests == null");
			}else {
				
				
				//MyLogger.debug(logger, method, "wait_tests :"+wait_tests.toString());
				MyLogger.debug(logger, method, "wait_tests != null");
				for (TestExecutionStruct testExecutionStruct :wait_tests) {
					MyLogger.debug(logger, method, "testExecutionStruct :"+testExecutionStruct.get_test_name());
					
					
					MyLogger.debug(logger, method, "get_test_id :"
							+ testExecutionStruct.get_test_id());
					MyLogger.debug(logger, method, "get_number_of_tests :"
							+ testExecutionStruct.get_number_of_tests());
					MyLogger.debug(logger, method, "get_number_of_tests_done :"
							+ testExecutionStruct.get_number_of_tests_done());
					MyLogger.debug(logger, method, "get_run_test_state :"
							+ testExecutionStruct.get_run_test_state());
					MyLogger.debug(logger, method, "get_task_list :"
							+ testExecutionStruct.get_task_list());
					MyLogger.debug(logger, method, "get_test_execution_location :"
							+ testExecutionStruct.get_test_execution_location());
					MyLogger.debug(logger, method, "get_test_name :"
							+ testExecutionStruct.get_test_name());
					MyLogger.debug(logger, method, "get_test_state :"
							+ testExecutionStruct.get_test_state());
					MyLogger.debug(logger, method, "get_test_type :"
							+ testExecutionStruct.get_test_type());
					
					
					List<ITaskResult> task_result_list = testExecutionStruct.get_task_list();
					if (task_result_list == null) {
						MyLogger.debug(logger, method, "task_result_list == null");
					}else {
						MyLogger.debug(logger, method, "task_result_list.size() :"+task_result_list.size());
						for (ITaskResult iTaskResult :task_result_list)
							MyLogger.debug(logger, method, "get_task_name :"+iTaskResult.get_task_name());
					}
					
					List<TaskJsonResult> task_json_result_list = testExecutionStruct.get_task_result_list();
					if (task_json_result_list == null)
						MyLogger.debug(logger, method, "task_json_result_list == null");
					else {
						MyLogger.debug(logger, method, "task_json_result_list.size() :"+task_json_result_list.size());
						for (TaskJsonResult taskJsonResult :task_json_result_list)
							MyLogger.debug(logger, method, "taskJsonResult_get_task_name :"+taskJsonResult.get_task_name());
					}
					
					List<ITask> task_list_to_do = testExecutionStruct.get_task_list_to_do();
					if (task_list_to_do == null)
						MyLogger.debug(logger, method, "task_list_to_do == null");
					else {
						MyLogger.debug(logger, method, "task_list_to_do.size() :"+task_list_to_do.size());
						for (ITask iTask :task_list_to_do) {
							MyLogger.debug(logger, method, "iTask.get_task_name :"+iTask.get_task_name());
							MyLogger.debug(logger, method, "iTask.get_task_id :"+iTask.get_task_id());
						}
					}
				}
				
				
			}
			
			MyLogger.debug(logger, method, "End listing....");
			
			result.addAll(testScheduler.get_running_tests());
			result.addAll(testScheduler.get_waiting_tests());
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}

		return result;
	}
	
	public synchronized List<IUI> get_update_test_info_listeners() {
		return update_test_info_listeners;
	}

	@Override
	public synchronized boolean registry_update_test_info(IUI ui_ref) {
		final String method = "registry_update_test_info";
		
		try {
			 return update_test_info_listeners.add(ui_ref);
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return false;
	}
	
	@Override
	public synchronized boolean remove_registry_update_test_info(IUI ui_ref) {
		final String method = "remove_registry_update_test_info";
		
		try {
			 return update_test_info_listeners.remove(ui_ref);
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return false;
	}

	@Override
	public List<ITestResult> get_tests_history() {
		final String method = "get_tests_history";
		
		try {
			
			return testHistory.get_test_history();
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}

		return null;
	}

	@Override
	public void send_pending_tests(IUI ui_ref) {
		final String method = "send_pending_tests";
		
		try {
			
			testHistory.send_tests_result_to_server();
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}

	@Override
	public CurrentConfiguration get_current_configuration() {
		final String method = "get_current_configuration";
		
		try {
			 return PrivateStore.getCurrentConfiguration();
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}

	@Override
	public boolean set_current_configuration(CurrentConfiguration configObject) {
		final String method = "set_current_configuration";
		
		try {
			 return PrivateStore.setCurrentConfiguration(configObject);
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}

		return false;
	}
	
	@Override
	public void get_wifi_information(IUI ui_ref) {
		final String method = "get_wifi_information";
		
		try {

			WifiBasicInfoStruct wifiBasicInfoStruct = wifi_task.get_WifiBasicInfoStruct();
			
			MyLogger.debug(logger, method, wifiBasicInfoStruct.toString());

			ui_ref.update_wifi_information(wifiBasicInfoStruct.get_link_speed(), wifiBasicInfoStruct.get_ssid(), 
					wifiBasicInfoStruct.get_rx_level(), wifiBasicInfoStruct.get_channel());

		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	@Override
	public void get_mobile_params(IUI ui_ref) {
		final String method = "get_mobile_params";
		
		try {

			MobileAdvancedInfoStruct mobileAdvancedInfoStruct = this.mobile_task.get_MobileAdvancedInfoStruct();
			
			TreeMap<String, String> keyValueParams = new TreeMap<String, String>();
			keyValueParams.put(getString(R.string.mobile_params_device_id), mobileAdvancedInfoStruct.get_device_id());
			keyValueParams.put(getString(R.string.mobile_params_cell_id), (mobileAdvancedInfoStruct.get_id_cell().equals("-1"))?getString(R.string.na):mobileAdvancedInfoStruct.get_id_cell());
			//keyValueParams.put(ParamsDetailsMapping.MobileParamsDetailsMapping(9, "PT"), mobileAdvancedInfoStruct.);
			keyValueParams.put(getString(R.string.mobile_params_provider_code), mobileAdvancedInfoStruct.get_mcc_mnc());
			if (mobileAdvancedInfoStruct.get_msisdn() != null)
				keyValueParams.put(getString(R.string.mobile_params_msisdn), (mobileAdvancedInfoStruct.get_msisdn().equals(""))?getString(R.string.na):mobileAdvancedInfoStruct.get_msisdn());
			else
				keyValueParams.put(getString(R.string.mobile_params_msisdn), getString(R.string.na));
			keyValueParams.put(getString(R.string.mobile_params_network_operator_name), mobileAdvancedInfoStruct.get_network_operator_name());
			keyValueParams.put(getString(R.string.mobile_params_imsi), mobileAdvancedInfoStruct.get_imsi());
			keyValueParams.put(getString(R.string.mobile_params_roaming), (mobileAdvancedInfoStruct.get_roaming())?getString(R.string.yes):getString(R.string.no));
					
			ui_ref.update_mobile_params(keyValueParams);

		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}

	@Override
	public void get_wifi_params(IUI ui_ref) {
		final String method = "get_wifi_params";
		
		try {

			WiFiAdvancedInfoStruct wiFiAdvancedInfoStruct = wifi_task.get_WiFiAdvancedInfoStruct();
			
			MyLogger.debug(logger, method, wiFiAdvancedInfoStruct.toString());
			
			TreeMap<String, String> keyValueParams = new TreeMap<String, String>();
			keyValueParams.put(getString(R.string.wifi_params_essid), wiFiAdvancedInfoStruct.get_ssid());
			keyValueParams.put(getString(R.string.wifi_params_channel), wiFiAdvancedInfoStruct.get_channel());
			keyValueParams.put(getString(R.string.wifi_params_bssid), wiFiAdvancedInfoStruct.get_bssid());
			keyValueParams.put(getString(R.string.wifi_params_hidden_ssid), wiFiAdvancedInfoStruct.get_hidden_ssid()?getString(R.string.yes):getString(R.string.no));
			keyValueParams.put(getString(R.string.wifi_params_mac_address), wiFiAdvancedInfoStruct.get_mac_address());
			keyValueParams.put(getString(R.string.wifi_params_ip_address), wiFiAdvancedInfoStruct.get_ip_address());
			keyValueParams.put(getString(R.string.wifi_params_primary_dns), wiFiAdvancedInfoStruct.get_dns1());
			keyValueParams.put(getString(R.string.wifi_params_secondary_dns), wiFiAdvancedInfoStruct.get_dns2());
			keyValueParams.put(getString(R.string.wifi_params_gateway_adress), wiFiAdvancedInfoStruct.get_gateway());
			keyValueParams.put(getString(R.string.wifi_params_lease_duration), wiFiAdvancedInfoStruct.get_lease_duration().endsWith("-1")?getString(R.string.na):wiFiAdvancedInfoStruct.get_lease_duration());
			keyValueParams.put(getString(R.string.wifi_params_netmask), wiFiAdvancedInfoStruct.get_netmask());
			keyValueParams.put(getString(R.string.wifi_params_server_address), wiFiAdvancedInfoStruct.get_server_address());
			
			ui_ref.update_wifi_params(keyValueParams);

		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	
	@Override
	public void get_mobile_information(IUI ui_ref) {
		final String method = "get_mobile_information";
		
		try {

			MobileBasicInfoStruct mobileBasicInfoStruct = this.mobile_task.get_MobileBasicInfoStruct();
			
			ui_ref.update_mobile_information(mobileBasicInfoStruct.get_mobile_state(), mobileBasicInfoStruct.get_network_type(),
					mobileBasicInfoStruct.get_mcc(), mobileBasicInfoStruct.get_signal_level(), mobileBasicInfoStruct.get_id_cell(),
					mobileBasicInfoStruct.get_cell_location());

		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	
	
	/************
	 * 
	 * 		IWifiCallback
	 * 
	 * ***********************/
	
	
	private void report_to_wifi_information_listeners(WifiBasicInfoStruct wifiBasicInfoStruct) {
		final String method = "report_to_wifi_information_listeners";
		
		try {

			List<IUI> list = get_update_wifi_information_listeners();
			
			for (IUI ref :list) {
				
				try {
					
					ref.update_wifi_information(wifiBasicInfoStruct.get_link_speed(), wifiBasicInfoStruct.get_ssid(), 
							wifiBasicInfoStruct.get_rx_level(), wifiBasicInfoStruct.get_channel());
					
				} catch(Exception ex) {
					MyLogger.error(logger, method, ex);
				}
				
			}

		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	private void report_to_wifi_params_listeners(WiFiAdvancedInfoStruct wiFiAdvancedInfoStruct) {
		final String method = "report_to_wifi_params_listeners";
		
		try {

			TreeMap<String, String> keyValueParams = new TreeMap<String, String>();
			keyValueParams.put(getString(R.string.wifi_params_essid), wiFiAdvancedInfoStruct.get_ssid());
			keyValueParams.put(getString(R.string.wifi_params_channel), wiFiAdvancedInfoStruct.get_channel());
			keyValueParams.put(getString(R.string.wifi_params_bssid), wiFiAdvancedInfoStruct.get_bssid());
			keyValueParams.put(getString(R.string.wifi_params_hidden_ssid), wiFiAdvancedInfoStruct.get_hidden_ssid()?getString(R.string.yes):getString(R.string.no));
			keyValueParams.put(getString(R.string.wifi_params_mac_address), wiFiAdvancedInfoStruct.get_mac_address());
			keyValueParams.put(getString(R.string.wifi_params_ip_address), wiFiAdvancedInfoStruct.get_ip_address());
			keyValueParams.put(getString(R.string.wifi_params_primary_dns), wiFiAdvancedInfoStruct.get_dns1());
			keyValueParams.put(getString(R.string.wifi_params_secondary_dns), wiFiAdvancedInfoStruct.get_dns2());
			keyValueParams.put(getString(R.string.wifi_params_gateway_adress), wiFiAdvancedInfoStruct.get_gateway());
			keyValueParams.put(getString(R.string.wifi_params_lease_duration), wiFiAdvancedInfoStruct.get_lease_duration().endsWith("-1")?getString(R.string.na):wiFiAdvancedInfoStruct.get_lease_duration());
			keyValueParams.put(getString(R.string.wifi_params_netmask), wiFiAdvancedInfoStruct.get_netmask());
			keyValueParams.put(getString(R.string.wifi_params_server_address), wiFiAdvancedInfoStruct.get_server_address());
			
			List<IUI> list = get_update_wifi_params_listeners();
			
			for (IUI ref :list) {
				
				try {
					ref.update_wifi_params(keyValueParams);
				} catch(Exception ex) {
					MyLogger.error(logger, method, ex);
				}
				
			}

		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	@Override
	public void wifi_information_change() {
		final String method = "wifi_information_change";
		
		try {

			WifiBasicInfoStruct wifiBasicInfoStruct = wifi_task.get_WifiBasicInfoStruct();
			
			MyLogger.debug(logger, method, wifiBasicInfoStruct.toString());
			report_to_wifi_information_listeners(wifiBasicInfoStruct);

		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}		
	}

	@Override
	public void wifi_params_change() {
		final String method = "wifi_params_change";
		
		try {

			WiFiAdvancedInfoStruct wiFiAdvancedInfoStruct = wifi_task.get_WiFiAdvancedInfoStruct();
			
			MyLogger.debug(logger, method, wiFiAdvancedInfoStruct.toString());
			report_to_wifi_params_listeners(wiFiAdvancedInfoStruct);

		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	
	
	
	/************
	 * 
	 * 		IMobileCallback
	 * 
	 * ***********************/

	private void report_to_mobile_information_listeners(MobileBasicInfoStruct mobileBasicInfoStruct) {
		final String method = "report_to_mobile_information_listeners";
		
		try {
			
			MyLogger.trace(logger, method, "In");

			List<IUI> list = get_update_mobile_information_listeners();
			
			if (list == null)
				MyLogger.trace(logger, method, "list == null");
			else
				MyLogger.trace(logger, method, "list.size() :"+list.size());
			
			for (IUI ref :list) {
				
				try {
					
					ref.update_mobile_information(mobileBasicInfoStruct.get_mobile_state(), mobileBasicInfoStruct.get_network_type(), mobileBasicInfoStruct.get_mcc_mnc(),
							mobileBasicInfoStruct.get_signal_level(), mobileBasicInfoStruct.get_id_cell(), mobileBasicInfoStruct.get_cell_location());
					
				} catch(Exception ex) {
					MyLogger.error(logger, method, ex);
				}
				
			}

		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	private void report_to_mobile_params_listeners(MobileAdvancedInfoStruct mobileAdvancedInfoStruct) {
		final String method = "report_to_mobile_params_listeners";
		
		try {
			
			MyLogger.trace(logger, method, "In");
			//TODO desduplicar isto
			TreeMap<String, String> keyValueParams = new TreeMap<String, String>();
			keyValueParams.put(getString(R.string.mobile_params_device_id), mobileAdvancedInfoStruct.get_device_id());
			keyValueParams.put(getString(R.string.mobile_params_cell_id), (mobileAdvancedInfoStruct.get_id_cell().equals("-1"))?getString(R.string.na):mobileAdvancedInfoStruct.get_id_cell());
			//keyValueParams.put(ParamsDetailsMapping.MobileParamsDetailsMapping(9, "PT"), mobileAdvancedInfoStruct.);
			keyValueParams.put(getString(R.string.mobile_params_provider_code), mobileAdvancedInfoStruct.get_mcc_mnc());
			if (mobileAdvancedInfoStruct.get_msisdn() != null)
				keyValueParams.put(getString(R.string.mobile_params_msisdn), (mobileAdvancedInfoStruct.get_msisdn().equals(""))?getString(R.string.na):mobileAdvancedInfoStruct.get_msisdn());
			else
				keyValueParams.put(getString(R.string.mobile_params_msisdn), getString(R.string.na));
			keyValueParams.put(getString(R.string.mobile_params_network_operator_name), mobileAdvancedInfoStruct.get_network_operator_name());
			keyValueParams.put(getString(R.string.mobile_params_imsi), mobileAdvancedInfoStruct.get_imsi());
			keyValueParams.put(getString(R.string.mobile_params_roaming), (mobileAdvancedInfoStruct.get_roaming())?getString(R.string.yes):getString(R.string.no));
			
			List<IUI> list = get_update_mobile_params_listeners();
			
			if (list == null)
				MyLogger.trace(logger, method, "list == null");
			else
				MyLogger.trace(logger, method, "list.size() :"+list.size());
			
			for (IUI ref :list) {
				
				try {
					
					ref.update_mobile_params(keyValueParams);
					
				} catch(Exception ex) {
					MyLogger.error(logger, method, ex);
				}
				
			}

		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	

	@Override
	public void mobile_information_change() {
		final String method = "mobile_information_change";
		
		try {

			MobileBasicInfoStruct mobileBasicInfoStruct = this.mobile_task.get_MobileBasicInfoStruct();
			
			MyLogger.debug(logger, method, mobileBasicInfoStruct.toString());
			report_to_mobile_information_listeners(mobileBasicInfoStruct);

		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}

	@Override
	public void mobile_params_change() {
		final String method = "mobile_params_change";
		
		try {

			MobileAdvancedInfoStruct mobileAdvancedInfoStruct = this.mobile_task.get_MobileAdvancedInfoStruct();
			
			MyLogger.debug(logger, method, mobileAdvancedInfoStruct.toString());
			report_to_mobile_params_listeners(mobileAdvancedInfoStruct);

		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}

	
	
	/************
	 * 
	 * 		IServiceNetworksInfo
	 * 
	 * ***********************/
	
	public Mobile get_mobile_ref() {
		return mobile_task;
	}
	
	public Wifi get_wifi_ref() {
		return wifi_task;
	}

	
	/************
	 * 
	 * 		ITestSchedulerReport
	 * 
	 * ***********************/
	
	@Override
	public void test_execution_complete(
			TestExecutionStruct testExecutionStruct) {
		final String method = "test_execution_complete";
		
		try {
			
			MyLogger.trace(logger, method, "In");
			MyLogger.debug(logger, method, "testExecutionStruct :"+testExecutionStruct.toString());
		
			testHistory.add_test_to_history(testExecutionStruct);
			
			MyLogger.debug(logger, method, "testHistory.get_test_history :"+testHistory.get_test_history().toString());
			
			boolean resultSave = private_store.save_test_history(testHistory);
			
			MyLogger.debug(logger, method, "resultSave :"+resultSave);
			
			// reporta a atualização para todos os listeners
			List<IUI> list = get_update_test_info_listeners();
			
			for (IUI ref :list) {
				
				try {
					
					ref.update_test_info();
					
				} catch(Exception ex) {
					MyLogger.error(logger, method, ex);
				}
				
			}	
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}

	@Override
	public void test_execution_updated(String test_id) {  
		final String method = "test_execution_updated";
		
		try {
			
			MyLogger.trace(logger, method, "In");
			
			List<IUI> list = get_update_test_info_listeners();
			
			for (IUI ref :list) {
				
				try {
					
					MyLogger.trace(logger, method, "Reported");
					ref.update_test_task(test_id);
					
				} catch(Exception ex) {
					MyLogger.error(logger, method, ex);
				}
			}
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		MyLogger.trace(logger, method, "Out");
	}


	
}
