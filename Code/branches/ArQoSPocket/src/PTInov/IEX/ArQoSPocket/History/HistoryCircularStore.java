package PTInov.IEX.ArQoSPocket.History;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import PTInov.IEX.ArQoSPocket.ParserFileTasks.TaskStruct;
import PTInov.IEX.ArQoSPocket.ResultLogs.TestLogsResult;
import PTInov.IEX.ArQoSPocket.StaticValues.TasksName;
import PTInov.IEX.ArQoSPocket.TaskStore.TaskStoreStruct;
import PTInov.IEX.ArQoSPocket.UserInterface.Style.RowDataTwoLinesHistory;
import android.util.Log;

public class HistoryCircularStore {
	
	private static String tag = "HistoryCircularStore";
	
	private List<TaskStoreStruct> taskList;
	private int maxlistSize;
	
	public HistoryCircularStore(int size) {
		maxlistSize = size;
		taskList = new ArrayList<TaskStoreStruct>();
	}
	
	public synchronized void addElem(TaskStoreStruct tss) {
		
		Log.v(tag, "addElem :: In");
		
		if (taskList.size()==maxlistSize) {
			// remove um elemento antes de adicionar
			removeLast();
		}
		
		taskList.add(tss);
		
		//Collections.sort(taskList);
		Collections.sort(taskList, new MyReverseSort());
		
		Log.v(tag, "History list :"+taskList.toString());
	}
	
	
	public List<RowDataTwoLinesHistory> getAllInformationAboutTask(int testIndex, int taskIndex) {
		
		List<RowDataTwoLinesHistory> listresult = new ArrayList<RowDataTwoLinesHistory>();
		
		Log.v(tag,"getAllInformationAboutTask :: In - with testIndex :"+testIndex+" and taskIndex :"+taskIndex);
		
		if (testIndex<0 || testIndex>=taskList.size()) {
			
			listresult.add(new RowDataTwoLinesHistory("Não existe informação disponível","",true));
			return listresult;
		}
		
		ArrayList<TestLogsResult> tlrlist = taskList.get(testIndex).getAllResultList();
		
		if (taskIndex<0 || taskIndex>=tlrlist.size()) {
			listresult.add(new RowDataTwoLinesHistory("Não existe informação disponível","",true));
			return listresult;
		}

		
		ArrayList<TaskStruct> listTask = taskList.get(testIndex).getTaskList();
		
		if (taskIndex<0 || taskIndex>=listTask.size()) {
			listresult.add(new RowDataTwoLinesHistory("Não existe informação disponível","",true));
			return listresult;
		}
		TaskStruct ts = listTask.get(taskIndex);
		
		TestLogsResult tlr = tlrlist.get(taskIndex);
		// necessario formatar esta data...........
		listresult.add(new RowDataTwoLinesHistory("Data de Início:",tlr.getDataInicio(),true));	
		// necessario formatar esta data..........
		listresult.add(new RowDataTwoLinesHistory("Data Fim:",tlr.getDataFim(),true));
		listresult.add(new RowDataTwoLinesHistory("Destinatário:",ts.getparamList().get(1),true));
		listresult.add(new RowDataTwoLinesHistory("Estado:",tlr.getErrorStatus(),tlr.getErrorStatus().contains("NOK")?false:true));
		
		return listresult;
	}
	
	// vai deixar de ser utilizado, mais tarde pode ser removido..................................
	public String[] getAllInformationAboutTask2(int testIndex, int taskIndex) {
		
		Log.v(tag,"getAllInformationAboutTask :: In - with testIndex :"+testIndex+" and taskIndex :"+taskIndex);
		
		if (testIndex<0 || testIndex>=taskList.size()) {
			String[] s = new String[1];
			s[0]="Não existe informação disponível";
			
			return s;
		}
		
		ArrayList<TestLogsResult> tlrlist = taskList.get(testIndex).getAllResultList();
		
		if (taskIndex<0 || taskIndex>=tlrlist.size()) {
			String[] s = new String[1];
			s[0]="Não existe informação disponível";
			
			return s;
		}
		
		TestLogsResult tlr = tlrlist.get(taskIndex);
		
		String[] s = new String[3];
		
		s[0] = "Data Inicio: "+ tlr.getDataInicio();
		s[1] = "Data Fim: "+tlr.getDataFim();
		s[2] = "Estado: "+tlr.getErrorStatus();	
		
		return s;
	}
	
	
	public List<RowDataTwoLinesHistory> getAllTaskByTestIndex(int testIndex) {
		List<RowDataTwoLinesHistory> resultList = new ArrayList<RowDataTwoLinesHistory>();
		
		if (testIndex<0 || testIndex>=taskList.size()) {
			
			resultList.add(new RowDataTwoLinesHistory("Não existem Tarefas","",false));
			return resultList;
		}
		
		ArrayList<TestLogsResult> tlr = taskList.get(testIndex).getAllResultList();
		
		int i;
		for (i=0;i<tlr.size();i++) {
			
			String taskName = TasksName.getTaskNameByID(Integer.parseInt(taskList.get(testIndex).getTaskList().get(i).getTaskId()));
			if (taskName == null) taskName = "NA";
			
			resultList.add(new RowDataTwoLinesHistory(taskName,taskList.get(testIndex).getAllResultList().get(i).getErrorStatus().contains("NOK")?"NOK":"OK",false));
				//s[i]="Tarefa ID : "+TasksName.getTaskNameByID(Integer.parseInt(taskList.get(testIndex).getTaskList().get(i).getTaskId()));
		}
		
		return resultList;
	}
	
	// para apagar não vai ser utilizado............................................
	public String[] getAllTaskByTestIndex2(int testIndex) {
		
		Log.v(tag,"getAllTask :: In - with testIndex :"+testIndex);
		
		if (testIndex<0 || testIndex>=taskList.size()) {
			String[] s = new String[1];
			s[0]="Não existem Tarefas";
			
			return s;
		}
		
		ArrayList<TestLogsResult> tlr = taskList.get(testIndex).getAllResultList();
		
		Log.v(tag,"getAllTask :: tlr.size() :"+tlr.size());
		String[] s = new String[tlr.size()];
		
		int i;
		for (i=0;i<tlr.size();i++) {
			
			// Tenho de ver o que se passa aqui com o tamanho do array........................................................................
			//pra nao dar problemas vou meter pra ja um if
			
			if (taskList.get(testIndex).getTaskList().size()>i) {
			
				s[i]="Tarefa ID : "+TasksName.getTaskNameByID(Integer.parseInt(taskList.get(testIndex).getTaskList().get(i).getTaskId()));
				Log.v(tag,"getAllTask :: save :"+taskList.get(testIndex).getTaskList().get(i).getTaskId());
			}
		}
		
		Log.v(tag,"getAllTask :: Out - with :"+s.toString());
		
		return s;
		
	}
	
	
	public List<TaskStoreStruct> getHistoryList() {
		return taskList;
	}
	
	// vai ser alterada................ deixando de ser necessario
	public String[] getAllHistory() {
		if (taskList.size()==0) {
			
			String[] s = new String[1];
			s[0]="Histórico Vazio";
			
			return s;
		}
		
		String[] s = new String[taskList.size()];
		
		int i;
		TaskStoreStruct tss = null;
		for (i=0;i<taskList.size();i++) {
			tss = taskList.get(i);
			s[i] = getTestNameHistoryFormat(tss.getTestHeadStruct().getTestName(), tss.getFileNameStructObject().getDataInicioResultFormat());
		}
		
		return s;
	}
	
	public String getTestNameHistoryFormat(String TestName, String dataInicio) {
		return "Teste ID: "+TestName+"Data: "+dataInicio;
	}
	
	/*
	public List<HistoryTestStore> getAllList() {
		return taskList;
	}*/
	
	private void removeLast() {
		taskList.remove(taskList.size()-1);
	}
	
	public void clearAll() {
		taskList = new ArrayList<TaskStoreStruct>();
	}
}
