package PTInov.IEX.ArQoSPocket.ApplicationSettings;

public class MainInterfaceState {
	
	private static String tag = "MainInterfaceState";
	
	private boolean button;
	private String lastTest;
	private int StateLastTest;
	private String NextTest;
	private long errors;
	private int ActualState;
	
	
	public MainInterfaceState(boolean pbutton, String plastTest, int pStateLastTest, String pNextTest, long perrors, int pActualState) {
		button = pbutton;
		lastTest = plastTest;
		StateLastTest = pStateLastTest;
		NextTest = pNextTest;
		errors = perrors;
		ActualState = pActualState;
	}
	
	public synchronized void setButton(boolean pbutton) {
		button = pbutton;
	}
	
	public synchronized void setLastTest(String plastTest) {
		lastTest = plastTest;
	}
	
	public synchronized void setStateLastTest(int pStateLastTest) {
		StateLastTest = pStateLastTest;
	}
	
	public synchronized void setNextTest(String pNextTest) {
		NextTest = pNextTest;
	}
	
	public synchronized void seterrors(long perrors) {
		errors = perrors;
	}
	
	public synchronized void setActualState(int pActualState) {
		ActualState = pActualState;
	}
	
	public synchronized boolean getButton() {
		return button;
	}
	
	public synchronized String getLastTest() {
		return lastTest;
	}
	
	public synchronized int getStateLastTest() {
		return StateLastTest;
	}
	
	public synchronized String getNextTest() {
		return NextTest;
	}
	
	public synchronized long getErrors() {
		return errors;
	}
	
	public synchronized int getActualState() {
		return ActualState;
	}
	
}
