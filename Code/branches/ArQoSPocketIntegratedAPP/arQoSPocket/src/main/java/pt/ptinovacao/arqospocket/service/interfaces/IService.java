package pt.ptinovacao.arqospocket.service.interfaces;

import java.util.List;
import java.util.TreeMap;

import pt.ptinovacao.arqospocket.service.enums.EEvent;
import pt.ptinovacao.arqospocket.service.store.CurrentConfiguration;

import android.graphics.Bitmap;
import android.location.Location;
import android.util.Pair;

import org.json.JSONObject;

public interface IService {
	
	/**
	 * 
	 * Devolve o estado da ligação movel
	 * 
	 * @return True - se a ligação movel estiver disponivel, False - se não estiver disponivel
	 * 
	 */
	public boolean isMobileAvailable();
	
	
	/**
	 * 
	 * Devolve o estado da ligação wifi
	 * 
	 * @return True - se a ligação movel estiver disponivel, False - se não estiver disponivel
	 * 
	 */
	public boolean isWiFiAvailable();
	
	/**
	 * 
	 * Devolve o estado de execução de testes
	 * 
	 * @return True - se estiver em execução, False - se não estiver em execução
	 */
	public boolean get_execution_state();
	
	/**
	 * 
	 * Corre um teste a pedido do utilizador
	 * 
	 * @return True - se for possivel correr o teste, False - se não for possivel correr o teste
	 */
	public Pair<Boolean, String> run_test(String test_id);

	/**
	 * 
	 * Devolve a informação de branding do operador
	 * NOTA: se a imagens for null, apresentar o texto.
	 * 
	 * @return Bitmap - imagem do operador, String - nome do operador
	 */
	public Pair<Bitmap, String> get_mobile_operator_branding();
	
	/**
	 * 
	 * Regista o pedido de callback para update da informação da rede móvel
	 * 
	 * @param ui_ref - Referencia para o callback à UI
	 */
	public boolean registry_update_mobile_information(IUI ui_ref);
	
	/**
	 * 
	 * Faz o pedido de callback para update da informação da rede móvel
	 * 
	 * @param ui_ref - Referencia para o callback à UI
	 */
	public void get_mobile_information(IUI ui_ref);
	
	/**
	 * 
	 * Remove o registo do pedido de callback para update da informação da rede móvel
	 * 
	 * @param ui_ref - Referencia para o callback à UI
	 */
	public boolean remove_registry_update_mobile_information(IUI ui_ref);
	
	/**
	 * 
	 * Devolve a informação de branding do operador
	 * NOTA: se a imagens for null, apresentar o texto.
	 * 
	 * @return Bitmap - imagem do operador, String - nome do operador
	 */
	public Pair<Bitmap, String> get_wifi_operator_branding();
	
	/**
	 * 
	 * Regista o pedido de callback para update da informação da rede Wifi
	 * 
	 * @param ui_ref - Referencia para o callback à UI
	 */
	public boolean registry_update_wifi_information(IUI ui_ref);
	
	/**
	 * 
	 * Faz o pedido de callback para update da informação da rede Wifi
	 * 
	 * @param ui_ref - Referencia para o callback à UI
	 */
	public void get_wifi_information(IUI ui_ref);
	
	/**
	 * 
	 * Remove o regista do pedido de callback para update da informação da rede Wifi
	 * 
	 * @param ui_ref - Referencia para o callback à UI
	 */
	public boolean remove_registry_update_wifi_information(IUI ui_ref);
	
	/**
	 * 
	 * Regista o pedido de callback para update dos parametros da rede móvel
	 * 
	 * @param ui_ref - Referencia para o callback à UI
	 */
	public boolean registry_update_mobile_params(IUI ui_ref);
	
	/**
	 * 
	 * Faz o pedido de callback para update dos parametros da rede móvel
	 * 
	 * @param ui_ref - Referencia para o callback à UI
	 */
	public void get_mobile_params(IUI ui_ref);
	
	/**
	 * 
	 * Faz o pedido de callback para update dos parametros da rede móvel
	 * 
	 * @param ui_ref - Referencia para o callback à UI
	 */
	public void get_wifi_params(IUI ui_ref);
	
	/**
	 * 
	 * Remove o registo do pedido de callback para update dos parametros da rede móvel
	 * 
	 * @param ui_ref - Referencia para o callback à UI
	 */
	public boolean remove_registry_update_mobile_params(IUI ui_ref);
	
	/**
	 * 
	 * Força o restart da interface móvel
	 * 
	 */
	public boolean restart_mobile_interface();
	
	/**
	 * 
	 * Regista o pedido de callback para update dos parametros da rede wifi
	 * 
	 * @param ui_ref - Referencia para o callback à UI
	 */
	public boolean registry_update_wifi_params(IUI ui_ref);
	
	/**
	 * 
	 * Remove o regista do pedido de callback para update dos parametros da rede wifi
	 * 
	 * @param ui_ref - Referencia para o callback à UI
	 */
	public boolean remove_registry_update_wifi_params(IUI ui_ref);
	
	/**
	 * 
	 * Força o restart da interface Wifi
	 * 
	 */
	public boolean restart_wifi_interface();
	
	/**
	 * 
	 * Devolve a lista de anomalias possiveis
	 * 
	 * @return Key (id para o icon dos resources), Value (Descrição/Nome da anomalia)
	 */
	public TreeMap<String, String> get_anomalies();
	
	/**
	 * 
	 * Devolve a lista de subcategorias das anomalias
	 * 
	 * @param anomaly - id/Nome da anomalia
	 * @return Key (id para o icon dos resources), Value (Descrição/Nome da anomalia)
	 */
	public TreeMap<String, String> get_anomalies_details(String anomaly);
	
	/**
	 * 
	 * Devolve a localização do dispositivo
	 * 
	 * @return localização
	 */
	public Location get_location();
	
	/**
	 * 
	 * regista/Envia um report
	 * 
	 * @param anomaly - id/nome da anomalia
	 * @param anomaly_details - id/Nome da subcategoria da anomalia
	 * @param location - localização da anomalia
	 * @param anomaly_report - descrição da anomalia (dada pelo utilizador)
	 * @param ui_ref - Referencia para o callback à UI
	 */
	public void send_report(String anomaly, String anomaly_details, Location location, String anomaly_report, IUI ui_ref);

	/**
	 *
	 * regista/Envia um report radio
	 *
	 * @param json - snapshot
	 * @param location - localização da anomalia
	 * @param ui_ref - Referencia para o callback à UI
	 */
	public void send_report_radio(String json, Location location, String report, IUI ui_ref);

	/**
	 * 
	 * Devolve a lista com o historico de anomalias
	 * 
	 * @return Lista com o historico de anomalias
	 */

	public List<IAnomaliesHistory> get_all_anomalies_history();

	/**
	 *
	 * Devolve a lista com o historico de radiologs
	 *
	 * @return Lista com o historico de radiologs
	 */
	public List<IRadiologsHistory> get_all_radiologs_history();
	
	/**
	 * 
	 * Devolve a lista com todos os testes disponiveis para execução
	 * 
	 * @return lista com todos os testes
	 */
	public List<ITest> get_all_available_tests();
	
	/**
	 * 
	 * Devolve a lista com todos os testes em execução
	 * 
	 * @return lista com todos os testes
	 */
	public List<ITestResult> get_all_tests_in_execution();
	
	/**
	 * 
	 * Regista o pedido de callback para update da informação dos testes
	 * 
	 * @param ui_ref - Referencia para o callback à UI
	 */
	public boolean registry_update_test_info(IUI ui_ref);
	
	/**
	 * 
	 * Remove o registo de pedido de callback para update da informação dos testes
	 * 
	 * @param ui_ref - Referencia para o callback à UI
	 */
	public boolean remove_registry_update_test_info(IUI ui_ref);
	
	/**
	 * 
	 * Devolve a lista com o historico dos testes
	 * 
	 * @return lista com o historico dos testes
	 */
	public List<ITestResult> get_tests_history();
	 
	/**
	 *  
	 *  Força o envio de resultados de testes que ainda não foram enviados para o sistema de gestão.
	 *  
	 * @param ui_ref - Referencia para o callback à UI
	 */
	public void send_pending_tests(IUI ui_ref);
	
	
	/**
	 * 
	 * Devolve o objecto com as configurações da aplicação
	 * 
	 * @return objecto com as configurações
	 */
	public CurrentConfiguration get_current_configuration();
	
	
	/**
	 * 
	 * Grava o objecto com as configurações da aplicação
	 * 
	 * @param configObject - objecto com as configurações
	 * @return true -  se as configurações gravadas com sucesso, False - se houve falha a gravar o objecto
	 */
	public boolean set_current_configuration(CurrentConfiguration configObject);

	/**
	 *
	 * Gera o objecto com as informações radio
	 *
	 *
	 * @return a json
	 */
    //TODO Radiologs only alowed w/ Event
	public void generate_radiolog();

	/**
	 *
	 * Gera o objecto com as informações radio
	 *
	 *
	 * @return a json
	 */
	public void generate_radiolog(EEvent type, String origin);

    /**
     *
     * Gera o objecto com as informações radio
     *
     *
     * @return a json
     */
    public JSONObject generate_radiolog(String user_feedback);

	/**
	 *
	 * Gera o objecto com as informações radio
	 *
	 *
	 * @return a json
	 */
	public void generate_scanlog();
	/**
	 *
	 * Gera int apartir do EEvent
	 *
	 *
	 * @return a integer
	 */
	public int get_EEventToInt(EEvent type);
	/**
	 *
	 * Gera String do nome do EEvent apartir do int fornecido
	 *
	 *
	 * @return a string
	 */
	public  String get_IntToEEvent(Integer type);
	 /*
	 * Gera String do nome do Status apartir do integer fornecido
	 *
	 *
	 * @return a string
	 */
	public String get_IntToStatus(Integer status);
	 /*
	 *
	 * Gera String do nome do Mode apartir do integer fornecido
	 *
	 *
	 * @return a string
	 */
	public String get_IntToMode(Integer mode);
	/*
	 *
	 * Gera String do nome do Roaming apartir do integer fornecido
	 *
	 *
	 * @return a string
	 */
	public String get_IntToRoaming(Integer roaming);
}
