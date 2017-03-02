package PTInov.IEX.ArQoSPocket.StaticValues;

public class TasksName {
	
	public static String getTaskNameByID(int taskId) {
		
		if (taskId<0 || taskId>tasksName.length) return null;
		
		return tasksName[taskId];
	}

	private static String[] tasksName = {
	    "Undefined",
	    "Making call",
	    "Answering call",
	    "Disconnecting call",
	    "Suspending active call",
	    "Recovering suspended call",
	    "Answering waiting call - Suspend active",
	    "Commuting between active and suspended call",
	    "Three party call setup",
	    "Rejecting call",
	    "Programming CallForwarding",
	    "Generic programmation",
	    "Sending SMS",
	    "Measuring level",
	    "Generate Frequency",
	    "Measuring Distortion",
	    "Measuring noise",
	    "Error rate",
	    "Troughput",
	    "Round Trip Delay",
	    "Registering IP on server",
	    "Asking for remote IP ID",
	    "Sending Fax",
	    "Receiving Fax",
	    "IP jitter - Source to destination",
	    "IP jitter - Destination to source",
	    "IP delay - Source to destination",
	    "IP delay - Destination to source",
	    "IP roudtrip delay",
	    "IP loss - Destination to source",
	    "IP loss - Source to destination",
	    "IP throughput - Source to destination",
	    "IP throughput - Destination to source",
	    "IP WAP access delay",
	    "IP FTP access delay",
	    "IP HTTP access delay",
	    "Receiving SMS",
	    "Initializing modem",
	    "ping",
	    "Dormmant",
	    "IP roudtrip delay jitter",
	    "IP roudtrip delay loss",
	    "Save to file",
	    "Send file",
	    "activate deactivate attach detach",
	    "Source delay",
	    "Destination delay",
	    "Source IP oneway parammeter",
	    "Destionation IP oneway parammeter",
	    "Source IP roundtrip parammeter",
	    "Destination IP roundtrip parammeter",
	    "Sending digit",
	    "PESQ tx",
	    "PESQ rx",
	    "Executing script",
	    "Source Error rate",
	    "Source throughput",
	    "Destination roundtrip delay",
	    "Radio parameters",
	    "Sending E-mail",
	    "Receiving E-mail",
	    "Sending MMS",
	    "Receiving MMS",
	    "USSD message",
	    "Source ISDN B1B2 delay", //PAFF
	    "Destination ISDN B1B2 delay", //PAFF
	    "Hops", //Bio
	    "Source Distortion",
	    "IP FTP Download",
	    "IP FTP Upload",
	    "Setup radio parameters",
	    "IP HTTP Upload",
	    "Select Remote SIM"
	};
}
