package pt.ptinovacao.arqospocket.service.interfaces;

import java.util.TreeMap;

import pt.ptinovacao.arqospocket.service.enums.EMobileNetworkMode;
import pt.ptinovacao.arqospocket.service.enums.ENetworkAction;
import pt.ptinovacao.arqospocket.service.enums.EMobileState;

public interface IUI {

	/**
	 * 
	 * Atualiza a informação da rede móvel
	 * 
	 * @param network_mode - modo de funcionamento da rede móvel (GPRS, EDGE, UMTS, etc)
	 * @param operator_code - código do operador
	 * @param rx_level - força de sinal
	 * @param cid - id de celula
	 * @param lac - código de localização da área
	 */
	public void update_mobile_information(EMobileState mobile_state, EMobileNetworkMode network_mode, String operator_code, String rx_level, String cid, String lac );
	
	/**
	 * 
	 * Atualiza a informação do Wifi
	 * 
	 * @param wifi_technology - norma utilizada (a,b,g,n,ac)
	 * @param ssid - nome da rede
	 * @param rx_level - força de sinal
	 * @param channel - canal
	 */
	public void update_wifi_information(String link_speed, String ssid, String rx_level, String channel);
	
	/**
	 * 
	 * Atualiza a informação/paramentros da rede móvel
	 * 
	 * @param keyValueParams - Key (Nome do paramentro), Value (valor a apresentar)
	 */
	public void update_mobile_params(TreeMap<String, String> keyValueParams);
	
	/**
	 * 
	 * Atualiza a informação/paramentros da Wifi
	 * 
	 * @param keyValueParams - Key (Nome do paramentro), Value (valor a apresentar)
	 */
	public void update_wifi_params(TreeMap<String, String> keyValueParams);
	
	/**
	 * 
	 * Atualiza a informação dos testes
	 * 
	 * @param test_info - Lista com a informação dos testes disponiveis
	 */
	public void update_test_info();
	
	/**
	 * 
	 * Atualiza a informação das tasks/tests
	 * 
	 * @param test_info - Lista com a informação dos testes disponiveis
	 */
	public void update_test_task(String test_id);
	
	/**
	 * 
	 * Callback informativo do pedido de envio de testes
	 * 
	 * @param action_state - estado da acção de envio de testes
	 */
	public void send_pending_tests_ack(ENetworkAction action_state);
	
	/**
	 * 
	 * Callback informativo do pedido de report de anomalias
	 * 
	 * @param action_state - estado da acção de report de anomalias
	 */
	public void send_report_ack(ENetworkAction action_state);
}
