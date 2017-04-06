package pt.ptinovacao.arqospocket.service.store;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.logs.MyLogger;

public class CurrentConfiguration implements Serializable {

	private static final long serialVersionUID = 1L;
	private final static Logger logger = LoggerFactory.getLogger(CurrentConfiguration.class);
	
	private int start_page;
	private int send_technology;
	private int report_frequency;
	private int management_connection;

	public CurrentConfiguration() {
		final String method = "CurrentConfiguration";
		
		try {
			
			this.start_page = 3;
			this.send_technology = 0;
			this.report_frequency = 0;
			this.management_connection = 0;

		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	public CurrentConfiguration(int start_page, int send_technology, int report_frequency, int management_connection) {
		final String method = "CurrentConfiguration";
		
		try {
			
			logger.debug(method + "start_page :"+start_page);
			logger.debug(method + "send_technology :"+send_technology);
			logger.debug(method + "report_frequency :"+report_frequency);
			logger.debug(method + "management_connection :"+management_connection);

			this.start_page = start_page;
			this.send_technology = send_technology;
			this.report_frequency = report_frequency;
			this.management_connection = management_connection;

		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	public int get_start_page() {
		return start_page;
	}
	
	public int get_send_technology() {
		return send_technology;
	}
	
	public int get_report_frequency() {
		return report_frequency;
	}

	public int get_management_connection() {
		return management_connection;
	}

	public String toString() {
		final String method = "toString";
		
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("start_page :"+start_page);
			sb.append("send_technology :"+send_technology);
			sb.append("report_frequency :"+report_frequency);
			sb.append("management_connection :"+management_connection);

		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	}
}
