package PTInov.IEX.ArQoSPocket.EngineService;

import java.util.Date;
import java.util.concurrent.Semaphore;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import PTInov.IEX.ArQoSPocket.Alarming.Alarming;
import PTInov.IEX.ArQoSPocket.History.HistoryCircularStore;
import PTInov.IEX.ArQoSPocket.ParserFileTasks.TaskStruct;
import PTInov.IEX.ArQoSPocket.ParserFileTasks.TestHeadStruct;
import PTInov.IEX.ArQoSPocket.RegistryAndFactory.ServiceRegistry;
import PTInov.IEX.ArQoSPocket.ResultLogs.TestLogsResult;
import PTInov.IEX.ArQoSPocket.ServicesInterfaces.EngineInterface;
import PTInov.IEX.ArQoSPocket.ServicesInterfaces.ServiceInterface;
import PTInov.IEX.ArQoSPocket.TaskStore.TaskStore;
import PTInov.IEX.ArQoSPocket.TaskStore.TaskStoreStruct;
import PTInov.IEX.ArQoSPocket.UserInterface.EngineService;
import PTInov.IEX.ArQoSPocket.UserInterface.MainInterface;

public class EngineThread implements Runnable {
	
	private static String tag = "EngineThread";
	
	private final int LOOP_TEST = 1;
	
	TaskStore myRuningTaskStore;
	HistoryCircularStore historyStore = null;
	public HistoryCircularStore errorHistoryStore = null;
	EngineInterface ieRef;
	
	private boolean running;
	private Semaphore mainSemaphore;
	
	public EngineThread(TaskStore runingtaskStore, HistoryCircularStore phistoryStore, HistoryCircularStore perrorHistoryStore, EngineInterface ie, Semaphore s) {
		
		Log.v(tag, "EngineThread - In");
		
		myRuningTaskStore = runingtaskStore;
		historyStore = phistoryStore;
		errorHistoryStore = perrorHistoryStore;
		ieRef = ie;
		mainSemaphore = s;
		
		running = true;
		
		Log.v(tag, "EngineThread - Out");
	}
	
	public void shutdown() {
		
		Log.v(tag, "shutdown - In");
		
	    running = false;
	  }

	public void run() {
		
		Log.v(tag, "run - In");
		
		logRuningtaskqueue();
		
		/*
		 * 
		 * Nesta função é feita a lógica da execução da aplicação
		 *  - De forma a facilitar a organização do código, 
		 *  - as diferentes acções sessão devididas em tarefas
		 * 
		 */
		
		if (!running) {
			
			// call back para copiar todas as execuções na lista de tarefas a correr para a lista de pause
			ieRef.ThreadStopCopyAllTestToPauseStore();
			
			Log.v(tag, "run :: shutdown - before wait");
			return;
		}
		
		// testa se existem tarefas, caso não existam sai...
		if (!myRuningTaskStore.haveTasks()){
			Log.v(tag, "Não existem tarefas para executar!");
			this.ieRef.EmptyQueueTest();
			//this.ieRef.ActionStop();
			return;
		}
		
		
		// Coloca a Thread em espera até a hora de execução da próxima tarefa
		WaitToNextTaskTime();
		
		
		if (!running) {
			
			// call back para copiar todas as execuções na lista de tarefas a correr para a lista de pause
			ieRef.ThreadStopCopyAllTestToPauseStore();
			
			Log.v(tag, "run :: shutdown - after wait");
			return;
		}
		
		try {
			mainSemaphore.acquire();
		
			try {
				// Obtem a tarefa a ser executada
				Log.v(tag, "run :: 1");
				
				// Retira a TaskStoreStruct da myRuningTaskStore, caso exista
				TaskStoreStruct tss = myRuningTaskStore.getFirstTask();
				
				 if (tss == null) {
					 Log.v(tag, "Erro a obter o TaskStoreStruct!");
					 
					 mainSemaphore.release();
					 return;
				 }
				
				 Log.v(tag, "run :: 2");
				// Executa as tarefa e devolve o resultado da sua execução
				ieRef.ActionRun();
				TestLogsResult tlr = ExecServiceTask(tss);
				
				Log.v(tag, "run :: 4");
				
				// retorna o erro a interface
				if (tlr.getErrorStatus().contains("NOK:")) {
					
					Log.v(tag, "run :: 5");
					
					//adiciona erro a interface
					ieRef.taskFail();
					Log.v(tag, "run :: 6");
					
					//adiciona o estado do ultimo teste
					ieRef.lastTaskState(2); //2 - NOK
					Log.v(tag, "run :: 7");
					
				} else {
					tss.setTestSucess();
					//adiciona o estado do ultimo teste
					ieRef.lastTaskState(1); //1 - OK
					Log.v(tag, "run :: 8");
				}
				
				Date d = new Date();
				ieRef.dateLastTask(d.toGMTString());
				Log.v(tag, "run :: 9");
		
				//Gere as lista de tarefas consoante o resultado e a dependencia da tarefa executada
				RebuildTaskList(tss,tlr);
				Log.v(tag, "run :: 11");
				
			} catch(Exception ex) {
				Log.v(tag, "run - ERROR :"+ex.toString());
			}
		
			mainSemaphore.release();
		} catch(Exception ex) {
			Log.v(tag, "run - Semaphore - ERROR :"+ex.toString());
		}
		
		Log.v(tag, "...........................................................run - Out");
		
		logRuningtaskqueue();
		run();
	}
	
	private void logRuningtaskqueue() {
		Log.v(tag, myRuningTaskStore.toString());
	}
	
	
	public void RebuildTaskList(TaskStoreStruct tss, TestLogsResult tlr) {
		
		Log.v(tag, "RebuildTaskList - In");
		
		// Grava os resultados do teste para ficheiro
		tss.makeTempResultFile(EngineService.fileLogsPath);
		Log.v(tag, "updateLoopTest - LogFile Writed!");
		
		// verifica se acorreu erro na execução da task e em caso de erro marca a estrutura com erro para identificar se houve uma task com erro
		if (tlr.getErrorStatus().contains("NOK:")) {
			tss.setTaskError();
		}
		
		// Testa se os teste devem continuar na proxima tarefa ou ficar por aki
		TaskStruct t = tss.getActualTask();
		int op = Integer.parseInt(t.getDependencia());
		switch(op) {
			case 0: // 0 - Continua com os testes
				
				Log.v(tag, "RebuildTaskList - case 0");
				
				// Testa se existem mais tarefas no test
				if (tss.haveMoreTask()) {
				
					// Caso exista mais tarefas, volta a colocar o teste na queue
					myRuningTaskStore.addTask(tss);
					ieRef.queueChanged();
					Log.v(tag, "RebuildTaskList - CASE 0 : add test to queue");
				} else {
				
					// Caso não exista mais tarefas
				
					// 1 - Fecha logs do teste e envia-os para o servidor
					//......................................................................................
				
					// 2 - Verifica se é um teste em loop e faz os respectivos ajustes
					updateLoopTest(tss);
					
					// verificar se acabou com erro
					if (!tss.getTestState()) {
						// lancha o alarme
						Alarming.showNotificationMessage((Context) ieRef, EngineService.mNM);
					}
				}
				
				break;
			case 1: // 1 - stop on ok
				
				Log.v(tag, "RebuildTaskList - case 1");
				
				// Verifica se a ultima tarefa acabou com ok
				if (tlr.getErrorStatus().contains("NOK:")) {
					
					Log.v(tag, "RebuildTaskList - status NOK");
					
					// 2 - NOK
				
					// 2.1 - Testa se existem mais tarefas no test
					if (tss.haveMoreTask()) {
				
						// 2.1.2 - Caso exista mais tarefas, volta a colocar o teste na queue
						myRuningTaskStore.addTask(tss);
						ieRef.queueChanged();
						Log.v(tag, "RebuildTaskList - CASE 1 : add test to queue");
					} else {
				
						// 2.2 - Caso não exista mais tarefas
				
						// 2.2.1 - Fecha logs do teste e envia-os para o servidor
						//......................................................................................
				
						// 2.2.2 - Verifica se é um teste em loop e faz os respectivos ajustes
						updateLoopTest(tss);
						
						// lancha o alarme
						Alarming.showNotificationMessage((Context) ieRef, EngineService.mNM);
					}
					
				} else {
					
					Log.v(tag, "RebuildTaskList - status OK");
					
					// 1 - ok
				
					// 1.1 - Fecha logs do teste e envia-os para o servidor
					//......................................................................................
				
					// 1.2 - Verifica se é um teste em loop e faz os respectivos ajustes
					updateLoopTest(tss);
				}
				
				break;
			case 2: // 2 - stop on NOK
				
				Log.v(tag, "RebuildTaskList - case 2");
				
				// Verifica se a ultima tarefa acabou com ok
				if (tlr.getErrorStatus().contains("NOK:")) {
					
					Log.v(tag, "RebuildTaskList - status NOK");
				
					// 1 - NOK
				
					// 1.1 - Fecha logs do teste e envia-os para o servidor
					//......................................................................................
				
					// 1.2 - Verifica se é um teste em loop e faz os respectivos ajustes
					updateLoopTest(tss);
					
					// lancha o alarme
					Alarming.showNotificationMessage((Context) ieRef, EngineService.mNM);
					
				} else {
					
					Log.v(tag, "RebuildTaskList - status OK");
				
					// 2 - ok
				
					// 2.1 - Testa se existem mais tarefas no test
					if (tss.haveMoreTask()) {
				
						// 2.1.2 - Caso exista mais tarefas, volta a colocar o teste na queue
						myRuningTaskStore.addTask(tss);
						ieRef.queueChanged();
						Log.v(tag, "RebuildTaskList - CASE 2 : add test to queue");
					} else {
				
						// 2.2 - Caso não exista mais tarefas
				
						// 2.2.1 - Fecha logs do teste e envia-os para o servidor
						//......................................................................................
				
						// 2.2.2 - Verifica se é um teste em loop e faz os respectivos ajustes
						updateLoopTest(tss);
					}
				}
				
				break;
		}
		
		Log.v(tag, "RebuildTaskList - Out");
		//tlr.getErrorStatus();
	}
	
	private void LogQueueLogs() {
		//Log.v(tag, OutPutResultsQueue.toString());
	}
	
	private void updateLoopTest(TaskStoreStruct tss) {
		
		Log.v(tag, "updateLoopTest - In");
		
		TestHeadStruct ths = tss.getTestHeadStruct();
		
		Log.v(tag, "updateLoopTest - ths.getTestType() :"+ths.getTestType());
		
		//LogQueueLogs();
		// Grava os resultados do teste para ficheiro
		tss.makeResultFile(EngineService.fileLogsPath, historyStore, errorHistoryStore);
		Log.v(tag, "updateLoopTest - LogFile Writed!");
		//LogQueueLogs();
		
		
		if (ths.getTestType()==LOOP_TEST) {
			
	
			// 2.1 - Se for um teste em loop, actualiza as datas das suas tarefas e a sua data de inicio e coloca-as na queue
			
			Log.v(tag, "updateLoopTest - getIntervaloLOOP :"+ths.getIntervaloLOOP());
			Log.v(tag, "RebuildTaskList - delayloop :"+ths.getIntervaloLOOP());
			
			// Actualiza a data do teste para gravar no futuro os logs
			tss.addDelayTest(ths.getIntervaloLOOP());
			
			myRuningTaskStore.addTask(tss);
			ieRef.queueChanged();
			Log.v(tag, "updateLoopTest - add test to queue");
			
		} else {
	
			// 2.2 - Se não for um teste em loop remove o teste da queue
			// o test já foi removido da queue.....

		}
		
		Log.v(tag, "updateLoopTest - Out");
	}
	
	
	public TestLogsResult ExecServiceTask(TaskStoreStruct tss) {
		
		Log.v(tag, "ExecServiceTask - In");
		
		TaskStruct t = tss.getNextTask();
		
		// vai ao registo instanciar o serviço
		ServiceInterface si = ServiceRegistry.getServiceInstance(t.getTaskId(), ieRef);
		
		Log.v(tag, "ExecServiceTask - Out");
		
		if (si == null){
			Log.v(tag, "Serviço não suportado!!!!");
			TestLogsResult tlr = new TestLogsResult(null,null,null,null,null,null,null,null,"NOK: Not Supported",null);
		}
		
		// vai correr o servico
		TestLogsResult tlr = si.runService(t);
		
		// Vou guardar o resultado do teste na estrutura
		tss.appendLogTest(tlr);
		
		return tlr;
	}
	
	
	public void WaitToNextTaskTime() {
		
		Log.v(tag, "WaitToNextTaskTime - In");
		
		try {
		
			Date now = new Date();
		
			Date d = myRuningTaskStore.getDateFirstTask();
		
			if (d.before(now)) {
				Log.v(tag,"run:: é necessário correr agora as tasks!!!");
			} else {
				//Testar quanto tempo tenho de esperar ate a task
				long timewait = getTimeWaitMilsec(d, now);
				Log.v(tag,"run:: tenho de esperar :"+timewait+" milsec");		
				try {
					Thread.sleep(timewait);
				} catch(Exception ex) {
					Log.v(tag, "WaitToNextTaskTime :: Sleep ERROR: "+ex.toString());
				}
			}
		} catch(Exception ex) {
			Log.v(tag, "WaitToNextTaskTime::ERROR: "+ex.toString());
		}
		
		Log.v(tag, "WaitToNextTaskTime - Out");
	}
	
	
	public long getTimeWaitMilsec(Date d1, Date d2) {
		
		Log.v(tag, "getTimeWaitMilsec - In");
		
		return d1.getTime()-d2.getTime();
		
	}

}
