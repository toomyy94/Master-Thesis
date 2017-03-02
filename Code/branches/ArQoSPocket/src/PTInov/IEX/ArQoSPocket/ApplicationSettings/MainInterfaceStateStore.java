package PTInov.IEX.ArQoSPocket.ApplicationSettings;

import android.content.Context;
import android.content.SharedPreferences;

public class MainInterfaceStateStore {
	
	private static Context myContext = null;
	
	private final static String SharedPreferencesID = "MainInterfaceStateStore";
	
	private final static String EngineState = "MainInterfaceStateStore.EngineState";
	private final static String Button = "MainInterfaceStateStore.Button";
	private final static String LastTest = "MainInterfaceStateStore.LastTest";
	private final static String StateLastTest = "MainInterfaceStateStore.StateLastTest";
	private final static String NextTest = "MainInterfaceStateStore.NextTest";
	private final static String Errors = "MainInterfaceStateStore.Errors";
	private final static String ActualState = "MainInterfaceStateStore.ActualState";
	
	private static SharedPreferences prefs;
	 
	
	public MainInterfaceStateStore(Context c) {
		
		prefs = c.getSharedPreferences(SharedPreferencesID, Context.MODE_PRIVATE);
		
		myContext = c;
	}
	
	public static void saveMainInterfaceState(MainInterfaceState mis, boolean engineState) {
		
		SharedPreferences prefs = myContext.getSharedPreferences(SharedPreferencesID, Context.MODE_PRIVATE);
		
		prefs.edit().putBoolean(EngineState, engineState).commit();
		prefs.edit().putBoolean(Button, mis.getButton()).commit();
		prefs.edit().putString(LastTest, mis.getNextTest()).commit();
		prefs.edit().putInt(StateLastTest, mis.getStateLastTest()).commit();
		prefs.edit().putString(NextTest, mis.getNextTest()).commit();
		//prefs.edit().putLong(Errors, mis.getErrors());
		prefs.edit().putInt(ActualState, mis.getActualState()).commit();
		
	}
	
	public static MainInterfaceState getSavedMainInterfaceState() {
		
		SharedPreferences prefs = myContext.getSharedPreferences(SharedPreferencesID, Context.MODE_PRIVATE);
		
		boolean button = prefs.getBoolean(Button, prefs.getBoolean(EngineState, false));
		String lastTest = prefs.getString(LastTest, "NA");
		int stateLastTest = prefs.getInt(StateLastTest, 3);
		String nextTest = prefs.getString(NextTest, "NA");
		//long errors não gravo os erros
		int actualState = prefs.getInt(ActualState, 1);
		
		return new MainInterfaceState(button,lastTest, stateLastTest, nextTest, 0, actualState);
	}
	
	public static boolean getEngineState() {
		SharedPreferences prefs = myContext.getSharedPreferences(SharedPreferencesID, Context.MODE_PRIVATE);
		
		return prefs.getBoolean(EngineState, false);
	}
}
