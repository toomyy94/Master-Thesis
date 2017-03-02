package pt.ptinovacao.arqospocket.service.jsonparser.structs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class TestEventObject {
	
	private final static Logger logger = LoggerFactory.getLogger(TestEventObject.class);
	
	/*
	public TestEventObject(JSONObject jObject) {
		final String method = "TestEventObject";
		
		try {
			
			
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}*/

	public TestEventObject() {
		
	}
	
	public abstract Object clone();
}
