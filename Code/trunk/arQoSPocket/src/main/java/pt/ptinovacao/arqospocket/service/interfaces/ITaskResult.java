package pt.ptinovacao.arqospocket.service.interfaces;

import android.content.Context;

import java.util.Map;

import pt.ptinovacao.arqospocket.service.enums.EConnectionTechnology;
import pt.ptinovacao.arqospocket.service.enums.ETestTaskState;
import pt.ptinovacao.arqospocket.service.enums.ERunTestTaskState;
import pt.ptinovacao.arqospocket.service.jsonresult.TaskJsonResult;
import pt.ptinovacao.arqospocket.service.structs.MyLocation;

public interface ITaskResult {

	
	/**
	 * 
	 * Devolve o estado atual da task
	 * 
	 * @return enum que identifica o estado atual da task
	 */
	public ETestTaskState get_task_state();
	
	/**
	 * 
	 * Devolve o estado de execução da task
	 * 
	 * @return enum que identifica o estado de execução atual da task
	 */
	public ERunTestTaskState get_run_task_state();
	
	/**
	 * 
	 * Devolve o task id
	 * 
	 * @return task id
	 */
	public String get_task_id();
	
	/**
	 * 
	 * Devolve o nome da task
	 * 
	 * @return nome da task
	 */
	public String get_task_name();
	
	/**
	 * 
	 * Devolve o map com os resultados das task.
	 * A key é uma Label do resultado
	 * O Value é o resultado associado à label da Key
	 * 
	 * @return map com os resultados das task
	 */
	public Map<String, String> get_task_results(Context context);
	
	/**
	 * 
	 * Devolve a tecnologia em que a task correu (Mobile ou WiFi)
	 * 
	 * @return tecnologia em que a task correu
	 */
	public EConnectionTechnology get_task_technology();
	
	/**
	 * 
	 * Devolve as coordenadas de execução da task
	 * 
	 */
	public MyLocation get_test_execution_location();
	
	/**
	 * 
	 * Verifica se a atual taskjsonresult e igual a outra taskjsonresult
	 * 
	 */
	public boolean equals(TaskJsonResult taskStruct);
	
	/**
	 * 
	 * Verifica se a atual taskjsonresult é igual a uma determinada iTask
	 * 
	 */
	public boolean equals(ITask iTask);
}
