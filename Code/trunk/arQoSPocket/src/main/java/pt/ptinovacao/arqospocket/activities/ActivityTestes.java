package pt.ptinovacao.arqospocket.activities;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.enums.ETestType;
import pt.ptinovacao.arqospocket.service.interfaces.IService;
import pt.ptinovacao.arqospocket.service.interfaces.ITask;
import pt.ptinovacao.arqospocket.service.interfaces.ITaskResult;
import pt.ptinovacao.arqospocket.service.interfaces.ITest;
import pt.ptinovacao.arqospocket.service.interfaces.ITestResult;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;
import pt.ptinovacao.arqospocket.ArqosActivity;
import pt.ptinovacao.arqospocket.MyApplication;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.core.RowItemTestes;
import pt.ptinovacao.arqospocket.fragments.FragmentTestes;
import pt.ptinovacao.arqospocket.fragments.FragmentTestesDetalhes;
import pt.ptinovacao.arqospocket.interfaces.IFragmentChange;
import pt.ptinovacao.arqospocket.util.Homepage;
import pt.ptinovacao.arqospocket.util.MenuOption;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

public class ActivityTestes extends ArqosActivity implements IFragmentChange {

	private final static Logger logger = LoggerFactory
			.getLogger(ActivityTestes.class);
	private static final String TAG = "Testes";
	private static final String ITEM_ID = "ItemID";
	private static final String ITEM_ID_2 = "ItemID2";

	private List<ITest> Testes;
	private List<ITestResult> TestesExecution;
	Fragment fragment;
	ArrayList<RowItemTestes> testes, testesExecution;
	int Emcurso = 0, imagetipe, Inactivo = 0, Filtro = 0;
	private FragmentManager fragMgr;
	MenuOption HomeP;
	Homepage home;


	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		super.onActionBarSetTitle(getString(R.string.tests));
		super.setMenuOption(MenuOption.Testes);

		setContentView(R.layout.activity_testes);
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		FragmentTestes llf = new FragmentTestes();
		home = new Homepage (this);

		ft.add(R.id.fragment_testes, llf);
		ft.commit();

		// regista para receber os testes
		getInfo();
	}

	public void getInfo() {
		final String method = "getInfo";

		try {
			
		Emcurso = 0;
		Inactivo = 0;
		MyApplication MyApplicationRef = (MyApplication) getApplicationContext();
		IService iService = MyApplicationRef.getEngineServiceRef();

		Testes = iService.get_all_available_tests();
		TestesExecution = iService.get_all_tests_in_execution();

		MyLogger.trace(logger, method, "get_all_available_tests :");
		if (Testes != null)
			for (ITest itest : Testes) {
				MyLogger.trace(logger, method,
						"get_test_id :" + itest.get_test_id());
				MyLogger.trace(logger, method,
						"get_test_name :" + itest.get_test_name());
				for (ITask iTask : itest.get_task_list()) {
					MyLogger.trace(logger, method,
							"get_task_id :" + iTask.get_task_id());
					MyLogger.trace(logger, method,
							"get_task_name :" + iTask.get_task_name());
				}
				MyLogger.trace(logger, method,
						"---------------------------------------");
			}

		MyLogger.trace(logger, method,
				"-----------  get_all_tests_in_execution : ------------");
		if (TestesExecution != null)
			for (ITestResult itestResult : TestesExecution) {
				MyLogger.trace(logger, method,
						"get_test_id :" + itestResult.get_test_id());
				MyLogger.trace(logger, method,
						"get_test_name :" + itestResult.get_test_name());
				MyLogger.trace(logger, method,
						"------------ get_task_list_to_do:");
				for (ITask iTask : itestResult.get_task_list_to_do()) {
					MyLogger.trace(logger, method,
							"get_task_id :" + iTask.get_task_id());
					MyLogger.trace(logger, method,
							"get_task_name :" + iTask.get_task_name());
				}
				MyLogger.trace(logger, method, "------------ get_task_list:");
				for (ITaskResult iTaskResult : itestResult.get_task_list()) {
					MyLogger.trace(logger, method, "get_task_id :"
							+ iTaskResult.get_task_id());
					MyLogger.trace(logger, method, "get_task_name :"
							+ iTaskResult.get_task_name());
					MyLogger.trace(logger, method, "get_task_state :"
							+ iTaskResult.get_task_state());
				}

				MyLogger.trace(logger, method,
						"---------------------------------------");
			}

		testes = teste();
		testesExecution = testeExecution();
		
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}

	}

	public boolean hasTestesExecution() {
		return TestesExecution != null && TestesExecution.size() > 0;
	}

	public boolean hasTeste() {
		return Testes != null && Testes.size() > 0;
	}

	public ArrayList<RowItemTestes> teste() {
		final String method = "teste";

		ArrayList<RowItemTestes> testList = null;
		
		try {
			
		if (hasTeste()) {
			testList = new ArrayList<RowItemTestes>();

			for (ITest testes : Testes) {
				RowItemTestes item = new RowItemTestes(testes.get_test_id(),
						testes.get_execution_date(), testes.get_test_name(),
						testes.get_test_type(), R.drawable.icon_on_demand_litle);
				Inactivo++;
				testList.add(item);

			}
		}
		
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return testList;

	}

	public ArrayList<RowItemTestes> testeExecution() {
		final String method = "testeExecution";
		
		ArrayList<RowItemTestes> testList = null;
		
		try {
		
		if (hasTestesExecution()) {
			testList = new ArrayList<RowItemTestes>();

			for (ITestResult testeExecution : TestesExecution) {
				RowItemTestes item = new RowItemTestes(
						testeExecution.get_test_id(),
						testeExecution.get_number_of_tests(),
						testeExecution.get_number_of_tests_done(),
						testeExecution.get_run_test_state(),
						testeExecution.get_task_list(),
						testeExecution.get_test_execution_location(),
						testeExecution.get_test_name(),
						testeExecution.get_test_state(),
						testeExecution.get_test_type(),
						Test_type(testeExecution.get_test_type()));
				Emcurso++;
				testList.add(item);

			}
		}
		
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return testList;

	}

	public ArrayList<RowItemTestes> getTestesExecution() {
		final String method = "getTestesExecution";
		
		try {
		if (testesExecution != null)
			/* Returns a copy of the testesexecution list */
			return new ArrayList<RowItemTestes>(testesExecution);
		else
			return new ArrayList<RowItemTestes>();
		
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}

	public ArrayList<RowItemTestes> getTestes() {
		final String method = "getTestes";

		try{
		if (testes != null)
			/* Returns a copy of the testes list */
			return new ArrayList<RowItemTestes>(testes);
		else
			return new ArrayList<RowItemTestes>();
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}

	public void close() {
		startActivity(ActivityDashboardMain.class);
		finish();

	}

	public int getFiltro() {
		return Emcurso;
	}

	public void setFiltro(int Filtro) {
		this.Filtro = Filtro;
		Log.i(TAG, "Filter: " + Filtro);
	}

	public int getEmcurso() {
		return Emcurso;
	}

	public void setEmcurso(int Emcurso) {
		this.Emcurso = Emcurso;
	}

	public int getInactivo() {
		return Inactivo;
	}

	public void setInactivo(int Inactivo) {
		this.Inactivo = Inactivo;
	}

	public int Test_type(ETestType state) {
		final String method = "Test_type";
		
		if (state == ETestType.SCHEDULED) {
			imagetipe = R.drawable.icon_agendado;
		} else {
			imagetipe = R.drawable.icon_on_demand_litle;
		}
		return imagetipe;

	}

	@Override
	public void changeFragment(int position) {
		final String method = "changeFragment";
		
		
		try{
		Bundle bundle = new Bundle();

		if (Filtro == 0) {
			if (position < Emcurso) {

				bundle.putString(ITEM_ID, TestesExecution.get(position)
						.get_test_id());

			} else {

				bundle.putString(ITEM_ID_2, Testes.get(position - Emcurso)
						.get_test_id());
			}
			Log.i(TAG, "positiontodos: " + position);
		}

		else if (Filtro == 1) {

			bundle.putString(ITEM_ID, TestesExecution.get(position)
					.get_test_id());
			Log.i(TAG, "position: " + position);

		}

		else if (Filtro == 2) {

			bundle.putString(ITEM_ID_2, Testes.get(position).get_test_id());
			Log.i(TAG, "positioninactivo: " + position);
		}

		FragmentTestesDetalhes f = new FragmentTestesDetalhes();
		f.setArguments(bundle);

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragment_testes, f).addToBackStack(null).commit();
		
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}

	public ITestResult getTestResult(String testId) {
		final String method = "getTestResult";
		
		ITestResult result = null;
		
		try {
		

			result = getTestResult(TestesExecution, testId);

		
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return result;
	}

	public ITest getTest(String testId) {
		final String method = "getTest";
		
		ITest result = null;
		
		try {
		

		if (Filtro == 0 || Filtro == 2) {

			result = getTest(Testes, testId);
		}
		
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return result;
	}

	private ITestResult getTestResult(List<ITestResult> list, String testId) {
		final String method = "getTestResult";
		
		ITestResult result = null;
		
		try {
		for (ITestResult test : list) {
			if (test.get_test_id().equals(testId)) {
				result = test;
				break;
			}
		}
		
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		return result;
	}

	private ITest getTest(List<ITest> list, String testId) {
		final String method = "getTest";
		
		ITest result = null;
		for (ITest test : list) {
			if (test.get_test_id().equals(testId)) {
				result = test;
				break;
			}
		}
		return result;
	}

	public void onBackPressed() {
		final String method = "onBackPressed";
		
		if (fragMgr == null)
			fragMgr = getSupportFragmentManager();

		if (fragMgr.getBackStackEntryCount() > 0) {
			fragMgr.popBackStack();
			overridePendingTransition(0, 0);
		} else {
			HomeP=home.ReadHome();
			if(HomeP==MenuOption.Testes){
				finish();
			}else{
			home.TesteTipe(HomeP);
			finish();
		
			super.onBackPressed();
		}}
	}

}
