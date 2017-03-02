package PTInov.IEX.ArQoSPocket.ServicesInterfaces;

import PTInov.IEX.ArQoSPocket.ResultLogs.TestLogsResult;


public interface ServiceInterface {
	
	public TestLogsResult runService(TaskInterface ti);
	
}
