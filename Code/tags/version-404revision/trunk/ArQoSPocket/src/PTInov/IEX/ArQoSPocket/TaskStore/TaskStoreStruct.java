package PTInov.IEX.ArQoSPocket.TaskStore;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import PTInov.IEX.ArQoSPocket.History.HistoryCircularStore;
import PTInov.IEX.ArQoSPocket.ParserFileTasks.FileNameStructObject;
import PTInov.IEX.ArQoSPocket.ParserFileTasks.TaskStruct;
import PTInov.IEX.ArQoSPocket.ParserFileTasks.TestHeadStruct;
import PTInov.IEX.ArQoSPocket.ResultLogs.TestLogsResult;
import android.util.Log;

public class TaskStoreStruct implements Comparable<TaskStoreStruct> {

	private final String tag = "TaskStoreStruct";

	private Date nextTaskDate;
	private FileNameStructObject TextFileNome;
	private TestHeadStruct HeadStruct;
	private ArrayList<TaskStruct> taskList;

	// Lista de Resultado das tarefas
	private ArrayList<TestLogsResult> testList = null;

	private int IndexCurrentTask;
	private boolean Sucess = false;
	private boolean haveErrors = false;

	public TaskStoreStruct(Date pnextTask, FileNameStructObject pTextFileNome,
			TestHeadStruct pHeadStruct, ArrayList<TaskStruct> ptaskList) {
		nextTaskDate = pnextTask;
		TextFileNome = pTextFileNome;
		HeadStruct = pHeadStruct;
		taskList = ptaskList;

		IndexCurrentTask = 0;
		Sucess = false;
		haveErrors = false;

		testList = new ArrayList<TestLogsResult>();
	}

	public TaskStoreStruct(Date pnextTask, FileNameStructObject pTextFileNome,
			TestHeadStruct pHeadStruct, ArrayList<TaskStruct> ptaskList,
			ArrayList<TestLogsResult> ptestList, boolean pSucess, boolean phaveErrors) {
		nextTaskDate = pnextTask;
		TextFileNome = pTextFileNome;
		HeadStruct = pHeadStruct;
		taskList = ptaskList;

		IndexCurrentTask = 0;

		testList = ptestList;
		Sucess = pSucess;
		haveErrors = phaveErrors;
	}
	
	public boolean getErrorsTasks() {
		return haveErrors;
	}
	
	public void setTaskError() {
		haveErrors = true;
	}

	public boolean getTestState() {
		return Sucess;
	}

	public void setTestSucess() {
		Sucess = true;
	}

	public void appendLogTest(TestLogsResult tl) {
		testList.add(tl);
	}

	public void RunNowOneTime() {
		// actualiza as datas para correr agora
		Date d = new Date();
		nextTaskDate = d;

		// Actualiza datas Teste
		TextFileNome.RunNow();
		HeadStruct.RunNowOneTime();

	}

	public void RunNow() {
		// actualiza as datas para correr agora
		Date d = new Date();
		nextTaskDate = d;

		// Actualiza datas Teste
		TextFileNome.RunNow();
		HeadStruct.RunNow();
	}

	public void addDelayTest(long delayInSec) {

		Log.v(tag, "addDelayInTasks - In with delay :" + delayInSec);

		// Adiona delay ao ficheiro
		TextFileNome.addDateDelay(delayInSec * 1000);

		// Adiciona delay a head
		HeadStruct.addDateDelay(delayInSec * 1000);

		resetIndexTask();
	}

	public Date getNextTaskDate() {
		return nextTaskDate;
	}

	public FileNameStructObject getFileNameStructObject() {
		return TextFileNome;
	}

	public TestHeadStruct getTestHeadStruct() {
		return HeadStruct;
	}

	public ArrayList<TaskStruct> getTaskList() {
		return taskList;
	}

	/*
	 * 
	 * Reinicia o teste
	 * 
	 */
	private void resetIndexTask() {
		IndexCurrentTask = 0;

		// actualiza a primeira data
		nextTaskDate = incrementSecondsToDate(TextFileNome.getDataInicio(),
				(int) taskList.get(IndexCurrentTask).getDataInicio());

		// Limpa lista de resultados
		testList = new ArrayList<TestLogsResult>();

		Sucess = false;
		haveErrors = false;
	}

	public TaskStruct getNextTask() {
		TaskStruct ts = taskList.get(IndexCurrentTask);
		IndexCurrentTask++;

		// actualiza a data para a proxima tarefa
		if (haveMoreTask()) {
			// Log.v(tag,"...............########################.................. TextFileNome.getDataInicio() :"+TextFileNome.getDataInicio());
			// Log.v(tag,"...............########################.................. taskList.get(IndexCurrentTask).getDataInicio() :"+taskList.get(IndexCurrentTask).getDataInicio());
			nextTaskDate = incrementSecondsToDate(TextFileNome.getDataInicio(),
					(int) taskList.get(IndexCurrentTask).getDataInicio());
			// Log.v(tag,"...............########################.................. nextTaskDate :"+nextTaskDate);
		}

		return ts;
	}

	public TaskStruct getActualTask() {
		if (IndexCurrentTask == 0)
			return taskList.get(IndexCurrentTask);
		else
			return taskList.get(IndexCurrentTask - 1);

	}

	public boolean haveMoreTask() {

		if (IndexCurrentTask < taskList.size())
			return true;

		return false;
	}

	public String toString() {

		StringBuilder s = new StringBuilder();

		s.append("\n############ Test File ############\n");
		s.append("Exec Date:" + nextTaskDate.toString());
		s.append("\n-----------------------------------\n");
		s.append("@@@@@ FileNameStructObject\n");
		s.append(TextFileNome.toString());
		s.append("\n-----------------------------------\n");
		s.append("@@@@@ TestHeadStruct\n");
		s.append(HeadStruct.toString());
		s.append("\n-----------------------------------\n");
		s.append("@@@@@ ArrayList<TaskStruct>\n");
		s.append(taskList.toString());
		s.append("\n-----------------------------------\n");
		s.append("###################################\n");

		return s.toString();
	}

	public int compareTo(TaskStoreStruct arg0) {
		// TODO Auto-generated method stub
		return nextTaskDate.compareTo(arg0.getNextTaskDate());
	}

	public TaskStoreStruct clone() {

		Log.v(tag, "clone :: In");

		ArrayList<TaskStruct> taskListClone = new ArrayList<TaskStruct>();

		for (TaskStruct ts : taskList) {
			taskListClone.add(ts.clone());
		}

		Log.v(tag, "clone :: cloned taskList");

		ArrayList<TestLogsResult> testListClone = new ArrayList<TestLogsResult>();

		if (testList == null)
			Log.v(tag, "clone :: testList is null");
		else
			Log.v(tag, "clone :: testList is not null :" + testList.toString());

		for (TestLogsResult tlr : testList) {
			if (tlr == null)
				Log.v(tag, "clone :: tlr is null");
			else {
				Log.v(tag, "clone :: vou adicionar ao array");
				testListClone.add(tlr.clone());
				Log.v(tag, "clone :: acabei de adicionar ao array");
			}
		}

		Log.v(tag, "clone :: cloned testList");

		return new TaskStoreStruct((Date) nextTaskDate.clone(), TextFileNome
				.clone(), HeadStruct.clone(), taskListClone, testListClone,
				Sucess, haveErrors);
	}

	public void verifyLogSpace(String path) {

		try {

			// Numero maximo de ficheiros de log
			 final long maxfileNumber = 1000;
			//final int maxfileNumber = 10;

			// Numero de ficheiro preservados até ser atingido o maximo
			final long clearFileTo = 700;
			//final int clearFileTo = 5;

			File fdir = new File(path);

			Log.v(path, "Numero de ficheiros na directoria :"
					+ fdir.list().length);

			String[] fileNameList = fdir.list();
			int listlength = fileNameList.length;

			if (listlength > maxfileNumber) {
				
				// copiar todos os ficheiro para um lista para os ordenar pela data
				ArrayList<ObjectFileToDelete> fileList = new ArrayList<ObjectFileToDelete>();
				
				for (String name :fileNameList) {
					File delFile = new File(path + "/" + name);
					fileList.add(new ObjectFileToDelete(delFile));
				}
				
				Collections.sort(fileList);
				
				// apagar os ultimos ficheiros
				int i;
				for(i=(listlength-1);i>(clearFileTo);i--) {
					fileList.get(i).deleteFile();
				}
				
			}

		} catch (Exception ex) {
			Log.v(path, "verifyLogSpace::ERROR: " + ex.toString());
		}

	}

	public boolean makeTempResultFile(String path) {

		Log.v(path, "makeTempResultFile :: In");

		verifyLogSpace(path);

		boolean result = true;

		String Mac = TextFileNome.getMac();
		String IDTest = TextFileNome.getTestID();
		String dataInicio = TextFileNome.getDataInicioResultFormat();
		String dataFim = TextFileNome.getDataFimResultFormat();
		String IDModulo = TextFileNome.getModuloID();

		Log.v(path, "makeTempResultFile :: Saved in history");

		try {

			String fileName = path + "Res_" + Mac + "#" + IDTest + "_"
					+ dataInicio + "_" + dataFim + "_R" + IDModulo + ".txt";
			FileWriter fstream = new FileWriter(fileName);
			BufferedWriter out = new BufferedWriter(fstream);

			if (testList != null) {
				for (TestLogsResult tl : testList) {
					out.write(tl.toFile() + "\n");
				}
			}

			out.close();
		} catch (Exception e) {
			Log.v(tag, "makeTempResultFile::ERROR :" + e.toString());
			result = false;
		}

		return result;
	}

	public boolean makeResultFile(String path, HistoryCircularStore historyStore, HistoryCircularStore errorHistoryStore) {

		Log.v(path, "makeResultFile :: In");

		verifyLogSpace(path);

		boolean result = true;

		String Mac = TextFileNome.getMac();
		String IDTest = TextFileNome.getTestID();
		String dataInicio = TextFileNome.getDataInicioResultFormat();
		String dataFim = TextFileNome.getDataFimResultFormat();
		String IDModulo = TextFileNome.getModuloID();

		Log.v(path, "makeResultFile :: Save in history");
		// Save in history store
		historyStore.addElem(this.clone());
		
		// adiciona ao historico de erros
		if (this.haveErrors) {
			errorHistoryStore.addElem(this.clone());
		}

		Log.v(path, "makeResultFile :: Saved in history");

		try {

			String fileName = path + "Res_" + Mac + "#" + IDTest + "_"
					+ dataInicio + "_" + dataFim + "_R" + IDModulo + ".txt";
			FileWriter fstream = new FileWriter(fileName);
			BufferedWriter out = new BufferedWriter(fstream);

			if (testList != null) {
				for (TestLogsResult tl : testList) {
					out.write(tl.toFile() + "\n");
				}
			}

			out.close();
		} catch (Exception e) {
			Log.v(tag, "makeResultFile::ERROR :" + e.toString());
			result = false;
		}

		return result;
	}

	public String toStringResultLogs() {

		String Mac = TextFileNome.getMac();
		String IDTest = TextFileNome.getTestID();
		String dataInicio = TextFileNome.getDataInicioResultFormat();
		String dataFim = TextFileNome.getDataFimResultFormat();
		String IDModulo = TextFileNome.getModuloID();

		StringBuilder s = new StringBuilder();

		s.append("\n############ Log test File ############\n");
		s.append("Mac:" + Mac + "\n");
		s.append("IDTest:" + IDTest + "\n");
		s.append("dataInicio:" + dataInicio + "\n");
		s.append("dataFim:" + dataFim + "\n");
		s.append("IDModulo:" + IDModulo + "\n");
		s.append("\n-----------------------------------\n");
		s.append(testList.toString());
		s.append("\n###################################\n");

		return s.toString();
	}

	public ArrayList<TestLogsResult> getAllResultList() {
		return testList;
	}

	public Date incrementSecondsToDate(Date d, int seconds) {

		try {

			Calendar c = Calendar.getInstance();
			c.setTime(d);
			// c.add(Calendar.SECOND, (int) taskList.get(0).getDataInicio());
			c.add(Calendar.SECOND, seconds);
			return c.getTime();

		} catch (Exception ex) {
			Log.v(tag, "incrementSecondsToDate::ERROR :" + ex.toString());
			return null;
		}

	}
}