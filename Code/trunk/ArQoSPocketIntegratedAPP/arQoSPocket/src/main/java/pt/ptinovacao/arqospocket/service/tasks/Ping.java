package pt.ptinovacao.arqospocket.service.tasks;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;
import pt.ptinovacao.arqospocket.service.tasks.results.PingResponse;
import pt.ptinovacao.arqospocket.service.tasks.results.PingResponse.ResponsePingEnum;

import android.content.Context;
import android.net.wifi.WifiManager;

public class Ping {
	
	private final static Logger logger = LoggerFactory.getLogger(Ping.class);
	
	private WifiManager wifi = null;
	private Context serviceContext = null;
	
	//private final String packet_size = "16";
	//private final String interval = "1";
	//private final String packet_number = "8";
	//private final String timeout = "10";
	//private final String ip_address = "8.8.8.8";
	
	public Ping(Context c) {
		final String method = "Ping";
		
		MyLogger.trace(logger, method, "IN");
		
		try {
			
			serviceContext = c;
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	public PingResponse DoPing(String packet_size, String interval, String packet_number, String timeout, String ip_address) {
		final String method = "DoPing";
		
		PingResponse response = new PingResponse();
		
		try {
			
			MyLogger.trace(logger, method, "IN");
			
			MyLogger.debug(logger, method, "packetSize :" + packet_size);
			MyLogger.debug(logger, method, "interval :" + interval);
			MyLogger.debug(logger, method, "packetsNr :" + packet_number);
			MyLogger.debug(logger, method, "timeout :" + timeout);
			MyLogger.debug(logger, method, "IP_addr :" + ip_address);
			
			String cmd = "ping -c "+packet_number+" -i "+interval+" -s "+packet_size+" -w "+timeout+" " + ip_address;
			MyLogger.debug(logger, method, "cmd to exec :" + cmd);
			
			Runtime runtime = Runtime.getRuntime();
			Process proc = runtime.exec(cmd); // other servers,
			
			proc.waitFor();
			int exit = proc.exitValue();

			BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
	        StringBuilder builder = new StringBuilder();
	        
	        Double min =-1.0, max = -1.0, sum = 0.0;
	        int count = 0;
	        
	        String packetTransmitted = serviceContext.getString(R.string.na);
			String packetReceived = serviceContext.getString(R.string.na);
			String percentPacketLost = serviceContext.getString(R.string.na);
			
			String line = null;
	        try {
	            while ((line = reader.readLine()) != null) {
	                builder.append(line);
	                builder.append("\n"); //appende a new line
	                
	                //Test if the line contains the three normal request  parameters (icmp_seq, ttl, time)
	                if (line.contains("icmp_seq") && line.contains("ttl") && line.contains("time")) {
	                	// parser the time
	                	try {
	                		
	                		Pattern pattern = Pattern.compile("(.+) bytes from (.+): icmp_seq=(.+) ttl=(.+) time=(.+) ms");
	            			Matcher matcher = pattern.matcher(line);
	            			
	            			if (matcher.find()) {
	            				
	            				String timeString = matcher.group(5);
	            				double time = Double.parseDouble(timeString);
	            				
	            				sum += time;
	            				count++;
	            				
	            				if (min == -1.0) {
	            					min = time;
	            					max = time;
	            				} else {
	            					if (time<min) {
	            						min = time;
	            					}
	            					if (time>max) {
	            						max = time;
	            					}
	            				}
	            				
	            			}
	                		
	                	} catch(Exception ex) {
	                		
	                	}
	                }
	                
	                // test if it is the line that have the final ping information
	                if (line.contains("packets transmitted") && line.contains("received") && line.contains("packet loss")) {
	                	// parser the time
	                	try {
	                		
	                		Pattern pattern = Pattern.compile("(.+) packets transmitted, (.+) received, (.+) packet loss, time (.+)ms");
	            			Matcher matcher = pattern.matcher(line);
	            			
	            			if (matcher.find()) {
	            				
	            				packetTransmitted = matcher.group(1);
	            				packetReceived = matcher.group(2);
	            				percentPacketLost = matcher.group(3);
	            				
	            			}
	                		
	                	} catch(Exception ex) {
	                		
	                	}
	                }
	                
	            }
	        } catch (Exception e) {
	        } 
	        
	        MyLogger.debug(logger, method, "max :" + max);
	        MyLogger.debug(logger, method, "min :" + min);
	        MyLogger.debug(logger, method, "media :" + sum/count);
	        MyLogger.debug(logger, method, "packetTransmitted :"+packetTransmitted);
	        MyLogger.debug(logger, method, "packetReceived :"+packetReceived);
	        MyLogger.debug(logger, method, "percentPacketLost :"+percentPacketLost);
	        
			
	        MyLogger.debug(logger, method, "result input :" + builder.toString());
	        MyLogger.debug(logger, method, "result error :" + proc.getErrorStream().toString());

	        ResponsePingEnum enumResponse = ResponsePingEnum.NA;
			if (exit == 0) { // normal exit
				MyLogger.debug(logger, method, "DoPing :: IP_addr :" + ip_address + " is reachable");
				enumResponse = ResponsePingEnum.OK;
			} else { // abnormal exit, so decide that the server is not
						// reachable
				MyLogger.debug(logger, method, "DoPing :: IP_addr :" + ip_address+ " is not reachable");
				enumResponse = ResponsePingEnum.NOCONNECTION;
			}
			
			response = new PingResponse(enumResponse, max+"", min+"", (sum/count)+"", packetTransmitted, packetReceived, percentPacketLost, builder.toString(), proc.getErrorStream().toString(), Integer.parseInt(packet_size));
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return response;
	}

}
