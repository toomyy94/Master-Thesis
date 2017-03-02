package pt.ptinovacao.arqospocket.service.store;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.logs.MyLogger;


public class FileIO {
	
	private final static Logger logger = LoggerFactory.getLogger(FileIO.class);
	
	private String fileName = null;
	
	
	public FileIO(String fileName) {
		this.fileName = fileName;
	}

	
	public void write_to_text_file(String data, Boolean append_data) {
		final String method = "write_to_text_file";
		
		BufferedWriter writer = null;
		
	    try {

	    	MyLogger.trace(logger, method, "IN");
	    	
	    	MyLogger.debug(logger, method, "data :"+data);
	    	MyLogger.debug(logger, method, "append_data :"+append_data);
	    	
	    	if (append_data)
	    		writer = new BufferedWriter(new FileWriter(fileName, true));
	    	else
	    		writer = new BufferedWriter(new FileWriter(fileName));
	    	
	    	writer.write(data);
	    	writer.close();
	    }
	    catch (Exception ex) {
	    	MyLogger.error(logger, method, ex);
	    } 
	}


	public String read_text_file() {
		final String method = "read_text_file";

		StringBuilder file_text = new StringBuilder();

	    try {
	    	
	    	MyLogger.trace(logger, method, "IN");
	    	
	    	BufferedReader reader = new BufferedReader(new FileReader(fileName));
	    	
	    	String line = null;
	    	while ((line = reader.readLine()) != null) {
	    		file_text.append(line);
	    	}
	    	
	    	reader.close();
	    	
	    	MyLogger.debug(logger, method, file_text.toString());
	    	
	    } catch (Exception ex) {
	    	MyLogger.error(logger, method, ex);
	    }

	    return file_text.toString();
	}
	
}
