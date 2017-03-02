package PT.PTIN.ArQoSPocketPTWiFi.Utils;

import java.util.Date;

import PT.PTIN.ArQoSPocketPTWiFi.Enums.ActionState;

public class ActionResult {

	private final static String tag = "ActionResult";
	
	// Save the last Location update
	private String Latitude;
	private String Longitude;
	
	private ActionState geralActionState; 
	
	// ResultState
	private ActionState ConnectToPTWiFi;
	private ActionState AuthPTWiFi;
	private ActionState DoPing;
	private ActionState DoDownloadTest;
	private ActionState DoUploadTest;
	private ActionState SentResults;
	
	//ActionResult
	private ConnectWiFiState connectWiFiState = null;
	//private AssociationResultState associationResultState = null;
	private PTWiFiAuthResult ptWiFiAuthResult = null;
	private PingResponse pingResponse = null;
	private HttpServiceResponse HttpDownloadResponse = null;
	private HttpServiceResponse HttpUploadResponse = null;
	
	private Date testDate = null;
	
	public ActionResult() {
		
		ConnectToPTWiFi = ActionState.NOTOK;
		AuthPTWiFi = ActionState.NOTOK;
		DoPing  = ActionState.NOTOK;
		DoDownloadTest = ActionState.NOTOK;
		DoUploadTest = ActionState.NOTOK;
		SentResults = ActionState.NOTOK;
		
		geralActionState = ActionState.NOTOK;
		
		connectWiFiState = new ConnectWiFiState();
		ptWiFiAuthResult = new PTWiFiAuthResult();
		pingResponse = new PingResponse();
		HttpDownloadResponse = new HttpServiceResponse();
		HttpUploadResponse = new HttpServiceResponse();
		
		Latitude = "NA";
		Longitude = "NA";
		
		testDate = new Date();
	}
	
	public Date getTestDate() {
		return testDate;
	}
	
	public void setLocation(String pLatitude, String pLongitude) {
		Latitude = pLatitude;
		Longitude = pLongitude;
	}
	
	public String getLongitude() {
		return Longitude;
	}
	
	public String getLatitude() {
		return Latitude;
	}
	
	public void setHttpDownloadResponse(HttpServiceResponse pHttpDownloadResponse) {
		HttpDownloadResponse = pHttpDownloadResponse;
	}
	
	public HttpServiceResponse getHttpDownloadResponse() {
		return HttpDownloadResponse;
	}
	
	public void setHttpUploadResponse(HttpServiceResponse pHttpUploadResponse) {
		HttpUploadResponse = pHttpUploadResponse;
	}
	
	public HttpServiceResponse getHttpUploadResponse() {
		return HttpUploadResponse;
	}
	
	public void setConnectWiFiState(ConnectWiFiState pConnectWiFiState) {
		connectWiFiState = pConnectWiFiState;
	}
	
	public ConnectWiFiState getConnectWiFiState() {
		return connectWiFiState;
	}
	
	/*
	public void setAssociationResultState(AssociationResultState pAssociationResultState) {
		associationResultState = pAssociationResultState;
	}
	
	public AssociationResultState getAssociationResultState() {
		return associationResultState;
	}*/
	
	public void setPTWiFiAuthResult(PTWiFiAuthResult pPTWiFiAuthResult) {
		ptWiFiAuthResult = pPTWiFiAuthResult;
	}
	
	public PTWiFiAuthResult getPTWiFiAuthResult() {
		return ptWiFiAuthResult;
	}
	
	public void setPingResponse(PingResponse pPingResponse) {
		pingResponse = pPingResponse;
	}
	
	public PingResponse getPingResponse() {
		return pingResponse;
	}
	
	public void verifyGeneralState() {
		
		geralActionState = ActionState.OK;
		
		if (ConnectToPTWiFi != ActionState.OK)
			geralActionState = ActionState.NOTOK;
			
		if (AuthPTWiFi != ActionState.OK)
			geralActionState = ActionState.NOTOK;
		
		if (DoPing != ActionState.OK)
			geralActionState = ActionState.NOTOK;
		
		if (DoDownloadTest != ActionState.OK)
			geralActionState = ActionState.NOTOK;
		
		if (DoUploadTest != ActionState.OK)
			geralActionState = ActionState.NOTOK;
		
		if (SentResults != ActionState.OK)
			geralActionState = ActionState.NOTOK;
	}	
	
	private void setGeralActionStateDONE() {
		geralActionState = ActionState.OK;
	}
	
	public ActionState getGeralActionState() {
		return geralActionState;
	}
	
	public void setConnectToPTWiFiDONE() {
		ConnectToPTWiFi = ActionState.OK;
	}
	
	public ActionState getConnectToPTWiFiSTATE() {
		return ConnectToPTWiFi;
	}
	
	public void setAuthPTWiFiDONE() {
		AuthPTWiFi = ActionState.OK;
	}
	
	public ActionState getAuthPTWiFiSTATE() {
		return AuthPTWiFi;
	}
	
	public void setDoPingDONE() {
		DoPing = ActionState.OK;
	}
	
	public ActionState getDoPingSTATE() {
		return DoPing;
	}
	
	public void setDoDownloadTestDONE() {
		DoDownloadTest = ActionState.OK;
	}
	
	public ActionState getDoDownloadTestSTATE() {
		return DoDownloadTest;
	}
	
	public void setDoUploadTestDONE() {
		DoUploadTest = ActionState.OK;
	}
	
	public ActionState getDoUploadTestSTATE() {
		return DoUploadTest;
	}
	
	public void setSentResultsDONE() {
		SentResults = ActionState.OK;
	}
	
	public ActionState getSentResultsSTATE() {
		return SentResults;
	}
}
