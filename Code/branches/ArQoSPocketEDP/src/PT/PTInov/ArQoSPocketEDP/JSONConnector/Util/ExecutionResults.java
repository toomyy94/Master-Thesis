package PT.PTInov.ArQoSPocketEDP.JSONConnector.Util;

public class ExecutionResults {
	
	private String internacionalcode;
	private String value;
	private String taskID;
	private String taskModule;
	private String taskOrderNumber;
	private String idModule;
	private String name;
	
	public ExecutionResults(String internacionalcode, String value, String taskID, String taskModule, String taskOrderNumber, String idModule, String name) {
		this.internacionalcode = internacionalcode;
		this.value = value;
		this.taskID = taskID;
		this.taskModule = taskModule;
		this.taskOrderNumber = taskOrderNumber;
		this.idModule = idModule;
		this.name = name;
	}
	
	public String getinternacionalcode() {
		return internacionalcode;
	}
	
	public String getvalue() {
		return value;
	}
	
	public String gettaskID() {
		return taskID;
	}
	
	public String gettaskModule() {
		return taskModule;
	}
	
	public String gettaskOrderNumber() {
		return taskOrderNumber;
	}
	
	public String getidModule() {
		return idModule;
	}
	
	public String getname() {
		return name;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("internacionalcode :"+internacionalcode+"\n");
		sb.append("value :"+value+"\n");
		sb.append("taskID :"+taskID+"\n");
		sb.append("taskModule :"+taskModule+"\n");
		sb.append("taskOrderNumber :"+taskOrderNumber+"\n");
		sb.append("idModule :"+idModule+"\n");
		sb.append("name :"+name+"\n");
		
		return sb.toString();
	}
}
