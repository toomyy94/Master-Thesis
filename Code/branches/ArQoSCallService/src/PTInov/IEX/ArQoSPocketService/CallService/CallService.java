package PTInov.IEX.ArQoSPocketService.CallService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.os.IBinder;
import android.util.Log;

import android.telephony.TelephonyManager;
import com.android.internal.telephony.ITelephony;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.IccUtils;
import com.android.internal.telephony.Call;
import android.os.PowerManager;
import android.content.Context;
import android.os.Handler;
import android.os.AsyncResult;
import android.provider.Settings;

import java.io.*;


public class CallService extends Service {
	
	private final String tag = "#### CallService ####";
	private final int myServerPortEvent = 22331;
	private final int myServerPortTest = 22332;

	private static final int DISCONNECT_PHONE_EVENT = 2;
	private static final int CALLSTATECHANGE_PHONE_EVENT = 3;
	private static final int RINGBACKTONE_PHONE_EVENT = 4;
	
	public final static String CALLSTATECHANGE_DIALING = "CALLSTATECHANGE_PHONE_EVENT:DIALING";
	public final static String CALLSTATECHANGE_ALERTING = "CALLSTATECHANGE_PHONE_EVENT:ALERTING";
	public final static String CALLSTATECHANGE_ACTIVE = "CALLSTATECHANGE_PHONE_EVENT:ACTIVE";
	public final static String CALLSTATECHANGE_DISCONNECTING = "CALLSTATECHANGE_PHONE_EVENT:DISCONNECTING";
	public final static String CALLSTATECHANGE_RINGING = "CALLSTATECHANGE_PHONE_EVENT:RINGING";
	public final static String CALLSTATECHANGE_IDLE = "CALLSTATECHANGE_PHONE_EVENT:IDLE";
	
	public final static String DISCONNECTPHONE_ERRORUNSPECIFIED = "DISCONNECT_PHONE_EVENT:ERROR_UNSPECIFIED";
	public final static String DISCONNECTPHONE_NORMAL = "DISCONNECT_PHONE_EVENT:NORMAL";
	public final static String DISCONNECTPHONE_LOCAL = "DISCONNECT_PHONE_EVENT:LOCAL";
	public final static String DISCONNECTPHONE_OUTOFSERVICE = "DISCONNECT_PHONE_EVENT:OUT_OF_SERVICE";
	public final static String DISCONNECTPHONE_INCOMINGMISSED = "DISCONNECT_PHONE_EVENT:INCOMING_MISSED";
	public final static String DISCONNECTPHONE_BUSY = "DISCONNECT_PHONE_EVENT:BUSY";
	
	
	//private ServerSocket serverSocketEvent = null;
	//private Socket clientSocketEvent = null;
	//private BufferedReader readbufferEvent = null;
	//private PrintWriter writeBufferEvent = null;
	
	
	private ServerSocket serverSocketTest = null;
	private Socket clientSocketTest = null;
	private BufferedReader readbufferTest = null;
	private PrintWriter writeBufferTest = null;
	private MyTimer TaskCodeTimer = null;
	private CallService myRef = null;
	private String status = null;
	
	private boolean inTest = false;
	private Connection connection = null;
	
	private TelephonyManager manager;
	private Phone phone = null;
	private TelephonyInfo telephonyinfo;
	private ITelephony telephonyinterface;
	
	private AudioManager audioManager;
	private int beginVolume = -1;

	private PowerManager pm;
	private PowerManager.WakeLock wl;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		Log.d(tag, "onCreate :: Service Started......................................................................................................................................................................................................................................................");

		// Because when the phone is idle, we can't create new calls, then we
		// need to lock the phone state
		pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "CallServiceLock");
		wl.acquire();
		
		super.onCreate(); 

		LoadTelephonyAPI();
		
		//Inicializa o socket na porta predefinida
		try {
			serverSocketTest  = new ServerSocket(myServerPortTest);
			
			inTest = false;
			myRef = this;
			
			// Arranca a thread de comunicação com o cliente do serviço
			listenNewTask();
		} catch(Exception e) {
			//tenta se ligar em outra porta.....................................
			Log.v(tag, "onCreate() :: ERROR :"+e.toString());
		}		
		
	}
	
	private void LoadTelephonyAPI() {
		// Get Telephony APIs
		this.manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		this.phone = PhoneFactory.getDefaultPhone();
		this.telephonyinfo = new TelephonyInfo(manager, this.phone);
		
		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

		this.phone.registerForDisconnect(handler, DISCONNECT_PHONE_EVENT, null);
		this.phone.registerForRingbackTone(handler, RINGBACKTONE_PHONE_EVENT,null);
		this.phone.registerForPreciseCallStateChanged(handler, CALLSTATECHANGE_PHONE_EVENT, null);
	}
	

	private void listenNewTask() {
		TimerTask waittask = new waitTask();
		Timer timer = new Timer();
		
		timer.schedule(waittask, 0);
	}

	public boolean doCall(String number) {
		try {
			if (phone.getState() == Phone.State.IDLE) {
				Log.d(tag, "doCall :: Do call!");
	
				connection = phone.dial(number);
				return true;
			} else {
				Log.d(tag, "doCall :: phone getState isn't in IDLE state!");

				return false;
			}
		} catch (Exception e) {
			Log.d(tag, "doCall :: ERROR :"+e.toString());
		}

		return false;
	}
	
	public boolean hangupCall() {
		try {
			// nota: estou a usar o apontador para a ligação, se for criada uma nova ligação é essa k vai a baixo...............
			connection.hangup();
			return true;
		} catch (Exception e) {
			Log.d(tag, "hangupCall :: ERROR :"+e.toString());
		}
		
		return false;
	}

	
	public class waitTask extends TimerTask {

		@Override
		public void run() {
			Log.d(tag, "waitTask :: Task Started..........................................................................................................................................................................................................");
			
			try {
			
				int defaultTimeOut = 5000; // 5seg para o cliente fazer o pedido de teste
				
				clientSocketTest = serverSocketTest.accept();
				clientSocketTest.setSoTimeout(defaultTimeOut);
				
				readbufferTest = new BufferedReader(new InputStreamReader(clientSocketTest.getInputStream()));
				writeBufferTest = new PrintWriter(clientSocketTest.getOutputStream(), true);
				
				// Recebi o pedido de chamada
				String test = readbufferTest.readLine();
				
				String testNumber = test.substring(0, test.indexOf(":"));
				Log.v(test, "Call to:"+testNumber+"|");
				String timeOut = test.substring(test.indexOf(":")+1,test.indexOf(":", test.indexOf(":")+1));
				Log.v(tag, "TimeOut :"+timeOut);
				String timeOutCall = test.substring(test.indexOf(":", test.indexOf(":")+1)+1,test.length());
				Log.v(timeOut, "TimeOutCall :"+timeOutCall);
				
				// timeout call
				int callTimeOut = Integer.parseInt(timeOutCall)*1000;
				Log.v(timeOut, "Ja tenho o timeOut em int :"+callTimeOut);
				
				// inicia o timeOut
				int timeOutSocket = Integer.parseInt(timeOut); 
				Log.v(tag, "passou para integer!");
				
				inTest = true;
				//phone.setMute(true);
				
				// inicia o teste
				doCall(testNumber);
				Log.v(tag, "fiz a chamada");
				
				
				// TimeOut tarefa, se não receber dentro deste timeout, reinicia o socket
				clientSocketTest.setSoTimeout(timeOutSocket);
				
				// espera pelo parametro de exit
				String msg = readbufferTest.readLine();
				inTest = false;
				
				readbufferTest.close();
				writeBufferTest.close();
				clientSocketTest.close();
				
				// espera os n segundos ate desligar a chamada
				waitHangUp(callTimeOut);
				
				
			} catch(Exception ex) {
				Log.v(tag, "Readling task error :"+ex.toString());
				
				inTest = false;
				//phone.setMute(false);
				
				//timeout socket, deitar agora a ligação a baixo
				hangupCall();
				
				//Log.v(tag, "Vou ligar o audio da chamada!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				//audioManager.setStreamMute(AudioManager.STREAM_VOICE_CALL, false);
			}

			//phone.setMute(false);
			//Log.v(tag, "Vou ligar o audio da chamada!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			//audioManager.setStreamMute(AudioManager.STREAM_VOICE_CALL, false);
			
			//audioManager.adjustStreamVolume(AudioManager.MODE_NORMAL, AudioManager.ADJUST_SAME, AudioManager.FLAG_SHOW_UI);
			
			listenNewTask();
		}

	}
	
	private void waitHangUp(int milsecs) {
		TimerTask hanguptask = new hangUpTask();
		Timer timer = new Timer();
		
		timer.schedule(hanguptask, milsecs);
	}
	
	public class hangUpTask extends TimerTask {

		@Override
		public void run() {
			Log.v(tag, "Estou no timer k vai desligar a chamada");
			hangupCall();
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		wl.release();
		// mNM.cancel(NOTIFICATION);
		super.onDestroy();
	}

	/*
	private void WriteLog(String slog) {
		try {
			FileWriter fstream = new FileWriter("/sdcard/Log.txt",true);
  			BufferedWriter out = new BufferedWriter(fstream);
  			out.write(tag+"::"+slog+"\n");
  
  			out.close();
			fstream.close();
		} catch(Exception e) {
		}
	}*/

	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			AsyncResult result = (AsyncResult) msg.obj;
			Log.v(tag, "Received Message on handler...............................");
			
			switch (msg.what) {
				case DISCONNECT_PHONE_EVENT:
					Log.v(tag, "DISCONNECT_PHONE_EVENT");

					Connection connection = (Connection) result.result;
					Connection.DisconnectCause cause = connection.getDisconnectCause();
					phone.clearDisconnected();
				
					switch (cause) {
						case POWER_OFF:
							Log.v(tag, "POWER_OFF: " + cause.toString());
							break;
						case LOCAL:
							Log.v(tag, "LOCAL: " + cause.toString());
							
							// Chamada estabelecida correctamente e desligada por mim
							
							
							if (inTest) {
								try {
									//PrintWriter writeBuffer = new PrintWriter(clientSocket.getOutputStream(), true);
									String response = CallService.DISCONNECTPHONE_LOCAL;
									
									writeBufferTest.println(response);
							
									Log.v(tag, "Enviei a resposta :"+response);
							
								} catch(Exception e) {
									Log.v(tag, "DISCONNECT_PHONE_EVENT ERROR :"+e.toString());
								}
							} else {
								sendEvent(CallService.DISCONNECTPHONE_LOCAL);
							}
							
							break;
						case NORMAL:
							Log.v(tag, "NORMAL: " + cause.toString());
							
							// Chamada estabelecida correctamente e desligada por o outro lado
							
							
							if (inTest) {
								try {
									//PrintWriter writeBuffer = new PrintWriter(clientSocket.getOutputStream(), true);
									String response = CallService.DISCONNECTPHONE_NORMAL;
									writeBufferTest.println(response);
							
									Log.v(tag, "Enviei a resposta :"+response);
							
								} catch(Exception e) {
									Log.v(tag, "DISCONNECT_PHONE_EVENT ERROR :"+e.toString());
								}
							} else {
								sendEvent(CallService.DISCONNECTPHONE_NORMAL);
							}
							
							break;
						case INCOMING_MISSED:
							Log.v(tag, "INCOMING_MISSED: " + cause.toString());


							if (inTest) {
								try {
									//PrintWriter writeBuffer = new PrintWriter(clientSocket.getOutputStream(), true);
									String response = CallService.DISCONNECTPHONE_INCOMINGMISSED;
									writeBufferTest.println(response);
							
									Log.v(tag, "Enviei a resposta :"+response);
							
								} catch(Exception e) {
									Log.v(tag, "DISCONNECT_PHONE_EVENT ERROR :"+e.toString());
								}
							} else {
								sendEvent(CallService.DISCONNECTPHONE_INCOMINGMISSED);
							}
							
							break;
						default:
							Log.v(tag, "default: " + cause.toString());
							
							String response = null;
							
							//OUT_OF_SERVICE - telefone desligado ou sem registo na rede
							if (cause.toString().contains("OUT_OF_SERVICE"))  {
								response = CallService.DISCONNECTPHONE_OUTOFSERVICE;
							} else if (cause.toString().contains("BUSY")) 
								response = CallService.DISCONNECTPHONE_BUSY;
							else {
								//ERROR_UNSPECIFIED
								response = CallService.DISCONNECTPHONE_ERRORUNSPECIFIED;								
							}

							if (inTest) {
								
								try {
									//PrintWriter writeBuffer = new PrintWriter(clientSocket.getOutputStream(), true);
									writeBufferTest.println(response);
							
									Log.v(tag, "Enviei a resposta :"+response);
							
								} catch(Exception e) {
									Log.v(tag, "DISCONNECT_PHONE_EVENT ERROR :"+e.toString());
								}
							} else {			
							
								sendEvent(response);
							}
							
							break;
					}
	
	
					break;
				case CALLSTATECHANGE_PHONE_EVENT:
					Log.v(tag, "CALLSTATECHANGE_PHONE_EVENT");
	
					Phone.State state = phone.getState();
					Call call = phone.getForegroundCall();
					Log.v(tag, "Call State: " + call.getState().toString());
	
					Log.v(tag, "Phone State: " + state.toString());
	
					//phone.setMute(true);

					Connection con = call.getEarliestConnection();
					if (con != null) {
						Log.d(tag, "Post Dial State: " + con.getPostDialState().toString());
					}
					
					if (call.getState().toString().equals("DIALING")) {
						status = CallService.CALLSTATECHANGE_DIALING;
					} else if (call.getState().toString().equals("ALERTING")) {
						status = CallService.CALLSTATECHANGE_ALERTING;
					} else if (call.getState().toString().equals("ACTIVE")) {
						status = CallService.CALLSTATECHANGE_ACTIVE;
					} else if (call.getState().toString().equals("DISCONNECTING")) {
						status = CallService.CALLSTATECHANGE_DISCONNECTING;
					} else if (call.getState().toString().equals("IDLE") && state.toString().equals("RINGING")) {
						status = CallService.CALLSTATECHANGE_RINGING;
					}else if (call.getState().toString().equals("IDLE")) {
						status = CallService.CALLSTATECHANGE_IDLE;
						//inTest = false;
					} else {
						status = call.getState().toString();
					}
					
					
					
					if (inTest) {
						try {
							//PrintWriter writeBuffer = new PrintWriter(clientSocket.getOutputStream(), true);
							writeBufferTest.println(status);
					
							Log.v(tag, "Enviei a resposta :"+status);
							
							//audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
							//audioManager.adjustStreamVolume(AudioManager.STREAM_VOICE_CALL, AudioManager.ADJUST_LOWER, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
							//audioManager.adjustStreamVolume(AudioManager.MODE_IN_CALL, AudioManager.ADJUST_LOWER, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
							
							//audioManager.setStreamMute(AudioManager.STREAM_VOICE_CALL, true);
							//audioManager.setStreamMute(AudioManager.MODE_IN_CALL, true);
							
							if (status.equals(CallService.CALLSTATECHANGE_ALERTING) && beginVolume == -1) {
							
									audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
								
									beginVolume = Settings.System.getInt(getContentResolver(),  Settings.System.VOLUME_VOICE);
							
									Log.v(tag, "Vou alterar o volume durante a chamada....................................!!!!!!!!!!! :"+beginVolume);
									for (int i=beginVolume;i>0;i--) {
										Log.v(tag, "alterei menos..");
										audioManager.adjustStreamVolume(AudioManager.STREAM_VOICE_CALL, AudioManager.ADJUST_LOWER, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
										//audioManager.adjustStreamVolume(AudioManager.MODE_IN_CALL, AudioManager.ADJUST_LOWER, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
									}
							}
		
					
						} catch(Exception e) {
							Log.v(tag, "DISCONNECT_PHONE_EVENT ERROR :"+e.toString());
						}
					}
				
					if (!inTest) {
						sendEvent(status);
						
						if (status.equals(CallService.CALLSTATECHANGE_IDLE) && beginVolume!=-1) {
							
							audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
							
							for (int i=0;i<beginVolume;i++) {
								Log.v(tag, "alterei menos..");
								audioManager.adjustStreamVolume(AudioManager.STREAM_VOICE_CALL, AudioManager.ADJUST_RAISE, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
								//audioManager.adjustStreamVolume(AudioManager.MODE_IN_CALL, AudioManager.ADJUST_RAISE, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
							}
							
							beginVolume = -1;
						}
					}
	
					break;
				case RINGBACKTONE_PHONE_EVENT:
					Log.v(tag, "RINGBACKTONE_PHONE_EVENT");
					break;
				
				default: 
					Log.v(tag, "Default Case!");
			}

			Log.v(tag, "........................................................");

		};
	};
	
	
	public void sendEvent(String s) {
		
		try {
			Socket clientSocketTest = new Socket("localhost", myServerPortEvent);
			PrintWriter writeBufferTest = new PrintWriter(clientSocketTest.getOutputStream(), true);
			
			writeBufferTest.println(s);
			
			writeBufferTest.close();
			clientSocketTest.close();
			
		} catch(Exception ex) {
			Log.v(tag,"sendEvent :: ERROR :"+ex.toString());
		}
		
	}
	
	public void tryCloseClienteSocketAndRunAgain() {
		
		inTest = false;
		
		try {
			//elimina a ligação
			readbufferTest.close();
			writeBufferTest.close();
			clientSocketTest.close();
			
			clientSocketTest = null;
			readbufferTest = null;
			writeBufferTest = null;
			
		} catch(Exception ex) {
			Log.v(tag, "tryCloseClienteSocketAndRunAgain :: ERROR :"+ex.toString());
		}

		//arranca novamente o socket
		//listenNewTask();
	}

}

