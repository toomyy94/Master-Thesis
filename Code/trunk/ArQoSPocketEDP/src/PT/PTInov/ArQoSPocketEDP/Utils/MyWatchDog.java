package PT.PTInov.ArQoSPocketEDP.Utils;

public class MyWatchDog  extends Thread{
	
	private final String tag = "MyWatchDog";

	private long sleepTimeSec = 0;
	
	private boolean cancelWatchDog = false;
	private WacthDogInterface ref = null;
	
	public MyWatchDog(long pSleepTime, WacthDogInterface wdi) {
		sleepTimeSec = pSleepTime;
		ref = wdi;
	}
	
	public void cancelWatchDog() {
		cancelWatchDog = true;
	}
	
	public void run() {
		try {
			
			Thread.sleep(sleepTimeSec);
			
			if (cancelWatchDog) return;
			
			ref.WacthDog();
			
		} catch(Exception ex) {
			Logger.v(tag, "run", LogType.Error, ex.toString());
		}
	}
}
