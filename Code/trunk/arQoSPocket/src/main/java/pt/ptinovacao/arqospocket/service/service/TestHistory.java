package pt.ptinovacao.arqospocket.service.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.interfaces.ITestResult;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;
import pt.ptinovacao.arqospocket.service.structs.TestExecutionStruct;

public class TestHistory implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static Logger logger = LoggerFactory.getLogger(TestHistory.class);
	
	private List<TestExecutionStruct> tests_list = null;
	
	public TestHistory() {
		final String method = "TestHistory";
		
		try {
			
			tests_list = new ArrayList<TestExecutionStruct>();
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	public boolean add_test_to_history(TestExecutionStruct testExecutionStruct) {
		final String method = "TestHistory";
		
		try {
			
			return tests_list.add(testExecutionStruct);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return false;
	}
	
	public List<ITestResult> get_test_history() {
		final String method = "TestHistory";
		
		try {
			
			List<ITestResult> iTestList = new ArrayList<ITestResult>();
			for (TestExecutionStruct testExecutionStruct :tests_list)
				iTestList.add(testExecutionStruct);
			
			return iTestList;
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}
	
	public void send_tests_result_to_server() {
		
	}
}
