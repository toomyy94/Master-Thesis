package PT.PTInov.ArQoSPocketEDP.JSONConnector.Util;

public class RunServiceModuloInfo {
	
	private String phonenumber = null;
	private String technology = null;
	private String centralarea = null;
	private String name = null;
	private String id = null;
	
	public RunServiceModuloInfo(String phonenumber, String technology, String centralarea, String name, String id) {
		this.phonenumber = phonenumber;
		this.technology = technology;
		this.centralarea = centralarea;
		this.name = name;
		this.id = id;
	}
	
	public String getPhoneNumber() {
		return phonenumber;
	}
	
	public String getTechnology() {
		return technology;
	}
	
	public String getCentralArea() {
		return centralarea;
	}
	
	public String getName() {
		return name;
	}
	
	public String getId() {
		return id;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("phonenumber :"+phonenumber);
		sb.append("technology :"+technology);
		sb.append("centralarea :"+centralarea);
		sb.append("name :"+name);
		sb.append("id :"+id);
		
		return sb.toString();
	}
}
