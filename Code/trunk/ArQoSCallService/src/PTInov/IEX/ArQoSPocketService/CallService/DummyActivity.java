package PTInov.IEX.ArQoSPocketService.CallService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import PTInov.IEX.ArQoSPocket.ResultLogs.TestLogsResult;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class DummyActivity extends Activity {

	private final int myServerPort = 22331;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		/*
		// Create the object with the run() method
		Runnable runnable = new BasicThread2();

		// Create the thread supplying it with the runnable object
		Thread thread = new Thread(runnable);

		// Start the thread
		thread.start();
		*/

		// simulo o boot
		Intent i = new Intent();
		i.setAction("PTInov.IEX.ArQoSPocketService.StartAtBootService");
		startService(i);
	}

	class BasicThread2 implements Runnable {
		// This method is called when the thread runs
		public void run() {
			
			Log.v("DummyActivity", "Entrei na thread k tenta ligar!");
			
			// vou serializar o object
			ArrayList<String> params = new ArrayList<String>();
			params.add("normal");
			params.add("966894265");
			//TaskStruct ts = new TaskStruct("1","2","3",null,"5","6","7","8",params);
			
			Socket kkSocket;
			int trys = 5;
			while (trys > 0) {
				try {
					kkSocket = new Socket("localhost", myServerPort);

					ObjectOutputStream oos = new ObjectOutputStream(kkSocket.getOutputStream());
					//oos.writeObject(ts);					
					
					ObjectInputStream ois = new ObjectInputStream(kkSocket.getInputStream());
					TestLogsResult tlr = (TestLogsResult) ois.readObject();
					
					Log.v("DummyActivity", "Resultado :"+tlr.getErrorStatus());
					
					oos.close();
					ois.close();
					kkSocket.close();
					
					break;

				} catch (Exception e) {
					Log.v("DummyActivity", "Não consegui ligar ao socket!");
					trys--;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			
			Log.v("DummyActivity", "Sai da thread k tenta ligar!");
		}
	}

}
