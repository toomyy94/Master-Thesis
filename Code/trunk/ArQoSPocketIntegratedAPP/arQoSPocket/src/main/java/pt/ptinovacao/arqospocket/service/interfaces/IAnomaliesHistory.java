package pt.ptinovacao.arqospocket.service.interfaces;

import java.util.Date;

import android.location.Location;

public interface IAnomaliesHistory {

	/**
	 * 
	 * Obtém o id do logo da anomalia.
	 * 
	 * @return o id do logo (este id será mapeado na UI por uma imagens dos resources)
	 */
	public String get_logo_id();
	
	/**
	 * 
	 * Obtém o id da anomalia
	 * 
	 * @return o id da anomalia (este id é o nome da anomalia que será único)
	 */
	public String get_anomalie_id();
	
	/**
	 * 
	 * Obtém o id do detalhe da anomalia
	 * 
	 * @return o id do detalhe da anomalia (este id é o nome do detalhe da anomalia que será único)
	 */
	public String get_anomalie_Details_id();
	
	/**
	 * 
	 * Devolve a data em que o report foi feito.
	 * 
	 * @return Data do report
	 */
	public Date get_anomalie_report_date();
	
	/**
	 * 
	 * Devolve a localização definida pelo utilizador no report
	 * 
	 * @return localização do report
	 */
	public Location get_location();
	
	/**
	 * 
	 * Devolve a mensagem reportada pelo utilizador
	 * 
	 * @return mensagem reportada pelo utilizador
	 */
	public String get_report_msg();
}
