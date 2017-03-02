package pt.ptinovacao.arqospocket.service.interfaces;

import pt.ptinovacao.arqospocket.service.jsonparser.TaskStruct;

public interface ITask {

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
	 * Devolve indice de execução das task
	 * 
	 * @return nome da task
	 */
	public String get_task_number();
	
	/**
	 * 
	 * Verifica se um task é igual à atual task
	 * 
	 * @return nome da task
	 */
	public boolean equals(TaskStruct taskStruct);
	
}
