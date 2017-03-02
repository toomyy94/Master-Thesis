package pt.ptinovacao.arqospocket.service.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.jsonparser.TestStruct;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;
import pt.ptinovacao.arqospocket.service.interfaces.ITest;

public class AvailableTests {

	private final static Logger logger = LoggerFactory.getLogger(AvailableTests.class);
	
	private List<TestStruct> test_list = null;
	
	public AvailableTests() {
		final String method = "AvailableTests";
		
		try {
			
			test_list = new ArrayList<TestStruct>();
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	public AvailableTests(List<TestStruct> test_list) {
		final String method = "AvailableTests";
		
		try {
			
			this.test_list = test_list;
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	public TestStruct get_test_by_id(String test_id) {
		final String method = "get_test_by_id";
		
		try {
			
			for (TestStruct testStruct :test_list)
				if (testStruct.get_test_id().endsWith(test_id))
					return testStruct;
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}
	
	public List<ITest> get_available_test() {
		final String method = "get_available_test";
		
		List<ITest> resultList = null;
		
		try {
			
			resultList = new ArrayList<ITest>();
		
			for (TestStruct testStruct :test_list)
				resultList.add(testStruct);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return resultList;
	}
	
	public boolean add_test(TestStruct test) {
		final String method = "add_test";
		
		try {
			
			return test_list.add(test);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return false;
	}
	
	public boolean replace_all_test_list(List<TestStruct> test_list) {
		final String method = "add_test";
		
		try {
			
			this.test_list.clear();
			this.test_list = test_list;
			
			return true;
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return false;
	}
	
	public String toString() {
		final String method = "toString";
		
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("\ntest_list :"+test_list.toString());
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	}
}
