package pt.ptinovacao.arqospocket.service.interfaces;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import pt.ptinovacao.arqospocket.service.enums.ERunTestTaskState;
import pt.ptinovacao.arqospocket.service.enums.ETestTaskState;
import pt.ptinovacao.arqospocket.service.enums.ETestType;
import pt.ptinovacao.arqospocket.service.structs.MyLocation;

public interface ITestResult extends Serializable {

	/**
	 * 
	 * Devolve o numero de testes concluidos
	 * 
	 * @return percentagem de conclusão do teste
	 */
	public int get_number_of_tests_done();
	
	/**
	 * 
	 * Devolve o numero de testes
	 * 
	 * @return percentagem de conclusão do teste
	 */
	public int get_number_of_tests();
	
	/**
	 * 
	 * Devolve o estado atual do test
	 * 
	 * @return enum que identifica o estado atual do test
	 */
	public ETestTaskState get_test_state();
	
	/**
	 * 
	 * Devolve o estado de execução do test
	 * 
	 * @return enum que identifica o estado de execução atual do test
	 */
	public ERunTestTaskState get_run_test_state();
	
	/**
	 * 
	 * Devolve o test id
	 * 
	 * @return test id
	 */
	public String get_test_id();
	
	/**
	 * 
	 * Devolve o nome do teste
	 * 
	 * @return nome do teste
	 */
	public String get_test_name();
	
	/**
	 * 
	 * Devolve o tipo de testes (agendado ou a pedido do utilizador)
	 * 
	 * @return tipo de testes
	 */
	public ETestType get_test_type();
	
	/**
	 * 
	 * Devolve a lista das tarefas do teste
	 * 
	 * @return lista das tarefas do teste
	 */
	public List<ITaskResult> get_task_list();
	
	/**
	 * 
	 * Devolve a lista das tarefas do teste a fazer
	 * 
	 * @return lista das tarefas do teste
	 */
	public List<ITask> get_task_list_to_do();
	
	
	/**
	 * 
	 * Indica se o teste já ou enviado para o sistema de gestão
	 * 
	 * @return true se já foi enviado, false se ainda não foi enviado.
	 */
	public boolean test_already_sent();
	
	/**
	 * 
	 * Devolve as coordenadas de execução do teste
	 * 
	 */
	public MyLocation get_test_execution_location();
	
	/**
	 * 
	 * Devolve a data de inicio de execução do teste
	 * 
	 */
	public Date get_date_init_execution();
	
	/**
	 * 
	 * Devolve a data de fim de execução do teste
	 * 
	 */
	public Date get_date_end_execution();
	
}
