package PTInov.IEX.ArQoSPocketService.CallService;

import android.util.Log;

public class MyTimer implements Runnable {
	
	private final String tag = "MyTimer";
	
	private long timeOutInMilSec;
	private MyTimerInterface objectRef;
	private int timerCode;

	public MyTimer(long timeInMilSec, MyTimerInterface pobjectRef, int TimerCoder) {
		timeOutInMilSec = timeInMilSec;
		objectRef = pobjectRef;
		timerCode = TimerCoder;
		
		Thread t = new Thread(this);
		t.start();
	}
	
	public synchronized void stopTime() {
		objectRef = null;
	}
	
	public synchronized MyTimerInterface getReference() {
		return objectRef;
	}

	public void run() {
		// TODO Auto-generated method stub
		
		try {
			
			Thread.sleep(timeOutInMilSec);
			
			// utiliza o metodo synchronized para travar a thread de lanchar o time
			// so o objecto estiver a fazer stop ao timer
			MyTimerInterface object = getReference();
			if (object!=null) objectRef.timeOut(timerCode);
			
		}catch(Exception e) {
			Log.v(tag, "run :: ERROR :"+e.toString());
		}
	}
	
}


