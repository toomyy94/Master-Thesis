package PTInov.IEX.ArQoSPocket.ResultLogs;

import java.util.ArrayList;

public class TestFileList {

	ArrayList<MakeTestFile> filesLogsQueue = null;
	
	public TestFileList() {
		filesLogsQueue = new ArrayList<MakeTestFile>();
	}
	
	public boolean addNewLogFile(MakeTestFile tf) {
		if (haveThisTest(tf.getTestID())) {
			return false;
		}
		
		filesLogsQueue.add(tf);
		return true;
	}
	
	public boolean makeThisLogsFile(String testID, String path) {
		if (haveThisTest(testID)){
			
			int testIndex = getIndexByTestID(testID);
			getTestFileByIndex(testIndex).makeFile(path);
			filesLogsQueue.remove(testIndex);
			
			return true;
			
		} else {
			return false;
		}
	}
	
	public void makeAllLogsFiles(String path) {
		
		// corre toda a lista e cria os respectivos ficheiros
		for (MakeTestFile mtf :filesLogsQueue) {
			mtf.makeFile(path);
		}
		
		// limpa toda a queue
		filesLogsQueue.clear();
	}
	
	public int getIndexByTestID(String testID) {
		
		int i=0;
		for (i=0;i<filesLogsQueue.size();i++) {
			if (filesLogsQueue.get(i).getTestID().equals(testID))
				return i;
		}
		
		return -1;
	}
	
	public boolean haveThisTest(String testID) {
		if (getIndexByTestID(testID)==-1) return false;
		else return true;
	}
	
	public MakeTestFile getTestFileByIndex(int index) {
		return filesLogsQueue.get(index);
	}
	
	public boolean setTestFileByIndex(int index, MakeTestFile t) {
		
		if (filesLogsQueue.size()<index || index<0) return false;
		
		filesLogsQueue.add(index, t);
		return true;
	}
}
