package pt.ptinovacao.arqospocket.service.interfaces;

import pt.ptinovacao.arqospocket.service.jsonresult.TaskJsonResult;

public interface IRunTaskWorker {

	public void async_result(TaskJsonResult taskJsonResult);
}
