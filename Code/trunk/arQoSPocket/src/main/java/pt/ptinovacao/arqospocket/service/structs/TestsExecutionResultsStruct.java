package pt.ptinovacao.arqospocket.service.structs;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.logs.MyLogger;
import pt.ptinovacao.arqospocket.service.jsonresult.TaskJsonResult;


/**
 * 
 * Objecto para contruir a msg para enviar para o servidor
 * 
 * @author Admin
 *
 */
public class TestsExecutionResultsStruct {

	private final static Logger logger = LoggerFactory.getLogger(TestsExecutionResultsStruct.class);
	
	private String serialnumber = null;
	private String macaddress = null;
	private String ipaddress = null;
	private String equipmenttype = null;
	private String timestamp = null;
	
	private List<TestExecutionStruct> associatedResponse = null;
	
	public TestsExecutionResultsStruct(String serialnumber, String macaddress, String ipaddress, String equipmenttype, String timestamp, List<TestExecutionStruct> associatedResponse) {
		final String method = "TestExecutionResultStruct";
		
		try {
			
			this.serialnumber = serialnumber;
			this.macaddress = macaddress;
			this.ipaddress = ipaddress;
			this.equipmenttype = equipmenttype;
			this.timestamp = timestamp;
			this.associatedResponse = associatedResponse;			
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	public String get_serialnumber() {
		return serialnumber;
	}
	
	public String get_macaddress() {
		return macaddress;
	}
	
	public String get_ipaddress() {
		return ipaddress;
	}
	
	public String get_equipmenttype() {
		return equipmenttype;
	}
	
	public String get_timestamp() {
		return timestamp;
	}
	
	public List<TestExecutionStruct> get_associatedResponse() {
		return associatedResponse;
	}
	
	private String format_date(Date date) {
		final String method = "format_date";
		
		try {
			
			return (date.getYear()+1900)+""+(date.getMonth()+1)+""+(date.getDate())+date.getHours()+date.getMinutes()+date.getSeconds();
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}
	
	public String toJson() {
		final String method = "toJson";
		
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("{");
			sb.append("\"probenotification\":{");
			sb.append("\"serialnumber\":"+serialnumber+",");
			sb.append("\"macaddress\":"+macaddress+",");
			sb.append("\"ipaddress\":"+ipaddress+",");
			sb.append("\"equipmenttype\":"+equipmenttype+",");
			sb.append("\"timestamp\":"+timestamp+",");
			sb.append("\"associatedResponse\":{");
			sb.append("\"probeResults\":[");
			
			for (TestExecutionStruct testExecutionStruct :associatedResponse) {
				sb.append("{");
				sb.append("\"modulo\":0,");
				sb.append("\"dataini\":"+format_date(testExecutionStruct.get_date_init_execution())+",");
				sb.append("\"datafim\":"+format_date(testExecutionStruct.get_date_end_execution())+",");
				sb.append("\"testeid\":"+testExecutionStruct.get_test_id()+",");
				sb.append("\"data\":[");
				
				List<TaskJsonResult> task_result_list = testExecutionStruct.get_task_result_list();
				
				for (TaskJsonResult taskJsonResult :task_result_list) {
					sb.append(taskJsonResult.buildTaskJsonResult());
					sb.append(",");
				}
				
				sb.deleteCharAt(sb.length()-1);
				
				sb.append("]");
				sb.append("},");
			}
			
			sb.deleteCharAt(sb.length()-1);
			
			sb.append("]");
			sb.append("}");
			sb.append("}");
			sb.append("}");
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	}
	
	public String toString() {
		final String method = "toString";
		
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("\nserialnumber :"+serialnumber);
			sb.append("\nmacaddress :"+macaddress);
			sb.append("\nipaddress :"+ipaddress);
			sb.append("\nequipmenttype :"+equipmenttype);
			sb.append("\ntimestamp :"+timestamp);
			sb.append("\nassociatedResponse :"+associatedResponse.toString());
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	}
}
