package PT.PTInov.ArQoSPocketEDP.JSONConnector.Util;

public class TestModuleResponse {
	
	private String centralarea = null;
	private String technology = null;
	private String phonenumber = null;
	private String id = null;
	private String name = null;

	public TestModuleResponse(String centralarea, String technology, String phonenumber, String id, String name) {
		this.centralarea = centralarea;
		this.technology = technology;
		this.phonenumber = phonenumber;
		this.id = id;
		this.name = name;
	}

	public String getCentralarea() {
		return centralarea;
	}
	
	public String getTechnology() {
		return technology;
	}
	
	public String getPhonenumber() {
		return phonenumber;
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("centralarea :"+centralarea+"\n");
		sb.append("technology :"+technology+"\n");
		sb.append("phonenumber :"+phonenumber+"\n");
		sb.append("id :"+id+"\n");
		sb.append("name :"+name+"\n");
		
		return sb.toString();
	}
}
