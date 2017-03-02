package PTInov.IEX.ArQoSPocket.ServicesInterfaces;

import java.util.ArrayList;
import java.util.Date;

public interface TaskInterface {
	
	public String getTaskId();
	
	public String getIDmacro();
	
	public String getTaskNumber();
	
	public String getICCID();
	
	public long getDataInicio();
	
	public String getTimeOut();
	
	//public String getExecNow();
	
	//public String getDependencia();
	
	public ArrayList<String> getparamList();
	
}
