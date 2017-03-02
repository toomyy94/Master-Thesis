package PTInov.IEX.ArQoSPocket.ResultLogs;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

import PTInov.IEX.ArQoSPocket.ResultLogs.TestLogsResult;
import android.util.Log;

public class MakeTestFile {
	
	private static String tag = "MakeTestFile";
	
	private String Mac;
	private String IDTest;
	private String dataInicio;
	private String dataFim;
	private String IDModulo;
	private ArrayList<TestLogsResult> testList = null; 
	
	public MakeTestFile(String pMac, String pIDTest,String pdataInicio, String pdataFim, String pIDModulo) {
		Mac = pMac;
		IDTest = pIDTest;
		dataInicio = pdataInicio;
		dataFim = pdataFim;
		IDModulo = pIDModulo;
		testList = new ArrayList<TestLogsResult>();
	}
	
	public String getTestID() {
		return IDTest;
	}
	
	public void appendLogTest(TestLogsResult tl) {
		testList.add(tl);
	}

	public boolean makeFile(String path) {
		boolean result = true;
		
		try {
			
			String fileName = path+"Res_"+Mac+"#"+IDTest+"_"+dataInicio+"_"+dataFim+"_R"+IDModulo+".txt";
			FileWriter fstream = new FileWriter(fileName);
			BufferedWriter out = new BufferedWriter(fstream);
			
			if (testList!=null) {
				for (TestLogsResult tl :testList) {
					out.write(tl.toFile());
				}
			}
			
			out.close();
		} catch (Exception e){
			Log.v(tag, "ERROR :"+e.toString());
			result = false;
		}
		
		return result;
	}
}
