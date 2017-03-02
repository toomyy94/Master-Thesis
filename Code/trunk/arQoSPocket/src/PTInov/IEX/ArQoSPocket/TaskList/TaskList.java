package PTInov.IEX.ArQoSPocket.TaskList;

import java.util.ArrayList;
import java.util.List;

import PTInov.IEX.ArQoSPocket.ParserFileTasks.TaskStruct;
import PTInov.IEX.ArQoSPocket.TaskStore.TaskStoreStruct;

public class TaskList {
	
	ArrayList<TaskStoreStruct> testList = null;
	
	public TaskList() {
		testList = new ArrayList<TaskStoreStruct>();
	}
	
	public void addTest(TaskStoreStruct t) {
		testList.add(t);
	}
	
	public ArrayList<TaskStoreStruct> getAllTask() {
		return testList;
	}
	
	public List<TaskStoreStruct> removeAllList() {
		List<TaskStoreStruct> r = testList;
		testList = new ArrayList<TaskStoreStruct>();
		
		return r;
	}
	
	public void addTestList(List<TaskStoreStruct> taskList) {
		for (TaskStoreStruct tss :taskList) {
			testList.add(tss);
		}
	}
	
	public String[] getTestList() {
		
		if (testList.size()==0){
			String[] sl = new String[1];
			
			sl[0]="Vazio";
			
			return sl;
		}
		
		String[] sl = new String[testList.size()];
		
		int i;
		for (i=0;i<testList.size();i++) {
			sl[i] = testList.get(i).getTestHeadStruct().getTestName();
		}
		
		return sl;
	}
	
	public String[] getTaskList(int testIndex) {
		
		// Caso não exista o teste
		if (testIndex<0 || testIndex>=testList.size()) {
			String[] sl = new String[1];
			
			sl[0]="Vazio";
			
			return sl;
		}
		
		TaskStoreStruct t = testList.get(testIndex);
		ArrayList<TaskStruct> taskList = t.getTaskList();
		
		if (taskList.size() ==0) {
			String[] sl = new String[1];
			
			sl[0]="Vazio";
			
			return sl;	
		}
		
		String[] sl = new String[taskList.size()];
		
		int i;
		for (i=0;i<taskList.size();i++) {
			sl[i] = "Ligar para: "+taskList.get(i).getparamList().get(1);
		}
		
		
		// ter atençao no fim de mudar o valor retornado...
		return sl;
	}
	
	
	// para alterar o numero
	public boolean SetNewNumber(int testIndex,int taskindex, String number) {
		
		if (testIndex<0 || testIndex>=testList.size()) {
			return false;
		}
		
		TaskStoreStruct t = testList.get(testIndex);
		ArrayList<TaskStruct> taskList = t.getTaskList();
		
		if (taskList.size() ==0) {
			return false;
		}
		
		taskList.get(taskindex).getparamList().set(1, number);
		
		
		// ter atençao no fim de mudar o valor retornado...
		return true;
	}
	
	
	public TaskStoreStruct getTaskStoreStructToRun(int index) {
		if (index>-1 && index<testList.size()) {
			
			// Alterar datas para execução
			TaskStoreStruct tss = testList.get(index);
			// já esta a ser feito o runOneTime....
			//tss.RunNow();
			
			return tss;
		}
		
		return null;
	}
}
