package PTInov.IEX.ArQoSPocket.TaskStore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.util.Log;

public class TaskStore {
	
	private static String tag = "TaskStore";
	
	// Esttrutura que guarda cada ficheiro de tarefas, ordenado por data - crescente
	private List<TaskStoreStruct> taskList;

	public TaskStore() {
		taskList = new ArrayList<TaskStoreStruct>();
	}
	
	public synchronized void addTask(TaskStoreStruct t) {
		taskList.add(t);
		
		Collections.sort(taskList);
		
		Log.v(tag, "List :"+taskList.toString());
	}
	
	public synchronized TaskStoreStruct getFirstTask() {
		
		// devolve null se não tiver nenhuma task
		if (taskList.size() == 0) return null;
		
		TaskStoreStruct t = taskList.get(0);
		taskList.remove(0);
		return t;
	}
	
	public synchronized boolean haveTasks() {
		if (taskList.size() == 0) return false;
		else return true;
	}
	
	public synchronized Date getDateFirstTask() {
		
		// devolve null se não tiver nenhuma task
		if (taskList.size() == 0) return null;
		
		return taskList.get(0).getNextTaskDate();
	}
	
	public synchronized String toString() {
		
		StringBuilder s = new StringBuilder();
		
		s.append("\n********************************************************\n");
		s.append("****                    TaskStore                   ****\n");    
		s.append("********************************************************\n");
		s.append(taskList.toString());
		s.append("********************************************************\n");
		
		return s.toString();
	}
	
	public List<TaskStoreStruct> removeAllTests() {
		
		// metodo utilizado para colocar os testes na queue de pause
		
		List<TaskStoreStruct> r = taskList;
		taskList = new ArrayList<TaskStoreStruct>();
		
		return r;
	}
	
	public void addTestList(List<TaskStoreStruct> r) {
		for (TaskStoreStruct tss:r) {
			taskList.add(tss);
		}
	}
	
	public List<TaskStoreStruct> getTestAllList() {
		return taskList;
	}
}
