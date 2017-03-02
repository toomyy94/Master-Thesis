package PT.PTInov.ArQoSPocket.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import PT.PTInov.ArQoSPocket.Enums.ResponseEnum;
import PT.PTInov.ArQoSPocket.Utils.LogType;
import PT.PTInov.ArQoSPocket.Utils.Logger;
import android.content.Context;
import android.net.wifi.WifiManager;

public class PingService {
	
	private final static String tag = "PingService";
	
	private WifiManager wifi = null;
	private Context serviceContext = null;
	
	private final String packetSize = "16";
	private final String interval = "1";
	private final String packetsNr = "8";
	private final String timeout = "10";
	private final String IP_addr = "8.8.8.8";
	
	public PingService(Context c) {
		Logger.v(tag, LogType.Trace,"PingService :: Creat a instance of ManageWiFiConnection");
		
		try {
			
			serviceContext = c;
			
		} catch(Exception ex) {
			Logger.v(tag, LogType.Error,"PingService :: Error :"+ex.toString());
		}
	}
	
	public PingResponse DoPing() {
		
		PingResponse response = new PingResponse();
		
		try {
			
			Logger.v(tag, LogType.Debug, "DoPing :: packetSize :" + packetSize);
			Logger.v(tag, LogType.Debug, "DoPing :: interval :" + interval);
			Logger.v(tag, LogType.Debug, "DoPing :: packetsNr :" + packetsNr);
			Logger.v(tag, LogType.Debug, "DoPing :: timeout :" + timeout);
			Logger.v(tag, LogType.Debug, "DoPing :: IP_addr :" + IP_addr);
			
			String cmd = "ping -c "+packetsNr+" -i "+interval+" -s "+packetSize+" -w "+timeout+" " + IP_addr;
			Logger.v(tag, LogType.Debug, "DoPing :: cmd to exec :" + cmd);
			
			Runtime runtime = Runtime.getRuntime();
			Process proc = runtime.exec(cmd); // other servers,
			
			proc.waitFor();
			int exit = proc.exitValue();

			BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
	        StringBuilder builder = new StringBuilder();
	        
	        Double min =-1.0, max = -1.0, sum = 0.0;
	        int count = 0;
	        
	        String packetTransmitted = "NA";
			String packetReceived = "NA";
			String percentPacketLost = "NA";
			
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
	        
	        Logger.v(tag, LogType.Debug, "DoPing :: max :" + max);
	        Logger.v(tag, LogType.Debug, "DoPing :: min :" + min);
	        Logger.v(tag, LogType.Debug, "DoPing :: media :" + sum/count);
	        Logger.v(tag, LogType.Debug, "DoPing :: packetTransmitted :"+packetTransmitted);
	        Logger.v(tag, LogType.Debug, "DoPing :: packetReceived :"+packetReceived);
	        Logger.v(tag, LogType.Debug, "DoPing :: percentPacketLost :"+percentPacketLost);
	        
			
	        Logger.v(tag, LogType.Debug, "DoPing :: result input :" + builder.toString());
	        Logger.v(tag, LogType.Debug, "DoPing :: result error :" + proc.getErrorStream().toString());

	        ResponseEnum enumResponse = ResponseEnum.NA;
			if (exit == 0) { // normal exit
				Logger.v(tag, LogType.Debug, "DoPing :: IP_addr :" + IP_addr + " is reachable");
				enumResponse = ResponseEnum.OK;
			} else { // abnormal exit, so decide that the server is not
						// reachable
				Logger.v(tag, LogType.Debug, "DoPing :: IP_addr :" + IP_addr
						+ " is not reachable");
				enumResponse = ResponseEnum.NOCONNECTION;
			}
			
			response = new PingResponse(enumResponse, max+"", min+"", (sum/count)+"", packetTransmitted, packetReceived, percentPacketLost, builder.toString(), proc.getErrorStream().toString(), Integer.parseInt(packetSize));
			
		} catch(Exception ex) {
			Logger.v(tag, LogType.Error,"DoPing :: Error :"+ex.toString());
		}
		
		return response;
	}

}
