package pt.ptinovacao.arqospocket.service.interfaces;

import android.location.Location;

import java.util.Date;

public interface IRadiologsHistory {

	/**
	 * 
	 * Obtém o id do logo do radiolog.
	 * 
	 * @return o id do logo (este id será mapeado na UI por uma imagens dos resources)
	 */
	public String get_logo();
	
	/**
	 * 
	 * Obtém o id do radiolog
	 * 
	 * @return o id do radiolog (este id é o nome da anomalia que será único)
	 */
	public String get_radiolog_id();
	
	/**
	 * 
	 * Obtém o id do detalhe do radiolog
	 * 
	 * @return o id do detalhe do radiolog (este id é o nome do detalhe do radiolog que será único)
	 */
	public String get_radiolog_Details_id();
	
	/**
	 * 
	 * Devolve a data em que o report foi feito.
	 * 
	 * @return Data do report
	 */
	public Date get_radiolog_report_date();
	
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
