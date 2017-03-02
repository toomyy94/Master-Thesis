package pt.ptinovacao.arqospocket.service.tasks.wispr;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.logs.MyLogger;

public class WisprParser {
	
	private final static Logger logger = LoggerFactory.getLogger(WisprParser.class);
	
	public String FilterWisprXml(String content)
    {         
		final String method = "FilterWisprXml";
		
		String wisprXml = null;
		
		try {
			
			Pattern pattern = Pattern.compile("(<WISPAccessGatewayParam.*</WISPAccessGatewayParam>)");
			Matcher matcher = pattern.matcher(content);
			
			while (matcher.find()) {
				wisprXml = matcher.group(1);
			}
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}

        return wisprXml;
    }

	public String ParseRefField(String wisprXml)
    {
    	final String method = "ParseField";
    	
    	String fieldValue = null;
    	
    	try {
    		
    		
    		Pattern pattern = Pattern.compile("<A(.*)>(.*)</A>");
			Matcher matcher = pattern.matcher(wisprXml);
			
			while (matcher.find()) {
				fieldValue = matcher.group(2);
			}
    		
    	} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}


        return fieldValue;
    }
	
    public String ParseField(String wisprXml, String fieldName)
    {
    	final String method = "ParseField";
    	
    	String fieldValue = null;
    	
    	try {
    		
    		
    		Pattern pattern = Pattern.compile("<" + fieldName + ">(.*)</" + fieldName + ">");
			Matcher matcher = pattern.matcher(wisprXml);
			
			while (matcher.find()) {
				fieldValue = matcher.group(1);
			}
    		
    	} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}


        return fieldValue;
    }
}
