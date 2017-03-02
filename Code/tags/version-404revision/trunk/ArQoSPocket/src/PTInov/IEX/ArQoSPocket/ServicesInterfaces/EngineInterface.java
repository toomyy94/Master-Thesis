package PTInov.IEX.ArQoSPocket.ServicesInterfaces;

public interface EngineInterface {
	public void queueChanged();
	public void taskFail();
	public void lastTaskState(int i);
	public void dateLastTask(String s);
	public void ActionRun();
	public void ActionStop();
	public void EmptyQueueTest();
	
	public void ThreadStopCopyAllTestToPauseStore();
	
	//para testes
	public void sendIntent();
	
	
	// eventos dos serviços
	public void initCallEvent();
	public void hungUpCallEvent();
}
