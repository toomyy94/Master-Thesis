package pt.ptinovacao.arqospocket.service.interfaces;

import pt.ptinovacao.arqospocket.service.structs.TestExecutionStruct;

public interface ITestSchedulerReport {

	public void test_execution_complete(TestExecutionStruct testExecutionStruct);
	public void test_execution_updated(String test_id);
}
