package pt.ptinovacao.arqospocket.service.jsonparser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.logs.MyLogger;

public class MainTestStruct implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static Logger logger = LoggerFactory.getLogger(MainTestStruct.class);
	
	private String json_string;
	
	private int msg_type;
	private String macaddress;

	private List<TestStruct> test_list = null;
	
	
	public MainTestStruct(String json_string) {
		final String method = "MainTestStruct";
		
		try {
			
			MyLogger.debug(logger, method, "json_string :"+json_string);
			
			this.json_string = json_string;
			
			// faz parse da msg
			JSONObject mainJObject = new JSONObject(json_string);
			this.msg_type = mainJObject.getInt("msg_type");
			this.macaddress = mainJObject.getString("macaddress");
			
			MyLogger.debug(logger, method, "msg_type :"+msg_type);
			MyLogger.debug(logger, method, "macaddress :"+macaddress);
			
			
			// faz parse do teste 
			JSONArray probetest = mainJObject.getJSONArray("probetest");
			
			test_list = new ArrayList<TestStruct>();
			
			for (int i=0; i<probetest.length(); i++) {
				test_list.add(new TestStruct((JSONObject) probetest.get(i)));
			}

			Collections.sort(test_list);
			
			MyLogger.debug(logger, method, "test_list :"+test_list.toString());
			
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	public int get_msg_type() {
		return msg_type;
	}
	
	public String get_macaddress() {
		return macaddress;
	}
	
	public List<TestStruct> get_test_list() {
		return test_list;
	}

	public String toString() {
		final String method = "toString";
		
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("\nmsg_type :"+msg_type);
			sb.append("\nmacaddress :"+macaddress);
			
			if (test_list != null)
				sb.append("\ntest_list :"+test_list.toString());
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	}
}
