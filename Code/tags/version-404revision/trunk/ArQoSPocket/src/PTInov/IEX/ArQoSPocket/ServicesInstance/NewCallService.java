package PTInov.IEX.ArQoSPocket.ServicesInstance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import PTInov.IEX.ArQoSPocket.ParserFileTasks.TaskStruct;
import PTInov.IEX.ArQoSPocket.ResultLogs.TestLogsResult;
import PTInov.IEX.ArQoSPocket.ServicesInterfaces.EngineInterface;
import PTInov.IEX.ArQoSPocket.ServicesInterfaces.MyTimerInterface;
import PTInov.IEX.ArQoSPocket.ServicesInterfaces.ServiceInterface;
import PTInov.IEX.ArQoSPocket.ServicesInterfaces.TaskInterface;
import android.content.Intent;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;

public class NewCallService implements ServiceInterface, Runnable {
	
	// Numero de teste
	// 3g - 967909837
	// 2g - 967909767
	
	private final static String tag = "NewCallService";
	
	private final int MyEventServerPort = 22331;
	private final int servicePortTest = 22332;

	
	private EngineInterface engineInterfaceCallback;
	
	public final static int timeOutConnectionCode = 1111;
	public final static int timeOutTaskCode = 1122;
	public final static int timeOutCallCode = 1133;	
	
	private ServerSocket serverSocketEvent;
	
	public NewCallService(EngineInterface ie) {
		
		Log.v(tag, "NewCallService :: Creat a instance of NewCallService");
		
		engineInterfaceCallback = ie;
		
		try {
			serverSocketEvent  = new ServerSocket(MyEventServerPort);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.v(tag, "serverSocketEvent :: ERROR :"+e.toString());
		}
		
		//Thread myThread = new Thread(this);
		//myThread.start();
		
		listenNewEvent();
	}
	
	private void listenNewEvent() {
		TimerTask waitevent = new waitEvent();
		Timer timer = new Timer();
		
		timer.schedule(waitevent, 0);
	}
	
	
	public class waitEvent extends TimerTask {

		@Override
		public void run() {
			try {
				
				Socket clientSocketEvent = serverSocketEvent.accept();
				clientSocketEvent.setSoTimeout(5000); // tem 5 seg para enviar o event
				
				BufferedReader readbufferEvent = new BufferedReader(new InputStreamReader(clientSocketEvent.getInputStream()));
				
				Log.v(tag, "RUN :: wait received");
				
				// Espero uma msg do serviço
				String serviceMSG = readbufferEvent.readLine();
				
				Log.v(tag, "RUN :: Event received");
					
				//Evento a avisar o engine do estado do telefone do android
					
				if (serviceMSG.equals(CallEventStateMachine.CALLSTATECHANGE_DIALING)) {
						
					// devolver o engine que o utilizador iniciou uma chamada
					engineInterfaceCallback.initCallEvent();
						
				} else if (serviceMSG.equals(CallEventStateMachine.CALLSTATECHANGE_ACTIVE)) {
						
					// devolver o engine que o utilizador iniciou uma chamada
					engineInterfaceCallback.initCallEvent();
						
				}else if (serviceMSG.equals(CallEventStateMachine.CALLSTATECHANGE_RINGING)) {	
						
					// devolver o engine que o utilizador iniciou uma chamada
					engineInterfaceCallback.initCallEvent();
						
				}else if (serviceMSG.equals(CallEventStateMachine.DISCONNECTPHONE_ERRORUNSPECIFIED)) {
						
					// devolver o engine que o utilizador terminou uma chamada
					engineInterfaceCallback.hungUpCallEvent();
						
				} else if (serviceMSG.equals(CallEventStateMachine.DISCONNECTPHONE_ERRORUNSPECIFIED)) {
						
					// devolver o engine que o utilizador terminou uma chamada
					engineInterfaceCallback.hungUpCallEvent();
						
				} else if (serviceMSG.equals(CallEventStateMachine.DISCONNECTPHONE_LOCAL)) {
						
					// devolver o engine que o utilizador terminou uma chamada
					engineInterfaceCallback.hungUpCallEvent();
						
				} else if (serviceMSG.equals(CallEventStateMachine.DISCONNECTPHONE_NORMAL)) {
						
					// devolver o engine que o utilizador terminou uma chamada
					engineInterfaceCallback.hungUpCallEvent();
						
				} else if (serviceMSG.equals(CallEventStateMachine.DISCONNECTPHONE_INCOMINGMISSED)) {
						
					// devolver o engine que o utilizador terminou uma chamada
					engineInterfaceCallback.hungUpCallEvent();
				}

				readbufferEvent.close();
				clientSocketEvent.close();
				clientSocketEvent = null;
				readbufferEvent = null;
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.v(tag, "run :: ERROR :"+e.toString());
			}
			
			listenNewEvent();
		}
	}
	
	
	public void run() {
		// TODO Auto-generated method stub
		try {
			
			Socket clientSocketEvent = serverSocketEvent.accept();
			clientSocketEvent.setSoTimeout(5000); // tem 5 seg para enviar o event
			
			BufferedReader readbufferEvent = new BufferedReader(new InputStreamReader(clientSocketEvent.getInputStream()));
			
			Log.v(tag, "RUN :: wait received");
			
			// Espero uma msg do serviço
			String serviceMSG = readbufferEvent.readLine();
			
			Log.v(tag, "RUN :: Event received");
				
			//Evento a avisar o engine do estado do telefone do android
				
			if (serviceMSG.equals(CallEventStateMachine.CALLSTATECHANGE_DIALING)) {
					
				// devolver o engine que o utilizador iniciou uma chamada
				engineInterfaceCallback.initCallEvent();
					
			} else if (serviceMSG.equals(CallEventStateMachine.CALLSTATECHANGE_ACTIVE)) {
					
				// devolver o engine que o utilizador iniciou uma chamada
				engineInterfaceCallback.initCallEvent();
					
			}else if (serviceMSG.equals(CallEventStateMachine.CALLSTATECHANGE_RINGING)) {	
					
				// devolver o engine que o utilizador iniciou uma chamada
				engineInterfaceCallback.initCallEvent();
					
			}else if (serviceMSG.equals(CallEventStateMachine.DISCONNECTPHONE_ERRORUNSPECIFIED)) {
					
				// devolver o engine que o utilizador terminou uma chamada
				engineInterfaceCallback.hungUpCallEvent();
					
			} else if (serviceMSG.equals(CallEventStateMachine.DISCONNECTPHONE_ERRORUNSPECIFIED)) {
					
				// devolver o engine que o utilizador terminou uma chamada
				engineInterfaceCallback.hungUpCallEvent();
					
			} else if (serviceMSG.equals(CallEventStateMachine.DISCONNECTPHONE_LOCAL)) {
					
				// devolver o engine que o utilizador terminou uma chamada
				engineInterfaceCallback.hungUpCallEvent();
					
			} else if (serviceMSG.equals(CallEventStateMachine.DISCONNECTPHONE_NORMAL)) {
					
				// devolver o engine que o utilizador terminou uma chamada
				engineInterfaceCallback.hungUpCallEvent();
					
			} else if (serviceMSG.equals(CallEventStateMachine.DISCONNECTPHONE_INCOMINGMISSED)) {
					
				// devolver o engine que o utilizador terminou uma chamada
				engineInterfaceCallback.hungUpCallEvent();
			}

			readbufferEvent.close();
			clientSocketEvent.close();
			clientSocketEvent = null;
			readbufferEvent = null;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.v(tag, "run :: ERROR :"+e.toString());
		}
		
		// tenho de meter aki um tasktimer, pk não está a libertar a thread
		run();
	}

	
	public TestLogsResult runService(TaskInterface ti) {
		
		String pIDMacro = ti.getIDmacro(); 
		String ptaskNumber = ti.getTaskNumber();
		String pdataInicio =  getResultDataFormat(new Date());
		//String pdataFim
		String ptaskID = ti.getTaskId();
		String pICCID = ti.getICCID();
		String pInfoCelula = "NA";
		String pinfoGPS = "NA";
		//String perrorStatus
		ArrayList<String> pparams = new ArrayList<String>();
		
		String statusMsg = "NOK: 3500 Não recebeu resposta do Módulo";
		
		boolean statusMsgChange = false;
		int state = 0; // 0 - idle, 1 - dialing, 2 alerting
		
		try {
			
			int timeout = Integer.parseInt(ti.getTimeOut())*1000;
			Log.v(tag,"Task timeout :"+timeout);
			
			// calcula o tempo final da tarefa
			Date endTask = new Date();
			endTask.setTime(endTask.getTime()+timeout);
			
			// estabelece ligação com o serviço
			Socket clientSocketTest = new Socket("localhost", servicePortTest);
			int actualTimeOut = (int) timeOutRefEndTask(endTask);			
			clientSocketTest.setSoTimeout(actualTimeOut);
			
			if (clientSocketTest == null) {
				// connection fail
				return new TestLogsResult(pIDMacro,ptaskNumber,pdataInicio,getResultDataFormat(new Date()),ptaskID,pICCID,pInfoCelula,pinfoGPS,"NOK: 47 Rede: Recurso indisponível",pparams);
			}
			
			BufferedReader readbufferTest = new BufferedReader(new InputStreamReader(clientSocketTest.getInputStream()));
			PrintWriter writeBufferTest = new PrintWriter(clientSocketTest.getOutputStream(), true);
			
			if (readbufferTest == null || writeBufferTest == null) {
				// connection fail
				return new TestLogsResult(pIDMacro,ptaskNumber,pdataInicio,getResultDataFormat(new Date()),ptaskID,pICCID,pInfoCelula,pinfoGPS,"NOK: Buffer Connection Problem",pparams);
			}
			
			// enviar o numero a efectuar a chamada
			String numberToCall = ti.getparamList().get(1);
			Log.v(ptaskID, "runService :: call number :"+numberToCall);
			String callTimeOut = ti.getparamList().get(16);
			writeBufferTest.println(numberToCall+":"+timeout+":"+callTimeOut);
			
			boolean sair = false;
			String response = null;
			
			actualTimeOut = (int) timeOutRefEndTask(endTask);
			while(!sair) {
				
				clientSocketTest.setSoTimeout(actualTimeOut);
				
				response = readbufferTest.readLine();
				
				if (response.equals(CallEventStateMachine.CALLSTATECHANGE_DIALING)){
					// erro a associar a este estado
					state = 1;
				} else if (response.equals(CallEventStateMachine.CALLSTATECHANGE_ALERTING)) {
					// erro a associar a este estado
					state = 2;
				} else if (response.equals(CallEventStateMachine.CALLSTATECHANGE_ACTIVE)) {
					// Chamada já activa
					statusMsg = "OK:";
					statusMsgChange = true;
					sair = true;
				} else if (response.equals(CallEventStateMachine.DISCONNECTPHONE_NORMAL)) {
					// chamada desligada por outro cliente
					statusMsg = "NOK: 3027 Não Atendida ou Rejeitada";
					statusMsgChange = true;
					sair = true;
				} else if (response.equals(CallEventStateMachine.DISCONNECTPHONE_LOCAL)) {
					// chamda desligada por mim
					statusMsg = "OK: LOCAL";
					statusMsgChange = true;
					sair = true;
				} else if (response.equals(CallEventStateMachine.DISCONNECTPHONE_OUTOFSERVICE)) {
					// sem serviço
					statusMsg = "NOK: 3521 Sem Serviço";
					statusMsgChange = true;
					sair = true;
				} else if (response.equals(CallEventStateMachine.DISCONNECTPHONE_ERRORUNSPECIFIED)) {
					if (state == 2) {
						statusMsg = "NOK: 3027 Não Atendida ou Rejeitada";
						statusMsgChange = true;
						sair = true;
					}
				} else if (response.equals(CallEventStateMachine.DISCONNECTPHONE_BUSY)) {
					// destino ocupado
					statusMsg = "NOK: 254 Ocupado";
					statusMsgChange = true;
					sair = true;
				}
				
				actualTimeOut = (int) timeOutRefEndTask(endTask);
				if (actualTimeOut<0) sair = true;
			}
			
			if (!statusMsgChange) { // se nao foi alterada
				switch(state) {
				case 1:
					statusMsg = "NOK: 255 Sem Marcação";
					break;
				case 2:
					break;
				}
			}
			
			
			//Send Exit
			writeBufferTest.println("EXIT");
			
			readbufferTest.close();
			writeBufferTest.close();
			clientSocketTest.close();
			clientSocketTest = null;
			writeBufferTest = null;
			readbufferTest = null;
			
		} catch(Exception ex) {
			Log.v(tag, "runService :: ERROR: "+ex.toString());
		}
		
		if (!statusMsgChange) { // se nao foi alterada
			switch(state) {
			case 1:
				break;
			case 2:
				statusMsg = "NOK: 3029 Destino não Atendeu";
				break;
			}
		}
		
		// resposta de erro
		return new TestLogsResult(pIDMacro,ptaskNumber,pdataInicio,getResultDataFormat(new Date()),ptaskID,pICCID,pInfoCelula,pinfoGPS,statusMsg,pparams);
	}
	
	
	
	public long timeOutRefEndTask(Date endTask) {
		Date nowDate = new Date();
		return endTask.getTime()- nowDate.getTime();
	}
	
	private String getResultDataFormat(Date date) {
		// 2011-08-03T10:41:14.000Z
		return (date.getYear()+1900)+"-"+(date.getMonth()+1)+"-"+date.getDate()+"T"+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds()+".000Z";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	public TestLogsResult runService(TaskInterface ti) {
		
		String pIDMacro = ti.getIDmacro(); 
		String ptaskNumber = ti.getTaskNumber();
		String pdataInicio =  getResultDataFormat(new Date());
		//String pdataFim
		String ptaskID = ti.getTaskId();
		String pICCID = ti.getICCID();
		String pInfoCelula = "NA";
		String pinfoGPS = "NA";
		//String perrorStatus
		ArrayList<String> pparams = new ArrayList<String>();
		
		try {
			
			int timeout = Integer.parseInt(ti.getTimeOut())*1000;
			Log.v(tag,"Task timeout :"+timeout);
			
			// estabelece ligação com o serviço
			Socket clientSocketTest = new Socket("localhost", servicePortTest);
			clientSocketTest.setSoTimeout(timeout);
			
			if (clientSocketTest == null) {
				// connection fail
				return new TestLogsResult(pIDMacro,ptaskNumber,pdataInicio,getResultDataFormat(new Date()),ptaskID,pICCID,pInfoCelula,pinfoGPS,"NOK: Application Connection Problem",pparams);
			}
			
			BufferedReader readbufferTest = new BufferedReader(new InputStreamReader(clientSocketTest.getInputStream()));
			PrintWriter writeBufferTest = new PrintWriter(clientSocketTest.getOutputStream(), true);
			
			if (readbufferTest == null || writeBufferTest == null) {
				// connection fail
				return new TestLogsResult(pIDMacro,ptaskNumber,pdataInicio,getResultDataFormat(new Date()),ptaskID,pICCID,pInfoCelula,pinfoGPS,"NOK: Buffer Connection Problem",pparams);
			}
			
			// enviar o numero a efectuar a chamada
			String numberToCall = ti.getparamList().get(1);
			Log.v(ptaskID, "runService :: call number :"+numberToCall);
			writeBufferTest.println(numberToCall+":"+timeout);
			
			// corre a thread para tentar obter a resposta
			String response = readbufferTest.readLine();
			
			//Send Exit
			writeBufferTest.println("EXIT");
			
			readbufferTest.close();
			writeBufferTest.close();
			clientSocketTest.close();
			
			return new TestLogsResult(pIDMacro,ptaskNumber,pdataInicio,getResultDataFormat(new Date()),ptaskID,pICCID,pInfoCelula,pinfoGPS,response,pparams);
		
			
		} catch(Exception e) {
			Log.v(ptaskID, "runService :: ERROR :"+e.toString());
		}
		
		// resposta de erro
		return new TestLogsResult(pIDMacro,ptaskNumber,pdataInicio,getResultDataFormat(new Date()),ptaskID,pICCID,pInfoCelula,pinfoGPS,"NOK: Service TimeOut",pparams);
		
	}*/
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	private final int servicePortEvent = 22331;
	private final int servicePortTest = 22332;

	private EngineInterface engineInterfaceCallback;
	
	// Lista de eventos possiveis a mandar ao player
	public final static int InitCallEvent = 1;
	public final static int HungUpCall = 2;
	
	
	public final static int timeOutConnectionCode = 1111;
	public final static int timeOutTaskCode = 1122;
	public final static int timeOutCallCode = 1133;
	
	
	private Socket clientSocketEvent = null;
	private BufferedReader readbufferEvent = null;
	private boolean connectionEventRuning;
	
	private Socket clientSocketTest = null;
	private BufferedReader readbufferTest = null;
	private PrintWriter writeBufferTest = null;
	
	
	private boolean activeTimeOut = false;
	
	
	public NewCallService(EngineInterface ie) {
		
		Log.v(tag, "NewCallService :: Creat a instance of NewCallService");
		
		engineInterfaceCallback = ie;
		
		connectionEventRuning = false;
		
		Thread myThread = new Thread(this);
		myThread.start();
	}
	
	
	private boolean initSocket() {
		
		boolean socketOpened = false;
		
		// Vou me tentar ligar o serviço, faço 10 tentativas em caso de falha
		int trys = 10;
		boolean repetir = false;;
		do {
			try {
				clientSocketEvent = new Socket("localhost", servicePortEvent);
				repetir = false;
				socketOpened = true;
			} catch (Exception e) {
				Log.v(tag, "Error open socket:"+e.toString());
				if (--trys>0) repetir = true;
				
				try {
					Thread.sleep(200);
				}catch(Exception ee) {
					
				}
			}
		} while (repetir);
		
		return socketOpened;
	}
	
	private boolean initBuffers() {
		
		boolean bufferReady = false;
		
		// Vou inicializar os buffer de comunicação, faço 10 tentativas em caso de falha
		int trys = 10;
		boolean repetir = false;;
		do {
			try {
				readbufferEvent = new BufferedReader(new InputStreamReader(clientSocketEvent.getInputStream()));
				repetir = false;
				bufferReady = true;
			} catch (Exception e) {
				Log.v(tag, "Error open buffers:"+e.toString());
				if (--trys>0) repetir = true;
				
				try {
					Thread.sleep(200);
				}catch(Exception ee) {
					
				}
			}
		} while (repetir);
		
		return bufferReady;
	}
	
	
	
	public void run() {
		
		Log.v(tag, "RUN :: In");
		
		// Thread onde vou receber os eventos
		
		// Teste se o serviço já foi arrancado, isto é,
		// se a ligação socket já está estabelecidade com o serviço
		if (!connectionEventRuning) {
			
			if (initSocket() && initBuffers()) {
				connectionEventRuning = true;
				Log.v(tag, "RUN :: Connection Estanlished with sucess!");
			} else {
				Log.v(tag, "RUN :: ############ Connection with CallService have problems! ############");
				// Avisar o engine que não foi possivel comunicar com o serviço.........................................
				//return;
			}
		}
		
		try {
			
			Log.v(tag, "RUN :: wait received");
			
			// Espero uma msg do serviço
			String serviceMSG = readbufferEvent.readLine();
			
			Log.v(tag, "RUN :: Event received");
				
			//Evento a avisar o engine do estado do telefone do android
				
			if (serviceMSG.equals(CallEventStateMachine.CALLSTATECHANGE_DIALING)) {
					
				// devolver o engine que o utilizador iniciou uma chamada
				engineInterfaceCallback.initCallEvent();
					
			} else if (serviceMSG.equals(CallEventStateMachine.CALLSTATECHANGE_ACTIVE)) {
					
				// devolver o engine que o utilizador iniciou uma chamada
				engineInterfaceCallback.initCallEvent();
					
			}else if (serviceMSG.equals(CallEventStateMachine.CALLSTATECHANGE_RINGING)) {	
					
				// devolver o engine que o utilizador iniciou uma chamada
				engineInterfaceCallback.initCallEvent();
					
			}else if (serviceMSG.equals(CallEventStateMachine.DISCONNECTPHONE_ERRORUNSPECIFIED)) {
					
				// devolver o engine que o utilizador terminou uma chamada
				engineInterfaceCallback.hungUpCallEvent();
					
			} else if (serviceMSG.equals(CallEventStateMachine.DISCONNECTPHONE_ERRORUNSPECIFIED)) {
					
				// devolver o engine que o utilizador terminou uma chamada
				engineInterfaceCallback.hungUpCallEvent();
					
			} else if (serviceMSG.equals(CallEventStateMachine.DISCONNECTPHONE_LOCAL)) {
					
				// devolver o engine que o utilizador terminou uma chamada
				engineInterfaceCallback.hungUpCallEvent();
					
			} else if (serviceMSG.equals(CallEventStateMachine.DISCONNECTPHONE_NORMAL)) {
					
				// devolver o engine que o utilizador terminou uma chamada
				engineInterfaceCallback.hungUpCallEvent();
					
			} else if (serviceMSG.equals(CallEventStateMachine.DISCONNECTPHONE_INCOMINGMISSED)) {
					
				// devolver o engine que o utilizador terminou uma chamada
				engineInterfaceCallback.hungUpCallEvent();
			}

			
		} catch (IOException e) {
			Log.v(tag, "@@@@@@@ RUN :: Error reading serviceMSG! @@@@@@@");
			
			// volto a reiniciar a minha ligação
			connectionEventRuning = false;
		}
		
		Log.v(tag, "RUN :: Out");
		run();
	}
	
	
	public TestLogsResult runService(TaskInterface ti) {
		
		int testState = CallEventStateMachine.BEGIN;
		String returnStateTask = "NOK: NA";
		
		String msg = null;
		setTimeOutFalse();
		
		String pIDMacro = ti.getIDmacro(); 
		String ptaskNumber = ti.getTaskNumber();
		String pdataInicio =  getResultDataFormat(new Date());
		//String pdataFim
		String ptaskID = ti.getTaskId();
		String pICCID = ti.getICCID();
		String pInfoCelula = "NA";
		String pinfoGPS = "NA";
		//String perrorStatus
		ArrayList<String> pparams = new ArrayList<String>();
		
		
		try {
			
			// estabelece ligação com o serviço
			clientSocketTest = new Socket("localhost", servicePortTest);
			
			if (clientSocketTest == null) {
				// connection fail
				return new TestLogsResult(pIDMacro,ptaskNumber,pdataInicio,getResultDataFormat(new Date()),ptaskID,pICCID,pInfoCelula,pinfoGPS,"NOK: Service Connection Problem",pparams);
			}
			
			readbufferTest = new BufferedReader(new InputStreamReader(clientSocketTest.getInputStream()));
			writeBufferTest = new PrintWriter(clientSocketTest.getOutputStream(), true);
			
			if (readbufferTest == null || writeBufferTest == null) {
				// connection fail
				return new TestLogsResult(pIDMacro,ptaskNumber,pdataInicio,getResultDataFormat(new Date()),ptaskID,pICCID,pInfoCelula,pinfoGPS,"NOK: Buffer Connection Problem",pparams);
			}
			
			// Activa o timer
			Integer i = Integer.parseInt(ti.getTimeOut()); 
			Log.v(ptaskID, "runService i :"+i.toString());
			long timeOutTask = i.longValue()*1000; 
			MyTimer TaskCodeTimer = new MyTimer(timeOutTask,this,timeOutTaskCode);
			
			// enviar o numero a efectuar a chamada
			String numberToCall = ti.getparamList().get(1);
			Log.v(ptaskID, "runService :: call number :"+numberToCall);
			writeBufferTest.println(numberToCall);
			
			// espera pelo ok de confirmação de execução
			msg = readbufferTest.readLine();
			
			// se for devolvido ok inicia a leitura dos eventos do teste
			if (msg.equals("OK")) {
				
				// Vou receber os eventos, ate que receba o evento de fim de testes
				// ou que o timeOut seja activado
				
				boolean sair = false;
				while (!getTimeState() && !sair) {
					
					// espera pelos eventos
					msg = readbufferTest.readLine();
					
					if (msg.equals(CallEventStateMachine.CALLSTATECHANGE_DIALING)) {
						
						testState = CallEventStateMachine.DIALING;
						
						returnStateTask = "NOK: TRY_CALL";
						
					} else if (msg.equals(CallEventStateMachine.CALLSTATECHANGE_ALERTING)) {
						
						testState = CallEventStateMachine.ALERTING;
						
						returnStateTask = "NOK: CALLING";
						
					} else if (msg.equals(CallEventStateMachine.DISCONNECTPHONE_OUTOFSERVICE)) {
						
						// Sem serviço
						testState = CallEventStateMachine.OUT_OF_SERVICE;
						
						returnStateTask = "NOK: OUT_OF_SERVICE";
						
						TaskCodeTimer.stopTime();
						
						// Termina o teste
						writeBufferTest.println("EXIT");
						
						writeBufferTest.close();
						readbufferTest.close();
						clientSocketTest.close();
						clientSocketTest = null;
						sair = true;
						
					} else if (msg.equals(CallEventStateMachine.CALLSTATECHANGE_ACTIVE)) {
						
						// chamada activa
						testState = CallEventStateMachine.ACTIVE;
						
						returnStateTask = "OK: Call Established";
						
					} else if (msg.equals(CallEventStateMachine.ERROR_UNSPECIFIED)) {
						
						if (testState == CallEventStateMachine.ALERTING) {
							// telemovel desligado ou ocupado
							
							returnStateTask = "NOK: PHONE_OFF";
							
						} else { //Active state
							// erro ainda não identificado
						}
						
						testState = CallEventStateMachine.ERROR_UNSPECIFIED;
						
						TaskCodeTimer.stopTime();
						
						// Termina o teste
						writeBufferTest.println("EXIT");
						
						writeBufferTest.close();
						readbufferTest.close();
						clientSocketTest.close();
						clientSocketTest = null;
						sair = true;
						
					} else if (msg.equals(CallEventStateMachine.DISCONNECTPHONE_NORMAL)) {
						
						//chamada desligada pelo outro cliente
						returnStateTask = "OK: CLENT_END_CALL";
						
						testState = CallEventStateMachine.NORMAL;
						
						TaskCodeTimer.stopTime();
						
						// Termina o teste
						writeBufferTest.println("EXIT");
						
						writeBufferTest.close();
						readbufferTest.close();
						clientSocketTest.close();
						clientSocketTest = null;
						sair = true;
						
					} else if (msg.equals(CallEventStateMachine.DISCONNECTPHONE_LOCAL)) {
						
						//chamada desligada pela APP
						
						testState = CallEventStateMachine.LOCAL;
						
						returnStateTask = "OK: END_CALL";
						
						TaskCodeTimer.stopTime();
						
						// Termina o teste
						writeBufferTest.println("EXIT");
						
						writeBufferTest.close();
						readbufferTest.close();
						clientSocketTest.close();
						clientSocketTest = null;
						sair = true;
						
					}
					
				}
				
				// Analisa o resultado após teste, e devolve esse resultado
				return new TestLogsResult(pIDMacro,ptaskNumber,pdataInicio,getResultDataFormat(new Date()),ptaskID,pICCID,pInfoCelula,pinfoGPS,returnStateTask,pparams);
				
			} else {
				
				// Termina o teste
				writeBufferTest.println("EXIT");
				
				writeBufferTest.close();
				readbufferTest.close();
				clientSocketTest.close();
				clientSocketTest = null;
				
				// Numero incorrecto
				return new TestLogsResult(pIDMacro,ptaskNumber,pdataInicio,getResultDataFormat(new Date()),ptaskID,pICCID,pInfoCelula,pinfoGPS,"NOK: NUMBER_ERROR",pparams);
			}
		
		} catch(Exception ex) {
			Log.v(tag, "runService :: ERROR :"+ex.toString());
			
			// falta fazer close dos stream e do socket...............................................
			try {
				// Termina o teste
				writeBufferTest.println("EXIT");
				
				writeBufferTest.close();
				readbufferTest.close();
				clientSocketTest.close();
				clientSocketTest = null;
			} catch(Exception e) {
				
			}
		}
		
		
		return new TestLogsResult(pIDMacro,ptaskNumber,pdataInicio,getResultDataFormat(new Date()),ptaskID,pICCID,pInfoCelula,pinfoGPS,"NOK: Application fail",pparams);
	}


	public void timeOut(int timerCode) {
		// TODO Auto-generated method stub
		try {
			if (timerCode == timeOutTaskCode) {
				setTimeOutTrue();
				if (clientSocketTest != null) {
					
					//envia fim de teste
					writeBufferTest.println("EXIT");
					
					writeBufferTest.close();
					readbufferTest.close();
					clientSocketTest.close();
					clientSocketTest = null;
				}
			}
		} catch(Exception ex){
			Log.v(tag, "timeOut :: ERROR :"+ex.toString());
		}
	}
	
	public synchronized void setTimeOutTrue() {
		activeTimeOut = true;
	}

	public synchronized void setTimeOutFalse() {
		activeTimeOut = false;
	}
	
	public synchronized boolean getTimeState() {
		return activeTimeOut;
	}*/
	
}
