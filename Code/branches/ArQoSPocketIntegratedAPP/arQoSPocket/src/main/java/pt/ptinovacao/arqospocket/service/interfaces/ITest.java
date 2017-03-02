package pt.ptinovacao.arqospocket.service.interfaces;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import pt.ptinovacao.arqospocket.service.enums.ETestType;

public interface ITest extends Serializable{

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
	 * Devolve a data de execução do teste (apenas valido para testes agendados)
	 * 
	 * @return tipo de testes
	 */
	public Date get_execution_date();
	
	
	/**
	 * 
	 * Devolve a lista de tasks para execução
	 * 
	 * @return tipo de testes
	 */
	public List<ITask> get_task_list();
}
