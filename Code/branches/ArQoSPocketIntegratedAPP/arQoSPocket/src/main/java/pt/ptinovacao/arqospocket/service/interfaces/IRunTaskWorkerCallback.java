package pt.ptinovacao.arqospocket.service.interfaces;

import pt.ptinovacao.arqospocket.service.jsonresult.TaskJsonResult;

public interface IRunTaskWorkerCallback {

	public void taskExecutionCompleted(TaskJsonResult taskJsonResult, String id_test);
}
